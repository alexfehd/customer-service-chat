import com.alexfehd.customerservicechat.config.WebSocketConfig;
import com.alexfehd.customerservicechat.entity.Status;
import com.alexfehd.customerservicechat.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = WebSocketConfig.class)
@EnableAutoConfiguration
@Slf4j
public class UserControllerIntegrationTest {
    @LocalServerPort
    private Integer port;

    WebSocketStompClient webSocketStompClient;
    @BeforeEach
    void setup() {
        this.webSocketStompClient = new WebSocketStompClient(new SockJsClient(
                List.of(new WebSocketTransport(new StandardWebSocketClient()))));
    }

    @Test
    void addUserTest() throws ExecutionException, InterruptedException, TimeoutException {

        User user = new User();
        user.setCustomerId("1");
        user.setFullName("Test");
        user.setStatus(Status.ONLINE);

        CompletableFuture<User> completableFuture = new CompletableFuture<>();

        webSocketStompClient.setMessageConverter(configureMessageConverter());

        StompSession session = webSocketStompClient
                .connect("ws://localhost:" + port + "/ws", new StompSessionHandlerAdapter() {})
                .get(1, SECONDS);

        session.subscribe("/user/public", new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                log.info("getPayloadType Headers:" + headers);
                return User.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                User receivedUser = (User) payload;
            }
        });

        StompSession.Receiptable send = session.send("/user.addUser", user);

        Thread.sleep(2000);

        User userReceived = completableFuture.get(10, SECONDS);

        assertNotNull(userReceived);
    }

    private MessageConverter configureMessageConverter() {
        DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        converter.setContentTypeResolver(resolver);
        return converter;
    }

}
