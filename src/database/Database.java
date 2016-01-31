package database;

import constants.ConstantsDb;
import log.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    private static final Logger logger = Logger.getLogger(Log.class.getName());
    private int lastInsertId = -1;

    protected Connection connection;
    protected Statement statement;
    protected ResultSet resultSet;

    private void connect() {
        try {
            connection = DriverManager.getConnection(ConstantsDb.CONNECTION_URL);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error connecting to DB", e);
        }
    }

    protected void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error closing resultSet, statement or connection");
        }
    }

    private boolean isConnectionPrepared() {
        boolean result = false;

        try {
            connect();

            if (connection != null) {
                statement = connection.createStatement();
                result = true;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error preparing connection", e);
        }

        return result;
    }

    public int getLastInsertId() {
        return lastInsertId;
    }

    public List<Map<String, String>> executeSelect(String sql) {
        if (!isConnectionPrepared() || sql == null || sql.isEmpty()) {
            logger.log(
                    Level.WARNING,
                    "Cannot execute select query because of connection is not prepared or sql is empty"
            );
            return null;
        }

        List<Map<String, String>> result = new ArrayList<>();

        try {
            resultSet = statement.executeQuery(sql);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int numberColumns = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, String> row = new HashMap<>();
                int i = 1;

                while (i <= numberColumns) {
                    row.put(metaData.getColumnName(i), resultSet.getString(i++));
                }

                result.add(row);
            }
        } catch (SQLException e){
            logger.log(Level.SEVERE, "Error executing query: " + sql, e);
        } finally {
            close();
        }

        return result;
    }

    public boolean executeQuery(String sql, boolean returnGeneratedKeys) {
        if (!isConnectionPrepared() || sql == null || sql.isEmpty()) {
            logger.log(
                    Level.WARNING,
                    "Cannot execute query because of connection is not prepared or sql is empty"
            );
            return false;
        }

        boolean result = true;

        try {
            if (returnGeneratedKeys) {
                statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                resultSet = statement.getGeneratedKeys();

                while (resultSet.next()) {
                    lastInsertId = resultSet.getInt(1);
                }
            } else {
                statement.executeUpdate(sql);
            }
        } catch (SQLException e) {
            result = false;
            logger.log(Level.SEVERE, "Error executing query: " + sql, e);
        } finally {
            close();
        }

        return result;
    }

}
