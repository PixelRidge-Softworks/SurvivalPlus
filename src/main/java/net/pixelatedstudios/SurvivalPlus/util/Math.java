package net.pixelatedstudios.SurvivalPlus.util;

/**
 * Simple math util methods
 * <p>Adds on to things Java is missing, but not many here so far</p>
 */
@SuppressWarnings("unused")
public class Math {

    /**
     * Math utility for getting a value between a min and a max double
     *
     * @param val Value to try
     * @param min Minimum value to pass
     * @param max Maximum value to pass
     * @return Value locked between min and max
     */
    public static double clamp(double val, double min, double max) {
        return java.lang.Math.max(min, java.lang.Math.min(max, val));
    }

    /**
     * Math utility for getting a value between a min and a max float
     *
     * @param val Value to try
     * @param min Minimum value to pass
     * @param max Maximum value to pass
     * @return Value locked between min and max
     */
    public static float clamp(float val, float min, float max) {
        return java.lang.Math.max(min, java.lang.Math.min(max, val));
    }

    /**
     * Math utility for getting a value between a min and a max int
     *
     * @param val Value to try
     * @param min Minimum value to pass
     * @param max Maximum value to pass
     * @return Value locked between min and max
     */
    public static int clamp(int val, int min, int max) {
        return java.lang.Math.max(min, java.lang.Math.min(max, val));
    }

    /**
     * Returns the smaller of two double values.
     *
     * @param a first double
     * @param b second double
     * @return The smaller of a and b
     */
    public static double min(double a, double b) {
        return java.lang.Math.min(a, b);
    }

    /**
     * Returns the smaller of two int values.
     *
     * @param a first int
     * @param b second int
     * @return The smaller of a and b
     */
    public static int min(int a, int b) {
        return java.lang.Math.min(a, b);
    }

    /**
     * Returns the greater of two double values.
     *
     * @param a first double
     * @param b second double
     * @return The larger of a and b
     */
    public static double max(double a, double b) {
        return java.lang.Math.max(a, b);
    }

    /**
     * Returns the greater of two int values.
     *
     * @param a first int
     * @param b second int
     * @return The larger of a and b
     */
    public static int max(int a, int b) {
        return java.lang.Math.max(a, b);
    }

    /**
     * Returns the closest long to the argument, with ties rounding to positive infinity.
     *
     * @param val a floating-point value to be rounded to a long.
     * @return the value of the argument rounded to the nearest long value.
     */
    public static long round(double val) {
        return java.lang.Math.round(val);
    }

    /**
     * Returns the closest int to the argument, with ties rounding to positive infinity.
     *
     * @param val a floating-point value to be rounded to a long.
     * @return the value of the argument rounded to the nearest int value.
     */
    public static int round(float val) {
        return java.lang.Math.round(val);
    }

    /**
     * See {@link java.lang.Math#pow(double, double)}
     *
     * @param a a
     * @param b b
     * @return return
     */
    public static double pow(double a, double b) {
        return java.lang.Math.pow(a, b);
    }
}

