package com.example.calum.childkeyboard;

import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

public class Keyboard extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);
        final RelativeLayout rl = findViewById(R.id.rel);
        KeyboardModel k = new KeyboardModel();
        rl.setOnTouchListener(new Touch(k));
        rl.addView(new MyView(this, k));
    }


    public class Touch implements View.OnTouchListener {

        KeyboardModel k = null;

        public Touch(KeyboardModel km){
            k = km;
        }

        public boolean onTouch(View v, MotionEvent me) {
            if (me.getAction() == android.view.MotionEvent.ACTION_UP) {
                int x = (int) me.getX();
                int y = (int) me.getY();
                TextView tv = (TextView) findViewById(R.id.textView);
                String sent = tv.getText().toString() + "  " + k.getKey(x, y);
                tv.setText(sent);
            }
            return true;
        }
    }

    public class MyView extends View
    {
        Paint paint = null;
        KeyboardModel k = null;

        public MyView(Context context, KeyboardModel keyboard)
        {
            super(context);
            paint = new Paint();
            k = keyboard;
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);
            int x = getWidth();
            int y = getHeight();
            int radius;
            radius = getWidth()/24;
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.parseColor("#E7E7E4"));
            canvas.drawPaint(paint);
            paint.setColor(Color.parseColor("#4CB5F5"));
            double textSize = (radius * 1.70);
            paint.setTextSize((float)textSize);

            int widthApart = x/10;
            int heightApart = y /5;

            for (int i = 1; i < 11; i++){
                int xco = widthApart*i - radius - (widthApart/10);
                int yco = heightApart - radius + (heightApart/10);
                canvas.drawCircle(xco, yco, radius, paint);
                paint.setColor(Color.WHITE);
                canvas.drawText(k.getFirstRowElem(i-1),xco - (paint.measureText(k.getFirstRowElem(i-1))/2),heightApart  - (radius/8),paint);
                k.createKey(xco,yco,k.getFirstRowElem(i-1));
                paint.setColor(Color.parseColor("#4CB5F5"));
            }
            for(int i = 1; i < 10; i++){
                int xco = widthApart*i - (widthApart/10);
                int yco = heightApart*2 - radius + (heightApart/10);
                canvas.drawCircle(xco, yco, radius, paint);
                paint.setColor(Color.WHITE);
                canvas.drawText(k.getSecondRowElem(i-1),xco - (paint.measureText(k.getSecondRowElem(i-1))/2),heightApart*2  - (radius/8),paint);
                k.createKey(xco,yco,k.getSecondRowElem(i-1));
                paint.setColor(Color.parseColor("#4CB5F5"));
            }
            for(int i = 2; i < 9; i++) {
                int xco = widthApart * i - (widthApart / 10);
                int yco = heightApart * 3 - radius + (heightApart / 10);
                canvas.drawCircle(xco, yco, radius, paint);
                paint.setColor(Color.WHITE);
                canvas.drawText(k.getThirdRowElem(i - 2), xco - (paint.measureText(k.getThirdRowElem(i-2))/2), heightApart * 3 - (radius / 8), paint);
                k.createKey(xco, yco, k.getThirdRowElem(i - 2));
                paint.setColor(Color.parseColor("#4CB5F5"));
            }
        }
    }
}
