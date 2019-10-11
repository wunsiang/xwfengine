package com.oilpeddler.wfengine.processmanagecomponent.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oilpeddler.wfengine.common.dataobject.BaseDO;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName(value="wf_process_params_relation")
public class WfProcessParamsRelationDO extends BaseDO {
    private static final long serialVersionUID=1L;

    /**
     * 画流程人员指定的必填项名称
     */
    private String ppName;

    /**
     * 该参数为流程参数、活动参数、任务参数(备用)
     */
    private String ppLevel;

    /**
     * 数据类型
     */
    private String ppType;

    /**
     * 对应的流程id
     */
    private String pdId;

    /**
     * 与所属流程的活动(环节)相关联，但并不是关联的表中的id，而是流程描述xml中的id
     */
    private String taskNo;

    /**
     * 流程引擎中读取该参数时使用的名称(备用)
     */
    private String enginePpName;

    private String businessName;
}
