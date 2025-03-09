package source.testmodule.Services;

import source.testmodule.DataBase.Entity.User;

public interface UserService {
    User getUserById(Long userId);
}
