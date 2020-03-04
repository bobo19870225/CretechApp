package cn.com.cretech.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import androidx.databinding.ViewDataBinding;
import cn.com.cretech.R;

public class CustomPopupDialog<V extends ViewDataBinding> {
    private Context mContext;
    private AlertDialog mDialog;
    private LayoutParams layoutParams;
    private V binDing ;

    public CustomPopupDialog(Context context,V binDing, boolean bottom) {
        mContext = context;
        if (mDialog == null) {
            this.binDing = binDing;
            mDialog = new AlertDialog.Builder(context,R.style.my_dialog).create();
            mDialog.setCanceledOnTouchOutside(true);
            mDialog.show();
            initView( bottom);
        }

    }


    public CustomPopupDialog(Context context, View layout, boolean bottom) {
        mContext = context;
        if (mDialog == null) {
            mDialog = new AlertDialog.Builder(context).create();
            mDialog.setCanceledOnTouchOutside(true);
            mDialog.show();
            mDialog.setContentView(layout);
            initUI(bottom);
        }

    }

    public void setAsSystemAlert() {
        mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    }

    public void initView( boolean bottom) {
        Window window = mDialog.getWindow();
        if (window != null) {
            //有编辑框时弹出键盘
            window.clearFlags(LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }
        mDialog.setContentView(binDing.getRoot());
        initUI(bottom);
    }

    private void initUI(boolean bottom) {
       /* WindowManager m = ((Activity) mContext).getWindowManager();
         Display d = m.getDefaultDisplay();
        layoutParams = mDialog.getWindow().getAttributes();
        layoutParams.width = d.getWidth() * 1;
        mDialog.getWindow().setAttributes(layoutParams);*/

        if (bottom) {
            mDialog.getWindow().setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
            //mDialog.getWindow().setWindowAnimations(R.style.AnimBottomDialog);
        }

        mDialog.setCanceledOnTouchOutside(true);

    }

    public <T extends View> T getViewById(int resId) {
        if (mDialog != null) {
            return (T) mDialog.findViewById(resId);
        }

        return null;
    }

    public void setOnClickListener(int viewId, OnClickListener l) {
        mDialog.findViewById(viewId).setOnClickListener(l);
    }

    public void setCanceledOnTouchOutside(boolean bOutCancel) {
        mDialog.setCanceledOnTouchOutside(bOutCancel);
    }

    public void setCancelable(boolean cancelable) {
        mDialog.setCancelable(cancelable);
    }

    public void show() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    public void setWH(int width , int height){
        Window window = mDialog.getWindow();
        //设置dialog弹窗宽高
        WindowManager.LayoutParams params= window.getAttributes();
        params.height= LinearLayout.LayoutParams.WRAP_CONTENT;
        params.width= LinearLayout.LayoutParams.MATCH_PARENT;

        window.setAttributes(params);

    }
}
