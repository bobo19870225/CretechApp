package cn.com.cretech.util;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.widget.TextView;

public class TimerHelper {
    private TextView mTextView;
    private int mTime = 60;
    private String msg = "获取验证码";
    private String color = "#025EDC";
    Context context;

    public TimerHelper(TextView tv, Context context) {
        this.context = context;
        mTextView = tv;
        mHandler = new Handler();
    }

    Handler mHandler = null;

    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            --mTime;
            if (mTime > 0) {
                updateText();
            } else {
                finish();
            }
        }


    };

    public void setMessageText(String msg) {
        this.msg = msg;
    }

    public void setMessageTextColor(String color) {
        this.color = color;
    }

    private void updateText() {
        mTextView.setText( + mTime + "S");
        //mTextView.setTextColor(Color.parseColor("#ff999999"));
        if (mHandler != null) {
            mHandler.postDelayed(mRunnable, 1000);
        }

    }


    public boolean start() {
        mTextView.setEnabled(false);
        if (mTime != 60) {
            return false;
        } else {
            mTime = 60;
            updateText();
        }
        return true;
    }


    public void finish() {
        mHandler.removeCallbacks(mRunnable);
        mTime = 60;
        mTextView.setText(msg);
       // mTextView.setBackground(context.getResources().getDrawable(R.drawable.shape_live));
        if (StringUtils.isNotEmpty(color)) {
            mTextView.setTextColor(Color.parseColor(color));
        }
        mTextView.setEnabled(true);
    }

    public void clear() {
        if (mHandler != null) {
            mHandler = null;
        }
    }

}
