package com.xwtec.tools.core.repository;

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

    void insertPhoneNumbers(List<String> numbers);
    @Select("TRUNCATE TABLE t_push_msgid")
    void truncate();

}
