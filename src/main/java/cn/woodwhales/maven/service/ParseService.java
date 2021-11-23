package cn.woodwhales.maven.service;

import cn.woodwhales.common.model.result.OpResult;
import cn.woodwhales.maven.controller.request.ProjectInfoRequestBody;
import cn.woodwhales.maven.model.BuildProjectResult;

/**
 * @author woodwhales on 2021-11-16 11:22
 */
public interface ParseService {

    /**
     * 解析 maven 工程
     * @param projectInfoRequestBody
     * @return
     */
    OpResult<BuildProjectResult> parse(ProjectInfoRequestBody projectInfoRequestBody);

}
