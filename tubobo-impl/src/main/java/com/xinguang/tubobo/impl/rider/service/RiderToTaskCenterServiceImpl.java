package com.xinguang.tubobo.impl.rider.service;

import com.hzmux.hzcms.common.config.Global;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.request.PayConfirmRequest;
import com.xinguang.tubobo.account.api.response.AccountInfo;
import com.xinguang.tubobo.account.api.response.PayInfo;
import com.xinguang.tubobo.account.api.response.TbbAccountResponse;
import com.xinguang.tubobo.api.enums.EnumAuthentication;
import com.xinguang.tubobo.api.enums.EnumRiderTaskStatus;
import com.xinguang.tubobo.api.rider.RiderToTaskCenterServiceInterface;
import com.xinguang.tubobo.api.rider.dto.RiderAuthDTO;
import com.xinguang.tubobo.api.rider.dto.RiderTaskDTO;
import com.xinguang.tubobo.api.rider.dto.RiderTaskStatusNotifyDTO;
import com.xinguang.tubobo.impl.rider.entity.RiderInfoEntity;
import com.xinguang.tubobo.impl.rider.entity.RiderTaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RiderToTaskCenterServiceImpl implements RiderToTaskCenterServiceInterface {

    protected Logger logger = LoggerFactory.getLogger(RiderToTaskCenterServiceImpl.class);

    @Autowired
    private RiderInfoService riderInfoService;
    @Autowired
    private RiderTaskService riderTaskService;
    @Autowired
    private TbbAccountService tbbAccountService;

    /**
     * 查询骑士信息
     * @param riderId
     * @return RiderAuthDTO.isRider() 判断是否为骑士
     */
    @Override
    public RiderAuthDTO riderInfo(String riderId) {
        RiderAuthDTO dto = new RiderAuthDTO();
        RiderInfoEntity rider = riderInfoService.findByUserId(riderId);
        if (rider != null){
            dto.setRiderId(rider.getUserId());
            dto.setRiderName(rider.getRealName());
            dto.setRiderPhone(rider.getPhone());
            if (EnumAuthentication.SUCCESS.getValue().equals(rider.getRiderStatus())){
                dto.setRider(true);

                TbbAccountResponse<AccountInfo> accountInfo = tbbAccountService.getAccountInfo(rider.getAccountId());
                if (null != accountInfo && accountInfo.isSucceeded() && null != accountInfo.getData()
                        && accountInfo.getData().getDeposit() > Long.valueOf(Global.getConfig("rider_deposit_base"))){
                    dto.setDepositEnough(true);
                }else {
                    dto.setDepositEnough(false);
                }
            }
        }
        return dto;
    }

    /**
     * 骑手接单，新增任务
     * @param dto
     */
    @Override
    public boolean riderGrabNewTask(RiderTaskDTO dto) {
        if (dto == null || StringUtils.isBlank(dto.getTaskNo())|| StringUtils.isBlank(dto.getRiderId())
                || null == dto.getTaskStatus() || null == dto.getPayId()) {
            logger.error("rider task save ERROR. task params error.",dto.getTaskNo());
            return false;
        }
        RiderTaskEntity entity = riderTaskService.findByTaskNo(dto.getTaskNo());
        if (entity != null){
            logger.error("rider task save ERROR. taskNo already exist. riderId:{}, taskNo:{}",dto.getRiderId(),dto.getTaskNo());
            return false;
        }
        RiderTaskEntity task = new RiderTaskEntity();
        BeanUtils.copyProperties(dto,task);
        riderTaskService.save(task);
        logger.info("rider task save SUCCESS. riderId:{}, taskNo:{}",dto.getRiderId(),dto.getTaskNo());
        return true;
    }

    /**
     * 任务状态改变回调
     * @param dto
     */
    @Override
    public boolean riderTaskStatusNotify(RiderTaskStatusNotifyDTO dto) {
        if (dto == null || dto.getTaskStatus() == null || StringUtils.isBlank(dto.getTaskNo())) return false;
        int i = riderTaskService.riderTaskStatusNotify(dto.getTaskNo(),dto.getTaskStatus(),dto.getAcceptTime(),dto.getPickTime(),dto.getDeliveryTime());
        if (i > 0){
            logger.info("notify update taskStatus SUCCESS. taskNo:{}, taskStatus:{}",dto.getTaskNo(),dto.getTaskStatus());

            //配送完成，支付骑手配送费
            if (null != dto.getDeliveryTime() && EnumRiderTaskStatus.FINISHED.getValue() == dto.getTaskStatus()){
                RiderTaskEntity task = riderTaskService.findByTaskNo(dto.getTaskNo());
                RiderInfoEntity riderInfo = riderInfoService.findByUserId(task.getRiderId());

                PayConfirmRequest request = PayConfirmRequest.getInstanceOfConfirm(task.getPayId(),riderInfo.getAccountId(),"rider task finish");
                TbbAccountResponse<PayInfo> response = tbbAccountService.payConfirm(request);
                if (null != response && response.isSucceeded()){
                    logger.info("rider task finish. payConfirm SUCCESS. taskNo:{}, riderId:{}, payId:{}, payAmount:{}",dto.getTaskNo(),task.getRiderId(),task.getPayId(),task.getPayAmount());
                }else {
                    logger.error("rider task finish. payConfirm ERROR. taskNo:{}, riderId:{}, payId:{}, payAmount:{}, errorCode:{}, errorMsg:{}",
                            dto.getTaskNo(),task.getRiderId(),task.getPayId(),task.getPayAmount(),response.getErrorCode(),response.getMessage());
                }
            }
            return true;
        }else {
            logger.error("notify update taskStatus ERROR. taskNo:{}, taskStatus:{}",dto.getTaskNo(),dto.getTaskStatus());
            return false;
        }
    }
}
