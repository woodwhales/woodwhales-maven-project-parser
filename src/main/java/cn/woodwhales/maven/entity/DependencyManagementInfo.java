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
 * dependencyManagement 信息表
 *
 * @author woodwhales on 2021-11-24 17:10:10
 *
 */
@TableName(value= "dependency_management_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DependencyManagementInfo implements Serializable {
    
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
     * 文件绝对路径hash值
     */
    @TableField(value = "project_key")
    private String projectKey;

    /**
     * 工程信息表主键
     */
    @TableField(value = "project_info_id")
    private Long projectInfoId;

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
     * scope
     */
    @TableField(value = "scope")
    private String scope;

    /**
     * type
     */
    @TableField(value = "type")
    private String type;

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