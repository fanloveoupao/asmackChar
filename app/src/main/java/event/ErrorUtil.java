package event;

/**
 * Created by bruse on 16/3/6.
 */
public class ErrorUtil {

    public static String ACCOUNT_INVALID = "0x00000001";  // 令牌失效
    public static String SERVER_ERROR = "0x00000005";  // 服务器异常
    public static String CHECK_COUNT_EMPTY = "0x00040001";
    public static String OUT_OF_AREA = "0x00040002";
    public static String UN_ACTIVE = "0x00040003";
    public static String PERSON_ACCOUNT_EXPIRED = "0x00040004";
    public static String PERSON_CHECK_COUNT_EMPTY = "0x00040005";
    public static String COMPANY_ACCOUNT_EXPIRED = "0x00040006";
    public static String COMPANY_COUNT_EMPTY = "0x00040007";
    public static String PERSON_OUT_OF_YEAR = "0x00040008";

    public static String getNetworkError() {
        return "网络异常，请检查你的网络";
    }

    public static String getServerError() {
        return "服务器异常";
    }


    public static String getCheckCountEmpty() {
        return "您的信息查看次数已用完，为了不影响您使用服务，请及时充值！";
    }

    public static String getAccountInvalid() {
        return "会话过期，请重新登录";
    }

    public static String getOutOfArea() {
        return "此工程信息超出您购买的服务区域！";
    }

    public static String getUnActive() {
        return "您的账号还未激活,请激活";
    }

    public static String getPersonAccountExpired() {
        return "您的信息查看次数已用完，为了不影响您使用服务，请及时充值！";
    }

    public static String getPersonCheckCountEmpty() {
        return "您的信息查看次数已用完，为了不影响您使用服务，请及时充值！";
    }

    public static String getCompanyAccountExpired() {
        return "您的高级会员服务试用期已结束，如需开通正式服务，请及时联系客服专员";
    }

    public static String getCompanyCheckCountEmpty() {
        return "您今天的信息查看次数已用完，明天才能查看哦！";
    }

    public static String getPersonOutOfYear() {
        return "抱歉，试用会员只能查看天工网最近一年发布的工程信息！";
    }

}

