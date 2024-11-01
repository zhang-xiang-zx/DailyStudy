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
 * @date 2024-10-31 16:29
 */
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String,Object> params;
}

