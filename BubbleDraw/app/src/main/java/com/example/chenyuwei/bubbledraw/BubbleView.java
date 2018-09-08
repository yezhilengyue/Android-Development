package com.example.chenyuwei.bubbledraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.view.View;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Random;

public class BubbleView extends ImageView implements View.OnTouchListener {

    private Random rand = new Random();
    private ArrayList<Bubble> bubbleList;
    private int size = 50;
    private int delay = 33;  // To get the animation speed, divide 1000 milliseconds by the number of frames per second, 30 fps

    private Paint myPaint = new Paint(); //paintbrush
    private Handler h = new Handler();

    public BubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bubbleList = new ArrayList<Bubble>();
        //testBubbles();
        setOnTouchListener(this);
    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            for (Bubble b : bubbleList)
                b.update();  // to update the location of each bubble for the next frame of animation
            invalidate();
        }
    };

    protected void onDraw(Canvas canvas) {
        for (Bubble b : bubbleList)
            b.draw(canvas);
        h.postDelayed(r, delay); // connect the handler h to R
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getPointerCount() > 1) {
            //first we need to determine the location of the user’s touch.
            for (int n = 1; n <= motionEvent.getPointerCount(); n++) {
                int x = (int) motionEvent.getX(n);
                int y = (int) motionEvent.getY(n);
                int s = size + rand.nextInt(size)/n;
                // Then we’ll add a bubble at that location.
                bubbleList.add(new Bubble(x, y, s));
            }
        }
        else{
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            int s = size + rand.nextInt(size);
            int speedx = 5;
            int speedy = 6;
            bubbleList.add(new Bubble(x,y,s,speedx,speedy));
        }
        return true;
    }

    public void testBubbles() {
        for (int n = 0; n < 100; n++) {
            int x = rand.nextInt(600);
            int y = rand.nextInt(400);
            int s = rand.nextInt(size) + size;
            bubbleList.add(new Bubble(x, y, s));
        }
        invalidate(); // same effect of repaint() in desktop version
    }

    private class Bubble {
        private int x;
        private int y;
        private int size;
        private int color;
        private int xspeed, yspeed;
        private final int MAX_SPEED = 15;

        public Bubble(int newX, int newY, int newSize) {
            x = newX;
            y = newY;
            size = newSize;
            color = Color.argb(rand.nextInt(256),
                    rand.nextInt(256),
                    rand.nextInt(256),
                    rand.nextInt(256));
            xspeed = rand.nextInt(MAX_SPEED * 2) - MAX_SPEED;
            yspeed = rand.nextInt(MAX_SPEED * 2) - MAX_SPEED;
        }

        public Bubble(int newX, int newY, int newSize, int xSpeed, int ySpeed) {
            x = newX;
            y = newY;
            size = newSize;
            color = Color.argb(rand.nextInt(256),
                    rand.nextInt(256),
                    rand.nextInt(256),
                    rand.nextInt(256));
            xspeed = xSpeed;
            yspeed = ySpeed;
        }

        public void draw(Canvas canvas) {
            myPaint.setColor(color);
            canvas.drawOval(x - size / 2, y - size / 2, x + size / 2, y + size / 2, myPaint);
        }

        public void update() {
            x += xspeed;
            y += yspeed;

            if (x - size / 2 <= 0 || x + size / 2 >= getWidth())
                xspeed = -xspeed;
            if (y - size / 2 <= 0 || y + size / 2 >= getWidth())
                yspeed = -yspeed;
        }
    }
}
