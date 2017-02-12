package com.example.atack08.ejemplo_juego;

import android.graphics.Canvas;
import android.provider.Settings;

/**
 * Created by atack08 on 11/2/17.
 */

public class GameLoopThread extends Thread {

    private VistaJuego view;
    private boolean running;
    static final long FPS = 10;

    public GameLoopThread(VistaJuego view){

        this.view = view;
        this.running = false;

    }

    public void setRunnig(Boolean run){
        this.running = run;
    }

    @Override
    public void run() {

        long ticksPS = 1000 / FPS;
        long startTime = System.currentTimeMillis();
        long sleepTime;

        while (running){

            Canvas c = null;
            try {
                c = view.getHolder().lockCanvas();
                synchronized (view.getHandler()) {
                    view.onDraw(c);
                }
            }
            finally {
                if (c != null){
                    view.getHolder().unlockCanvasAndPost(c);
                }
            }

            /*try {
                sleepTime = ticksPS - (System.currentTimeMillis() - startTime);

                if (sleepTime > 0)
                    sleep(sleepTime);
                else
                    sleep(10);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

        }

    }
}
