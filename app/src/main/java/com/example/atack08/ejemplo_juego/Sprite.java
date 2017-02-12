package com.example.atack08.ejemplo_juego;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by atack08 on 12/2/17.
 */

public class Sprite {


    private int x,y,xSpeed,height,width;
    private VistaJuego vistaJuego;
    private Bitmap bmp;


    public Sprite(VistaJuego vistJ, Bitmap bmp){

        xSpeed = 0;
        this.vistaJuego = vistJ;
        this.bmp = bmp;

    }

    public void moverDerecha(){

        if(xSpeed < 0)
            xSpeed = 0;
        else
            xSpeed = 5;
    }

    public void moverIzqda(){

        if(xSpeed > 0)
            xSpeed = 0;
        else
            xSpeed = -5;
    }

    private void update(){

        //LÍMITE DERECHO
        if (x > vistaJuego.getWidth() - bmp.getWidth() - xSpeed){
            xSpeed = 0;
        }

        //LÍMITE IZQDO
        if(x + xSpeed < 0){
            xSpeed = 0;
        }

        x = x + xSpeed;
        vistaJuego.setAnchoSpriteJugador(x);
    }

    public void onDraw (Canvas canvas){
        update();
        canvas.drawBitmap(bmp,x,y,null);
    }

    public void fijarCoordenadasIniciales(int w, int h){

        setHeight(h);
        setWidth(w);

        x = (w/2) - (bmp.getWidth()/2);
        y =  h - 450;

        vistaJuego.setAltoSpriteJugador(y);

        System.out.println("ANCHO INICIAL = " + x);
        System.out.println("ALTO INICIAL = " + y);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
