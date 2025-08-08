package frc.robot.subsystems;

import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.commands.PPSwerveControllerCommand;

import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.sensors.Gyro7454;

public class Swerve extends SubsystemBase {
  private static Swerve instance = null;
  private ChassisSpeeds m_chassisSpeeds = new ChassisSpeeds(0.0, 0.0, 0.0);
  public static final TrapezoidProfile.Constraints ROT_PROFILE = new TrapezoidProfile.Constraints(
      Constants.Swerve.maxSpeed, Constants.Swerve.maxAngularVelocity);
  public static final PIDController X_PID_CONTROLLER = new PIDController(.25, 0, 0);
  public static final PIDController Y_PID_CONTROLLER = new PIDController(.25, 0, 0);
  public static final ProfiledPIDController ROT_PID_CONTROLLER = new ProfiledPIDController(2.8, 0, 0, ROT_PROFILE);
  private HolonomicDriveController follower = new HolonomicDriveController(
      X_PID_CONTROLLER,
      Y_PID_CONTROLLER,
      ROT_PID_CONTROLLER);
  private final SwerveDriveKinematics m_kinematics = Constants.Swerve.swerveKinematics;

  public static Swerve getInstance() {
    return instance;
  }

  // private final Pigeon2 gyro2;
  public Gyro7454 gyro = new Gyro7454();

  private SwerveDriveOdometry swerveOdometry;
  private SwerveModule[] mSwerveMods = new SwerveModule[] {
      new SwerveModule(0, Constants.Swerve.Mod0.constants),
      new SwerveModule(1, Constants.Swerve.Mod1.constants),
      new SwerveModule(2, Constants.Swerve.Mod2.constants),
      new SwerveModule(3, Constants.Swerve.Mod3.constants)
  };


  private Field2d field;
  public SwerveModuleState[] m_desiredStates;

  public Swerve() {
    // gyro2 = new Pigeon2(Constants.Swerve.pigeonID);
    // gyro2.configFactoryDefault();
    gyro.initPigeon();
    follower.setTolerance(new Pose2d(0, 0, new Rotation2d(Math.toRadians(0))));
    zeroGyro();

    // 7454 Added delay to give systems chance to get online,
    // then resent encoder values to offset.
    Timer.delay(1.0);
    resetModulesToAbsolute();

    swerveOdometry = new SwerveDriveOdometry(
        Constants.Swerve.swerveKinematics, getYaw(), getModulePositions());

    field = new Field2d();
    SmartDashboard.putData("Field", field);
    m_desiredStates = m_kinematics.toSwerveModuleStates(m_chassisSpeeds);
  }

  // ALD created for Gyro Balance
  public void drive(
      double xSpeed, double ySpeed, double turn, boolean fieldRelative) {
    drive(
        new Translation2d(xSpeed, ySpeed),
        turn,
        fieldRelative,
        true);
  }

  public void drive(
      Translation2d translation, double rotation,
      boolean fieldRelative, boolean isOpenLoop) {
    SwerveModuleState[] swerveModuleStates = Constants.Swerve.swerveKinematics.toSwerveModuleStates(
        fieldRelative ? ChassisSpeeds.fromFieldRelativeSpeeds(
                translation.getX(), translation.getY(), rotation, getYaw())
            : new ChassisSpeeds(translation.getX(),
                translation.getY(), rotation));
    SwerveDriveKinematics.desaturateWheelSpeeds(
        swerveModuleStates, Constants.Swerve.maxSpeed);

    for (SwerveModule mod : mSwerveMods) {
      mod.setDesiredState(swerveModuleStates[mod.moduleNumber], isOpenLoop);
    }
  }

  public void lockWheels() {
    drive(0, 0, 45, false);
  }

  /* Used by SwerveControllerCommand in Auto */
  public void setModuleStates(SwerveModuleState[] desiredStates) {
    SwerveDriveKinematics.desaturateWheelSpeeds(
        desiredStates, Constants.Swerve.maxSpeed);
    for (SwerveModule mod : mSwerveMods) {
      mod.setDesiredState(desiredStates[mod.moduleNumber], false);
    }
  }

  public Pose2d getPose() {
    return swerveOdometry.getPoseMeters();
  }

  public void resetOdometry(Pose2d pose) {
    swerveOdometry.resetPosition(getYaw(), getModulePositions(), pose);
  }

  public SwerveModuleState[] getStates() {
    SwerveModuleState[] states = new SwerveModuleState[4];
    for (SwerveModule mod : mSwerveMods) {
      states[mod.moduleNumber] = mod.getState();
    }
    return states;
  }

  public SwerveModulePosition[] getModulePositions() {
    SwerveModulePosition[] positions = new SwerveModulePosition[4];

    for (SwerveModule mod : mSwerveMods) {
      positions[mod.moduleNumber] = mod.getPosition();
    }
    return positions;
  }

  public void zeroGyro() {
    gyro.setYaw(0);
  }

  public void resetModulesToAbsolute() {
    for (SwerveModule mod : mSwerveMods) {
      mod.resetToAbsolute();
    }
  }

  public Rotation2d getYaw() {
    return (Constants.Swerve.invertGyro)
        ? Rotation2d.fromDegrees(360 - gyro.getYaw())
        : Rotation2d.fromDegrees(gyro.getYaw());
  }

  public SwerveDriveKinematics getKinemtaKinematics() {
    return m_kinematics;
  }

  /*
   * From: https://github-wiki-see.page/m/mjansen4857/
   * pathplanner/wiki/PathPlannerLib:-Java-Usage
   */
  public Command FollowTrajectoryCommand(
      PathPlannerTrajectory traj, boolean isFirstPath) {
    return new SequentialCommandGroup(
        new InstantCommand(() -> {
          // Reset odometry for the first path you run during auto
          if (isFirstPath) {
            this.resetOdometry(traj.getInitialHolonomicPose());
          }
        }),
        new PPSwerveControllerCommand(
            traj,
            this::getPose, // Pose supplier
            this.getKinemtaKinematics(), // DriveKinematics
            // X controller. Tune these values for your robot.
            // Leaving them 0 will only use feedforwards.
            new PIDController(0, 0, 0),
            // Y controller (usually the same values as X controller)
            new PIDController(0, 0, 0),
            // Rotation controller. Tune these values for your robot.
            // Leaving them 0 will only use feedforwards.
            new PIDController(0, 0, 0),
            this::setModuleStates, // Module states consumer
            // Should path be automatically mirrored depending on alliance color.
            true,
            this // Requires this drive subsystem
        ));
  }

  @Override
  public void periodic() {
    // 7454 swerveOdometry.update(getYaw(), getStates());
    swerveOdometry.update(getYaw(), getModulePositions());
    field.setRobotPose(getPose());

    for (SwerveModule mod : mSwerveMods) {
      SmartDashboard.putNumber(
          "Mod " + mod.moduleNumber + " Cancoder",
          mod.getCanCoder().getDegrees());
      SmartDashboard.putNumber(
          "Mod " + mod.moduleNumber + " Integrated",
          mod.getState().angle.getDegrees());
      SmartDashboard.putNumber(
          "Mod " + mod.moduleNumber + " Velocity",
          mod.getState().speedMetersPerSecond);
      //var t1 = mod.getPosition().distanceMeters; // Bennets test thing
    }
  }

}
