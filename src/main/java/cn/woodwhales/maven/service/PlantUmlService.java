package cn.woodwhales.maven.service;

import cn.woodwhales.common.model.vo.RespVO;
import cn.woodwhales.maven.controller.request.ProjectInfoRequestBody;
import cn.woodwhales.maven.controller.response.PlantUmlVo;
import cn.woodwhales.maven.model.BuildProjectResult;

/**
 * @author woodwhales on 2021-11-16 19:49
 */
public interface PlantUmlService {

    /**
     * 解析 maven 工程依赖
     * @param projectInfoRequestBody
     * @param buildProjectResult
     * @return
     */
    RespVO<PlantUmlVo> parse(ProjectInfoRequestBody projectInfoRequestBody, BuildProjectResult buildProjectResult);
}
