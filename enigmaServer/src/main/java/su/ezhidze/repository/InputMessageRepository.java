package su.ezhidze.repository;

import org.springframework.data.repository.CrudRepository;
import su.ezhidze.entity.InputMessage;

public interface InputMessageRepository  extends CrudRepository<InputMessage, Integer> {
}
