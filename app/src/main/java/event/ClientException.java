package event;

/**
 * Created by bruse on 16/3/4.
 */
public class ClientException extends Exception {

    private Exception mException;
    private String mDetail;
    private int mCode;

    public ClientException() { }

    public ClientException(int code) {
        setCode(code);
    }

    public ClientException(Exception ex) {
        setException(ex);
    }

    public ClientException(String detail) {
        setDetail(detail);
    }

    public int getCode() {
        return mCode;
    }

    public void setCode(int code) {
        this.mCode = code;
    }

    public Exception getException() {
        return mException;
    }

    public void setException(Exception mException) {
        this.mException = mException;
    }

    public String getDetail() {
        return mDetail;
    }

    public void setDetail(String mDetail) {
        this.mDetail = mDetail;
    }
}
