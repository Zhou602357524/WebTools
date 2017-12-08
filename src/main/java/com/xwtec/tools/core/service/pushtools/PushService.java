package com.xwtec.tools.core.service.pushtools;

import com.xwtec.tools.core.entity.PushParm;
import com.xwtec.tools.core.entity.UserInfoEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zc
 * \* Date: 2017/12/1 0001
 * \* Time: 上午 9:24
 * \* Description:
 * \
 */
public interface PushService {
    /**
     * 处理推送文件
     * @param sourceData 源文件
     * @return 返回对应ZipPath路径
     */
    String compressedFiles(MultipartFile sourceData, PushParm pushParm);

    void insertPhoneNumbers(List<UserInfoEntity> numbers);

    void truncate();

    int getCount();
}
