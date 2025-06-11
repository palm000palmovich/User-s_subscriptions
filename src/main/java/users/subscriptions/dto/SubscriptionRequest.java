package users.subscriptions.dto;

import jakarta.validation.constraints.NotNull;

public class SubscriptionRequest {
    private Long id;
    @NotNull(message = "Тип подписки не может быть пустым")
    private String type;

    public SubscriptionRequest(Long id, String type) {
        this.id = id;
        this.type = type;
    }

    public SubscriptionRequest(){}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}