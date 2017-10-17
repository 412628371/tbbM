package com.xinguang.tubobo.impl.merchant.service;

import com.hzmux.hzcms.common.utils.CalCulateUtil;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.api.AdminToMerchantService;
import com.xinguang.tubobo.api.dto.CarTypeDTO;
import com.xinguang.tubobo.impl.merchant.amap.RoutePlanning;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.disconf.Config;
import com.xinguang.tubobo.impl.merchant.dto.DeliveryFeeDto;
import com.xinguang.tubobo.impl.merchant.manager.MerchantDeliverFeeConfigServiceImpl;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.merchant.api.dto.MerchantDeliverFeeConfigDTO;
import com.xinguang.tubobo.merchant.api.dto.MerchantTypeDTO;
import com.xinguang.tubobo.merchant.api.enums.EnumBindStatusType;
import com.xinguang.tubobo.merchant.api.enums.EnumOrderType;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/4/14.
 */
@Service
public class DeliveryFeeService  {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryFeeService.class);
    @Autowired
    MerchantInfoService merchantInfoService;
    @Resource
    Config config;

    @Autowired
    RoutePlanning routePlanning;
    @Autowired
    MerchantDeliverFeeConfigServiceImpl merchantDeliverFeeConfigService;
    @Autowired
    AdminToMerchantService adminToMerchantService;
    @Autowired
    MerchantTypeService merchantTypeService;

/*  //TODO 1.3版本 待1.6稳定后删除
    public double sumDeliveryFeeByDistance(double distance) throws MerchantClientException {
        double distanceInKm = Math.ceil((distance/1000));
        double fee = config.getFirstLevelInitPrice();
        double firstLevelPricePerKM = config.getFirstLevelPricePerKM();
        double secondLevelDistance = config.getSecondLevelDistance();
        double secondLevelPricePerKM = config.getSecondLevelPricePerKM();
//        double initDistanceByMiles = config.getInitDistanceByMiles();
//        double pricePerKiloMiles = config.getPricePerKiloMiles();
        //TODO 配置,
        if (distanceInKm <= 1){
            return fee;
        }else {
//            distanceInKm-=1;
            if (distanceInKm <= secondLevelDistance){
                fee += (distanceInKm-1) * firstLevelPricePerKM;
            }else {
                fee+= (secondLevelDistance-1)*firstLevelPricePerKM;
                fee+= (distanceInKm-secondLevelDistance)*secondLevelPricePerKM;
            }

        }
//        double overDistanceFee = Math.ceil((distance/1000))*pricePerKiloMiles;
//        fee += overDistanceFee;
        return fee;
    }
*/

