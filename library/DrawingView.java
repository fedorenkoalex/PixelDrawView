package com.pixeldrawview.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.pixeldrawview.library.Pixel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fedor on 27.07.2016.
 */
public class DrawingView extends View {

    private Context mContext;

    private int rectSize = 0;
    private int width = 0;
    private int height = 0;

    private int rectXCount = 50;
    private int rectYCount = 50;

    public int getRectXCount() {
        return rectXCount;
    }

    public int getRectYCount() {
        return rectYCount;
    }

    private float currZoom = 1.0f;

    private final float MIN_ZOOM = 0.41f;
    private final float MAX_ZOOM = 1.8f;

    private int currentColor = 0xFF000000;

    private boolean isBrushEnabled = false;

    private boolean isPipette = false;

    private DrawingViewListener mListener;

    public void setListener(DrawingViewListener listener) {
        this.mListener = listener;
    }

    private List<Pixel> mPixelsList;

    public void setColor(int color) {
        this.currentColor = color;
    }

    public int getColor() {
        return currentColor;
    }

    public interface DrawingViewListener {
        void onPrepared();

        void onBitmapReady(Bitmap bmp);

        void onPickedColor(int color);
    }

    public List<Pixel> getPixels() {
        return mPixelsList;
    }

    public void setPixels(List<Pixel> data) {
        mPixelsList = data;
        currZoom = mPixelsList.get(0).getZoom();
        invalidate();
    }

    public void enableBrushing() {
        isBrushEnabled = true;
    }

    public void disableBrushing() {
        isBrushEnabled = false;
    }

    public boolean isBrushEnabled() {
        return isBrushEnabled;
    }

    public void setPipette(boolean enabled) {
        isPipette = enabled;
    }

    public DrawingView(Context context) {
        super(context);
        init(context);
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        mPixelsList = new ArrayList<>();
    }

    private Bitmap result;
    private Canvas bitmapCanvas;

    public Bitmap getResultThumb() {

        Bitmap result = getResultAreaV2(false);
        Bitmap resized = Bitmap.createScaledBitmap(result, result.getWidth() / 5, result.getHeight() / 5, false);
        return resized;
    }

    public void getResultAll(boolean withBorders) {

        Pixel pisi = mPixelsList.get(0);
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        result = Bitmap.createBitmap((int) (pisi.getCellSize() * 50), (int) (pisi.getCellSize() * 50), conf);
        bitmapCanvas = new Canvas(result);

        for (int i = 0; i < mPixelsList.size(); i++) {
            Pixel pixel = mPixelsList.get(i);
            Rect rect = pixel.getRectangleClear();
            bitmapCanvas.drawRect(rect, pixel.getFillPaint());
            if (withBorders) {
                bitmapCanvas.drawRect(rect, pixel.getBorderPaint());
            }
        }
        if (mListener != null) {
            mListener.onBitmapReady(result);
        }
    }

    public void getResultArea(boolean withBorders) {
        getResultAreaV2(withBorders);
    }


    private Bitmap overlay;


    public Bitmap getOverlayBmp() {
        return overlay;
    }

