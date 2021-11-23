package cn.woodwhales.maven.manager;

import cn.woodwhales.common.model.result.OpResult;
import cn.woodwhales.maven.model.BuildProjectResult;
import cn.woodwhales.maven.util.MavenPomParseTool;

/**
 * @author woodwhales on 2021-11-16 11:25
 */
public interface MavenProjectManager {

    /**
     * 保存 maven 工程信息
     * @param projectInfoDto
     * @return
     */
    OpResult<BuildProjectResult> saveProjectInfo(MavenPomParseTool.ProjectInfoDto projectInfoDto);

}
