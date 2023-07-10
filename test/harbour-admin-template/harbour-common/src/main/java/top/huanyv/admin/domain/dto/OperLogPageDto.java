package top.huanyv.admin.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huanyv
 * @date 2023/5/7 17:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperLogPageDto {

    private String operUser;

    private String operAddr;

    private String operDateRange;
}
