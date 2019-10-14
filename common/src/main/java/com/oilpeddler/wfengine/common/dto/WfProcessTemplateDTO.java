package com.oilpeddler.wfengine.common.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oilpeddler.wfengine.common.dataobject.BaseDO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 流程模板文件DTO
 * </p>
 *
 * @author wenxiang
 * @since 2019-10-11
 */
@Data
@Accessors(chain = true)
public class WfProcessTemplateDTO implements Serializable {
    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    private String id;
    /**
     * 创建时间
     */

    private Date createtime;
    /**
     * 最后更新时间
     */
    private Date updatetime;

    /**
     * 流程描述文件内容
     */
    private String ptContent;

    /**
     * 流程描述文件名称
     */
    private String ptFilename;

}

