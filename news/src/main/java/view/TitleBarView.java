package view;

/**
 * Created by bruse on 16/3/22.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bruse.com.news.R;

public class TitleBarView extends FrameLayout {

    private RelativeLayout rl_leftHeadLayout; // 左边头像和红点

    private ImageCircleView icv_head; // 左边头像

    private ImageView iv_redPoint; // 左边红点

    private ImageView iv_leftBack; // 左边返回

    public static TextView tv_centerTitle; // 中间标题

    private TextView tv_rightSuccess; // 右边完成

    private ImageView iv_rightGoTo;   // 右边下一步

    private LinearLayout ll_leftBackImageLayout;

    private LinearLayout ll_rightNextImageLayout;

    private LinearLayout ll_rightNextTextLayout;

    public static LinearLayout ll_centerLayout;

    public static RelativeLayout rl_parentLayout;

    private ImageView iv_rightBtnOne;   // 右边第一个按钮

    private ImageView iv_rightBtnTwo;   // 右边第二个按钮

    private RelativeLayout rl_rightMessageLayout; //右边消息按钮

    private ImageView iv_messageBtn; // 右边消息按钮

    private TextView iv_messageRedPoint; // 右边消息红点按钮

    public static TextView tv_centerText;

    private ImageView iv_centerIcon;

    private ImageView iv_arrowUp;

    private ImageView iv_new;

    private AlphaAnimation alphaAnimation;


    // 枚举，调用方法用的
    public enum TitleBarEnum {
        icv_head,tv_centerTitle, ll_centerLayout,ll_leftBackImageLayout, ll_rightNextImageLayout, ll_rightNextTextLayout, iv_rightBtnOne, iv_rightBtnTwo, iv_messageBtn;
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.fragment_titlebar_view, this);

        initView();
    }

    public void initView() {

        rl_leftHeadLayout = (RelativeLayout) findViewById(R.id.left_head);
        icv_head = (ImageCircleView) findViewById(R.id.icv_head);
        iv_redPoint = (ImageView) findViewById(R.id.iv_red_point);
        iv_leftBack = (ImageView) findViewById(R.id.left_back_image_botton);
        tv_centerTitle = (TextView) findViewById(R.id.center_title);
        tv_rightSuccess = (TextView) findViewById(R.id.right_text_button);
        iv_rightGoTo = (ImageView) findViewById(R.id.right_image_button);
        tv_centerText = (TextView) findViewById(R.id.center_text);
        iv_centerIcon = (ImageView) findViewById(R.id.center_icon);

        ll_leftBackImageLayout = (LinearLayout) findViewById(R.id.left_back_layout);
        ll_rightNextImageLayout = (LinearLayout) findViewById(R.id.right_next_image_layout);
        ll_rightNextTextLayout = (LinearLayout) findViewById(R.id.right_next_text_layout);
        ll_centerLayout = (LinearLayout) findViewById(R.id.center_layout);

        rl_parentLayout = (RelativeLayout) findViewById(R.id.index_popup_layout);

        iv_rightBtnOne = (ImageView) findViewById(R.id.right_image_button_one);
        iv_rightBtnTwo = (ImageView) findViewById(R.id.right_image_button_two);

        rl_rightMessageLayout = (RelativeLayout) findViewById(R.id.right_message_layout);
        iv_messageBtn = (ImageView) findViewById(R.id.message_icon);
        iv_messageRedPoint = (TextView) findViewById(R.id.message_red_point);
        iv_arrowUp = (ImageView) findViewById(R.id.image_arrow_up);

        iv_new = (ImageView) findViewById(R.id.iv_new);
    }

    //设置三角图片是否显示
    public void setArrowUpIsVisibler(boolean isVisible) {

        if (isVisible) {
            iv_arrowUp.setVisibility(View.VISIBLE);
        } else {
            iv_arrowUp.setVisibility(View.GONE);
        }
    }

    //设置new图片是否显示
    public void setNewIconIsVisibler(boolean isVisible) {

        if (isVisible) {
            iv_new.setVisibility(View.VISIBLE);
            alphaAnimation= new AlphaAnimation(1.0f,0.0f);
            alphaAnimation.setDuration(1000);
            alphaAnimation.setRepeatCount(Animation.INFINITE);
            alphaAnimation.setStartOffset(1500);
            iv_new.setAnimation(alphaAnimation);
        } else {
            if(alphaAnimation!=null){
                alphaAnimation.cancel();
                alphaAnimation = null;
                iv_new.setAnimation(null);
            }
            iv_new.setVisibility(View.GONE);
        }
    }

    //设置左边返回按钮是否显示
    public void setLeftBackButtonIsVisibler(boolean isVisible) {

        if (isVisible) {
            iv_leftBack.setVisibility(View.VISIBLE);
        } else {
            iv_leftBack.setVisibility(View.GONE);
        }
    }

    //设置左边的头像是否显示
    public void setLeftHeadIsVisibler(boolean isVisible) {

        if (isVisible) {
            icv_head.setVisibility(View.VISIBLE);
        } else {
            icv_head.setVisibility(View.GONE);
        }
    }

    //设置左边的头像红点是否显示
    public void setLeftHeadRedPointIsVisible(boolean isVisible) {

        if (isVisible) {
            iv_redPoint.setVisibility(View.VISIBLE);
        } else {
            iv_redPoint.setVisibility(View.GONE);
        }
    }

    //设置左边那些都不显示
    public void setLeftLayoutIsGone() {

        iv_leftBack.setVisibility(View.GONE);
        iv_rightGoTo.setVisibility(View.GONE);
    }

    //设置中间标题的文本
    public void setCenterTitle(String titleString) {
        tv_centerTitle.setVisibility(View.VISIBLE);
        tv_centerTitle.setText(titleString);
    }

    //设置右边的文字按钮显示和内容
    public void setRightTextIsVisible(boolean isVisible, String textString) {
        if (isVisible) {
            tv_rightSuccess.setVisibility(View.VISIBLE);
            tv_rightSuccess.setText(textString);
        } else {
            tv_rightSuccess.setVisibility(View.GONE);
        }
    }

    //设置右边图片按钮显示和样式
    public void setRigthImageIsVisible(boolean isVisible, int id) {

        if (isVisible) {
            iv_rightGoTo.setVisibility(View.VISIBLE);
            iv_rightGoTo.setBackgroundResource(id);
        } else {
            iv_rightGoTo.setVisibility(View.GONE);
        }
    }

    //设置中间图片显示的样式
    public void setCenterIconDrawable(int id) {
        iv_centerIcon.setBackgroundResource(id);
    }

    //设置中间带图标的布局
    public void setCenterLayoutIsVisible(boolean isVisible,String text) {

        if (isVisible) {
            ll_centerLayout.setVisibility(View.VISIBLE);
            tv_centerTitle.setVisibility(View.GONE);
            tv_centerText.setText(text);
        } else {
            ll_centerLayout.setVisibility(View.GONE);
        }
    }

    //设置右边那些都不显示
    public void setRightLayoutIsGone() {

        tv_rightSuccess.setVisibility(View.GONE);
        rl_leftHeadLayout.setVisibility(View.GONE);
    }

    //设置右边第一个按钮的显示和图标
    public void setRightBtnOneIsVisible(boolean isVisible, int drawable) {
        if (isVisible) {
            iv_rightBtnOne.setVisibility(View.VISIBLE);
            iv_rightBtnOne.setBackgroundResource(drawable);
        }
    }

    //设置右边第二个按钮的显示和图标
    public void setRightBtnTwoIsVisible(boolean isVisible, int drawable) {
        if (isVisible) {
            iv_rightBtnTwo.setVisibility(View.VISIBLE);
            iv_rightBtnTwo.setBackgroundResource(drawable);
        }
    }

    //设置右边消息按钮的显示和红点是否显示
    public void setRightMessageBtnIsVisible(boolean isMessageBtnVisible, boolean isRedPointVisible) {
        if (isMessageBtnVisible) {
            rl_rightMessageLayout.setVisibility(View.VISIBLE);
            if (isRedPointVisible) {
                iv_messageRedPoint.setVisibility(View.VISIBLE);
            } else {
                iv_messageRedPoint.setVisibility(View.GONE);
            }
        }
    }

    //设置右边消息按钮红点显示的个数
    public void setRightMessageRedContent(String count) {
        iv_messageRedPoint.setText(count);
    }

    public void setIcv(String url, int defImgRes) {
        icv_head.setImageURL(url, defImgRes);
    }

    public void setListener(TitleBarEnum e, OnClickListener listener) {
        if (e == TitleBarEnum.icv_head) {
            icv_head.setOnClickListener(listener);
        } else if (e == TitleBarEnum.ll_leftBackImageLayout) {
            ll_leftBackImageLayout.setOnClickListener(listener);
        } else if (e == TitleBarEnum.ll_rightNextImageLayout) {
            ll_rightNextImageLayout.setOnClickListener(listener);
        } else if (e == TitleBarEnum.ll_rightNextTextLayout) {
            ll_rightNextTextLayout.setOnClickListener(listener);
        } else if (e == TitleBarEnum.iv_rightBtnOne) {
            iv_rightBtnOne.setOnClickListener(listener);
        } else if (e == TitleBarEnum.iv_rightBtnTwo) {
            iv_rightBtnTwo.setOnClickListener(listener);
        } else if (e == TitleBarEnum.iv_messageBtn) {
            iv_messageBtn.setOnClickListener(listener);
        }else if(e == TitleBarEnum.tv_centerTitle){
            tv_centerTitle.setOnClickListener(listener);
        }else if(e == TitleBarEnum.ll_centerLayout){
            ll_centerLayout.setOnClickListener(listener);
        }
    }
}

