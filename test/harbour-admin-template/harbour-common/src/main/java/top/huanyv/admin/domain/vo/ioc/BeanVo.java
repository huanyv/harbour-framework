package top.huanyv.admin.domain.vo.ioc;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author huanyv
 * @date 2023/4/22 15:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BeanVo {
    private String beanName;
    private String beanClass;
    private String beanType;
    private Boolean singleton;
    private Boolean lazy;
    private Boolean aop;
    private String[] interfaces;
    private String superclass;
    private String[] fields;
    private String[] methods;
}
