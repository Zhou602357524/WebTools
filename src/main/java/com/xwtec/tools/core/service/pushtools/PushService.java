package com.xwtec.tools.core.service.pushtools;

import org.springframework.web.multipart.MultipartFile;

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
     * @param splitNumber 切片数量
     * @return 返回对应ZipPath路径
     */
    String compressedFiles(MultipartFile sourceData,int splitNumber);

}
