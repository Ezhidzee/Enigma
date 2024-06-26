package su.ezhidze.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> users;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval=true)
    private List<Message> messages;

    public Chat() {
        users = new ArrayList<>();
        messages = new ArrayList<>();
    }
}
