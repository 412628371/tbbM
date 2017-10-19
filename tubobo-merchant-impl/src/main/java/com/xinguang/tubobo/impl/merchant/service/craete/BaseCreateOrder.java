/*
package com.xinguang.tubobo.impl.merchant.service.craete;

*/
/**
 * Created by yanxu on 2017/10/13.
 *//*


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

*/
/**
 * 抢单/接单基类.
 *//*

public abstract class BaseCreateService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    protected OrderGrabHelper orderGrabHelper;
    @Autowired
    private TaskOrderService taskOrderService;





    public void doAccept(String taskNo, String riderId, String reqId,RequestTaskOperatePB.RequestTaskOperate request){
        TaskNotifyProtoParam taskNotifyProtoParam = new TaskNotifyProtoParam(taskNo,riderId,reqId);
        TaskOrderEntity taskOrderEntity = taskOrderService.getByTaskNo(taskNo);
        // 1.校验任务状态
        TbbResultCodePB.TbbResultCode taskCode = judgeTaskStatus(taskOrderEntity,riderId);
        if (taskCode != TbbResultCodePB.TbbResultCode.TBB_SUCCESS){
            recoverAcceptableSet(taskNo);
            commonOperateService.notifyTaskOperateResult(taskCode, EnumTaskCallbackType.GRAB,taskNotifyProtoParam,taskOrderEntity);
            return;
        }
        //检查骑手信息
        RiderAuthDTO riderAuthDTO = riderToTaskCenterServiceInterface.riderInfo(riderId);
        taskCode = judgeRiderStatus(taskNo,riderId,riderAuthDTO);
        if (taskCode != TbbResultCodePB.TbbResultCode.TBB_SUCCESS){
            recoverAcceptableSet(taskNo);
            commonOperateService.notifyTaskOperateResult(taskCode, EnumTaskCallbackType.GRAB,taskNotifyProtoParam,taskOrderEntity);
            return;
        }
        //检查骑手和任务的关系
        taskCode = judgeTaskAndRider(taskOrderEntity.getProviderId(),riderAuthDTO.getProviderId());
        if (taskCode != TbbResultCodePB.TbbResultCode.TBB_SUCCESS){
            recoverAcceptableSet(taskNo);
            commonOperateService.notifyTaskOperateResult(taskCode, EnumTaskCallbackType.GRAB,taskNotifyProtoParam,taskOrderEntity);
            return;
        }

        Date now = new Date();
        double pickupDistance = getPickDistance(request.getLongitude(),request.getLatitude(),taskOrderEntity.getSenderLongitude(),
                taskOrderEntity.getSenderLatitude());

        Date expectPickTime = getExpectPickTime(now,pickupDistance);
        Date expectFinishTime = getExpectFinishTime(now,taskOrderEntity.getDeliveryDistance());
        taskNotifyProtoParam.setExpectFinishTime(expectFinishTime);
        boolean result = updateOrderStatusAndRiderInfo(taskNo,riderId,riderAuthDTO.getRiderPhone(),riderAuthDTO.getRiderName(),
                now,pickupDistance,expectFinishTime,expectPickTime);

        if (result){
            //7.发送消息 保存order的取货时地理信息
            TaskLocationRecordParam taskLocationRecordParam= new TaskLocationRecordParam(request.getLatitude(),request.getLongitude(),now,taskNo,0.0);
            taskLocationRecordParam.setTaskType(taskOrderEntity.getTaskType());
            notifyTaskOrderLocationRecord(taskLocationRecordParam,EnumTaskCallbackType.GRAB);
            logger.info("取货成功，taskNo: {},userId: ",taskNo,riderId);

            notifyGrab(taskOrderEntity,riderId,riderAuthDTO.getRiderPhone(),riderAuthDTO.getRiderName(),now,pickupDistance,expectFinishTime,expectPickTime);
            if (isCrowdTask()){
                orderGrabHelper.acceptOneTask(riderId);
                geoService.delete(Constants.GEO_REGISTER_TYPE_TASK,taskNo);
            }
            taskCode = TbbResultCodePB.TbbResultCode.TBB_SUCCESS;
        }else {
            taskCode = TbbResultCodePB.TbbResultCode.TBB_FAILURE;
            logger.info("骑手抢单，更新数据失败。taskNo: {}, riderId:{} ",taskNo,riderId);
        }
        //8.长连接通知骑手
        commonOperateService.notifyTaskOperateResult(taskCode,EnumTaskCallbackType.GRAB,taskNotifyProtoParam,taskOrderEntity);
    }

    */
