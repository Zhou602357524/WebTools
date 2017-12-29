package com.xwtec.tools.core.service.pushtools;

import com.xwtec.tools.core.entity.PushParams;
import com.xwtec.tools.core.entity.ResultMsg;
import com.xwtec.tools.core.entity.UserInfoEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zc
 * \* Date: 2017/12/1 0001
 * \* Time: 上午 9:24
 * \* Description:
 * \
 */
public interface PushService {

    ResultMsg compressedFiles(MultipartFile sourceData, PushParams pushParams, HttpServletResponse response);

    int getCount();

    void truncate();

    void insertPhoneNumbersBySqlLoader(Set<UserInfoEntity> entities);

}
