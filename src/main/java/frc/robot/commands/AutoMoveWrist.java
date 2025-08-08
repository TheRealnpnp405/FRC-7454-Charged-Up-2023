package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arm;

public class AutoMoveWrist extends CommandBase {

    private Arm m_Arm;
    private double targetPosition;

    private double slowValue = 0;
    private double fastSpeed = .6;
    private double slowSpeed = .2;
    private boolean goingUp = false;

    public AutoMoveWrist(Arm m_Arm,double targetPosition) {
        this.m_Arm=m_Arm;
        this.targetPosition=targetPosition;

    }
    @Override 
    public boolean isFinished() {
        if ((goingUp && m_Arm.m_arm_wrist.getEncoder().getPosition() > targetPosition) || 
                (!goingUp && m_Arm.m_arm_wrist.getEncoder().getPosition() < targetPosition )) {

            m_Arm.m_arm_wrist.stopMotor();
            return true;
        }
        return false;
    }

    @Override
    public void execute() {
        //public CommandBase smoothBicep(double targetPosition) {
        double bicepPaddingValue = 10;
        // move fast                    
        // decide to go forward or backward from current vs. target
        // Note Bicep is inverted, so we need to invert when copied
        if(targetPosition > m_Arm.m_arm_wrist.getEncoder().getPosition()) {
            goingUp = true;
            slowValue = targetPosition - bicepPaddingValue;
            if (m_Arm.m_arm_wrist.getEncoder().getPosition() < slowValue) {
                if(m_Arm.m_arm_wrist.get() != fastSpeed)
                    m_Arm.m_arm_wrist.set(fastSpeed);
            }   
            else if (m_Arm.m_arm_wrist.getEncoder().getPosition() < targetPosition) {
                m_Arm.m_arm_wrist.set(slowSpeed);
            }                                                   
        } 
        else if (!goingUp) {
            goingUp = false;
            slowValue = targetPosition + bicepPaddingValue;
            if (m_Arm.m_arm_wrist.getEncoder().getPosition() > slowValue) {
                m_Arm.m_arm_wrist.set(fastSpeed*-1);
            }     
            else if (m_Arm.m_arm_wrist.getEncoder().getPosition() > targetPosition) {
                m_Arm.m_arm_wrist.set(slowSpeed*-1);
            }                         
        }
    }

    
}
