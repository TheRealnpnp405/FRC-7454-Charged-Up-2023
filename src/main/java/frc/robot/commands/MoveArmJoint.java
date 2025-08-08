package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arm;
import static frc.robot.Constants.ArmConstants.WRIST_TOLERANCE;
import static frc.robot.Constants.ArmConstants.WRIST_OUT_SPEED;
import static frc.robot.Constants.ArmConstants.WRIST_IN_SPEED;
import static frc.robot.Constants.ArmConstants.BICEP_TOLERANCE;
import static frc.robot.Constants.ArmConstants.BICEP_UP_SPEED;
import static frc.robot.Constants.ArmConstants.BICEP_DOWN_SPEED;
import static frc.robot.Constants.ArmConstants.GRIP_TOLERANCE;
import static frc.robot.Constants.ArmConstants.GRIP_OPEN_SPEED;
import static frc.robot.Constants.ArmConstants.GRIP_CLOSE_SPEED;

import java.lang.invoke.MethodHandles;

public class MoveArmJoint extends CommandBase {

    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName); 
    }

    private final Arm arm;

    private boolean WristRunning=false;
    private boolean BicepRunning=false;
    private boolean GripRunning=false;
    private boolean useGrip=false;

    private double Bicep=0.0;
    private double Wrist=0.0;
    private double Grip=0.0;

    public enum ArmPart {
        BICEP, WRIST, CLAW
    }

    /**
    * Robot Arm Autonomous Movement
    * Activates motors on various arm joints until passed in encoder values are reached.
    *
    * @param arm Arm subsytem.
    * @param part ArmPart optional joint motor requested for movement.
    * @param encoderValue Double when joint part passed, encoder limit for movement.
    * @param Bicep Double encoder value for bicep motor.
    * @param Wrist Double encoder value for wrist motor.
    * @param Grip Double encoder value for grip motor.
    * @param useGrip Boolean flag for whether to move the grip or not
    */
    public MoveArmJoint(
            Arm arm, 
            ArmPart part, Double encoderValue, 
            Double Bicep, Double Wrist, Double Grip, Boolean useGrip) {
        this.arm=arm;
        this.Bicep=arm.BicepPos(); this.Wrist=arm.WristPos(); this.Grip=arm.GripPos();
        if(part==null) {
            this.BicepRunning=true;
            this.WristRunning=true;
            this.GripRunning=true;
            this.Bicep=Bicep;
            this.Wrist=Wrist;
            this.Grip=Grip;
            this.useGrip=useGrip;
        }
        else {
            switch(part) {
                case BICEP:
                    this.BicepRunning=true; this.Bicep=encoderValue; this.useGrip=false; break;
                case WRIST:
                    this.WristRunning=true; this.Wrist=encoderValue; this.useGrip=false; break;
                case CLAW:
                    this.GripRunning=true; this.Grip=encoderValue; this.useGrip=true; break;
            }
        }
    }
    public MoveArmJoint(Arm arm, double Bicep, double Wrist, double Grip, boolean useGrip) {
        this(arm, null, null, Bicep, Wrist, Grip, useGrip);
    }

    public MoveArmJoint(Arm arm, ArmPart part, double encoderValue) {
        this(arm, part, encoderValue, null, null, null, null);
    }

    // Called when the command is initially scheduled.
    @Override public void initialize() { }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
        // Wrist Section
        if (WristRunning) {
            if (Math.abs(Wrist - arm.WristPos()) > WRIST_TOLERANCE) {
                if (Wrist < arm.WristPos()) arm.m_arm_wrist.set(WRIST_OUT_SPEED);
                else if (Wrist > arm.WristPos()) arm.m_arm_wrist.set(WRIST_IN_SPEED);
            } else {
                arm.m_arm_wrist.stopMotor();
                WristRunning = false;
            }
        }

        // Bicep Section
        if (BicepRunning) {
            if (Math.abs(Bicep - arm.BicepPos()) > BICEP_TOLERANCE) {
                if (Bicep < arm.BicepPos()) arm.m_arm_bicep.set(BICEP_UP_SPEED);
                else if (Bicep > arm.BicepPos()) arm.m_arm_bicep.set(BICEP_DOWN_SPEED);
            } else {
                arm.m_arm_bicep.stopMotor();
                BicepRunning = false;
            }
        }

        // Grip Setting
        if (GripRunning) {
            if (Math.abs(Grip - arm.GripPos()) > GRIP_TOLERANCE) {
                if (Grip < arm.GripPos() & useGrip == true) arm.m_arm_grip.set(GRIP_OPEN_SPEED);
                else if (Grip > arm.GripPos() & useGrip == true) arm.m_arm_grip.set(GRIP_CLOSE_SPEED);
            } else {
                arm.m_arm_grip.stopMotor();
                GripRunning = false;
            }
        }
    }

    @Override
    public boolean isFinished() {
       if (BicepRunning || WristRunning || GripRunning) return false;
       return true;
    }

   // Called once the command ends or is interrupted.
   @Override
   public void end(boolean interrupted) { 
    if(BicepRunning) arm.m_arm_bicep.stopMotor();
    if(WristRunning) arm.m_arm_wrist.stopMotor();
    if(GripRunning) arm.m_arm_grip.stopMotor();
   }

   @Override
   public String toString() { return "AutoMoveArm(Arm,ArmPart,encoderValue,Bicep,Wrist,Grip,useGrip)"; }
    
}
