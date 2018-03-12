package com.xwtec.tools.core.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zc
 * \* Date: 2017/12/8 0008
 * \* Time: 上午 11:48
 * \* Description:
 * \
 */
public class PushParams {
    @NotNull(message = "不能为空")
    private String type;
    private boolean version_ios;
    private boolean version_android;
    private boolean show_phone;
    private boolean show_msgid;
    private int splitNumber;
    @Min(value = 10000,message = "最小值不能小于10000")
    @NotNull(message = "参数不能为空")
    private int numberSplit;
    //0无分隔符,1以,分隔
    private String separator;

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public int getNumberSplit() {
        return numberSplit;
    }

    public void setNumberSplit(int numberSplit) {
        this.numberSplit = numberSplit;
    }

    public boolean isVersion_ios() {
        return version_ios;
    }

    public void setVersion_ios(boolean version_ios) {
        this.version_ios = version_ios;
    }

    public boolean isVersion_android() {
        return version_android;
    }

    public void setVersion_android(boolean version_android) {
        this.version_android = version_android;
    }

    public boolean isShow_phone() {
        return show_phone;
    }

    public void setShow_phone(boolean show_phone) {
        this.show_phone = show_phone;
    }

    public boolean isShow_msgid() {
        return show_msgid;
    }

    public void setShow_msgid(boolean show_msgid) {
        this.show_msgid = show_msgid;
    }

    public int getSplitNumber() {
        return splitNumber;
    }

    public void setSplitNumber(int splitNumber) {
        this.splitNumber = splitNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
