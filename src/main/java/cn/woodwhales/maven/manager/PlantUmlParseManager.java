package cn.woodwhales.maven.manager;

import cn.woodwhales.common.model.result.OpResult;
import cn.woodwhales.maven.controller.request.ProjectInfoRequestBody;
import cn.woodwhales.maven.model.MavenComponentInfo;

/**
 * @author woodwhales on 2021-11-16 19:40
 */
public interface PlantUmlParseManager {

    /**
     * 绘制组件关系图
     * @param projectInfoRequestBody
     * @param rootMavenComponentInfo
     * @return
     */
    OpResult<String> drawMavenComponentInfo(ProjectInfoRequestBody projectInfoRequestBody,
                                            MavenComponentInfo rootMavenComponentInfo);

    /**
     * 绘制依赖关系图
     * @param projectInfoRequestBody
     * @param rootMavenComponentInfo
     * @return
     */
    OpResult<String> drawMavenDependencyInfo(ProjectInfoRequestBody projectInfoRequestBody,
                                             MavenComponentInfo rootMavenComponentInfo);
}
