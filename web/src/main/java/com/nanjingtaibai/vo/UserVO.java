package com.nanjingtaibai.vo;

import lombok.Data;

@Data
public class UserVO {
    private String username;
    private String nickname;
    private String email;
    private Integer sex;
    private Long departmentId;
}
