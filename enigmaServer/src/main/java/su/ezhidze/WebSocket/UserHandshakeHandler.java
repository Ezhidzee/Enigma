package su.ezhidze.WebSocket;

import com.sun.security.auth.UserPrincipal;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

public class UserHandshakeHandler extends DefaultHandshakeHandler {

    public static HttpHeaders lastHttpHeaders;

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        final String randomId = UUID.randomUUID().toString();
        lastHttpHeaders = request.getHeaders();

        System.out.println(randomId);
        return new UserPrincipal(randomId);
    }
}
