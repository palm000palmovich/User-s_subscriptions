package users.subscriptions.exceptions;

public class SubscriptionAlreadyExistsException extends RuntimeException{
    public SubscriptionAlreadyExistsException(){
        super("Подписка уже существет.");
    }
}
