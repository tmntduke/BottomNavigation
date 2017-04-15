package tmnt.example.bottomnavigation.weight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import tmnt.example.bottomnavigation.R;

/**
 * Created by tmnt on 2017/4/14.
 */

public class TabItem extends LinearLayout {

    private ImageView bottomImg;
    private TextView bottomTV;
    private View view;
    private int selectColor = Color.BLACK;
    private int defaultColor = Color.BLUE;
    private int res;
    private boolean choice = false;

    private Handler mHandler;

    private static final String TAG = "TabItem";


    public TabItem(Context context) {
        super(context);
        init(context);
    }

    public TabItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TabItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setRes(int res) {
        this.res = res;
    }

    private void init(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.bottom_item_lay, null);
        bottomImg = (ImageView) view.findViewById(R.id.img_bottom);
        bottomTV = (TextView) view.findViewById(R.id.tv_bottom);
        addView(view);
    }

    private void setTest(String text) {
        Log.i(TAG, "setTest: " + text);
        if (TextUtils.isEmpty(text)) {
            bottomTV.setVisibility(GONE);
        } else {
            bottomTV.setText(text);
        }

        Log.i(TAG, "setTest: " + bottomTV);
    }

    public void setIcon(boolean isChoice) {
        Bitmap bitmap = null;
        if (!isChoice) {
            bitmap = drawIcon(defaultColor);
            bottomTV.setTextColor(defaultColor);
        } else {
            bitmap = drawIcon(selectColor);
            bottomTV.setTextColor(selectColor);
        }
        Log.i(TAG, "setIcon: " + bitmap);
        bottomImg.setImageBitmap(bitmap);
        invalidate();
    }

    private Bitmap drawIcon(int color) {
        Bitmap bimap = ((BitmapDrawable) getResources().getDrawable(res)).getBitmap();
        Bitmap outBitmap = Bitmap.createBitmap(bimap.getWidth(), bimap.getHeight(), Bitmap.Config.ARGB_8888);


        Log.i(TAG, "drawIcon: " + bimap.getWidth() + "  " + bimap.getHeight());
        Bitmap alphaBitmap = bimap.extractAlpha();
        Canvas canvas = new Canvas(outBitmap);

        Paint paint = new Paint();
        paint.setColor(color);

        canvas.drawBitmap(alphaBitmap, 0, 0, paint);
        return outBitmap;
    }

    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }


    public static class Builder {
        private int selectColor;
        private int defaultColor;
        private String test;
        private int res;
        private Object mObject;
        private Context mContext;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setSelectColor(int selectColor) {
            this.selectColor = selectColor;
            return this;
        }

        public Builder setDefaultColor(int defaultColor) {
            this.defaultColor = defaultColor;
            return this;
        }

        public Builder setTest(String test) {
            this.test = test;
            return this;
        }

        public Builder setRes(int res) {
            this.res = res;
            return this;
        }

        public Builder setTag(Object o) {
            this.mObject = o;
            return this;
        }

        public TabItem build() {
            TabItem tabItem = new TabItem(mContext);
            tabItem.defaultColor = defaultColor;
            tabItem.selectColor = selectColor;
            tabItem.setTest(test);
            tabItem.res = res;
            Log.i(TAG, "build: " + mObject);
            tabItem.setTag(mObject);
            // tabItem.addView(tabItem.view);
            tabItem.setIcon(false);
            tabItem.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    tabItem.mHandler.obtainMessage(2001, v.getTag()).sendToTarget();
                    return true;
                }
            });
            return tabItem;

        }
    }

}
