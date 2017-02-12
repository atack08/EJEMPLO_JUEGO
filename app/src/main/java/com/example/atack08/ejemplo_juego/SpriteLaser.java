package com.example.atack08.ejemplo_juego;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by atack08 on 12/2/17.
 */

public class SpriteLaser {

    private int x,y,ySpeed;
    private VistaJuego vistaJuego;
    private Bitmap bmp;
    private boolean mostrar;



    public SpriteLaser(VistaJuego vistJ, Bitmap bmp, int x, int y){


        mostrar = true;
        this.vistaJuego = vistJ;
        this.bmp = bmp;
        ySpeed = -5;
        this.x = x;
        this.y = y;

    }


    private void update(){

        //LÍMITE ARRIBA
        if(y + ySpeed < 0){
            ySpeed = 0;
            mostrar = false;
        }

        y = y + ySpeed;
    }

    public void onDraw (Canvas canvas){

        if(mostrar){
            update();
            canvas.drawBitmap(bmp,x,y,null);
        }

    }

    public boolean isMostrar() {
        return mostrar;
    }

    public void setMostrar(boolean mostrar) {
        this.mostrar = mostrar;
    }
}
