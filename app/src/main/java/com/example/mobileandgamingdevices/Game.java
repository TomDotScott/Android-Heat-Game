package com.example.mobileandgamingdevices;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.io.Console;

public class Game extends SurfaceView implements SurfaceHolder.Callback
{
    private GameLoop m_gameLoop;
    private Context m_context;

    public Game(Context context)
    {
        super(context);

        m_context = context;

        // Add the SurfaceHolder callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        // Create a gameloop object to update and render to the surface
        m_gameLoop = new GameLoop(this, surfaceHolder);

        setFocusable(true);
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder)
    {
        // Start the game as soon as we have a surface to draw to
        m_gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2)
    {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder)
    {

    }

    // This function will be responsible for drawing objects to
    // the screen
    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);

        drawStats(canvas);
    }

    public void drawStats(Canvas canvas)
    {
        // Draw FPS text to the screen
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);

        canvas.drawText(
                String.format("FPS: %s", m_gameLoop.getAverageFPS()),
                100,
                60,
                paint
        );

        canvas.drawText(
                String.format("UPS: %s", m_gameLoop.getAverageUPS()),
                100,
                120,
                paint
        );
    }

    public void update()
    {
    }
}
