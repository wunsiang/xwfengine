package com.oilpeddler.wfengine.processmanagecomponent.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oilpeddler.wfengine.common.dataobject.BaseDO;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流程模板文件实体
 * </p>
 *
 * @author wenxiang
 * @since 2019-10-08
 */
@Data
@Accessors(chain = true)
@TableName(value="wf_process_template")
public class WfProcessTemplateDO extends BaseDO {
    private static final long serialVersionUID=1L;

    /**
     * 流程描述文件内容
     */
    private String ptContent;

    /**
     * 流程描述文件名称
     */
    private String ptFilename;

}

