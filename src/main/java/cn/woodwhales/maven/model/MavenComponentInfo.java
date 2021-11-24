package cn.woodwhales.maven.model;

import java.util.List;

/**
 * @author woodwhales on 2021-11-16 20:17
 */
public class MavenComponentInfo {

    public Long projectInfoId;
    public String groupId;
    public String artifactId;
    public String version;
    public String alias;
    public List<MavenComponentInfo> childComponentList;

    public MavenComponentInfo(Long projectInfoId,
                              String groupId,
                              String artifactId,
                              String version,
                              String alias) {
        this.projectInfoId = projectInfoId;
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.alias = alias;
    }
}
