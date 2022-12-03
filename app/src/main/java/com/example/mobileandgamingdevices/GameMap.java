package com.example.mobileandgamingdevices;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.example.mobileandgamingdevices.graphics.TextureManager;

import org.apache.commons.lang3.ArrayUtils;
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

public class GameMap
{
    public static final float TILE_SIZE = 64f;

    // Draw these first
    private List<Tile> m_lowerTiles = new ArrayList<>();

    // These will be drawn after the player, to appear on top
    private List<Tile> m_upperTiles = new ArrayList<>();

    private List<RectF> m_colliders = new ArrayList<>();
    private List<RectF> m_dropOffColliders = new ArrayList<>();
    private List<RectF> m_restaurantColliders = new ArrayList<>();

    public GameMap(Context context)
    {
        try
        {
            parseLevelXml(context, R.raw.map);

            Log.d("MAP", "Parsed " + m_lowerTiles.size() + m_upperTiles.size() + " tiles from the XML");
            Log.d("MAP", "Parsed " + m_colliders.size() + " box colliders from the XML");
            Log.d("MAP", "Parsed " + m_dropOffColliders.size() + " drop off locations from the XML");
            Log.d("MAP", "Parsed " + m_restaurantColliders.size() + " restaurant locations from the XML");

        } catch (Exception e)
        {
            Log.e("TILEMAP", e.getMessage());
            e.printStackTrace();
        }
    }

    public void drawLowerTiles(Canvas canvas, GameDisplay display, Vector2 playerPosition)
    {
        drawTiles(canvas, display, m_lowerTiles, playerPosition);
    }

    public void drawUpperTiles(Canvas canvas, GameDisplay display, Vector2 playerPosition)
    {
        drawTiles(canvas, display, m_upperTiles, playerPosition);
    }

    private void drawTiles(Canvas canvas, GameDisplay display, List<Tile> tileList, Vector2 playerPosition)
    {
        for (Tile tile : tileList)
        {
            // Only draw if the tile is near the player's radius
            float dx = Math.abs(tile.getPosition().x.floatValue() - playerPosition.x.floatValue());
            float dy = Math.abs(tile.getPosition().y.floatValue() - playerPosition.y.floatValue());

            if (dx <= GameDisplay.SCREEN_WIDTH && dy <= GameDisplay.SCREEN_HEIGHT)
            {
                TextureManager.getInstance().drawSprite(
                        canvas,
                        "MAP",
                        tile.getID(),
                        display.worldToScreenSpace(tile.getPosition()),
                        TILE_SIZE
                );
            }
        }
    }

    private void drawDebugColliders(Canvas canvas, GameDisplay display)
    {
        Paint debugPaint = new Paint();
        debugPaint.setColor(Color.MAGENTA);

        for (RectF collider : m_colliders)
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
        for (RectF collider : m_colliders)
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
                if (playerPosition.y > currentCollider.top && playerPosition.y < currentCollider.bottom)
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

                if (playerPosition.x > currentCollider.left && playerPosition.x < currentCollider.right)
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

    private void parseTileCsv(String csvContent, String[] priorityTileIDs)
    {
        String[] csvRows = csvContent.split("\n");

        int row = 0;

        for (String line : csvRows)
        {
            String[] tileRow = line.split(",");

            //Log.d("TILEMANAGER", "SIZE OF ROW " + tileRow.length + " !");
            //Log.d("TILEMANAGER", line);

            for (int i = 0; i < tileRow.length; i++)
            {
                String cell = tileRow[i].replaceAll("\\s+", "");

                if (cell.isEmpty())
                {
                    continue;
                }

                // Work out the data needed to build the tile object
                int ID = Integer.parseInt(cell) - 1;

                if (ID != -1)
                {
                    Vector2 position = new Vector2(
                            (double) i * TILE_SIZE,
                            (double) row * TILE_SIZE
                    );

                    Tile tile = new Tile(ID, position);

                    if (ArrayUtils.contains(priorityTileIDs, cell))
                    {
                        m_upperTiles.add(tile);
                    } else
                    {
                        m_lowerTiles.add(tile);
                    }
                }
            }

            row++;
        }

        Log.d("TILEMANAGER", "READ " + row + " ROWS!");
    }

    void parseLevelXml(Context context, int resourceId) throws XmlPullParserException, IOException
    {
        // All of the priority tiles are on one line in CSV format
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.getResources().openRawResource(R.raw.render_priority_tiles),
                        Charset.forName("UTF-8"))
        );

        String[] priorityTileIDs = reader.readLine().split(",");

        XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = parserFactory.newPullParser();

        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

        InputStream iStream = context.getResources().openRawResource(resourceId);
        parser.setInput(iStream, null);

        // Iterate over the XML
        m_lowerTiles = new ArrayList<>();

        int eventType = parser.getEventType();

        boolean addingColliders = false;
        boolean addingDropOffs = false;
        boolean addingRestaurants = false;

        while (eventType != XmlPullParser.END_DOCUMENT)
        {
            String elementName = "";

            // Log.d("XML PARSER", "TYPE" + eventType);
            switch (eventType)
            {
                case XmlPullParser.START_TAG:
                    elementName = parser.getName();

                    if (elementName.equals("layer"))
                    {
                        String layerContent = parser.nextText();
                        parseTileCsv(layerContent, priorityTileIDs);
                    } else if (elementName.equals("objectgroup"))
                    {
                        addingColliders = false;
                        addingDropOffs = false;
                        addingRestaurants = false;

                        String type = parser.getAttributeValue(null, "name");
                        if (type.equals("colliders"))
                        {
                            addingColliders = true;
                        } else if (type.equals("drop_offs"))
                        {
                            addingDropOffs = true;
                        } else if (type.equals("restaurants"))
                        {
                            addingRestaurants = true;
                        }
                    } else if (elementName.equals("object"))
                    {
                        float posX = Float.parseFloat(parser.getAttributeValue(null, "x")) * (TILE_SIZE / 16);
                        float posY = Float.parseFloat(parser.getAttributeValue(null, "y")) * (TILE_SIZE / 16) + TILE_SIZE;
                        float width = Float.parseFloat(parser.getAttributeValue(null, "width")) * (TILE_SIZE / 16);
                        float height = Float.parseFloat(parser.getAttributeValue(null, "height")) * (TILE_SIZE / 16);

                        RectF rect = new RectF(posX, posY, posX + width, posY + height);

                        if (addingColliders)
                        {
                            m_colliders.add(rect);
                        } else if (addingDropOffs)
                        {
                            m_dropOffColliders.add(rect);
                        } else if (addingRestaurants)
                        {
                            m_restaurantColliders.add(rect);
                        }
                    }
                    break;
            }

            eventType = parser.next();
        }
    }

    public RectF getRandomRestaurant()
    {
        return m_restaurantColliders.get(Game.RandomInt(0, m_restaurantColliders.size() - 1));
    }

    public RectF getRandomDropOff()
    {
        return m_dropOffColliders.get(Game.RandomInt(0, m_dropOffColliders.size() - 1));
    }
}
