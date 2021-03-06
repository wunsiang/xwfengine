package com.oilpeddler.wfengine.schedulecomponent.element;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * <p>
 * 并行网关元素类——流程描述类组成要素
 * </p>
 *
 * @author wenxiang
 * @since 2019-09-22
 */
@Data
@Accessors(chain = true)
public class ParallelGateway extends Gateway {
    //分支和聚合网关之间的关联
    //protected ParallelGateway relatedGateWay;
/*    protected String relatedGateWay;*/

}
