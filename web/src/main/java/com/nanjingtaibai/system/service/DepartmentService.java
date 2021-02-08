package com.nanjingtaibai.system.service;

import com.nanjingtaibai.system.entity.Department;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyz
 * @since 2021-01-05
 */
public interface DepartmentService extends IService<Department> {
    List<Department> findDeptAndCount();

}
