package users.subscriptions.mapper;

import users.subscriptions.dto.UserDto;
import users.subscriptions.model.User;

public class UserMapper {

    public User dtoToUser(UserDto userDto){
        return new User(userDto.getUserName(), userDto.getEmail());
    }
}