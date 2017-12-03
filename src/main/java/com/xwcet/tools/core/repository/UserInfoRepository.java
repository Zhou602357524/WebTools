package com.xwcet.tools.core.repository;

import com.xwcet.tools.core.entity.UserInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserInfoRepository extends CrudRepository<UserInfo, Integer> {
    UserInfo findUserInfoById(int id);
    List<UserInfo> findUserInfoByRole(String role);

    @Query(value = "select * from t_user limit ?1", nativeQuery =true)
    List<UserInfo> findAllUsersByCount(int count);

    UserInfo findUserInfoByName(String name);
}
