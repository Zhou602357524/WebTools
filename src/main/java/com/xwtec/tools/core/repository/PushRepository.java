package com.xwtec.tools.core.repository;

import com.xwtec.tools.core.entity.UserInfoEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zc
 * \* Date: 2017/12/7 0007
 * \* Time: 下午 5:41
 * \* Description:
 * \
 */
public interface PushRepository {

    void insertPhoneNumbers(List<UserInfoEntity> numbers);
    @Select("TRUNCATE TABLE t_push_msgid")
    void truncate();
    @Select("select a.phone from t_all_user_detail a where a.last_source='android' and exists " +
            "(select d.phone from (select c.phone from (select b.phone,rownum rn from t_push_msgid b) c where c.rn > #{begin} and c.rn <= #{end}) d where d.phone = a.phone)")
    @Results({
            @Result(property = "phone",column = "phone")
    })
    List<UserInfoEntity> queryAndroidPhone(@Param("begin") int begin,@Param("end") int end);
    @Select("select a.phone from t_all_user_detail a where a.last_source='iphone' and exists " +
            "(select d.phone from (select c.phone from (select b.phone,rownum rn from t_push_msgid b) c where c.rn > #{begin} and c.rn <= #{end}) d where d.phone = a.phone)")
    @Results({
            @Result(property = "phone",  column = "phone", javaType = String.class),
    })
    List<UserInfoEntity> queryIOSPhone(@Param("begin") int begin,@Param("end") int end);
    @Select("select a.phone,a.msgid from t_all_user_detail a where a.last_source='android' and a.msgid<>'null' and a.msgid<>'iphone' and \n" +
            "a.msgid<>'android' and a.msgid is not null and exists " +
            "(select d.phone from (select c.phone from (select b.phone,rownum rn from t_push_msgid b) c where c.rn > #{begin} and c.rn <= #{end}) d where d.phone = a.phone)")
    @Results({
            @Result(property = "phone",column = "phone"),
            @Result(property = "msgid",column = "msgid")
    })
    List<UserInfoEntity> queryAndroidPhoneAndMsgid(@Param("begin") int begin,@Param("end") int end);
    @Select("select a.phone,a.msgid from t_all_user_detail a where a.last_source='iphone' and a.msgid<>'null' and a.msgid<>'iphone' and \n" +
            "a.msgid<>'android' and a.msgid is not null and exists " +
            "(select d.phone from (select c.phone from (select b.phone,rownum rn from t_push_msgid b) c where c.rn > #{begin} and c.rn <= #{end}) d where d.phone = a.phone)")
    @Results({
            @Result(property = "phone",column = "phone"),
            @Result(property = "msgid",column = "msgid")
    })
    List<UserInfoEntity> queryIOSPhoneAndMsgid(@Param("begin") int begin,@Param("end") int end);
    @Select("select a.msgid from t_all_user_detail a where a.last_source='iphone' and a.msgid<>'null' and a.msgid<>'iphone' and \n" +
            "a.msgid<>'android' and a.msgid is not null and exists " +
            "(select d.phone from (select c.phone from (select b.phone,rownum rn from t_push_msgid b) c where c.rn > #{begin} and c.rn <= #{end}) d where d.phone = a.phone)")
    @Results({
            @Result(property = "msgid",column = "msgid")
    })
    List<UserInfoEntity> queryIOSMsgid(@Param("begin") int begin,@Param("end") int end);
    @Select("select a.msgid from t_all_user_detail a where a.last_source='android' and a.msgid<>'null' and a.msgid<>'iphone' and \n" +
            "a.msgid<>'android' and a.msgid is not null and exists " +
            "(select d.phone from (select c.phone from (select b.phone,rownum rn from t_push_msgid b) c where c.rn > #{begin} and c.rn <= #{end}) d where d.phone = a.phone)")
    @Results({
            @Result(property = "msgid",column = "msgid")
    })
    List<UserInfoEntity> queryAndroidMsgid(@Param("begin") int begin,@Param("end") int end);
    @Select("select count(1) from t_push_msgid")
    int selectCount();

}
