package top.huanyv.rpc.consumer;

import top.huanyv.rpc.provider.RequestDTO;

public interface ConsumerServer {
    Object execute(String address, RequestDTO requestDTO, int timeout);
}
