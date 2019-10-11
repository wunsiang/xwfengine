package com.oilpeddler.wfengine.schedulecomponent.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oilpeddler.wfengine.common.dataobject.BaseDO;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName(value="wf_element")
public class WfElementDO extends BaseDO {
    private static final long serialVersionUID=1L;

    /**
     * 元素编号
     */
    private String elementNo;

    /**
     * 元素所属流程主键
     */
    private String elementProcessId;

    /**
     * 元素实际发放令牌数量
     */
    private Integer tokenNumber;

    /**
     * 目前的作用是区分网关作用为分支(1)/聚合(2)
     */
    private Integer elementRole;
}
