package cn.woodwhales.maven.manager.impl;

import cn.hutool.crypto.SecureUtil;
import cn.woodwhales.common.business.DataTool;
import cn.woodwhales.common.model.result.OpResult;
import cn.woodwhales.maven.batchmapper.*;
import cn.woodwhales.maven.entity.*;
import cn.woodwhales.maven.enums.ParentFlagEnum;
import cn.woodwhales.maven.enums.RootProjectFlagEnum;
import cn.woodwhales.maven.manager.MavenProjectManager;
import cn.woodwhales.maven.mapper.*;
import cn.woodwhales.maven.model.GlobalProjectConfig;
import cn.woodwhales.maven.model.BuildProjectResult;
import cn.woodwhales.maven.util.MavenPomParseTool;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static cn.woodwhales.common.mybatisplus.MybatisPlusExecutor.batchInsert;
import static cn.woodwhales.maven.enums.RootProjectFlagEnum.NON_ROOT_FLAG;
import static cn.woodwhales.maven.enums.RootProjectFlagEnum.ROOT_FLAG;

/**
 * @author woodwhales on 2021-11-16 11:25
 */
@Service
public class MavenProjectManagerImpl implements MavenProjectManager {

    @Autowired
    private BatchProjectInfoMapper batchProjectInfoMapper;

    @Autowired
    private BatchModuleInfoMapper batchModuleInfoMapper;

    @Autowired
    private BatchPropertiesInfoMapper batchPropertiesInfoMapper;

    @Autowired
    private BatchDependencyManagementInfoMapper batchDependencyManagementInfoMapper;

    @Autowired
    private BatchDependencyInfoMapper batchDependencyInfoMapper;

    @Autowired
    private BatchExclusionInfoMapper batchExclusionInfoMapper;

    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    @Autowired
    private ModuleInfoMapper moduleInfoMapper;

    @Autowired
    private PropertiesInfoMapper propertiesInfoMapper;

    @Autowired
    private DependencyManagementInfoMapper dependencyManagementInfoMapper;

    @Autowired
    private DependencyInfoMapper dependencyInfoMapper;

    @Autowired
    private ExclusionInfoMapper exclusionInfoMapper;

    @Override
    @Transactional(rollbackFor = {Error.class, Exception.class})
    public OpResult<BuildProjectResult> saveProjectInfo(MavenPomParseTool.ProjectInfoDto projectInfoDto) {
        final Date now = new Date();
        final String absoluteFilePath = projectInfoDto.getAbsoluteFilePath();
        final String rootProjectKey = SecureUtil.sha256(absoluteFilePath);

        this.deleteProjectInfo(rootProjectKey);

        GlobalProjectConfig globalProjectConfig = new GlobalProjectConfig(absoluteFilePath, rootProjectKey, now);
        this.saveProjectInfo(projectInfoDto, globalProjectConfig, ROOT_FLAG);
        return OpResult.success(new BuildProjectResult(projectInfoDto, rootProjectKey));
    }


    private MavenPomParseTool.ProjectInfoDto saveProjectInfo(MavenPomParseTool.ProjectInfoDto projectInfoDto,
                                                             GlobalProjectConfig globalProjectConfig,
                                                             RootProjectFlagEnum rootProjectFlagEnum) {
        // ?????? project_info
        final ProjectInfo currentProjectInfo = this.buildProjectInfo(projectInfoDto, globalProjectConfig, rootProjectFlagEnum);
        batchProjectInfoMapper.save(currentProjectInfo);

        // ?????? module_info
        batchInsert(batchModuleInfoMapper, this.buildModuleInfo(currentProjectInfo, projectInfoDto, globalProjectConfig));

        // ?????? properties_info
        batchInsert(batchPropertiesInfoMapper, this.buildPropertiesInfo(currentProjectInfo, projectInfoDto, globalProjectConfig));

        // ?????? dependency_management_info
        batchInsert(batchDependencyManagementInfoMapper, this.buildDependencyManagementInfo(currentProjectInfo, projectInfoDto, globalProjectConfig));

        // ?????? dependency_info
        List<DependencyInfo> dependencyInfoList = this.buildDependencyInfo(currentProjectInfo, projectInfoDto, globalProjectConfig);
        batchInsert(batchDependencyInfoMapper, dependencyInfoList);

        // ?????? exclusion_info
        batchInsert(batchExclusionInfoMapper, this.buildExclusionInfo(currentProjectInfo, projectInfoDto, dependencyInfoList, globalProjectConfig));

        // ?????????????????????
        final List<MavenPomParseTool.ProjectInfoDto> subProjectInfoList = projectInfoDto.getSubProjectInfoList();
        if(CollectionUtils.isNotEmpty(subProjectInfoList)) {
            subProjectInfoList.forEach(subProjectInfoDto ->
                    this.saveProjectInfo(subProjectInfoDto, globalProjectConfig, NON_ROOT_FLAG));
        }

        return projectInfoDto;
    }

