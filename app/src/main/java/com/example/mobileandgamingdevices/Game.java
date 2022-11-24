package com.example.mobileandgamingdevices;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.mobileandgamingdevices.graphics.TextureManager;

import java.util.HashMap;
import java.util.Iterator;
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
    private GameDisplay m_gameDisplay;

    private TileMap m_map;

    private Context m_context;

    private SensorManager m_sensorManager;
    private Sensor m_gyroscope;
    private SensorEventListener m_gyroscopeListener;

    private boolean m_tiltToSteer = false;

    private float m_sensorTimeStamp;
    private final float[] m_deltaRotationVector = new float[4];
    private float[] m_currentRotationMatrix = new float[9];
    private float m_rotationFromGyroscope;

    public Game(Context context)
    {
        super(context);

        m_context = context;

        // Add the SurfaceHolder callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        TextureManager.getInstance().addSpriteSheet(context, "PLAYER", 64, R.drawable.player_car);
        TextureManager.getInstance().addSpriteSheet(context, "MAP", 16, R.drawable.tileset);
        TextureManager.getInstance().addSpriteSheet(context, "WHEEL", 256, R.drawable.steering_wheel);

        m_map = new TileMap(context);

        // Create a gameLoop object to update and render to the surface
        m_gameLoop = new GameLoop(this, surfaceHolder);

        // Create Gameobjects
        m_player = new Player(
                new Vector2(400d, 300d)
        );

        // Find the width and height of the screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        m_gameDisplay = new GameDisplay(m_player, new Vector2(
                ((double) displayMetrics.widthPixels),
                ((double) displayMetrics.heightPixels))
        );

        // Initialise the inactive pointers
        for (int i = 0; i < MAX_FINGERS; i++)
        {
            m_inactivePointers.add(new TouchInfo());
        }

        // Create UI Elements
        m_steeringWheel = new SteeringWheel(new Vector2(200d, 500d));
        m_accelerateButton = new Button(Button.eButtonType.Circle, "A", new Vector2(1750d, 400d), 200, 0xff0047c2);
        m_brakeButton = new Button(Button.eButtonType.Circle, "B", new Vector2(1500d, 700d), 200, 0xffc20037);

        setFocusable(true);
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder)
    {
        try
        {
            m_sensorManager = (SensorManager) m_context.getSystemService(Context.SENSOR_SERVICE);
        } catch (Exception e)
        {
            Log.d("GAME", e.getMessage());
        }

        // Set up the accelerometer if we are tilting to steer
        if (m_tiltToSteer)
        {
            setUpGyroscope();

            m_currentRotationMatrix = new float[]{
                    1.f, 0.f, 0.f,
                    0.f, 1.f, 0.f,
                    0.f, 0.f, 1.f
            };
        }


        // Create a new thread if the .join() function was called previously
        if (m_gameLoop.getState().equals(Thread.State.TERMINATED))
        {
            m_gameLoop = new GameLoop(this, surfaceHolder);
        }

        // Start the game as soon as we have a surface to draw to
        m_gameLoop.startLoop();
    }

    private void setUpGyroscope()
    {
        m_gyroscope = m_sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        if (m_gyroscope == null)
        {
            Log.d("ACCELEROMETER", "Uh oh...");
        }

        m_gyroscopeListener = new SensorEventListener()
        {
            @Override
            public void onSensorChanged(SensorEvent event)
            {
                if (m_sensorTimeStamp != 0)
                {
                    // Get the change in time so we can integrate to find the current rotation
                    float deltaTime = (event.timestamp - m_sensorTimeStamp) * (1 / 1000000000.f);

                    float x = event.values[0];
                    float y = event.values[1];
                    float z = event.values[2];

                    // Normalise the rotation vector with the angular speed of the sample
                    float magnitude = (float) Math.sqrt(x * x + y * y + z * z);

                    x /= magnitude;
                    y /= magnitude;
                    z /= magnitude;

                    // Integrate over time to get the change in rotation
                    float thetaOverTwo = magnitude * deltaTime / 2.0f;
                    float sinThetaOverTwo = (float) Math.sin(thetaOverTwo);
                    float cosThetaOverTwo = (float) Math.cos(thetaOverTwo);

                    m_deltaRotationVector[0] = sinThetaOverTwo * x;
                    m_deltaRotationVector[1] = sinThetaOverTwo * y;
                    m_deltaRotationVector[2] = sinThetaOverTwo * z;
                    m_deltaRotationVector[3] = cosThetaOverTwo;
                }

                m_sensorTimeStamp = event.timestamp;
                float[] deltaRotationMatrix = new float[9];

                SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, m_deltaRotationVector);

                // Concatenate the delta matrix to the current matrix so we can use it when steering the car!
                m_currentRotationMatrix = MultiplyMat3x3(m_currentRotationMatrix, deltaRotationMatrix);

                m_rotationFromGyroscope = m_currentRotationMatrix[1] * 180.f / 3.14f;
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy)
            {

            }
        };

        m_sensorManager.registerListener(
                m_gyroscopeListener,
                m_gyroscope,
                SensorManager.SENSOR_DELAY_GAME
        );
    }

    private float[] MultiplyMat3x3(float[] lhs, float[] rhs)
    {
        float out0 = lhs[0] * rhs[0] + lhs[1] * rhs[3] + lhs[2] * rhs[6];
        float out1 = lhs[0] * rhs[1] + lhs[1] * rhs[4] + lhs[2] * rhs[7];
        float out2 = lhs[0] * rhs[2] + lhs[1] * rhs[5] + lhs[2] * rhs[8];
        float out3 = lhs[3] * rhs[0] + lhs[4] * rhs[3] + lhs[5] * rhs[6];
        float out4 = lhs[3] * rhs[1] + lhs[4] * rhs[4] + lhs[5] * rhs[7];
        float out5 = lhs[3] * rhs[2] + lhs[4] * rhs[5] + lhs[5] * rhs[8];
        float out6 = lhs[6] * rhs[0] + lhs[7] * rhs[3] + lhs[8] * rhs[6];
        float out7 = lhs[6] * rhs[1] + lhs[7] * rhs[4] + lhs[8] * rhs[7];
        float out8 = lhs[6] * rhs[2] + lhs[7] * rhs[5] + lhs[8] * rhs[8];

        return new float[]
                {
                        out0, out1, out2,
                        out3, out4, out5,
                        out6, out7, out8
                };
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
            Iterator<Map.Entry<Integer, TouchInfo>> pointerIt = m_activePointers.entrySet().iterator();
            while (pointerIt.hasNext())
            {
                Map.Entry<Integer, TouchInfo> entry = pointerIt.next();
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

        if (m_accelerateButton.isPressed())
        {
            m_player.accelerate();
        } else
        {
            m_player.accelerateReleased();
        }

        if (m_brakeButton.isPressed())
        {
            m_player.brake();
        }

        if (m_tiltToSteer)
        {
            m_player.setRotation(m_rotationFromGyroscope);
        } else
        {
            m_steeringWheel.update();
            m_player.setRotation(m_steeringWheel.getAngle());
        }

        m_player.update();

        m_gameDisplay.update();

        m_map.checkCollision(m_player);
    }

    // This function will be responsible for drawing objects to
    // the screen
    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);

        m_map.drawLowerTiles(canvas, m_gameDisplay);

        m_player.draw(canvas, m_gameDisplay);

        m_map.drawUpperTiles(canvas, m_gameDisplay);

        if(!m_tiltToSteer)
        {
            m_steeringWheel.draw(canvas);
        }

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

        // Unregister the gyroscope
        m_sensorManager.unregisterListener(m_gyroscopeListener);
    }
}
