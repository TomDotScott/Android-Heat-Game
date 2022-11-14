package com.example.mobileandgamingdevices;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.example.mobileandgamingdevices.graphics.Quad;
import com.example.mobileandgamingdevices.graphics.TextureManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class TileMap
{
    public final float TILE_SIZE = 128f;

    // Each CSV file is a layer to be drawn from bottom to top
    // Each layer will have its own properties for collision etc
    private List<List<Quad>> m_tileMap = new ArrayList<>();

    public TileMap(Context context)
    {
        //m_tileMap.add(openCsvFile(context, R.raw.street_roads));
        m_tileMap.add(openCsvFile(context, R.raw.test));
        //m_tileMap.add(openCsvFile(context, R.raw.street_buildings));
        //m_tileMap.add(openCsvFile(context, R.raw.street_windows_and_doors));
        //m_tileMap.add(openCsvFile(context, R.raw.street_decoration));
    }

    private List<Quad> openCsvFile(Context context, int resourceId)
    {
        InputStream iStream = context.getResources().openRawResource(resourceId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(iStream, Charset.forName("UTF-8")));

        List<Quad> csvContents = new ArrayList<>();
        String line = "";
        int row = 0;

        try
        {
            while ((line = reader.readLine()) != null)
            {
                String[] tileRow = line.split(",");

                Log.d("TILEMANAGER", line);

                for (int i = 0; i < tileRow.length; i++)
                {
                    // Work out the data needed to build the tile object
                    int ID = Integer.parseInt(tileRow[i]);

                    if (ID != -1)
                    {
                        Vector2 position = new Vector2(
                                (double) (i * TILE_SIZE),
                                (double) (row * TILE_SIZE)
                        );

                        csvContents.add(new Quad(ID, position, TILE_SIZE));
                    }
                }

                row++;
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        Log.d("TILEMANAGER", "READ " + row + " ROWS!");
        Log.d("TILEMANAGER", "SIZE OF ROW " + csvContents.size() + " !");

        return csvContents;
    }
}
