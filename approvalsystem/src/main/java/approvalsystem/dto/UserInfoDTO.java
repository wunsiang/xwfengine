package approvalsystem.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class UserInfoDTO implements Serializable {
    private static final long serialVersionUID=1L;

    private String id;

    private String uiName;

    //租户id
    private String tenantId;

    //角色
    private String giId;

    //人员编号
    private String uiNo;
}
