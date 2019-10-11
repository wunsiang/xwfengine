package com.oilpeddler.wfengine.processmanagecomponent.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oilpeddler.wfengine.common.dataobject.BaseDO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
@Data
@Accessors(chain = true)
@TableName(value="wf_process_instance")
public class WfProcessInstanceDO extends BaseDO {
    private static final long serialVersionUID=1L;

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
