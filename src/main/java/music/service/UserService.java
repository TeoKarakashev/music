package music.service;

import music.model.service.UserRegisterServiceModel;

public interface UserService {

    void seedUsers();

    void registerAndLoginUser(UserRegisterServiceModel serviceModel);


    boolean userExists(String username);
}
