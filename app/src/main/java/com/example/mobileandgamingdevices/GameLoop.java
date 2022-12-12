package com.example.mobileandgamingdevices;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameLoop extends Thread
{
    private static final int MAXIMUM_UPDATES_PER_SECOND = 60;
    private static final long TARGET_UPDATE_PERIOD = 1000 / MAXIMUM_UPDATES_PER_SECOND;

    private boolean m_isRunning = false;
    private SurfaceHolder m_surfaceHolder;

    private Game m_game;

    private double m_averageFPS;
    private double m_averageUPS;

    public GameLoop(Game game, SurfaceHolder surfaceHolder)
    {
        m_game = game;
        m_surfaceHolder = surfaceHolder;
    }

    public double getAverageUPS()
    {
        return m_averageUPS;
    }

    public double getAverageFPS()
    {
        return m_averageFPS;
    }

    public void startLoop()
    {
        m_isRunning = true;
        start();
    }

    @Override
    public void run() {
        super.run();

        int updateCount = 0;
        int frameCount = 0;

        long startTime = System.currentTimeMillis();
        long elapsedTime;
        long sleepTime;

        Canvas canvas = null;
        // The game loop itself
        while(m_isRunning)
        {
            try
            {
                canvas = m_surfaceHolder.lockCanvas();

                // Make sure only this thread can draw to the surface view!
                synchronized (m_surfaceHolder)
                {
                    // Update objects from Game
                    m_game.update();
                    updateCount++;

                    // Render objects
                    m_game.draw(canvas);
                }
            }
            catch (IllegalArgumentException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if(canvas != null)
                {
                    try {
                        m_surfaceHolder.unlockCanvasAndPost(canvas);
                        frameCount++;
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }

            elapsedTime = System.currentTimeMillis() - startTime;
            sleepTime = ((long)updateCount * TARGET_UPDATE_PERIOD) - elapsedTime;
            if(sleepTime > 0)
            {
                try
                {
                    sleep(sleepTime);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

            // Skip frames to keep up with the target UPS
            while(sleepTime < 0 && updateCount < MAXIMUM_UPDATES_PER_SECOND - 1)
            {
                m_game.update();
                updateCount++;

                elapsedTime = System.currentTimeMillis() - startTime;
                sleepTime = ((long)updateCount * TARGET_UPDATE_PERIOD) - elapsedTime;
            }

            // If we have hit a new second, calculate the new averages!
            if(elapsedTime >= 1000l)
            {
                m_averageFPS = frameCount / (0.001 * elapsedTime);
                m_averageUPS = updateCount / (0.001 * elapsedTime);

                // Reset the counters for the next second...
                frameCount = 0;
                updateCount = 0;

                startTime = System.currentTimeMillis();
            }
        }
    }

    public void stopLoop()
    {
        m_isRunning = false;
        // Wait for the thread to join...
        try
        {
            join();
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
