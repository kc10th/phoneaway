package com.rg.phone_away.weibo;

public abstract class ConstantsOfWeibo {

	// 新浪官方申请的正式 APP_KEY
	public static final String APP_KEY = "565174806";

	public static final String APP_SECRET = "2293aa6ce61511f0030db8a31a9d1f61";
	// App设定的回调地址 REDIRECT_URL
	public static final String REDIRECT_URL = "http://www.sina.com";

	public static final String AUTH_APIURL = "https://api.weibo.com/oauth2/authorize";

	public static final String AUTH_URL = ConstantsOfWeibo.AUTH_APIURL
			+ "?client_id=" + ConstantsOfWeibo.APP_KEY + "&redirect_uri="
			+ ConstantsOfWeibo.REDIRECT_URL
			+ "&response_type=code&display=mobile";

	// 新浪官方设定的scope权限，用逗号分隔
	public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";

	public static final String CLIENT_ID = "client_id";
	public static final String RESPONSE_TYPE = "response_type";
	public static final String USER_REDIRECT_URL = "redirect_uri";
	public static final String DISPLAY = "display";
	public static final String USER_SCOPE = "scope";
	public static final String PACKAGE_NAME = "packagename";
	public static final String KEY_HASH = "key_hash";

	public abstract class ResutlCode {
		public static final int FromWebViewActivity_Success = 1;
		public static final int FromWebViewActivity_Fail = -1;
	}

	public abstract class RequestCode {
		public static final int FromMainActivity = 1;
	}
}
