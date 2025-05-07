package users.subscriptions.dto;

import jakarta.persistence.Column;
import users.subscriptions.model.User;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDto {
    @Size(min = 4, max = 16, message = "от 4 до 16 символов")
    private String userName;

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$",
            message = "неправильный формат мыла")
    private String email;

    public UserDto(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}