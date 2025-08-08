package frc.lib.math;

public class Conversions {

    /**
     * @param positionCounts Falcon Position Counts
     * @param circumference Circumference of Wheel
     * @param gearRatio Gear Ratio between Falcon and Wheel
     * @return Meters
     */
    public static double neoencoderToMeters(double positionCounts, double circumference, double gearRatio){
        return positionCounts * (circumference / (gearRatio * 2048.0));
    }
    
}
