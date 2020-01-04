package com.oilpeddler.wfengine.schedulecomponent.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oilpeddler.wfengine.common.dataobject.BaseDO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@TableName(value="wf_task_history_instance")
public class WfTaskHistoryInstanceDO extends BaseDO {
    private static final long serialVersionUID=1L;

    /**
     * 运行时的任务实例主键
     */
    private String tiId;


    /**
     * 对应活动运行时主键
     */
    private String aiId;

    /**
     * 任务名称
     */
    private String tiName;

    /**
     * 任务状态0正在运行1已完成
     */
    private String tiStatus;

    /**
     * 任务关联表单外键
     */
    private String bfId;

    /**
     * 运行时任务开始时间
     */
    private Date tiCreatetime;

    /**
     * 运行时任务状态更新时间
     */
    private Date tiUpdatetime;

    /**
     * 运行时任务结束时间
     */
    private Date tiEndtime;
}
