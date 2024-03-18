package su.ezhidze.repository;

import org.springframework.data.repository.CrudRepository;
import su.ezhidze.entity.Message;

public interface MessageRepository extends CrudRepository<Message, Integer> {

}
