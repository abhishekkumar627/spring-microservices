package com.abhishek.springbootdemo.service;

import com.abhishek.springbootdemo.model.Employee;

import java.util.List;

public interface EmployeeService {

    Employee save(Employee employee);

    List<Employee> getEmployees();

    Employee getEmployeeById(String id);

    Employee deleteEmployeeById(String id);
}
