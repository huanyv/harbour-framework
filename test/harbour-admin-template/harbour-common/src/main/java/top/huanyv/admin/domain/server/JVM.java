package top.huanyv.admin.domain.server;

import lombok.ToString;
import top.huanyv.admin.utils.Util;
import top.huanyv.tools.utils.StringUtil;

import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@ToString
public class JVM {
    /**
     * 当前JVM占用的内存总数(M)
     */
    private double total;

    /**
     * JVM最大可用内存总数(M)
     */
    private double max;

    /**
     * JVM空闲内存(M)
     */
    private double free;

    /**
     * JDK版本
     */
    private String version;

    /**
     * JDK路径
     */
    private String home;

    public double getTotal() {
        BigDecimal bigDecimal = new BigDecimal(total / (1024 * 1024));
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getMax() {
        BigDecimal bigDecimal = new BigDecimal(max / (1024 * 1024));
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getFree() {
        BigDecimal bigDecimal = new BigDecimal(free / (1024 * 1024));
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void setFree(double free) {
        this.free = free;
    }

    public double getUsed() {
        BigDecimal bigDecimal = new BigDecimal((total - free) / (1024 * 1024));
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public double getUsage() {
        BigDecimal bigDecimal = new BigDecimal(((total - free) / total) * 100);
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 获取JDK名称
     */
    public String getName() {
        return ManagementFactory.getRuntimeMXBean().getVmName();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    /**
     * JDK启动时间
     */
    public String getStartTime() {
        Date date = new Date(Util.getStartTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    /**
     * JDK运行时间
     */
    public String getRunTime() {
        return String.valueOf((new Date().getTime() - Util.getStartTime()) / 1000);
    }

    /**
     * 运行参数
     */
    public String getInputArgs() {
        return ManagementFactory.getRuntimeMXBean().getInputArguments().toString();
    }

}
