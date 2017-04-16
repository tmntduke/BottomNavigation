## 底部导航栏

![bottom](bottom.png)

### 实现

1. TabItem采用复合控件形式（ImageView + TextView）
   
   添加图标并设置指定颜色

   ```java
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
   ```

2. BottomNavigation添加TabItem

  ```java
   class BaseController implements Controller {
        private static final String TAG = "BottomNavigationLayout";

        @Override
        public Controller addTabItem(TabItem tabItem) {
            mTabItems.add(tabItem);
            return this;
        }

        @Override
        public Controller build() {
            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT);
            layoutParams.weight = 1.0f;
            layoutParams.gravity = Gravity.CENTER;
            for (TabItem tabItem : mTabItems) {
                tabItem.setHandler(new BottomHanler());
                tabItem.setLayoutParams(layoutParams);
                BottomNavigationLayout.this.addView(tabItem, layoutParams);
            }

            setSelectBottom();
            return this;
        }
  ```

3. 使用Handler通信 获取TabItem点击事件，设置选中颜色
   
   由handler发送当前点击TabIem的tag，在BottomNavigation中循环查找后设置监听

  底部导航
  ```java

     class BottomHanler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2001) {
                mTag = msg.obj;
                Log.i(TAG, "handleMessage: " + mTag);
                for (int i = 0; i < mTabItems.size(); i++) {
                    TabItem tabItem = mTabItems.get(i);
                    if (mTabItems.get(i).getTag().equals(mTag)) {
                        index = i;
                        Log.i(TAG, "handleMessage: " + index);
                        mOnTabItemSelectListener.onSelected(index, mTag);

                        tabItem.setIcon(true);
                    } else {
                        tabItem.setIcon(false);
                    }
                }
                invalidate();//刷新视图
            }
        }
    }

  ```

  TabItem

  ```java
   tabItem.setOnClickListener(v ->
                    tabItem.mHandler.obtainMessage(2001, v.getTag()).sendToTarget());
  ```

### 使用
1. 创建选项卡
    
      ```java
       TabItem tabItem3 = new TabItem.Builder(this)
                .setDefaultColor(0xFFACACAC)
                .setSelectColor(getResources().getColor(R.color.colorAccent))
                .setTest("wo")
                .setRes(R.drawable.lsv)
                .setTag("ja")
                .build();
      ```

2. 添加选项卡

   ```java
     Controller controller = mBottom.create()
                .addTabItem(tabItem)
                .addTabItem(tabItem1)
                .addTabItem(tabItem2)
                .addTabItem(tabItem3)
                .addTabItem(tabItem4)
                .build();
   ```

   Controller用于控制导航栏

3. 添加点击事件

   ```java
    controller.addTabItemClickListener(new OnTabItemSelectListener() {
            @Override
            public void onSelected(int index, Object tag) {
               
            }

            @Override
            public void onRepeatClick(int index, Object tag) {

            }
        });
   ```


