package top.huanyv.webmvc.core.request.convert;

import junit.framework.TestCase;
import org.junit.Test;
import top.huanyv.webmvc.utils.ClassDesc;
import top.huanyv.webmvc.utils.convert.TypeConverter;
import top.huanyv.webmvc.utils.convert.TypeConverterComposite;

import java.math.BigDecimal;
import java.util.*;

public class TypeConverterCompositeTest extends TestCase {

    TypeConverter converter = new TypeConverterComposite();

    public void testConvert() {
        Map<String, String[]> map = new HashMap<>();
        map.put("username",new String[]{"admin"});
        map.put("password",new String[]{"123456"});
        map.put("sex",new String[]{"男"});
        map.put("age",new String[]{"18"});
        map.put("birthday",new String[]{"2022-06-09 16:00:00"});
        map.put("money",new String[]{"1234.12"});
        map.put("aihao",new String[]{"1", "2", "3"});
        map.put("nums",new String[]{"1", "5", "3"});
        ClassDesc classDesc = new ClassDesc();
        classDesc.setType(User.class);
        Object result = converter.convert(map, classDesc);
        System.out.println("result = " + result);
    }

    @Test
    public void test02() {
        
    }
}

class User {
    private String username;
    private String password;
    private String sex;
    private Integer age;
    private Date birthday;
    private BigDecimal money;
    private List<Integer> aihao;
    private Integer[] nums;

    private boolean bool;

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    public User() {
    }

    public User(String username, String password, String sex, Integer age) {
        this.username = username;
        this.password = password;
        this.sex = sex;
        this.age = age;
    }

    public Integer[] getNums() {
        return nums;
    }

    public void setNums(Integer[] nums) {
        this.nums = nums;
    }

    public void setAihao(List<Integer> aihao) {
        this.aihao = aihao;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", birthday=" + birthday +
                ", money=" + money +
                ", aihao=" + aihao +
                ", nums=" + Arrays.toString(nums) +
                '}';
    }

}
