package com.example.mobileandgamingdevices.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

import com.example.mobileandgamingdevices.GameDisplay;
import com.example.mobileandgamingdevices.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextureManager
{
    private static TextureManager INSTANCE;

    private HashMap<String, SpriteSheet> m_spriteSheets;

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
        m_spriteSheets = new HashMap<>();
    }

    public void addSpriteSheet(Context context, String spriteSheetName, int spriteSize, int resourceID)
    {
        m_spriteSheets.put(spriteSheetName, new SpriteSheet(context, resourceID, spriteSize));
    }

    public void drawSprite(Canvas canvas, String spritesheetID, String spriteID, Vector2 position, float size)
    {
        // Only draw if it will actually be on screen!
        if ((position.x > -size && position.x < GameDisplay.SCREEN_WIDTH + size) &&
                (position.y > -size && position.y < GameDisplay.SCREEN_HEIGHT + size))
        {
            SpriteSheet currentSpriteSheet = m_spriteSheets.get(spritesheetID);
            Rect sprite = currentSpriteSheet.getSpriteBounds(spriteID);

            Vector2 topLeft = position;
            Vector2 bottomRight = new Vector2(
                    topLeft.x + size,
                    topLeft.y + size
            );

            Rect screenBounds = new Rect(
                    topLeft.x.intValue(),
                    topLeft.y.intValue(),
                    bottomRight.x.intValue(),
                    bottomRight.y.intValue()
            );

            int spriteSize = currentSpriteSheet.getSpriteSize();

            Bitmap spritePixels = Bitmap.createBitmap(
                    m_spriteSheets.get(spritesheetID).getBitmap(),
                    sprite.left,
                    sprite.top,
                    spriteSize,
                    spriteSize
            );

            canvas.drawBitmap(
                    spritePixels,
                    new Rect(0,
                            0,
                            spriteSize,
                            spriteSize
                    ),
                    screenBounds,
                    null
            );
        }
    }
}
