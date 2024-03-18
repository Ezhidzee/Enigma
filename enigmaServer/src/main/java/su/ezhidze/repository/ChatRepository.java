package su.ezhidze.repository;

import org.springframework.data.repository.CrudRepository;
import su.ezhidze.entity.Chat;

public interface ChatRepository extends CrudRepository<Chat, Integer> {
}
