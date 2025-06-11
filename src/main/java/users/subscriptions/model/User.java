package users.subscriptions.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 4, max = 16, message = "от 4 до 16 символов")
    @Column(name = "username")
    private String userName;
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
            message = "неправильный формат мыла")
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_id")
    private Subscriptions subscription;

    public User(){}

    public User(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Subscriptions getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscriptions subscription) {
        this.subscription = subscription;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                //", subs=" + subs +
                '}';
    }
}