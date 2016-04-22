package com.xuhongchuan.axenote.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 格式验证
 * Created by 网络
 */
public class FormatUtils {

    /**
     * 验证手机号码格式
     *
     * @param phoneNum
     * @return
     */
    public static boolean isMobileNumber(String phoneNum) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[0-9])|(17[0-9])|(18[0-9])|(14[0-9]))\\d{8}$");
        return p.matcher(phoneNum).matches();
    }

    /**
     * 验证邮箱格式
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
