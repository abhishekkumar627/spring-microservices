package com.abhishek.springbootdemo.service;

import com.abhishek.springbootdemo.error.EmployeeNotFoundException;
import com.abhishek.springbootdemo.model.Employee;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    List<Employee> employeeList = new ArrayList<>();
    @Override
    public Employee save(Employee employee) {
        if(Objects.isNull(employee.getEmployeeId()) || !StringUtils.hasLength(employee.getEmployeeId())){
            employee.setEmployeeId(UUID.randomUUID().toString());
        }
        employeeList.add(employee);
        return employee;
    }

    @Override
    public List<Employee> getEmployees() {
        return employeeList;
    }

    @Override
    public Employee getEmployeeById(String id) {
        return employeeList.stream().filter(employee -> employee.getEmployeeId().equals(id))
                .findFirst()
                //.orElseThrow(()-> new RuntimeException("Employee Not Found with Id "+ id));
                .orElseThrow(()-> new EmployeeNotFoundException("Employee Not Found with Id "+ id));
    }

    @Override
    public Employee deleteEmployeeById(String id) {
        Employee emp= employeeList.stream().filter(employee -> employee.getEmployeeId().equals(id))
                .findFirst()
                //.orElseThrow(()-> new RuntimeException("Employee Not Found with Id "+ id));
                .orElseThrow(()-> new EmployeeNotFoundException("Employee Not Found with Id "+ id));
        employeeList.remove(emp);
        return emp;
    }
}
