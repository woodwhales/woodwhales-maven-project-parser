package cn.woodwhales.maven.manager.impl;

import cn.woodwhales.common.business.DataTool;
import cn.woodwhales.common.model.result.OpResult;
import cn.woodwhales.common.mybatisplus.MybatisPlusExecutor;
import cn.woodwhales.maven.controller.request.ProjectInfoRequestBody;
import cn.woodwhales.maven.entity.DependencyInfo;
import cn.woodwhales.maven.manager.PlantUmlParseManager;
import cn.woodwhales.maven.mapper.DependencyInfoMapper;
import cn.woodwhales.maven.model.MavenComponentInfo;
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
    public OpResult<String> drawMavenComponentInfo(MavenComponentInfo rootMavenComponentInfo) {
        StringBuffer modules = new StringBuffer();
        modules.append("@startuml");
        modules.append(BR);
        modules.append(BR);
        modules.append(String.format("package \"%s\" {", rootMavenComponentInfo.alias));
        modules.append(BR);
        for (MavenComponentInfo componentInfo : rootMavenComponentInfo.childComponentList) {
            modules.append(String.format("    component \"%s\"", componentInfo.alias));
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
        dependencyInfoList.forEach(dependencyInfo -> this.cache(dependencyInfoMap, cache, dependencyInfo));
        ArrayList<String> relationList = new ArrayList<>(cache);
        for (String relation : relationList) {
            relations.append(relation).append(BR);
        }
        relations.append(BR);
        relations.append("@enduml");
        return OpResult.success(relations.toString());
    }

    private void cache(Map<Long, MavenComponentInfo> dependencyInfoMap,
                       Set<String> cache,
                       DependencyInfo dependencyInfo) {
        String parentArtifactId = dependencyInfoMap.get(dependencyInfo.getProjectInfoId()).artifactId;
        String value = String.format("[ %s ] -down-> [ %s ]", parentArtifactId, dependencyInfo.getArtifactId());
        cache.add(value);
    }

}
