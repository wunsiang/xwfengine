package com.oilpeddler.wfengine.common.element;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 排他网关元素类——流程描述类组成要素
 * </p>
 *
 * @author wenxiang
 * @since 2019-09-22
 */
@Data
@Accessors(chain = true)
public class ExclusiveGateway extends Gateway implements Serializable {
    private static final long serialVersionUID = 1L;
}
