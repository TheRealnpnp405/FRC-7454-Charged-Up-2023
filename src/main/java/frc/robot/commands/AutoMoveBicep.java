package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arm;

public class AutoMoveBicep extends CommandBase {

    private Arm m_Arm;
    private double targetPosition;

    private double slowValue = 0;
    private double fastSpeed = .6;
    private double slowSpeed = .2;
    private boolean goingUp = false;

    public AutoMoveBicep(Arm m_Arm,double targetPosition) {
        this.m_Arm=m_Arm;
        this.targetPosition=targetPosition;

    }
    @Override 
    public boolean isFinished() {
        if ((goingUp && m_Arm.m_arm_bicep.getEncoder().getPosition() < targetPosition) || 
                (!goingUp && m_Arm.m_arm_bicep.getEncoder().getPosition() > targetPosition )) {

            m_Arm.m_arm_bicep.stopMotor();
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
        if(targetPosition < m_Arm.m_arm_bicep.getEncoder().getPosition()) {
            goingUp = true;
            slowValue = targetPosition + bicepPaddingValue;
            if (m_Arm.m_arm_bicep.getEncoder().getPosition() > slowValue) {
                if(m_Arm.m_arm_bicep.get() != fastSpeed *-1)
                    m_Arm.m_arm_bicep.set(fastSpeed*-1);
            }   
            else if (m_Arm.m_arm_bicep.getEncoder().getPosition() > targetPosition) {
                m_Arm.m_arm_bicep.set(slowSpeed*-1);
            }                                                   
        } 
        else if (!goingUp) {
            goingUp = false;
            slowValue = targetPosition - bicepPaddingValue;
            if (m_Arm.m_arm_bicep.getEncoder().getPosition() < slowValue) {
                m_Arm.m_arm_bicep.set(fastSpeed);
            }     
            else if (m_Arm.m_arm_bicep.getEncoder().getPosition() < targetPosition) {
                m_Arm.m_arm_bicep.set(slowSpeed);
            }                         
        }
    }
}
