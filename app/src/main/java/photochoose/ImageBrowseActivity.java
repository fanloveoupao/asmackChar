package photochoose;

/**
 * Created by bruse on 16/3/6.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import bruse.com.bruseapp.R;
import photochoose.adapter.ImagePagerAdapter;

/**
 * 大图图片浏览器
 */
public class ImageBrowseActivity extends Activity {
    public enum LaunchEnum {
        noSelected,//不需要显示checkbox
        selected//需要显示checkbox
    }
    public static void  launch(Activity activity, ArrayList<String> list, int index){
        Intent i = new Intent(activity, ImageBrowseActivity.class);
        i.putExtra(ImageBrowseActivity.EXTRA_IMAGES, list);
        i.putExtra(ImageBrowseActivity.EXTRA_INDEX, index);
        i.putExtra(ImageBrowseActivity.EXTRA_LAUNCH_MODEL, LaunchEnum.noSelected);
        activity.startActivity(i);
        //设置启动的动画
        activity.overridePendingTransition(R.anim.zoom_in, R.anim.no_anim);
    }

    public static void  launch(Activity activity, ArrayList<String> list, int index, LaunchEnum launchEnum){
        Intent i = new Intent(activity, ImageBrowseActivity.class);
        i.putExtra(ImageBrowseActivity.EXTRA_IMAGES, list);
        i.putExtra(ImageBrowseActivity.EXTRA_INDEX, index);
        i.putExtra(ImageBrowseActivity.EXTRA_LAUNCH_MODEL, launchEnum);
        activity.startActivity(i);
        //设置启动的动画
        activity.overridePendingTransition(R.anim.zoom_in, R.anim.no_anim);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.no_anim, R.anim.zoom_out);
    }

    public static final String EXTRA_LAUNCH_MODEL = "extra_launch_model";
    public static final String EXTRA_IMAGES = "extra_images";
    public static final String EXTRA_INDEX = "extra_index";

    private ArrayList<String> mDatas = new ArrayList<String>();
    private LaunchEnum mLaunchEnum = LaunchEnum.noSelected;
    private int mPageIndex = 0;
    private ImagePagerAdapter mImageAdapter = null;
    private ViewPager mViewPager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_browse);
        mViewPager = (ViewPager)findViewById(R.id.image_vp);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_IMAGES)) {
            mDatas = intent.getStringArrayListExtra(EXTRA_IMAGES);
            mLaunchEnum = (LaunchEnum) intent.getSerializableExtra(EXTRA_LAUNCH_MODEL);
            mPageIndex = intent.getIntExtra(EXTRA_INDEX, 0);
            mImageAdapter = new ImagePagerAdapter(this, mDatas, mLaunchEnum);
            mViewPager.setAdapter(mImageAdapter);
            mViewPager.setCurrentItem(mPageIndex);
        }
    }


}
