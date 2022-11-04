package com.example.mobileandgamingdevices.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

import com.example.mobileandgamingdevices.R;
import com.example.mobileandgamingdevices.Vector2;

import java.util.HashMap;
import java.util.Map;

public class TextureManager
{
    private static TextureManager INSTANCE;
    final private static int SPRITE_SIZE = 16;

    private SpriteSheet m_spriteSheet;

    private Map<Integer, Rect> m_sprites;

    public static TextureManager getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new TextureManager();
        }

        return INSTANCE;
    }

    private TextureManager()
    {
    }

    public void init(Context context)
    {
        m_spriteSheet = new SpriteSheet(context, SPRITE_SIZE);

        m_sprites = new HashMap<>();

        int width = m_spriteSheet.getWidth();
        int height = m_spriteSheet.getHeight();

        Log.d("TEXTUREMANAGER", String.valueOf(width));
        Log.d("TEXTUREMANAGER", String.valueOf(height));

        int id = 0;
        for (int j = 0; j < height; j += SPRITE_SIZE)
        {
            for (int i = 0; i < width; i += SPRITE_SIZE)
            {
                int startPixelsI = i;
                int startPixelJ = j;

                Rect bounds = new Rect(startPixelsI, startPixelJ, startPixelsI + SPRITE_SIZE, startPixelJ + SPRITE_SIZE);

                Log.d("TEXTUREMANAGER",
                        "ID: " + id + " i: " + i + " j: " + j + "\nTL=" + bounds.left + "," + bounds.top + "BR="+ bounds.right + ", " + bounds.bottom + "\n\n  ");

                m_sprites.put(id++, bounds);

                if(id == 426)
                {
                    Log.d("TEXTUREMANAGER", "FOUND IT");
                }
            }
        }
    }

    public void drawSprite(int id, Canvas canvas, Vector2 position, Vector2 size, float rotation)
    {
        Rect sprite = m_sprites.get(id);

        Vector2 topLeft = position;
        Vector2 bottomRight = topLeft.add(size);

        Rect screenBounds = new Rect(
                topLeft.x.intValue(),
                topLeft.y.intValue(),
                bottomRight.x.intValue(),
                bottomRight.y.intValue()
        );

        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);

        Bitmap spritePixels = Bitmap.createBitmap(
                m_spriteSheet.getBitmap(),
                sprite.left,
                sprite.top,
                SPRITE_SIZE,
                SPRITE_SIZE,
                matrix,
                true
        );

        canvas.drawBitmap(
                spritePixels,
                new Rect(0,
                        0,
                        SPRITE_SIZE,
                        SPRITE_SIZE),
                screenBounds,
                null
        );
    }
}
