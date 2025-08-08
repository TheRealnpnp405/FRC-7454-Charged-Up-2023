package frc.robot;

import com.ctre.phoenix.sensors.Pigeon2.AxisDirection;
import com.revrobotics.CANSparkMax.IdleMode;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;
import frc.lib.config.SwerveModuleConstants;

public final class Constants {
  
  /* Controller Constants */
  public static class CtrlConstants {
    public static final int DRIVER_CTRL_PORT = 0;
    public static final int OPERATOR_CTRL_PORT = 1;
  }

  /* LED Constants */
  public static class LEDConstants {
    // Plug into Roborio PWM Port 0
    public static final int LED_PWM_PORT = 0;  
    // Number of lights we will be lighting up    
    public static final int LED_PWM_COUNT = 278;
  }

  /* Arm Constants */  
  public static class ArmConstants {
    public static final int BICEP_MOTOR_ID = 10;
    public static final int WRIST_MOTOR_ID = 11;
    public static final int GRIP_MOTOR_ID  = 12;

    // presets
    public static final double HOME_BICEP        =    0;
    public static final double HOME_WRIST        =    0;
    public static final double HOME_GRIP         =    0;
    public static final double INTAKE_BICEP      =    0;
    public static final double INTAKE_WRIST      =  60;
    public static final double INTAKE_GRIP_OPEN  =  -40;
    public static final double BOT_BICEP         =    0;
    public static final double BOT_WRIST         =  40;
    public static final double MID_BICEP         = -156;
    public static final double MID_WRIST         =  108;    
    public static final double TOP_BICEP         = -167;
    public static final double TOP_WRIST         =  108;
    public static final double SHELF_BICEP       = -163;
    public static final double SHELF_WRIST       = 120;     
    public static final double SHELF_GRIP_OPEN   =    0;
    // BICEP_WATCH is used in presets.  Will not move the wrist until the bicep has reached this height. 
    public static final double BICEP_WATCH       =  -20; // for presets, we move the bicep to this height before moving the wrist
    //BICEP_WAIT_HEIGHT is used in Auto and in the presets in teleopmovearm.java
    public static final double BICEP_WAIT_HEIGHT =  -50; // the height we move the bicep to before moving the wrist.  
    // speeds
    public static final double BICEP_UP_SPEED    =  -0.85; // negative 
    public static final double BICEP_DOWN_SPEED  =   0.8; 
    public static final double WRIST_IN_SPEED    =   0.85;
    public static final double WRIST_OUT_SPEED   =  -0.6; // negative
    public static final double GRIP_CLOSE_SPEED  =   0.75;
    public static final double GRIP_OPEN_SPEED   =  -0.75; // negative

    // tolerances
    public static final double BICEP_TOLERANCE   =  3; //BASE 3
    public static final double WRIST_TOLERANCE   =  2; //BASE 2
    public static final double GRIP_TOLERANCE    =  3; //BASE 3

    //Current Limits
    public static final int BICEP_CURRENT_LIMIT =  60; 
    public static final int WRIST_CURRENT_LIMIT =  20; 
    public static final int GRIP_CURRENT_LIMIT  =  60; 
    
    // These are not used
    //public static final double FINE_TUNE_SPEED = 0.05;         //speed of arm fine tuning
    //public static final double COARSE_TUNE_SPEED = 0.4;        //speed of arm coarse tuning
    //public static final double FINE_TUNE_BICEP_WRIST_START_VAL = 0; //start encoder value for wrist and bicep fine tuning
    //public static final double FINE_TUNE_GRIP_START_VAL = 5;   //start encoder value for grip fine tuning
    //public static final double MOTOR_TOLERANCE = 3;            //tolerance allowed on motors        

  }

  public static class Gyro 
  {
      public static final int PIGEON_ID = 25;
      public static final AxisDirection FORWARD_AXIS = AxisDirection.PositiveX;
      public static final AxisDirection UP_AXIS = AxisDirection.PositiveZ;
      public static final double RESET_GYRO_DELAY = 0.1;
      public static final String PIGEON_CAN_BUS = "rio";
  }

  public static final class Swerve {
    public static final double stickDeadband = 0.1;
    public static final int pigeonID = 25;
    // Always ensure Gyro is CCW+ CW-
    public static final boolean invertGyro = false; 

    /* Drivetrain Constants */
    // 7454 Center to Center of left and right mods, measured off encoder centers
    public static final double trackWidth = Units.inchesToMeters(21.8);
    // 7454 Center to Center of front to back mods, measured off encoder centers
    public static final double wheelBase = Units.inchesToMeters(21.8); 
    public static final double wheelDiameter = Units.inchesToMeters(4.0);
    public static final double wheelCircumference = wheelDiameter * Math.PI;
    public static final double openLoopRamp = 0.25;
    public static final double closedLoopRamp = 0.0;
    // 7454 MK4i Level 1 
    public static final double driveGearRatio = (8.14 / 1.0); 
    // 7454 MK4i Level 1 
    public static final double angleGearRatio = ((150.0 / 7.0) / 1.0); 
    public static final double AUTONOMOUS_MAX_VELOCITY = 2.0;
    public static final double AUTONOMOUS_MAX_ACCELERATION = 1.0;
    

