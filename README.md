# Pixel Draw View
Simple view for creating small pixel arts

# Instructions

**Copy `DrawingView.java` and `Pixel.java` classes to Your project**

**Declare view in Your layout file**
```	
     <com.yourpacket.DrawingView
        android:id="@+id/draw_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```
	
**Write in Your activity**
Note: xCount and yCount params number of cells on X and Y
```
        mDrawView = (DrawingView) findViewById(R.id.draw_view);
        mDrawView.setColor(Color.BLACK);
        mDrawView.setListener(new DrawingView.DrawingViewListener() {
            @Override
            public void onPrepared() {
                mDrawView.initView(xCount, yCount);
            }

            @Override
            public void onBitmapReady(Bitmap bmp) {

            }

            @Override
            public void onPickedColor(int color) {

            }
        });
```

# Class methods description

**`void initView(int xCount,int yCount)` - init view with x and y size**
Note: Call in `onPrepared()` callback 

**`List<Pixel> getPixels()` - get list of pixels from view**

**`void setPixels(List<Pixel> data)` - set list of pixels to view**

**`void enableBrushing()` - enable paint when move**
Note: Disable by default. By default one tap on cell fill it with color

**`void disableBrushing()` - disable paint when move**	

**`void disableBrushing()` - disable paint when move**

**`boolean isBrushEnabled()` - is enabled paint when move**

**`void setPipette(boolean enabled)` - enable/disable pipette mode(pick color from cell)**

**`void getResultAll(boolean withBorders)` - get all view bitmap**
Note: result will be returned to `void onBitmapReady(Bitmap bmp)`

**`void getResultArea(boolean withBorders)` - get bitmap of area on the screen**
Note: result will be returned to `void onBitmapReady(Bitmap bmp)`

**`boolean zoomIn()` - increase zoom**
Note: return boolean param is zoom applied

**`boolean zoomOut()` - decrease zoom**
Note: return boolean param is zoom applied

**`void clearAll()` - clear view**

