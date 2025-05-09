package users.subscriptions.services;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import users.subscriptions.dto.UserDto;
import users.subscriptions.exceptions.UserNotFoundException;
import users.subscriptions.mapper.UserMapper;
import users.subscriptions.model.User;
import users.subscriptions.repository.UserRepository;

@Service
public class UserService {
    private Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private UserService(UserRepository userRepository,
                        UserMapper userMapper){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    //SAVE
    public User uploadUser(UserDto userDto){
        logger.info("Received: " + userDto.toString());

        User savedUser = userRepository.save(userMapper.dtoToUser(userDto));

        logger.info("Saved user: " + savedUser.toString());

        return savedUser;
    }

    //FIND
    public User getUserById(Long userId){
        return userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(userId));
    }

    //DELETE
    public void deleteUserById(Long userId){
        if (!userRepository.findById(userId).isPresent()){
            throw new UserNotFoundException(userId);
        }

        userRepository.deleteById(userId);
    }

    //PUT
    public User updateUser(Long userId, UserDto userDto){
        User userForUpdate = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(userId));

        userForUpdate.setUserName(userForUpdate.getUserName());
        userForUpdate.setEmail(userForUpdate.getEmail());

        return userRepository.save(userForUpdate);
    }
}