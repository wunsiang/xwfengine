package com.oilpeddler.wfengine.common.message;


import com.oilpeddler.wfengine.common.dataobject.ParmObject;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
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
     * 流程定义标识(外键)
     */
    private String pdId;

    private String piName;

    private String piStarter;

    private String piBusinesskey;

    /**
     * 第一个活动对应的必填项数据
     */
    Map<String, ParmObject> requiredData;

}
