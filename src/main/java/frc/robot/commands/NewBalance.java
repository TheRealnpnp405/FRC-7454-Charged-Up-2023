package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.sensors.Gyro7454;
import frc.robot.subsystems.Swerve;


public class NewBalance extends CommandBase
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName); 
    }

    public enum BalanceState
    {
        // level means gyro is at zero, but not done balancing
        // engaged  means gyro has been at zero for 1 second, and is done balancing
        kNotLevel, kLevel, kBeenLevel;
    }

    // *** CLASS AND INSTANCE VARIABLES ***
    private final Swerve drivetrain;
    private final Gyro7454 gyro;
    private double drivePower;
    

    // private final double CS_BALANCE_GOAL_DEGREES = 0.0;
    private final double BALANCE_SPEED= 1.6;
    private final double RAMP_SPEED= -0.6;
    private final double REVERSE_SPEED= .6;
    private final double TIP_PITCH = -4;
    private final double UP_PITCH = 11.8;
    private final double CORRECT_TIME = 2060;

    private boolean drivefoward;

    /**
    * Creates a new AutoBalance.
    * Balances the robot on the charge station with no help from the driver.
    *
    * @param drivetrain Drivetrain subsytem.
    * @param gyro Gyro4237 sensor.
    */
    public NewBalance(Swerve drivetrain, boolean driveForward) 
    {
        // System.out.println("Constructor");
        this.drivetrain = drivetrain;
        this.gyro = drivetrain.gyro;
        this.drivefoward = driveForward;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize()
    {
    }

    boolean firstPhaseFinished = false;
    boolean secondPhaseFinished = false;
    boolean timeForSlow = false;

    @Override
    public boolean isFinished() {
        if(gyro.getPitch() < TIP_PITCH && !firstPhaseFinished) {
            firstPhaseFinished=true;
        }
        else if (firstPhaseFinished && secondPhaseFinished) {
            drivetrain.lockWheels();
            firstPhaseFinished = false;
            secondPhaseFinished = false;
            timeForSlow = false;
            return true;
        }
        return false;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
        if(drivetrain != null)
        {
            drivePower = BALANCE_SPEED;
            
            if(!firstPhaseFinished) {
                if (!drivefoward) drivePower = -drivePower;
                if(gyro.getPitch()>UP_PITCH || timeForSlow) {
                    timeForSlow=true;
                    drivetrain.drive(RAMP_SPEED, 0.0, 0.0, false);
                }
                else drivetrain.drive(drivePower, 0.0, 0.0, false);
            }
            else {
                for(int i=0; i< CORRECT_TIME; i++)
                {
                    drivetrain.drive(REVERSE_SPEED, 0.0, 0.0, false);
                }
                secondPhaseFinished=true;
            }

            /*
            for(int i=0; i< CORRECT_TIME; i++)
            {
                drivePower = -drivePower;
                drivetrain.drive(drivePower, 0.0, 0.0, false);
            }
            */
        }     
     }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {
        if(drivetrain != null)
        {
           //stop
           // drivetrain.drive(0.0, 0.0, 0.0, false);
           // turn 90 degrees to lock wheels
           // drivetrain.lockWheels();
           drivetrain.drive(0, 0.0, 0.0, false);
           drivetrain.lockWheels();
           drivetrain.drive(0, 0.0, 0.0, false);           
        }
    }

    @Override
    public String toString()
    {
        return "AutoBalance(drivetrain, gyro)";
    }

}