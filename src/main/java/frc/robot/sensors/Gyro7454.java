package frc.robot.sensors;

import java.lang.invoke.MethodHandles;
import com.ctre.phoenix.sensors.WPI_Pigeon2;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants;

public class Gyro7454 extends SensorBase
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = 
        MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    public enum ResetState
    {
        kStart, kTry, kDone;
    }

    private class PeriodicIO
    {
        // Inputs
        private double yaw;
        private double pitch;
        private double roll;
        private Rotation2d rotation2d;

        // Outputs
    }

    private final WPI_Pigeon2 gyro = new WPI_Pigeon2(
        Constants.Gyro.PIGEON_ID, Constants.Gyro.PIGEON_CAN_BUS);
    private ResetState resetState = ResetState.kDone;
    private Timer timer = new Timer();

    private final PeriodicIO periodicIO = new PeriodicIO();

    public Gyro7454() {
        //reset();
        initPigeon();
        // periodicIO.angle = gyro.getYaw();
        periodicIO.rotation2d = gyro.getRotation2d();
    }

    public void initPigeon() {
        gyro.configFactoryDefault();
        // Forward axis and up axis
        gyro.configMountPose(
            Constants.Gyro.FORWARD_AXIS, Constants.Gyro.UP_AXIS); 
        gyro.reset();
        Timer.delay(0.5);
        gyro.setYaw(0.0);  // start with front facong driver
        Timer.delay(0.5);
    }

    public void reset() {
        resetState = ResetState.kStart;
        // gyro.reset();
    }


    public double getRoll()
    {
        return periodicIO.pitch + 1.5; // x-axis 7454 Our pigeon is mounted on the side and not level, sketchy fix
    }



    public double getPitch()
    {
        return periodicIO.roll + 3.5; // x-axis 7454 Our pigeon is mounted on the side and not level, sketchy fix
    }

    public double getYaw() {
        return periodicIO.yaw; // z-axis
    }

    public void setYaw(double angleDeg) {
        gyro.setYaw(angleDeg);
    }    

    public Rotation2d getRotation2d() {
        return periodicIO.rotation2d;
        // return gyro.getRotation2d();
    }

    @Override
    public synchronized void readPeriodicInputs() {
        if (resetState == ResetState.kDone) {
            periodicIO.yaw = gyro.getYaw();
            periodicIO.pitch = gyro.getPitch();
            periodicIO.roll = gyro.getRoll();
            periodicIO.rotation2d = gyro.getRotation2d();
        }
    }

    @Override
    public synchronized void writePeriodicOutputs()
    {
        if(resetState == ResetState.kStart)
        {
            gyro.reset();
            timer.reset();
            timer.start();
            gyro.setYaw(180.0); //7454 ALD changed from 180.0
            resetState = ResetState.kTry;
        }
        else if (resetState == ResetState.kTry && 
                timer.hasElapsed(Constants.Gyro.RESET_GYRO_DELAY))
            resetState = ResetState.kDone;

        // System.out.println(periodicIO.angle + "   " + periodicIO.rotation2d.getDegrees());
    }

    @Override
    public String toString()
    {
        return String.format("Gyro %f \n", periodicIO.yaw);
    }

    
}
