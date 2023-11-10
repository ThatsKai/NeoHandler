package it.thatskai.litehandlervelocity.database;

import it.thatskai.litehandlervelocity.database.tables.LogsTable;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLProvider {

    private final String driver, host, database, username, password;

    @Getter
    private final LogsTable serverTable = new LogsTable(this);

    @Getter
    private Connection connection;

    public SQLProvider(String driver,String host, String database, String username, String password){
        this.driver = driver;
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public boolean connect(){
        try {
            if (connection != null && !connection.isClosed()) return false;
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                Bukkit.getLogger().severe("Cannot find driver class "+driver);
                return false;
            }

            String[] driverSplit = driver.split("\\.");
            String var = driverSplit[1];

            connection = DriverManager.getConnection("jdbc:"+var+"://" + host + "/" + database + "?characterEncoding=UTF-8", username, password);
            serverTable.createTable();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @SneakyThrows
    public void disconnect(){
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

}
