package top.huanyv.admin.domain.server;

import lombok.ToString;

import java.math.BigDecimal;

@ToString
public class Mem {
    /**
     * 内存总量
     */
    private double total;

    /**
     * 已用内存
     */
    private double used;

    /**
     * 剩余内存
     */
    private double free;

    public double getTotal() {
        BigDecimal bigDecimal = new BigDecimal(total / (1024 * 1024 * 1024));
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getUsed() {
        BigDecimal bigDecimal = new BigDecimal(used / (1024 * 1024 * 1024));
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void setUsed(double used) {
        this.used = used;
    }

    public double getFree() {
        BigDecimal bigDecimal = new BigDecimal(free / (1024 * 1024 * 1024));
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public void setFree(double free) {
        this.free = free;
    }

    public double getUsage() {
        BigDecimal bigDecimal = new BigDecimal((used / total) * 100);
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
