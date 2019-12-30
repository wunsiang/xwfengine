package com.oilpeddler.wfengine.taskmanagecomponent.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oilpeddler.wfengine.common.dataobject.BaseDO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@TableName(value="wf_task_instance")
public class WfTaskInstanceDO extends BaseDO {
    private static final long serialVersionUID=1L;
    /**
     * 任务名称
     */
    private String tiName;

    /**
     * 任务参与者/执行人
     */
    private String tiAssigner;

    /**
     * 任务状态01正在运行02已完成03该次活动已结束04已移到历史库
     */
    private String tiStatus;

    /**
     * 任务关联表单外键
     */
    private String bfId;

    /**
     * 所属活动实例标识
     */
    private String aiId;

    /**
     * 任务结束时间
     */
    private Date endtime;

    private String piId;

    /**
     * 流程定义标识(外键)
     */
    private String pdId;


}
