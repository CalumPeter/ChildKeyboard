package com.example.calum.childkeyboard;

import android.graphics.RectF;
import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
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
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.Spanned;

import java.util.List;

public class Keyboard extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);
        final RelativeLayout rl = findViewById(R.id.rel);
        KeyboardModel k = new KeyboardModel();
        Dictionary d = new Dictionary(getAssets());
        rl.setOnTouchListener(new Touch(k,d));
        rl.addView(new MyView(this, k));
    }


    public class Touch implements View.OnTouchListener {

        KeyboardModel k = null;
        Dictionary d = null;

        public Touch(KeyboardModel km, Dictionary dict) {
            k = km;
            d = dict;
        }

        public boolean onTouch(View v, MotionEvent me) {
            if (me.getAction() == android.view.MotionEvent.ACTION_UP) {
                int x = (int) me.getX();
                int y = (int) me.getY();
                TextView tv = (TextView) findViewById(R.id.textView);
                String key = k.getKey(x, y);
                String sent = tv.getText().toString() + key;
                SpannableString ss = new SpannableString(sent);
                /* k.ltools(sent); */
                if (key.equals(" ")) {
                    for (String word : sent.split("\\s+")) {
                        List<String> s = d.checkWord(word);
                        if (!d.getDict().contains(word.toLowerCase())) {
                            Log.d("LEVEN-T", "HERE");
                            ss = highlightString(word + " ", ss);
                        }
                        for (String found : s) {
                            Log.d("LEVEN3", found);
                        }
                    }
                }
                tv.setText(ss);
            }
            return true;
        }
    }

    private SpannableString highlightString(String input, SpannableString tv) {
        //Get the text from text view and create a spannable string
        SpannableString spannableString = tv;
        //Get the previous spans and remove them

        //BackgroundColorSpan[] backgroundSpans = spannableString.getSpans(0, spannableString.length(), BackgroundColorSpan.class);

        /*for (BackgroundColorSpan span : backgroundSpans) {
            spannableString.removeSpan(span);
        }*/

        //Search for all occurrences of the keyword in the string
        int indexOfKeyword = spannableString.toString().indexOf(input);

        while (indexOfKeyword >= 0) {
            //Create a background color spn on the keyword
            spannableString.setSpan(new BackgroundColorSpan(Color.RED), indexOfKeyword, indexOfKeyword + input.length() -1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            //Get the next index of the keyword
            indexOfKeyword = spannableString.toString().indexOf(input, indexOfKeyword + input.length());
        }

        return spannableString;
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
            canvas.drawRoundRect(new RectF(widthApart*2, (heightApart*4) - radius, widthApart*8, (heightApart*4)+ radius ), 10,10, paint);
            paint.setColor(Color.WHITE);
            canvas.drawText("SPACE", widthApart*4, (heightApart*4) + radius/2, paint);
            k.createKey(widthApart*5, heightApart *4, " ");
            k.createKey(widthApart*2, heightApart *4, " ");
            k.createKey(widthApart*8, heightApart *4, " ");
        }
    }
}
