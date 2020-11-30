package com.example.m0618015;


import android.util.Log;

import java.util.Random;

public class CustomThread extends Thread {
    protected UIThreadedWrapper uiThreadedWrapper;
    protected Coordinate coordinateDir;
    protected Coordinate coordinateMax;
    protected Coordinate coordinateStart;
    protected boolean stopped = false;
    protected boolean isClicked = false;


    public CustomThread(UIThreadedWrapper uiThreadedWrapper, Coordinate coordinateDir, Coordinate coordinateMax, Coordinate coordinateMin){
        this.uiThreadedWrapper = uiThreadedWrapper;
        this.coordinateDir = coordinateDir;
        this.coordinateMax = coordinateMax;
        this.coordinateStart = coordinateMin;
    }

    public void stopThread(){
        Log.d("TAG", "stopThread: ");
        this.stopped = true;
    }

    public void run() {
        this.stopped = false;
        while(checkValid(this.coordinateStart.getX(), this.coordinateStart.getY()) && !this.stopped){
            try {
                Thread.sleep(100);
                if(!this.isClicked){
                    uiThreadedWrapper.clearCircle(new Coordinate(this.coordinateStart.getX(), this.coordinateStart.getY()));
                }
                this.coordinateStart.setX(this.coordinateStart.getX() + this.coordinateDir.getX());
                this.coordinateStart.setY(this.coordinateStart.getY() + this.coordinateDir.getY());
                if(!this.isClicked){
                    uiThreadedWrapper.drawCircle(new Coordinate(this.coordinateStart.getX(), this.coordinateStart.getY()));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(!this.isClicked){
            uiThreadedWrapper.addPenalty();
        }
        return;
    }

    public void checkTap(Coordinate tap){
        if(!isClicked){
            if(tap.getX() >= this.coordinateStart.getX() - 100 && tap.getX() <= this.coordinateStart.getX() + 100){
                if(tap.getY() >= this.coordinateStart.getY() - 100 && tap.getY() <= this.coordinateStart.getY() + 100){
                    this.uiThreadedWrapper.addScore();
                    this.isClicked = true;
                    //this.stopThread();
                    uiThreadedWrapper.clearCircle(new Coordinate(this.coordinateStart.getX(), this.coordinateStart.getY()));
                }
            }
        }
    }

    public boolean checkValid(float x, float y){
        if(x < 0 - 105 || y < 0 - 105){
            //this.coordinateDir.setX(random.nextInt(20) - 10);
            //this.coordinateDir.setY(random.nextInt(20) - 10);
            return false;
        }

        if(x > this.coordinateMax.getX() + 105 || y > this.coordinateMax.getY() + 105){
            //this.coordinateDir.setX(random.nextInt(20) - 10);
            //this.coordinateDir.setY(random.nextInt(20) - 10);
            return false;
        }

        return true;
    }
}
