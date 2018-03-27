package com.example.calum.childkeyboard;

import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.Spanned;
import android.graphics.Typeface;
import android.graphics.Rect;
import android.widget.ArrayAdapter;

import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Keyboard extends AppCompatActivity{

    private Ngram ngram;
    private Dictionary dictionary;
    private KeyboardModel keyboard;
    private EditText tv;
    private Sentence sentence;
    private Spinner spinner;
    private String selectedWord;
    private Button button;
    private Button gameButton;
    private boolean gameMode;
    private TextView gameWord;
    private Game game;
    private TextView score;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);
        final RelativeLayout rl = findViewById(R.id.rel);
        game = new Game();
        gameMode = false;
        button = findViewById(R.id.button);
        button.setOnClickListener(new ButtonTouch());
        gameButton = findViewById(R.id.Game);
        gameButton.setOnClickListener(new GameButton());
        keyboard = new KeyboardModel();
        dictionary = new Dictionary(getAssets());
        ngram = new Ngram(getAssets());
        sentence = new Sentence();
        tv = (EditText) findViewById(R.id.textView);
        gameWord = findViewById(R.id.textView2);
        score = findViewById(R.id.score);
        spinner = (Spinner) findViewById(R.id.spinner);
        //tv.setInputType(InputType.TYPE_NULL);
        selectedWord="";
        tv.setActivated(true);
        tv.setPressed(true);
        tv.setTextIsSelectable(true);
        tv.setSelection(0);
        tv.setOnTouchListener(new EditTouch(this));
        tv.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/bookman.ttf");
        tv.setTypeface(type);
        gameWord.setTypeface(type);
        score.setTypeface(type);
        rl.setOnTouchListener(new Touch());
        rl.addView(new MyView(this));
        fileName = "LogFile_" + System.currentTimeMillis();
    }

    public void writeLog(String type,String text){

        Date d = new Date();
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss");

        String line = df.format(d) + "\t" + type + "\t" + text + "\n";

        File f = new File(this.getFilesDir(),"log");
        if(!f.exists()){
            f.mkdir();
        }
        try{
            File f2 = new File(f, fileName);
            FileWriter fw = new FileWriter(f2,true);
            fw.append(line);
            fw.flush();
            fw.close();
        } catch (IOException e) {

        }

    }

    public class ButtonTouch implements View.OnClickListener {

        public void onClick(View v) {

            String newSent = tv.getText().toString();
            tv.setText(newSent.replace(selectedWord,spinner.getSelectedItem().toString()));
            game.addWord(spinner.getSelectedItem().toString());
            spinner.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
            writeLog("CORR",selectedWord+" to " + spinner.getSelectedItem().toString());
        }

    }

        public class EditTouch implements View.OnTouchListener {

        Context context;

        public EditTouch(Context c){
            context = c;
        }

        public boolean onTouch(View v, MotionEvent me) {

            v.onTouchEvent(me);
            InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }

            int startSelection = tv.getSelectionStart();
            tv.setSelection(startSelection);

            int length = 0;

            for(String currentWord : tv.getText().toString().split("\\s+")) {
                length = length + currentWord.length() + 1;
                if(length > startSelection) {
                    selectedWord = currentWord.replace(".","");
                    break;
                }
            }

            for (Word word : sentence.getSentence()){
                if(word.getWord().equals(selectedWord)){
                    List<String> poss = word.getPossible();
                    poss.add("");
                    System.out.println(poss);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            context, android.R.layout.simple_spinner_item, poss);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                }
            }
            spinner.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);
            System.out.println(selectedWord);
            return true;
        }

    }

    public class GameButton implements View.OnClickListener {

        public void onClick(View v) {

            if (gameMode) {
                writeLog("MODE","Change to keyboard.");
                tv.setVisibility(View.VISIBLE);
                gameWord.setVisibility(View.GONE);
                score.setVisibility(View.GONE);
                tv.setText("");
                gameButton.setText("Game");
                sentence.clearSentence();
                gameMode = false;
            }
            else{
                writeLog("MODE","Change to game.");
                tv.setText("");
                spinner.setVisibility(View.GONE);
                button.setVisibility(View.GONE);
                gameWord.setVisibility(View.VISIBLE);
                score.setVisibility(View.VISIBLE);
                score.setText(""+game.getScore());
                gameButton.setText("Keyboard");
                game.resetCounter();
                game.newWord();
                gameWord.setText(game.getWord());
                gameMode = true;
            }


        }

    }

    public class Touch implements View.OnTouchListener {

        public boolean onTouch(View v, MotionEvent me) {
            if (me.getAction() == android.view.MotionEvent.ACTION_UP) {
                int x = (int) me.getX();
                int y = (int) me.getY();
                String key = keyboard.getKey(x, y);
                writeLog("PRES", "Key: " + key + " x-co: " + x + " y-co " + y);
                if(!gameMode) {
                    String orig = tv.getText().toString().replace(".", "");
                    String sent = keyboard.newSent(tv.getText().toString(), key);
                    SpannableString ss = new SpannableString(sent);
                    Sentence newSentence = new Sentence();
                    if (key.equals(" ") | key.equals(". ")) {
                        String gram = ngram.checkSentence(orig);
                        String[] splitGram = gram.split("\\s+");
                        String[] splitSent = orig.split("\\s+");

                        for (int i = 0; i < splitSent.length; i++) {
                            if (!splitSent[i].equals(splitGram[i])) {
                                ss = highlightString(splitSent[i] + key, ss, Color.CYAN);
                                newSentence.addGram(splitSent[i], splitGram[i]);
                            }
                        }
                        for (String word : splitSent) {
                            List<String> s = dictionary.checkWord(word);
                            if (!dictionary.getDict().contains(word.toLowerCase())) {
                                ss = highlightString(word + key, ss, Color.RED);
                                newSentence.addWord(word, s);
                            }
                        }
                    }
                    sentence = newSentence;
                    tv.setText(ss);
                    tv.setSelection(ss.length());
                    writeLog("SENT", ss.toString());
                }
                else{
                    String input = keyboard.newSent(tv.getText().toString(), key);
                    writeLog("GAME","Current input: " + input);
                    if (input.equals(game.getWord())){
                        tv.setText("");
                        writeLog("GAME", "Counter: " + game.getCount());
                        game.incrementCount();
                        if (game.getCount()==2){
                            gameWord.setText("");
                            writeLog("GAME","Word is hidden.");
                        }
                        else if(game.getCount()==0){
                            game.incrementScore(game.getWord());
                            writeLog("GAME","Current score: " + game.getScore());
                            game.newWord();
                            writeLog("GAME","CurrentWord: " +game.getWord());
                            String s = ""+game.getScore();
                            score.setText(s);
                            gameWord.setText(game.getWord());
                        }
                        else{
                            gameWord.setText(game.getWord());
                        }
                    }
                    else{
                        if(game.getCount()==2 && input.length() == game.getWord().length()){
                            game.decrementScore(game.getWord());
                            writeLog("GAME","Current Score: " + game.getScore());
                            game.incrementCount();
                            writeLog("GAME","Current Count: " + game.getCount());
                            game.newWord();
                            writeLog("GAME","New word: " +game.getWord());
                            gameWord.setText(game.getWord());
                            tv.setText("");
                            String s = ""+game.getScore();
                            score.setText(s);
                        }
                        else {
                            tv.setText(input);
                        }
                    }
                }
            }

            return true;
        }
    }

    private SpannableString highlightString(String input, SpannableString tv, int color) {

        SpannableString spannableString = tv;

        int indexOfKeyword = spannableString.toString().indexOf(input);

        while (indexOfKeyword >= 0) {

            spannableString.setSpan(new BackgroundColorSpan(color), indexOfKeyword, indexOfKeyword + input.length() -1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            indexOfKeyword = spannableString.toString().indexOf(input, indexOfKeyword + input.length());
        }

        return spannableString;
    }

    public class MyView extends View
    {
        Paint paint = null;

        public MyView(Context context)
        {
            super(context);
            paint = new Paint();
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
                Rect bounds = new Rect();
                paint.getTextBounds(keyboard.getFirstRowElem(i-1), 0, keyboard.getFirstRowElem(i-1).length(), bounds);
                int height = bounds.height();
                canvas.drawText(keyboard.getFirstRowElem(i-1),xco - (paint.measureText(keyboard.getFirstRowElem(i-1))/2), heightApart - (height/4) ,paint);
                keyboard.createKey(xco,yco,keyboard.getFirstRowElem(i-1));
                writeLog("KEYS","key: " + keyboard.getFirstRowElem(i-1) + " x-co: " + xco + " y-co: " + yco);
                paint.setColor(Color.parseColor("#4CB5F5"));
            }
            for(int i = 1; i < 10; i++){
                int xco = widthApart*i - (widthApart/10);
                int yco = heightApart*2 - radius + (heightApart/10);
                canvas.drawCircle(xco, yco, radius, paint);
                paint.setColor(Color.WHITE);
                Rect bounds = new Rect();
                paint.getTextBounds(keyboard.getFirstRowElem(i-1), 0, keyboard.getFirstRowElem(i-1).length(), bounds);
                int height = bounds.height();
                canvas.drawText(keyboard.getSecondRowElem(i-1),xco - (paint.measureText(keyboard.getSecondRowElem(i-1))/2),heightApart*2  - (height/4),paint);
                keyboard.createKey(xco,yco,keyboard.getSecondRowElem(i-1));
                writeLog("KEYS","key: " + keyboard.getSecondRowElem(i-1) + " x-co: " + xco + " y-co: " + yco);
                paint.setColor(Color.parseColor("#4CB5F5"));
            }
            for(int i = 1; i < 10; i++) {
                int xco = widthApart * i - (widthApart / 10);
                int yco = heightApart * 3 - radius + (heightApart / 10);
                canvas.drawCircle(xco, yco, radius, paint);
                paint.setColor(Color.WHITE);
                Rect bounds = new Rect();
                paint.getTextBounds(keyboard.getFirstRowElem(i-1), 0, keyboard.getFirstRowElem(i-1).length(), bounds);
                int height = bounds.height();
                canvas.drawText(keyboard.getThirdRowElem(i - 1), xco - (paint.measureText(keyboard.getThirdRowElem(i-1))/2), heightApart * 3 - (height / 4), paint);
                keyboard.createKey(xco, yco, keyboard.getThirdRowElem(i - 1));
                writeLog("KEYS","key: " + keyboard.getThirdRowElem(i-1) + " x-co: " + xco + " y-co: " + yco);
                paint.setColor(Color.parseColor("#4CB5F5"));
            }
            canvas.drawRoundRect(new RectF(widthApart*2, (heightApart*4) - radius, widthApart*8, (heightApart*4)+ radius ), 10,10, paint);
            paint.setColor(Color.WHITE);
            canvas.drawText("SPACE", widthApart*4, (heightApart*4) + radius/2, paint);
            for (int i = 2; i < 9; i++){
                keyboard.createKey(widthApart*i, heightApart *4, " ");
                writeLog("KEYS","key: space bar " + " x-co: " + widthApart*i + " y-co: " + heightApart *4);
            }
        }
    }
}
