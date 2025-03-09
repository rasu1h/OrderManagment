package source.testmodule.Services.util_services;

import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import source.testmodule.Configurations.Jwt.JwtAuthFilter;
import source.testmodule.Configurations.Jwt.JwtTokenProvider;

import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReceiveData { /// to fasilitate extract the data from token
    private final JwtTokenProvider receiveToken;
    private final JwtAuthFilter jwtAuthFilter;

    public Map<String, Object> data() throws AuthException {
        HttpServletRequest request = getCurrentHttpRequest();
        String token = jwtAuthFilter.getJwtFromRequest(request);
        if (token == null) {
            throw new AuthException("Token is null or not valid");
        }
        return receiveToken.getUserDataFromToken(token);
    }

    /**
     * Get current http request
     * @return HttpServletRequest object
     */
    private HttpServletRequest getCurrentHttpRequest() {
        return ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest();
    }

    public String getUserId() throws AuthException {
       return data().get("id").toString();
    }

}