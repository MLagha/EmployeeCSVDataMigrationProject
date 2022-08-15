package com.sparta.ml;

import com.sparta.ml.dao.EmployeeDTO;

import java.util.List;

public interface Filters {
    public List<EmployeeDTO> filter (List<EmployeeDTO> records);
}
