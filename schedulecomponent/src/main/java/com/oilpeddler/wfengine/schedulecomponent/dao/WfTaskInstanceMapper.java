package com.oilpeddler.wfengine.schedulecomponent.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.WfTaskInstanceDO;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wenxiang
 * @since 2019-10-08
 */
@Repository
public interface WfTaskInstanceMapper extends BaseMapper<WfTaskInstanceDO> {
    void updateAssignerType(String id,String ti_assigner);
}
