package com.nanjingtaibai.system.service.impl;

import com.nanjingtaibai.system.entity.User;
import com.nanjingtaibai.system.mapper.UserMapper;
import com.nanjingtaibai.system.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
