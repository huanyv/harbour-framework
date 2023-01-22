package top.huanyv.rpc.register;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZookeeperRegistry extends AbstractRegistry {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperRegistry.class);

    private static final int SESSION_TIMEOUT_MS = 5000;
    private static final int SLEEP_TIME_MS = 1000;
    private static final int MAX_RETRIES = 2;

    private final Map<String, List<String>> serviceProviderMap = new HashMap<>();
    private CuratorFramework curatorFramework;

    public ZookeeperRegistry() {
        this("127.0.0.1:2181");
    }

    public ZookeeperRegistry(String address) {
        init(address);
    }

    @Override
    protected void init(String registerAddress) {
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(registerAddress)
                .sessionTimeoutMs(SESSION_TIMEOUT_MS)
                .retryPolicy(new ExponentialBackoffRetry(SLEEP_TIME_MS, MAX_RETRIES))
                .build();

        curatorFramework.start();

    }

    public void register(String providerAddress, String service) {
        try {
            String servicePath = FOLDER + SEPARATOR + service;
            Stat stat = curatorFramework.checkExists().forPath(servicePath);
            if (stat == null) {
                curatorFramework.create().creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT).forPath(servicePath);
            }
            String provider = servicePath + SEPARATOR + providerAddress;

            curatorFramework.create().withMode(CreateMode.EPHEMERAL)
                    .forPath(provider);
            logger.info("provider:{} is registered to {}", providerAddress, servicePath);
        } catch (Exception e) {

            logger.error(e.getMessage(), e);
        }
    }


    @Override
    public List<String> lookup(String service) {
        String path = FOLDER + SEPARATOR + service;
        try {
            List<String> provider = curatorFramework.getChildren().forPath(path);
            serviceProviderMap.put(service, provider);

            watchProvider(path);

            return serviceProviderMap.get(service);
        } catch (Exception e) {
            logger.error(String.format("call ZookeeperRegistry.discover, occur exception, service:[%s], e.getMessage:[%s]", service, e.getMessage()), e);
            return null;
        }
    }

    private void watchProvider(final String path) {
        PathChildrenCache childrenCache = new PathChildrenCache(curatorFramework, path, true);
        PathChildrenCacheListener listener = new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                //提供者有更新，则及时更新到内存中
                serviceProviderMap.put(path, curatorFramework.getChildren().forPath(path));
            }
        };
        childrenCache.getListenable().addListener(listener);
        try {
            childrenCache.start();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