    private void deleteProjectInfo(String rootProjectKey) {
        projectInfoMapper.delete(Wrappers.<ProjectInfo> lambdaQuery().eq(ProjectInfo::getRootProjectKey, rootProjectKey));
        moduleInfoMapper.delete(Wrappers.<ModuleInfo> lambdaQuery().eq(ModuleInfo::getRootProjectKey, rootProjectKey));
        propertiesInfoMapper.delete(Wrappers.<PropertiesInfo> lambdaQuery().eq(PropertiesInfo::getRootProjectKey, rootProjectKey));
        dependencyManagementInfoMapper.delete(Wrappers.<DependencyManagementInfo> lambdaQuery().eq(DependencyManagementInfo::getRootProjectKey, rootProjectKey));
        dependencyInfoMapper.delete(Wrappers.<DependencyInfo> lambdaQuery().eq(DependencyInfo::getRootProjectKey, rootProjectKey));
        exclusionInfoMapper.delete(Wrappers.<ExclusionInfo> lambdaQuery().eq(ExclusionInfo::getRootProjectKey, rootProjectKey));
    }

    private List<ExclusionInfo> buildExclusionInfo(final ProjectInfo parentProjectInfo,
                                                   final MavenPomParseTool.ProjectInfoDto projectInfoDtoModel,
                                                   final List<DependencyInfo> dependencyInfoList,
                                                   final GlobalProjectConfig globalProjectConfig) {
        Map<String, DependencyInfo> dependencyInfoMap = DataTool.toMap(dependencyInfoList, dependencyInfo -> {
            String groupId = dependencyInfo.getGroupId();
            String artifactId = dependencyInfo.getArtifactId();
            String version = dependencyInfo.getVersion();
            return StringUtils.joinWith("_", groupId, artifactId, version);
        });

        List<MavenPomParseTool.DependencyInfoDto> dependencyDtoList = projectInfoDtoModel.getDependencyList();
        List<MavenPomParseTool.DependencyInfoDto> containExclusionOfDependencyInfoDtoList = DataTool.filter(dependencyDtoList, dependencyDto -> CollectionUtils.isNotEmpty(dependencyDto.getExclusions()));

        List<ExclusionInfo> exclusionInfoList = new ArrayList<>();
        for (MavenPomParseTool.DependencyInfoDto dependencyInfoDto : containExclusionOfDependencyInfoDtoList) {

            String groupId = dependencyInfoDto.getGroupId();
            String artifactId = dependencyInfoDto.getArtifactId();
            String version = dependencyInfoDto.getVersion();
            String key = StringUtils.joinWith("_", groupId, artifactId, version);
            DependencyInfo dependencyInfo = dependencyInfoMap.get(key);
            List<MavenPomParseTool.DependencyInfoDto.DependencyExclusionInfoDto> exclusionList = dependencyInfoDto.getExclusions();
            exclusionInfoList.addAll(DataTool.toList(exclusionList, exclusion -> {
                ExclusionInfo exclusionInfo = new ExclusionInfo();
                exclusionInfo.setRootProjectKey(globalProjectConfig.rootProjectKey);
                exclusionInfo.setProjectKey(dependencyInfo.getProjectKey());
                exclusionInfo.setProjectInfoId(dependencyInfo.getId());
                exclusionInfo.setDependencyInfoId(dependencyInfo.getId());
                exclusionInfo.setGroupId(exclusion.getGroupId());
                exclusionInfo.setArtifactId(exclusion.getArtifactId());
                exclusionInfo.setCreateTime(globalProjectConfig.now);
                exclusionInfo.setUpdateTime(globalProjectConfig.now);
                return exclusionInfo;
            }));
        }
        return exclusionInfoList;
    }

    private List<DependencyInfo> buildDependencyInfo(final ProjectInfo parentProjectInfo,
                                                     final MavenPomParseTool.ProjectInfoDto projectInfoDtoModel,
                                                     final GlobalProjectConfig bootProjectConfig) {
        return DataTool.toList(projectInfoDtoModel.getDependencyList(), dependency -> {
            DependencyInfo dependencyInfo = new DependencyInfo();
            dependencyInfo.setRootProjectKey(bootProjectConfig.rootProjectKey);
            dependencyInfo.setProjectInfoId(parentProjectInfo.getId());
            dependencyInfo.setProjectKey(parentProjectInfo.getProjectKey());
            dependencyInfo.setGroupId(dependency.getGroupId());
            dependencyInfo.setArtifactId(dependency.getArtifactId());
            dependencyInfo.setVersion(dependency.getVersion());
            dependencyInfo.setScope(dependency.getScope());
            dependencyInfo.setType(dependency.getType());
            dependencyInfo.setCreateTime(bootProjectConfig.now);
            dependencyInfo.setUpdateTime(bootProjectConfig.now);
            return dependencyInfo;
        });
    }

