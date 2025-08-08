package frc.robot.shuffleboard;

import java.lang.invoke.MethodHandles;

//import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.networktables.GenericEntry;
//import edu.wpi.first.util.datalog.DoubleLogEntry;
//import edu.wpi.first.util.sendable.SendableRegistry;
// import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
//import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;
//import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import frc.robot.RobotContainer;
import frc.robot.sensors.Gyro7454;
// import frc.robot.RobotContainer;
//import frc.robot.subsystems.Arm;
//import frc.robot.subsystems.SwerveModule;

public class SensorTab
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    private ShuffleboardTab sensorTab = Shuffleboard.getTab("Sensor");
    private Gyro7454 gyro;
    // private Double encoderValue = 0.0;
    // private GenericEntry shoulderEncoderBox;
    // private GenericEntry grabberEncoderBottomBox;
    // private GenericEntry grabberEncoderTopBox;
    // private GenericEntry grabberMotorBottomCurrentBox;
    // private GenericEntry grabberMotorTopCurrentBox;
    private GenericEntry gyroRoll;
    private GenericEntry gyroPitch;
    private GenericEntry gyroYaw;
    // private GenericEntry armEncoderBox;
    // private GenericEntry flsEncoderBox;
    // private GenericEntry frsEncoderBox;
    // private GenericEntry blsEncoderBox;
    // private GenericEntry brsEncoderBox;
    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS CONSTRUCTOR ***
    SensorTab(Gyro7454 gyro)
    {
        System.out.println(fullClassName + " : Constructor Started");

        this.gyro = gyro;
        if(gyro != null)
        {
             gyroRoll = sensorTab.add("Gyro Roll", gyro.getRoll())
            .withWidget(BuiltInWidgets.kTextView)   // specifies type of widget: "kTextView"
            //.withPosition(1, 9)  // sets position of widget
            .withSize(4, 2)    // sets size of widget
            .getEntry();

            gyroPitch = sensorTab.add("Gyro Pitch", gyro.getPitch())
            .withWidget(BuiltInWidgets.kTextView)   // specifies type of widget: "kTextView"
            //.withPosition(3, 9)  // sets position of widget
            .withSize(4, 2)    // sets size of widget
            .getEntry();

            gyroYaw = sensorTab.add("Gyro Yaw", gyro.getYaw())
            .withWidget(BuiltInWidgets.kTextView)   // specifies type of widget: "kTextView"
            //.withPosition(5, 9)  // sets position of widget
            .withSize(4, 2)    // sets size of widget
            .getEntry();


        }
        System.out.println(fullClassName + ": Constructor Finished");
    }

    public void updateEncoderData()
    {
        if(gyro != null)
        {
            gyroRoll.setDouble(gyro.getRoll());
            gyroPitch.setDouble(gyro.getPitch());
            gyroYaw.setDouble(gyro.getYaw());
        }
    }
}