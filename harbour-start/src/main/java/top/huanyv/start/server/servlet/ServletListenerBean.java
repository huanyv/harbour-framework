package top.huanyv.start.server.servlet;

import top.huanyv.start.server.NativeServletRegistry;

import java.util.EventListener;

/**
 * @author huanyv
 * @date 2022/12/25 16:51
 */
public class ServletListenerBean implements Registration {

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

    @Override
    public void addRegistration(NativeServletRegistry servletRegistry) {
        servletRegistry.addListener(this.eventListener);
    }
}
