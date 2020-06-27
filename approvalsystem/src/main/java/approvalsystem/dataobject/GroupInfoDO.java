package approvalsystem.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@TableName(value="group_info")
public class GroupInfoDO implements Serializable {
    private static final long serialVersionUID=1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    private String giName;

    private String giNo; //编号

    private String tenantId; //租户ID

}
