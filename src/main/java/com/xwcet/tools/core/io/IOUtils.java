package com.xwcet.tools.core.io;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zc
 * \* Date: 2017/12/1 0001
 * \* Time: 下午 12:29
 * \* Description:
 * \ io工具
 */
public class IOUtils {

    private static final int SUCESS = 1;
    private static final int ERROR = -1;

    /**
     * 通过response将目标文件输出
     *
     * @param response response
     * @param path     目标文件
     * @return 返回成功与否
     */
    public static int responseWrite(HttpServletResponse response, String path) {
        File file = new File(path);
        try
                (
                        OutputStream out = response.getOutputStream();
                        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file))
                ) {
            String fileName = file.getName();
            String prefix=fileName.substring(fileName.lastIndexOf("."));
            response.setHeader("content-disposition",
            "attachment;filename=" + new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()) + prefix);
            byte[] bytes = new byte[1028 * 8];
            int length;
            while ((length = in.read(bytes)) != -1) {
                out.write(bytes, 0, length);
            }
            return SUCESS;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (file.exists())
                file.delete();
        }
        return ERROR;
    }


}
