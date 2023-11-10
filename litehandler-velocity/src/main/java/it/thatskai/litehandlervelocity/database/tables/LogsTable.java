package it.thatskai.litehandlervelocity.database.tables;

import it.thatskai.litehandlervelocity.database.SQLProvider;
import lombok.SneakyThrows;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LogsTable {

    private final SQLProvider provider;

    public LogsTable(SQLProvider provider){
        this.provider = provider;
    }

    @SneakyThrows
    public CompletableFuture<Void> createTable() {
        return CompletableFuture.runAsync(() -> {
            try {
                Statement statement = provider.getConnection().createStatement();
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS aclogs (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "player VARCHAR(255)," +
                        "log VARCHAR(255)" +
                        ")");
            } catch (SQLException ignored) {}
        });
    }
    public CompletableFuture<Boolean> addPlayer(String playerName){
        return CompletableFuture.supplyAsync(()->{
            try {
                PreparedStatement statement = provider.getConnection().prepareStatement("INSERT INTO aclogs (player, log) VALUES (?, ?)");
                statement.setString(1, playerName);
                statement.setString(2, "");
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    public void addLog(String playerName, String log){
        CompletableFuture.runAsync(()->{
            String query = "INSERT INTO aclogs (player, log) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = provider.getConnection().prepareStatement(query)) {
                preparedStatement.setString(1, playerName);
                preparedStatement.setString(2, log);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
    public CompletableFuture<List<String>> getAllLogs(String playerName) {
        return CompletableFuture.supplyAsync(()->{
            provider.connect();
            List<String> logs = new ArrayList<>();
            String query = "SELECT log FROM aclogs WHERE player = ?";
            try (PreparedStatement preparedStatement = provider.getConnection().prepareStatement(query)) {
                preparedStatement.setString(1, playerName);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String value = resultSet.getString("log");
                    logs.add(value);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return logs;
        });
    }

}
