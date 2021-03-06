package cn.woodwhales.maven.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cn.woodwhales.common.business.DataTool.toList;
import static cn.woodwhales.maven.util.Dom4jTool.getElementValue;
import static java.util.Collections.emptyList;

/**
 * @author woodwhales on 2021-11-15 11:38
 */
public class MavenPomParseTool {

    public static ProjectInfoDto buildProjectInfo(String absoluteFilePath) {
        File file = new File(absoluteFilePath);
        if(!file.exists()) {
            throw new RuntimeException(String.format("%s 文件目录不存在", absoluteFilePath));
        }

        return new ProjectInfoDto(absoluteFilePath);
    }

    private static ProjectInfoDto buildProjectInfo(Element rootElement, ProjectInfoDto projectInfoDto) {
        String groupId = getElementValue(rootElement, "groupId");
        String artifactId = getElementValue(rootElement, "artifactId");
        String version = getElementValue(rootElement, "version");
        String packaging = getElementValue(rootElement, "packaging");
        String name = getElementValue(rootElement, "name");
        String description = getElementValue(rootElement, "description");

        // 构建基础信息
        projectInfoDto.buildBaseInfo(groupId, artifactId, version, packaging, name, description);
        // 设置 parent
        projectInfoDto.setParentInfoDto(MavenPomParseTool.parentInfo(rootElement));
        // 设置 modules
        projectInfoDto.setModules(MavenPomParseTool.modules(rootElement, projectInfoDto));
        // 设置 properties
        projectInfoDto.setProperties(MavenPomParseTool.properties(rootElement));
        // 设置 dependencyManagement
        projectInfoDto.setDependencyManagement(MavenPomParseTool.dependencyManagement(rootElement));
        // 设置 dependencies
        projectInfoDto.setDependencyList(MavenPomParseTool.dependencies(rootElement));
        // 设置
        projectInfoDto.setSubProjectInfoList(toList(projectInfoDto.getModules(), ProjectInfoDto::new));

        return projectInfoDto;
    }

    public static Map<String, String> properties(Element rootElement) {
        return Dom4jTool.mapElementValue(rootElement, "properties");
    }

    public static ParentInfoDto parentInfo(Element rootElement) {
        Element parentElement = rootElement.element("parent");
        ParentInfoDto parentInfoDto;
        if(Objects.isNull(parentElement)) {
            parentInfoDto = new ParentInfoDto();
        } else {
            String groupId = getElementValue(parentElement, "groupId");
            String artifactId = getElementValue(parentElement, "artifactId");
            String version = getElementValue(parentElement, "version");
            parentInfoDto = new ParentInfoDto(groupId, artifactId, version);
        }
        return parentInfoDto;
    }

    public static List<DependencyInfoDto> dependencyManagement(Element rootElement) {
        List<DependencyInfoDto> dependencyInfoDtoList = new ArrayList<>();
        Element dependencyManagementElement = rootElement.element("dependencyManagement");
        if(Objects.nonNull(dependencyManagementElement)) {
            Element dependenciesElementList = dependencyManagementElement.element("dependencies");
            return getDependencyInfoList(dependencyInfoDtoList, dependenciesElementList);
        }
        return emptyList();
    }

    public static List<DependencyInfoDto> dependencies(Element rootElement) {
        List<DependencyInfoDto> dependencyInfoDtoList = new ArrayList<>();
        Element dependenciesElementList = rootElement.element("dependencies");
        return getDependencyInfoList(dependencyInfoDtoList, dependenciesElementList);
    }

    private static List<DependencyInfoDto> getDependencyInfoList(List<DependencyInfoDto> dependencyInfoDtoList, Element dependenciesElementList) {
        if(Objects.isNull(dependenciesElementList)) {
            return emptyList();
        }

        List<Element> dependencyElementList = dependenciesElementList.elements();
        for (Element dependencyElement : dependencyElementList) {
            DependencyInfoDto dependencyInfoDto;
            if(Objects.isNull(dependencyElement)) {
                dependencyInfoDto = new DependencyInfoDto();
            } else {
                List<Element> elements = dependencyElement.elements();
                if(CollectionUtils.isEmpty(elements)) {
                    dependencyInfoDto = new DependencyInfoDto();
                } else {
                    String groupId = getElementValue(dependencyElement, "groupId");
                    String artifactId = getElementValue(dependencyElement, "artifactId");
                    String version = getElementValue(dependencyElement, "version");
                    String scope = getElementValue(dependencyElement, "scope");
                    String type = getElementValue(dependencyElement, "type");
                    dependencyInfoDto = new DependencyInfoDto(groupId, artifactId, version, scope, type);
                    Element exclusionsElement = dependencyElement.element("exclusions");
                    if(Objects.nonNull(exclusionsElement)) {
                        List<Element> exclusionElements = exclusionsElement.elements();
                        for (Element exclusionElement : exclusionElements) {
                            String exclusionGroupId = getElementValue(exclusionElement, "groupId");
                            String exclusionArtifactId = getElementValue(exclusionElement, "artifactId");
                            dependencyInfoDto.addExclusion(new DependencyInfoDto.DependencyExclusionInfoDto(exclusionGroupId, exclusionArtifactId));
                        }
                    }
                }
            }
            dependencyInfoDtoList.add(dependencyInfoDto);
        }
        return dependencyInfoDtoList;
    }

