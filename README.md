# watt-starter-swagger2

```
<dependency>
  <groupId>org.fuelteam</groupId>
  <artifactId>watt-starter-swagger2</artifactId>
  <version>${version}</version>
</dependency>
```

```java
import org.fuelteam.watt.swagger2.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class Bootstrap {

    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class, args);
    }
}
```

## example
```yaml
swagger2:
  enabled: true
  docket:
    default:
      title: ${spring.application.name}
      description: 'powered by watt-starter-swagger2'
      base-package: cn.fuelteam
      base-path: /**
      excludes: /error
      contact:
        email: 'xxx@fuelteam.org'
        name: 'xxx'
      globalOperationParameters:
        0:
          name: XAuthorization
          description: field of authorization code
          modelRef: string
          parameterType: header
          required: true
```
