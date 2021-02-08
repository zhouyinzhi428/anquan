package com.nanjingtaibai.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nanjingtaibai.system.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author zyz
 * @since 2020-12-18
 */
public interface UserService extends IService<User> {
    IPage<User> findUserPage(Page<User> page,  QueryWrapper<User> wrapper);

    /**
     * 添加用户
     * @param user
     */
    void addUser(User user);
    User getUserInfo(Integer id);

}
