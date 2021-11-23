package cn.woodwhales.maven.plantuml.impl;

import cn.woodwhales.common.business.DataTool;
import cn.woodwhales.common.model.result.OpResult;
import cn.woodwhales.common.model.vo.RespVO;
import cn.woodwhales.common.mybatisplus.MybatisPlusExecutor;
import cn.woodwhales.maven.controller.request.ProjectInfoRequestBody;
import cn.woodwhales.maven.controller.response.PlantUmlVo;
import cn.woodwhales.maven.entity.ProjectInfo;
import cn.woodwhales.maven.manager.PlantUmlParseManager;
import cn.woodwhales.maven.mapper.ProjectInfoMapper;
import cn.woodwhales.maven.model.BuildProjectResult;
import cn.woodwhales.maven.model.MavenComponentInfo;
import cn.woodwhales.maven.plantuml.PlantUmlService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.woodwhales.maven.enums.RootProjectFlagEnum.ROOT_FLAG;

/**
 * @author woodwhales on 2021-11-16 20:10
 */
@Service
public class PlantUmlServiceImpl implements PlantUmlService {

    @Autowired
    private PlantUmlParseManager plantUmlParseManager;

    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    @Override
    public RespVO<PlantUmlVo> parse(ProjectInfoRequestBody projectInfoRequestBody,
                                    BuildProjectResult buildProjectResult) {
        String rootProjectKey = buildProjectResult.getRootProjectKey();
        List<ProjectInfo> projectInfoList = MybatisPlusExecutor.executeQueryList(projectInfoMapper, wrapper -> {
            wrapper.eq(ProjectInfo::getRootProjectKey, rootProjectKey);
        });

        ProjectInfo rootProjectInfo = getRootProjectInfo(projectInfoList);
        if(StringUtils.isBlank(projectInfoRequestBody.getProjectGroupId())) {
            projectInfoRequestBody.setProjectGroupId(rootProjectInfo.getGroupId());
        }

        MavenComponentInfo rootMavenComponentInfo = this.tree(projectInfoList, rootProjectInfo);

        OpResult<String> modulesOpResult;
        if(Boolean.TRUE.equals(projectInfoRequestBody.getShowComponent())) {
            modulesOpResult = plantUmlParseManager.drawMavenComponentInfo(rootMavenComponentInfo);
        } else {
            modulesOpResult = OpResult.success();
        }

        OpResult<String> relationsOpResult = plantUmlParseManager.drawMavenDependencyInfo(projectInfoRequestBody, rootMavenComponentInfo);
        PlantUmlVo plantUmlVo = new PlantUmlVo(modulesOpResult.getData(), relationsOpResult.getData());
        return RespVO.success(plantUmlVo);
    }

    private MavenComponentInfo tree(List<ProjectInfo> projectInfoList, ProjectInfo rootProjectInfo) {
        MavenComponentInfo rootMavenComponentInfo = buildMavenComponentInfo(rootProjectInfo);
        List<ProjectInfo> nonRootProjectInfoList = getNonRootProjectInfo(projectInfoList);
        List<MavenComponentInfo> childComponentList = DataTool.toList(nonRootProjectInfoList, this::buildMavenComponentInfo);
        rootMavenComponentInfo.childComponentList = childComponentList;
        return rootMavenComponentInfo;
    }

    private ProjectInfo getRootProjectInfo(List<ProjectInfo> projectInfoList) {
        return DataTool.filter(projectInfoList, projectInfo -> ROOT_FLAG.match(projectInfo.getRootProjectFlag())).get(0);
    }

    private List<ProjectInfo> getNonRootProjectInfo(List<ProjectInfo> projectInfoList) {
        return DataTool.filter(projectInfoList, projectInfo -> !ROOT_FLAG.match(projectInfo.getRootProjectFlag()));
    }

    private MavenComponentInfo buildMavenComponentInfo(ProjectInfo projectInfo) {
        return new MavenComponentInfo(projectInfo.getId(),
                                      projectInfo.getGroupId(),
                                      projectInfo.getArtifactId(),
                                      projectInfo.getProjectAlias());
    }

}
