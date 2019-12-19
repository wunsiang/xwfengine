package com.oilpeddler.wfengine.schedulecomponent.dataobject;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.oilpeddler.wfengine.common.dataobject.BaseDO;
import com.oilpeddler.wfengine.schedulecomponent.element.Node;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 令牌对象，持久化类
 */
@Data
@Accessors(chain = true)
@TableName(value="wf_token")
public class Token extends BaseDO {
    private static final long serialVersionUID=1L;
    protected Date endtime;

    /**
     * 数据库用,当前持有令牌的节点的编号
     */
    protected String elementNo;
    /**
     * 当前持有令牌的节点
     */
    @TableField(exist = false)
    protected Node currentNode = null;

    /**
     * 父token，如果存在
     */
    @TableField(exist = false)
    protected Token parent = null;

    private String parentId;
    /**
     * 流程实例主键
     */
    protected String piId;

    /**
     * 所属流程的流程定义主键，用于取表单数据
     */
    protected String pdId;

    /**
     * 孩子
     */
    @TableField(exist = false)
    List<Token> children = new ArrayList<>();


    /**
     * 非UserTask节点不会使用signal信号驱动，是通过execute自动转移，而也只有UserTask节点需要调用getDefaultOutgoings()方法
     */
    public void signal(){
        if(currentNode == null || currentNode.getDefaultOutgoing() == null)
            return;
        currentNode.leave(this);
    }


}
