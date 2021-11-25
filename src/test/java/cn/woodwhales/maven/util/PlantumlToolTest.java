package cn.woodwhales.maven.util;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author woodwhales on 2021-11-17 10:30
 */
public class PlantumlToolTest {

    @Test
    public void test() throws IOException {
        File file = new File("src/test/resources/index.html");
        FileUtils.writeStringToFile(file, "<img src=\"\"/>", StandardCharsets.UTF_8);
        System.setProperty("PLANTUML_LIMIT_SIZE", "10240");
        String imageBase64 = PlantumlTool.generateImageBase64(getUml());
        FileUtils.writeStringToFile(file, "<img src=\"" + imageBase64 + "\"/>", StandardCharsets.UTF_8);
    }

    private String getUml() {
        String uml = "@startuml\n" +
                "[ keycloak-dependencies-server-min ] -down-> [ keycloak-common ]\n" +
                "[ keycloak-servlet-filter-adapter ] -down-> [ keycloak-adapter-spi ]\n" +
                "[ keycloak-jetty-adapter-spi ] -down-> [ keycloak-adapter-spi ]\n" +
                "[ spring-boot-container-bundle ] -down-> [ keycloak-jetty93-adapter ]\n" +
                "[ keycloak-wildfly-extensions ] -down-> [ keycloak-services ]\n" +
                "[ kcinit ] -down-> [ keycloak-installed-adapter ]\n" +
                "[ keycloak-jetty93-adapter ] -down-> [ keycloak-adapter-core ]\n" +
                "[ keycloak-spring-boot-adapter-core ] -down-> [ keycloak-spring-security-adapter ]\n" +
                "[ keycloak-saml-wildfly-elytron-adapter ] -down-> [ keycloak-saml-core ]\n" +
                "[ keycloak-wildfly-elytron-oidc-adapter ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-saml-core ] -down-> [ keycloak-common ]\n" +
                "[ keycloak-sssd-federation ] -down-> [ keycloak-server-spi-private ]\n" +
                "[ keycloak-saml-wildfly-adapter ] -down-> [ keycloak-saml-adapter-api-public ]\n" +
                "[ keycloak-ldap-federation ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-wildfly-adduser ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-legacy-spring-boot-starter ] -down-> [ keycloak-spring-security-adapter ]\n" +
                "[ keycloak-saml-core-public ] -down-> [ keycloak-common ]\n" +
                "[ keycloak-tomcat-adapter ] -down-> [ keycloak-adapter-core ]\n" +
                "[ keycloak-jetty92-adapter ] -down-> [ keycloak-adapter-core ]\n" +
                "[ keycloak-osgi-adapter ] -down-> [ keycloak-adapter-spi ]\n" +
                "[ keycloak-jetty93-adapter ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-kerberos-federation ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-wildfly-adapter ] -down-> [ keycloak-adapter-core ]\n" +
                "[ keycloak-saml-undertow-adapter ] -down-> [ keycloak-saml-core ]\n" +
                "[ keycloak-sssd-federation ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-services ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-spring-boot-starter ] -down-> [ spring-boot-container-bundle ]\n" +
                "[ keycloak-util-embedded-ldap ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-spring-boot-2-adapter ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-as7-adapter ] -down-> [ keycloak-jboss-adapter-core ]\n" +
                "[ keycloak-tomcat-adapter-spi ] -down-> [ keycloak-common ]\n" +
                "[ keycloak-jetty92-adapter ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-undertow-adapter ] -down-> [ keycloak-adapter-core ]\n" +
                "[ keycloak-servlet-filter-adapter ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-servlet-adapter-spi ] -down-> [ keycloak-common ]\n" +
                "[ keycloak-authz-policy-common ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-saml-adapter-api-public ] -down-> [ keycloak-adapter-spi ]\n" +
                "[ keycloak-spring-boot-adapter ] -down-> [ keycloak-core ]\n" +
                "[ spring-boot-container-bundle ] -down-> [ keycloak-tomcat-adapter ]\n" +
                "[ keycloak-saml-wildfly-adapter ] -down-> [ keycloak-common ]\n" +
                "[ keycloak-model-infinispan ] -down-> [ keycloak-server-spi ]\n" +
                "[ keycloak-spring-security-adapter ] -down-> [ keycloak-adapter-core ]\n" +
                "[ keycloak-servlet-oauth-client ] -down-> [ keycloak-adapter-core ]\n" +
                "[ keycloak-spring-boot-2-adapter ] -down-> [ keycloak-spring-security-adapter ]\n" +
                "[ keycloak-server-spi-private ] -down-> [ keycloak-server-spi ]\n" +
                "[ keycloak-spring-boot-2-adapter ] -down-> [ keycloak-spring-boot-adapter-core ]\n" +
                "[ keycloak-wildfly-subsystem ] -down-> [ keycloak-wildfly-elytron-oidc-adapter ]\n" +
                "[ keycloak-authz-policy-common ] -down-> [ keycloak-server-spi ]\n" +
                "[ keycloak-kerberos-federation ] -down-> [ keycloak-server-spi ]\n" +
                "[ keycloak-client-registration-api ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-jetty-adapter-spi ] -down-> [ keycloak-common ]\n" +
                "[ keycloak-saml-wildfly-elytron-adapter ] -down-> [ keycloak-common ]\n" +
                "[ keycloak-wildfly-adapter ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-services ] -down-> [ keycloak-server-spi ]\n" +
                "[ keycloak-wildfly-server-subsystem ] -down-> [ keycloak-server-spi-private ]\n" +
                "[ keycloak-spring-boot-adapter ] -down-> [ spring-boot-container-bundle ]\n" +
                "[ keycloak-wildfly-elytron-oidc-adapter ] -down-> [ keycloak-adapter-spi ]\n" +
                "[ keycloak-saml-adapter-core ] -down-> [ keycloak-saml-core ]\n" +
                "[ keycloak-wildfly-extensions ] -down-> [ keycloak-server-spi-private ]\n" +
                "[ keycloak-kerberos-federation ] -down-> [ keycloak-server-spi-private ]\n" +
                "[ keycloak-ldap-federation ] -down-> [ keycloak-server-spi-private ]\n" +
                "[ keycloak-saml-wildfly-elytron-adapter ] -down-> [ keycloak-adapter-core ]\n" +
                "[ keycloak-spring-boot-2-adapter ] -down-> [ spring-boot-container-bundle ]\n" +
                "[ keycloak-dependencies-server-all ] -down-> [ keycloak-sssd-federation ]\n" +
                "[ keycloak-admin-cli ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-spring-security-adapter ] -down-> [ keycloak-adapter-spi ]\n" +
                "[ keycloak-dependencies-server-all ] -down-> [ keycloak-ldap-federation ]\n" +
                "[ keycloak-tomcat-core-adapter ] -down-> [ keycloak-adapter-core ]\n" +
                "[ keycloak-legacy-spring-boot-starter ] -down-> [ keycloak-spring-boot-adapter ]\n" +
                "[ keycloak-osgi-adapter ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-jaxrs-oauth-client ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-legacy-spring-boot-starter ] -down-> [ spring-boot-container-bundle ]\n" +
                "[ keycloak-wildfly-adapter ] -down-> [ keycloak-jboss-adapter-core ]\n" +
                "[ keycloak-saml-adapter-api-public ] -down-> [ keycloak-common ]\n" +
                "[ keycloak-jetty-core ] -down-> [ keycloak-adapter-core ]\n" +
                "[ keycloak-saml-undertow-adapter ] -down-> [ keycloak-saml-adapter-api-public ]\n" +
                "[ keycloak-saml-tomcat-adapter-core ] -down-> [ keycloak-tomcat-adapter-spi ]\n" +
                "[ keycloak-dependencies-server-all ] -down-> [ keycloak-authz-policy-common ]\n" +
                "[ keycloak-servlet-oauth-client ] -down-> [ keycloak-adapter-spi ]\n" +
                "[ keycloak-authz-policy-common ] -down-> [ keycloak-server-spi-private ]\n" +
                "[ keycloak-saml-servlet-filter-adapter ] -down-> [ keycloak-adapter-spi ]\n" +
                "[ keycloak-saml-tomcat-adapter-core ] -down-> [ keycloak-adapter-spi ]\n" +
                "[ keycloak-authz-client ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-adapter-core ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-saml-tomcat-adapter-core ] -down-> [ keycloak-saml-core ]\n" +
                "[ keycloak-legacy-spring-boot-starter ] -down-> [ keycloak-authz-client ]\n" +
                "[ keycloak-model-jpa ] -down-> [ keycloak-server-spi ]\n" +
                "[ keycloak-services ] -down-> [ keycloak-common ]\n" +
                "[ keycloak-jaxrs-oauth-client ] -down-> [ keycloak-adapter-spi ]\n" +
                "[ keycloak-camel-undertow ] -down-> [ keycloak-undertow-adapter ]\n" +
                "[ keycloak-model-infinispan ] -down-> [ keycloak-server-spi-private ]\n" +
                "[ keycloak-saml-adapter-api-public ] -down-> [ keycloak-saml-core-public ]\n" +
                "[ keycloak-as7-adapter ] -down-> [ keycloak-tomcat-core-adapter ]\n" +
                "[ keycloak-server-spi-private ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-jetty93-adapter ] -down-> [ keycloak-jetty-core ]\n" +
                "[ keycloak-tomcat-core-adapter ] -down-> [ keycloak-adapter-spi ]\n" +
                "[ keycloak-jetty92-adapter ] -down-> [ keycloak-jetty-core ]\n" +
                "[ keycloak-dependencies-server-min ] -down-> [ keycloak-services ]\n" +
                "[ keycloak-dependencies-server-min ] -down-> [ keycloak-server-spi-private ]\n" +
                "[ keycloak-services ] -down-> [ keycloak-server-spi-private ]\n" +
                "[ keycloak-dependencies-server-all ] -down-> [ keycloak-kerberos-federation ]\n" +
                "[ keycloak-as7-adapter ] -down-> [ keycloak-as7-adapter-spi ]\n" +
                "[ keycloak-saml-undertow-adapter ] -down-> [ keycloak-adapter-spi ]\n" +
                "[ keycloak-tomcat-core-adapter ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-saml-wildfly-adapter ] -down-> [ keycloak-jboss-adapter-core ]\n" +
                "[ keycloak-model-infinispan ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-as7-adapter ] -down-> [ keycloak-adapter-core ]\n" +
                "[ keycloak-saml-wildfly-adapter ] -down-> [ keycloak-saml-undertow-adapter ]\n" +
                "[ keycloak-admin-client ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-pax-web-undertow ] -down-> [ keycloak-undertow-adapter ]\n" +
                "[ keycloak-undertow-adapter-spi ] -down-> [ keycloak-adapter-spi ]\n" +
                "[ keycloak-saml-as7-adapter ] -down-> [ keycloak-adapter-spi ]\n" +
                "[ keycloak-installed-adapter ] -down-> [ keycloak-adapter-spi ]\n" +
                "[ keycloak-dependencies-server-min ] -down-> [ keycloak-js-adapter ]\n" +
                "[ keycloak-wildfly-adapter ] -down-> [ keycloak-undertow-adapter-spi ]\n" +
                "[ keycloak-undertow-adapter-spi ] -down-> [ keycloak-common ]\n" +
                "[ keycloak-jaxrs-oauth-client ] -down-> [ keycloak-adapter-core ]\n" +
                "[ keycloak-client-cli-dist ] -down-> [ keycloak-client-registration-cli ]\n" +
                "[ keycloak-dependencies-server-min ] -down-> [ keycloak-themes ]\n" +
                "[ keycloak-saml-wildfly-subsystem ] -down-> [ keycloak-saml-wildfly-adapter ]\n" +
                "[ keycloak-jetty-core ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-tomcat-adapter-spi ] -down-> [ keycloak-adapter-spi ]\n" +
                "[ keycloak-client-registration-cli ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-wildfly-server-subsystem ] -down-> [ keycloak-services ]\n" +
                "[ keycloak-wildfly-adduser ] -down-> [ keycloak-server-spi-private ]\n" +
                "[ keycloak-saml-core ] -down-> [ keycloak-saml-core-public ]\n" +
                "[ keycloak-wildfly-adapter ] -down-> [ keycloak-adapter-spi ]\n" +
                "[ keycloak-saml-as7-adapter ] -down-> [ keycloak-saml-adapter-core ]\n" +
                "[ keycloak-saml-wildfly-elytron-adapter ] -down-> [ keycloak-saml-adapter-core ]\n" +
                "[ keycloak-model-jpa ] -down-> [ keycloak-services ]\n" +
                "[ keycloak-saml-adapter-core ] -down-> [ keycloak-common ]\n" +
                "[ keycloak-servlet-filter-adapter ] -down-> [ keycloak-servlet-adapter-spi ]\n" +
                "[ keycloak-saml-wildfly-adapter ] -down-> [ keycloak-undertow-adapter-spi ]\n" +
                "[ keycloak-servlet-oauth-client ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-saml-wildfly-adapter ] -down-> [ keycloak-adapter-spi ]\n" +
                "[ keycloak-authz-policy-drools ] -down-> [ keycloak-server-spi-private ]\n" +
                "[ keycloak-ldap-federation ] -down-> [ keycloak-kerberos-federation ]\n" +
                "[ keycloak-authz-policy-drools ] -down-> [ keycloak-server-spi ]\n" +
                "[ keycloak-pax-web-undertow ] -down-> [ keycloak-osgi-adapter ]\n" +
                "[ keycloak-as7-adapter-spi ] -down-> [ keycloak-adapter-spi ]\n" +
                "[ keycloak-dependencies-server-all ] -down-> [ keycloak-authz-policy-drools ]\n" +
                "[ keycloak-saml-wildfly-subsystem ] -down-> [ keycloak-saml-wildfly-elytron-adapter ]\n" +
                "[ keycloak-tomcat-adapter ] -down-> [ keycloak-tomcat-core-adapter ]\n" +
                "[ keycloak-servlet-filter-adapter ] -down-> [ keycloak-adapter-core ]\n" +
                "[ keycloak-authz-policy-drools ] -down-> [ keycloak-services ]\n" +
                "[ keycloak-saml-adapter-core ] -down-> [ keycloak-saml-core-public ]\n" +
                "[ keycloak-servlet-adapter-spi ] -down-> [ keycloak-adapter-spi ]\n" +
                "[ keycloak-undertow-adapter ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-wildfly-adduser ] -down-> [ keycloak-common ]\n" +
                "[ keycloak-model-jpa ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-dependencies-server-all ] -down-> [ keycloak-dependencies-server-min ]\n" +
                "[ keycloak-spring-boot-starter ] -down-> [ keycloak-spring-security-adapter ]\n" +
                "[ keycloak-spring-boot-starter ] -down-> [ keycloak-authz-client ]\n" +
                "[ keycloak-wildfly-extensions ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-wildfly-adduser ] -down-> [ keycloak-server-spi ]\n" +
                "[ keycloak-client-cli-dist ] -down-> [ keycloak-admin-cli ]\n" +
                "[ keycloak-services ] -down-> [ keycloak-saml-core ]\n" +
                "[ spring-boot-container-bundle ] -down-> [ keycloak-undertow-adapter ]\n" +
                "[ keycloak-wildfly-elytron-oidc-adapter ] -down-> [ keycloak-adapter-core ]\n" +
                "[ keycloak-as7-subsystem ] -down-> [ keycloak-as7-adapter ]\n" +
                "[ keycloak-saml-undertow-adapter ] -down-> [ keycloak-common ]\n" +
                "[ keycloak-saml-tomcat-adapter-core ] -down-> [ keycloak-common ]\n" +
                "[ keycloak-server-spi ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-dependencies-server-all ] -down-> [ keycloak-model-infinispan ]\n" +
                "[ keycloak-saml-adapter-api-public ] -down-> [ keycloak-saml-core ]\n" +
                "[ keycloak-dependencies-server-min ] -down-> [ keycloak-server-spi ]\n" +
                "[ keycloak-services ] -down-> [ keycloak-saml-core-public ]\n" +
                "[ keycloak-saml-servlet-filter-adapter ] -down-> [ keycloak-saml-core ]\n" +
                "[ keycloak-installed-adapter ] -down-> [ keycloak-adapter-core ]\n" +
                "[ keycloak-test-helper ] -down-> [ keycloak-client-registration-api ]\n" +
                "[ keycloak-wildfly-subsystem ] -down-> [ keycloak-wildfly-adapter ]\n" +
                "[ keycloak-model-jpa ] -down-> [ keycloak-server-spi-private ]\n" +
                "[ keycloak-spring-boot-adapter ] -down-> [ keycloak-spring-boot-adapter-core ]\n" +
                "[ keycloak-jboss-adapter-core ] -down-> [ keycloak-common ]\n" +
                "[ keycloak-saml-as7-subsystem ] -down-> [ keycloak-saml-as7-adapter ]\n" +
                "[ keycloak-dependencies-server-all ] -down-> [ keycloak-saml-core ]\n" +
                "[ keycloak-test-helper ] -down-> [ keycloak-admin-client ]\n" +
                "[ keycloak-osgi-adapter ] -down-> [ keycloak-adapter-core ]\n" +
                "[ keycloak-as7-adapter-spi ] -down-> [ keycloak-tomcat-adapter-spi ]\n" +
                "[ keycloak-saml-as7-adapter ] -down-> [ keycloak-as7-adapter-spi ]\n" +
                "[ keycloak-adapter-core ] -down-> [ keycloak-adapter-spi ]\n" +
                "[ keycloak-saml-servlet-filter-adapter ] -down-> [ keycloak-common ]\n" +
                "[ keycloak-saml-adapter-core ] -down-> [ keycloak-adapter-spi ]\n" +
                "[ keycloak-saml-undertow-adapter ] -down-> [ keycloak-saml-adapter-core ]\n" +
                "[ keycloak-services ] -down-> [ keycloak-ldap-federation ]\n" +
                "[ keycloak-core ] -down-> [ keycloak-common ]\n" +
                "[ keycloak-sssd-federation ] -down-> [ keycloak-server-spi ]\n" +
                "[ keycloak-undertow-adapter ] -down-> [ keycloak-adapter-spi ]\n" +
                "[ keycloak-tomcat-core-adapter ] -down-> [ keycloak-authz-client ]\n" +
                "[ keycloak-saml-servlet-filter-adapter ] -down-> [ keycloak-servlet-adapter-spi ]\n" +
                "[ keycloak-wildfly-extensions ] -down-> [ keycloak-server-spi ]\n" +
                "[ keycloak-saml-adapter-core ] -down-> [ keycloak-saml-adapter-api-public ]\n" +
                "[ keycloak-spring-boot-adapter-core ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-spring-security-adapter ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-jetty-core ] -down-> [ keycloak-jetty-adapter-spi ]\n" +
                "[ keycloak-tomcat-core-adapter ] -down-> [ keycloak-tomcat-adapter-spi ]\n" +
                "[ keycloak-saml-wildfly-elytron-adapter ] -down-> [ keycloak-adapter-spi ]\n" +
                "[ keycloak-saml-tomcat-adapter-core ] -down-> [ keycloak-saml-adapter-core ]\n" +
                "[ keycloak-dependencies-server-min ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-dependencies-server-all ] -down-> [ keycloak-model-jpa ]\n" +
                "[ keycloak-saml-as7-adapter ] -down-> [ keycloak-common ]\n" +
                "[ keycloak-saml-wildfly-elytron-adapter ] -down-> [ keycloak-saml-adapter-api-public ]\n" +
                "[ keycloak-saml-servlet-filter-adapter ] -down-> [ keycloak-saml-adapter-api-public ]\n" +
                "[ keycloak-saml-servlet-filter-adapter ] -down-> [ keycloak-saml-adapter-core ]\n" +
                "[ keycloak-saml-undertow-adapter ] -down-> [ keycloak-undertow-adapter-spi ]\n" +
                "[ keycloak-installed-adapter ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-saml-tomcat-adapter ] -down-> [ keycloak-saml-tomcat-adapter-core ]\n" +
                "[ keycloak-adapter-core ] -down-> [ keycloak-authz-client ]\n" +
                "[ keycloak-spring-boot-adapter-core ] -down-> [ spring-boot-container-bundle ]\n" +
                "[ keycloak-undertow-adapter ] -down-> [ keycloak-undertow-adapter-spi ]\n" +
                "[ keycloak-ldap-federation ] -down-> [ keycloak-server-spi ]\n" +
                "[ keycloak-jboss-adapter-core ] -down-> [ keycloak-adapter-spi ]\n" +
                "[ keycloak-as7-adapter ] -down-> [ keycloak-adapter-spi ]\n" +
                "[ keycloak-wildfly-adapter ] -down-> [ keycloak-undertow-adapter ]\n" +
                "[ keycloak-as7-adapter ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-spring-boot-adapter ] -down-> [ keycloak-spring-security-adapter ]\n" +
                "[ keycloak-saml-as7-adapter ] -down-> [ keycloak-saml-adapter-api-public ]\n" +
                "[ keycloak-jetty-core ] -down-> [ keycloak-adapter-spi ]\n" +
                "[ keycloak-tomcat-adapter ] -down-> [ keycloak-core ]\n" +
                "[ keycloak-spring-boot-starter ] -down-> [ keycloak-spring-boot-2-adapter ]\n" +
                "[ keycloak-saml-as7-adapter ] -down-> [ keycloak-saml-tomcat-adapter-core ]\n" +
                "[ keycloak-saml-tomcat-adapter-core ] -down-> [ keycloak-saml-adapter-api-public ]\n" +
                "[ keycloak-saml-wildfly-adapter ] -down-> [ keycloak-saml-adapter-core ]\n" +
                "@enduml";
        return uml;
    }

}
