package com.example.mobileandgamingdevices.graphics;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.example.mobileandgamingdevices.Vector2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Quad
{
    private final String m_vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String m_fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private FloatBuffer m_vertexBuffer;
    private ShortBuffer m_elementBuffer;

    private final int m_shaderProgram;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float m_vertexCoords[] = {   // in counterclockwise order:
            -0.5f,  0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,   // bottom right
            0.5f,  0.5f, 0.0f // top right
    };

    private short m_indices[] = { 0, 2, 1, 0, 3, 2 }; // order to draw vertices

    // Set color with red, green, blue and alpha (opacity) values
    float m_colour[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};

    private int m_positionHandle;
    private int m_colourHandle;
    private int m_ViewxProjectionMatrixHandle;

    private final int m_vertexCount = m_vertexCoords.length / COORDS_PER_VERTEX;
    private final int m_vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    private Vector2 m_position = new Vector2();
    private final float m_scale;

    public Quad(float scale)
    {
        m_scale = scale;

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                m_vertexCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        m_vertexBuffer = bb.asFloatBuffer();
        m_vertexBuffer.put(m_vertexCoords);
        m_vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                m_indices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        m_elementBuffer = dlb.asShortBuffer();
        m_elementBuffer.put(m_indices);
        m_elementBuffer.position(0);

        int vertexShader = OpenGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                m_vertexShaderCode);
        int fragmentShader = OpenGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                m_fragmentShaderCode);

        // create empty OpenGL ES Program
        m_shaderProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(m_shaderProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(m_shaderProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(m_shaderProgram);
    }

    public void draw(float[] mvpMatrix)
    {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(m_shaderProgram);

        // get handle to vertex shader's vPosition member
        m_positionHandle = GLES20.glGetAttribLocation(m_shaderProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(m_positionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(m_positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                m_vertexStride, m_vertexBuffer);

        // get handle to fragment shader's vColor member
        m_colourHandle = GLES20.glGetUniformLocation(m_shaderProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(m_colourHandle, 1, m_colour, 0);

        // get handle to shape's transformation matrix
        m_ViewxProjectionMatrixHandle = GLES20.glGetUniformLocation(m_shaderProgram, "uMVPMatrix");

        Matrix.translateM(mvpMatrix, 0, m_position.x.floatValue(), m_position.y.floatValue(), 0f);

        Matrix.scaleM(mvpMatrix, 0, m_scale, m_scale, m_scale);

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(m_ViewxProjectionMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, m_indices.length, GLES20.GL_UNSIGNED_SHORT, m_elementBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(m_positionHandle);
    }

    public void setPosition(Vector2 position)
    {
        m_position = position;
    }
}
