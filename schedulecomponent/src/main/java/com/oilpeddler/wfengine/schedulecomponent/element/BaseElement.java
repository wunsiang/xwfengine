package com.oilpeddler.wfengine.schedulecomponent.element;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 要素抽象基类
 * </p>
 *
 * @author wenxiang
 * @since 2019-09-22
 */
@Data
@Accessors(chain = true)
public abstract class BaseElement {
    /**
     * 编号，流程描述文件中的要素类唯一标识
     */
    protected String no;


}