/*
package com.xwcet.tools.core.web;

import com.xwcet.tools.core.entity.*;
import com.xwcet.tools.core.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired(required = false)
    private UserInfoRepository userRepository;

    @RequestMapping("getuser")
    public Object getUser(int id)
    {
        UserInfo userEntity = userRepository.findUserInfoById(id);
        ResultMsg resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), userEntity);
        return resultMsg;
    }

    @RequestMapping("getusers")
    public Object getUsers(String role, HttpServletRequest request, HttpServletResponse response)
    {
        response.setHeader("Authorization","Basic dGVzdDp0ZXN0");
        List<UserInfo> userEntities = userRepository.findUserInfoByRole(role);
        ResultMsg resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), userEntities);
        return resultMsg;
    }

    @Modifying
    @RequestMapping("adduser")
    public Object addUser(@RequestBody UserInfo userEntity)
    {
        userRepository.save(userEntity);
        ResultMsg resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), userEntity);
        return resultMsg;
    }

    @Modifying
    @RequestMapping("updateuser")
    public Object updateUser(@RequestBody UserInfo userEntity)
    {
        UserInfo user = userRepository.findUserInfoById(userEntity.getId());
        if (user != null)
        {
            user.setName(userEntity.getName());
            userRepository.save(user);
        }
        ResultMsg resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), null);
        return resultMsg;
    }

    @Modifying
    @RequestMapping("deleteuser")
    @Transactional
    public Object deleteUser(int id)
    {
        userRepository.delete(id);
        ResultMsg resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), null);
        return resultMsg;
    }

    @Autowired
    private Audience audience;

    @RequestMapping("getaudience")
    public Object getAudience()
    {
        ResultMsg resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), audience);
        return resultMsg;
    }

    @RequestMapping("globalexceptiontest")
    public Object globalExceptionTest(@Validated @RequestBody BeanValidation data)
    {
        ResultMsg resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), data);
        return resultMsg;
    }

}*/
