package frc.robot.shuffleboard;

import java.lang.invoke.MethodHandles;
import frc.robot.RobotContainer;

public class MainShufflebrd {
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS & INSTANCE VARIABLES ***

    private boolean useSensorTab = true;
    private boolean useArmTab = true;

    public final SensorTab sensorTab;
    public final ArmTab armTab;

    // *** CLASS CONSTRUCTOR ***
    public MainShufflebrd(RobotContainer robotContainer) {
        System.out.println(fullClassName + " : Constructor Started");
        //boolean useFullRobot = robotContainer.fullRobot;
        sensorTab = (useSensorTab) ? new SensorTab(robotContainer.gyro) : null;
        armTab = (useArmTab) ? new ArmTab(robotContainer.s_Arm) : null;
       // cameraTab = new CameraTab();
        System.out.println(fullClassName + ": Constructor Finished");
    }

    // *** CLASS & INSTANCE METHODS ***
    // -------------------------------------------------------------------//
    // SENSOR TAB
    public void setSensors() {
        if (sensorTab != null)
            sensorTab.updateEncoderData();
    }
}
