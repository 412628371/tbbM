package com.xinguang.tubobo.api.rider;

import com.xinguang.tubobo.api.rider.dto.RiderAuthDTO;
import com.xinguang.tubobo.api.rider.dto.RiderTaskDTO;
import com.xinguang.tubobo.api.rider.dto.RiderTaskStatusNotifyDTO;

/**
 * 骑手端提供给任务中心的dubbo接口
 */
public interface RiderToTaskCenterServiceInterface {

    /**
     * 查询骑士信息
     * @param riderId
     * @return
     */
    public RiderAuthDTO riderInfo(String riderId);

    /**
     * 骑手接单，新增任务
     * @param dto
     */
    public boolean riderGrabNewTask(RiderTaskDTO dto);

    /**
     * 任务状态改变回调
     * @param dto
     */
    public boolean riderTaskStatusNotify(RiderTaskStatusNotifyDTO dto);

}
