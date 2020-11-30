package com.example.m0618015;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

public class UIThreadedWrapper extends Handler {
    protected final static int SET_CIRCLE_COORDINATE=0;
    protected final static int CLEAR_CIRCLE_COORDINATE=1;
    protected final static int ADD_SCORE=2;
    protected final static int ADD_PENALTY=3;

    protected MainActivity mainActivity;

    public UIThreadedWrapper(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        if(msg.what == UIThreadedWrapper.SET_CIRCLE_COORDINATE){
            Coordinate param = (Coordinate) msg.obj;
            this.mainActivity.setCircle(param);
        } else if (msg.what == UIThreadedWrapper.CLEAR_CIRCLE_COORDINATE){
            Coordinate param = (Coordinate) msg.obj;
            this.mainActivity.setWhiteCirlce(param);
        } else if(msg.what == UIThreadedWrapper.ADD_SCORE){
            this.mainActivity.addScore();
        } else if(msg.what == UIThreadedWrapper.ADD_PENALTY){
            this.mainActivity.addPenalty();
        }
    }

    public void drawCircle(Coordinate coordinate){
        Message msg = new Message();
        msg.what = SET_CIRCLE_COORDINATE;
        msg.obj = coordinate;
        this.sendMessage(msg);
    }

    public void clearCircle(Coordinate coordinate){
        Message msg = new Message();
        msg.what = CLEAR_CIRCLE_COORDINATE;
        msg.obj = coordinate;
        this.sendMessage(msg);
    }

    public void addScore(){
        Message msg = new Message();
        msg.what = ADD_SCORE;
        this.sendMessage(msg);
    }

    public void addPenalty(){
        Message msg = new Message();
        msg.what = ADD_PENALTY;
        this.sendMessage(msg);
    }
}
