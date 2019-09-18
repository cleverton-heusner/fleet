package com.fleet.vehicle.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = VehicleIdValidator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface VehicleId {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
