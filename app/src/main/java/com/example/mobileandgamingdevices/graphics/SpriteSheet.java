package com.example.mobileandgamingdevices.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import com.example.mobileandgamingdevices.R;

import java.util.HashMap;
import java.util.Map;

public class SpriteSheet
{
    private Bitmap m_spriteSheetImage;

    final private int m_spriteSize;

    private Map<String, Rect> m_sprites;

    public SpriteSheet(Context context, int resourceID, int spriteSize)
    {
        m_sprites = new HashMap<>();

        m_spriteSize = spriteSize;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

        bitmapOptions.inScaled = false;

        m_spriteSheetImage = BitmapFactory.decodeResource(
                context.getResources(),
                resourceID,
                bitmapOptions
        );

        int width = m_spriteSheetImage.getWidth();
        int height = m_spriteSheetImage.getHeight();

        int id = 0;
        for (int j = 0; j < height; j += m_spriteSize)
        {
            for (int i = 0; i < width; i += m_spriteSize)
            {
                Rect bounds = new Rect(
                        i,
                        j,
                        i + m_spriteSize,
                        j + m_spriteSize
                );

                Log.d("SPRITESHEET",
                        "ID: " + id + " i: " + i + " j: " + j + "\nTL=" + bounds.left + "," + bounds.top + "BR=" + bounds.right + ", " + bounds.bottom + "\n\n  ");

                m_sprites.put(String.valueOf(id++), bounds);
            }
        }

    }

    public Rect getSpriteBounds(String spriteID)
    {
        return m_sprites.get(spriteID);
    }

    public Bitmap getBitmap()
    {
        return m_spriteSheetImage;
    }

    public int getSpriteSize()
    {
        return m_spriteSize;
    }
}
