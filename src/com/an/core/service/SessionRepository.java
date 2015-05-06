package com.an.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Session 状态管理
 * Created by karas on 9/2/14.
 */
public class SessionRepository {
    private static final Logger logger = LoggerFactory
            .getLogger(SessionRepository.class);

    private static Map<String, String> SESSION_STORE = new HashMap<>();

    public static void forceQuit(String userId) {
        for (Iterator<Map.Entry<String, String>> i = SESSION_STORE.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry<String, String> entry = i.next();
            if (userId.equals(entry.getValue())) {
                i.remove();
            }
        }
    }

    public static void add(String userId, String sessionId) {
        SESSION_STORE.put(sessionId, userId);
    }

    public static boolean isAlive(String sid) {
        return SESSION_STORE.containsKey(sid);
    }

    public static boolean isOnline(String userId, HttpSession session) {
        for (Map.Entry<String, String> entry : SESSION_STORE.entrySet()) {
            if (entry.getValue().equals(userId) && !session.getId().equals(entry.getKey())) {
                return true;
            }
        }
        return false;
    }

    public static void remove(String sid) {
        SESSION_STORE.remove(sid);
    }


}
