package org.example.dao;

import lombok.AllArgsConstructor;
import org.example.model.CalcModel;

import java.sql.*;
import java.util.List;

@AllArgsConstructor
public class JdbcCalcModelDao implements CalcModelDao {
    private static final String SQL_SAVE = """
            INSERT INTO calc_model VALUES (?,?);
            """;
    private static final String SQL_FIND_ALL = """
            SELECT * FROM calc_model ORDER BY argument ASC;
            """;

    private static final String SQL_DELETE_ALL = """
            DELETE FROM calc_model;
            """;

    private JdbcCalcModelMapper mapper;

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/cacheDB", "admin", "admin");
    }

    @Override
    public void save(CalcModel calcModel) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SQL_SAVE);
            statement.setLong(1, calcModel.getArgument());
            statement.setLong(2, calcModel.getResult());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveAll(List<CalcModel> calcModels) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SQL_SAVE);
            for (CalcModel calcModel : calcModels) {
                statement.setLong(1, calcModel.getArgument());
                statement.setLong(2, calcModel.getResult());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CalcModel> findAllSortedByArgument() {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL);
            ResultSet queryResult = statement.executeQuery();
            return mapper.mapList(queryResult);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearAll() {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE_ALL);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
