package cn.woodwhales.maven.manager.impl;

import cn.woodwhales.common.business.DataTool;
import cn.woodwhales.common.model.result.OpResult;
import cn.woodwhales.common.mybatisplus.MybatisPlusExecutor;
import cn.woodwhales.maven.controller.request.ProjectInfoRequestBody;
import cn.woodwhales.maven.entity.DependencyInfo;
import cn.woodwhales.maven.manager.PlantUmlParseManager;
import cn.woodwhales.maven.mapper.DependencyInfoMapper;
import cn.woodwhales.maven.model.MavenComponentInfo;
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
                modules.append(String.format("    component \"%s\\n%s\" ", componentInfo.alias, componentInfo.version));
            } else {
                modules.append(String.format("    component \"%s\"", componentInfo.alias));
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

        List<DependencyInfo> dependencyInfoList = MybatisPlusExecutor.executeQueryList(dependencyInfoMapper, wrapper -> {
            wrapper.in(DependencyInfo::getProjectInfoId, projectInfoIdList)
                    .eq(DependencyInfo::getGroupId, projectGroupId);
        });

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
