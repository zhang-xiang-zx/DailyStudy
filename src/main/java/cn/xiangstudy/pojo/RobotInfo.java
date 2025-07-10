package cn.xiangstudy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zhangxiang
 * @date 2025-07-04 15:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RobotInfo {

    private String robotName;

    private Integer robotId;

    private Long trackDistance;
}
