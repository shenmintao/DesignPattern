package com.minq.DBConnectionPool;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public abstract class Pool {

    public String propertiesName = "dbConnection.properties";

    private  static Pool instance = null;   //定义唯一实例

    protected int maxConnect = 100; //最大连接数

    protected int normalConnect = 10;   //保持连接数

    protected String driverName = null; //驱动字符串

    protected Driver driver = null; //驱动变量

    protected Pool() {
        try {
            init();
            loadDrivers(driverName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //初始化配置文件
    private void init() throws IOException {
        InputStream inputStream = Pool.class.getResourceAsStream(propertiesName);
        Properties properties = new Properties();
        properties.load(inputStream);

        this.driverName = properties.getProperty("driverName");
        this.maxConnect = Integer.parseInt(properties.getProperty("maxConnect"));
        this.normalConnect = Integer.parseInt(properties.getProperty("normal"));
    }

    //加载JDBC驱动
    protected void loadDrivers(String driverName) {
        try{
            driver = (Driver) Class.forName(driverName).newInstance();
            DriverManager.registerDriver(driver);
        } catch (Exception e){
            System.out.println("无法注册JDBC驱动程序：" + driverName + "，错误：" + e);
        }
    }

    //创建连接池
    public abstract void createPool();

    //获得连接池实例（单例模式）
    public static synchronized Pool getInstance() throws IOException, InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        if (instance != null) {

            //三种反射方式
            //Class.forName("com.minq.DBConnectionPool.Pool").newInstance();
            //Pool.class.newInstance();
            //Pool.class.getConstructor().newInstance();
            instance = Pool.class.newInstance();
        }
        return instance;
    }

    //获得一个可用的连接，如果没有则创建一个连接，且小于最大连接限制
    public abstract Connection getConnection();

    //获得一个连接，有时间限制
    public abstract Connection getConnection(long time);

    //将连接对象返还给连接池
    public abstract void freeConnection(Connection connection);

    //返回当前空闲连接数
    public abstract int getNum();

    //返回当前工作的连接数
    public abstract int getNumActive();

    //撤销驱动
    protected synchronized void release() {
        try{
            DriverManager.deregisterDriver(driver);
            System.out.println("撤销JDBC驱动程序" + driver.getClass().getName());
        } catch (SQLException e) {
            System.out.println("无法撤销JDBC驱动程序" + driver.getClass().getName() + "，错误：" + e);
        }
    }

}
