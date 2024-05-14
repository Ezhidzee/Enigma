package su.ezhidze.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import su.ezhidze.entity.InputMessage;
import su.ezhidze.model.InputMessageModel;
import su.ezhidze.repository.InputMessageRepository;

@Service
public class InputMessageService {

    @Autowired
    InputMessageRepository inputMessageRepository;

    public void addInputMessage(final InputMessage message) {
        inputMessageRepository.save(message);
    }

    public InputMessage addInputMessage(final InputMessageModel message) {
        return inputMessageRepository.save(new InputMessage(message));
    }
}
