package com.example.mobileandgamingdevices;

public class Vector2
{
    public Double x;
    public Double y;

    public Vector2()
    {
        this.x = 0d;
        this.y = 0d;
    }

    public Vector2(Double x, Double y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector2 add(Vector2 other)
    {
        return new Vector2(this.x + other.x, this.y + other.y);
    }

    public Vector2 sub(Vector2 other)
    {
        return new Vector2(other.x - this.x, other.y - this.y);
    }

    public double magnitude()
    {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public double sqrMagnitude()
    {
        return this.x * this.x + this.y * this.y;
    }

    public void normalize()
    {
        double mag = magnitude();
        x /= mag;
        y /= mag;
    }

    public static Vector2 normalize(Vector2 vector)
    {
        double mag = vector.magnitude();
        return new Vector2(vector.x / mag, vector.y / mag);
    }

    public static double dot(Vector2 a, Vector2 b)
    {
        return a.x * b.x + a.y * b.y;
    }

    public static Vector2 rotate(Vector2 v, double degrees)
    {
        double x = v.x * Math.cos(degrees * (Math.PI / 180d)) -
                v.y * Math.sin(degrees * (Math.PI / 180d));

        double y = v.x * Math.sin(degrees * (Math.PI / 180d)) +
                v.y * Math.cos(degrees * (Math.PI / 180d));

        return new Vector2(x, y);
    }

    public static double angle(Vector2 a, Vector2 b)
    {
        return Math.acos( Vector2.dot(a, b) / (a.magnitude() * b.magnitude()) );
    }
}
