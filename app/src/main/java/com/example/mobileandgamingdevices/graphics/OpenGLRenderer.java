package com.example.mobileandgamingdevices.graphics;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.example.mobileandgamingdevices.Game;
import com.example.mobileandgamingdevices.Vector2;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLRenderer implements GLSurfaceView.Renderer
{
    private Game m_owner;

    private final float[] m_ProjectionMatrix = new float[16];

    public static List<Quad> DRAW_LIST = new ArrayList<>();

    public OpenGLRenderer(Game owner)
    {
        m_owner = owner;
    }

    public static int loadShader(int type, String shaderCode)
    {
        // Create the shader, compile, and return the reference
        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig)
    {
        GLES20.glClearColor(0f, 0f, 0f, 1.f);

        GLES20.glClearDepthf(1.f);

        GLES20.glEnable(GL10.GL_DEPTH_TEST);

        GLES20.glDepthFunc(GL10.GL_LEQUAL);

        GLES20.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

        // Finally, initialise the game
        m_owner.init();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        // Set the viewport rectangle
        GLES20.glViewport(0, 0, width, height);

        float aspect = (float) width / (float) height;
        Matrix.orthoM(m_ProjectionMatrix, 0, -aspect, aspect, -1, 1, 3, 7);
    }

    @Override
    public void onDrawFrame(GL10 gl)
    {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        for (Quad drawable : DRAW_LIST)
        {
            float[] viewMatrix = new float[16];

            // Set up the camera position
            Matrix.setLookAtM(
                    viewMatrix,
                    0,
                    0,
                    0,
                    3,
                    0f,
                    0f,
                    0f,
                    0f,
                    1.0f,
                    0f
            );

            float[] viewxProjectionMatrix = new float[16];

            // Calulate the view transformation
            Matrix.multiplyMM(viewxProjectionMatrix, 0, m_ProjectionMatrix, 0, viewMatrix, 0);


            float[] matrix = viewxProjectionMatrix;
            drawable.draw(matrix);
        }
    }
}
