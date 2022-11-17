package top.huanyv.webmvc.utils;

/**
 * @author huanyv
 * @date 2022/11/15 20:14
 */
public interface ResponseHeaderConst {

    // 表明服务器是否支持指定范围请求及哪种类型的分段请求	Accept-Ranges: bytes
    String ACCEPT_RANGES = "Accept-Ranges";

    // 从原始服务器到代理缓存形成的估算时间（以秒计，非负）	Age: 12
    String AGE = "Age";

    // 对某网络资源的有效的请求行为，不允许则返回405	Allow: GET, HEAD
    String ALLOW = "Allow";

    // 告诉所有的缓存机制是否可以缓存及哪种类型	Cache-Control: no-cache
    String CACHE_CONTROL = "Cache-Control";

    // web服务器支持的返回内容压缩编码类型。	Content-Encoding: gzip
    String CONTENT_ENCODING = "Content-Encoding";

    // 响应体的语言	Content-Language: en,zh
    String CONTENT_LANGUAGE = "Content-Language";

    // 响应体的长度	Content-Length: 348
    String CONTENT_LENGTH = "Content-Length";

    // 请求资源可替代的备用的另一地址	Content-Location: /index.htm
    String CONTENT_LOCATION = "Content-Location";

    // 返回资源的MD5校验值	Content-MD5: MD5校验值
    String CONTENT_MD5 = "Content-MD5";

    // 在整个返回体中本部分的字节位置	Content-Range: bytes 21010-47021/47022
    String CONTENT_RANGE = "Content-Range";

    // 返回内容的MIME类型	Content-Type: text/html; charset=utf-8
    String CONTENT_TYPE = "Content-Type";

    // attachment:表示以附件方式下载，如果要在页面中打开，可以改为inline.  Content-disposition: attachment; filename=file
    String CONTENT_DISPOSITION = "Content-Disposition";

    // 原始服务器消息发出的时间	Date: Tue, 15 Nov 2010 08:12:31 GMT
    String DATE = "Date";

    // 请求变量的实体标签的当前值	ETag: “请求变量实体标签当前值”
    String ETAG = "ETag";

    // 响应过期的日期和时间	Expires: Thu, 01 Dec 2010 16:00:00 GMT
    String EXPIRES = "Expires";

    // 请求资源的最后修改时间	Last-Modified: Tue, 15 Nov 2010 12:45:26 GMT
    String LAST_MODIFIED = "Last-Modified";

    // 用来重定向接收方到非请求URL的位置来完成请求或标识新的资源	Location: http://www.jsons.cn
    String LOCATION = "Location";

    // 包括实现特定的指令，它可应用到响应链上的任何接收方	Pragma: no-cache
    String PRAGMA = "Pragma";

    // 它指出认证方案和可应用到代理的该URL上的参数	Proxy-Authenticate: Basic
    String PROXY_AUTHENTICATE = "Proxy-Authenticate";

    // 应用于重定向或一个新的资源被创造，在5秒之后重定向（由网景提出，被大部分浏览器支持）	Refresh: 5; url= http://www.jsons.cn
    String REFRESH = "refresh";

    // 如果实体暂时不可取，通知客户端在指定时间之后再次尝试	Retry-After: 120
    String RETRY_AFTER = "Retry-After";

    // web服务器软件名称	Server: Apache/1.3.27 (Unix) (Red-Hat/Linux)
    String SERVER = "Server";

    // 设置Http Cookie	Set-Cookie: UserID=JohnDoe; Max-Age=3600; Version=1
    String SET_COOKIE = "Set-Cookie";

    // 指出头域在分块传输编码的尾部存在	Trailer: Max-Forwards
    String TRAILER = "Trailer";

    // 文件传输编码	Transfer-Encoding:chunked
    String TRANSFER_ENCODING = "Transfer-Encoding";

    // 告诉下游代理是使用缓存响应还是从原始服务器请求	Vary: *
    String VARY = "Vary";

    // 告知代理客户端响应是通过哪里发送的	Via: 1.0 fred, 1.1 nowhere.com (Apache/1.1)
    String VIA = "Via";

    // 警告实体可能存在的问题	Warning: 199 Miscellaneous warning
    String WARNING = "Warning";

    // 表明客户端请求实体应该使用的授权方案	WWW-Authenticate: Basic
    String WWW_Authenticate = "WWW-Authenticate";

}
