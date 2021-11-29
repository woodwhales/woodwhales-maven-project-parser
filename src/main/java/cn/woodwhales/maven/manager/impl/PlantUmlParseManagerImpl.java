package cn.woodwhales.maven.manager.impl;

import cn.woodwhales.common.model.result.OpResult;
import cn.woodwhales.maven.controller.request.ProjectInfoRequestBody;
import cn.woodwhales.maven.entity.DependencyInfo;
import cn.woodwhales.maven.manager.PlantUmlParseManager;
import cn.woodwhales.maven.mapper.DependencyInfoMapper;
import cn.woodwhales.maven.model.BuildProjectResult;
import cn.woodwhales.maven.model.MavenComponentInfo;
import cn.woodwhales.maven.util.MavenPomParseTool;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static cn.woodwhales.common.business.DataTool.filter;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author woodwhales on 2021-11-16 19:41
 */
@Service
public class PlantUmlParseManagerImpl implements PlantUmlParseManager {

    @Autowired
    private DependencyInfoMapper dependencyInfoMapper;

    private final static String BR = "\n";

    @Override
    public OpResult<String> drawMavenComponentInfo(ProjectInfoRequestBody projectInfoRequestBody,
                                                   BuildProjectResult buildProjectResult) {
        if (Boolean.FALSE.equals(projectInfoRequestBody.getShowComponent())) {
            return OpResult.success();
        }

        final MavenPomParseTool.ProjectInfoDto projectInfoDto = buildProjectResult.getProjectInfoDto();
        StringBuffer componentPlantuml = new StringBuffer();
        componentPlantuml.append("@startuml");
        componentPlantuml.append(BR);
        componentPlantuml.append(BR);
        componentPlantuml.append(this.drawMavenComponentInfo(projectInfoRequestBody, projectInfoDto));
        componentPlantuml.append(BR);
        componentPlantuml.append("@enduml");
        return OpResult.success(componentPlantuml.toString());
    }

    private String drawMavenComponentInfo(ProjectInfoRequestBody projectInfoRequestBody,
                                          final MavenPomParseTool.ProjectInfoDto projectInfoDto) {
        final String artifactId = projectInfoDto.getArtifactId();
        final List<MavenPomParseTool.ModuleInfoDto> modules = projectInfoDto.getModules();
        StringBuffer packagePlantuml = new StringBuffer();
        if (CollectionUtils.isEmpty(modules)) {
            if (projectInfoRequestBody.getShowVersion()) {
                packagePlantuml.append(String.format("  component \"%s\\n%s\" ", artifactId, projectInfoDto.getVersion()));
            } else {
                packagePlantuml.append(String.format("  component \"%s\"", artifactId));
            }
            packagePlantuml.append(BR);
        } else {
            if (projectInfoRequestBody.getShowVersion()) {
                packagePlantuml.append(String.format("package \"%s-%s\" {", artifactId, projectInfoDto.getVersion()));
                packagePlantuml.append(BR);
                packagePlantuml.append(BR);
            } else {
                packagePlantuml.append(String.format("package \"%s\" {", artifactId));
                packagePlantuml.append(BR);
                packagePlantuml.append(BR);
            }

            final List<MavenPomParseTool.ProjectInfoDto> subProjectInfoList = projectInfoDto.getSubProjectInfoList();
            if (CollectionUtils.isEmpty(subProjectInfoList)) {
                packagePlantuml.append(BR);
            }
            for (MavenPomParseTool.ProjectInfoDto subProjectInfo : subProjectInfoList) {
                packagePlantuml.append("  " + this.drawMavenComponentInfo(projectInfoRequestBody, subProjectInfo));
                packagePlantuml.append(BR);
            }
            packagePlantuml.append("}");
            packagePlantuml.append(BR);
        }
        return packagePlantuml.toString();
    }

    @Override
    public OpResult<String> drawMavenDependencyInfo(ProjectInfoRequestBody projectInfoRequestBody,
                                                    BuildProjectResult buildProjectResult) {

        MavenPomParseTool.ProjectInfoDto rootProjectInfoDto = buildProjectResult.getProjectInfoDto();
        LinkedHashSet<String> relationComponentSet = new LinkedHashSet<>();
        this.drawMavenDependencyInfo(projectInfoRequestBody, rootProjectInfoDto, relationComponentSet);

        if(CollectionUtils.isEmpty(relationComponentSet)) {
            return OpResult.success();
        } else {
            StringBuffer relations = new StringBuffer();
            relations.append("@startuml");
            relations.append(BR);
            relations.append(BR);
            if(projectInfoRequestBody.getShowRootComponent()) {
                relations.append(String.format("package \"%s\" {", rootProjectInfoDto.getArtifactId()));
                relations.append(BR);
            }
            relationComponentSet.forEach(relationComponent -> relations.append(relationComponent).append(BR));
            relations.append(BR);

            if(projectInfoRequestBody.getShowRootComponent()) {
                relations.append("}");
                relations.append(BR);
                relations.append(BR);
            }
            relations.append("@enduml");
            return OpResult.success(relations.toString());
        }
    }