/**
     * 是否是众包任务
     * @return
     *//*

    protected abstract boolean isCrowdTask();

    protected abstract double getPickDistance(Double lng1, Double lat1, Double lng2, Double lat2);
    protected abstract Date getExpectPickTime(Date date,double pickDistance);
    protected abstract Date getExpectFinishTime(Date date,double deliveryDistance);
    protected abstract boolean updateOrderStatusAndRiderInfo(String taskNo,String riderId,String riderPhone,String riderName,Date now,
                                                             double pickupDistance,Date expectFinishTime,Date expectPickTime);
    */
/**
     * 判断定制化的骑手条件
     * @param riderAuthDTO
     * @return
     *//*

    protected abstract TbbResultCodePB.TbbResultCode judgeRiderStatusSpecial(RiderAuthDTO riderAuthDTO);

    */
/**
     * 判断骑手状态
     * @param taskNo
     * @param riderId
     * @param riderAuthDTO
     * @return
     *//*

    private TbbResultCodePB.TbbResultCode judgeRiderStatus(String taskNo,String riderId,RiderAuthDTO riderAuthDTO){
        if (riderAuthDTO == null ){
            logger.info("骑手抢单失败，未找到骑手。taskNo: {}, riderId:{} ",taskNo,riderId);
            return TbbResultCodePB.TbbResultCode.TBB_NO_USER;
        }
        logger.info("骑手接单：骑手信息：{}",riderAuthDTO.toString());
        if (orderGrabHelper.isRiderForbiddenWhenCancel(riderId)){
            logger.info("骑手抢单失败，骑手因为取消订单被禁止接单。taskNo: {}, riderId:{} ",taskNo,riderId);
            return TbbResultCodePB.TbbResultCode.TBB_RIDER_FORBIDDEN_AFTER_CANCEL;
        }
        if (!EnumRiderAuthentication.SUCCESS.getValue().equals(riderAuthDTO.getRiderStatus())){
            if (EnumRiderAuthentication.FROZEN.getValue().equals(riderAuthDTO.getRiderStatus())){
                logger.info("骑手抢单失败，骑手为冻结状态。taskNo: {}, riderId:{} ",taskNo,riderId);
                return TbbResultCodePB.TbbResultCode.TBB_FROZEN;
            }else if (EnumRiderAuthentication.FAIL.getValue().equals(riderAuthDTO.getRiderStatus())){
                logger.info("骑手抢单失败，骑手认证失败。taskNo: {}, riderId:{} ",taskNo,riderId);
                return TbbResultCodePB.TbbResultCode.TBB_VERIFY_FAIL;
            }else if (EnumRiderAuthentication.APPLY.getValue().equals(riderAuthDTO.getRiderStatus())){
                logger.info("骑手抢单失败，骑手审核中。taskNo: {}, riderId:{} ",taskNo,riderId);
                return TbbResultCodePB.TbbResultCode.TBB_VERIFYING;
            }else {
                logger.info("骑手抢单失败，骑手未认证。taskNo: {}, riderId:{} ",taskNo,riderId);
                return TbbResultCodePB.TbbResultCode.TBB_UNAUTHED;
            }
        }
        if (!riderAuthDTO.isAccountCreated()){
            logger.info("骑手抢单失败，骑手没有资金账户。taskNo: {}, riderId:{} ",taskNo,riderId);
            return TbbResultCodePB.TbbResultCode.TBB_NO_FUND_ACCOUNT;
        }
        return judgeRiderStatusSpecial(riderAuthDTO);
    }

    */
