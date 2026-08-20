package com.nikofinance.config;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class SQLiteDialect extends Dialect {

    public SQLiteDialect() {
        super();
        registerColumnType(java.sql.Types.BIT, "integer");
        registerColumnType(java.sql.Types.TINYINT, "tinyint");
        registerColumnType(java.sql.Types.SMALLINT, "smallint");
        registerColumnType(java.sql.Types.INTEGER, "integer");
        registerColumnType(java.sql.Types.BIGINT, "bigint");
        registerColumnType(java.sql.Types.FLOAT, "float");
        registerColumnType(java.sql.Types.REAL, "real");
        registerColumnType(java.sql.Types.DOUBLE, "double");
        registerColumnType(java.sql.Types.NUMERIC, "numeric");
        registerColumnType(java.sql.Types.DECIMAL, "decimal");
        registerColumnType(java.sql.Types.CHAR, "char");
        registerColumnType(java.sql.Types.VARCHAR, "varchar");
        registerColumnType(java.sql.Types.LONGVARCHAR, "longvarchar");
        registerColumnType(java.sql.Types.DATE, "date");
        registerColumnType(java.sql.Types.TIME, "time");
        registerColumnType(java.sql.Types.TIMESTAMP, "timestamp");
        registerColumnType(java.sql.Types.BINARY, "blob");
        registerColumnType(java.sql.Types.VARBINARY, "blob");
        registerColumnType(java.sql.Types.LONGVARBINARY, "blob");
        registerColumnType(java.sql.Types.BLOB, "blob");
        registerColumnType(java.sql.Types.CLOB, "clob");
        registerColumnType(java.sql.Types.BOOLEAN, "integer");

        registerFunction("abs", new StandardSQLFunction("abs", StandardBasicTypes.INTEGER));
        registerFunction("round", new StandardSQLFunction("round", StandardBasicTypes.DOUBLE));
    }

    @Override
    public boolean supportsIdentityColumns() {
        return true;
    }

    @Override
    public boolean supportsSequences() {
        return false;
    }

    @Override
    public String getIdentitySelectString() {
        return "select last_insert_rowid()";
    }

    @Override
    public boolean supportsLimit() {
        return true;
    }

    @Override
    public String getLimitString(String query, boolean hasOffset) {
        return query + (hasOffset ? " limit ? offset ?" : " limit ?");
    }

    @Override
    public boolean supportsCurrentTimestampSelection() {
        return true;
    }

    @Override
    public boolean isCurrentTimestampSelectStringCallable() {
        return false;
    }

    @Override
    public String getCurrentTimestampSelectString() {
        return "datetime('now')";
    }

    @Override
    public boolean supportsResultSetPositionQueryMethodsOnForwardOnlyCursor() {
        return true;
    }
}