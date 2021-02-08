package com.nanjingtaibai.system.mapper;

import com.nanjingtaibai.system.entity.Department;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zyz
 * @since 2021-01-05
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    List<Department> findDeptAndCount();
}
