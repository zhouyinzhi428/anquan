package com.nanjingtaibai.system.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nanjingtaibai.response.Result;
import com.nanjingtaibai.system.entity.User;
import com.nanjingtaibai.system.service.UserService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
public class UserController {
    @Autowired
    private UserService userService;
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
}

