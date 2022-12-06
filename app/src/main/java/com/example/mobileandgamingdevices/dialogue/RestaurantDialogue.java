package com.example.mobileandgamingdevices.dialogue;

import com.example.mobileandgamingdevices.Food;
import com.example.mobileandgamingdevices.Game;
import com.example.mobileandgamingdevices.StringTable;

public class RestaurantDialogue extends DialogueScene
{
    public RestaurantDialogue(String restaurantName, String streetName)
    {
        super(0, Game.RandomInt(0, 5));

        m_food = new Food(Food.eFoodType.randomFood(), Game.RandomInt(2, 4), restaurantName, streetName);

        int prompt = Game.RandomInt(1, 5);
        String promptID = "RestaurantPrompt" + prompt;

        m_dialogue = StringTable.getInstance().getStringEntry(promptID);

        // Format dialogue, looking for {FOOD}, {NUM} and {LOCATION}
        m_dialogue = m_dialogue.replaceAll("%FOOD%", m_food.getType().toString());
        m_dialogue = m_dialogue.replaceAll("%NUM%", String.valueOf(m_food.getQuantity()));
        m_dialogue = m_dialogue.replaceAll("%LOCATION%", streetName);
    }
}
