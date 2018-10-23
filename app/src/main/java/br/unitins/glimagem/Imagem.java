package br.unitins.glimagem;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class Imagem extends Geometria {

    private Activity tela;
    private int codTextura;
    private float[] coordenadasTexturas;
    private FloatBuffer bufferTextura;
    private int textura;
    private ArrayList<Imagem> pilhaImagem;
    private boolean flag = false;

    public Imagem(GL10 gl, int tamanho, Activity tela) {
        super(gl, tamanho);
        this.tela = tela;
        this.coordenadas = new float[]{
                -tamanho / 2, -tamanho / 2,
                -tamanho / 2, tamanho / 2,
                tamanho / 2, -tamanho / 2,
                tamanho / 2, tamanho / 2
        };

        this.coordenadasTexturas = new float[]{
                0, 1,
                0, 0,
                1, 1,
                1, 0
        };

        this.tamanho = tamanho;
        bufferQuadrado = generateBuffer(coordenadas);
        bufferTextura = generateBuffer(coordenadasTexturas);

        pilhaImagem = new ArrayList<>();

    }

    public void setSpriteSize(float[] coordenadasTexturas){
        this.coordenadasTexturas = coordenadasTexturas;
        bufferTextura = generateBuffer(coordenadasTexturas);
    }
    public void setImagem(int textura) {
        this.codTextura = textura;

    }

    private int carregaTextura(int codTextura) {
        //CARREGA A IMAGEM NA MEMORIA RAAAMMMM
        Bitmap imagem = BitmapFactory.decodeResource(tela.getResources(), codTextura);

        //DEFINE UM ARRAY PARA ARMAZ. DOS IDS DE TEXTURA (APENAS 1 POSICAO)
        int[] idTextura = new int[1];

        //GERA AS AREAS NA GPU E CRIA UM ID PARA CADA UMA
        gl.glGenTextures(1, idTextura, 0);

        //DIZER PARA A MAQUINA QUAL DAS AREAS CRIADAS NA VRAM EU QUERO TRABALHAR
        gl.glBindTexture(GL10.GL_TEXTURE_2D, idTextura[0]);

        //COPIA A IMAGEM DA RAM PARA A VRAM
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, imagem, 0);

        //CONFIGURA OS ALGORITMOS QUE SERAO UTILIZADOS PARA RECALCULAR A IMAGEM EM CASO DE ESCLA PARA MAIS OU MENOS (MIN E MAG)
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);

        //APONTA A VRAM OPENGL PARA O NADA (CODIGO ZERO)
        gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);

        //LIBERA A MEMORIA RAM
        imagem.recycle();

        return idTextura[0];
    }

    public void empilhaImagem(Imagem imagem) {
        this.pilhaImagem.add(imagem);
    }

    @Override
    public void desenha() {

        setAfterDraw();

        if (!flag)
            gl.glLoadIdentity();
        //HABILITANTO A MÁQUINA OPENGL PARA O USO DE TEXTURAS
        gl.glEnable(GL10.GL_TEXTURE_2D);

        //HABILITA A MAQUINA PARA UTILIZAR UM VETOR DE COORDENADAS DE TEX
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        //registra o vetor de coordenadas na OpenGl
        gl.glVertexPointer(2, GL10.GL_FLOAT,
                0, bufferQuadrado);

        gl.glColor4f(vermelho, verde, azul, 1);

        gl.glTranslatef(posX, posY, 0);

        gl.glRotatef(getRotacao(), 0, 0, 1);

        gl.glScalef(escalaX, escalaY, 1);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        if (pilhaImagem.size() > 0) {
            gl.glPushMatrix();

            for (Imagem imagem : pilhaImagem
                    ) {
                imagem.flag = true;
                imagem.desenha();
            }
            gl.glPopMatrix();

            flag = false;

        }


    }

    private void setAfterDraw() {
        this.gl.glEnable(GL10.GL_BLEND);
        this.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);


        //HABILITANTO A MÁQUINA OPENGL PARA O USO DE TEXTURAS
        this.gl.glEnable(GL10.GL_TEXTURE_2D);

        //HABILITA A MAQUINA PARA UTILIZAR UM VETOR DE COORDENADAS DE TEX
        this.gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        //REGISTRA AS COORDENADAS DE TEXTURA NA MÁQUINA OPENGL
        this.gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, this.bufferTextura);

        this.textura = carregaTextura(codTextura);

        //ASSINAR A TEXTURA QUE A OPENGL VAI UTILIZAR NO DESENHO DA PRIMITIVA
        this.gl.glBindTexture(GL10.GL_TEXTURE_2D, this.textura);

    }

}