/**
     * 判断任务状态
     * @param taskOrderEntity
     * @param riderId
     * @return
     *//*

    protected  TbbResultCodePB.TbbResultCode judgeTaskStatus(TaskOrderEntity taskOrderEntity,String riderId){
        String taskNo = taskOrderEntity.getTaskNo();
        if (isCrowdTask()){
            //先判断任务是否可抢
            if (!orderGrabHelper.verify(taskNo)){
                logger.info("骑手抢单失败，任务单不可抢。taskNo: {}, riderId:{} ",taskNo,riderId);
                return TbbResultCodePB.TbbResultCode.TBB_TASK_UN_GETABLE;
            }
        }
        if (null == taskOrderEntity){
            return TbbResultCodePB.TbbResultCode.TBB_TASK_UN_GETABLE;
        }
        if (taskOrderEntity.getTaskStatus() == TaskStatusEnum.ACCEPTED.getValue()||
                taskOrderEntity.getTaskStatus() == TaskStatusEnum.DELIVERYING.getValue())
            return TbbResultCodePB.TbbResultCode.TBB_TASK_ALREADY_ACCEPTED;
        //判断任务在数据库中的状态
        if (taskOrderEntity.getTaskStatus() != TaskStatusEnum.WAITING_ACCEPTED.getValue())
            return TbbResultCodePB.TbbResultCode.TBB_TASK_UN_GETABLE;
        //众包订单，判断是否达到每个骑手可抢最大单数
        if (isCrowdTask()){
            if (orderGrabHelper.isMaxTasks(riderId)){
                return TbbResultCodePB.TbbResultCode.TBB_TASK_TOO_MUCH;
            }
        }
        return TbbResultCodePB.TbbResultCode.TBB_SUCCESS;
    }

    */
/**
     * 判断骑手和任务的关系，如果满足，返回success
     * @return
     *//*

    protected abstract TbbResultCodePB.TbbResultCode judgeTaskAndRider(Long taskProviderId,Long riderProviderId);

    */
