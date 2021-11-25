package cn.woodwhales.maven.model;

import cn.woodwhales.maven.util.MavenPomParseTool;
import lombok.Data;

/**
 * @author woodwhales on 2021-11-16 19:25
 */
@Data
public class BuildProjectResult {

    private final MavenPomParseTool.ProjectInfoDto projectInfoDto;
    private final String rootProjectKey;

    public BuildProjectResult(MavenPomParseTool.ProjectInfoDto projectInfoDto, String rootProjectKey) {
        this.projectInfoDto = projectInfoDto;
        this.rootProjectKey = rootProjectKey;
    }
}
