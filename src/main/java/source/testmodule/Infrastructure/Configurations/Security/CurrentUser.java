package source.testmodule.Infrastructure.Configurations.Security;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {
    /**
     * Annotation for getting the current user.
     */
}
