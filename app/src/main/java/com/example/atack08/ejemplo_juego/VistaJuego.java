package com.example.atack08.ejemplo_juego;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.nfc.Tag;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by atack08 on 11/2/17.
 */

public class VistaJuego extends SurfaceView{

    //LA IMAGEN
    private Bitmap bmp,botonIzqda,botonDerecha,botonFuego;
    private Sprite sprite;
    private Bitmap fondo, fondoEscalado;
    private GameLoopThread gameLoopThread;
    private SurfaceHolder holder;

    //Coordenada x
    //Coordenada y
    private int x;
    private int y;
    private int x2;
    private int y2;

    //Speed EjeX
    //Speed EjeY
    private int xSpeed;
    private int ySpeed;
    private int xSpeed2;
    private int ySpeed2;

    //MEDIDAS DEL SURFACEVIEW
    private  int anchoVista, altoVista;


    public VistaJuego(Context context) {
        super(context);


        //INICIALOZAMOS EL HILO QUE IRÁ ACTUALIZANDO
        gameLoopThread = new GameLoopThread(this);

        x = 0;
        y = 0;
        xSpeed = 12;
        ySpeed = 12;



        x2 = 240;
        y2 = 240;
        xSpeed2 = 12;
        ySpeed2= 12;



        //FONDO
        fondo = BitmapFactory.decodeResource(getResources(),R.raw.stars);

        //CARGAMOS EL SPRITE
        bmp = BitmapFactory.decodeResource(getResources(),R.raw.ship);

        sprite = new Sprite(this,bmp);

        holder = getHolder();

        //EVENTOS DE CANVAS
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {


                gameLoopThread.setRunnig(true);
                gameLoopThread.start();


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


                //ESCALAMOS EL FONDO CON LAS MEDIDAS DE LA PANTALLA
                fondo = Bitmap.createScaledBitmap(fondo,width,height,true);



            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

                boolean retry = true;
                gameLoopThread.setRunnig(false);
                System.out.println("ENTRA AL SURFACE DESTROYED");

                while(retry){

                    try {
                        gameLoopThread.join();
                        retry = false;

                    } catch (InterruptedException e) {

                    }
                }
                
            }
        });

        //CARGAMOS BOTONES
        botonFuego = BitmapFactory.decodeResource(getResources(),R.raw.boton_fuego);
        botonDerecha = BitmapFactory.decodeResource(getResources(),R.raw.boton_derecha);
        botonIzqda = BitmapFactory.decodeResource(getResources(),R.raw.boton_izqda);



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        //FONDO IMGAEN SPACIO
        canvas.drawBitmap(fondo,0,0,null);
        //PINTAMOS BOTONES
        canvas.drawBitmap(botonFuego,this.getWidth() - botonFuego.getWidth(),this.getHeight() - botonFuego.getHeight(),null);
        canvas.drawBitmap(botonIzqda,10,canvas.getHeight() - botonIzqda.getHeight(),null);
        canvas.drawBitmap(botonDerecha,10 + botonDerecha.getWidth(),canvas.getHeight() - botonDerecha.getHeight() ,null);


        sprite.onDraw(canvas);

    }

    //EVENTOS PANTALLA


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x2 =  event.getX();
        float y2 =  event.getY();

        if (x2 >= 10 + botonDerecha.getWidth() && x2 <= 10 + botonDerecha.getWidth()*2
                    && y2 >= altoVista - botonDerecha.getHeight() ){
            System.out.println("HAS TOCADO EN BOTON DERECHA");

            sprite.moverDerecha();
        }

        if (x2 >= 10 && x2 <= 10 + botonIzqda.getWidth()
                && y2 >= altoVista - botonIzqda.getHeight() ){
            System.out.println("HAS TOCADO EN BOTON IZQDA");

            sprite.moverIzqda();
        }

        return super.onTouchEvent(event);
    }

    public int getAltoVista() {
        return altoVista;
    }

    public int getAnchoVista() {
        return anchoVista;
    }
}
