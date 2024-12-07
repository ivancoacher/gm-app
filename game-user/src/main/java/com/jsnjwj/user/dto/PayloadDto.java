package com.jsnjwj.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class PayloadDto {

    private String sub;

    private Long iat;

    private Long exp;

    private String jti;

    private String username;

    private List<String> authorities;

}
