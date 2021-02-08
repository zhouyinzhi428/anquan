package com.nanjingtaibai.system.service.impl;

import com.nanjingtaibai.system.entity.Department;
import com.nanjingtaibai.system.mapper.DepartmentMapper;
import com.nanjingtaibai.system.service.DepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zyz
 * @since 2021-01-05
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Override
    public List<Department> findDeptAndCount() {
        return this.baseMapper.findDeptAndCount();
    }
}