    private void drawMavenDependencyInfo(ProjectInfoRequestBody projectInfoRequestBody,
                                         MavenPomParseTool.ProjectInfoDto projectInfoDto,
                                         LinkedHashSet<String> plantuml) {
        Boolean showRootComponent = projectInfoRequestBody.getShowRootComponent();
        if(StringUtils.equalsIgnoreCase(projectInfoRequestBody.getProjectFilePath(), projectInfoDto.getAbsoluteFilePath())) {
            // 项目根目录不设置依赖
            List<MavenPomParseTool.ProjectInfoDto> subProjectInfoList = projectInfoDto.getSubProjectInfoList();
            if(CollectionUtils.isNotEmpty(subProjectInfoList)) {
                for (MavenPomParseTool.ProjectInfoDto subProjectInfo : subProjectInfoList) {
                    this.drawMavenDependencyInfo(projectInfoRequestBody, subProjectInfo, plantuml);
                }
            }
        } else {
            this.drawSubProject(projectInfoRequestBody, projectInfoDto, plantuml);
            this.drawDependency(projectInfoRequestBody, projectInfoDto, plantuml);
        }
    }

    private void drawSubProject(ProjectInfoRequestBody projectInfoRequestBody,
                                MavenPomParseTool.ProjectInfoDto projectInfoDto,
                                LinkedHashSet<String> plantuml) {
        List<MavenPomParseTool.ProjectInfoDto> subProjectInfoList =
                filter(projectInfoDto.getSubProjectInfoList(),
                        subProjectInfo -> isBlank(subProjectInfo.getGroupId())
                                || projectInfoRequestBody.getProjectGroupIdSet().contains(subProjectInfo.getGroupId()));

        for (MavenPomParseTool.ProjectInfoDto subProjectInfo : subProjectInfoList) {
            String value1;
            if (projectInfoRequestBody.getShowVersion()) {
                value1 = String.format("[ %s\\n%s ] -down-> [ %s\\n%s ]", projectInfoDto.getArtifactId(), projectInfoDto.getVersion(),
                        subProjectInfo.getArtifactId(), subProjectInfo.getVersion());
            } else {
                value1 = String.format("[ %s ] -down-> [ %s ]", projectInfoDto.getArtifactId(), subProjectInfo.getArtifactId());
            }
            plantuml.add(value1);
            this.drawMavenDependencyInfo(projectInfoRequestBody, subProjectInfo, plantuml);
        }
    }

    private void drawDependency(ProjectInfoRequestBody projectInfoRequestBody,
                                MavenPomParseTool.ProjectInfoDto projectInfoDto,
                                LinkedHashSet<String> plantuml) {
        List<MavenPomParseTool.DependencyInfoDto> dependencyInfoDtoList =
                filter(projectInfoDto.getDependencyList(),
                        dependency -> projectInfoRequestBody.getProjectGroupIdSet().contains(dependency.getGroupId()));

        for (MavenPomParseTool.DependencyInfoDto dependencyInfoDto : dependencyInfoDtoList) {
            String value2;
            if (projectInfoRequestBody.getShowVersion()) {
                value2 = String.format("[ %s\\n%s ] -down-> [ %s\\n%s ]", projectInfoDto.getArtifactId(), projectInfoDto.getVersion(),
                        dependencyInfoDto.getArtifactId(), dependencyInfoDto.getVersion());
            } else {
                value2 = String.format("[ %s ] -down-> [ %s ]", projectInfoDto.getArtifactId(), dependencyInfoDto.getArtifactId());
            }
            plantuml.add(value2);
        }

    }

    private void cache(ProjectInfoRequestBody projectInfoRequestBody,
                       Map<Long, MavenComponentInfo> dependencyInfoMap,
                       Set<String> cache,
                       Set<Long> hasDependencySet,
                       DependencyInfo dependencyInfo) {
        Long projectInfoId = dependencyInfo.getProjectInfoId();
        hasDependencySet.add(projectInfoId);
        MavenComponentInfo parentProjectInfo = dependencyInfoMap.get(projectInfoId);
        String value;
        if (projectInfoRequestBody.getShowVersion()) {
            value = String.format("[ %s\\n%s ] -down-> [ %s\\n%s ]", parentProjectInfo.artifactId, parentProjectInfo.version,
                    dependencyInfo.getArtifactId(), dependencyInfo.getVersion());
        } else {
            value = String.format("[ %s ] -down-> [ %s ]", parentProjectInfo.artifactId, dependencyInfo.getArtifactId());
        }
        cache.add(value);
    }

}
