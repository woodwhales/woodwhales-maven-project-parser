package cn.woodwhales.maven.model;

import lombok.Data;

/**
 * @author woodwhales on 2021-11-16 19:25
 */
@Data
public class BuildProjectResult {

    private final String rootProjectKey;

    public BuildProjectResult(String rootProjectKey) {
        this.rootProjectKey = rootProjectKey;
    }

}
