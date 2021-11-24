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
 * 模块信息表
 *
 * @author woodwhales on 2021-11-24 17:10:10
 *
 */
@TableName(value= "module_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuleInfo implements Serializable {
    
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
     * 当前工程绝对路径hash值
     */
    @TableField(value = "project_key")
    private String projectKey;

    /**
     * 工程信息表主键
     */
    @TableField(value = "project_info_id")
    private Long projectInfoId;

    /**
     * module
     */
    @TableField(value = "module")
    private String module;

    /**
     * 文件绝对路径
     */
    @TableField(value = "absolute_file_path")
    private String absoluteFilePath;

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