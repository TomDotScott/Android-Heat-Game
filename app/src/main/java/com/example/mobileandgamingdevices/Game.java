package com.example.mobileandgamingdevices;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class Game extends SurfaceView implements SurfaceHolder.Callback
{
    private GameLoop m_gameLoop;

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


        // Create UI Elements
        m_steeringWheel = new SteeringWheel(new Vector2(275d, 700d));
        m_accelerateButton = new Button(Button.eButtonType.Circle, "A", new Vector2(1750d, 400d), 200, 0xff0047c2);
        m_brakeButton = new Button(Button.eButtonType.Circle, "B", new Vector2(1500d, 700d), 200, 0xffc20037);

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

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        Vector2 pressedPosition = new Vector2((double)event.getX(), (double)event.getY());

        switch(event.getAction())
        {
            // Action Down for screen press
            case MotionEvent.ACTION_DOWN:
                m_steeringWheel.checkIfPressed(pressedPosition);
                m_accelerateButton.checkIfPressed(pressedPosition);
                m_brakeButton.checkIfPressed(pressedPosition);

                if(m_steeringWheel.isPressed())
                {
                    m_steeringWheel.setPressedPosition(pressedPosition);
                    m_player.setRotation(m_steeringWheel.getAngle());
                }

                if(m_accelerateButton.isPressed())
                {
                    Log.d("Message", "ACCELERATE!");
                }

                if(m_brakeButton.isPressed())
                {
                    Log.d("Message", "BRAKE!");
                }

                return true;
            // Action Move for press and drag
            case MotionEvent.ACTION_MOVE:
                if(m_steeringWheel.isPressed())
                {
                    m_steeringWheel.setPressedPosition(pressedPosition);
                    m_player.setRotation(m_steeringWheel.getAngle());
                }
                return true;
            case MotionEvent.ACTION_UP:
                m_steeringWheel.fingerReleased();
                m_player.setRotation(0d);
                return true;
        }

        return super.onTouchEvent(event);
    }

    public void update()
    {
        m_steeringWheel.update();
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
}
