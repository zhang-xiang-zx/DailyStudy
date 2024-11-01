package cn.xiangstudy.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Map;

/**
 * @author zhangxiang
 * @date 2024-10-31 14:10
 */
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity{
    private Long userId;

    private String account;

    private String nickname;

    private String password;

    private int status;
}

