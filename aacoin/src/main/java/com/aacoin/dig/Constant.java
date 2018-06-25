package com.aacoin.dig;

public class Constant {

    public final static String APPLICATION_JSON_UTF8 = "application/json;charset=utf-8";
    public final static String DATE_PATTERN_NORMAL = "yyyy-MM-dd HH:mm:ss";
    public final static String SYSTEM_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";
    public final static String ENCODING = "utf-8";
    public final static String APPLICATION_TEXT = "text/plain";

    //http头信息,api-gateway验证用
    public final static String HEADER_PUBLIC_KEY = "publickey";
    public final static String HEADER_SIGNATURE_KEY = "signature";
    public final static String HEADER_TIMESTAMP_KEY = "timestamp";

    //http头信息,api-gateway签名，权限控制用
    public final static String HEADER_APPKEY_KEY = "appKey";
    public final static String HEADER_TOKEN_KEY = "token";
    public final static String HEADER_SESSION_KEY = "session";

    /**
     * 四大分类
     **/
    public static final Integer CONTENT_APP_TYPE = 1;
    public static final Integer CONTENT_IMG_TYPE = 2;
    public static final Integer CONTENT_SDK_TYPE = 3;
    public static final Integer CONTENT_VIDEO_TYPE = 4;

    public static final String LANGUAGE_JUYUWANG_CN = "局域网";
    public static final String LANGUAGE_JUYUWANG = "juyuwang";
    public static final String LANGUAGE_LOCALHOST_CN = "本机地址";
    public static final String LANGUAGE_LOCALHOST = "benjidizhi";
    public static final String LANGUAGE_ZHONGGUO = "zhongguo";
    public static final String LANGUAGE_RIBEN = "riben";
    public static final String LANGUAGE_HANGUO = "hanguo";
    public static final String LANGUAGE_MEIGUO = "meiguo";

    public static final String BASE_PATH = "/api/*";

    //国家
    public static final Integer COUNTRY_LAN = 0;
    public static final Integer COUNTRY_CN = 1;
    public static final Integer COUNTRY_US = 2;
    public static final Integer COUNTRY_JP = 3;
    public static final Integer COUNTRY_KOR = 4;

    //云服务商
    public static final String LAN = "LAN";
    public static final String CLOUD_ALI = "ALI";
    public static final String CLOUD_AWS = "AWS";

    /**
     * 字典表type常量
     */
    static public class DICTIONARY {
        public static final String CLOUD = "cloud";
        public static final String COUNTRY = "country";
        public static final String DUBBING = "dubbing";
        public static final String FORMATTYPE = "formattype";
        public static final String RECOMMENDSORT = "recommendSort";
        public static final String RELEASETIME = "releasetime";
        public static final String STEREOTYPE = "stereotype";
        public static final String VIDEOSORT = "videoSort";
        public static final String VIDEOTYPE = "videoType";
    }

    /* 分页每页最大值 */
    public static final Integer PAGE_NUM_MAX = 50;

}
