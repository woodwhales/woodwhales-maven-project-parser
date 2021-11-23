package cn.woodwhales.maven.util;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.code.Base64Coder;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * @author woodwhales on 2021-11-18 10:26
 */
@Slf4j
public class PlantumlTool {
    public static String generateImageBase64(String plantuml) {
        return generateImageBase64(plantuml, 10240);
    }

    public static String generateImageBase64(String plantuml, int plantumlLimitSize) {
        if(StringUtils.isBlank(plantuml)) {
            return "";
        }

        if (Objects.isNull(plantumlLimitSize)) {
            System.setProperty("PLANTUML_LIMIT_SIZE", "10240");
        } else {
            System.setProperty("PLANTUML_LIMIT_SIZE", String.valueOf(plantumlLimitSize));
        }

        SourceStringReader reader = new SourceStringReader(plantuml);
        byte[] imageBytes;
        try (ByteArrayOutputStream outStream = new ByteArrayOutputStream()) {
            reader.generateImage(outStream, 0, new FileFormatOption(FileFormat.PNG));
            imageBytes = outStream.toByteArray();
            final String base64 = Base64Coder.encodeLines(imageBytes).replaceAll("\\s", "");
            final String encodedBytes = "data:image/png;base64," + base64;
            return encodedBytes;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
