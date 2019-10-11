package com.oilpeddler.wfengine.schedulecomponent.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oilpeddler.wfengine.common.dataobject.BaseDO;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName(value="wf_activtity_instance")
public class WfActivtityInstanceDO extends BaseDO {
    private static final long serialVersionUID=1L;

    /**
     * 活动名称（当前环节业务描述）
     */
    private String aiName;

    /**
     * 活动状态(0运行中1已完成)
     */
    private String aiStatus;

    /**
     * 活动参与者，可以是外键
     */
    private String aiAssignerId;

    /**
     * 活动参与者种类（0个人1职位）
     */
    private String aiAssignerType;

    /**
     * 活动关联表单外键
     */
    private String bfId;


    /**
     * xml活动编号
     */
    private String usertaskNo;

    /**
     * 活动类型(会签、可回退、普通等等)
     */
    private String aiCategory;

    private String piId;
}
