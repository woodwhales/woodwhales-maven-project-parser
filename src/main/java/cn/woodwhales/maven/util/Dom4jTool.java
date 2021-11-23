package cn.woodwhales.maven.util;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author woodwhales on 2021-11-15 10:21
 */
@Slf4j
public class Dom4jTool {

    /**
     * 获取文件的 Document 对象
     * @param filePathName 文件绝对目录
     * @return Document 对象+
     */
    public static Document load(String filePathName) {
        Document document = null;
        try {
            SAXReader reader = new SAXReader();
            document = reader.read(filePathName);
        } catch (DocumentException e) {
            log.error("read file process is failed, cause by : {}", e.getMessage(), e);
        }
        return document;
    }

    /**
     * 获取文件的 Document 对象
     * @param inputStream 文件输入流
     * @return Document 对象+
     */
    public static Document load(InputStream inputStream) {
        Document document = null;
        try {
            SAXReader reader = new SAXReader();
            document = reader.read(inputStream);
        } catch (DocumentException e) {
            log.error("read file process is failed, cause by : {}", e.getMessage(), e);
        }
        return document;
    }

    public static String getElementValue(Element element, String attributeName) {
        return element.elementText(attributeName);
    }

    public static Map<String, String> mapElementValue(Element element, String attributeName) {
        Element childElement = element.element(attributeName);
        if(Objects.isNull(childElement)) {
            return null;
        }
        List<Element> elements = childElement.elements();
        if(CollectionUtils.isEmpty(elements)) {
            return null;
        } else {
            Map<String, String> map = new HashMap<>();
            for (Element keyElement : elements) {
                String key = keyElement.getName();
                String value = getElementValue(childElement, key);
                map.put(key, value);
            }
            return map;
        }
    }

}
