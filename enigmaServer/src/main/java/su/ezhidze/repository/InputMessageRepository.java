package su.ezhidze.repository;

import org.springframework.data.repository.CrudRepository;
import su.ezhidze.entity.InputMessage;
import su.ezhidze.entity.User;

import java.util.List;
import java.util.Optional;

public interface InputMessageRepository  extends CrudRepository<InputMessage, Integer> {
    Optional<List<InputMessage>> findInputMessageByUser(User user);
}
