package source.testmodule.Application.Services;

import source.testmodule.Domain.Entity.User;

public interface UserService {
    User getUserById(Long userId);
}
