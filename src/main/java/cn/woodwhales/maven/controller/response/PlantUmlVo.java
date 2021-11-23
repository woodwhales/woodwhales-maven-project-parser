package cn.woodwhales.maven.controller.response;

import lombok.Data;

/**
 * @author woodwhales on 2021-11-16 19:42
 */
@Data
public class PlantUmlVo {

    private String modules;

    private String relations;

    public PlantUmlVo(String modules, String relations) {
        this.modules = modules;
        this.relations = relations;
    }
}
