package su.ezhidze.repository;

import org.springframework.data.repository.CrudRepository;
import su.ezhidze.entity.Chat;
import su.ezhidze.entity.Message;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends CrudRepository<Message, Integer> {
    Optional<List<Message>> findMessagesByChat(Chat chat);
}
