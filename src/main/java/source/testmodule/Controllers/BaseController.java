package source.testmodule.Controllers;

import jakarta.security.auth.message.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import source.testmodule.Services.util_services.ReceiveData;

import java.util.Map;

public abstract class BaseController {
    @Autowired
    protected ReceiveData receiveToken;

    protected Map<String, Object> data;
    protected Long userId;

    @ModelAttribute
    public void initData() throws AuthException {
        this.data = receiveToken.data();
        this.userId = Long.parseLong(receiveToken.getUserId());
    }
}
