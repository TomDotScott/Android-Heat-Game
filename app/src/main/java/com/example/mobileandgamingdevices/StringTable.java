package com.example.mobileandgamingdevices;


import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class StringTable
{
    private static StringTable INSTANCE = null;

    // TODO: Stringtables might contain data such as the size, duration on screen and font, if I can be bothered... For now, it's just the titles and strings
    private final HashMap<String, String> m_stringEntries;

    public static StringTable getInstance()
    {
        if(INSTANCE == null)
        {
            INSTANCE = new StringTable();
        }

        return INSTANCE;
    }

    private StringTable()
    {
        m_stringEntries = new HashMap<>();
    }

    public void parseStringTableData(Context context) throws IOException
    {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.getResources().openRawResource(R.raw.stringtable), StandardCharsets.UTF_8)
        );

        boolean firstLine = true;
        String line = reader.readLine();
        while(line != null)
        {
            // Check if it's the CSV Header or a comment...
            if(line.charAt(0) != '#' && !firstLine)
            {
                Log.d("STRINGTABLE", line);
                String[] contents = line.split(",");

                m_stringEntries.put(contents[0], contents[1]);
            }

            line = reader.readLine();
            firstLine = false;
        }
    }

    public String getStringEntry(String title)
    {
        return m_stringEntries.get(title);
    }
}
