package com.oilpeddler.wfengine.formcomponent.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oilpeddler.wfengine.common.dataobject.BaseDO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class WfBusinessFormDTO implements Serializable {
    private static final long serialVersionUID=1L;

    private String id;
    /**
     * 创建时间
     */

    private Date createtime;
    /**
     * 最后更新时间
     */
    private Date updatetime;

    private String formName;

    private String formUrl;

}
