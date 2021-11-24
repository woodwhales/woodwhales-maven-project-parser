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
 * 依赖排除信息表
 *
 * @author woodwhales on 2021-11-24 17:10:10
 *
 */
@TableName(value= "exclusion_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExclusionInfo implements Serializable {
    
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
     * 工程信息表主键
     */
    @TableField(value = "dependency_info_id")
    private Long dependencyInfoId;

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