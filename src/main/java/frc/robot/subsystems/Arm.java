package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.REVPhysicsSim;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.ArmConstants;

public class Arm extends SubsystemBase {

    public CANSparkMax m_arm_bicep = new CANSparkMax(10, MotorType.kBrushless);
    public PIDController test1 = new PIDController(0, 0, 0);
    public CANSparkMax m_arm_wrist = new CANSparkMax(11, MotorType.kBrushless);
    public CANSparkMax m_arm_grip = new CANSparkMax(12, MotorType.kBrushless);

    public boolean WristRunning;
    public boolean BicepRunning;
    public boolean GripRunning;

    private RelativeEncoder encoderBicep;
    private RelativeEncoder encoderWrist;
    private RelativeEncoder encoderGrip;

// helper functions
    public double BicepPos() {
        return encoderBicep.getPosition();
    }

    public double WristPos() {
        return encoderWrist.getPosition();
    }


    public double GripPos() {
        return encoderGrip.getPosition();
    }
    public Arm() { 
        configCANSparkMax();
    }

    private void configCANSparkMax()
    {
        /* Bicep Motor Config */ 
        m_arm_bicep.restoreFactoryDefaults();    // Will want to plug into REV sofwatre to make sure 
                                                 // nothing was missed in this config
        m_arm_bicep.setInverted(false);  // THIS IS WRONG, but we are too far along. this would 
                                                     // have saved about 90 headaches
        m_arm_bicep.setIdleMode(IdleMode.kBrake);
        m_arm_bicep.setSmartCurrentLimit(ArmConstants.BICEP_CURRENT_LIMIT);
        encoderBicep = m_arm_bicep.getEncoder();
        //encoderBicep.setPositionConversionFactor(4096); // ALD - not using, but could to get more granularity 
                                                          // but would have to multiply all constants by 4096
        //Soft Limits - This motor was not inverted so this is all backasswards
        m_arm_bicep.setSoftLimit(SoftLimitDirection.kForward, (float)ArmConstants.HOME_BICEP);
        m_arm_bicep.enableSoftLimit(SoftLimitDirection.kForward, true);
        m_arm_bicep.setSoftLimit(SoftLimitDirection.kReverse, (float)(ArmConstants.TOP_BICEP - 10));
        m_arm_bicep.enableSoftLimit(SoftLimitDirection.kReverse, true);

        /* Wrist Motor Config */ 
        m_arm_wrist.restoreFactoryDefaults();  // Will want to plug into REV sofwatre to make sure 
                                               // nothing was missed in this config
        m_arm_wrist.setInverted(true);
        m_arm_wrist.setIdleMode(IdleMode.kBrake);
        m_arm_wrist.setSmartCurrentLimit(ArmConstants.WRIST_CURRENT_LIMIT);
        encoderWrist = m_arm_wrist.getEncoder();
        //encoderWrist.setPositionConversionFactor(4096); // ALD - not using, but could to get more 
                                                          // granularity but would have to multiply 
                                                          // all constants by 4096
        //Soft Limits
        m_arm_wrist.setSoftLimit(SoftLimitDirection.kForward, (float)ArmConstants.SHELF_WRIST + 10);
        m_arm_wrist.enableSoftLimit(SoftLimitDirection.kForward, true);
        m_arm_wrist.setSoftLimit(SoftLimitDirection.kReverse, (float)(ArmConstants.HOME_GRIP));
        m_arm_wrist.enableSoftLimit(SoftLimitDirection.kReverse, true);        

        /* Grip Motor Config */ 
        m_arm_grip.restoreFactoryDefaults();    // Will want to plug into REV sofwatre to make sure 
                                                // nothing was missed in this config
        m_arm_grip.setInverted(false); // THIS IS WRONG, but we are too far along.
        m_arm_grip.setIdleMode(IdleMode.kBrake);
        m_arm_grip.setSmartCurrentLimit(ArmConstants.GRIP_CURRENT_LIMIT);
        encoderGrip = m_arm_grip.getEncoder();
        //encoderWrist.setPositionConversionFactor(4096); // ALD - not using, but could to get more 
                                                          // granularity but would have to multiply 
                                                          // all constants by 4096
        //Soft Limits - This motor was not inverted so this is all backasswards
        m_arm_grip.setSoftLimit(SoftLimitDirection.kForward, (float)ArmConstants.HOME_GRIP);
        m_arm_grip.enableSoftLimit(SoftLimitDirection.kForward, true);
        m_arm_grip.setSoftLimit(SoftLimitDirection.kReverse, (float)(ArmConstants.INTAKE_GRIP_OPEN - 3));
        m_arm_grip.enableSoftLimit(SoftLimitDirection.kReverse, true);              
    }    

