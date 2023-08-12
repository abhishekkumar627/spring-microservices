package com.abhishek.springbootdemo.service;

import com.abhishek.springbootdemo.entity.EmployeeEntity;
import com.abhishek.springbootdemo.model.Employee;
import com.abhishek.springbootdemo.repository.EmployeeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeV2ServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository repository;

    @Override
    public Employee save(Employee employee) {
        if(Objects.isNull(employee.getEmployeeId()) || !StringUtils.hasLength(employee.getEmployeeId())){
            employee.setEmployeeId(UUID.randomUUID().toString());
        }
        EmployeeEntity entity = new EmployeeEntity();
        BeanUtils.copyProperties(employee,entity);
        repository.save(entity);
        return employee;
    }

    @Override
    public List<Employee> getEmployees() {
        List<EmployeeEntity> employeeEntityList= repository.findAll();
        List<Employee> employeeList = employeeEntityList.stream().
                                        map(employeeEntity -> {
                                            Employee employee = new Employee();
                                            BeanUtils.copyProperties(employeeEntity,employee);
                                            return employee;
                                        }).collect(Collectors.toList());
        return employeeList;
    }

    @Override
    public Employee getEmployeeById(String id) {
        EmployeeEntity employeeEntity = repository.findById(id).get();
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeEntity,employee);
        return employee;
    }

    @Override
    public Employee deleteEmployeeById(String id) {
        Employee employee = getEmployeeById(id);
        repository.deleteById(id);
        return employee;
    }
}
