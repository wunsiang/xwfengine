package approvalsystem.controller;

import approvalsystem.dto.LeaveInfoDTO;
import approvalsystem.dto.UserInfoDTO;
import approvalsystem.service.LeaveInfoService;
import approvalsystem.service.UserInfoService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oilpeddler.wfengine.common.api.formservice.ClientProcessService;
import com.oilpeddler.wfengine.common.api.formservice.ClientTaskService;
import com.oilpeddler.wfengine.common.bo.WfProcessDefinitionBO;
import com.oilpeddler.wfengine.common.bo.WfProcessInstanceBO;
import com.oilpeddler.wfengine.common.bo.WfTaskInstanceBO;
import com.oilpeddler.wfengine.common.constant.ParamType;
import com.oilpeddler.wfengine.common.constant.ProcessInstanceState;
import com.oilpeddler.wfengine.common.dataobject.ParmObject;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private LeaveInfoService leaveInfoService;

    @Reference
    private ClientProcessService clientProcessService;

    @Reference
    private ClientTaskService clientTaskService;


    @ResponseBody
    @PostMapping("/users/completeApproval")
    public String completeApproval(@RequestParam("pdId") String pdId,@RequestParam("tiId") String tiId,@RequestParam("usertaskNo") String usertaskNo,@RequestParam("businessjudge") Boolean businessjudge,@RequestParam("uiId") String uiId,@RequestParam("piId") String piId) {
        Map<String, Object> requiredData = new HashMap<String, Object>();
        requiredData.put("businessjudge",businessjudge);
        if(uiId.equals("5") && businessjudge)
            clientProcessService.changeProcessState(piId, ProcessInstanceState.PROCESS_INSTANCE_STATE_PASS);
        if(uiId.equals("5") && !businessjudge)
            clientProcessService.changeProcessState(piId, ProcessInstanceState.PROCESS_INSTANCE_STATE_FAIL);
        clientTaskService.completeTask(tiId,pdId,usertaskNo,requiredData);
        return "1";
    }

    @ResponseBody
    @PostMapping("/users/queryUnObtainList")
    public String queryUnObtainList(@RequestParam("uiId") String uiId){
        UserInfoDTO userInfoDTO = userInfoService.queryUser(uiId);

        List<WfTaskInstanceBO> wfTaskInstanceBOList = clientTaskService.selectUnObtainTask(userInfoDTO.getGiId());
        JSONArray wfdArray = JSON.parseArray(JSONObject.toJSONString(wfTaskInstanceBOList));
        return wfdArray.toJSONString();
    }

    @ResponseBody
    @PostMapping("/users/queryProcessList")
    public String queryProcessList(@RequestParam("uiId") String uiId){
        List<WfProcessInstanceBO> wfProcessInstanceBOList = clientProcessService.getProcessListByUserId(uiId);
        JSONArray wfdArray = JSON.parseArray(JSONObject.toJSONString(wfProcessInstanceBOList));
        return wfdArray.toJSONString();
    }

    @ResponseBody
    @PostMapping("/users/obtainTask")
    public String obtainTask(@RequestParam("taskId") String taskId,@RequestParam("uiId") String uiId) {
        clientTaskService.obtainTask(taskId,uiId);
        return "1";
    }

    @ResponseBody
    @PostMapping("/users/loginValidate")
    public String loginValidate(@RequestParam("uiName") String uiName) {
        UserInfoDTO userInfoDTO = userInfoService.validateUser(uiName);
        System.out.println(uiName);
        return JSON.toJSONString(userInfoDTO);
    }

    @ResponseBody
    @PostMapping("/users/addLeave")
    public String addLeave(@RequestParam("uiId") String uiId,@RequestParam("durations") String durations,@RequestParam("pdId") String pdId) {
        LeaveInfoDTO leaveInfoDTO = new LeaveInfoDTO().setDurations(Integer.parseInt(durations))
                .setUiId(uiId);
        leaveInfoDTO = leaveInfoService.addLeave(leaveInfoDTO);
        UserInfoDTO userInfoDTO = userInfoService.queryUser(uiId);
        Map<String, ParmObject> requiredData = new HashMap<>();
        ParmObject parmObject = new ParmObject()
                .setPpType(ParamType.PARAM_TYPE_INT)
                .setVal(Integer.parseInt(durations));
        requiredData.put("durations",parmObject);
        clientProcessService.startProcess(pdId,userInfoDTO.getUiName() + "请假申请",userInfoDTO.getId(),leaveInfoDTO.getId(),requiredData);
        return "1";
    }

    @ResponseBody
    @PostMapping("/users/personinfo")
    public String personinfo(@RequestParam("pdId") String pdId,@RequestParam("tiId") String tiId,@RequestParam("usertaskNo") String usertaskNo,@RequestParam("businessdynamicassignee01") String businessdynamicassignee01) {
        Map<String, Object> requiredData = new HashMap<>();
        requiredData.put("businessdynamicassignee01",businessdynamicassignee01);
        clientTaskService.completeTask(tiId,pdId,usertaskNo,requiredData);
        return "1";
    }

    @ResponseBody
    @PostMapping("/users/completedRead")
    public String completedRead(@RequestParam("pdId") String pdId,@RequestParam("tiId") String tiId,@RequestParam("usertaskNo") String usertaskNo) {
        clientTaskService.completeTask(tiId,pdId,usertaskNo,new HashMap<String, Object>());
        return "1";
    }

    @ResponseBody
    @PostMapping("/users/queryApprovalList")
    public String queryApprovalList(@RequestParam("uiId") String uiId,@RequestParam("uiName") String uiName,@RequestParam("tenantId") String tenantId){
        List<WfProcessDefinitionBO> wfProcessDefinitionBOList = clientProcessService.queryDefinitionList();
        JSONArray wfdArray = JSON.parseArray(JSONObject.toJSONString(wfProcessDefinitionBOList));
        return wfdArray.toJSONString();
    }

    @ResponseBody
    @PostMapping("/users/queryUnTaskList")
    public String queryUnTaskList(@RequestParam("uiId") String uiId){
        List<WfTaskInstanceBO> wfTaskInstanceBOList = clientTaskService.selectUnCompletedTask(uiId,"0");
        JSONArray wfdArray = JSON.parseArray(JSONObject.toJSONString(wfTaskInstanceBOList));
        return wfdArray.toJSONString();
    }

    @ResponseBody
    @PostMapping("/users/queryLeaveInfo")
    public String queryLeaveInfo(@RequestParam("piBusinesskey") String piBusinesskey) {
        LeaveInfoDTO leaveInfoDTO = leaveInfoService.selectById(piBusinesskey);
        return JSON.toJSONString(leaveInfoDTO);
    }
}
