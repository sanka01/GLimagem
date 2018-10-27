package br.unitins.glimagem;


import android.app.Activity;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

//Classe que ira implementar a logica do desenho
class Render implements GLSurfaceView.Renderer {


    static final int TAMANHO = 50;


    private float largura = 0;
    private float altura;
    private Activity tela;
    private Imagem sol = null;
    private Imagem terra = null;
    private Imagem lua = null;
    private Imagem sprite = null;
    private long inicio, atual;
    private int angulo = 0, frame = 0;

    public Render(Activity tela) {
        this.tela = tela;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        inicio = System.currentTimeMillis();
        atual = System.currentTimeMillis();

        //chamado quando a sup. é criada

        //define a cor de limpeza no formato RGBA
        gl.glClearColor(1, 1, 1, 1);
        gl.glClear(0);

    }

    private void configuraTela(GL10 gl, int width, int height) {
        //configurando a area de coordenadas do plano cartesiano
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();

        altura = height;
        largura = width;
        //configurando o volume de renderização
        gl.glOrthof(0, largura,
                0, altura,
                1, -1);

        //configurando a matriz de Transferencias geometricas
        //translação, rotação e escala
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        //configura a area de visualização na tela do DISP
        gl.glViewport(0, 0, width, height);

        //Habilita o desenho por vertices
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        //HABILITANTO A MÁQUINA OPENGL PARA O USO DE TEXTURAS
        gl.glEnable(GL10.GL_TEXTURE_2D);

        //HABILITA A MAQUINA PARA UTILIZAR UM VETOR DE COORDENADAS DE TEX
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);


    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        configuraTela(gl, width, height);

        sprite = new Imagem(gl, TAMANHO, tela);
        sprite.setSpriteSize(new int[]{2, 4});
//        sprite.setXY(largura/2,altura/2);
        sprite.setImagem(R.mipmap.sprite);


//        sol = new Imagem(gl, TAMANHO*2,tela);
//        sol.setXY(largura / 2, altura / 2);
//        sol.setCor(Geometria.BRANCO);
//        sol.setImagem(R.mipmap.sol);
//
//        terra = new Imagem(gl,TAMANHO,tela);
//        terra.setXY(100 + terra.tamanho,0)
//                .setCor(Geometria.BRANCO);
//        terra.setImagem(R.mipmap.terra);
//
//        lua = new Imagem(gl,TAMANHO/2,tela);
//        lua.setXY(100+lua.tamanho,0).setCor(Geometria.BRANCO);
//        lua.setImagem(R.mipmap.lua);
//
//        sol.empilhaImagem(terra);
//        sol.empilhaImagem(lua);
//        terra.empilhaImagem(lua);

        //ASSINAR A TEXTURA QUE A OPENGL VAI UTILIZAR NO DESENHO DA PRIMITIVA
    }


    @Override
    public void onDrawFrame(GL10 gl) {

        atual = System.currentTimeMillis();

        //Desenha a tela (e mede o fps)

        //Aplica a cor de limpeza da tela a todos os bits do buffer de cor
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        sprite.setSpriteFrame(frame);
        sprite.desenha();

        if (atual >= inicio + 50) {
            frame++;
            inicio = System.currentTimeMillis();
            sprite.animacao(largura);
            if (frame >= 8) {
                frame = 0;
            }
        }
//        sol.desenha();
//        sol.setRotacao(angulo);
//        terra.setRotacao(angulo+1);
//        lua.setRotacao(angulo+2);
//        angulo++;
    }
}