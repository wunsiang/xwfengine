package approvalsystem.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@TableName(value="leave_info")
public class LeaveInfoDO implements Serializable {
    private static final long serialVersionUID=1L;
    /**
     * 主键
     */
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    private String uiId;

    private int durations;

}
