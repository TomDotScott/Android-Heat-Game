package com.example.mobileandgamingdevices;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.view.MotionEventCompat;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class Game extends SurfaceView implements SurfaceHolder.Callback
{
    private GameLoop m_gameLoop;

    // Keep track of the total number of fingers on screen
    final private static int MAX_FINGERS = 3;
    private LinkedList<TouchInfo> m_inactivePointers = new LinkedList<>();
    private Map<Integer, TouchInfo> m_activePointers = new HashMap<>();

    // GameObjects
    private Player m_player;
    private SteeringWheel m_steeringWheel;
    private Button m_accelerateButton;
    private Button m_brakeButton;


    public Game(Context context)
    {
        super(context);

        // Add the SurfaceHolder callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        // Create a gameLoop object to update and render to the surface
        m_gameLoop = new GameLoop(this, surfaceHolder);

        // Create Gameobjects
        m_player = new Player(new Vector2(400d, 300d));

        // Initialise the inactive pointers
        for (int i = 0; i < MAX_FINGERS; i++)
        {
            m_inactivePointers.add(new TouchInfo());
        }

        // Create UI Elements
        m_steeringWheel = new SteeringWheel(new Vector2(275d, 700d));
        m_accelerateButton = new Button(Button.eButtonType.Circle, "A", new Vector2(1750d, 400d), 200, 0xff0047c2);
        m_brakeButton = new Button(Button.eButtonType.Circle, "B", new Vector2(1500d, 700d), 200, 0xffc20037);

        setFocusable(true);
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder)
    {
        // Create a new thread if the .join() function was called previously
        if(m_gameLoop.getState().equals(Thread.State.TERMINATED))
        {
            m_gameLoop = new GameLoop(this, surfaceHolder);
        }

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


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getActionMasked())
        {
            // One or more fingers pressed down
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
            {
                if (m_inactivePointers.size() > 0)
                {
                    int index = event.getActionIndex();
                    int pointerID = event.getPointerId(index);

                    TouchInfo info = m_inactivePointers.remove();

                    if (info != null)
                    {
                        info.TouchPosition.x = (double) event.getX(index);
                        info.TouchPosition.x = (double) event.getY(index);
                        info.TouchType = TouchInfo.eTouchType.Press;

                        m_activePointers.put(pointerID, info);

                        Log.d("NEW TOUCH!", String.format("New touch at %f %f", info.TouchPosition.x.floatValue(), info.TouchPosition.y.floatValue()));
                    }
                } else
                {
                    Log.d("MAX TOUCHES", "More than 3 pointers");
                }
                return true;
            }

            // One or more fingers released
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            {
                int index = event.getActionIndex();
                int pointerId = event.getPointerId(index);

                TouchInfo info = m_activePointers.remove(pointerId);

                if (info != null)
                {
                    info.TouchType = TouchInfo.eTouchType.Release;
                    m_inactivePointers.add(info);

                    fingerReleased(info);
                }
                return true;
            }

            // One of the active touches moved
            case MotionEvent.ACTION_MOVE:
            {
                for (int i = 0; i < event.getPointerCount(); i++)
                {
                    int pointerID = event.getPointerId(i);

                    TouchInfo info = m_activePointers.get(pointerID);

                    if (info != null)
                    {
                        info.TouchType = TouchInfo.eTouchType.Move;
                        info.TouchPosition.x = (double) event.getX(i);
                        info.TouchPosition.y = (double) event.getY(i);
                    }
                }
                return true;
            }
        }

        return super.onTouchEvent(event);
    }

    private void fingerReleased(TouchInfo info)
    {
        m_steeringWheel.fingerReleased(info);

        m_accelerateButton.fingerReleased(info);

        m_brakeButton.fingerReleased(info);
    }


    private void handleInput()
    {
        //Log.d("ACTIVE SIZE: ", String.valueOf(m_activePointers.size()));
        //Log.d("INACTIVE SIZE: ", String.valueOf(m_inactivePointers.size()));

        if (!m_activePointers.isEmpty())
        {
            for (Map.Entry<Integer, TouchInfo> entry : m_activePointers.entrySet())
            {
                TouchInfo info = entry.getValue();

                m_steeringWheel.checkIfPressed(info);

                m_accelerateButton.checkIfPressed(info);

                m_brakeButton.checkIfPressed(info);

                /*Log.d("ACTIVE POINTERS: ", String.format("ID: %f\tTYPE: %s\tPOSITION(x: %f, y: %f)",
                        entry.getKey().floatValue(),
                        info.TouchType.toString(),
                        info.TouchPosition.x.floatValue(),
                        info.TouchPosition.y.floatValue())
                );*/
            }
        }
    }


    public void update()
    {
        handleInput();

        if(m_accelerateButton.isPressed())
        {
            m_player.accelerate();
        }
        else
        {
            m_player.accelerateReleased();
        }

        if(m_brakeButton.isPressed())
        {
            m_player.brake();
        }

        m_steeringWheel.update();
        m_player.setRotation(m_steeringWheel.getAngle());
        m_player.update();
    }

    // This function will be responsible for drawing objects to
    // the screen
    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);

        m_player.draw(canvas);

        m_steeringWheel.draw(canvas);

        m_accelerateButton.draw(canvas);
        m_brakeButton.draw(canvas);

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

    public void pause()
    {
        m_gameLoop.stopLoop();
    }
}
