package com.nanjingtaibai.system.controller;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nanjingtaibai.response.Result;
import com.nanjingtaibai.system.entity.Mcgs;
import com.nanjingtaibai.system.entity.OpcItemValue;
import com.nanjingtaibai.system.entity.User;
import com.nanjingtaibai.system.entity.Yonghu;
import com.nanjingtaibai.system.mapper.McgsMapper;
import com.nanjingtaibai.system.mapper.OpcItemValueMapper;
import com.nanjingtaibai.system.mapper.YonghuMapper;
import com.nanjingtaibai.system.service.AliOssService;
import com.nanjingtaibai.system.service.UserService;
import com.nanjingtaibai.vo.UserVO;
import com.sun.xml.internal.bind.v2.model.core.PropertyInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author zyz
 * @since 2020-12-18
 */
@RestController
@RequestMapping("/user/")
@EnableScheduling
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AliOssService aliOssService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private YonghuMapper yonghuMapper;
    @Autowired
    private OpcItemValueMapper opcItemValueMapper;
    @Autowired
    private McgsMapper mcgsMapper;
    /**
     * 分页查询用户列表
     * @return
     */
    @GetMapping("findUserList")
    public Result findUserList(@RequestParam(required = true,defaultValue = "1") Integer current,
                               @RequestParam(required = true,defaultValue = "7") Integer size){
        Page<User> page=new Page<>(current,size);
        Page<User> userPage = userService.page(page);
        long total = userPage.getTotal();
        List<User> records = userPage.getRecords();
        return Result.ok().data("total",total).data("records",records);
    }

    @PostMapping("findUserPage")
    public Result findUserPage(@RequestParam(required = true,defaultValue = "1") Integer current,
                               @RequestParam(required = true,defaultValue = "7") Integer size,
                               @RequestBody UserVO userVO){
        Page<User> page=new Page<>(current,size);
        QueryWrapper<User> wrapper = getWrapper(userVO);
        IPage<User> userPage = userService.findUserPage(page,wrapper);
        long total = userPage.getTotal();
        List<User> records = userPage.getRecords();
        return Result.ok().data("total",total).data("records",records);
    }

    private QueryWrapper<User> getWrapper(UserVO userVO) {
        QueryWrapper<User> queryWrapper =new QueryWrapper<>();
        if(userVO!=null){
            if(!StringUtils.isEmpty(userVO.getDepartmentId())){
                queryWrapper.eq("department_id",userVO.getDepartmentId());
            }
            if(!StringUtils.isEmpty(userVO.getUsername())){
                queryWrapper.like("username",userVO.getUsername());
            }
            if(!StringUtils.isEmpty(userVO.getEmail())){
                queryWrapper.eq("email",userVO.getEmail());
            }
            if(!StringUtils.isEmpty(userVO.getSex())){
                queryWrapper.eq("sex",userVO.getSex());
            }
            if(!StringUtils.isEmpty(userVO.getNickname())){
                queryWrapper.eq("nickname",userVO.getNickname());
            }
        }
        return queryWrapper;
    }
    @ApiOperation(value = "添加用户" ,notes = "添加用户信息")
    @PostMapping("addUser")
    public Result  addUser(@RequestBody User user){
        try {
            userService.addUser(user);
            return Result.ok();

        }catch (Exception e)
        {
            return  Result.error();
        }
    }

    @ApiOperation(value = "获取用户信息" ,notes = "获取用户详细信息")
    @PostMapping("getUserInfo")
    public Result  getUserInfo(@RequestParam(required = true) Integer id){
        try {
            User userInfo = userService.getUserInfo(id);
            return Result.ok().data("userInfo",userInfo);

        }catch (Exception e)
        {
            return  Result.error();
        }
    }


}

