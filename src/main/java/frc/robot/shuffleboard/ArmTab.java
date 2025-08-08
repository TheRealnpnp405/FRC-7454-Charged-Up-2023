package frc.robot.shuffleboard;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.subsystems.Arm;

public class ArmTab {

    private ShuffleboardTab armTab = Shuffleboard.getTab("Arm");
    private Arm arm;
    private GenericEntry armBicep;
    private GenericEntry armWrist;
    private GenericEntry armGrip;

    public ArmTab(Arm a_arm) {
        this.arm = a_arm;

        // Display current position
        armBicep = armTab.add("Bicep Position:", a_arm.BicepPos())
            .withWidget(BuiltInWidgets.kTextView)
            .getEntry();
         
        armWrist = armTab.add("Wrist Position:", a_arm.WristPos())
            .withWidget(BuiltInWidgets.kTextView)
            .getEntry();

        armGrip = armTab.add("Grip Position:", a_arm.GripPos())
            .withWidget(BuiltInWidgets.kTextView)
            .getEntry();
    }
    public void updateEncoderData()
    {
        if( arm != null)
        {
            armBicep.setDouble(arm.BicepPos());
            armWrist.setDouble(arm.WristPos());
            armGrip.setDouble(arm.GripPos());
        }
    } 
    
}
