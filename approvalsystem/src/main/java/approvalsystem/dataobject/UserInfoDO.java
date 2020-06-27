package approvalsystem.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@TableName(value="user_info")
public class UserInfoDO implements Serializable {
    private static final long serialVersionUID=1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    private String uiName;

    //租户id
    private String tenantId;

    //角色
    private String giId;

    //人员编号
    private String uiNo;
}
