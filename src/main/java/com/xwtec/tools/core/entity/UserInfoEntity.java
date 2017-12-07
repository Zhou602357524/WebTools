package com.xwtec.tools.core.entity;

/**
 一、数据库连接

 IP: 10.105.123.181/182/183都行
 用户：ZTAPP
 实例名：ZTAPP
 端口：1521
 密码：5`#%h8F#



 二、匹配语句

 1、区分安卓与IOS。
 （1）安卓用户电话号码：
 select a.phone from t_all_user_detail a where a.last_source='android' and exists
 (select b.phone from t_push_msgid b where b.phone=a.phone);
 （2）IOS用户电话号码：
 select a.phone from t_all_user_detail a where a.last_source='iphone' and exists
 (select b.phone from t_push_msgid b where b.phone=a.phone);
 上面的t_push_msgid是指：中间t_push_msgid，中间t_push_msgid要创建，创建什么t_push_msgid，叫什么你们自己定，创建后上传推送的电话号码进行区分。

 2、提MSGID
 （1）安卓用户的电话号码和MSGID：
 select a.phonea.msgid from t_all_user_detail a where a.last_source='android' and a.msgid<>'null' and a.msgid<>'iphone' and
 a.msgid<>'android' and a.msgid is not null and exists (select b.phone from t_push_msgid b where b.phone=a.phone);
 （1）IOS用户的电话号码和MSGID：
 select a.phonea.msgid from t_all_user_detail a where a.last_source='iphone' and a.msgid<>'null' and a.msgid<>'iphone' and
 a.msgid<>'android' and a.msgid is not null and exists (select b.phone from t_push_msgid b where b.phone=a.phone);
 上面的t_push_msgid是指：中间t_push_msgid，中间t_push_msgid要创建，创建什么t_push_msgid，叫什么你们自己定，创建后上传推送的电话号码进行区分。


 三、自定义拆分号码数量（没这功能，反正我不晓得哟）
 * \* Created with IntelliJ IDEA.
 * \* User: zc
 * \* Date: 2017/12/7 0007
 * \* Time: 下午 5:03
 * \* Description:
 * \
 */

public class UserInfoEntity {

    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
