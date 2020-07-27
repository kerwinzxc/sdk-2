package com.game.sdk.domain;

public class LoginErrorMsg {

	public int code;// 登录失败的状态码
	public String msg;// 登录失败的消息提示

	public LoginErrorMsg(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
}
