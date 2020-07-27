package com.game.sdk.listener;

import com.game.sdk.domain.LoginErrorMsg;
import com.game.sdk.domain.LogincallBack;
import com.game.sdk.domain.NotProguard;
import com.game.sdk.domain.result.UserResultBean;

@NotProguard
public interface OnLoginListener {
	/**
	 * 成功登录后的回调
	 * @param userResultBean
	 */
	@NotProguard
	void loginSuccess(UserResultBean userResultBean);

	/**
	 * 登录失败的回调 有可能是用户名与密码不正确，也有可能是服务端临时出问题
	 * @param userResultBean 登录失败时返回的提示
	 */
	@NotProguard
	void loginError(UserResultBean userResultBean);

	/**
	 * 实名认证的回调 （不管是否认证完成）
	 */
	@NotProguard
	void loginAuthenticOver();

}
