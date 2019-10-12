package com.oilpeddler.wfengine.schedulecomponent.tools;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class jexlUtilTest {

    @Test
    public void testJexl(){
        String conditionStr = "pass==true && a == \"高文翔\"";
        HashMap<String,Object> map = new HashMap<>();
        map.put("a","高文翔");
        map.put("pass",true);
        map.put("ahaha",12);
        Assert.assertTrue(JexlUtil.conditionIsMacth(conditionStr,map));
    }
}
