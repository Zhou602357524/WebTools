package com.xwtec.tools.core.utils.ziptools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.*;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipTools {
    private String baseName;
    private final Logger logger;

    public ZipTools(String baseName) {
        this.baseName = baseName;
        logger = LoggerFactory.getLogger(this.getClass());
    }

    public void zip(@NotNull String zipFileName, @NotNull String... sourceFileName) throws Exception {

        //File zipFile = new File(zipFileName);
        logger.info("压缩中...");
        //创建zip输出流
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFileName)));

        for (String filename : sourceFileName) {
            File sourceFile = new File(filename);
            //如果路径为目录（文件夹）
            if (sourceFile.isDirectory()) {
                //取出文件夹中的文件（或子文件夹）
                File[] fileList = sourceFile.listFiles();
                if (fileList.length == 0)//如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
                {
                    logger.info("空目录");
                    out.putNextEntry(new ZipEntry(baseName + "/"));
                } else {
                    for (int i = 0; i < fileList.length; i++) {
                        File file = fileList[i];
                        if (!file.getPath().equals(zipFileName))
                            compress(out, file, baseName + "/" + file.getName());
                    }
                }
            } else {
                compress(out, sourceFile, baseName + "/" + sourceFile.getName());
            }
        }
        out.close();
        logger.info("压缩完成");
    }

    public void zipByteOutArr(String zipFileName, ByteArrayOutputStream[] byteOutArr, String baseName) {
        File zipFile = new File(zipFileName);
        checkFileDirectory(zipFile.getParent());
        try
                (ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)))) {
            if (baseName.endsWith(".csv"))
                baseName = baseName.replace(".csv", ".txt");
            //File zipFile = new File(zipFileName);
            logger.info("压缩中...");
            Integer flag = 0;
            //stream方式操作
            /*
            Stream.of(byteOutArr).parallel().forEach(e -> {
                try {
                    String uuid = UUID.randomUUID().toString().replace("-", "") + "_";
                    compress(out,e,this.baseName + "/" + uuid + baseName);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            });*/
            //创建zip输出流
            for (int i = 0; i < byteOutArr.length; i++) {
                logger.info("压缩part" + i + " start");
                compress(out, byteOutArr[i], this.baseName + "/" + "part" + i + "_" + baseName);
                logger.info("压缩part" + i + " end");
            }
            logger.info("压缩完成");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void checkFileDirectory(String fileNane) {
        File fileDirectory = new File(fileNane);
        if (!fileDirectory.exists())
            fileDirectory.mkdirs();
    }

    /**
     * @param out        zip输出流
     * @param sourceFile 源文件
     * @param base       文件名
     * @throws Exception 可能产生压缩异常
     */
    private void compress(ZipOutputStream out, File sourceFile, String base) throws Exception {

        out.putNextEntry(new ZipEntry(base));
        //读取源文件
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile));

        byte[] buffer = new byte[1024 * 8];
        int len = -1;
        logger.info(base);
        //将源文件写入到zip文件中
        while ((len = bis.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        bis.close();
    }

    private void compress(ZipOutputStream out, ByteArrayOutputStream byteOut, String base) throws Exception {
        try {
            out.putNextEntry(new ZipEntry(base));

            out.write(byteOut.toByteArray());
        } finally {
            if (byteOut != null) {
                byteOut.close();
                byteOut = null;
            }
        }

    }

}
