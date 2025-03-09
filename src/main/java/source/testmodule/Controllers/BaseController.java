package source.testmodule.Controllers;

import jakarta.security.auth.message.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import source.testmodule.Services.util_services.ReceiveData;

import java.util.Map;

@Slf4j
public abstract class BaseController {
    @Autowired
    protected ReceiveData receiveToken;

    protected Map<String, Object> data;
    protected Long userId;

    @ModelAttribute
    public void initData() throws AuthException {
        try{
            this.data = receiveToken.data();
            this.userId = Long.parseLong(receiveToken.getUserId());
        } catch (AuthException e){
            e.printStackTrace();
        }
    }
}
