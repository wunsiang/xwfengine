package com.oilpeddler.wfengine.common.dataobject;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体对象
 */
public class BaseDO implements Serializable {

    /**
     * 主键
     */
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;
    /**
     * 创建时间
     */

    private Date createtime;
    /**
     * 最后更新时间
     */
    private Date updatetime;

    @Override
    public String toString() {
        return "BaseDO{" +
                "createTime=" + createtime +
                ", updateTime=" + updatetime +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}
