package com.oilpeddler.wfengine.schedulecomponent.dao;

import com.oilpeddler.wfengine.schedulecomponent.dataobject.WfProcessTemplateDO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TemplateResourceMapperTest {

    @Autowired
    private WfProcessTemplateMapper wfProcessTemplateMapper;
    @Test
    public void testSelect() {
        /*System.out.println(("----- selectAll method test ------"));
        List<WfProcessTemplateDO> wfProcessTemplateDOList = wfProcessTemplateMapper.selectList(null);
        Assert.assertEquals(1, wfProcessTemplateDOList.size());
        wfProcessTemplateDOList.forEach(System.out::println);*/
    }
}
