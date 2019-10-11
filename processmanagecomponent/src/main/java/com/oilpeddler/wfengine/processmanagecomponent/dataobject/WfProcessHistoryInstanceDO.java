package com.oilpeddler.wfengine.processmanagecomponent.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oilpeddler.wfengine.common.dataobject.BaseDO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author wenxiang
 * @since 2019-10-08
 */
@Data
@Accessors(chain = true)
@TableName(value="wf_process_history_instance")
public class WfProcessHistoryInstanceDO extends BaseDO {
    private static final long serialVersionUID=1L;

    private String piId;

    private String piName;

    private String pdId;

    /**
     * 当前记录动作
     */
    private String piStatus;

    private String piStarter;

    private String piBusinesskey;

    private Date piCreatetime;

    private Date piUpdatetime;

    private Date piEndtime;
}
