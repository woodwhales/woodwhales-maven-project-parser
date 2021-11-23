package cn.woodwhales.maven.model;

import java.util.Date;

/**
 * @author woodwhales on 2021-11-16 15:21
 */
public class BuildProjectConfig {

    public final String absoluteFilePath;
    public final String rootProjectKey;
    public final Date now;

    public BuildProjectConfig(String absoluteFilePath,
                              String rootProjectKey,
                              Date now) {
        this.absoluteFilePath = absoluteFilePath;
        this.rootProjectKey = rootProjectKey;
        this.now = now;
    }

    public BuildProjectConfig copyInstance(String absoluteFilePath) {
        return new BuildProjectConfig(absoluteFilePath, this.rootProjectKey, this.now);
    }
}
