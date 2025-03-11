package source.testmodule.Infrastructure.Configurations.Logs;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import source.testmodule.Domain.Model.User;
import source.testmodule.Presentation.DTO.OrderDTO;

import java.util.Arrays;
import java.util.Optional;

@Aspect
@Component
@Slf4j
public class OrderLoggingAspect {

    // Pointcut для методов с аннотацией @Loggable
    @Pointcut("@annotation(source.testmodule.Infrastructure.Configurations.Logs.Loggable)")
    public void loggableMethods() {}

    // Pointcut для всех методов в OrderController
    @Pointcut("execution(* source.testmodule.Presentation.Controllers.OrderController.*(..))")
    public void orderControllerMethods() {}

    // Объединение условий
    @Pointcut("loggableMethods() || orderControllerMethods()")
    public void combinedPointcut() {}

    @AfterReturning(pointcut = "combinedPointcut()", returning = "result")
    public void logOrderAction(JoinPoint joinPoint, Object result) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Loggable loggable = signature.getMethod().getAnnotation(Loggable.class);

            String action = loggable != null ? loggable.action() : "AUTO_DETECTED";
            String methodName = signature.getName();

            // Поиск пользователя в аргументах
            Optional<User> currentUser = Arrays.stream(joinPoint.getArgs())
                    .filter(arg -> arg instanceof User)
                    .map(arg -> (User) arg)
                    .findFirst();

            String userId = currentUser.map(u -> u.getId().toString()).orElse("SYSTEM");
            String userName = currentUser.map(User::getName).orElse("SYSTEM");

            // Логирование
            String logMessage = String.format(
                    "[ORDER] User: %s: %s | Action: %s | Method: %s",
                    userId, userName,action, methodName
            );

            if (result instanceof OrderDTO) {
                OrderDTO order = (OrderDTO) result;
                logMessage += " | OrderID: " + order.getId();
            } else if (joinPoint.getArgs().length > 0 && joinPoint.getArgs()[0] instanceof Long) {
                Long orderId = (Long) joinPoint.getArgs()[0];
                logMessage += " | OrderID: " + orderId;
            }

            log.info(logMessage);

        } catch (Exception e) {
            log.error("Ошибка в аспекте логирования: {}", e.getMessage());
        }
    }
}