/**
     *将订单重新加入可抢单集合
     * @param taskNo
     *//*

    private void recoverAcceptableSet(String taskNo){
        if (isCrowdTask())
            orderGrabHelper.put(taskNo);
    }

    private void notifyGrab(TaskOrderEntity taskOrderEntity,String riderId,String riderPhone,String riderName,
                            Date grabTime,Double pickupDistance,Date expectFinishTime,Date expectAcceptTime){
        String taskNo = taskOrderEntity.getTaskNo();
        logger.info("骑手抢单成功。taskNo: {}, riderId:{} ",taskNo,riderId);
        RiderTaskDTO riderTaskDTO = new RiderTaskDTO();
        taskOrderEntity.setRiderId(riderId);
        copyValuesToDto(taskOrderEntity,riderTaskDTO);
        riderTaskDTO.setTaskNo(taskNo);
        riderTaskDTO.setAcceptTime(grabTime);
        riderTaskDTO.setTaskStatus(TaskStatusEnum.DELIVERYING.getValue());//驿站订单，接单后状态直接为配送中
        if (isCrowdTask()){
            riderTaskDTO.setTaskStatus(TaskStatusEnum.ACCEPTED.getValue());
        }
        if (!isCrowdTask()){
            //驿站订单设置取货时间
            riderTaskDTO.setPickTime(grabTime);
        }
        riderTaskDTO.setPayId(taskOrderEntity.getPayId());
        riderTaskDTO.setPickupDistance(pickupDistance);
        riderTaskDTO.setExpectFinishTime(expectFinishTime);
        riderTaskDTO.setExpectAcceptTime(expectAcceptTime);
        riderTaskDTO.setSenderId(taskOrderEntity.getSenderId());
        riderTaskDTO.setWeight(taskOrderEntity.getWeight());
        riderTaskDTO.setShipment(taskOrderEntity.getShipment());
        rabbitmqService.notifyRiderGrab(riderTaskDTO);

        logger.info("通知骑手端，抢单成功，riderTaskDTO：{},taskNo:{},payId:{}",riderTaskDTO.toString(),taskNo,taskOrderEntity.getPayId());
        notifySenderGrabRabbitmq(taskOrderEntity,grabTime,riderId,riderPhone,riderName,pickupDistance,expectFinishTime);
//        //骑手抢单成功的dubbo回调和mq回调会在骑手端产生两条数据，目前只保留mq回调
//        //        riderToTaskCenterServiceInterface.riderGrabNewTask(riderTaskDTO);
//        if(TaskTypeEnum.LEG_ORDER.getValue().equals(taskOrderEntity.getTaskType())){
//            //兔快送订单
//            legToTaskCenterService.riderGrabOrder(new LegGrabCallbackDTO(riderId,riderName,
//                    riderPhone,taskNo,grabTime,pickupDistance,expectFinishTime,riderCarNo,riderCarType));
//            logger.info("通知兔快送微信端订单："+taskNo + " 已被接单");
//
//        }else {
//            //众包订单 避免grab超时 采用dubbo调用
//            merchantToTaskCenterServiceInterface.riderGrabOrder(new MerchantGrabCallbackDTO(riderId,riderName,
//                    riderPhone,taskNo,grabTime,pickupDistance,expectFinishTime,riderCarNo,riderCarType));
//            logger.info("通知商家订单："+taskNo + " 已被接单");
//        }
    }
    public abstract void notifySenderGrabRabbitmq(TaskOrderEntity taskOrderEntity,Date date,String riderId,String riderPhone,
                                                  String riderName,double pickupDistance,Date expectFinishTime);

    private void copyValuesToDto(TaskOrderEntity taskOrderEntity,RiderTaskDTO riderTaskDTO){
        BeanUtils.copyProperties(taskOrderEntity,riderTaskDTO);
        riderTaskDTO.setSenderAddressDetail(ConvertUtil.handleNullString(taskOrderEntity.getSenderAddressDetail())+
                ConvertUtil.handleNullString(taskOrderEntity.getSenderAddressRoomNo()));
        riderTaskDTO.setReceiverAddressDetail(ConvertUtil.handleNullString(taskOrderEntity.getReceiverAddressDetail())+
                ConvertUtil.handleNullString(taskOrderEntity.getReceiverAddressRoomNo()));
    }
    private void notifyTaskOrderLocationRecord(TaskLocationRecordParam taskLocationRecordParam,
                                               EnumTaskCallbackType enumTaskCallbackType){
        if (null==taskLocationRecordParam){return ;}
        TaskOrderLocationRecordEntity entity = new TaskOrderLocationRecordEntity();
        String taskNo = taskLocationRecordParam.getTaskNo();
        //封装取货信息
        entity.setAcceptDate(taskLocationRecordParam.getDate());
        entity.setAcceptLatitude(taskLocationRecordParam.getLatitude());
        entity.setAcceptLongitude(taskLocationRecordParam.getLongitude());
        entity.setOrderNo(taskNo);
        entity.setPickLocationOffsetMiles(0.0);
        entity.setEnumTaskCallbackType(enumTaskCallbackType);
        entity.setPickExpiredIllegalFlag(false);
        entity.setPickLocationIllegalFlag(false);
        rabbitmqService.notifyTaskOrderLocationRecord(entity);
        logger.info("异步通知task端保存骑手取货或完成时间地理信息：{}",entity.toString());
    }
}
*/
