package com.example.mobileandgamingdevices;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.example.mobileandgamingdevices.graphics.SpriteSheet;
import com.example.mobileandgamingdevices.graphics.TextureManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class TileMap
{
    public static final float TILE_SIZE = 128f;

    // Each CSV file is a layer to be drawn from bottom to top
    // Each layer will have its own properties for collision etc
    private List<Tile> m_tileMap = new ArrayList<>();
    private List<RectF> m_colliders = new ArrayList<>();

    public TileMap(Context context)
    {
        try
        {
            parseXmlFile(context, R.raw.demo);
        } catch (Exception e)
        {
            Log.e("TILEMAP", e.getMessage());
        }
    }

    public void draw(Canvas canvas, GameDisplay display)
    {
        for (Tile tile : m_tileMap)
        {
            TextureManager.getInstance().drawSprite(
                    canvas,
                    "MAP",
                    tile.getID(),
                    display.worldToScreenSpace(tile.getPosition()),
                    TILE_SIZE
            );
        }

        Paint debugPaint = new Paint();
        debugPaint.setColor(Color.MAGENTA);

        for(RectF collider : m_colliders)
        {
            Vector2 topLeft = display.worldToScreenSpace(new Vector2(collider.left, collider.top));
            Vector2 bottomRight = display.worldToScreenSpace(new Vector2(collider.right, collider.bottom));

            RectF onScreenRect = new RectF(
                    topLeft.x.floatValue(),
                    topLeft.y.floatValue(),
                    bottomRight.x.floatValue(),
                    bottomRight.y.floatValue()
            );

            canvas.drawRect(onScreenRect, debugPaint);
        }
    }

    public void checkCollision(Player player)
    {
        for(RectF collider : m_colliders)
        {
            // Because Java is a stupid programming language, .intersect() overrides the value stored in the
            // Rectangle to the amount they intersect by... Creating a copy to work on
            RectF currentCollider = new RectF(collider);
            RectF playerCollider = new RectF(player.getCollider());


            if (currentCollider.intersect(playerCollider))
            {
                // work out where the player is in relation to the tile
                Vector2 playerPosition = player.getPosition();
                Vector2 resolution = new Vector2();

                // If the player is below the top, check the X Y collisions
                if(playerPosition.y > currentCollider.top && playerPosition.y < currentCollider.bottom)
                {
                    if (playerPosition.x < currentCollider.left)
                    {
                        // Player is on the left
                        resolution.x = Double.valueOf(playerCollider.right - currentCollider.left);
                    } else
                    {
                        // Player is on the right
                        resolution.x = Double.valueOf(playerCollider.left - currentCollider.right);
                    }
                }

                if(playerPosition.x > currentCollider.left && playerPosition.x < currentCollider.right)
                {
                    if (playerPosition.y < currentCollider.top)
                    {
                        // Player is above
                        Log.d("TILE", "Collision Detected above");
                        resolution.y = Double.valueOf(playerCollider.bottom - currentCollider.top);
                    } else
                    {
                        // Player is below
                        Log.d("TILE", "Collision Detected below");
                        resolution.y = Double.valueOf(playerCollider.top - currentCollider.bottom);
                    }
                }

                // Resolve the collision by moving the player back by x amount
                player.resolveCollision(resolution);
            }
        }
    }

    private void parseCsvFile(String csvContent)
    {
        String[] csvRows = csvContent.split("\n");

        int row = 0;

        for (String line : csvRows)
        {
            String[] tileRow = line.split(",");

            Log.d("TILEMANAGER", "SIZE OF ROW " + tileRow.length + " !");

            Log.d("TILEMANAGER", line);

            for (int i = 0; i < tileRow.length; i++)
            {
                String rowContent = tileRow[i].replaceAll("\\s+", "");

                if (rowContent.isEmpty())
                {
                    continue;
                }

                // Work out the data needed to build the tile object
                int ID = Integer.parseInt(rowContent) - 1;

                if (ID != -1)
                {
                    Vector2 position = new Vector2(
                            (double) i * TILE_SIZE,
                            (double) row * TILE_SIZE
                    );

                    m_tileMap.add(new Tile(ID, position));
                }
            }

            row++;
        }

        Log.d("TILEMANAGER", "READ " + row + " ROWS!");
    }

    void parseXmlFile(Context context, int resourceId) throws XmlPullParserException, IOException
    {
        XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = parserFactory.newPullParser();

        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

        InputStream iStream = context.getResources().openRawResource(resourceId);
        parser.setInput(iStream, null);

        // Iterate over the XML
        m_tileMap = new ArrayList<>();

        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT)
        {
            String elementName = "";
            Log.d("XML PARSER", "TYPE" + eventType);
            switch (eventType)
            {
                case XmlPullParser.START_TAG:
                    elementName = parser.getName();

                    if (elementName.equals("layer"))
                    {
                        String layerContent = parser.nextText();
                        parseCsvFile(layerContent);
                    } else if (elementName.equals("object"))
                    {
                        float posX = Float.parseFloat(parser.getAttributeValue(null, "x")) * (TILE_SIZE / 16);
                        float posY = Float.parseFloat(parser.getAttributeValue(null, "y")) * (TILE_SIZE / 16) + TILE_SIZE;
                        float width = Float.parseFloat(parser.getAttributeValue(null, "width")) * (TILE_SIZE / 16);
                        float height = Float.parseFloat(parser.getAttributeValue(null, "height")) * (TILE_SIZE / 16);

                        m_colliders.add(
                                new RectF(posX, posY, posX + width, posY + height)
                        );
                    }
                    break;
            }

            eventType = parser.next();
        }
    }
}
