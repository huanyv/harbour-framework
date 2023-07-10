package top.huanyv.admin.domain.server;

import lombok.ToString;

import java.math.BigDecimal;

@ToString
public class CPU {
    /**
     * 核心数
     */
    private int cpuNum;

    /**
     * CPU总的使用率
     */
    private double total;

    /**
     * CPU用户使用率
     */
    private double used;

    /**
     * CPU系统使用率
     */
    private double sys;

    /**
     * CPU当前空闲率
     */
    private double free;

    public int getCpuNum() {
        return cpuNum;
    }

    public void setCpuNum(int cpuNum) {
        this.cpuNum = cpuNum;
    }

    public double getTotal() {
        return this.total * 100;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getSys() {
        BigDecimal bigDecimal = new BigDecimal((sys / total) * 100);
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void setSys(double sys) {
        this.sys = sys;
    }

    public double getFree() {
        BigDecimal bigDecimal = new BigDecimal((free / total) * 100);
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void setFree(double free) {
        this.free = free;
    }

    public void setUsed(double used) {
        this.used = used;
    }

    public double getUsed() {
        BigDecimal bigDecimal = new BigDecimal((used / total) * 100);
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
