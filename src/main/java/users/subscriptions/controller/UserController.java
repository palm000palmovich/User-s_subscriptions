package users.subscriptions.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import users.subscriptions.dto.SubscriptionRequest;
import users.subscriptions.dto.UserDto;
import users.subscriptions.exceptions.SubscriptionAlreadyExistsException;
import users.subscriptions.exceptions.SubscriptionIsNotFoundException;
import users.subscriptions.exceptions.UserAlreadyHaveSubscriptionException;
import users.subscriptions.exceptions.UserNotFoundException;
import users.subscriptions.model.Subscriptions;
import users.subscriptions.model.User;
import users.subscriptions.services.SubscriptionsService;
import users.subscriptions.services.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final SubscriptionsService subscriptionsService;

    public UserController(UserService userService,
                          SubscriptionsService subscriptionsService) {
        this.userService = userService;
        this.subscriptionsService = subscriptionsService;
    }

    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody @Valid UserDto userDto) {
        logger.info("Validation with data " + userDto.toString() + " passed!");

        User user = userService.uploadUser(userDto);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<User> findUserById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (UserNotFoundException e) {
            logger.info("User with id " + id + " not found.");
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            logger.info("User with id " + id + " not found.");
        }

        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id,
                                           @RequestBody UserDto userDto) {

        try {
            return ResponseEntity.ok(userService.updateUser(id, userDto));
        } catch (UserNotFoundException e) {
            logger.info("User with id " + id + " not found.");
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/{id}/subscriptions")
    public ResponseEntity<Subscriptions> uploadSubs(@PathVariable Long id,
                                                    @RequestBody @Valid SubscriptionRequest subscriptionRequest) {
        try {
            return ResponseEntity.ok(subscriptionsService.uploadSubscription(id, subscriptionRequest.getType()));
        } catch (UserNotFoundException e1) {
            logger.info("Не найден юзер с id " + id);
            return ResponseEntity.noContent().build();
        } catch (UserAlreadyHaveSubscriptionException e2) {
            logger.info("У юзера уже есть подписка.");
            return ResponseEntity.badRequest().build();
        } catch (SubscriptionAlreadyExistsException e3) {
            logger.info("Подписка уже существует.");
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "/{id}/subscriptions")
    public ResponseEntity<SubscriptionRequest> getUsersSubs(@PathVariable("id") Long userId){
        try{
            return ResponseEntity.ok(subscriptionsService.getUsersSubs(userId));
        } catch(UserNotFoundException e1){
            logger.info("Пользователь с айди " + userId + " не найден.");
            return ResponseEntity.noContent().build();
        } catch(SubscriptionIsNotFoundException e2){
            logger.info("У пользователя с id " + userId + " нет подписок.");
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping(path = "/{id}/subscriptions/{sub_id}")
    public ResponseEntity<?> deleteSub(@PathVariable("id") Long userId,
                                       @PathVariable("sub_id") Long subId){
        subscriptionsService.deleteSubscription(userId, subId);
        return ResponseEntity.ok().build();
    }
}
