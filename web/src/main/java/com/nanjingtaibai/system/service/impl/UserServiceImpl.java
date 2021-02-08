package com.nanjingtaibai.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nanjingtaibai.handle.BusinessException;
import com.nanjingtaibai.response.ResultCode;
import com.nanjingtaibai.system.entity.Department;
import com.nanjingtaibai.system.entity.User;
import com.nanjingtaibai.system.enums.UserStatusEnum;
import com.nanjingtaibai.system.enums.UserTypeEnum;
import com.nanjingtaibai.system.mapper.DepartmentMapper;
import com.nanjingtaibai.system.mapper.UserMapper;
import com.nanjingtaibai.system.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author zyz
 * @since 2020-12-18
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public IPage<User> findUserPage(Page<User> page, QueryWrapper<User> wrapper) {
        return this.baseMapper.findUserPage(page,wrapper);
    }

    /**
     * 添加用户
     *
     * @param user
     */
    @Override
    public void addUser(User user) {
        //判断用户名是否重复
        String username = user.getUsername();
        //获取部门ID
        Long departmentId = user.getDepartmentId();
        QueryWrapper<User> wrapper=new QueryWrapper<>();
        wrapper.eq("username",username);
        Integer count = this.baseMapper.selectCount(wrapper);
        if(count!=0)
        {
            throw new BusinessException(ResultCode.USER_ACCOUNT_ALREADY_EXIST.getCode(),
                    ResultCode.USER_ACCOUNT_ALREADY_EXIST.getMessage());
        }
        Department department = departmentMapper.selectById(departmentId);
        if(department==null){
            throw new BusinessException(ResultCode.DEPARTMENT_NOT_EXIST.getCode(),
                    ResultCode.DEPARTMENT_NOT_EXIST.getMessage());
        }
        String salt= UUID.randomUUID().toString().substring(0,32);
        user.setSalt(salt);
//        user.setCreateTime(new Date());
//        user.setModifiedTime(new Date());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setType(UserTypeEnum.STEM_USER.getTypeCode());
        user.setStatus(UserStatusEnum.AVAILABLE.getStatusCode());
        user.setDeleted(false);

        this.baseMapper.insert(user);

    }

    @Override
    public User getUserInfo(Integer id) {
        return this.baseMapper.selectById(id);
    }
}
