package com.oilpeddler.wfengine.common.element;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 活动要素类——流程描述类组成要素
 * </p>
 *
 * @author wenxiang
 * @since 2019-09-22
 */
@Data
@Accessors(chain = true)
public class UserTask extends BaseElement implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 活动名称
     */
    protected String name;

    /**
     * 活动参与人类型
     */
    protected String assigneeType;

    /**
     * 活动执行人
     */
    protected List<String> assignees;

    /**
     * 活动类型
     */
    protected String taskType;

    /**
     * 挂接表单页面
     */
    protected String pageKey;

    protected List<DataParam> paramList;


    protected List<SequenceFlow> incomingFlows = new ArrayList<SequenceFlow>();
    protected List<SequenceFlow> outgoingFlows = new ArrayList<SequenceFlow>();
}
