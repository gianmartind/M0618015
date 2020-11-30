package com.example.m0618015;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.m0618015.databinding.ActivityMainBinding;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    ActivityMainBinding bind;
    Bitmap mBitmap;
    Canvas mCanvas;
    Paint paint;
    Paint whitePaint;
    UIThreadedWrapper handler;
    LinkedList<CustomThread> threadList;
    int score;
    int penalty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bind = ActivityMainBinding.inflate(getLayoutInflater());
        View view = this.bind.getRoot();
        setContentView(view);

        this.paint = new Paint();
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setColor(ResourcesCompat.getColor(getResources(), R.color.black, null));

        this.whitePaint = new Paint();
        //this.whitePaint.setStyle(Paint.Style.FILL);
        this.whitePaint.setColor(Color.TRANSPARENT);
        this.whitePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        //this.whitePaint.setAntiAlias(true);


        this.bind.ivCanvas.setOnTouchListener(this);
        this.bind.btnStart.setOnClickListener(this);
        this.bind.btnStop.setOnClickListener(this);

        this.score = 0;
        this.penalty = 0;
        this.threadList = new LinkedList<>();
        this.handler = new UIThreadedWrapper(this);
    }

    @Override
    public void onClick(View v) {
        if(v == this.bind.btnStart){
            this.initiateCanvas();
            Random random = new Random();
            float randX = random.nextInt(20) - 10;
            float randY = random.nextInt(20) - 10;
            Coordinate coordinateMax = new Coordinate(this.bind.ivCanvas.getWidth(), this.bind.ivCanvas.getHeight());
            Coordinate coordinateStart = new Coordinate(this.bind.ivCanvas.getWidth()/2, this.bind.ivCanvas.getHeight()/2);
            Coordinate coordinateDir = new Coordinate(randX, randY);
            this.threadList.addFirst(new CustomThread(this.handler, coordinateDir, coordinateMax, coordinateStart));
            this.threadList.getFirst().start();
        } else if(v == this.bind.btnStop){
            while(!this.threadList.isEmpty()){
                this.threadList.removeFirst().stopThread();
            }
        }
    }

    public void initiateCanvas(){
        this.mBitmap = Bitmap.createBitmap(this.bind.ivCanvas.getWidth(), this.bind.ivCanvas.getHeight(), Bitmap.Config.ARGB_8888);
        this.bind.ivCanvas.setImageBitmap(this.mBitmap);
        this.mCanvas = new Canvas(this.mBitmap);
    }

    public void setCircle(Coordinate coordinate){
        //this.mCanvas.drawColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
        this.mCanvas.drawCircle(coordinate.getX(), coordinate.getY(), 100, this.paint);
        this.bind.ivCanvas.invalidate();
    }

    public void setWhiteCirlce(Coordinate coordinate){
        this.mCanvas.drawCircle(coordinate.getX(), coordinate.getY(), 100, this.whitePaint);
        this.bind.ivCanvas.invalidate();
    }

    public void addScore(){
        this.score += 1;
        this.bind.scoreText.setText(Integer.toString(this.score));
    }

    public void addPenalty(){
        this.penalty += 1;
        this.bind.penaltyText.setText(Integer.toString(this.penalty));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Coordinate tap = new Coordinate(event.getX(), event.getY());
        for(int i = 0; i < this.threadList.size(); i++){
            this.threadList.get(i).checkTap(tap);
        }
        return true;
    }
}