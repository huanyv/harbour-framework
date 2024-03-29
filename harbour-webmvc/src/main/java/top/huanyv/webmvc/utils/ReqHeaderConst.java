package top.huanyv.webmvc.utils;

/**
 * @author huanyv
 * @date 2022/11/15 20:14
 */
public interface ReqHeaderConst {

    // 指定客户端能够接收的内容类型	Accept: text/plain, text/html
    String ACCEPT = "Accept";

    // 浏览器可以接受的字符编码集。	Accept-Charset: iso-8859-5
    String ACCEPT_CHARSET = "Accept-Charset";

    // 指定浏览器可以支持的web服务器返回内容压缩编码类型。	Accept-Encoding: compress, gzip
    String ACCEPT_ENCODING = "Accept-Encoding";

    // 浏览器可接受的语言	Accept-Language: en,zh
    String ACCEPT_LANGUAGE = "Accept-Language";

    // 可以请求网页实体的一个或者多个子范围字段	Accept-Ranges: bytes
    String ACCEPT_RANGES = "Accept-Ranges";

    // HTTP授权的授权证书	Authorization: 授权证书
    String AUTHORIZATION = "Authorization";

    // 指定请求和响应遵循的缓存机制	Cache-Control: no-cache
    String CACHE_CONTROL = "Cache-Control";

    // 表示是否需要持久连接。（HTTP 1.1默认进行持久连接）	Connection: close
    String CONNECTION = "Connection";

    // HTTP请求发送时，会把保存在该请求域名下的所有cookie值一起发送给web服务器。	Cookie: $Version=1; Skin=new;
    String COOKIE = "Cookie";

    // 请求的内容长度	Content-Length: 348
    String CONTENT_LENGTH = "Content-Length";

    // 请求的与实体对应的MIME信息	Content-Type: application/x-www-form-urlencoded
    String CONTENT_TYPE = "Content-Type";

    // 请求发送的日期和时间	Date: Tue, 15 Nov 2010 08:12:31 GMT
    String DATE = "Date";

    // 请求的特定的服务器行为	Expect: 100-continue
    String EXPECT = "Expect";

    // 发出请求的用户的Email	From: user@jsons.cn
    String FROM = "From";

    // 指定请求的服务器的域名和端口号	Host: www.jsons.cn
    String HOST = "Host";

    // 只有请求内容与实体相匹配才有效	If-Match: “特定值”
    String IF_MATCH = "If-Match";

    // 如果请求的部分在指定时间之后被修改则请求成功，未被修改则返回304代码	If-Modified-Since: Sat, 29 Oct 2010 19:43:31 GMT
    String IF_MODIFIED_SINCE = "If-Modified-Since";

    // 如果内容未改变返回304代码，参数为服务器先前发送的Etag，与服务器回应的Etag比较判断是否改变	If-None-Match: “特定值”
    String IF_NONE_MATCH = "If-None-Match";

    // 如果实体未改变，服务器发送客户端丢失的部分，否则发送整个实体。参数也为Etag	If-Range: “特定值”
    String IF_RANGE = "If-Range";

    // 只在实体在指定时间之后未被修改才请求成功	If-Unmodified-Since: Sat, 29 Oct 2010 19:43:31 GMT
    String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";

    // 限制信息通过代理和网关传送的时间	Max-Forwards: 10
    String MAX_FORWARDS = "Max-Forwards";

    // 用来包含实现特定的指令	Pragma: no-cache
    String PRAGMA = "Pragma";

    // 连接到代理的授权证书	Proxy-Authorization: 链接到代理的授权证书
    String PROXY_AUTHORIZATION = "Proxy-Authorization";

    // 只请求实体的一部分，指定范围	Range: bytes=500-999
    String RANGE = "Range";

    // 先前网页的地址，当前请求网页紧随其后,即来路	Referer: http://www.jsons.cn
    String REFERER = "Referer";

    // 客户端愿意接受的传输编码，并通知服务器接受接受尾加头信息	TE: trailers,deflate;q=0.5
    String TE = "TE";

    // 向服务器指定某种传输协议以便服务器进行转换（如果支持）	Upgrade: HTTP/2.0, SHTTP/1.3, IRC/6.9, RTA/x11
    String UPGRADE = "Upgrade";

    // User-Agent的内容包含发出请求的用户信息	User-Agent: Mozilla/5.0 (Linux; X11)
    String USER_AGENT = "User-Agent";

    // 通知中间网关或代理服务器地址，通信协议	Via: 1.0 fred, 1.1 nowhere.com (Apache/1.1)
    String VIA = "Via";

    // 关于消息实体的警告信息	Warn: 199 Miscellaneous warning
    String WARNING = "Warning";

}