package cn.woodwhales.maven.enums;

import java.util.Objects;

/**
 * 是否为项目根目录枚举
 * @author woodwhales on 2021-11-16 13:02
 */
public enum RootProjectFlagEnum {

    /**
     * 是父工程
     */
    ROOT_FLAG((byte) 1, "是根目录"),

    /**
     * 不是父工程
     */
    NON_ROOT_FLAG((byte) 0, "不是根目录"),
    ;

    public final byte code;
    public final String desc;

    RootProjectFlagEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public boolean match(byte code) {
        return Objects.equals(code, this.code);
    }

}
