package users.subscriptions.mapper;

import org.springframework.stereotype.Component;
import users.subscriptions.dto.UserDto;
import users.subscriptions.model.User;

@Component
public class UserMapper {

    public User dtoToUser(UserDto userDto){
        User user = new User();
        user.setUserName(userDto.getUserName());
        user.setEmail(userDto.getEmail());
        return user;
    }
}