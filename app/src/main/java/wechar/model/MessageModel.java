package wechar.model;

/**
 * Created by bruse on 16/4/11.
 */
public class MessageModel {
    public static String TYPE_SEND="0";
    public static String TYPE_RECEIVE="1";
    private String from;
    private String message;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
