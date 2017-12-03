package com.xwcet.tools.core.utils.cookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * cookie工具类
 */
public class CookieUtils {


    public static String getName(HttpServletRequest request, String cookieName) {

        Cookie[] cookies = request.getCookies();

        String name = null;

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName))
                name = cookie.getName();
        }

        return name;
    }

    public static String getValue(HttpServletRequest request, String cookieName) {

        String value = null;
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName))
                value = cookie.getValue();
        }
        return value;
    }

}