    public static List<ModuleInfoDto> modules(Element rootElement, ProjectInfoDto projectInfoDto) {
        Element modulesElement = rootElement.element("modules");
        List<ModuleInfoDto> moduleInfoDtoList = null;
        if(Objects.nonNull(modulesElement)) {
            moduleInfoDtoList = new ArrayList<>();
            List<Element> elements = modulesElement.elements();
            for (Element moduleElement : elements) {
                moduleInfoDtoList.add(new ModuleInfoDto(projectInfoDto.getAbsoluteFilePath(), String.valueOf(moduleElement.getData())));
            }
        }
        return moduleInfoDtoList;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ParentInfoDto {
        private String groupId;
        private String artifactId;
        private String version;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ModuleInfoDto {
        /**
         * module 名称
         */
        private String moduleName;

        /**
         * 父工程文件绝对路径
         */
        private String parentFilePath;

        /**
         * 项目绝对路径
         */
        private String absoluteFilePath;

        /**
         * pom 文件绝对路径
         */
        private String pomAbsoluteFilePath;

        public ModuleInfoDto(String parentFilePath, String moduleName) {
            this.parentFilePath = parentFilePath;
            this.absoluteFilePath = parentFilePath + File.separator + moduleName;
            this.pomAbsoluteFilePath = this.absoluteFilePath + File.separator + "pom.xml";
            this.moduleName = moduleName;
        }
    }

    @Data
    public static class ProjectInfoDto {
        /**
         * 项目绝对路径
         */
        private String absoluteFilePath;

        /**
         * pom 文件绝对路径
         */
        private String pomAbsoluteFilePath;

        private String groupId;
        private String artifactId;
        private String version;
        private String packaging;
        private String name;
        private String description;

        private ParentInfoDto parentInfoDto;
        private List<ModuleInfoDto> modules;
        private Map<String, String> properties;
        private List<DependencyInfoDto> dependencyManagement;
        private List<DependencyInfoDto> dependencyList;
        private List<ProjectInfoDto> subProjectInfoList;

        public ProjectInfoDto(String absoluteFilePath) {
            this.absoluteFilePath = absoluteFilePath;
            this.pomAbsoluteFilePath = this.absoluteFilePath + File.separator + "pom.xml";
            build();
        }

        public ProjectInfoDto(ModuleInfoDto moduleInfoDto) {
            this.absoluteFilePath = moduleInfoDto.getAbsoluteFilePath();
            this.pomAbsoluteFilePath = this.absoluteFilePath + File.separator + "pom.xml";
            build();
        }

        private ProjectInfoDto buildBaseInfo(String groupId, String artifactId, String version,
                                             String packaging, String name, String description) {
            this.groupId = groupId;
            this.artifactId = artifactId;
            this.version = version;
            this.packaging = packaging;
            this.name = name;
            this.description = description;
            return this;
        }

        private void build() {
            Document rootDocument = Dom4jTool.load(this.pomAbsoluteFilePath);
            Element rootElement = rootDocument.getRootElement();
            MavenPomParseTool.buildProjectInfo(rootElement, this);
        }

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DependencyInfoDto {
        private String groupId;
        private String artifactId;
        private String version;
        private String scope;
        private String type;

        public DependencyInfoDto(String groupId, String artifactId, String version, String scope, String type) {
            this.groupId = groupId;
            this.artifactId = artifactId;
            this.version = version;
            this.scope = scope;
            this.type = type;
        }

        List<DependencyExclusionInfoDto> exclusions = new ArrayList<>();

        public void addExclusion(DependencyExclusionInfoDto dependencyExclusionInfoDto) {
            exclusions.add(dependencyExclusionInfoDto);
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class DependencyExclusionInfoDto {
            private String groupId;
            private String artifactId;
        }
    }


}
