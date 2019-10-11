package com.oilpeddler.wfengine.common.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class WfProcessInstanceBO {
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
     * 流程实例名称(自动拼接用户名+模板名称)
     */
    private String piName;

    /**
     * 流程定义标识(外键)
     */
    private String pdId;

    /**
     * 流程执行状态0未开启01运行中02已完成
     */
    private String piStatus;

    /**
     * 流程发起人(具体用户ID)
     */
    private String piStarter;

    /**
     * 流程结束时间
     */
    private Date endtime;

    /**
     * 流程关联的业务数据主键
     */
    private String piBusinesskey;

}
