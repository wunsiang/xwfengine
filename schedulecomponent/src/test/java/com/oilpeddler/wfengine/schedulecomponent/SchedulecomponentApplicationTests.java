package com.oilpeddler.wfengine.schedulecomponent;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oilpeddler.wfengine.common.api.scheduleservice.WfProcessDefinitionService;
import com.oilpeddler.wfengine.common.bo.WfProcessDefinitionBO;
import com.oilpeddler.wfengine.common.dto.WfProcessTemplateDTO;
import com.oilpeddler.wfengine.schedulecomponent.dao.TokenMapper;
import com.oilpeddler.wfengine.schedulecomponent.dao.redis.BpmnModelCacheDao;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.Token;
import com.oilpeddler.wfengine.schedulecomponent.service.WfProcessParamsRecordService;
import com.oilpeddler.wfengine.schedulecomponent.service.WfProcessTemplateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SchedulecomponentApplicationTests {
    @Autowired
    WfProcessTemplateService wfProcessTemplateService;

    @Autowired
    WfProcessDefinitionService wfProcessDefinitionService;

/*    @Reference
    WfProcessInstanceService wfProcessInstanceService;

    @Reference
    WfTaskInstanceService wfTaskInstanceService;*/

    @Autowired
    WfProcessParamsRecordService wfProcessParamsRecordService;

    @Test
    public void contextLoads() {
        mockDefAndStartProcess();
    }

    /**
     * 模拟第二步完成任务
     */
/*    private void mockCompleteTask(){
        //模拟业务controller收到用户提交处理表单同时提交的暗含在页面中的的已完成的任务标识和与该任务相关的必填项数据
        String taskId = "1212220806042537987";
        String dynamicAss = "e78dsdf78678sd,sdf89s7f89sdfsd89";
        Map<String,Object> requiredData = new HashMap<>();
        requiredData.put("businessdynamicassignee01",dynamicAss);
        //requiredData.put("businessday",6);
        //requiredData.put("businesspass",true);
        //requiredData.put("businessok",true);
        wfTaskInstanceService.completeTask(taskId,requiredData);
    }*/
    /**
     * 模拟第一步
     */
    private void mockDefAndStartProcess(){
        /**
         * 模拟流程图提交流程图并定义一个新的流程模板
         */
        WfProcessTemplateDTO wfProcessTemplateDTO = wfProcessTemplateService.selectByPtFilename("请假审批");
        WfProcessDefinitionBO wfProcessDefinitionBO = wfProcessDefinitionService.generatePDFromTemplateFile(wfProcessTemplateDTO);
        //wfProcessDefinitionBO.setBpmnModel(null);


        //wfProcessDefinitionService.setToCache(wfProcessDefinitionBO);

/*        *//**
         * 模拟开发人员配置业务表单参数名称和流程引擎参数映射关系,演示的时候直接在数据库里面配吧，直观又省事
         *//*
        *//**********************************************************************************************//*

        *//**
         * 模拟申请发起流程，向流程引擎传入1、想要发起的流程模板定义标识2、发起人的唯一标识
         * 3、该流程对应的业务主键4、流程实例名称（流程发起人指定或者自动拼接生成都行）
         *//*
        WfProcessInstanceStartDTO wfProcessInstanceStartDTO = new WfProcessInstanceStartDTO()
                .setPdId(wfProcessDefinitionBO.getId())
                .setPiName("processstarter001" + "流程定义1")
                .setPiStarter("processstarter001")
                .setPiBusinesskey("businesskey001");

        wfProcessInstanceService.startProcess(wfProcessInstanceStartDTO);*/
    }

    @Autowired
    TokenMapper tokenMapper;

    @Test
    @Transactional
    public void bf(){
        int r = tokenMapper.deleteById("1213400206196912130");
        QueryWrapper<Token> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("parent_id","1213398753059319810");
        int count = tokenMapper.selectCount(queryWrapper);
        System.out.println(count);
    }





}
