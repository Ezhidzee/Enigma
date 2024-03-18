package su.ezhidze.WebSocket;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import su.ezhidze.entity.User;
import su.ezhidze.exception.RecordNotFoundException;
import su.ezhidze.repository.UserRepository;
import su.ezhidze.service.UserService;
import su.ezhidze.util.JwtUtil;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String userUuid = event.getUser().getName();
        User user = userRepository.findByUUID(userUuid).orElseThrow(() -> new RecordNotFoundException("User not found"));
        user.setIsOnline(false);
        user.setUUID(null);
        userRepository.save(user);
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        JwtUtil jwtUtil = new JwtUtil();
        String userUuid = event.getUser().getName();
        String bearerToken = UserHandshakeHandler.lastHttpHeaders.get("Authorization").get(0);
        String tokenPrefix = "Bearer ";
        String jwtToken = null;
        if (bearerToken != null && bearerToken.startsWith(tokenPrefix)) {
            jwtToken = bearerToken.substring(tokenPrefix.length());
        }
        Claims claims = jwtUtil.parseJwtClaims(jwtToken);
        User user = userService.loadUserByNickname(claims.getSubject());
        user.setUUID(userUuid);
        user.setIsOnline(true);
        userRepository.save(user);
    }
}
