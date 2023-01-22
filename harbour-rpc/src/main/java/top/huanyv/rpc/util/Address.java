package top.huanyv.rpc.util;

/**
 * @author huanyv
 * @date 2023/1/19 15:41
 */
public class Address {
    private String ip;

    private int port;

    public Address() {
    }

    public Address(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public static Address parse(String address) {
        String[] arr = address.split(":");
        if (arr.length < 2) {
            throw new IllegalArgumentException();
        }
        return new Address(arr[0], Integer.parseInt(arr[1]));
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "Address{" +
                "ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                '}';
    }
}
