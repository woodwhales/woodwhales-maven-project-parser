package cn.woodwhales.maven.service.impl;

import cn.woodwhales.common.model.result.OpResult;
import cn.woodwhales.maven.controller.request.ProjectInfoRequestBody;
import cn.woodwhales.maven.manager.MavenProjectManager;
import cn.woodwhales.maven.model.BuildProjectResult;
import cn.woodwhales.maven.service.ParseService;
import cn.woodwhales.maven.util.MavenPomParseTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author woodwhales on 2021-11-16 11:24
 */
@Service
public class ParseServiceImpl implements ParseService {

    @Autowired
    private MavenProjectManager mavenProjectManager;

    @Override
    public OpResult<BuildProjectResult> parse(ProjectInfoRequestBody projectInfoRequestBody) {
        MavenPomParseTool.ProjectInfoDto projectInfoDto = MavenPomParseTool.projectInfo(projectInfoRequestBody.getProjectFilePath());
        return mavenProjectManager.saveProjectInfo(projectInfoDto);
    }
}
