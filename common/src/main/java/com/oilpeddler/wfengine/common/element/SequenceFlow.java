package com.oilpeddler.wfengine.common.element;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 顺序流类——流程描述类组成要素
 * </p>
 *
 * @author wenxiang
 * @since 2019-09-22
 */
@Data
@Accessors(chain = true)
public class SequenceFlow extends BaseElement implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 顺序流名称
     */
    protected String name;

    /**
     * 顺序流条件表达式
     */
    protected String conditionExpression;

    /**
     * 顺序流起始节点
     */
    protected String sourceRef;

    /**
     * 顺序流目标节点
     */
    protected String targetRef;


    protected List<DataParam> paramList;
}
