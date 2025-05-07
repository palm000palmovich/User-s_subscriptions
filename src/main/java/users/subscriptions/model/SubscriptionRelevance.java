package users.subscriptions.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "SubscriptionRelevance")
public class SubscriptionRelevance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long user_id;
    private Long sub_id;
    private LocalDateTime end_time;

    public SubscriptionRelevance(Long id, Long user_id, Long sub_id, LocalDateTime end_time) {
        this.id = id;
        this.user_id = user_id;
        this.sub_id = sub_id;
        this.end_time = end_time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getSub_id() {
        return sub_id;
    }

    public void setSub_id(Long sub_id) {
        this.sub_id = sub_id;
    }

    public LocalDateTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(LocalDateTime end_time) {
        this.end_time = end_time;
    }
}
