package com.minq.DBConnectionPool;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class DBConnectionPool extends Pool{

    private int checkedOut; //正在使用的连接数

    //private Vector<Connection> freeConnections = new Vector<Connection>();
    private List<Connection> freeConnections = Collections.synchronizedList(new ArrayList());   //存放已释放的连接对象容器
    private String password = null; //密码
    private String url = null;  //连接字符串
    private String userName = null; //用户名
    private static int num = 0; //空连接数
    private static int numActive = 0; //当前工作的连接数
    private static DBConnectionPool pool = null;    //连接池实例变量

    //产生数据连接池
    public static synchronized DBConnectionPool getInstance() {
        if (pool == null) {
            pool = new DBConnectionPool();
        }
        return pool;
    }

    //获得一个数据库连接池的实例
    private DBConnectionPool() {
        try{
            init();
            for(int i =0 ;i < normalConnect; i++) {
                Connection connection = newConnection();
                if (connection != null) {
                    freeConnections.add(connection);
                    num++;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //初始化
    private void init() throws IOException {
        InputStream inputStream = DBConnectionPool.class.getResourceAsStream(propertiesName);
        Properties properties = new Properties();
        properties.load(inputStream);
        this.userName = properties.getProperty("userName");
        this.password = properties.getProperty("password");
        this.driverName = properties.getProperty("driverName");
        this.url = properties.getProperty("url");
        this.maxConnect = Integer.parseInt(properties.getProperty("maxConnect"));
        this.normalConnect = Integer.parseInt(properties.getProperty("normalConnect"));
    }

    //创建一个新连接
    private Connection newConnection(){
        Connection connection = null;
        try {
            if (userName == null) {
                //用户名为空
                connection = DriverManager.getConnection(url);
            } else {
                connection = DriverManager.getConnection(url, userName, password);
            }
            System.out.println("连接池创建一个新的连接");
        } catch (SQLException e) {
            System.out.println("无法创建这个URL的连接" + url);
            return null;
        }
        return connection;
    }

    @Override
    public void createPool() {
        pool = new DBConnectionPool();
        if(pool != null) {
            System.out.println("创建连接池成功");
        } else {
            System.out.println("创建连接池失败");
        }
    }

    @Override
    public synchronized Connection getConnection() {
        Connection connection = null;
        if (freeConnections.size() > 0) {
            //还有空闲连接
            num --;

            connection = freeConnections.get(0);
            //可能会有性能问题？因为要复制一个新的list
            freeConnections.remove(0);

            //或许可以直接connection = freeConnections.remove(0)
            try{
                if (connection.isClosed()) {
                    System.out.println("从连接池中删除一个无效连接");
                    connection = getConnection();
                }
            } catch (SQLException e) {
                System.out.println("从连接池中删除一个无效连接");
                connection = getConnection();
            }
        } else if (maxConnect == 0 || checkedOut < maxConnect) {
            //没有空闲连接且当前连接小于最大允许值，最大值为0则不限制
            connection = newConnection();
        }

        if (connection != null) {
            checkedOut ++;
        }
        numActive ++;
        return connection;
    }

    @Override
    public synchronized Connection getConnection(long timeout) {
        long startTime = new Date().getTime();
        Connection connection;
        while ((connection = getConnection()) == null) {
            try{
                wait(timeout);
            } catch (InterruptedException e) {

            }
            if (new Date().getTime() - startTime >= timeout) {
                return null;
            }
        }
        return  connection;
    }

    //如果不再使用某个连接对象，可调用此方法讲该对象释放到连接池
    @Override
    public synchronized void freeConnection(Connection connection) {
        freeConnections.add(connection);
        num++;
        checkedOut--;
        numActive--;
        notifyAll();    //解锁
    }


    @Override
    public int getNum() {
        return num;
    }

    @Override
    public int getNumActive() {
        return numActive;
    }

    @Override
    public synchronized void release() {
        try {
            synchronized (freeConnections) {
                Iterator i = freeConnections.iterator(); // Must be in synchronized block
                while (i.hasNext()) {
                    Connection connection = (Connection)i.next();
                    try{
                        connection.close();
                        num --;
                    } catch (SQLException e) {
                        System.out.println("无法关闭连接池中的连接");
                    }
                }
                freeConnections.clear();
                numActive = 0;
            }
        } finally {
            super.release();
        }
    }
}

