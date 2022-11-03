package com.example.mobileandgamingdevices;

public class TouchInfo
{
    public enum eTouchType
    {
        Invalid,
        Press,
        Release,
        Move
    }

    public eTouchType TouchType;
    public Vector2 TouchPosition;

    public TouchInfo()
    {
        TouchType = eTouchType.Invalid;
        TouchPosition = new Vector2();
    }

    public TouchInfo(eTouchType type, Vector2 touchPosition)
    {
        TouchType = type;
        TouchPosition = touchPosition;
    }
}
