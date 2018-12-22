package org.fuelteam.watt.swagger2;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({ Swagger2AutoConfiguration.class })
public @interface EnableSwagger2 {}
