package cn.woodwhales.maven.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 配置信息表
 *
 * @author woodwhales on 2021-11-16 11:31:40
 *
 */
@TableName(value= "properties_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertiesInfo implements Serializable {
    
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
     * 工程信息表主键
     */
    @TableField(value = "project_info_id")
    private Long projectInfoId;

    /**
     * key
     */
    @TableField(value = "prop_key")
    private String propKey;

    /**
     * value
     */
    @TableField(value = "prop_value")
    private String propValue;

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