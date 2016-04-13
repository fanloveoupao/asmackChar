package photochoose.controller;

/**
 * Created by bruse on 16/3/6.
 */
public interface Listener<T> {
    public void onStart(Object... params);

    public void onComplete(T result, Object... params);

    public void onFail(String msg, Object... params);
}

