package com.aacoin.dig;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.security.SignatureException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;


public class Utils {

    private static Lock lock = null;

    static {
        lock = new ReentrantLock();
    }

    /**
     * 判断是否为整数
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * <p>Description: 生成uuid </p>
     * @return
     * <p>Author:jmzhang/张际明, 16/09/20</p>
     */
    public static String generateUUID() {
        String uuid = null;
        try {
            lock.lock();
            uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        } finally {
            lock.unlock();
        }

        return uuid;
    }

    /**
     * <p>Description: HmacSHA1算法 </p>
     * @return
     * <p>Author:jmzhang/张际明, 16/09/20</p>
     */
    public static String calculateRFC2104HMAC(String data, String key) throws SignatureException{
        String result;
        try{
            final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
            // get an hmac_sha1 key from the raw key bytes
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(),HMAC_SHA1_ALGORITHM);

            // get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);

            // compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(data.getBytes("UTF-8"));

            // base64-encode the hmac
            result = Base64.encodeBase64String(rawHmac) ;
//			result = new sun.misc.BASE64Encoder().encode(rawHmac) ;

        }catch(Exception e){
            throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }

    /**
     * <p>Description: 实体对象转换treemap </p>
     * @return
     * <p>Author:jmzhang/张际明, 16/09/20</p>
     */
    public static TreeMap<String, Object> beanToMap(Object obj) throws Exception {
        TreeMap<String, Object> params = new TreeMap<String, Object>();

        try {
            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
            for (int i = 0; i < descriptors.length; i++) {
                String name = descriptors[i].getName();
                if (!StringUtils.equals(name, "class")) {
                    Object value = propertyUtilsBean.getNestedProperty(obj, name);
                    if(null != value) {
                        params.put(name, value);
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }

        return params;
    }

    public static Date getUTCTime() {
        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        int offset = calendar.get(Calendar.ZONE_OFFSET);
        calendar.add(Calendar.MILLISECOND, -offset);
        Date date = calendar.getTime();

        return date;
    }

    public static Date getUTCTimeAddDay(int day) {
//        Calendar calendar = Calendar.getInstance();
//        int offset = calendar.get(Calendar.ZONE_OFFSET);
//        calendar.add(Calendar.MILLISECOND, -offset);
//        calendar.add(Calendar.DAY_OF_MONTH, day);
//        Date date = calendar.getTime();
//        return date;


        return DateUtils.addDays(Utils.getUTCTime(), day);
    }

    public static String getUTCTimeAddDayStr(int day) {
        Date date = Utils.getUTCTimeAddDay(day);
        return DateFormatUtils.format(date, "yyyy-MM-dd'T'HH:mm:ss.'000Z'");
    }

    public static Date getLocalDateByTimeZone(String zoneId) {
        long utcTime = new Date().getTime();
        TimeZone timeZone = TimeZone.getTimeZone(zoneId);

//        Date localDate = Calendar.getInstance(timeZone).getTime();

        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.clear();
        calendar.setTimeInMillis(utcTime);
        int offset = timeZone.getRawOffset();
        calendar.add(Calendar.MILLISECOND, offset);
        calendar.setTimeZone(timeZone);
        Date localDate = calendar.getTime();

        return localDate;
    }

    public static String getLocalDateStringByTimeZone(String zoneId, Locale locale) {
        Date utcDate = Utils.getUTCTime();

        long utcTime = utcDate.getTime();

        TimeZone timeZone = TimeZone.getTimeZone(zoneId);
//        String localDateFormat = DateFormatUtils.format(utcDate, "yyyy-MM-dd'T'HH:mm:ssZZ", timeZone);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (z)", locale);
//        dateFormat.setTimeZone(timeZone);
//        String localDateFormat = dateFormat.format(utcDate);

        String localDateFormat = DateFormatUtils.format(utcDate, "EEE MMM dd yyyy HH:mm:ss 'GMT'Z (z)", timeZone, locale);
        System.out.println("本地化后时间("+zoneId+"): "+localDateFormat);

//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeZone(timeZone);
//        calendar.setTimeInMillis(utcTime);
//        int offset = calendar.get(Calendar.ZONE_OFFSET);
//        calendar.add(Calendar.MILLISECOND, offset);
//        Date localDate = calendar.getTime();
//
//        String localDateFormat = DateFormatUtils.format(localDate, "EEE MMM dd yyyy HH:mm:ss 'GMT'Z (z)", timeZone, locale);
//        System.out.println("本地化后时间("+zoneId+"): "+localDateFormat);

        return localDateFormat;
    }

    public static void main(String[] args) throws Exception {
        //http://fate.windada.com/cgi-bin/worldtime_gb?FUNC=ShowWoldTime
        //http://blog.csdn.net/l_serein/article/details/6271755

        //设置系统默认时区UTC
        System.out.println("设置当前系统默认时区: UTC");
        System.setProperty("user.timezone", "UTC");
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        //当前时间Date类型
        Date nowLocalDate = Utils.getLocalDateByTimeZone("Asia/Shanghai");
        System.out.println("当前时间Date类型: "+nowLocalDate);

        //获取utc时间
        Date utcDate = Utils.getUTCTime();
        System.out.println("当前UTC时间: "+DateFormatUtils.format(new Date(), "yyyy-MM-dd'T'HH:mm:ssZZ", TimeZone.getTimeZone("UTC")));

        //本地化时间America/New_York
        String zoneIdNew_York = "America/New_York";
        Utils.getLocalDateStringByTimeZone(zoneIdNew_York, Locale.US);

        //本地化时间Asia/Shanghai
        String zoneIdShanghai = "Asia/Shanghai";
        Utils.getLocalDateStringByTimeZone(zoneIdShanghai, Locale.CHINESE);

        //本地化时间Asia/Tokyo
        String zoneIdTokyo = "Asia/Tokyo";
        Utils.getLocalDateStringByTimeZone(zoneIdTokyo, Locale.JAPANESE);

        //本地化时间Asia/Seoul
        String zoneIdSeoul = "Asia/Seoul";
        Utils.getLocalDateStringByTimeZone(zoneIdSeoul, Locale.KOREA);

//        //本地化时间
//        String usDate = DateFormatUtils.format(utcDate, "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
//        System.out.println(usDate);
        //Thu Nov 24 2016 13:22:44 GMT+0800 (CST)
    }

    /**
     *
     * <br/>Description:获取ip
     * <p>Author:jmzhang/张际明</p>
     * @param request
     * @return
     */
    static public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equalsIgnoreCase(ip)?"127.0.0.1":ip;
    }

    public static Locale getLocale(String language) {
        Locale locale = null;

        switch(language) {
            case Constant.LANGUAGE_JUYUWANG_CN:
                locale = Locale.CHINA;
                break;
            case Constant.LANGUAGE_JUYUWANG:
                locale = Locale.CHINA;
                break;
            case Constant.LANGUAGE_LOCALHOST_CN:
                locale = Locale.CHINA;
                break;
            case Constant.LANGUAGE_LOCALHOST:
                locale = Locale.CHINA;
                break;
            case Constant.LANGUAGE_ZHONGGUO:
                locale = Locale.CHINA;
                break;
            case Constant.LANGUAGE_RIBEN:
                locale = Locale.JAPAN;
                break;
            case Constant.LANGUAGE_HANGUO:
                locale = Locale.KOREA;
                break;
            default:
                locale = Locale.US;
        }

        return locale;
    }
}


