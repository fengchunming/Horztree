package com.an.core.service;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

/**
 * Session过期时从 SessionStore中移除Session
 * Created by karas on 10/13/14.
 */
public class SessionListener implements HttpSessionListener {
	private static Logger logger = Logger.getLogger(SecurityFilter.class);
    @Override
    public void sessionCreated(HttpSessionEvent sessionEvent) {

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent sessionEvent) {
        HttpSession session = sessionEvent.getSession();
        SessionRepository.remove(session.getId());
        
        logger.info("---------sessionId="+session.getId());
        logger.info("---------sessionCeationTime="+session.getCreationTime());
        logger.info("---------sessionMaxInactiveInterval="+session.getMaxInactiveInterval());
        logger.info("---------sessionLastAccessedTime="+session.getLastAccessedTime());
    }
}
