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
import android.widget.TextView;

public class Keyboard extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);
        final Button b1 = findViewById(R.id.Q);
        b1.setOnClickListener(new ButtonListener());
        final Button b2 = findViewById(R.id.W);
        b2.setOnClickListener(new ButtonListener());

        /*
        final Button b1 = findViewById(R.id.button);
        b1.setOnTouchListener(new Touch());
        final ImageView iv = findViewById(R.id.tick);
        iv.setOnClickListener(new ButtonListener2());*/

    }

    /*
    public class Touch implements View.OnTouchListener {
        public boolean onTouch(View v, MotionEvent me) {
            int x = (int)me.getX();
            int y = (int)me.getY();
            TextView tv = (TextView) findViewById(R.id.textView);
            tv.setText(tv.getText().toString() + x + "      " + y);
            return true;
        }
    }*/

    public class ButtonListener implements View.OnClickListener{

        public void onClick(View v){
            TextView tv = (TextView) findViewById(R.id.textView);
            Button b = (Button) v;
            tv.setText(tv.getText().toString() + " " + b.getText().toString());
        }
    }
    /*
    public class ButtonListener2 implements View.OnClickListener{

        public void onClick(View v){
            TextView tv = (TextView) findViewById(R.id.textView);
            ImageView b = (ImageView) v;

        }
    }*/
}
