package com.oilpeddler.wfengine.common.element;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 网关抽象基类
 * </p>
 *
 * @author wenxiang
 * @since 2019-09-22
 */
@Data
@Accessors(chain = true)
public abstract class Gateway extends BaseElement{

    /**
     * 网关名称
     */
    protected String name;
    /**
     * Todo wenxiang先放在这，后面可能会要，现在还没想好要不要这两元素，后面再决定
     */
    protected List<SequenceFlow> incomingFlows = new ArrayList<SequenceFlow>();
    protected List<SequenceFlow> outgoingFlows = new ArrayList<SequenceFlow>();
}
