//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.scanlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.scanlibrary.R.color;
import com.scanlibrary.R.drawable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PolygonView extends FrameLayout {
  protected Context context;
  private Paint paint;
  private ImageView pointer1;
  private ImageView pointer2;
  private ImageView pointer3;
  private ImageView pointer4;
  private ImageView midPointer13;
  private ImageView midPointer12;
  private ImageView midPointer34;
  private ImageView midPointer24;
  private PolygonView polygonView;

  public PolygonView(Context context) {
    super(context);
    this.context = context;
    this.init();
  }

  public PolygonView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
    this.init();
  }

  public PolygonView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.context = context;
    this.init();
  }

  private void init() {
    this.polygonView = this;
    this.pointer1 = this.getImageView(0, 0);
    this.pointer2 = this.getImageView(this.getWidth(), 0);
    this.pointer3 = this.getImageView(0, this.getHeight());
    this.pointer4 = this.getImageView(this.getWidth(), this.getHeight());
    this.midPointer13 = this.getImageView(0, this.getHeight() / 2);
    this.midPointer13.setOnTouchListener(new MidPointTouchListenerImpl(this.pointer1, this.pointer3));
    this.midPointer12 = this.getImageView(0, this.getWidth() / 2);
    this.midPointer12.setOnTouchListener(new MidPointTouchListenerImpl(this.pointer1, this.pointer2));
    this.midPointer34 = this.getImageView(0, this.getHeight() / 2);
    this.midPointer34.setOnTouchListener(new MidPointTouchListenerImpl(this.pointer3, this.pointer4));
    this.midPointer24 = this.getImageView(0, this.getHeight() / 2);
    this.midPointer24.setOnTouchListener(new MidPointTouchListenerImpl(this.pointer2, this.pointer4));
    this.addView(this.pointer1);
    this.addView(this.pointer2);
    this.addView(this.midPointer13);
    this.addView(this.midPointer12);
    this.addView(this.midPointer34);
    this.addView(this.midPointer24);
    this.addView(this.pointer3);
    this.addView(this.pointer4);
    this.initPaint();
  }

  protected void attachViewToParent(View child, int index, LayoutParams params) {
    super.attachViewToParent(child, index, params);
  }

  private void initPaint() {
    this.paint = new Paint();
    this.paint.setColor(this.getResources().getColor(color.blue));
    this.paint.setStrokeWidth(2.0F);
    this.paint.setAntiAlias(true);
  }

  public Map<Integer, PointF> getPoints() {
    List<PointF> points = new ArrayList();
    points.add(new PointF(this.pointer1.getX(), this.pointer1.getY()));
    points.add(new PointF(this.pointer2.getX(), this.pointer2.getY()));
    points.add(new PointF(this.pointer3.getX(), this.pointer3.getY()));
    points.add(new PointF(this.pointer4.getX(), this.pointer4.getY()));
    return this.getOrderedPoints(points);
  }

  public Map<Integer, PointF> getOrderedPoints(List<PointF> points) {
    PointF centerPoint = new PointF();
    int size = points.size();

    PointF pointF;
    for(Iterator var4 = points.iterator(); var4.hasNext(); centerPoint.y += pointF.y / (float)size) {
      pointF = (PointF)var4.next();
      centerPoint.x += pointF.x / (float)size;
    }

    Map<Integer, PointF> orderedPoints = new HashMap();

    PointF a_pointF;
    byte index;
    for(Iterator var9 = points.iterator(); var9.hasNext(); orderedPoints.put(Integer.valueOf(index), a_pointF)) {
      a_pointF = (PointF)var9.next();
      index = -1;
      if (a_pointF.x < centerPoint.x && a_pointF.y < centerPoint.y) {
        index = 0;
      } else if (a_pointF.x > centerPoint.x && a_pointF.y < centerPoint.y) {
        index = 1;
      } else if (a_pointF.x < centerPoint.x && a_pointF.y > centerPoint.y) {
        index = 2;
      } else if (a_pointF.x > centerPoint.x && a_pointF.y > centerPoint.y) {
        index = 3;
      }
    }

    return orderedPoints;
  }

  public void setPoints(Map<Integer, PointF> pointFMap) {
    if (pointFMap.size() == 4) {
      this.setPointsCoordinates(pointFMap);
    }

  }

  private void setPointsCoordinates(Map<Integer, PointF> pointFMap) {
    this.pointer1.setX(((PointF)pointFMap.get(0)).x);
    this.pointer1.setY(((PointF)pointFMap.get(0)).y);
    this.pointer2.setX(((PointF)pointFMap.get(1)).x);
    this.pointer2.setY(((PointF)pointFMap.get(1)).y);
    this.pointer3.setX(((PointF)pointFMap.get(2)).x);
    this.pointer3.setY(((PointF)pointFMap.get(2)).y);
    this.pointer4.setX(((PointF)pointFMap.get(3)).x);
    this.pointer4.setY(((PointF)pointFMap.get(3)).y);
  }

  protected void dispatchDraw(Canvas canvas) {
    super.dispatchDraw(canvas);
    canvas.drawLine(this.pointer1.getX() + (float)(this.pointer1.getWidth() / 2), this.pointer1.getY() + (float)(this.pointer1.getHeight() / 2), this.pointer3.getX() + (float)(this.pointer3.getWidth() / 2), this.pointer3.getY() + (float)(this.pointer3.getHeight() / 2), this.paint);
    canvas.drawLine(this.pointer1.getX() + (float)(this.pointer1.getWidth() / 2), this.pointer1.getY() + (float)(this.pointer1.getHeight() / 2), this.pointer2.getX() + (float)(this.pointer2.getWidth() / 2), this.pointer2.getY() + (float)(this.pointer2.getHeight() / 2), this.paint);
    canvas.drawLine(this.pointer2.getX() + (float)(this.pointer2.getWidth() / 2), this.pointer2.getY() + (float)(this.pointer2.getHeight() / 2), this.pointer4.getX() + (float)(this.pointer4.getWidth() / 2), this.pointer4.getY() + (float)(this.pointer4.getHeight() / 2), this.paint);
    canvas.drawLine(this.pointer3.getX() + (float)(this.pointer3.getWidth() / 2), this.pointer3.getY() + (float)(this.pointer3.getHeight() / 2), this.pointer4.getX() + (float)(this.pointer4.getWidth() / 2), this.pointer4.getY() + (float)(this.pointer4.getHeight() / 2), this.paint);
    this.midPointer13.setX(this.pointer3.getX() - (this.pointer3.getX() - this.pointer1.getX()) / 2.0F);
    this.midPointer13.setY(this.pointer3.getY() - (this.pointer3.getY() - this.pointer1.getY()) / 2.0F);
    this.midPointer24.setX(this.pointer4.getX() - (this.pointer4.getX() - this.pointer2.getX()) / 2.0F);
    this.midPointer24.setY(this.pointer4.getY() - (this.pointer4.getY() - this.pointer2.getY()) / 2.0F);
    this.midPointer34.setX(this.pointer4.getX() - (this.pointer4.getX() - this.pointer3.getX()) / 2.0F);
    this.midPointer34.setY(this.pointer4.getY() - (this.pointer4.getY() - this.pointer3.getY()) / 2.0F);
    this.midPointer12.setX(this.pointer2.getX() - (this.pointer2.getX() - this.pointer1.getX()) / 2.0F);
    this.midPointer12.setY(this.pointer2.getY() - (this.pointer2.getY() - this.pointer1.getY()) / 2.0F);
  }

  private ImageView getImageView(int x, int y) {
    ImageView imageView = new ImageView(this.context);
    LayoutParams layoutParams = new LayoutParams(-2, -2);
    imageView.setLayoutParams(layoutParams);
    imageView.setImageResource(drawable.circle);
    imageView.setX((float)x);
    imageView.setY((float)y);
    imageView.setOnTouchListener(new TouchListenerImpl());
    return imageView;
  }

  public boolean onTouchEvent(MotionEvent event) {
    return super.onTouchEvent(event);
  }

  public boolean isValidShape(Map<Integer, PointF> pointFMap) {
    return pointFMap.size() == 4;
  }

  private class TouchListenerImpl implements OnTouchListener {
    PointF DownPT;
    PointF StartPT;

    private TouchListenerImpl() {
      this.DownPT = new PointF();
      this.StartPT = new PointF();
    }

    public boolean onTouch(View v, MotionEvent event) {
      int eid = event.getAction();
      switch(eid) {
        case 0:
          this.DownPT.x = event.getX();
          this.DownPT.y = event.getY();
          this.StartPT = new PointF(v.getX(), v.getY());
          break;
        case 1:
          boolean colorx = false;
          int a_color;
          if (PolygonView.this.isValidShape(PolygonView.this.getPoints())) {
            a_color = PolygonView.this.getResources().getColor(color.blue);
          } else {
            a_color = PolygonView.this.getResources().getColor(color.orange);
          }

          PolygonView.this.paint.setColor(a_color);
          break;
        case 2:
          PointF mv = new PointF(event.getX() - this.DownPT.x, event.getY() - this.DownPT.y);
          if (this.StartPT.x + mv.x + (float)v.getWidth() < (float)PolygonView.this.polygonView.getWidth() && this.StartPT.y + mv.y + (float)v.getHeight() < (float)PolygonView.this.polygonView.getHeight() && this.StartPT.x + mv.x > 0.0F && this.StartPT.y + mv.y > 0.0F) {
            v.setX((float)((int)(this.StartPT.x + mv.x)));
            v.setY((float)((int)(this.StartPT.y + mv.y)));
            this.StartPT = new PointF(v.getX(), v.getY());
          }
      }

      PolygonView.this.polygonView.invalidate();
      return true;
    }
  }

  private class MidPointTouchListenerImpl implements OnTouchListener {
    PointF DownPT = new PointF();
    PointF StartPT = new PointF();
    private ImageView mainPointer1;
    private ImageView mainPointer2;

    public MidPointTouchListenerImpl(ImageView mainPointer1, ImageView mainPointer2) {
      this.mainPointer1 = mainPointer1;
      this.mainPointer2 = mainPointer2;
    }

    public boolean onTouch(View v, MotionEvent event) {
      int eid = event.getAction();
      switch(eid) {
        case 0:
          this.DownPT.x = event.getX();
          this.DownPT.y = event.getY();
          this.StartPT = new PointF(v.getX(), v.getY());
          break;
        case 1:
          boolean colorx = false;
          int b_color;
          if (PolygonView.this.isValidShape(PolygonView.this.getPoints())) {
            b_color = PolygonView.this.getResources().getColor(color.blue);
          } else {
            b_color = PolygonView.this.getResources().getColor(color.orange);
          }

          PolygonView.this.paint.setColor(b_color);
          break;
        case 2:
          PointF mv = new PointF(event.getX() - this.DownPT.x, event.getY() - this.DownPT.y);
          if (Math.abs(this.mainPointer1.getX() - this.mainPointer2.getX()) > Math.abs(this.mainPointer1.getY() - this.mainPointer2.getY())) {
            if (this.mainPointer2.getY() + mv.y + (float)v.getHeight() < (float)PolygonView.this.polygonView.getHeight() && this.mainPointer2.getY() + mv.y > 0.0F) {
              v.setX((float)((int)(this.StartPT.y + mv.y)));
              this.StartPT = new PointF(v.getX(), v.getY());
              this.mainPointer2.setY((float)((int)(this.mainPointer2.getY() + mv.y)));
            }

            if (this.mainPointer1.getY() + mv.y + (float)v.getHeight() < (float)PolygonView.this.polygonView.getHeight() && this.mainPointer1.getY() + mv.y > 0.0F) {
              v.setX((float)((int)(this.StartPT.y + mv.y)));
              this.StartPT = new PointF(v.getX(), v.getY());
              this.mainPointer1.setY((float)((int)(this.mainPointer1.getY() + mv.y)));
            }
          } else {
            if (this.mainPointer2.getX() + mv.x + (float)v.getWidth() < (float)PolygonView.this.polygonView.getWidth() && this.mainPointer2.getX() + mv.x > 0.0F) {
              v.setX((float)((int)(this.StartPT.x + mv.x)));
              this.StartPT = new PointF(v.getX(), v.getY());
              this.mainPointer2.setX((float)((int)(this.mainPointer2.getX() + mv.x)));
            }

            if (this.mainPointer1.getX() + mv.x + (float)v.getWidth() < (float)PolygonView.this.polygonView.getWidth() && this.mainPointer1.getX() + mv.x > 0.0F) {
              v.setX((float)((int)(this.StartPT.x + mv.x)));
              this.StartPT = new PointF(v.getX(), v.getY());
              this.mainPointer1.setX((float)((int)(this.mainPointer1.getX() + mv.x)));
            }
          }
      }

      PolygonView.this.polygonView.invalidate();
      return true;
    }
  }
}
