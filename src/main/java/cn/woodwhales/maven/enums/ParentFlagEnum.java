package cn.woodwhales.maven.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 是否为父工程枚举
 * @author woodwhales on 2021-11-16 13:02
 */
public enum ParentFlagEnum {

    /**
     * 是父工程
     */
    PARENT_FLAG((byte) 1, "是父工程"),

    /**
     * 不是父工程
     */
    NON_PARENT_FLAG((byte) 0, "不是父工程"),
    ;

    public final byte code;
    public final String desc;

    ParentFlagEnum(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ParentFlagEnum packagingOf(String packaging) {
        return StringUtils.equals(packaging, "pom") ? PARENT_FLAG : NON_PARENT_FLAG;
    }

    public boolean match(byte code) {
        return Objects.equals(code, this.code);
    }

    public boolean match(String packaging) {
        return Objects.equals(packagingOf(packaging).code, this.code);
    }
}
