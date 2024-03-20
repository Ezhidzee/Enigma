package su.ezhidze;

import generators.PrimesGenerator;
import generators.PrimitiveRootGenerator;
import org.springframework.http.*;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import su.ezhidze.models.ChatModel;
import su.ezhidze.models.InputMessageModel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.*;

public class ChatClient {

    public static Long bPublicKey;

    public static Long kPrivateKey;

    public static void main(String args[]) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        String url = "ws://localhost:8080/chat";
        WebSocketClient client = new StandardWebSocketClient();
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(client));
        SockJsClient sockJsClient = new SockJsClient(transports);

        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new MyStompSessionHandler();
        WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();
        System.out.println("jwt token:");
        String jwtToken = in.readLine();
        webSocketHttpHeaders.add("Authorization", "Bearer " + jwtToken);
        StompSession session = stompClient.connect(url, webSocketHttpHeaders, sessionHandler).get();

        System.out.println("Nickname: ");
        String email = in.readLine();
        System.out.println("Chat id: ");
        Integer chatID = Integer.valueOf(in.readLine());

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer " + jwtToken);
        header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        header.add("Accept", "application/json");
//        Map<String, Integer> params = new HashMap<>();
//        params.put("id", chatID);
        HttpEntity entity = new HttpEntity(header);
        ResponseEntity<ChatModel> response = restTemplate.exchange("http://localhost:8080/chat?id={id}", HttpMethod.GET, entity, ChatModel.class, chatID);

        Integer privateKey = (int) (Math.random() * 1000000000);
        if (response.getBody().getA() == null || response.getBody().getA() == 0) {
            Long p = PrimesGenerator.getPrime();
            Long g = PrimitiveRootGenerator.getRoot(p);
//            Long A = g;
//            A = (long) Math.pow(g, privateKey % p) % p;
            BigInteger A = new BigInteger(String.valueOf(g));
            BigInteger a = new BigInteger(String.valueOf(privateKey));
            A = A.pow(a.mod(BigInteger.valueOf(p)).intValue()).mod(BigInteger.valueOf(p));
            response = restTemplate.exchange("http://localhost:8080/setAgp?id={id}&A={A}&g={g}&p={p}", HttpMethod.POST, entity, ChatModel.class, chatID, A.intValue(), g, p);
        } else {
            response = restTemplate.exchange("http://localhost:8080/chat?id={id}", HttpMethod.GET, entity, ChatModel.class, chatID);
            BigInteger b = new BigInteger(String.valueOf(privateKey));
            BigInteger K = new BigInteger(String.valueOf(response.getBody().getA()));
            K = K.pow(b.mod(BigInteger.valueOf(response.getBody().getP())).intValue()).mod(BigInteger.valueOf(response.getBody().getP()));
            kPrivateKey = (long) K.intValue();
            Map<String, Object> m = new HashMap<String, Object>();
            BigInteger B = new BigInteger(String.valueOf(response.getBody().getG()));
            B = B.mod(BigInteger.valueOf(response.getBody().getP()));
            B = B.pow(privateKey % response.getBody().getP());
            B = B.mod(BigInteger.valueOf(response.getBody().getP()));
            m.put("B", B.toString());
            InputMessageModel data = new InputMessageModel("DATA", email, chatID, m.toString());
            session.send("/app/private-chat", data);
        }

//        //{A=1, G=1}
//        Map<String, Object> m = new HashMap<String, Object>();
//        m.put("A", 1);
//        m.put("G", 1);
//        InputMessageModel data = new InputMessageModel("DATA", email, chatID, m.toString());
//        session.send("/app/private-chat", data);

        for (; ; ) {
            String line = in.readLine();
            if (line == null) break;
            if (line.isEmpty()) continue;
            InputMessageModel msg = new InputMessageModel("MESSAGE", email, chatID, line);
            session.send("/app/private-chat", msg);
        }
    }

    static public class MyStompSessionHandler extends StompSessionHandlerAdapter {

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return Object.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            LinkedHashMap params = (LinkedHashMap) payload;
            if (Objects.equals(params.get("type"), "DATA")) {
                String data = (String) params.get("data");
                Map<String, String> values = new HashMap<>();
                String[] pairs = data.split(", ");
                for (int i = 0; i < pairs.length; i++) {
                    String pair = pairs[i];
                    String[] keyValue = pair.split("=");
                    values.put(keyValue[0], keyValue[1]);
                }
                bPublicKey = Long.valueOf(Integer.valueOf(values.get("B")));
            } else {
                System.out.println("Received : " + params.get("data") + " from : " + params.get("senderSubject"));
            }

        }

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            session.subscribe("/topic/messages", this);
            session.subscribe("/user/topic/private-messages", this);
//            session.subscribe("/user/topic/private-data", this);
        }
    }
}
