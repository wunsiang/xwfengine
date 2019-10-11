package com.oilpeddler.wfengine.common.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class WfActivtityInstanceDTO implements Serializable {
    private static final long serialVersionUID=1L;

    private String id;
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

    /**
     * 创建时间
     */

    private Date createtime;
    /**
     * 最后更新时间
     */
    private Date updatetime;

    private String piId;


}
