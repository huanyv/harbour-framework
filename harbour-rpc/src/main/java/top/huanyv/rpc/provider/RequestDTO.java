package top.huanyv.rpc.provider;

import java.io.Serializable;
import java.util.Arrays;

public class RequestDTO implements Serializable {

    private static final long serialVersionUID = 4658547498030903725L;

    private String serviceName;
    private String methodName;
    private Class<?>[] methodParameterTypes;
    private Object[] methodArgs;

    public String getServiceName() {
        return this.serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getMethodParameterTypes() {
        return methodParameterTypes;
    }

    public void setMethodParameterTypes(Class<?>[] methodParameterTypes) {
        this.methodParameterTypes = methodParameterTypes;
    }

    public Object[] getMethodArgs() {
        return methodArgs;
    }

    public void setMethodArgs(Object[] methodArgs) {
        this.methodArgs = methodArgs;
    }

    @Override
    public String toString() {
        return "{serviceName='" + serviceName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", methodParameterTypes=" + Arrays.toString(methodParameterTypes) +
                ", methodArgs=" + Arrays.toString(methodArgs) +
                '}';
    }
}
