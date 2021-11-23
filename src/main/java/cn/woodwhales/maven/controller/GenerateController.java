package cn.woodwhales.maven.controller;

import cn.woodwhales.common.model.result.OpResult;
import cn.woodwhales.common.model.vo.RespVO;
import cn.woodwhales.maven.controller.request.ProjectInfoRequestBody;
import cn.woodwhales.maven.controller.response.PlantUmlVo;
import cn.woodwhales.maven.model.BuildProjectResult;
import cn.woodwhales.maven.service.ParseService;
import cn.woodwhales.maven.plantuml.PlantUmlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author woodwhales on 2021-11-16 11:10
 */
@Slf4j
@RestController
public class GenerateController {

    @Autowired
    private ParseService parseService;

    @Autowired
    private PlantUmlService plantUmlService;

    @PostMapping("/generate")
    public RespVO<PlantUmlVo> generate(@RequestBody ProjectInfoRequestBody projectInfoRequestBody) {
        OpResult<BuildProjectResult> opResult = parseService.parse(projectInfoRequestBody);
        BuildProjectResult buildProjectResult = opResult.getData();
        RespVO<PlantUmlVo> respVO = plantUmlService.parse(projectInfoRequestBody, buildProjectResult);
        return respVO;
    }

}
