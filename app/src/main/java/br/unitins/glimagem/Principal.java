package br.unitins.glimagem;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class Principal extends Activity {

    //Declara referencia para a superficie de desenho
    private GLSurfaceView superficieDesenho = null;
    //declara referencia para o Render
    private Render render = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Instancia um objeto da superficie de desenho
        superficieDesenho = new GLSurfaceView(this);
        //instancia o objeto renderizador
        render = new Render(this);
        //Configura o objeto de desenho na superficie
        superficieDesenho.setRenderer(render);


        //Publicar a superficie de desenho na tela
        setContentView(superficieDesenho);
    }
}

