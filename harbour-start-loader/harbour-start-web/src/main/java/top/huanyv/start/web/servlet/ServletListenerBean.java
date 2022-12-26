package top.huanyv.start.web.servlet;

import java.util.EventListener;

/**
 * @author huanyv
 * @date 2022/12/25 16:51
 */
public class ServletListenerBean {

    private EventListener eventListener;

    public ServletListenerBean(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public EventListener getEventListener() {
        return eventListener;
    }
}
