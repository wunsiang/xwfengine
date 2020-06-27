package com.oilpeddler.wfengine.schedulecomponent.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oilpeddler.wfengine.common.dataobject.BaseDO;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流程定义实体
 * </p>
 *
 * @author wenxiang
 * @since 2019-10-23
 */
@Data
@Accessors(chain = true)
@TableName(value="wf_process_definition")
public class WfProcessDefinitionDO extends BaseDO {
    private static final long serialVersionUID=1L;
    /**
     * 流程定义名称(中文，从xml中抽取)
     */
    private String pdName;

    /**
     * 对应的流程资源文件no，即唯一标识，由画图软件自动生成，从xml中抽取
     */
    private String pdNo;

    /**
     * 流程模板主键
     */
    private String ptId;

    private String startForm;

}
