package cn.woodwhales.maven.manager.impl;

import cn.woodwhales.common.business.DataTool;
import cn.woodwhales.common.model.result.OpResult;
import cn.woodwhales.common.mybatisplus.MybatisPlusExecutor;
import cn.woodwhales.maven.controller.request.ProjectInfoRequestBody;
import cn.woodwhales.maven.entity.DependencyInfo;
import cn.woodwhales.maven.manager.PlantUmlParseManager;
import cn.woodwhales.maven.mapper.DependencyInfoMapper;
import cn.woodwhales.maven.model.BuildProjectResult;
import cn.woodwhales.maven.model.MavenComponentInfo;
import cn.woodwhales.maven.util.MavenPomParseTool;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author woodwhales on 2021-11-16 19:41
 */
@Service
public class PlantUmlParseManagerImpl implements PlantUmlParseManager {

    @Autowired
    private DependencyInfoMapper dependencyInfoMapper;

    private final static String BR = "\n";

    @Override
    public OpResult<String> drawMavenComponentInfo2(ProjectInfoRequestBody projectInfoRequestBody,
                                                    BuildProjectResult buildProjectResult) {
        if(Boolean.FALSE.equals(projectInfoRequestBody.getShowComponent())) {
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
        if(CollectionUtils.isEmpty(modules)) {
            if(projectInfoRequestBody.getShowVersion()) {
                packagePlantuml.append(String.format("  component \"%s\\n%s\" ", artifactId, projectInfoDto.getVersion()));
            } else {
                packagePlantuml.append(String.format("  component \"%s\"", artifactId));
            }
            packagePlantuml.append(BR);
        } else {
            if(projectInfoRequestBody.getShowVersion()) {
                packagePlantuml.append(String.format("package \"%s-%s\" {", artifactId, projectInfoDto.getVersion()));
            } else {
                packagePlantuml.append(String.format("package \"%s\" {", artifactId));
            }

            packagePlantuml.append(BR);
            packagePlantuml.append(BR);

            final List<MavenPomParseTool.ProjectInfoDto> subProjectInfoList = projectInfoDto.getSubProjectInfoList();
            for (MavenPomParseTool.ProjectInfoDto subProjectInfo : subProjectInfoList) {
                packagePlantuml.append("  " + this.drawMavenComponentInfo(projectInfoRequestBody, subProjectInfo));
                packagePlantuml.append(BR);
            }
            packagePlantuml.append(BR);
            packagePlantuml.append("}");
        }
        return packagePlantuml.toString();
    }

    @Override
    public OpResult<String> drawMavenComponentInfo(ProjectInfoRequestBody projectInfoRequestBody,
                                                   MavenComponentInfo rootMavenComponentInfo) {
        if(Boolean.FALSE.equals(projectInfoRequestBody.getShowComponent())) {
            return OpResult.success();
        }

        StringBuffer modules = new StringBuffer();
        modules.append("@startuml");
        modules.append(BR);
        modules.append(BR);
        modules.append(String.format("package \"%s\" {", rootMavenComponentInfo.alias));
        modules.append(BR);
        for (MavenComponentInfo componentInfo : rootMavenComponentInfo.childComponentList) {
            if(projectInfoRequestBody.getShowVersion()) {
                modules.append(String.format("  component \"%s\\n%s\" ", componentInfo.alias, componentInfo.version));
            } else {
                modules.append(String.format("  component \"%s\"", componentInfo.alias));
            }
            modules.append(BR);
        }
        modules.append("}");
        modules.append(BR);
        modules.append(BR);
        modules.append("@enduml");
        return OpResult.success(modules.toString());
    }

    @Override
    public OpResult<String> drawMavenDependencyInfo(ProjectInfoRequestBody projectInfoRequestBody,
                                                    MavenComponentInfo rootMavenComponentInfo) {
        List<Long> projectInfoIdList = DataTool.toList(rootMavenComponentInfo.childComponentList,
                                                        componentInfo -> componentInfo.projectInfoId);

        String projectGroupId = projectInfoRequestBody.getProjectGroupId();

        List<DependencyInfo> dependencyInfoList;
        if(CollectionUtils.isNotEmpty(projectInfoIdList)) {
            dependencyInfoList = MybatisPlusExecutor.executeQueryList(dependencyInfoMapper, wrapper -> {
                wrapper.in(DependencyInfo::getProjectInfoId, projectInfoIdList)
                        .eq(DependencyInfo::getGroupId, projectGroupId);
            });
        } else {
            // 表示没有任何依赖
            return OpResult.success();
        }

        Map<Long, MavenComponentInfo> dependencyInfoMap = DataTool.toMap(rootMavenComponentInfo.childComponentList,
                                                                        componentInfo -> componentInfo.projectInfoId);
        StringBuffer relations = new StringBuffer();
        relations.append("@startuml");
        relations.append(BR);
        relations.append(BR);
        Set<String> cache = new HashSet<>();
        Set<Long> hasDependencySet = new HashSet<>();
        dependencyInfoList.forEach(dependencyInfo -> this.cache(projectInfoRequestBody, dependencyInfoMap, cache, hasDependencySet, dependencyInfo));
        ArrayList<String> relationList = new ArrayList<>(cache);
        for (String relation : relationList) {
            relations.append(relation).append(BR);
        }

        if(projectInfoRequestBody.getShowAllRelation()) {
            // 存在独立的模块，没有依赖任何指定 groupId
            Sets.SetView<Long> noDependencyModuleSet = Sets.difference(dependencyInfoMap.keySet(), hasDependencySet);
            if(CollectionUtils.isNotEmpty(noDependencyModuleSet)) {
                noDependencyModuleSet.forEach(moduleProjectId -> {
                    MavenComponentInfo mavenComponentInfo = dependencyInfoMap.get(moduleProjectId);
                    if(projectInfoRequestBody.getShowVersion()) {
                        relations.append(String.format("[ %s\\n%s ]", mavenComponentInfo.artifactId, mavenComponentInfo.version));
                    } else {
                        relations.append(String.format("[ %s ]", mavenComponentInfo.artifactId));
                    }
                    relations.append(BR);
                });
            }
        }

        relations.append(BR);
        relations.append("@enduml");
        return OpResult.success(relations.toString());
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
        if(projectInfoRequestBody.getShowVersion()) {
            value = String.format("[ %s\\n%s ] -down-> [ %s\\n%s ]", parentProjectInfo.artifactId, parentProjectInfo.version,
                    dependencyInfo.getArtifactId(), dependencyInfo.getVersion());
        } else {
            value = String.format("[ %s ] -down-> [ %s ]", parentProjectInfo.artifactId, dependencyInfo.getArtifactId());
        }
        cache.add(value);

    }

}
