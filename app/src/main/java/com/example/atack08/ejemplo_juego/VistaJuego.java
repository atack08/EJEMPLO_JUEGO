package com.example.atack08.ejemplo_juego;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * Created by atack08 on 11/2/17.
 */

public class VistaJuego extends SurfaceView{

    private  int altoSpriteJugador = 0;
    private  int anchoSpriteJugador = 0;

    //LA IMAGEN
    private Bitmap bmp,botonIzqda,botonDerecha,botonFuego, laser;
    private Sprite sprite;
    private Bitmap fondo;
    private GameLoopThread gameLoopThread;
    private SurfaceHolder holder;
    private Context context;


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

    //BOOLEAN PARA EL FONDO
    private int imgF;

    //LISTA SPRITES TEMPORALES
    private ArrayList<SpriteLaser> listaST;


    public VistaJuego(Context context) {
        super(context);

        this.context = context;
        listaST = new ArrayList<>();

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
        laser = BitmapFactory.decodeResource(getResources(), R.raw.laser_img);

        sprite = new Sprite(this,bmp);

        holder = getHolder();

        //EVENTOS DE CANVAS
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                //ESCALAMOS LOS FONDOS
                fondo = Bitmap.createScaledBitmap(fondo,getWidth(),getHeight(),true);

                //COLOCAMOS EL SPRITE
                sprite.fijarCoordenadasIniciales(getWidth(),getHeight());


                //INICIAMOS EL LOOP
                gameLoopThread.setRunnig(true);
                gameLoopThread.start();


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {



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

        ArrayList<Integer> listaBorrado = new ArrayList<>();

        if(!listaST.isEmpty()) {
            for (SpriteLaser spl : listaST) {

                if (spl.isMostrar())
                    spl.onDraw(canvas);
                else
                    listaBorrado.add(listaST.indexOf(spl));
            }
        }

        sprite.onDraw(canvas);

    }

    //EVENTOS PANTALLA


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x2 =  event.getX();
        float y2 =  event.getY();

        if (x2 >= 10 + botonDerecha.getWidth() && x2 <= 10 + botonDerecha.getWidth()*2
                    && y2 >= getHeight() - botonDerecha.getHeight() ){
            System.out.println("HAS TOCADO EN BOTON DERECHA");

            sprite.moverDerecha();
        }

        if (x2 >= 10 && x2 <= 10 + botonIzqda.getWidth()
                && y2 >= getHeight() - botonIzqda.getHeight() ){
            System.out.println("HAS TOCADO EN BOTON IZQDA");

            sprite.moverIzqda();
        }

        if (x2 >= this.getWidth() - botonFuego.getWidth() && x2 <= this.getWidth()
                && y2 >= getHeight() - botonFuego.getHeight() ){
            System.out.println("HAS TOCADO EN BOTON FUEGO");

            listaST.add(new SpriteLaser(this,laser,anchoSpriteJugador + (bmp.getWidth()/2),altoSpriteJugador));

            SonidoDisparo sd = new SonidoDisparo();
            sd.execute();

        }



        return super.onTouchEvent(event);
    }

    public class SonidoDisparo extends AsyncTask {

        private MediaPlayer media;

        @Override
        protected Object doInBackground(Object[] params) {

            media =  MediaPlayer.create(context, R.raw.laser_sound);
            playSound();

            return null;
        }

        private synchronized void playSound(){

            media.start();
            media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    media.release();
                }
            });
        }
    }


    public int getAltoSpriteJugador() {
        return altoSpriteJugador;
    }

    public void setAltoSpriteJugador(int altoSpriteJugador) {
        this.altoSpriteJugador = altoSpriteJugador;
    }

    public int getAnchoSpriteJugador() {
        return anchoSpriteJugador;
    }

    public void setAnchoSpriteJugador(int anchoSpriteJugador) {
        this.anchoSpriteJugador = anchoSpriteJugador;
    }

}
