package com.kochab02.moreThanYAC.utils;

public class MathUtils
{
    public static int sgn(int a){
        return Integer.compare(a, 0);
    }
    public static float sgn(float a){
        return Float.compare(a, 0);
    }
    public static double sgn(double a){
        return Double.compare(a, 0);
    }
}
