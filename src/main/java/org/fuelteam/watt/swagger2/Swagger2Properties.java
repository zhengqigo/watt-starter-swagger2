package org.fuelteam.watt.swagger2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.google.common.collect.Maps;

@ConfigurationProperties("swagger2")
public class Swagger2Properties {

    private Boolean enabled;

    private Contact contact = new Contact();

    private Map<String, DocketInfo> docket = Maps.newLinkedHashMap();

    private List<GlobalOperationParameter> globalOperationParameters;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Map<String, DocketInfo> getDocket() {
        return docket;
    }

    public void setDocket(Map<String, DocketInfo> docket) {
        this.docket = docket;
    }

    public List<GlobalOperationParameter> getGlobalOperationParameters() {
        return globalOperationParameters;
    }

    public void setGlobalOperationParameters(List<GlobalOperationParameter> globalOperationParameters) {
        this.globalOperationParameters = globalOperationParameters;
    }

    public static class GlobalOperationParameter {
        private String name;

        private String description;

        private String modelRef;

        /** header, query, path, body.form **/
        private String parameterType;

        private String required;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getModelRef() {
            return modelRef;
        }

        public void setModelRef(String modelRef) {
            this.modelRef = modelRef;
        }

        public String getParameterType() {
            return parameterType;
        }

        public void setParameterType(String parameterType) {
            this.parameterType = parameterType;
        }

        public String getRequired() {
            return required;
        }

        public void setRequired(String required) {
            this.required = required;
        }
    }

    public static class DocketInfo {
        private String title = "";
        private String description = "";
        private String version = "";

        private Contact contact = new Contact();

        private String basePackage = "";

        private List<String> basePath = new ArrayList<>();
        private List<String> excludes = new ArrayList<>();

        private List<GlobalOperationParameter> globalOperationParameters;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public Contact getContact() {
            return contact;
        }

        public void setContact(Contact contact) {
            this.contact = contact;
        }

        public String getBasePackage() {
            return basePackage;
        }

        public void setBasePackage(String basePackage) {
            this.basePackage = basePackage;
        }

        public List<String> getBasePath() {
            return basePath;
        }

        public void setBasePath(List<String> basePath) {
            this.basePath = basePath;
        }

        public List<String> getExcludes() {
            return excludes;
        }

        public void setExcludes(List<String> excludes) {
            this.excludes = excludes;
        }

        public List<GlobalOperationParameter> getGlobalOperationParameters() {
            return globalOperationParameters;
        }

        public void setGlobalOperationParameters(List<GlobalOperationParameter> globalOperationParameters) {
            this.globalOperationParameters = globalOperationParameters;
        }
    }

    public static class Contact {
        private String name = "";
        private String email = "";

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
