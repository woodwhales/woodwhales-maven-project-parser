package cn.woodwhales.maven.model;

import java.util.Date;

/**
 * @author woodwhales on 2021-11-16 15:21
 */
public class GlobalProjectConfig {

    public final String absoluteFilePath;
    public final String rootProjectKey;
    public final Date now;

    public GlobalProjectConfig(String absoluteFilePath,
                               String rootProjectKey,
                               Date now) {
        this.absoluteFilePath = absoluteFilePath;
        this.rootProjectKey = rootProjectKey;
        this.now = now;
    }

    public GlobalProjectConfig(String rootProjectKey,
                               Date now) {
        this.absoluteFilePath = "";
        this.rootProjectKey = rootProjectKey;
        this.now = now;
    }

    public GlobalProjectConfig copyInstance(String absoluteFilePath) {
        return new GlobalProjectConfig(absoluteFilePath, this.rootProjectKey, this.now);
    }
}
