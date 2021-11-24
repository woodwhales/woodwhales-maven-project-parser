package cn.woodwhales.maven.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * maven工程信息表
 *
 * @author woodwhales on 2021-11-24 17:10:10
 *
 */
@TableName(value= "project_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectInfo implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目根目录hash值
     */
    @TableField(value = "root_project_key")
    private String rootProjectKey;

    /**
     * 项目别名
     */
    @TableField(value = "project_alias")
    private String projectAlias;

    /**
     * 文件绝对路径hash值
     */
    @TableField(value = "project_key")
    private String projectKey;

    /**
     * 文件绝对路径
     */
    @TableField(value = "absolute_file_path")
    private String absoluteFilePath;

    /**
     * groupId
     */
    @TableField(value = "group_id")
    private String groupId;

    /**
     * artifactId
     */
    @TableField(value = "artifact_id")
    private String artifactId;

    /**
     * version
     */
    @TableField(value = "version")
    private String version;

    /**
     * packaging
     */
    @TableField(value = "packaging")
    private String packaging;

    /**
     * name
     */
    @TableField(value = "name")
    private String name;

    /**
     * description
     */
    @TableField(value = "description")
    private String description;

    /**
     * 文件相对路径
     */
    @TableField(value = "relative_file_path")
    private String relativeFilePath;

    /**
     * 父工程标识：0-否，1-是
     */
    @TableField(value = "parent_flag")
    private Byte parentFlag;

    /**
     * 是否为项目跟目录：0-否，1-是
     */
    @TableField(value = "root_project_flag")
    private Byte rootProjectFlag;

    /**
     * parent-groupId
     */
    @TableField(value = "parent_group_id")
    private String parentGroupId;

    /**
     * parent-artifactId
     */
    @TableField(value = "parent_artifact_id")
    private String parentArtifactId;

    /**
     * parent-version
     */
    @TableField(value = "parent_version")
    private String parentVersion;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private java.util.Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private java.util.Date updateTime;

}