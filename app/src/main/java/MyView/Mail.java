package MyView;

import java.util.HashMap;

/**
 * Created by bruse on 16/3/8.
 */
public class Mail {
    public static final String KEY_MY_PROJECT_RECORD = "my_project_record";

    private static HashMap<String, Object> mail = new HashMap<String, Object>();

    public static void putMail(String key, Object obj) {
        mail.put(key, obj);
    }

    public static boolean getBooleanMailNoRemove(String key, boolean defaultBool) {
        Object obj = mail.get(key);
        if (obj != null) {
            if (obj instanceof Boolean) {
                return ((Boolean)obj).booleanValue();
            } else {
                return defaultBool;
            }
        } else {
            return defaultBool;
        }
    }

    public static void remove(String key){
        mail.remove(key);
    }

    public static Object getMail(String key) {
        Object obj = mail.get(key);
        mail.remove(key);
        return obj;
    }

    public static Object getMail(String key, Object defaultObj) {
        Object obj = null;
        if ((obj = getMail(key)) == null) {
            return defaultObj;
        } else {
            return obj;
        }
    }

    public static boolean getBooleanMail(String key, boolean defaultBool) {
        Object obj = null;
        if ((obj = getMail(key)) != null) {
            if (obj instanceof Boolean) {
                return ((Boolean)obj).booleanValue();
            } else {
                return defaultBool;
            }
        } else {
            return defaultBool;
        }
    }

}
