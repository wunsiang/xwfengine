package com.oilpeddler.wfengine.schedulecomponent.element;

import com.oilpeddler.wfengine.common.constant.ParamType;
import com.oilpeddler.wfengine.schedulecomponent.bo.WfProcessParamsRecordBO;
import com.oilpeddler.wfengine.schedulecomponent.dao.TokenMapper;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.Token;
import com.oilpeddler.wfengine.schedulecomponent.service.WfProcessParamsRecordService;
import com.oilpeddler.wfengine.schedulecomponent.tools.JexlUtil;
import com.oilpeddler.wfengine.schedulecomponent.tools.SpringUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
    //private ApplicationContext applicationContext =  SpringUtil.getApplicationContext();
    //WfProcessParamsRecordService wfProcessParamsRecordService = applicationContext.getBean(WfProcessParamsRecordService.class);

    //WfProcessParamsRecordService wfProcessParamsRecordService = SpringUtil.getBean(WfProcessParamsRecordService.class);
    //ExclusiveGateway的业务逻辑不同于其他类型的网关
    @Override
    public void execute(Token token){
        /*TODO 这款先暂时还是遍历sf，找第一个符合条件的转移路线，后续想改成在ExclusiveGateway加一个表达式属性，然后直接返回转移sf的no
        这样ExclusiveGateway相连的sf就可以不写判断表达式，减少判断次数，也能比较好的控制排他性*/
        for(SequenceFlow sequenceFlow : outgoingFlows){
            if(sequenceFlow.getConditionExpression() == null){
                //在连接线上没有令牌住所，佩特里网transition不持有令牌
                token.setCurrentNode(null);
                ((Node)sequenceFlow.getTo()).enter(token);
            }else if(sequenceFlow.getConditionExpression() != null && sequenceFlow.getConditionExpression().length() > 0){
                //TODO 如有条件表达式，则开始条件判断，符合条件才能通过，否则丢弃令牌，条件判断逻辑同排他网关handler部分
                Map<String,Object> requiredData = new HashMap<>();
                for(DataParam dataParam : sequenceFlow.getParamList()){
                    WfProcessParamsRecordBO wfProcessParamsRecordBO = SpringUtil.getBean(WfProcessParamsRecordService.class).getByEnginePpName(dataParam.getEnginePpName(),token.getPiId(),token.getPdId(),dataParam.getTaskNo());
                    if(wfProcessParamsRecordBO != null){
                        switch (wfProcessParamsRecordBO.getPpType()){
                            case ParamType.PARAM_TYPE_BOOL:
                                requiredData.put(dataParam.getEnginePpName(),wfProcessParamsRecordBO.getPpRecordValue().equals("1") ? true : false);
                                break;
                            case ParamType.PARAM_TYPE_INT:
                                requiredData.put(dataParam.getEnginePpName(),Integer.parseInt(wfProcessParamsRecordBO.getPpRecordValue()));
                                break;
                            case ParamType.PARAM_TYPE_FLOAT:
                                requiredData.put(dataParam.getEnginePpName(),Float.parseFloat(wfProcessParamsRecordBO.getPpRecordValue()));
                                break;
                            case ParamType.PARAM_TYPE_STRING:
                                requiredData.put(dataParam.getEnginePpName(),wfProcessParamsRecordBO.getPpRecordValue());
                                break;
                        }
                    }
                }
                if(JexlUtil.conditionIsMacth(sequenceFlow.getConditionExpression(),requiredData)){
                    token.setCurrentNode(null);
                    token.setElementNo(no);
                    ((Node)sequenceFlow.getTo()).enter(token);
                    break;
                }
            }
        }
    }
}
