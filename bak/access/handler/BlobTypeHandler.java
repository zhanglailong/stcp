package org.jeecg.modules.access.handler;

import net.ucanaccess.jdbc.UcanaccessBlob;
import net.ucanaccess.jdbc.UcanaccessConnection;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.io.ByteArrayInputStream;
import java.sql.*;

@MappedJdbcTypes(JdbcType.BLOB) // 声明数据库中对应数据类型
@MappedTypes(value = byte[].class) // 转化后的数据类型
public class BlobTypeHandler extends BaseTypeHandler<byte[]> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, byte[] parameter, JdbcType jdbcType)
            throws SQLException {
        ByteArrayInputStream bis = new ByteArrayInputStream(parameter);
        ps.setBinaryStream(i, bis, parameter.length);
    }

    @Override
    public byte[] getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        byte[] bytes = rs.getBytes(columnName);
        return bytes;
    }

    @Override
    public byte[] getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        byte[] bytes = rs.getBytes(columnIndex);
        return bytes;
    }

    @Override
    public byte[] getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        byte[] bytes = cs.getBytes(columnIndex);
        return bytes;
    }
}