    public void setOverlay(Bitmap overlay) {
        this.overlay = overlay;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPixelsList == null) {
            return;
        }
        for (int i = 0; i < mPixelsList.size(); i++) {
            Pixel pixel = mPixelsList.get(i);

            Rect rect = pixel.getRectangle();
            if (((rect.right > 0 && rect.right < width) && (rect.bottom > 0 && rect.bottom < height)) || ((rect.left < width && rect.left > 0) && (rect.top < height && rect.top > 0))) {
                canvas.drawRect(rect, pixel.getFillPaint());
                canvas.drawRect(rect, pixel.getBorderPaint());
            }
        }
    }

    public void fillArea(Pixel tapped) {
        //todo
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectSize = (int) ((w * 5) / 100);
        width = w;
        height = h;
        if (mListener != null) {
            mListener.onPrepared();
        }
    }


    public void initView(int xCount, int yCount) {
        this.rectXCount = xCount;//= (int) (width / rectSize);
        this.rectYCount = yCount;//= (int) (height / rectSize);
        for (int i = 0; i < rectYCount; i++) {
            for (int j = 0; j < rectXCount; j++) {
                Pixel pixel = new Pixel();
                pixel.setCellSize(rectSize);
                pixel.setiPos(i);
                pixel.setjPos(j);
                mPixelsList.add(pixel);
            }
        }
        invalidate();
    }

    public Bitmap getResultAreaV2(boolean withBorders) {
        List<Pixel> resultList = new ArrayList<>();
        for (int i = 0; i < mPixelsList.size(); i++) {
            if (mPixelsList.get(i).getFillPaintColor() != Color.WHITE) {
                resultList.add(mPixelsList.get(i));
            }
        }
        //x = J param; y = i param;
        int minXPosition = resultList.get(0).getjPos();
        int maxXPosition = resultList.get(0).getjPos();

        int minYPosition = resultList.get(0).getiPos();
        int maxYPosition = resultList.get(0).getiPos();

        for (int i = 0; i < resultList.size(); i++) {
            //getMinX
            if (resultList.get(i).getjPos() < minXPosition) {
                minXPosition = resultList.get(i).getjPos();
            }
            //getMaxX
            if (resultList.get(i).getjPos() > maxXPosition) {
                maxXPosition = resultList.get(i).getjPos();
            }
            //getMinY
            if (resultList.get(i).getiPos() < minYPosition) {
                minYPosition = resultList.get(i).getiPos();
            }
            //getMaxY
            if (resultList.get(i).getiPos() > maxYPosition) {
                maxYPosition = resultList.get(i).getiPos();
            }
        }
        int xLength = maxXPosition - minXPosition + 1;
        int yLength = maxYPosition - minYPosition + 1;

        List<Pixel> filledPixelsList = new ArrayList<>();
        Pixel ethalonPixel = resultList.get(0);
        //x = J param; y = i param;
        for (int j = minXPosition; j < (minXPosition + xLength); j++) {
            for (int i = minYPosition; i < (minYPosition + yLength); i++) {
                Pixel pixelA = findPixelInList(resultList, i, j);

                if (pixelA == null) {
                    Log.d("Pixel Null", "Fill with empty");
                    pixelA = Pixel.getEmptyPixel(i, j);
                    pixelA.setZoom(ethalonPixel.getZoom());
                    pixelA.setCellSize(ethalonPixel.getCellSize());
                    pixelA.setOffsetY(ethalonPixel.getOffsetY());
                    pixelA.setOffsetX(ethalonPixel.getOffsetX());
                }
                Pixel findedPixel = Pixel.copy(pixelA);
                findedPixel.setiPos(findedPixel.getiPos() - minYPosition);
                findedPixel.setjPos(findedPixel.getjPos() - minXPosition);
                filledPixelsList.add(findedPixel);
            }
        }

        //draw
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        result = Bitmap.createBitmap((xLength) * ethalonPixel.getCellSize(), (yLength) * ethalonPixel.getCellSize(), conf);
        bitmapCanvas = new Canvas(result);
        for (int i = 0; i < filledPixelsList.size(); i++) {
            Pixel pixel = filledPixelsList.get(i);
            Rect rect = pixel.getRectangleClear();
            if ((rect.right > 0 && rect.bottom > 0) || (rect.left < width && rect.top < height)) {
                bitmapCanvas.drawRect(rect, pixel.getFillPaint());
                if (withBorders) {
                    bitmapCanvas.drawRect(rect, pixel.getBorderPaint());
                }
            }
        }
        if (mListener != null) {
            mListener.onBitmapReady(result);
        }
        return result;
    }

    private Pixel findPixelInList(List<Pixel> list, int x, int y) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getiPos() == x && list.get(i).getjPos() == y) {
                return list.get(i);
            }
        }
        return null;
    }

    public boolean zoomIn() {
        if (currZoom >= MAX_ZOOM) {
            return false;
        }
        currZoom += 0.2;
        Log.d("CurrentZoom", "" + currZoom);
        Log.d("CurrentZoomInt", "" + getSeekBarZoomPosition());
        for (int i = 0; i < mPixelsList.size(); i++) {
            mPixelsList.get(i).setZoom(currZoom);
        }
        invalidate();
        return true;
    }

    public int getSeekBarZoomPosition() {
        float realZoom = currZoom - MIN_ZOOM;
        int zoomInt = (int) ((realZoom * 10) / 2);
        return zoomInt;
    }

    public boolean zoomOut() {
        if (currZoom <= MIN_ZOOM) {
            return false;
        }
        currZoom -= 0.2;
        Log.d("CurrentZoom", "" + currZoom);
        Log.d("CurrentZoomInt", "" + getSeekBarZoomPosition());
        for (int i = 0; i < mPixelsList.size(); i++) {
            mPixelsList.get(i).setZoom(currZoom);
        }
        invalidate();
        return true;
    }

    public void clearAll() {
        for (int i = 0; i < mPixelsList.size(); i++) {
            mPixelsList.get(i).fillWithColor(Color.WHITE);
        }
        invalidate();
    }

    //touch variables

    private int touchX = 0;
    private int touchY = 0;

    private int lasttouchX = 0;
    private int lasttouchY = 0;

    private int startPointX = 0;
    private int startPointY = 0;

    private boolean hasMoved = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                touchX = (int) event.getX(0);
                touchY = (int) event.getY(0);
                hasMoved = false;
                break;
            }

            case MotionEvent.ACTION_UP:
                if (hasMoved) {
                    break;
                }
                if (mPixelsList == null) {
                    return false;
                }

                if ((int) event.getX(0) != lasttouchX && (int) event.getY(0) != lasttouchY) {
                    for (int i = 0; i < mPixelsList.size(); i++) {
                        if (mPixelsList.get(i).getRectangle().contains((int) event.getX(0), (int) event.getY(0))) {
                            if (isPipette) {
                                if (mListener != null) {
                                    mListener.onPickedColor(mPixelsList.get(i).getFillPaint().getColor());
                                }
                            } else {
                                mPixelsList.get(i).fillWithColor(currentColor);
                                invalidate();
                            }
                            break;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() < 2) {
                    if (isBrushEnabled) {
                        int deltaX = touchX - (int) event.getX(0);
                        int deltaY = touchY - (int) event.getY(0);
                        int cellTrigger = mPixelsList.get(0).getCellSize() / 8;
                        if (Math.abs(deltaX) < cellTrigger || Math.abs(deltaY) < cellTrigger) {
                            break;
                        }

                        for (int i = 0; i < mPixelsList.size(); i++) {
                            if (mPixelsList.get(i).getRectangle().contains((int) event.getX(0), (int) event.getY(0))) {
                                if (mPixelsList.get(i).getFillPaint().getColor() != currentColor) {
                                    mPixelsList.get(i).fillWithColor(currentColor);
                                    invalidate();
                                }
                                break;
                            }
                        }
                    }
                    break;
                }
                hasMoved = true;
                int deltaX = touchX - (int) event.getX(0);
                int deltaY = touchY - (int) event.getY(0);
                startPointX = startPointX - deltaX;
                startPointY = startPointY - deltaY;
                touchX = (int) event.getX();
                touchY = (int) event.getY();
                lasttouchX = touchX;
                lasttouchY = touchY;
                for (int i = 0; i < mPixelsList.size(); i++) {
                    mPixelsList.get(i).setOffsetX(startPointX);
                    mPixelsList.get(i).setOffsetY(startPointY);
                }
                invalidate();

                break;
        }
        return true;
    }
}
