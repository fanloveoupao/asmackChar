package event;

/**
 * Created by bruse on 16/3/6.
 */

import de.greenrobot.event.EventBus;

/**
 * 本单例持有两个时间总线，一个是消息中心的事件总线，一个是通用的事件总线，如有需要可以自己扩展总线
 *
 * @author HU
 *
 */
public class EventBusUtil {
    private EventBus mMessageEventBus = new EventBus();
    private EventBus mCommonEventBus = EventBus.getDefault();

    private EventBusUtil() {
    }

    private static class SingletonHolder {
        public static final EventBusUtil INSTANCE = new EventBusUtil();
    }

    public static EventBusUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public EventBus getMessageEventBus() {
        return mMessageEventBus;
    }

    public EventBus getCommonEventBus() {
        return mCommonEventBus;
    }
}
