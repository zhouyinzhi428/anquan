package com.nanjingtaibai.system.controller;


import com.nanjingtaibai.handle.BusinessException;
import com.nanjingtaibai.response.Result;
import com.nanjingtaibai.response.ResultCode;
import com.nanjingtaibai.system.entity.Department;
import com.nanjingtaibai.system.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zyz
 * @since 2021-01-05
 */
@Api(value = "部门管理")
@RestController
@RequestMapping("/department/")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;
    @ApiOperation(value = "查询部门及人数",notes = "从部门表中分组查询用户人数")
    @GetMapping("findDeptAndCount")
    public Result findDeptAndCount(){
        List<Department> departments = departmentService.findDeptAndCount();
        if(departments.size()==0){
            throw new BusinessException(ResultCode.DEPARTMENT_NOT_EXIST.getCode(),
                    ResultCode.DEPARTMENT_NOT_EXIST.getMessage());
        }
        return Result.ok().data("departments",departments);
    }

}

