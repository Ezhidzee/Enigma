package su.ezhidze.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import su.ezhidze.entity.User;
import su.ezhidze.exception.ExceptionBodyBuilder;
import su.ezhidze.exception.RecordNotFoundException;
import su.ezhidze.model.ChatModel;
import su.ezhidze.model.InputMessageModel;
import su.ezhidze.service.ChatService;
import su.ezhidze.service.UserService;
import su.ezhidze.service.WSService;

import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private WSService wsService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @MessageMapping("/private-chat")
    @SendToUser("/topic/private-messages")
    public void getPrivateMessage(String message) throws Exception {
        Gson gson = new Gson();
        InputMessageModel inputMessage = gson.fromJson(message, InputMessageModel.class);
        wsService.sendMessage(inputMessage);
    }

    @MessageMapping("/connection-notifications")
    @SendToUser("/topic/private-notifications")
    public void getConnectionNotifications(String notification) {
        User user = userService.loadUserByNickname(notification.split(" ")[0]);
        if (!user.getUnreadMessages().isEmpty()) {
            Gson gson = new Gson();
            wsService.sendNotificationResponse(gson.toJson(user.getUnreadMessages().stream().map(InputMessageModel::new).toList(), new TypeToken<List<InputMessageModel>>() {}.getType()), user.getUUID());
            userService.clearUnreadMessages(user.getId());
        }
    }

    @PostMapping(path = "/addChat")
    public ResponseEntity addNewChat() {
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

    @DeleteMapping("/deleteChat")
    public ResponseEntity deleteChat(@RequestParam Integer id) {
        try {
            List<User> users = chatService.deleteChat(id);
            for (User user : users) {
                if (user.getIsOnline()) {
                    wsService.sendChatRemovalNotification(id, user.getUUID());
                }
            }
            return ResponseEntity.ok("");
        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionBodyBuilder.build(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ExceptionBodyBuilder.build(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    @DeleteMapping("/deleteUserChats")
    public ResponseEntity deleteUserChats(@RequestParam Integer id) {
        try {
            User user = userService.getUserById(id);
            while (!user.getChats().isEmpty()) {
                int chatId = user.getChats().get(0).getId();
                List<User> users = chatService.deleteChat(chatId);
                for (User u : users) {
                    if (u.getIsOnline() && !u.getId().equals(user.getId())) {
                        wsService.sendChatRemovalNotification(chatId, u.getUUID());
                    }
                }
            }
            return ResponseEntity.ok("");
        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionBodyBuilder.build(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ExceptionBodyBuilder.build(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }
}
