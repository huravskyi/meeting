package com.newcode.meeting.component;

import com.newcode.meeting.domain.User;
import com.newcode.meeting.service.UserOnlineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.Objects;


@Component
public class WebSocketEventListener {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
    private final UserOnlineService onlineService;

    public WebSocketEventListener(UserOnlineService onlineService) {
        this.onlineService = onlineService;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
//        logger.info("User  connection:=> " + event.getUser().getName());
        if (event.getUser() != null)
            onlineService.setUserOnline(getUserId(event.getUser()), true);
    }


    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
//        logger.info("User Disconnected:=>  " + event.getUser().getName());
        if (event.getUser() != null)
        onlineService.setUserOnline(getUserId(event.getUser()), false);
    }


    private Long getUserId(Principal principal) {

//        User user  = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        System.out.println("user = " + user);
//        String email = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();
        String userString = principal.toString();
        int startIndex = userString.indexOf("id=");
        int endIndex = userString.indexOf(",", startIndex);
        return Long.valueOf(userString.substring(startIndex + 3, endIndex));
    }
}