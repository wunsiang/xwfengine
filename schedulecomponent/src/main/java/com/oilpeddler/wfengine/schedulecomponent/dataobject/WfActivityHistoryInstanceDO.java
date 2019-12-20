package com.oilpeddler.wfengine.schedulecomponent.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oilpeddler.wfengine.common.dataobject.BaseDO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@TableName(value="wf_activity_history_instance")
public class WfActivityHistoryInstanceDO extends BaseDO {
    private static final long serialVersionUID=1L;

    private String actId;


    /**
     * 运行时记录名称
     */
    private String aiName;

    /**
     * 运行时执行人类型
     */
    private String aiAssignerType;

    /**
     * 运行时执行人标识
     */
    private String aiAssignerId;

    /**
     * 即席活动执行人
     *//*
    private String aiDynamicAssignerId;

    *//**
     * 即席活动执行人种类（0个人1职位）
     *//*
    private String aiDynamicAssignerType;*/


    /**
     * 挂接业务表单主键
     */
    private String bfId;

    /**
     * 运行时记录状态
     */
    private String aiStatus;

    /**
     * 活动开始时间
     */
    private Date aiCreatetime;

    /**
     * 活动更新时间
     */
    private Date aiUpdatetime;

    /**
     * xml活动编号
     */
    private String usertaskNo;

    /**
     * 活动类型(会签、可回退、普通等等)
     */
    private String aiCategory;

    private String piId;

    /**
     * 流程定义标识(外键)
     */
    private String pdId;

    /**
     * 当前活动未完成实例个数
     */
    private int activeTiNum;
}
