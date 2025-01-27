package org.example.dao;

import org.example.model.CalcModel;

import java.util.List;

public interface CalcModelDao {
    void save(CalcModel calcModel);
    void saveAll(List<CalcModel> calcModels);
    List<CalcModel> findAllSortedByArgument();
    void clearAll();
}
