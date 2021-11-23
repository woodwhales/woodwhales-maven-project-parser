package cn.woodwhales.maven.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author woodwhales
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectInfoRequestBody {

	private String projectFilePath;
	
	private Boolean showComponent = false;

	private String projectGroupId;
	
}
