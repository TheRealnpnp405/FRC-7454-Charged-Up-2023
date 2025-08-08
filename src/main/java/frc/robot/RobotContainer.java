// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.CtrlConstants;
import frc.robot.controls.DriverCtrl;
import frc.robot.controls.OperatorCtrl;
import frc.robot.sensors.Gyro7454;
import frc.robot.shuffleboard.MainShufflebrd;
import frc.robot.subsystems.*;

/**
 * This class is where the bulk of the robot should be declared. Since 
 * Command-based is a "declarative" paradigm, very little robot logic 
 * should actually be handled in the {@link Robot} periodic methods 
 * (other than the scheduler calls). Instead, the structure of the robot 
 * (including subsystems, commands, and button mappings) should be 
 * declared here.
 */
public class RobotContainer 
{
  // This string gets the full name of the class, including the package name
  private static final String fullClassName = 
    MethodHandles.lookup().lookupClass().getCanonicalName();

  // *** STATIC INITIALIZATION BLOCK ***
  // This block of code is run first when the class is loaded
  static
  {
      System.out.println("Loading: " + fullClassName);
  }
	
  /* These private booleans allow us to disable features for testing, 
   * a.k.a. "Break Points that don't Break Things"
   */
  private boolean useFullRobot = true;
  private boolean useBindings = true;
  private boolean useExampleSubsystem	= false;
  private boolean useGyro = true;
	private boolean useDriverCtrl = true;
	private boolean useOperatorCtrl = true;
	private boolean useMainShufflebrd = true;
  private boolean useLED = true;
  private boolean useArm = true;

  /* public interfaces for the items that we will enable or disable using the 
   * booleans above
   */
	public final boolean fullRobot;  
  public final ExampleSubsystem m_exampleSubsystem;
  public final Gyro7454 gyro;
  public final DriverCtrl driverController;
  public final OperatorCtrl operatorController;
  public final LEDSubsystem m_LEDSubsystem;
  public final MainShufflebrd mainShuffleboard;
	
  //public final MainShuffleboard mainShuffleboard

  /* Controllers */
  public final CommandXboxController m_driverController = new CommandXboxController(CtrlConstants.DRIVER_CTRL_PORT);
  public final CommandXboxController m_operatorController = new CommandXboxController(CtrlConstants.OPERATOR_CTRL_PORT);

  /* Drive Controls */
  public final int translationAxis = XboxController.Axis.kLeftY.value;
  public final int strafeAxis = XboxController.Axis.kLeftX.value;
  public final int rotationAxis = XboxController.Axis.kRightX.value;

  /* Subsystems */
  public final Arm s_Arm;
  public final Swerve s_Swerve = new Swerve();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() 
  {
    // this block allows you to create systems based on the booleans above
		fullRobot = useFullRobot;
		m_exampleSubsystem = (useExampleSubsystem) ? new ExampleSubsystem() : null;
    gyro = (useFullRobot || useGyro) ? new Gyro7454(): null;	
    driverController = (useFullRobot || useDriverCtrl) ? 
      new DriverCtrl(Constants.CtrlConstants.DRIVER_CTRL_PORT) : null;
    operatorController = (useFullRobot || useOperatorCtrl) ? 
      new OperatorCtrl(Constants.CtrlConstants.OPERATOR_CTRL_PORT) : null;
    m_LEDSubsystem = (useFullRobot || useLED) ? new LEDSubsystem() : null;
    s_Arm = (useFullRobot || useArm) ? new Arm() : null;
    mainShuffleboard = (useMainShufflebrd) ? new MainShufflebrd(this) : null;

    /*
    s_Swerve.setDefaultCommand(
        new TeleopSwerve(
            s_Swerve,
            () -> -driver.getRawAxis(translationAxis),
            () -> -driver.getRawAxis(strafeAxis),
            () -> -driver.getRawAxis(rotationAxis),
            () -> false
        )); // ALD This used to be the robotCentric.toBoolean 
 */
    /*/
    s_Arm.setDefaultCommand(
        new TeleopMoveArm(
            s_Arm,
            m_driverController,
            m_operatorController
        ));
    */

    // Configure the button bindings
    if(useFullRobot || useBindings) configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses 
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and 
   * then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    //Swerve
    m_driverController.rightStick().onTrue(
      new InstantCommand(() -> s_Swerve.zeroGyro()));
  
    //LED
    if(m_LEDSubsystem != null)
		{
      /* ALD this is the preffered method for calling button functions 
       * in a subsystem  
       */
      m_driverController.start().onTrue( m_LEDSubsystem.cycleLEDs());
      m_driverController.back().onTrue( m_LEDSubsystem.cycleFunLEDs());
      m_operatorController.start().onTrue( m_LEDSubsystem.cycleLEDs());
      m_operatorController.back().onTrue( m_LEDSubsystem.cycleFunLEDs());
    }
       
    /* ALD the following line would also work, but it is not as readable and 
     * puts more code in the container vs the subsystem. I left commented 
     * method setColor() in LEDSubsystem 
     */
     // m_driverController.start().onTrue(Commands.runOnce(() -> 
     //   {m_LEDSubsystem.setColor();}, m_LEDSubsystem)); 

     //m_operatorController.rightStick().onTrue(new OverBalance(s_Swerve, false));

     //m_operatorController.rightStick().onTrue(new AutoTest(s_Swerve));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  // public Command getAutonomousCommand(String autonomousPath) {
  //   return new exampleAuto(s_Swerve);
  // }

}