package com.example.mobileandgamingdevices.graphics;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.example.mobileandgamingdevices.Vector2;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLRenderer implements GLSurfaceView.Renderer
{
    private Quad m_quad;

    private final float[] m_ViewxProjectionMatrix = new float[16];
    private final float[] m_ProjectionMatrix = new float[16];
    private final float[] m_ViewMatrix = new float[16];

    public OpenGLRenderer()
    {
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
        m_quad = new Quad(141, new Vector2(), 0.1f);

        GLES20.glClearColor(0f, 0f, 0f, 1.f);

        GLES20.glClearDepthf(1.f);

        GLES20.glEnable(GL10.GL_DEPTH_TEST);

        GLES20.glDepthFunc(GL10.GL_LEQUAL);

        GLES20.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height)
    {
        // Set the viewport rectangle
        GLES20.glViewport(0, 0, width, height);

        float aspect = (float) width / (float) height;

        Matrix.frustumM(m_ProjectionMatrix, 0, -aspect, aspect, -1, 1, 3, 7);
    }

    @Override
    public void onDrawFrame(GL10 gl)
    {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Set up the camera position
        Matrix.setLookAtM(
                m_ViewMatrix,
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

        // Calulate the view transformation
        Matrix.multiplyMM(m_ViewxProjectionMatrix, 0, m_ProjectionMatrix, 0, m_ViewMatrix, 0);

        m_quad.draw(m_ViewxProjectionMatrix);
    }
}
