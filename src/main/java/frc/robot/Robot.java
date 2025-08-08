// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.revrobotics.REVPhysicsSim;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.lib.config.CTREConfigs;
import frc.robot.commands.TeleopMoveArm;
import frc.robot.commands.TeleopSwerve;
import frc.robot.commands.BackBalance;
import frc.robot.commands.MoveArmJoint;
import frc.robot.commands.OverBalance;
import edu.wpi.first.wpilibj.DriverStation;

import static frc.robot.Constants.ArmConstants.*;
import static frc.robot.commands.MoveArmJoint.ArmPart.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the project.
 */
public class Robot extends TimedRobot {
  public static CTREConfigs ctreConfigs;
  private Command m_autonomousCommand;
  private RobotContainer m_robCtr;

  // This is called in robotinit and autonomous
  SendableChooser<String> autonomousPlannedPath = new SendableChooser<>();

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    SmartDashboard.putData(CommandScheduler.getInstance());
    ctreConfigs = new CTREConfigs();

    // Place Path selection on Shuffleboard

    // autonomousPlannedPath.addOption("Straight8", "Straight8");
    autonomousPlannedPath.addOption("Bump8pt", "Bump8pt");
    autonomousPlannedPath.addOption("Open8pt", "Open8pt");
    // autonomousPlannedPath.addOption("Open12pt", "Open12pt");
    autonomousPlannedPath.addOption("Middle18pt", "Middle18pt");
    autonomousPlannedPath.addOption("Middle20pt", "Middle20pt");

    SmartDashboard.putData("Autonomous routine", autonomousPlannedPath);

    // Instantiate our RobotContainer. This performs all button bindings, ...
    m_robCtr = new RobotContainer();
    // Needed to simulate arm code
    //REVPhysicsSim.getInstance().addSparkMax(m_robCtr.s_Arm.m_arm_bicep, DCMotor.getNeo550(1));
    //REVPhysicsSim.getInstance().addSparkMax(m_robCtr.s_Arm.m_arm_wrist, DCMotor.getNeo550(1));
    //REVPhysicsSim.getInstance().addSparkMax(m_robCtr.s_Arm.m_arm_grip, DCMotor.getNeo550(1));

