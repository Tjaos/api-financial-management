package br.com.finance.ms_user.user.kafka.event;

import java.io.Serializable;
import java.util.UUID;

public class UserCreatedEvent implements Serializable {
    private UUID userId;
    private String email;

    public UserCreatedEvent(UUID userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    public UserCreatedEvent() {
    }

    public UUID getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }
}
