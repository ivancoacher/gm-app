package com.jsnjwj.user.reponse;

import lombok.Data;

import java.util.List;

@Data
public class UserInfoResponse {

	private Long userId;

	private String name;

	private String avatar;

	private List<String> roles;

	private String info;

}