    public List<PropertiesInfo> buildPropertiesInfo(final ProjectInfo parentProjectInfo,
                                                    final MavenPomParseTool.ProjectInfoDto projectInfoDtoModel,
                                                    final GlobalProjectConfig globalProjectConfig) {
        return DataTool.mapToList(projectInfoDtoModel.getProperties(), (key, value) -> {
            PropertiesInfo propertiesInfo = new PropertiesInfo();
            propertiesInfo.setRootProjectKey(globalProjectConfig.rootProjectKey);
            propertiesInfo.setProjectInfoId(parentProjectInfo.getId());
            propertiesInfo.setProjectKey(parentProjectInfo.getProjectKey());
            propertiesInfo.setPropKey(key);
            propertiesInfo.setPropValue(value);
            propertiesInfo.setCreateTime(globalProjectConfig.now);
            propertiesInfo.setUpdateTime(globalProjectConfig.now);
            return propertiesInfo;
        });
    }

    public List<DependencyManagementInfo> buildDependencyManagementInfo(final ProjectInfo parentProjectInfo,
                                                                        final MavenPomParseTool.ProjectInfoDto projectInfoDtoModel,
                                                                        final GlobalProjectConfig globalProjectConfig) {
        return DataTool.toList(projectInfoDtoModel.getDependencyManagement(), dependencyManagement -> {
            DependencyManagementInfo dependencyManagementInfo = new DependencyManagementInfo();
            dependencyManagementInfo.setRootProjectKey(globalProjectConfig.rootProjectKey);
            dependencyManagementInfo.setProjectInfoId(parentProjectInfo.getId());
            dependencyManagementInfo.setProjectKey(parentProjectInfo.getProjectKey());
            dependencyManagementInfo.setGroupId(dependencyManagement.getGroupId());
            dependencyManagementInfo.setArtifactId(dependencyManagement.getArtifactId());
            dependencyManagementInfo.setVersion(dependencyManagement.getVersion());
            dependencyManagementInfo.setScope(dependencyManagement.getScope());
            dependencyManagementInfo.setType(dependencyManagement.getType());
            dependencyManagementInfo.setCreateTime(globalProjectConfig.now);
            dependencyManagementInfo.setUpdateTime(globalProjectConfig.now);
            return dependencyManagementInfo;
        });
    }

    public List<ModuleInfo> buildModuleInfo(final ProjectInfo parentProjectInfo,
                                            final MavenPomParseTool.ProjectInfoDto projectInfoDtoModel,
                                            final GlobalProjectConfig globalProjectConfig) {
        return DataTool.toList(projectInfoDtoModel.getModules(), module -> {
            ModuleInfo moduleInfo = new ModuleInfo();
            moduleInfo.setRootProjectKey(globalProjectConfig.rootProjectKey);
            moduleInfo.setProjectInfoId(parentProjectInfo.getId());
            moduleInfo.setProjectKey(parentProjectInfo.getProjectKey());
            moduleInfo.setProjectKey(SecureUtil.sha256(module.getAbsoluteFilePath()));
            moduleInfo.setAbsoluteFilePath(module.getAbsoluteFilePath());
            moduleInfo.setModule(module.getModuleName());
            moduleInfo.setCreateTime(globalProjectConfig.now);
            moduleInfo.setUpdateTime(globalProjectConfig.now);
            return moduleInfo;
        });
    }

    public ProjectInfo buildProjectInfo(final MavenPomParseTool.ProjectInfoDto projectInfoDto,
                                        final GlobalProjectConfig globalProjectConfig,
                                        final RootProjectFlagEnum rootProjectFlagEnum) {
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setRootProjectKey(globalProjectConfig.rootProjectKey);
        String absoluteFilePath = projectInfoDto.getAbsoluteFilePath();
        if(StringUtils.endsWith(absoluteFilePath, File.separator)) {
            absoluteFilePath = StringUtils.substringBeforeLast(absoluteFilePath, File.separator);
        }

        if(ROOT_FLAG.equals(rootProjectFlagEnum)) {
            projectInfo.setProjectKey(globalProjectConfig.rootProjectKey);
        } else {
            projectInfo.setProjectKey(SecureUtil.sha256(absoluteFilePath));
        }

        projectInfo.setAbsoluteFilePath(absoluteFilePath);
        projectInfo.setProjectAlias(projectInfoDto.getArtifactId());
        projectInfo.setGroupId(projectInfoDto.getGroupId());
        projectInfo.setArtifactId(projectInfoDto.getArtifactId());
        projectInfo.setVersion(projectInfoDto.getVersion());
        projectInfo.setPackaging(projectInfoDto.getPackaging());
        projectInfo.setName(projectInfoDto.getName());
        projectInfo.setDescription(projectInfoDto.getDescription());
        projectInfo.setRelativeFilePath(null);
        projectInfo.setParentFlag(ParentFlagEnum.packagingOf(projectInfoDto.getPackaging()).code);
        projectInfo.setRootProjectFlag(rootProjectFlagEnum.code);
        MavenPomParseTool.ParentInfoDto parentInfoDto = projectInfoDto.getParentInfoDto();
        projectInfo.setParentGroupId(parentInfoDto.getGroupId());
        projectInfo.setParentArtifactId(parentInfoDto.getArtifactId());
        projectInfo.setParentVersion(parentInfoDto.getVersion());
        projectInfo.setCreateTime(globalProjectConfig.now);
        projectInfo.setUpdateTime(globalProjectConfig.now);
        return projectInfo;
    }

}
