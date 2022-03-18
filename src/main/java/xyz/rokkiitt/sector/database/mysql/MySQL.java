package xyz.rokkiitt.sector.database.mysql;

import com.zaxxer.hikari.*;
import xyz.rokkiitt.sector.config.*;
import java.sql.*;

public class MySQL
{
    public HikariDataSource database;
    
    public MySQL() {
        try {
            final HikariConfig config = new HikariConfig();
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            config.setJdbcUrl("jdbc:mysql://mysql.titanaxe.com:3306/srv207254?serverTimezone=UTC&useLegacyDatetimeCode=false");
            config.setUsername("srv207254");
            config.setPassword("4d7Q4p88");
            config.addDataSourceProperty("characterEncoding", "utf8");
            config.addDataSourceProperty("useUnicode", "true");
            config.addDataSourceProperty("cachePrepStmts", true);
            config.addDataSourceProperty("prepStmtCacheSize", 250);
            config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
            config.setAutoCommit(true);
            config.setMaximumPoolSize(20);
            config.setIdleTimeout(300000L);
            config.setConnectionTimeout(300000L);
            config.setLeakDetectionThreshold(300000L);
            config.setMaxLifetime(300000L);
            this.database = new HikariDataSource(config);
            System.out.println("Created MySQL connection");
        }
        catch (Exception e) {
            System.out.println("Error occured on creating MySQL connection: " + e.getMessage());
        }
    }
    
    public HikariDataSource getDatabase() {
        return this.database;
    }
    
    public void update(final String query) {
        try {
            final Connection connection = this.database.getConnection();
            try {
                final PreparedStatement ps = connection.prepareStatement(query);
                try {
                    ps.executeUpdate();
                    if (ps != null) {
                        ps.close();
                    }
                }
                catch (Throwable t) {
                    if (ps != null) {
                        try {
                            ps.close();
                        }
                        catch (Throwable t2) {
                            t.addSuppressed(t2);
                        }
                    }
                    throw t;
                }
                if (connection != null) {
                    connection.close();
                }
            }
            catch (Throwable t3) {
                if (connection != null) {
                    try {
                        connection.close();
                    }
                    catch (Throwable t4) {
                        t3.addSuppressed(t4);
                    }
                }
                throw t3;
            }
        }
        catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public ResultSet query(final String query) {
        try {
            final Statement stmt = this.database.getConnection().createStatement();
            final ResultSet rsge = stmt.executeQuery(query);
            return rsge;
        }
        catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
    
    public void close() {
        try {
            this.database.close();
        }
        catch (Exception e) {
            System.err.println("Error occured on closing connection: " + e.getMessage());
        }
    }
}
