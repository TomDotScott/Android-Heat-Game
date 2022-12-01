package com.example.mobileandgamingdevices;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Food
{
    public enum eFoodType
    {
        Pizza,
        Burger,
        HotDog; // TODO : Maybe swap with or add Parmo?

        private static final List<eFoodType> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
        private static final int SIZE = VALUES.size();
        private static final Random RANDOM = new Random();

        public static eFoodType randomFood()
        {
            return VALUES.get(RANDOM.nextInt(SIZE));
        }
    }

    private eFoodType m_type;

    private static final float PIZZA_COOLDOWN_DURATION = 120f;
    private static final float BURGER_COOLDOWN_DURATION = 90f;
    private static final float HOTDOG_COOLDOWN_DURATION = 45f;

    private float m_cooldownDuration;

    public Food(eFoodType type)
    {
        switch (type)
        {
            case Pizza:
                m_cooldownDuration = PIZZA_COOLDOWN_DURATION;
                break;
            case Burger:
                m_cooldownDuration = BURGER_COOLDOWN_DURATION;
                break;
            case HotDog:
                m_cooldownDuration = HOTDOG_COOLDOWN_DURATION;
                break;
            default:
                Log.e("FOOD", "UNHANDLED CASE!");
                break;
        }

        m_type = type;
    }

    public void update()
    {
        m_cooldownDuration -= 0.016f; // TODO: Make it countdown in real-time... For now hardcoding to 60ups
    }

    public float getCooldownPercentage()
    {
        switch (m_type)
        {
            case Pizza:
                return m_cooldownDuration / PIZZA_COOLDOWN_DURATION * 100f;
            case Burger:
                return m_cooldownDuration / BURGER_COOLDOWN_DURATION * 100f;
            case HotDog:
                return m_cooldownDuration / HOTDOG_COOLDOWN_DURATION * 100f;
        }

        Log.e("FOOD", "UNHANDLED CASE! RETURNING -1");
        return -1f;
    }

    public String getFoodString()
    {
        switch (m_type)
        {
            case Pizza:
                return "Pizza";
            case Burger:
                return "Burger";
            case HotDog:
                return "Hotdog";
        }
        return "UNKNOWN!";
    }
}
