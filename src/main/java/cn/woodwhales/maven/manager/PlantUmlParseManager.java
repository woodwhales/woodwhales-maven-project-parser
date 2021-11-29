package cn.woodwhales.maven.manager;

import cn.woodwhales.common.model.result.OpResult;
import cn.woodwhales.maven.controller.request.ProjectInfoRequestBody;
import cn.woodwhales.maven.model.BuildProjectResult;

/**
 * @author woodwhales on 2021-11-16 19:40
 */
public interface PlantUmlParseManager {

    /**
     * 绘制组件关系图
     * @param projectInfoRequestBody
     * @param buildProjectResult
     * @return
     */
    OpResult<String> drawMavenComponentInfo(ProjectInfoRequestBody projectInfoRequestBody,
                                             BuildProjectResult buildProjectResult);

    /**
     * 绘制依赖关系图
     * @param projectInfoRequestBody
     * @param buildProjectResult
     * @return
     */
    OpResult<String> drawMavenDependencyInfo(ProjectInfoRequestBody projectInfoRequestBody,
                                            BuildProjectResult buildProjectResult);
}
