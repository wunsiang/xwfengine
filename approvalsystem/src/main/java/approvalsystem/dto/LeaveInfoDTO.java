package approvalsystem.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class LeaveInfoDTO implements Serializable {
    private static final long serialVersionUID=1L;
    /**
     * 主键
     */
    private String id;

    private String uiName;

    private String uiId;

    private int durations;

}
