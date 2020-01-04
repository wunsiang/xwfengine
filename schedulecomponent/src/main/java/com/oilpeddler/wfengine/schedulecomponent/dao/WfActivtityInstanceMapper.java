package com.oilpeddler.wfengine.schedulecomponent.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.WfActivtityInstanceDO;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wenxiang
 * @since 2019-10-09
 */
@Repository
public interface WfActivtityInstanceMapper extends BaseMapper<WfActivtityInstanceDO> {
    int decrementActiveNum(String id);
}