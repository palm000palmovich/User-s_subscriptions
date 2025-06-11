package users.subscriptions.exceptions;

import users.subscriptions.model.Subscriptions;

public class UserAlreadyHaveSubscriptionException extends RuntimeException{

    public UserAlreadyHaveSubscriptionException(Subscriptions sub){
        super("У юзера уже есть подписка: " + sub.toString());
    }
}
