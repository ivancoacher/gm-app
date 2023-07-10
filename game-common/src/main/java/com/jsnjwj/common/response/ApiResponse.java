package com.jsnjwj.common.response;

import com.jsnjwj.common.enums.ResponseEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ApiResponse<T> implements Serializable {

	private int code = 20000;

	private String message = "成功";

	private T data;

	// 私有构造器
	public ApiResponse() {
	}

	// 通用成功
	public static ApiResponse success() {
		return success(null);
	}

	public static ApiResponse success(Object data) {
		ApiResponse ApiResponse = new ApiResponse<>();
		ApiResponse.setCode(ResponseEnum.SUCESS.getResultCode());
		ApiResponse.setMessage(ResponseEnum.SUCESS.getResultMsg());
		ApiResponse.setData(data);

		return ApiResponse;
	}

	// 通用失败
	public static ApiResponse error() {
		return error(null);
	}

	public static ApiResponse error(Object data) {
		ApiResponse ApiResponse = new ApiResponse<>();
		ApiResponse.setCode(ResponseEnum.ERROR.getResultCode());
		ApiResponse.setMessage(ResponseEnum.ERROR.getResultMsg());
		ApiResponse.setData(data);

		return ApiResponse;
	}

	// 自定义参数,链式编程
	public ApiResponse data(T data) {
		this.setData(data);
		return this;
	}

	public ApiResponse code(int code) {
		this.setCode(code);
		return this;
	}

	public ApiResponse msg(String msg) {
		this.setMessage(msg);
		return this;
	}

}
