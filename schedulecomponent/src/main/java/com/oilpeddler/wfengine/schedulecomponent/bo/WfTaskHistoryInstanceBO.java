package com.oilpeddler.wfengine.schedulecomponent.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class WfTaskHistoryInstanceBO implements Serializable {
    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    private String id;
    /**
     * 创建时间
     */

    private Date createtime;
    /**
     * 最后更新时间
     */
    private Date updatetime;
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

    private String piId;

    /**
     * 流程定义标识(外键)
     */
    private String pdId;
}
