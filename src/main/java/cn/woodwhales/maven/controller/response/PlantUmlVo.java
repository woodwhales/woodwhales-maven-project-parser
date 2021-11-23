package cn.woodwhales.maven.controller.response;

import cn.woodwhales.maven.util.PlantumlTool;
import lombok.Data;

/**
 * @author woodwhales on 2021-11-16 19:42
 */
@Data
public class PlantUmlVo {

    private String modules;

    private String modulesImageBase64;

    private String relations;

    private String relationsImageBase64;

    public PlantUmlVo(String modules, String relations) {
        this.modules = modules;
        this.relations = relations;
        this.modulesImageBase64 = PlantumlTool.generateImageBase64(this.modules);
        this.relationsImageBase64 = PlantumlTool.generateImageBase64(this.relations);
    }


}
