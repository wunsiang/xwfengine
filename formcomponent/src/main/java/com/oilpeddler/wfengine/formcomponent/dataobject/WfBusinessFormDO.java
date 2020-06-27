package com.oilpeddler.wfengine.formcomponent.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oilpeddler.wfengine.common.dataobject.BaseDO;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName(value="wf_business_form")
public class WfBusinessFormDO extends BaseDO {
    private static final long serialVersionUID=1L;

    private String formName;

    private String formUrl;

}
