package org.retrotime.configuration.websocket;

import org.apache.log4j.Logger;
import org.retrotime.dto.ContentPartDTO;
import org.retrotime.service.websocket.WebSocketService;
import org.retrotime.service.websocket.encoding.ContentPartDTODecoder;
import org.retrotime.service.websocket.encoding.ContentPartDTOEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import java.io.IOException;

/**
 * Created by vzhemevko on 12.09.15.
 */
@ServerEndpoint(value = "/sock/{retroId}",
        encoders = ContentPartDTOEncoder.class, decoders = ContentPartDTODecoder.class,
        configurator = SpringConfigurator.class)

public class WebSocketServerEndpoint {

    public final static Logger logger = Logger.getLogger(WebSocketServerEndpoint.class);

    @Autowired
    WebSocketService webSocketService;

    @OnOpen
    public void onOpen(Session session, @PathParam(WebSocketService.RETRO_ID) final Integer retroId) {
        try {
            webSocketService.registerNewClient(session, retroId);

        } catch(Exception ex) {
            logger.error("Caught an exception during a connection openening.", ex);
        }
    }

    @OnMessage
    public void onMessage(Session session, final ContentPartDTO partDTO) {
        try {
            webSocketService.handleMessage(session, partDTO);

        } catch(Exception ex) {
            logger.error("Caught an exception during receiving a message.", ex);
        }
    }

    @OnError
    public void onError(Session session, Throwable th) throws IOException {
        logger.error("Caught an error.");
        session.getBasicRemote().sendText("EROR"); // TODO;
    }

    @OnClose
    public void onClose(Session session) {
        try {
            webSocketService.unregisterClient(session);

        } catch(Exception ex) {
            logger.error("Caught an exception during closing  the connection.", ex);
        }
    }
}
