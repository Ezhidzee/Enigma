package su.ezhidze.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import su.ezhidze.entity.InputMessage;
import su.ezhidze.entity.User;
import su.ezhidze.exception.AuthenticationFailException;
import su.ezhidze.model.InputMessageModel;
import su.ezhidze.repository.InputMessageRepository;

import java.util.List;

@Service
public class InputMessageService {

    @Autowired
    InputMessageRepository inputMessageRepository;

    public void addInputMessage(final InputMessage message) {
        inputMessageRepository.save(message);
    }

    public InputMessage addInputMessage(final InputMessageModel message, User user) {
        return inputMessageRepository.save(new InputMessage(message, user));
    }

    public void deleteInputMessages(User user) {
        List<InputMessage> messages = inputMessageRepository.findInputMessageByUser(user).orElseThrow(() -> new AuthenticationFailException("InputMessages not found"));
        messages.stream().forEach(inputMessage -> inputMessageRepository.delete(inputMessage));
    }
}
