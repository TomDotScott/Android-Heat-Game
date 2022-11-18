package com.example.mobileandgamingdevices.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.example.mobileandgamingdevices.R;

public class SpriteSheet
{
    private Bitmap m_spriteSheetImage;

    final public int SPRITE_SIZE;


    public SpriteSheet(Context context, int spriteSize)
    {
        SPRITE_SIZE = spriteSize;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

        bitmapOptions.inScaled = false;

        m_spriteSheetImage = BitmapFactory.decodeResource(
                context.getResources(),
                R.drawable.sprite_sheet,
                bitmapOptions
        );
    }

    public Rect getSpriteBounds(int row, int col)
    {
        // Work out the bounds of the pixels from the row and column
        int tlX = row * SPRITE_SIZE;
        int tlY = col * SPRITE_SIZE;

        int brX = tlX + SPRITE_SIZE;
        int brY = tlY + SPRITE_SIZE;

        return new Rect(tlX, tlY, brX, brY);
    }

    public Bitmap getBitmap()
    {
        return m_spriteSheetImage;
    }

    public int getWidth()
    {
        return m_spriteSheetImage.getWidth();
    }

    public int getHeight()
    {
        return m_spriteSheetImage.getHeight();
    }
}
