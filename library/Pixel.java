package com.pixeldrawview.library;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;


/**
 * Created by fedor on 27.07.2016.
 */

public class Pixel {

    public static Pixel copy(Pixel pixel) {
        Pixel result = new Pixel();
        result.setArtid(pixel.getArtid());
        result.setOffsetX(pixel.getOffsetX());
        result.setOffsetY(pixel.getOffsetY());
        result.setCellSize(pixel.getCellSize());
        result.setiPos(pixel.getiPos());
        result.setjPos(pixel.getjPos());
        result.setZoom(pixel.getZoom());
        result.setFillPaint(pixel.getFillPaintColor());
        return result;
    }

    private long artid;
    private int offsetX;
    private int offsetY;
    private int cellSize;
    private int iPos;
    private int jPos;
    private float zoom;
    private int fillPaint;

    private Paint paintFill;
    private Paint paintBorder;

    public void setFillPaint(int fillPaint) {
        this.fillPaint = fillPaint;
    }

    public int getFillPaintColor() {
        return fillPaint;
    }

    public float getZoom() {
        return zoom;
    }

    public void setiPos(int iPos) {
        this.iPos = iPos;
    }

    public void setjPos(int jPos) {
        this.jPos = jPos;
    }


    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
    }

    public int getCellSize() {
        return cellSize;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public int getiPos() {
        return iPos;
    }

    public int getjPos() {
        return jPos;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }


    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public long getArtid() {
        return artid;
    }

    public void setArtid(long artid) {
        this.artid = artid;
    }


    public Pixel() {
        fillPaint = Color.WHITE;
        paintBorder = new Paint();
        paintBorder.setColor(Color.BLACK);
        paintBorder.setStyle(Paint.Style.STROKE);
        paintFill = new Paint();
        paintFill.setColor(fillPaint);
        paintFill.setStyle(Paint.Style.FILL);
        offsetX = 0;
        offsetY = 0;
        zoom = 1.0f;
    }


    public Rect getRectangle() {
        int left = (int) ((cellSize * zoom) * jPos);
        int top = (int) ((cellSize * zoom) * iPos);
        int right = (int) (left + (cellSize * zoom));
        int bottom = (int) (top + +(cellSize * zoom));
        return new Rect(left + offsetX, top + offsetY, right + offsetX, bottom + offsetY);
    }

    public Rect getRectangleClear() {
        int left = (int) ((cellSize) * jPos);
        int top = (int) ((cellSize) * iPos);
        int right = (int) (left + (cellSize));
        int bottom = (int) (top + +(cellSize));
        return new Rect(left, top, right, bottom);
    }

    public Paint getBorderPaint() {
        paintBorder = new Paint();
        paintBorder.setColor(Color.BLACK);
        paintBorder.setStyle(Paint.Style.STROKE);
        return paintBorder;
    }

    public Paint getFillPaint() {

        paintFill = new Paint();
        paintFill.setColor(fillPaint);
        paintFill.setStyle(Paint.Style.FILL);

        return paintFill;
    }

    public void fillWithColor(int color) {
        fillPaint = color;
        paintFill.setColor(color);
    }
}
