package top.huanyv.jdbc.core;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author admin
 * @date 2022/7/21 17:31
 */
public class SqlSessionFactory {

    public static final String SCAN_PACKAGE_KEY = "mapper.scan";

    /**
     * 从配置文件创建一个sqlsession
     * <br/>
     * <br/>
     * <code>
     *    mapper.scan=top.huanyv.jdbc.core <br />
     *    driverClassName=com.mysql.jdbc.Driver <br/>
     *    url=jdbc:mysql://localhost:3306/temp?useSSL=false <br/>
     *    username=root <br/>
     *    password=123 <br/>
     * </code>
     * <br/>
     * @param inputStream
     * @return
     */
    public static SqlSession openSession(InputStream inputStream) {
        // 加载配置文件
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DataSource dataSource = SimpleDataSource.createDataSource(properties);
        String scanPack = properties.getProperty(SCAN_PACKAGE_KEY);

        // 创建mapperscan实例
        MapperScanner mapperScanner = new MapperScanner();
        mapperScanner.setDataSource(dataSource);
        mapperScanner.setScanPack(scanPack);

        return openSession(mapperScanner);
    }

    /**
     * 创建一个sqlSession
     */
    public static SqlSession openSession(MapperScanner scan) {
        SqlSession sqlSession = new SqlSession();
        sqlSession.setMapperScanner(scan);
        try {
            Connection connection = scan.getDataSource().getConnection();
            sqlSession.setConnection(connection);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return sqlSession;
    }






}
