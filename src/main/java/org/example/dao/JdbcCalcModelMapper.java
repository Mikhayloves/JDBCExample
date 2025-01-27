package org.example.dao;

import org.example.model.CalcModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcCalcModelMapper {
    public List<CalcModel> mapList(ResultSet resultSet) throws SQLException {
        List<CalcModel> calcModels = new ArrayList<>();
        while (resultSet.next()) {
            calcModels.add(mapOne(resultSet, false));
        }
        return calcModels;
    }

    public CalcModel mapOne(ResultSet resultSet, boolean needToCallNext) throws SQLException {
        boolean hasNext = true;
        if (needToCallNext) {
            hasNext = resultSet.next();
        }
        if (!hasNext) {
            return null;
        }
        CalcModel calcModel = new CalcModel();
        calcModel.setArgument(resultSet.getInt("argument"));
        calcModel.setResult(resultSet.getInt("result"));
        return calcModel;
    }
}
