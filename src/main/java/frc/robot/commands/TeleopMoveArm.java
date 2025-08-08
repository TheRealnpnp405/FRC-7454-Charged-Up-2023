
package frc.robot.commands;
//import edu.wpi.first.networktables.GenericEntry;
//import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
//import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
//import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
//import javax.print.event.PrintEvent;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Arm;
import frc.robot.Constants;

public class TeleopMoveArm extends CommandBase  {

    private Arm m_Arm;
    private CommandXboxController driverController;
    private CommandXboxController operatorController;
    private double stickDeadband = 0.2;

    private boolean OperatorActive;

    public TeleopMoveArm(
            Arm m_Arm,
            CommandXboxController xboxController,
            CommandXboxController operatorController) {
        this.m_Arm = m_Arm;
        this.driverController=xboxController;
        this.operatorController=operatorController;
        addRequirements(m_Arm);
    }

    //private ShuffleboardTab armTab = Shuffleboard.getTab("Arm");
    //private GenericEntry intakeGrip = armTab.add("INTAKE_GRIP",Constants.ArmConstants.INTAKE_GRIP).withWidget(BuiltInWidgets.kNumberSlider).getEntry();
    
    @Override
    public void execute() {

        if (operatorController.getLeftY() > 0.2 || operatorController.getLeftY() < -0.2 || operatorController.getRightY() > 0.2 || operatorController.getRightY() < -0.2 ) {
            OperatorActive = true;
        } else {
            OperatorActive = false;
        }

        // Bottom
        if (driverController.a().getAsBoolean() || operatorController.a().getAsBoolean())  { 
            m_Arm.ArmAutoPresets(Constants.ArmConstants.BOT_BICEP, Constants.ArmConstants.BOT_WRIST, 0.0, false);
        } 

        // Intake
        else if (driverController.b().getAsBoolean() || operatorController.b().getAsBoolean()) { 
            m_Arm.ArmAutoPresets(
                Constants.ArmConstants.INTAKE_BICEP,
                Constants.ArmConstants.INTAKE_WRIST,
                Constants.ArmConstants.INTAKE_GRIP_OPEN, 

                true);
        } 

        // Middle
        else if (driverController.x().getAsBoolean() || operatorController.x().getAsBoolean()) { 
            if(m_Arm.BicepPos() > Constants.ArmConstants.BICEP_WATCH)
                m_Arm.ArmAutoPresets(
                    Constants.ArmConstants.BICEP_WAIT_HEIGHT, m_Arm.WristPos(), 0.0, false);
            else
                m_Arm.ArmAutoPresets(Constants.ArmConstants.MID_BICEP, Constants.ArmConstants.MID_WRIST, 0.0, false);
        } 

        // Top
        else if (driverController.y().getAsBoolean() || operatorController.y().getAsBoolean()) { 
            if(m_Arm.BicepPos() > Constants.ArmConstants.BICEP_WATCH)
                m_Arm.ArmAutoPresets(
                    Constants.ArmConstants.BICEP_WAIT_HEIGHT, m_Arm.WristPos(), 0.0, false);
            else
                m_Arm.ArmAutoPresets(Constants.ArmConstants.TOP_BICEP, Constants.ArmConstants.TOP_WRIST, 0.0, false);
        } 

        // Home
        else if (driverController.leftBumper().getAsBoolean() || 
        operatorController.leftBumper().getAsBoolean()) { 
            m_Arm.ArmAutoPresets(Constants.ArmConstants.HOME_BICEP, Constants.ArmConstants.HOME_WRIST, 0.0, false);
        } 

        // Shelf
        else if (driverController.rightBumper().getAsBoolean() || 
        operatorController.rightBumper().getAsBoolean()) { 
            m_Arm.ArmAutoPresets(Constants.ArmConstants.SHELF_BICEP, Constants.ArmConstants.SHELF_WRIST, Constants.ArmConstants.SHELF_GRIP_OPEN, false);
        }

        /*
         * DRIVER MANUAL CONTROLS
         */ 

        // Bicep code

        else if ((driverController.povDown().getAsBoolean() || (operatorController.getLeftY() > stickDeadband)) && m_Arm.m_arm_bicep.getEncoder().getPosition() < 0) {
            if (OperatorActive) {
                m_Arm.m_arm_bicep.set(operatorController.getLeftY());
            } else {
            m_Arm.m_arm_bicep.set(Constants.ArmConstants.BICEP_DOWN_SPEED); // DOWN
        }
    }
        else if (driverController.povUp().getAsBoolean() || (operatorController.getLeftY() < (stickDeadband * -1))) {
            if (OperatorActive) {
                m_Arm.m_arm_bicep.set(operatorController.getLeftY());
            } else {
             m_Arm.m_arm_bicep.set(Constants.ArmConstants.BICEP_UP_SPEED); // UP
        }
    }

        // Wrist code
        else if (driverController.povRight().getAsBoolean() || (operatorController.getRightY() > stickDeadband)) {
            if (OperatorActive) {
                System.out.println("Is in FWD");
                m_Arm.m_arm_wrist.set(-operatorController.getRightY());
            } else {
                System.out.println("Failed QwQ");
            m_Arm.m_arm_wrist.set(Constants.ArmConstants.WRIST_OUT_SPEED); // FWD
            }
        }
        else if ((driverController.povLeft().getAsBoolean() || (operatorController.getRightY() < (stickDeadband * -1))) && (m_Arm.m_arm_wrist.getEncoder().getPosition() > 0)) {
            if (OperatorActive) {
                System.out.println("Is in REV"); 
                m_Arm.m_arm_wrist.set(-operatorController.getRightY());
            } else {
                System.out.println("Failed QwQ");
            m_Arm.m_arm_wrist.set(Constants.ArmConstants.WRIST_IN_SPEED); // REV
        }
    }

        // Grip code
        else if (driverController.rightTrigger().getAsBoolean() || operatorController.rightTrigger().getAsBoolean()) {
            m_Arm.m_arm_grip.set(Constants.ArmConstants.GRIP_CLOSE_SPEED); // GRIP ON
        }
        else if (driverController.leftTrigger().getAsBoolean() || operatorController.leftTrigger().getAsBoolean()) {
            m_Arm.m_arm_grip.set(Constants.ArmConstants.GRIP_OPEN_SPEED); // GRIP OFF
        } 
        else {
            m_Arm.m_arm_bicep.stopMotor();
            m_Arm.m_arm_grip.stopMotor();
            m_Arm.m_arm_wrist.stopMotor();
        }        

    }

}
