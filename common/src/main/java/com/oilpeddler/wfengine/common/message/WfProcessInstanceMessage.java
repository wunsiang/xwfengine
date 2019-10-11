package com.oilpeddler.wfengine.common.message;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * <p>
 * 流程实例mq用调度request message对象
 * </p>
 *
 * @author wenxiang
 * @since 2019-10-08
 */
@Data
@Accessors(chain = true)
public class WfProcessInstanceMessage {
    //TODO 消息队列中传递的对象需要实现Serializable吗？？？
    /**
     * 主键
     */
    private String id;

    /**
     * 流程定义标识(外键)
     */
    private String pdId;

}
