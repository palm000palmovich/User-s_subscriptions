package users.subscriptions.exceptions;

public class SubscriptionIsNotFoundException extends RuntimeException{
    public SubscriptionIsNotFoundException(Long id){
        super("Подписка с id " + id + " не найдена.");
    }
}