package com.nanjingtaibai.test.generator;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nanjingtaibai.AnQuanApplication;
import com.nanjingtaibai.system.entity.OpcItemValue;
import com.nanjingtaibai.system.entity.OssEntity;

import com.nanjingtaibai.system.mapper.OpcItemValueMapper;
import com.nanjingtaibai.system.service.AliOssService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest(classes = AnQuanApplication.class)
public class OssTest {
    @Autowired
    private OssEntity ossEntity;
    @Autowired
    private AliOssService aliOssService;
    @Autowired
    private OpcItemValueMapper opcItemValueMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Test
    public void ContextLoads() throws IOException {
        //$2a$10$sBD9Y9YJPPGPbaYzQ0p3YeDmw81PPBwykhpns7XLh3.c0J/uSsAXe
        //$2a$10$Q5.P0A7O/NR4mvy0k6ZKd./HSgIFn05vdsKT7Bmm.rOypsGvXQtMa
        //aliOssService.doesObjectExist("2021/01/14/名册（截止2020年2月份）_1de396972e8b4d39aa8149a27fd3f7d3.xlsx");
        List<String> list = new ArrayList<>();
        list.add("F_SO2");
        list.add("F_NO");
        list.add("F_SO2B");
        list.add("F_NOB");
        list.add("TE10_013");
        list.add("TE10_014");
        QueryWrapper<OpcItemValue> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("POINT_NAME", list);
        List<OpcItemValue> opcItemValueList = opcItemValueMapper.selectList(queryWrapper);
        for (OpcItemValue opcItemValue : opcItemValueList) {
            System.out.println(opcItemValue.getFvalue() + opcItemValue.getPointname());
        }

        Optional<OpcItemValue> cartOptional = opcItemValueList.stream().filter(item -> item.getPointname().equals("TE10_013")).findFirst();
        if (cartOptional.isPresent()) {
            // 存在
            OpcItemValue opcItemValue = cartOptional.get();
            System.out.println(opcItemValue.getFvalue() + opcItemValue.getPointname());
        } else {
            // 不存在

        }
    }

}