    public void ArmAutoPresets(double Bicep, double Wrist, double Grip, boolean useGrip) {
        // Wrist Section
        if (Math.abs(Wrist - WristPos()) > Constants.ArmConstants.WRIST_TOLERANCE) {
            if (Wrist < WristPos()) { // Wrist auto drive
                m_arm_wrist.set(Constants.ArmConstants.WRIST_OUT_SPEED);
            } 
            else if (Wrist > WristPos()) { // Wrist rev auto drive
                m_arm_wrist.set(Constants.ArmConstants.WRIST_IN_SPEED);
            }
        } else m_arm_wrist.stopMotor();

        // Bicep Section
        if (Math.abs(Bicep - BicepPos()) > Constants.ArmConstants.BICEP_TOLERANCE) {

            if (Bicep < BicepPos()) { // bicep auto drive
                m_arm_bicep.set(Constants.ArmConstants.BICEP_UP_SPEED);
            } 
            else if (Bicep > BicepPos()) { // bicep rev auto drive
                m_arm_bicep.set(Constants.ArmConstants.BICEP_DOWN_SPEED);
            }
        } else m_arm_bicep.stopMotor();

        // Grip Setting
        if (Math.abs(Grip - GripPos()) > Constants.ArmConstants.GRIP_TOLERANCE) {
            if (Grip < GripPos() & useGrip == true) { // Fwd
                m_arm_grip.set(Constants.ArmConstants.GRIP_OPEN_SPEED);
            } 
            else if (Grip > GripPos() & useGrip == true) { // Rev
                m_arm_grip.set(Constants.ArmConstants.GRIP_CLOSE_SPEED);
            }
        } else m_arm_grip.stopMotor();
    }

    // ALD IS THIS USED?
    public void ArmAutoPresets2(double Bicep, double Wrist, double Grip, boolean useGrip) {
        WristRunning = true;
        BicepRunning = true;
        GripRunning = true;
        while (BicepRunning || WristRunning || GripRunning) {
            // Wrist Section
            if (Math.abs(Wrist - WristPos()) > Constants.ArmConstants.WRIST_TOLERANCE) {
                if (Wrist < WristPos()) { // Wrist auto drive
                    m_arm_wrist.set(Constants.ArmConstants.WRIST_OUT_SPEED);
                } else if (Wrist > WristPos()) {  // Wrist rev auto drive
                    m_arm_wrist.set(Constants.ArmConstants.WRIST_IN_SPEED);
                }
            } else {
                m_arm_wrist.stopMotor();
                WristRunning = false;
            }

            // Bicep Section
            if (Math.abs(Bicep - BicepPos()) > Constants.ArmConstants.BICEP_TOLERANCE) {
                if (Bicep < BicepPos()) { // bicep auto drive
                    m_arm_bicep.set(Constants.ArmConstants.BICEP_UP_SPEED);
                } else if (Bicep > BicepPos()) { // bicep rev auto drive
                    m_arm_bicep.set(Constants.ArmConstants.BICEP_DOWN_SPEED);
                }
            } else {
                m_arm_bicep.stopMotor();
                BicepRunning = false;
            }

            // Grip Setting
            if (Math.abs(Grip - GripPos()) > Constants.ArmConstants.GRIP_TOLERANCE) {
                if (Grip < GripPos() & useGrip == true) { // Fwd
                    m_arm_grip.set(Constants.ArmConstants.GRIP_OPEN_SPEED);
                } else if (Grip > GripPos() & useGrip == true) { // Rev
                    m_arm_grip.set(Constants.ArmConstants.GRIP_CLOSE_SPEED);
                }
            } else {
                m_arm_grip.stopMotor();
                GripRunning = false;
            }
        }
    }

    public CommandBase smoothBicep(double targetPosition) {
        return new InstantCommand(
                () -> {
                    double bicepPaddingValue = 10;
                    double slowValue = 0;
                    double fastSpeed = .6;
                    double slowSpeed = .2;
                    // move fast                    
                    // decide to go forward or backward from current vs. target
                    if(targetPosition > m_arm_bicep.getEncoder().getPosition()) {  // UP
                        slowValue = targetPosition - bicepPaddingValue;

                        while (m_arm_bicep.getEncoder().getPosition() < slowValue) {
                            m_arm_bicep.set(fastSpeed);


                        }                                                   
                    } else { // DOWN
                        slowValue = targetPosition + bicepPaddingValue;
                        while (m_arm_bicep.getEncoder().getPosition() > slowValue) {
                            m_arm_bicep.set(fastSpeed*-1);
                        }     
                        while (m_arm_bicep.getEncoder().getPosition() > targetPosition) {
                            m_arm_bicep.set(slowSpeed*-1);
                        }                         
                    }
                }
                ).andThen(
                    () -> {                    
                        m_arm_bicep.stopMotor();
                    }
                );
    }

    public double BicepPosition() {
        return m_arm_bicep.getEncoder().getPosition();
    }

    public double WristPosition() {
        return m_arm_wrist.getEncoder().getPosition();
    }

    public double GripPosition() {
        return m_arm_grip.getEncoder().getPosition();
    }    
    
// Overrides
    @Override
    public void periodic() {
        REVPhysicsSim.getInstance().run();
        SmartDashboard.putNumber("Encoder Bicep", m_arm_bicep.getEncoder().getPosition());
        SmartDashboard.putNumber("Encoder Grip",  m_arm_grip.getEncoder().getPosition());
        SmartDashboard.putNumber("Encoder Wrist", m_arm_wrist.getEncoder().getPosition());
    }
}
