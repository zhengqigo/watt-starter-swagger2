package org.fuelteam.watt.swagger2;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;

import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@Import({ Swagger2Configuration.class })
public class Swagger2AutoConfiguration implements BeanFactoryAware {

    private BeanFactory beanFactory;

    @Bean
    @ConditionalOnMissingBean
    public Swagger2Properties swagger2Properties() {
        return new Swagger2Properties();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "swagger2.enabled", matchIfMissing = true)
    public List<Docket> initDocket(Swagger2Properties swagger2Properties) {
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
        List<Docket> docketList = Lists.newLinkedList();
        for (String groupName : swagger2Properties.getDocket().keySet()) {
            Swagger2Properties.DocketInfo docketInfo = swagger2Properties.getDocket().get(groupName);
            Contact contract = new Contact(docketInfo.getContact().getName(), null, docketInfo.getContact().getEmail());
            ApiInfo apiInfo = new ApiInfoBuilder().title(docketInfo.getTitle()).description(docketInfo.getDescription())
                    .version(docketInfo.getVersion()).contact(contract).build();
            if (docketInfo.getBasePath().isEmpty()) docketInfo.getBasePath().add("/**");
            List<Predicate<String>> basePath = Lists.newArrayList();
            for (String path : docketInfo.getBasePath()) {
                basePath.add(PathSelectors.ant(path));
            }
            List<Predicate<String>> excludes = Lists.newArrayList();
            for (String path : docketInfo.getExcludes()) {
                excludes.add(PathSelectors.ant(path));
            }
            List<Parameter> parameterList = assemblyGlobalOperationParameters(
                    swagger2Properties.getGlobalOperationParameters(), docketInfo.getGlobalOperationParameters());
            Predicate<RequestHandler> requestHandlers = RequestHandlerSelectors
                    .basePackage(docketInfo.getBasePackage());
            Predicate<String> paths = Predicates.and(Predicates.not(Predicates.or(excludes)), Predicates.or(basePath));
            Docket docket = new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo)
                    .globalOperationParameters(parameterList).groupName(groupName).select().apis(requestHandlers)
                    .paths(paths).build();
            configurableBeanFactory.registerSingleton(groupName, docket);
            docketList.add(docket);
        }
        return docketList;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    private List<Parameter> buildGlobalOperationParametersFromSwaggerProperties(
            List<Swagger2Properties.GlobalOperationParameter> globalOperationParameters) {
        List<Parameter> parameters = Lists.newArrayList();
        if (Objects.isNull(globalOperationParameters)) return parameters;
        for (Swagger2Properties.GlobalOperationParameter globalOperationParameter : globalOperationParameters) {
            parameters.add(new ParameterBuilder().name(globalOperationParameter.getName())
                    .description(globalOperationParameter.getDescription())
                    .modelRef(new ModelRef(globalOperationParameter.getModelRef()))
                    .parameterType(globalOperationParameter.getParameterType())
                    .required(Boolean.parseBoolean(globalOperationParameter.getRequired())).build());
        }
        return parameters;
    }

    private List<Parameter> assemblyGlobalOperationParameters(
            List<Swagger2Properties.GlobalOperationParameter> globalOperationParameters,
            List<Swagger2Properties.GlobalOperationParameter> docketOperationParameters) {
        if (Objects.isNull(docketOperationParameters) || docketOperationParameters.isEmpty()) {
            return buildGlobalOperationParametersFromSwaggerProperties(globalOperationParameters);
        }
        Set<String> docketNames = docketOperationParameters.stream()
                .map(Swagger2Properties.GlobalOperationParameter::getName).collect(Collectors.toSet());
        List<Swagger2Properties.GlobalOperationParameter> resultOperationParameters = Lists.newArrayList();
        if (Objects.nonNull(globalOperationParameters)) {
            for (Swagger2Properties.GlobalOperationParameter parameter : globalOperationParameters) {
                if (!docketNames.contains(parameter.getName())) resultOperationParameters.add(parameter);
            }
        }
        resultOperationParameters.addAll(docketOperationParameters);
        return buildGlobalOperationParametersFromSwaggerProperties(resultOperationParameters);
    }
}
