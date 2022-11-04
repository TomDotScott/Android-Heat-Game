package com.example.mobileandgamingdevices.graphics;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.mobileandgamingdevices.Vector2;

public class Sprite
{
    private final SpriteSheet m_spriteSheet;

    private final Rect m_bounds;

    public Sprite(SpriteSheet spriteSheet, Rect rect)
    {
        m_spriteSheet = spriteSheet;
        m_bounds = rect;
    }

    public void draw(Canvas canvas, Vector2 position, Vector2 size)
    {
        Vector2 topLeft = position;
        Vector2 bottomRight = topLeft.add(size);

        Rect screenBounds = new Rect(
                topLeft.x.intValue(),
                topLeft.y.intValue(),
                bottomRight.x.intValue(),
                bottomRight.y.intValue()
        );

        canvas.drawBitmap(
                m_spriteSheet.getBitmap(),
                m_bounds,
                screenBounds,
                null
        );
    }
}
