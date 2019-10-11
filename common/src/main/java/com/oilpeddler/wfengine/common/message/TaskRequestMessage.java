package com.oilpeddler.wfengine.common.message;

import com.oilpeddler.wfengine.common.bo.WfActivtityInstanceBO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 请求任务管理器开启任务
 * </p>
 *
 * @author wenxiang
 * @since 2019-10-11
 */
@Data
@Accessors(chain = true)
public class TaskRequestMessage {
    public static final String TOPIC = "taskmanagecomponent";

    List<WfActivtityInstanceBO> wfActivtityInstanceBOList;
}
