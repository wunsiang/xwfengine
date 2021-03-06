package com.oilpeddler.wfengine.schedulecomponent.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oilpeddler.wfengine.schedulecomponent.dataobject.WfProcessParamsRecordDO;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wenxiang
 * @since 2019-10-23
 */
@Repository
public interface WfProcessParamsRecordMapper extends BaseMapper<WfProcessParamsRecordDO> {
    int updateParamsValue(WfProcessParamsRecordDO wfProcessParamsRecordDO);

}
