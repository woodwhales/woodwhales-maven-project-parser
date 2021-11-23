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
import cn.woodwhales.maven.model.BuildProjectConfig;
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

        BuildProjectConfig buildProjectConfig = new BuildProjectConfig(absoluteFilePath, rootProjectKey, now);
        MavenPomParseTool.ProjectInfoDto projectInfo = this.save(buildProjectConfig, ROOT_FLAG);
        this.saveChild(projectInfo, buildProjectConfig);
        return OpResult.success(new BuildProjectResult(rootProjectKey));
    }

    private void deleteProjectInfo(String rootProjectKey) {
        projectInfoMapper.delete(Wrappers.<ProjectInfo> lambdaQuery().eq(ProjectInfo::getRootProjectKey, rootProjectKey));
        moduleInfoMapper.delete(Wrappers.<ModuleInfo> lambdaQuery().eq(ModuleInfo::getRootProjectKey, rootProjectKey));
        propertiesInfoMapper.delete(Wrappers.<PropertiesInfo> lambdaQuery().eq(PropertiesInfo::getRootProjectKey, rootProjectKey));
        dependencyManagementInfoMapper.delete(Wrappers.<DependencyManagementInfo> lambdaQuery().eq(DependencyManagementInfo::getRootProjectKey, rootProjectKey));
        dependencyInfoMapper.delete(Wrappers.<DependencyInfo> lambdaQuery().eq(DependencyInfo::getRootProjectKey, rootProjectKey));
        exclusionInfoMapper.delete(Wrappers.<ExclusionInfo> lambdaQuery().eq(ExclusionInfo::getRootProjectKey, rootProjectKey));
    }

    private MavenPomParseTool.ProjectInfoDto save(final BuildProjectConfig buildProjectConfig,
                                                  final RootProjectFlagEnum rootProjectFlagEnum) {
        MavenPomParseTool.ProjectInfoDto projectInfoDto = MavenPomParseTool.projectInfo(buildProjectConfig.absoluteFilePath);

        // 保存 project_info
        ProjectInfo projectInfo = buildProjectInfo(projectInfoDto, buildProjectConfig, rootProjectFlagEnum);
        batchProjectInfoMapper.save(projectInfo);

        final Long projectInfoId = projectInfo.getId();

        // 保存 module_info
        batchInsert(batchModuleInfoMapper, buildModuleInfo(projectInfoId, projectInfoDto, buildProjectConfig));

        // 保存 properties_info
        batchInsert(batchPropertiesInfoMapper, buildPropertiesInfo(projectInfoId, projectInfoDto, buildProjectConfig));

        // 保存 dependency_management_info
        batchInsert(batchDependencyManagementInfoMapper, buildDependencyManagementInfo(projectInfoId, projectInfoDto, buildProjectConfig));

        // 保存 dependency_info
        List<DependencyInfo> dependencyInfoList = buildDependencyInfo(projectInfoId, projectInfoDto, buildProjectConfig);
        batchInsert(batchDependencyInfoMapper, dependencyInfoList);

        // 保存 exclusion_info
        batchInsert(batchExclusionInfoMapper, buildExclusionInfo(projectInfoId, projectInfoDto, dependencyInfoList, buildProjectConfig));
        return projectInfoDto;
    }

    private void saveChild(final MavenPomParseTool.ProjectInfoDto projectInfoDtoModel,
                           final BuildProjectConfig buildProjectConfig) {
        if(ParentFlagEnum.PARENT_FLAG.match(projectInfoDtoModel.getPackaging())) {
            List<String> modules = projectInfoDtoModel.getModules();
            for (String module : modules) {
                String absoluteFilePath = projectInfoDtoModel.getAbsoluteFilePath();
                String moduleAbsoluteFilePath = absoluteFilePath + File.separator + module;
                BuildProjectConfig moduleBuildProjectConfig = buildProjectConfig.copyInstance(moduleAbsoluteFilePath);
                MavenPomParseTool.ProjectInfoDto moduleProjectInfoDto = this.save(moduleBuildProjectConfig, NON_ROOT_FLAG);
                this.saveChild(moduleProjectInfoDto, moduleBuildProjectConfig);
            }
        }
    }

    private List<ExclusionInfo> buildExclusionInfo(final Long projectInfoId,
                                                   final MavenPomParseTool.ProjectInfoDto projectInfoDtoModel,
                                                   final List<DependencyInfo> dependencyInfoList,
                                                   final BuildProjectConfig buildProjectConfig) {
        Map<String, DependencyInfo> dependencyInfoMap = DataTool.toMap(dependencyInfoList, dependencyInfo -> {
            String groupId = dependencyInfo.getGroupId();
            String artifactId = dependencyInfo.getArtifactId();
            String version = dependencyInfo.getVersion();
            return StringUtils.joinWith("_", groupId, artifactId, version);
        });

        List<MavenPomParseTool.DependencyInfoDto> dependencyDtoList = projectInfoDtoModel.getDependency();
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
                exclusionInfo.setRootProjectKey(buildProjectConfig.rootProjectKey);
                exclusionInfo.setDependencyInfoId(dependencyInfo.getId());
                exclusionInfo.setGroupId(exclusion.getGroupId());
                exclusionInfo.setArtifactId(exclusion.getArtifactId());
                exclusionInfo.setCreateTime(buildProjectConfig.now);
                exclusionInfo.setUpdateTime(buildProjectConfig.now);
                return exclusionInfo;
            }));
        }
        return exclusionInfoList;
    }

    private List<DependencyInfo> buildDependencyInfo(final Long projectInfoId,
                                                     final MavenPomParseTool.ProjectInfoDto projectInfoDtoModel,
                                                     final BuildProjectConfig bootProjectConfig) {
        return DataTool.toList(projectInfoDtoModel.getDependency(), dependency -> {
            DependencyInfo dependencyInfo = new DependencyInfo();
            dependencyInfo.setRootProjectKey(bootProjectConfig.rootProjectKey);
            dependencyInfo.setProjectInfoId(projectInfoId);
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

    public List<PropertiesInfo> buildPropertiesInfo(final Long projectInfoId,
                                                    final MavenPomParseTool.ProjectInfoDto projectInfoDtoModel,
                                                    final BuildProjectConfig buildProjectConfig) {
        return DataTool.mapToList(projectInfoDtoModel.getProperties(), (key, value) -> {
            PropertiesInfo propertiesInfo = new PropertiesInfo();
            propertiesInfo.setRootProjectKey(buildProjectConfig.rootProjectKey);
            propertiesInfo.setProjectInfoId(projectInfoId);
            propertiesInfo.setPropKey(key);
            propertiesInfo.setPropValue(value);
            propertiesInfo.setCreateTime(buildProjectConfig.now);
            propertiesInfo.setUpdateTime(buildProjectConfig.now);
            return propertiesInfo;
        });
    }

    public List<DependencyManagementInfo> buildDependencyManagementInfo(final Long projectInfoId,
                                                                        final MavenPomParseTool.ProjectInfoDto projectInfoDtoModel,
                                                                        final BuildProjectConfig buildProjectConfig) {
        return DataTool.toList(projectInfoDtoModel.getDependencyManagement(), dependencyManagement -> {
            DependencyManagementInfo dependencyManagementInfo = new DependencyManagementInfo();
            dependencyManagementInfo.setRootProjectKey(buildProjectConfig.rootProjectKey);
            dependencyManagementInfo.setProjectInfoId(projectInfoId);
            dependencyManagementInfo.setGroupId(dependencyManagement.getGroupId());
            dependencyManagementInfo.setArtifactId(dependencyManagement.getArtifactId());
            dependencyManagementInfo.setVersion(dependencyManagement.getVersion());
            dependencyManagementInfo.setScope(dependencyManagement.getScope());
            dependencyManagementInfo.setType(dependencyManagement.getType());
            dependencyManagementInfo.setCreateTime(buildProjectConfig.now);
            dependencyManagementInfo.setUpdateTime(buildProjectConfig.now);
            return dependencyManagementInfo;
        });
    }

    public List<ModuleInfo> buildModuleInfo(final Long projectInfoId,
                                            final MavenPomParseTool.ProjectInfoDto projectInfoDtoModel,
                                            final BuildProjectConfig buildProjectConfig) {
        return DataTool.toList(projectInfoDtoModel.getModules(), module -> {
            ModuleInfo moduleInfo = new ModuleInfo();
            moduleInfo.setRootProjectKey(buildProjectConfig.rootProjectKey);
            moduleInfo.setProjectInfoId(projectInfoId);
            moduleInfo.setModule(module);
            moduleInfo.setCreateTime(buildProjectConfig.now);
            moduleInfo.setUpdateTime(buildProjectConfig.now);
            return moduleInfo;
        });
    }

    public ProjectInfo buildProjectInfo(final MavenPomParseTool.ProjectInfoDto projectInfoDtoModel,
                                        final BuildProjectConfig buildProjectConfig,
                                        final RootProjectFlagEnum rootProjectFlagEnum) {
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setRootProjectKey(buildProjectConfig.rootProjectKey);
        String absoluteFilePath = projectInfoDtoModel.getAbsoluteFilePath();
        if(StringUtils.endsWith(absoluteFilePath, File.separator)) {
            absoluteFilePath = StringUtils.substringBeforeLast(absoluteFilePath, File.separator);
        }
        projectInfo.setAbsoluteFilePath(absoluteFilePath);
        projectInfo.setProjectAlias(projectInfoDtoModel.getArtifactId());
        projectInfo.setGroupId(projectInfoDtoModel.getGroupId());
        projectInfo.setArtifactId(projectInfoDtoModel.getArtifactId());
        projectInfo.setVersion(projectInfoDtoModel.getVersion());
        projectInfo.setPackaging(projectInfoDtoModel.getPackaging());
        projectInfo.setName(projectInfoDtoModel.getName());
        projectInfo.setDescription(projectInfoDtoModel.getDescription());
        projectInfo.setRelativeFilePath(null);
        projectInfo.setParentFlag(ParentFlagEnum.packagingOf(projectInfoDtoModel.getPackaging()).code);
        projectInfo.setRootProjectFlag(rootProjectFlagEnum.code);
        MavenPomParseTool.ParentInfoDto parentInfoDto = projectInfoDtoModel.getParentInfoDto();
        projectInfo.setParentGroupId(parentInfoDto.getGroupId());
        projectInfo.setParentArtifactId(parentInfoDto.getArtifactId());
        projectInfo.setParentVersion(parentInfoDto.getVersion());
        projectInfo.setCreateTime(buildProjectConfig.now);
        projectInfo.setUpdateTime(buildProjectConfig.now);
        return projectInfo;
    }



}
