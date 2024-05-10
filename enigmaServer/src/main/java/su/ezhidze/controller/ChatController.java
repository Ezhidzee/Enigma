package su.ezhidze.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import su.ezhidze.exception.ExceptionBodyBuilder;
import su.ezhidze.exception.RecordNotFoundException;
import su.ezhidze.model.ChatModel;
import su.ezhidze.model.InputMessageModel;
import su.ezhidze.service.ChatService;
import su.ezhidze.service.WSService;
import su.ezhidze.validator.Validator;

@Controller
public class ChatController {

    @Autowired
    private WSService service;

    @Autowired
    private ChatService chatService;

    @MessageMapping("/private-chat")
    @SendToUser("/topic/private-messages")
    public String getPrivateMessage(String message) throws Exception {
        Gson gson = new Gson();

        Validator.validate(message);
        InputMessageModel inputMessage = gson.fromJson(message, InputMessageModel.class);
        service.sendMessage(inputMessage);
        return message;
    }

    @PostMapping(path = "/addChat")
    public ResponseEntity addNewCinema() {
        try {
            return ResponseEntity.ok(chatService.addNewChat());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ExceptionBodyBuilder.build(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    @GetMapping(path = "/chats")
    public ResponseEntity getChats() {
        try {
            return ResponseEntity.ok(chatService.getChats());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ExceptionBodyBuilder.build(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    @GetMapping(path = "/chats", params = {"id"})
    public ResponseEntity getChats(@RequestParam Integer id) {
        try {
            return ResponseEntity.ok(new ChatModel(chatService.getChatById(id)));
        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionBodyBuilder.build(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ExceptionBodyBuilder.build(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    @PutMapping(path = "/joinUser")
    public ResponseEntity addUser(@RequestParam Integer chatId, @RequestParam Integer userId) {
        try {
            return ResponseEntity.ok(chatService.joinUser(chatId, userId));
        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionBodyBuilder.build(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ExceptionBodyBuilder.build(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    @PutMapping(path = "/deleteUser")
    public ResponseEntity deleteUser(@RequestParam Integer chatId, @RequestParam Integer userId) {
        try {
            return ResponseEntity.ok(chatService.deleteUser(chatId, userId));
        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionBodyBuilder.build(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ExceptionBodyBuilder.build(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    @PutMapping(path = "/deleteMessage")
    public ResponseEntity deleteMessage(@RequestParam Integer chatId, @RequestParam Integer messageId) {
        try {
            return ResponseEntity.ok(chatService.deleteMessage(chatId, messageId));
        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionBodyBuilder.build(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ExceptionBodyBuilder.build(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }
}