    public static final SwerveDriveKinematics swerveKinematics =
        new SwerveDriveKinematics(
            new Translation2d(wheelBase / 2.0, trackWidth / 2.0),
            new Translation2d(wheelBase / 2.0, -trackWidth / 2.0),
            new Translation2d(-wheelBase / 2.0, trackWidth / 2.0),
            new Translation2d(-wheelBase / 2.0, -trackWidth / 2.0));

    /* Swerve Voltage Compensation */
    public static final double voltageComp = 12.0;

    /* Swerve Current Limiting */
    public static final int angleContinuousCurrentLimit = 20;
    public static final int driveContinuousCurrentLimit = 80;

    /* Angle Motor PID Values */
    public static final double angleKP = 0.03; // MK4i Level 1 
    public static final double angleKI = 0.0;
    public static final double angleKD = 0.0;
    public static final double angleKFF = 0.0;

    /* Drive Motor PID Values */
    // This must be tuned to specific robot
    public static final double driveKP = 0.1; 
    public static final double driveKI = 0.0;
    public static final double driveKD = 0.0;
    public static final double driveKFF = 0.0;

    /* Drive Motor Characterization Values 
     * Divide SYSID values by 12 to convert from volts to 
     * percent output for CTRE
     */ 
    // This must be tuned to specific robot        
    public static final double driveKS = 0.667;  
    public static final double driveKV = 2.44;
    public static final double driveKA = 0.27;

    /* Drive Motor Conversion Factors */
    public static final double driveConversionPositionFactor =
        (wheelDiameter * Math.PI) / driveGearRatio;
    public static final double driveConversionVelocityFactor = 
        driveConversionPositionFactor / 60.0;
    public static final double angleConversionFactor = 
        360.0 / angleGearRatio;

    /* Swerve Profiling Values */
    // This must be tuned to specific robot    
    public static final double maxSpeed = 4.5; // meters per second
    public static final double maxAngularVelocity = 11.5;

    /* Neutral Modes */
    public static final IdleMode angleNeutralMode = IdleMode.kBrake;
    public static final IdleMode driveNeutralMode = IdleMode.kBrake;

    /* Motor Inverts */
    public static final boolean driveInvert = false;
    public static final boolean angleInvert = true; // 7454 MK4i Level 1 

    /* Angle Encoder Invert */
    public static final boolean canCoderInvert = false;

    /* Module Specific Constants */
    /* Front Left Module - Module 0 */ //7454: done
    public static final class Mod0 {
      public static final int driveMotorID = 2;
      public static final int angleMotorID = 1;
      public static final int canCoderID = 21;
      public static final Rotation2d angleOffset = 
          Rotation2d.fromDegrees(189.75585);
      public static final SwerveModuleConstants constants =
          new SwerveModuleConstants(
            driveMotorID, angleMotorID, canCoderID, angleOffset);
    }

    /* Front Right Module - Module 1 */ //7454: done
    public static final class Mod1 {
      public static final int driveMotorID = 4;
      public static final int angleMotorID = 3;
      public static final int canCoderID = 22;
      public static final Rotation2d angleOffset = 
          Rotation2d.fromDegrees(172.96875);
      public static final SwerveModuleConstants constants =
          new SwerveModuleConstants(
            driveMotorID, angleMotorID, canCoderID, angleOffset);
    }

    /* Back Left Module - Module 2 */ //7454: done
    public static final class Mod2 {
      public static final int driveMotorID = 8;
      public static final int angleMotorID = 7;
      public static final int canCoderID = 24;
      public static final Rotation2d angleOffset = 
          Rotation2d.fromDegrees(191.51367);
      public static final SwerveModuleConstants constants =
          new SwerveModuleConstants(
            driveMotorID, angleMotorID, canCoderID, angleOffset);
    }

    /* Back Right Module - Module 3 */ //7454: done
    public static final class Mod3 {
      public static final int driveMotorID = 6;
      public static final int angleMotorID = 5;
      public static final int canCoderID = 23;
      public static final Rotation2d angleOffset = 
          Rotation2d.fromDegrees(214.89257);
      public static final SwerveModuleConstants constants =
          new SwerveModuleConstants(
            driveMotorID, angleMotorID, canCoderID, angleOffset);
    }
  }

  /* The below constants are used in the example auto, and must be 
   * tuned to specific robot
   */
  public static final class AutoConstants {  
    public static final double kMaxSpeedMetersPerSecond = 3;
    public static final double kMaxAccelerationMetersPerSecondSquared = 3;
    public static final double kMaxAngularSpeedRadiansPerSecond = Math.PI;
    public static final double kMaxAngularSpeedRadiansPerSecondSqd = Math.PI;

    public static final double kPXController = 1;
    public static final double kPYController = 1;
    public static final double kPThetaController = 1;

    // Constraint for the motion profilied robot angle controller
    public static final TrapezoidProfile.Constraints 
        kThetaControllerConstraints =
            new TrapezoidProfile.Constraints(
                kMaxAngularSpeedRadiansPerSecond, 
                kMaxAngularSpeedRadiansPerSecondSqd);
  }

}
