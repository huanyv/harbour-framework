package top.huanyv.admin.controller;

import redis.clients.jedis.Jedis;
import top.huanyv.admin.domain.vo.cache.CacheNode;
import top.huanyv.admin.domain.vo.LayUIPageVo;
import top.huanyv.admin.domain.vo.cache.RedisVo;
import top.huanyv.admin.utils.RestResult;
import top.huanyv.bean.annotation.Component;
import top.huanyv.bean.utils.StringUtil;
import top.huanyv.webmvc.annotation.Get;
import top.huanyv.webmvc.annotation.Route;
import top.huanyv.webmvc.annotation.argument.Param;
import top.huanyv.webmvc.core.HttpRequest;
import top.huanyv.webmvc.core.action.ActionResult;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author huanyv
 * @date 2023/4/23 19:06
 */
@Component
@Route("/monitor/cache")
public class SysCacheController {

    @Route("/list")
    public ActionResult list() {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        Set<String> keys = jedis.keys("*");
        jedis.close();
        List<String> keyGroups = keys.stream().map(key -> key.split(":")[0]).distinct().collect(Collectors.toList());
        List<CacheNode> nodes = new ArrayList<>();
        int count = 1;
        for (int i = 0; i < keyGroups.size(); i++) {
            nodes.add(new CacheNode(count++, 0, keyGroups.get(i)));
        }
        addChildren(nodes, keys);
        return new LayUIPageVo(nodes.size(), nodes);
    }

    public void addChildren(List<CacheNode> nodes, Collection<String> keys) {
        int count = nodes.size() + 1;
        for (String key : keys) {
            int parentId = 0;
            for (CacheNode node : nodes) {
                if (key.startsWith(node.getTitle() + ":")) {
                    parentId = node.getId();
                    nodes.add(new CacheNode(count++, parentId, key));
                    break;
                }
            }
        }
    }

    @Get("/get")
    public ActionResult getValue(@Param("key") String key) {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        String dataType = jedis.type(key);
        RedisVo redisVo = new RedisVo(key, dataType, String.valueOf(jedis.ttl(key)));
        String data = "";
        switch (dataType) {
            case "string":
                data = jedis.get(key);
                break;
            case "hash":
                data = jedis.hgetAll(key).toString();
                break;
            case "list":
                data = jedis.lrange(key, 0, -1).toString();
                break;
            case "set":
                data = jedis.smembers(key).toString();
                break;
            case "zset":
                data = jedis.zrange(key, 0, -1).toString();
                break;
            default:
                System.err.println("Unrecognized data type: " + dataType);
                break;
        }
        jedis.close();
        redisVo.setData(data);
        redisVo.setDataType(StringUtil.firstLetterUpper(redisVo.getDataType().toLowerCase()));
        return RestResult.ok("获取成功", redisVo);
    }

}
