package users.subscriptions.services;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import users.subscriptions.model.User;
import users.subscriptions.repository.UserRepository;

@Service
public class UserService {
    private Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    private UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }


}