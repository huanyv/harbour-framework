package top.huanyv.admin.domain.vo.cache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huanyv
 * @date 2023/4/24 20:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisVo {
    private String name;
    private String dataType;
    private String ttl;
    private String data;

    public RedisVo(String name) {
        this.name = name;
    }

    public RedisVo(String name, String ttl) {
        this.name = name;
        this.ttl = ttl;
    }

    public RedisVo(String name, String dataType, String ttl) {
        this.name = name;
        this.dataType = dataType;
        this.ttl = ttl;
    }
}