//    public HashMap<Double, Double>  sumDeliveryFeeByLocation(String userId,Double lat,Double lng,String goodsType) throws MerchantClientException {
//        double distance = sumDeliveryDistanceMerchant(userId,lat,lng);
//        HashMap<Double, Double> map = new HashMap<>();
//        map.put(distance,sumDeliveryFeeByDistance(distance));
//        return map;
//    }

    /**
     * 获取配送费
     * @param distance
     * @param merchantInfoEntity
     * @return
     * @throws MerchantClientException
     */
    public Double sumDeliveryFeeByLocation(Double distance, MerchantInfoEntity merchantInfoEntity) throws MerchantClientException {
        String orderType;
        MerchantTypeDTO merchantTypeDTO = null;
        double totalFee = 0.0;
        Long merTypeId =  merchantInfoEntity.getMerTypeId();
        if (merTypeId!=null){
            merchantTypeDTO = merchantTypeService.findById(merTypeId);
        }
        if (merchantTypeDTO==null){
            throw new MerchantClientException(EnumRespCode.MERCHANT_TYPEERROR);
        }
        if (EnumBindStatusType.SUCCESS.getValue().equals(merchantInfoEntity.getBindStatus())){
            orderType = EnumOrderType.POSTORDER.getValue();
        }else {
            orderType = EnumOrderType.SMALLORDER.getValue();
        }
        totalFee = sumDeliveryFeeByDistance(distance, merchantInfoEntity.getAddressAdCode(),orderType,merchantTypeDTO.getTemId());
        return totalFee;
    }
    public Double sumChepeiFee(String carType, Double distance) throws MerchantClientException {
        Double distanceByKm = Math.ceil(distance/1000);
        CarTypeDTO carTypeDTO ;
        carTypeDTO =  adminToMerchantService.queryCarTypeInfo(carType);
        if (carTypeDTO == null){
            throw new MerchantClientException(EnumRespCode.MERCHANT_CAR_TYPE_NOT_SUPPORT);
        }
        double startPrice = carTypeDTO.getStartPrice();
        double beyondPrice = carTypeDTO.getBeyondPrice();
        double startDistance = carTypeDTO.getStartDistance();
        double fee = startPrice;
        if (distanceByKm>startDistance){
            distanceByKm-=startDistance;
        }else {
            return fee;
        }


        double overDistanceFee = distanceByKm*beyondPrice;
        fee+=overDistanceFee;
        return fee;
    }
    public double sumDeliveryDistanceMerchant(String userId,Double lat,Double lng) throws MerchantClientException {
        MerchantInfoEntity entity = merchantInfoService.findByUserId(userId);
        if (null == entity)
            throw new MerchantClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
        if (lat==null || lng == null){
            throw new MerchantClientException(EnumRespCode.PARAMS_ERROR);
        }
        double distance = routePlanning.getDistanceWithWalkFirst(lng,lat,entity.getLongitude(),entity.getLatitude());
        if (distance > config.getMaxDeliveryMills()){
            throw new MerchantClientException(EnumRespCode.MERCHANT_DELIVERY_DISTANCE_TOO_FAR);
        }
        return distance;
    }
    public double sumDeliveryDistanceChePei(Double lng1,Double lat1,Double lng2,Double lat2) throws MerchantClientException {
        double distance = routePlanning.getDistanceWithWalkFirst(lng1,lat1,lng2,lat2);
        return distance;
    }
    /**
     * 计算配送时效
     * @param distance
     * @return
     */
    public Double sumDistributionLimitation(Double distance){
        try{
            String expression = config.getDistributionLimitationExpression();
            if (StringUtils.isBlank(expression)){
                //TODO
            }
            String[] es = expression.split(MerchantConstants.DEFAULT_SEPARATOR);
            ArrayList<Double> distanceList = new ArrayList(es.length);
            ArrayList<Double> minutesList = new ArrayList(es.length);
            for(String unitEp :es){
                String[] kvs = unitEp.split(":");
                distanceList.add(Double.valueOf(kvs[0]));
                minutesList.add(Double.valueOf(kvs[1]));
            }
            int index = -1;
            for (int i = 0;i< distanceList.size()-1;i++){
                if (distance<= distanceList.get(i)){
                    index = i;
                    break;
                }
            }
            if (index == -1){
                index = distanceList.size()-1;
            }
            Double minute = minutesList.get(index);
            return minute;
        }catch (Exception e){
            logger.error("计算配送时效错误，使用默认时间，60分钟");
            return 60.0;
        }

    }
    /**
     * 根据距离 计算阶梯配送费
     * @param distance 单位km
     * @return
     */
    public Double sumDeliveryFeeByDistance(Double distance, String areaCode,String orderType,Long temId) throws MerchantClientException {
        List<MerchantDeliverFeeConfigDTO> all = merchantDeliverFeeConfigService.findFeeByAreaCodeAndOrderTypeAndTemId(areaCode,orderType,temId);
        //double distanceInKm = Math.ceil((distance/1000));
        double distanceInKm = (distance/1000);

        double fee = 0.0;
        Double initFee=0.0;
        Double finalEndDistance=0.0;
        Double finalPerFee=0.0;
        if (null!=all||all.size()>0){
            for (MerchantDeliverFeeConfigDTO merchantDeliverFeeConfigDTO : all) {
               //起送费
                initFee=merchantDeliverFeeConfigDTO.getInitFee();
                Double beginDistance=merchantDeliverFeeConfigDTO.getBeginDistance();
                beginDistance=beginDistance==null?0.0:beginDistance;
                Double endDistance=merchantDeliverFeeConfigDTO.getEndDistance();
                endDistance=endDistance==null?0.0:endDistance;
                if(endDistance>finalEndDistance){
                    //获取数据库中最大结束距离及该部分单价
                    finalEndDistance=endDistance;
                    finalPerFee=merchantDeliverFeeConfigDTO.getPerFee();
                    finalPerFee=finalPerFee==null?0.0:finalPerFee;

                }
                if (distanceInKm>beginDistance&&distanceInKm>endDistance){
                    //传入距离大于开始距离并大于结束距离 计算该段全段费用
                    Double perFee = merchantDeliverFeeConfigDTO.getPerFee();
                    perFee=perFee==null?0.0:perFee;
                    Double mulFee=CalCulateUtil.mul(perFee,CalCulateUtil.sub(endDistance,beginDistance));
                    fee  = CalCulateUtil.add(fee, mulFee);
                }else if (distanceInKm>beginDistance&&distanceInKm<=endDistance){
                    //传入距离大于每条开始距离但是小于结束距离 计算该段超出部分费用
                    Double perFee = merchantDeliverFeeConfigDTO.getPerFee();
                    perFee=perFee==null?0.0:perFee;
                    Double moreDistance=CalCulateUtil.sub(distanceInKm,beginDistance);
                    moreDistance=Math.ceil(moreDistance);
                    Double mulFee=CalCulateUtil.mul(perFee,moreDistance);
                    fee  = CalCulateUtil.add(fee, mulFee);
                }
            }
            if (distanceInKm>finalEndDistance){
                //当传入长度大于数据库最大的结束长度时,需要额外加上该部分费用
                Double moreDistance=CalCulateUtil.sub(distanceInKm,finalEndDistance);
                moreDistance=Math.ceil(moreDistance);
                Double mulFee=CalCulateUtil.mul(finalPerFee,moreDistance);
                fee  = CalCulateUtil.add(fee, mulFee);
            }
                fee  = CalCulateUtil.add(fee, initFee);
        }else{
            //出现异常,后台数据中无数据时采用此套代码
           /* fee = 2.5;
            double firstLevelPricePerKM = 1.0;
            double secondLevelDistance = 3.0;
            double secondLevelPricePerKM = 2.0;
            if (distanceInKm <= 1){
                return fee;
            }else {
                if (distanceInKm <= secondLevelDistance){
                    fee += (distanceInKm-1) * firstLevelPricePerKM;
                }else {
                    fee+= (secondLevelDistance-1)*firstLevelPricePerKM;
                    fee+= (distanceInKm-secondLevelDistance)*secondLevelPricePerKM;
                }

            }*/
            logger.info("数据库无数据,计算配送费失效,请检查配送费阶梯价设置,现已采取后台默认配价");
            throw new MerchantClientException(EnumRespCode.CANT_FIND_DELIVERY_RULE);

        }
        return fee;

    }
}
