package users.subscriptions.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id){
        super("Юзер с id " + id + " не найден.");
    }
}
