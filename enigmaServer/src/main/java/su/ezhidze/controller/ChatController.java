package su.ezhidze.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
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

import java.security.Principal;

@Controller
public class ChatController {

    @Autowired
    private WSService service;

    @Autowired
    private ChatService chatService;

    @MessageMapping("/private-chat")
    public InputMessageModel getPrivateMessage(final InputMessageModel message, final Principal principal) throws Exception {
        Validator.validate(message);
        service.sendMessage(message);
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

    @GetMapping(path = "/chat", params = {"id"})
    public ResponseEntity getChat(@RequestParam Integer id) {
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

    @PostMapping(path = "/setAgp", params = {"id", "A", "g", "p"})
    public ResponseEntity deleteMessage(@RequestParam Integer id, @RequestParam Integer A, @RequestParam Integer g, @RequestParam Integer p) {
        try {
            return ResponseEntity.ok(chatService.setAgp(id, A, g, p));
        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionBodyBuilder.build(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ExceptionBodyBuilder.build(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }
}
