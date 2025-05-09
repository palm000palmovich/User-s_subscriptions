package users.subscriptions.dto;

import jakarta.validation.constraints.NotNull;

public class SubscriptionRequest {
    @NotNull(message = "Тип подписки не может быть пустым")
    private String type;

    public SubscriptionRequest(String type) {
        this.type = type;
    }

    public SubscriptionRequest(){}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}