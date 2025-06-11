package users.subscriptions.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subscriptions")
public class Subscriptions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "subscribers_counter")
    private int subscribersCounter;

    @OneToMany(mappedBy = "subscription")
    @JsonBackReference
    private List<User> users;

    public Subscriptions() {
    }

    public void addUser(User user) {
        if (users == null) {
            users = new ArrayList<>();
        }
        users.add(user);
    }
    public Subscriptions(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSubscribersCounter() {
        return subscribersCounter;
    }

    public void setSubscribersCounter(int subscribersCounter) {
        this.subscribersCounter = subscribersCounter;
    }

    @Override
    public String toString() {
        return "Subscriptions{" +
                "id=" + id +
                ", type='" + type + '\'' +
                //", user=" + user
                '}';
    }
}
