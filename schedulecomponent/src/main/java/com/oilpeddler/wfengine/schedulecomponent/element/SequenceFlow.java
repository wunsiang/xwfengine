package com.oilpeddler.wfengine.schedulecomponent.element;

import com.oilpeddler.wfengine.common.bo.WfProcessParamsRecordBO;
import com.oilpeddler.wfengine.common.constant.ParamType;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.Token;
import com.oilpeddler.wfengine.schedulecomponent.service.WfProcessParamsRecordService;
import com.oilpeddler.wfengine.schedulecomponent.tools.JexlUtil;
import com.oilpeddler.wfengine.schedulecomponent.tools.SpringUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private ApplicationContext applicationContext =  SpringUtil.getApplicationContext();

    WfProcessParamsRecordService wfProcessParamsRecordService = applicationContext.getBean(WfProcessParamsRecordService.class);

    private static final long serialVersionUID = 1L;

    /**
     * 顺序流名称
     */
    protected String name;

    /**
     * 顺序流条件表达式
     */
    protected String conditionExpression = null;

    /**
     * 顺序流起始节点
     */
    protected String sourceRef;

    /**
     * 顺序流目标节点
     */
    protected String targetRef;

    protected BaseElement from = null;
    protected BaseElement to = null;
    protected List<DataParam> paramList;

    public void take(Token token){
        //如sf不带有条件表达式，则无脑往目标走
        if(conditionExpression == null){
            //在连接线上没有令牌住所，佩特里网transition不持有令牌
            token.setCurrentNode(null);
            ((Node)to).enter(token);
        }else if(conditionExpression != null && conditionExpression.length() > 0){
            //TODO 如有条件表达式，则开始条件判断，符合条件才能通过，否则丢弃令牌，条件判断逻辑同排他网关handler部分
            Map<String,Object> requiredData = new HashMap<>();
            for(DataParam dataParam : paramList){
                WfProcessParamsRecordBO wfProcessParamsRecordBO = wfProcessParamsRecordService.getByEnginePpName(dataParam.getEnginePpName(),token.getPiId(),token.getPdId(),dataParam.getTaskNo());
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
            if(JexlUtil.conditionIsMacth(conditionExpression,requiredData)){
                token.setCurrentNode(null);
                token.setElementNo(no);
                ((Node)to).enter(token);
            }
        }
    }
}
