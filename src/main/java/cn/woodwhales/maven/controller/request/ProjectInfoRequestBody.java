package cn.woodwhales.maven.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * @author woodwhales
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectInfoRequestBody {

	/**
	 * maven 项目根路径
	 */
	@NotBlank(message = "maven 项目根路径不能未空")
	private String projectFilePath;

	/**
	 * 是否展示组件
	 */
	private Boolean showComponent = false;

	/**
	 * 是否展示全部依赖组件
	 */
	private Boolean showAllRelation = false;

	/**
	 * 是否展示版本号
	 */
	private Boolean showVersion = false;

	/**
	 * 是否显示根项目组件
	 */
	private Boolean showRootComponent = true;

	/**
	 * 指定 groupId
	 */
	private Set<String> projectGroupIdSet;
	
}