    // Init Limelight helpers
    /*
     * LimelightHelpers.LimelightResults llresults =
     * LimelightHelpers.getLatestResults("");
     */

  }

  /**
   * This function is called every robot packet, no matter the mode. Use this
   * for items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test. This runs after the mode specific periodic
   * functions, but before LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    /**
     * Runs the Scheduler. This is responsible for polling buttons, adding
     * newly-scheduled commands, running already-scheduled commands, removing
     * finished or interrupted commands, and running subsystem periodic()
     * methods. This must be called from the robot's periodic block in order
     * for anything in the Command-based framework to work.
     */
    PeriodicIO.readInputs();
    CommandScheduler.getInstance().run();
    PeriodicIO.writeOutputs();
    CommandScheduler.getInstance().onCommandInitialize(
        tCommand -> SmartDashboard.putString("Command Init", tCommand.getName()));
    CommandScheduler.getInstance().onCommandInterrupt(
        tCommand -> SmartDashboard.putString("Command Interrupted", tCommand.getName()));
    CommandScheduler.getInstance().onCommandFinish(
        tCommand -> SmartDashboard.putString("Command Finished", tCommand.getName()));
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  @Override
  public void disabledExit() {
  }

  /**
   * This autonomous runs the autonomous command selected by your
   * {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
    if (m_robCtr.s_Arm != null) {
      String autonomousPath = autonomousPlannedPath.getSelected();
      if (autonomousPath != null) {
        //m_robCtr.s_Arm.ArmAutoPresets2(Constants.ArmConstants.BICEP_WAIT_HEIGHT,m_robCtr.s_Arm.WristPos(), m_robCtr.s_Arm.GripPos(), false);
        //m_robCtr.s_Arm.ArmAutoPresets2(Constants.ArmConstants.MID_BICEP,Constants.ArmConstants.MID_WRIST, m_robCtr.s_Arm.GripPos(), false);
        //m_robCtr.s_Arm.ArmAutoPresets2(m_robCtr.s_Arm.BicepPos(),
        //m_robCtr.s_Arm.WristPos(), Constants.ArmConstants.INTAKE_GRIP , true);
        //m_robCtr.s_Arm.ArmAutoPresets2(0, 0, m_robCtr.s_Arm.GripPos(), false);

        /*
        (new AutoMoveBicep(m_robCtr.s_Arm, -60)).
        andThen((new AutoMoveWrist(m_robCtr.s_Arm, 45))).
          alongWith(new AutoMoveBicep(m_robCtr.s_Arm, -147)).
          andThen((new AutoMoveWrist(m_robCtr.s_Arm, 117))).
          schedule();
        */
        
        PathPlannerTrajectory autoPath = PathPlanner.loadPath(autonomousPath, new PathConstraints(
            Constants.Swerve.AUTONOMOUS_MAX_VELOCITY, Constants.Swerve.AUTONOMOUS_MAX_ACCELERATION));
    

        if (autonomousPath == "Middle18pt") {
          // uncomment to drive only
          //m_autonomousCommand = (new BackBalance(m_robCtr.s_Swerve));
          m_autonomousCommand = new MoveArmJoint(m_robCtr.s_Arm, BICEP, BICEP_WAIT_HEIGHT).
          andThen((new MoveArmJoint(m_robCtr.s_Arm, BICEP, MID_BICEP)).
            alongWith(new MoveArmJoint(m_robCtr.s_Arm, WRIST, MID_WRIST))).
          andThen(new MoveArmJoint(m_robCtr.s_Arm, CLAW, INTAKE_GRIP_OPEN)).          
          andThen((new MoveArmJoint(m_robCtr.s_Arm, BICEP, 0)).
            alongWith(new MoveArmJoint(m_robCtr.s_Arm, WRIST, 0))).
          andThen(new BackBalance(m_robCtr.s_Swerve))   // comment this out to arm only
          ;  
        } 

        else if (autonomousPath == "Middle20pt") {
          // uncomment to drive only          
           //m_autonomousCommand = (new OverBalance(m_robCtr.s_Swerve));
          m_autonomousCommand = new MoveArmJoint(m_robCtr.s_Arm, BICEP, BICEP_WAIT_HEIGHT).
            andThen((new MoveArmJoint(m_robCtr.s_Arm, BICEP, MID_BICEP)).
            alongWith(new MoveArmJoint(m_robCtr.s_Arm, WRIST, MID_WRIST))).
            andThen(new MoveArmJoint(m_robCtr.s_Arm, CLAW, INTAKE_GRIP_OPEN)).
            andThen((new OverBalance(m_robCtr.s_Swerve)).  // comment this out to arm only
            alongWith(new MoveArmJoint(m_robCtr.s_Arm, BICEP, 0)).
            alongWith(new MoveArmJoint(m_robCtr.s_Arm, WRIST, 0)));
            // m_autonomousCommand = new MoveArmJoint(m_robCtr.s_Arm, BICEP, BICEP_WAIT_HEIGHT).
            // andThen((new MoveArmJoint(m_robCtr.s_Arm, BICEP, MID_BICEP)).
            //   alongWith(new MoveArmJoint(m_robCtr.s_Arm, WRIST, MID_WRIST))).
            // andThen(new MoveArmJoint(m_robCtr.s_Arm, CLAW, INTAKE_GRIP_OPEN)).          
            // andThen((new MoveArmJoint(m_robCtr.s_Arm, BICEP, 0)).
            //   alongWith(new MoveArmJoint(m_robCtr.s_Arm, WRIST, 0))).
            // andThen(new OverBalance(m_robCtr.s_Swerve))   // comment this out to arm only          
            // ;
        }         
        else {
          // uncomment to drive only 
          //m_autonomousCommand = m_robCtr.s_Swerve.FollowTrajectoryCommand(autoPath, true);
          m_autonomousCommand = new MoveArmJoint(m_robCtr.s_Arm, BICEP, BICEP_WAIT_HEIGHT).
          andThen((new MoveArmJoint(m_robCtr.s_Arm, BICEP, MID_BICEP)).
          alongWith(new MoveArmJoint(m_robCtr.s_Arm, WRIST, MID_WRIST))).
          andThen(new MoveArmJoint(m_robCtr.s_Arm, CLAW, INTAKE_GRIP_OPEN)).
          andThen((m_robCtr.s_Swerve.FollowTrajectoryCommand(autoPath, true)).  // comment this out to arm only
          alongWith(new MoveArmJoint(m_robCtr.s_Arm, BICEP, 0)).
          alongWith(new MoveArmJoint(m_robCtr.s_Arm, WRIST, 0))).
          andThen(new MoveArmJoint(m_robCtr.s_Arm, WRIST, Constants.ArmConstants.INTAKE_WRIST));  
        }
      }
      if (m_autonomousCommand != null) {
        m_autonomousCommand.schedule();
      }
    }
  }

  @Override
  public void autonomousPeriodic() {
    if (m_robCtr.mainShuffleboard != null && m_robCtr.mainShuffleboard.sensorTab != null) {
      m_robCtr.mainShuffleboard.sensorTab.updateEncoderData();
      m_robCtr.mainShuffleboard.armTab.updateEncoderData();
    }
    SmartDashboard.putData(CommandScheduler.getInstance());
    //REVPhysicsSim.getInstance().run(); // arm sim
  }

  @Override
  public void autonomousExit() {
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    new TeleopMoveArm(
        m_robCtr.s_Arm,
        m_robCtr.m_driverController,
        m_robCtr.m_operatorController).schedule();

    new TeleopSwerve(
        m_robCtr.s_Swerve,
        () -> -m_robCtr.m_driverController.getRawAxis(m_robCtr.translationAxis),
        () -> -m_robCtr.m_driverController.getRawAxis(m_robCtr.strafeAxis),
        () -> -m_robCtr.m_driverController.getRawAxis(m_robCtr.rotationAxis),
        () -> false).schedule();
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    if (!DriverStation.isFMSAttached()) {
      if (m_robCtr.mainShuffleboard != null && m_robCtr.mainShuffleboard.sensorTab != null
          && m_robCtr.mainShuffleboard.armTab != null) {
        m_robCtr.mainShuffleboard.sensorTab.updateEncoderData();
        m_robCtr.mainShuffleboard.armTab.updateEncoderData();
      }
    }
  }

  @Override
  public void teleopExit() {
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
  }

  @Override
  public void testExit() {
  }

  @Override
  public void simulationPeriodic() {
    m_robCtr.s_Swerve.simulationPeriodic();
    m_robCtr.s_Arm.simulationPeriodic();
    REVPhysicsSim.getInstance().run();
  }
}