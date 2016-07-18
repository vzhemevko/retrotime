package org.retrotime.service.websocket;

import org.apache.log4j.Logger;
import org.retrotime.dto.ContentPartDTO;
import org.retrotime.service.ContentService;
import org.retrotime.springdata.jpa.ContentRepository;
import org.retrotime.springdata.jpa.RetroRepository;
import org.retrotime.springdata.jpa.UserRepository;
import org.retrotime.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.websocket.EncodeException;
import javax.websocket.Session;

import java.io.IOException;
import java.util.*;

/**
 * Created by vzhemevko on 12.09.15.
 */
@Service
public class WebSocketService {

    public final static String RETRO_ID = "retroId";

    public final static Logger logger = Logger.getLogger(WebSocketService.class);

    private final static Object MUTEX = new Object();

    private static Map<Integer, Set<Session>> clientRegister = Collections.synchronizedMap(new HashMap<>());

    private static Map<Integer, Set<ContentPartDTO>> activeLocksFroRetro = Collections.synchronizedMap(new HashMap<>());
    private static Map<Session, Set<ContentPartDTO>> activeLocksByUser = Collections.synchronizedMap(new HashMap<>());

    @Resource
    private RetroRepository retroRepository;

    @Resource
    private ContentRepository contentRepository;

    @Resource
    private UserRepository userRepository;

    @Autowired
    private ContentService contentService;

    private void sendMessageToClient(Session session, ContentPartDTO partDTO) {
        try {
            session.getBasicRemote().sendObject(partDTO);
        }
        catch (IOException | EncodeException ex) {
            logger.error("Caught an exception during attempt to send a message to client", ex);
        }
    }

    public void registerNewClient(Session session, Integer retroId) {
        synchronized (MUTEX) {
            session.getUserProperties().put(RETRO_ID, retroId);
            Set<Session> sessions = clientRegister.get(retroId);
            if (sessions == null || sessions.isEmpty()) {
                sessions = new HashSet<>();
            }
            if (!sessions.contains(session)) {
                sessions.add(session);
            }
            clientRegister.put(retroId, sessions);
            logger.info("WebSocketServer - client [ " + session.getId() + " ] connected ... ");
            sendActiveLocksToClient(session, retroId);
        }

    }

    public void unregisterClient(Session session) {
        synchronized (MUTEX) {
            Integer retroId = (Integer) session.getUserProperties().get(RETRO_ID);
            Set<Session> sessions = clientRegister.get(retroId);
            if (Util.isNull(sessions)) {
                return;
            }
            sessions.remove(session);
            if (sessions.size() == 0) {
                clientRegister.remove(retroId);
                activeLocksFroRetro.remove(retroId); // remove all locks for retro.
                logger.info("WebSocketServer - client [ " + session.getId() + " ] disconnected ... ");
            }
            Set<ContentPartDTO> locksByUser = activeLocksByUser.get(session);
            if (!Util.isNull(locksByUser)) {
                for (ContentPartDTO partDTO : locksByUser) {
                    partDTO.setLocked(false); // unlock message.
                    notifyPeers(session, retroId, partDTO);
                }
                Set<ContentPartDTO> locks = activeLocksFroRetro.get(retroId);
                activeLocksByUser.remove(session);
                locks.removeAll(locksByUser);
            }
        }
    }

    public void handleMessage(Session session, ContentPartDTO partDTO) {
        synchronized (MUTEX) {
            logger.info("Received a websocket message.");
            Integer retroId = (Integer) session.getUserProperties().get(RETRO_ID);
            if (!partDTO.isLockType()) {
                updateContent(partDTO);
            }
            else {
                logger.info("Received a websocket message of lock type.");
                handleLockMessage(partDTO, session);
            }
            notifyPeers(session, retroId, partDTO);
        }
    }

    private void notifyPeers(Session session, Integer retroId, ContentPartDTO partDTO) {
        Set<Session> sessions = clientRegister.get(retroId);
        if (Util.isNull(sessions)) {
            logger.debug("No active session for retro with Id : " + retroId);
        }
        for (Session s : sessions) {
            if (s.isOpen() && retroId.equals(s.getUserProperties().get(RETRO_ID)) && !s.equals(session)) {
                sendMessageToClient(s, partDTO);

            }
        }
    }

    private void updateContent(ContentPartDTO contPartDTO) {
        contentService.updateContentPart(contPartDTO);
    }

    private void sendActiveLocksToClient(Session session, Integer retroId) {
        logger.debug("Sending active locks to the new client");
        Set<ContentPartDTO> locks = activeLocksFroRetro.get(retroId);
        if (Util.isNull(locks)) {
            return;
        }
        for (ContentPartDTO partDTO : locks) {
            sendMessageToClient(session, partDTO);
        }
    }

    private void handleLockMessage(ContentPartDTO partDTO, Session session) {
        Integer retroId = partDTO.getRetroId();
        Set<ContentPartDTO> locks = activeLocksFroRetro.get(retroId);
        Set<ContentPartDTO> locksByUser = activeLocksByUser.get(session);

        if (Util.isNull(locks)) {
            locks = new HashSet<>();
        }

        if (Util.isNull(locksByUser)) {
            locksByUser = new HashSet<>();
        }

        if (!partDTO.isLocked() && locks.contains(partDTO)
                                && locksByUser.contains(partDTO)) {
            logger.debug("Removed a lock.");
            locks.remove(partDTO);
            locksByUser.remove(partDTO);
        }
        else if (partDTO.isLocked() && !locks.contains(partDTO)
                                    && !locksByUser.contains(partDTO)) {
            locks.add(partDTO);
            locksByUser.add(partDTO);
            logger.debug("Added a lock.");
        }
        activeLocksFroRetro.put(retroId, locks);
        activeLocksByUser.put(session, locksByUser);
    }

}
