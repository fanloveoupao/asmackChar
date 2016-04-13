package bruse.com.wechar.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bruse on 16/3/31.
 */
public class Friends implements Serializable {
    /**
     * 返回码
     */
    private int code;
    /**
     * 错误码 message
     */
    private String message;
    /**
     * 好友信息
     */
    private List<ApiResult> result;

    public Friends(){

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ApiResult> getResult() {
        return result;
    }

    public void setResult(List<ApiResult> result) {
        this.result = result;
    }
}

