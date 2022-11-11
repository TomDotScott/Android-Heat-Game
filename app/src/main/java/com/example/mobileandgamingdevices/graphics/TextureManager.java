package com.example.mobileandgamingdevices.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

import com.example.mobileandgamingdevices.GameDisplay;
import com.example.mobileandgamingdevices.Vector2;

import java.util.HashMap;
import java.util.Map;

public class TextureManager
{
    private static TextureManager INSTANCE;
    final public static int SPRITE_SIZE = 16;

    private SpriteSheet m_spriteSheet;

    private Map<Integer, Bitmap> m_sprites;

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
                        "ID: " + id + " i: " + i + " j: " + j + "\nTL=" + bounds.left + "," + bounds.top + "BR=" + bounds.right + ", " + bounds.bottom + "\n\n  ");

                Bitmap spritePixels = Bitmap.createBitmap(
                        m_spriteSheet.getBitmap(),
                        bounds.left,
                        bounds.top,
                        SPRITE_SIZE,
                        SPRITE_SIZE,
                        new Matrix(),
                        true
                );

                m_sprites.put(id++, spritePixels);
            }
        }

        Log.d("TEXTUREMANAGER", String.valueOf(m_sprites.size()));
    }

    public Bitmap getSprite(int spriteID)
    {
        if(m_sprites.containsKey(spriteID))
        {
            Log.d("TEXTUREMANAGER", "Accessing Sprite with ID " + spriteID);
            return m_sprites.get(spriteID);
        }
        Log.d("TEXTUREMANAGER", "SPRITE DOESN'T EXIST");
        return null;
    }
}
