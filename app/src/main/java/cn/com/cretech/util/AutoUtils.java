package cn.com.cretech.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;

public class AutoUtils {

    private static int projectWidth = 360;//项目的宽度
    private static int projectHeight = 640;//项目的高度

    public static float currentWidth;//使用当前手机屏幕的宽度的dp值
    private static float currentHeight;//使用当前手机屏幕的高度的dp值


    private static float widthRatio;
    private static float heightRatio;


    public static void setAutoUtil(FragmentActivity mContext){

        DisplayMetrics dm = new DisplayMetrics();
        Display defaultDisplay = mContext.getWindowManager().getDefaultDisplay();
        defaultDisplay.getMetrics(dm);
        currentWidth = dm.widthPixels;
    }

    public static void setRatioValue(FragmentActivity mContext){
        DisplayMetrics dm = new DisplayMetrics();
        Display defaultDisplay = mContext.getWindowManager().getDefaultDisplay();
        defaultDisplay.getMetrics(dm);
        currentWidth = dm.widthPixels;
        int widthPixels = dm.widthPixels;
        int heightPixels = dm.heightPixels;
        float density = dm.density;
        int densityDpi = dm.densityDpi;
        //Log.i("TAG","-----------------"+widthPixels);

        currentWidth = widthPixels/density+0.5f;
        currentHeight = heightPixels/density+0.5f;

        widthRatio = currentWidth / projectWidth;
        heightRatio = currentHeight / projectHeight;
    }

    /**
     * 获得顶部状态栏的高度
     */
    public static int getStatusHeight(Context context){

        int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");

        return  context.getResources().getDimensionPixelOffset(identifier);
    }
    /**
     * 获得底部导航栏的高度
     */
    private static int getNavigationHeight(Context context){

        int identifier = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");

        return  context.getResources().getDimensionPixelOffset(identifier);
    }

    public static void auto(FragmentActivity act){
        if(act==null || currentWidth<1 || currentHeight<1)return;

        View view=act.getWindow().getDecorView();
        auto(view);
    }

    public static void auto(View view){
        if(view==null || currentWidth<1 || currentHeight<1)return;
        AutoUtils.autoTextSize(view);
        AutoUtils.autoSize(view);
        AutoUtils.autoPadding(view);
        AutoUtils.autoMargin(view);
        if(view instanceof ViewGroup){
            auto((ViewGroup)view);
        }

    }

    private static void auto(ViewGroup viewGroup){
        int count = viewGroup.getChildCount();

        for (int i = 0; i < count; i++) {

            View child = viewGroup.getChildAt(i);

            if(child!=null){
                auto(child);
            }
        }
    }

    public static void autoMargin(View view){
        if (!(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams))
            return;

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if(lp == null)return ;

        lp.leftMargin = getDisplayWidthValue(lp.leftMargin);
        lp.topMargin = getDisplayHeightValue(lp.topMargin);
        lp.rightMargin = getDisplayWidthValue(lp.rightMargin);
        lp.bottomMargin = getDisplayHeightValue(lp.bottomMargin);

    }

    public static void autoPadding(View view){
        int l = view.getPaddingLeft();
        int t = view.getPaddingTop();
        int r = view.getPaddingRight();
        int b = view.getPaddingBottom();

        l = getDisplayWidthValue(l);
        t = getDisplayHeightValue(t);
        r = getDisplayWidthValue(r);
        b = getDisplayHeightValue(b);

        view.setPadding(l, t, r, b);
    }

    public static void autoSize(View view){
        ViewGroup.LayoutParams lp = view.getLayoutParams();

        if (lp == null) return;

        if(lp.width>0){
            lp.width = getDisplayWidthValue(lp.width);
        }

        if(lp.height>0){
            lp.height = getDisplayHeightValue(lp.height);
        }

    }

    public static void autoTextSize(View view){
        if(view instanceof TextView){
            double designPixels=((TextView) view).getTextSize();
            double displayPixels=widthRatio*designPixels;
            ((TextView) view).setIncludeFontPadding(false);
            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) displayPixels);
        }
    }

    public static int getDisplayWidthValue(int designWidthValue){
        if(designWidthValue<2){
            return designWidthValue;
        }
        int i =(int) ( designWidthValue * widthRatio);
        return i;
    }

    public static int getDisplayHeightValue(int designHeightValue){
        if(designHeightValue<2){
            return designHeightValue;
        }
        int i = (int) (designHeightValue * heightRatio);
        return i;
    }
}
