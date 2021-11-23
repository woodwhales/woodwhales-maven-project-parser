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

import static cn.woodwhales.maven.util.Dom4jTool.getElementValue;
import static java.util.Collections.emptyList;

/**
 * @author woodwhales on 2021-11-15 11:38
 */
public class MavenPomParseTool {

    public static ProjectInfoDto projectInfo(String absoluteFilePath) {
        File file = new File(absoluteFilePath);
        if(!file.exists()) {
            throw new RuntimeException(String.format("%s 文件目录不存在", absoluteFilePath));
        }

        ProjectInfoDto projectInfoDto = new ProjectInfoDto(absoluteFilePath).build();
        return projectInfoDto;
    }

    public static ProjectInfoDto projectInfo(Element rootElement, ProjectInfoDto projectInfoDto) {
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
        projectInfoDto.setModules(MavenPomParseTool.modules(rootElement));
        // 设置 properties
        projectInfoDto.setProperties(MavenPomParseTool.properties(rootElement));
        // 设置 dependencyManagement
        projectInfoDto.setDependencyManagement(MavenPomParseTool.dependencyManagement(rootElement));
        // 设置 dependencies
        projectInfoDto.setDependency(MavenPomParseTool.dependencies(rootElement));

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
        List<Element> dependencyElementList = dependenciesElementList.elements();
        for (Element dependencyElement : dependencyElementList) {
            DependencyInfoDto dependencyInfoDto = null;
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

    public static List<String> modules(Element rootElement) {
        Element modulesElement = rootElement.element("modules");
        List<String> modules = null;
        if(Objects.nonNull(modulesElement)) {
            modules = new ArrayList<>();
            List<Element> elements = modulesElement.elements();

            for (Element moduleElement : elements) {
                modules.add(String.valueOf(moduleElement.getData()));
            }
        }
        return modules;
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
        private List<String> modules;
        private Map<String, String> properties;
        private List<DependencyInfoDto> dependencyManagement;
        private List<DependencyInfoDto> dependency;

        public ProjectInfoDto(String absoluteFilePath) {
            this.absoluteFilePath = absoluteFilePath;
            this.pomAbsoluteFilePath = this.absoluteFilePath + File.separator + "pom.xml";
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

        public ProjectInfoDto build() {
            Document rooDocument = Dom4jTool.load(this.pomAbsoluteFilePath);
            Element rootElement = rooDocument.getRootElement();
            MavenPomParseTool.projectInfo(rootElement, this);
            return this;
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
