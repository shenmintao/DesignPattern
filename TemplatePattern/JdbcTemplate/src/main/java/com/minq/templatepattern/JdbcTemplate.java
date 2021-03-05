package com.minq.templatepattern;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public abstract class JdbcTemplate {

    private DataSource dataSource;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<?> executeQuery(String sql, RowMapper<?> rowMapper, Object[] values) {
        try {
            //获取连接
            Connection conn = this.getConnection();

            //创建语句集
            PreparedStatement pstm = this.createPreparedStatement(conn, sql);

            //执行语句集
            ResultSet rs = this.executeQuery(pstm, values);

            //处理结果集
            List<?> result = this.parseResultSet(rs, rowMapper);

            //关闭结果集
            this.closeResultSet(rs);
            //关闭语句集
            this.closeStatement(pstm);
            //关闭连接
            this.closeConnection(conn);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Connection getConnection() throws Exception{
        return this.dataSource.getConnection();
    }

    protected PreparedStatement createPreparedStatement(Connection conn, String sql) throws Exception{
        return conn.prepareStatement(sql);
    }

    protected ResultSet executeQuery(PreparedStatement pstm, Object[] values) throws Exception {
        for(int i = 0; i < values.length; i++) {
            pstm.setObject(i, values[i]);
        }
        return pstm.executeQuery();
    }

    protected List<?> parseResultSet(ResultSet rs, RowMapper<?> rowMapper) throws Exception {
        List<Object> result = new ArrayList<>();
        int rowNum = 1;
        while (rs.next()) {
            result.add(rowMapper.mapRow(rs, rowNum ++));
        }
        return result;
    }

    protected void closeConnection(Connection conn) throws Exception {
        conn.close();
    }

    protected void closeStatement(PreparedStatement pstm) throws Exception {
        pstm.close();
    }

    protected void closeResultSet(ResultSet rs) throws Exception {
        rs.close();
    }
}
