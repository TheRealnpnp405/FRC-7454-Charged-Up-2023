package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.sensors.Gyro7454;
import frc.robot.subsystems.Swerve;
import static frc.robot.commands.OverBalance.Phases.START_WAIT;
import static frc.robot.commands.OverBalance.Phases.BACK;
import static frc.robot.commands.OverBalance.Phases.FORWARD;
import static frc.robot.commands.OverBalance.Phases.SLOWROLL;
import static frc.robot.commands.OverBalance.Phases.WAIT;
import static frc.robot.commands.OverBalance.Phases.CORRECT;
import static frc.robot.commands.OverBalance.Phases.EXIT;
import static frc.robot.commands.OverBalance.Phases.OVERWAIT;

public class OverBalance extends CommandBase
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

    public enum Phases { START_WAIT, BACK, OVERWAIT, FORWARD, SLOWROLL, WAIT, CORRECT,EXIT }

    // *** CLASS AND INSTANCE VARIABLES ***
    private final Swerve drivetrain;
    private final Gyro7454 gyro;
    
    // private final double CS_BALANCE_GOAL_DEGREES = 0.0;
    private final double OVER_SPEED    = 1.8;
    private final double OVER_TIME     = 3500; // in millis
    private final double OVERWAIT_TIME = 300; // in millis
    private final double BALANCE_SPEED = 1.6;
    private final double RAMP_SPEED    = 0.6;
    private final double REVERSE_SPEED = 0.6;
    private final double UP_PITCH      = -11.8;
    private final double TIP_PITCH     = 4;
    private final double WAIT_TIME     = 200; // in millis
    private final double CORRECT_TIME  = 1225; // in millis
    private final double FREQ          = 20; // Tics
    private final double START_WAIT_TIME     = 1000; // in millis
    private final double START_WAIT_SPEED     = .3 ; // in millis  

    private int ticCount = 0;
    private Phases currentPhase = START_WAIT;

    /**
    * Creates a new AutoBalance.
    * Balances the robot on the charge station with no help from the driver.
    *
    * @param drivetrain Drivetrain subsytem.
    * @param gyro Gyro4237 sensor.
    */
    public OverBalance(Swerve drivetrain) 
    {
        this.drivetrain = drivetrain;
        this.gyro = drivetrain.gyro;
    }

    // Called when the command is initially scheduled.
    @Override public void initialize() { }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
        if(drivetrain != null)
        {
            switch(currentPhase) {
                case START_WAIT:
                ticCount++;
                drivetrain.drive((START_WAIT_SPEED*-1), 0.0, 0.0, false);   
                if (ticCount>=(START_WAIT_TIME/FREQ)) {
                    ticCount=0;                        
                    currentPhase=BACK;
                }     
                break; 
                case BACK: // back over charging station                  
                    drivetrain.drive((OVER_SPEED*-1), 0.0, 0.0, false);
                    ticCount++;
                    if (ticCount>=(OVER_TIME/FREQ)) {
                        ticCount=0;                        
                        currentPhase=OVERWAIT;
                    }    
                    break;   
                case OVERWAIT:
                    ticCount++;
                    drivetrain.drive(0, 0.0, 0.0, false);   
                    if (ticCount>=(OVERWAIT_TIME/FREQ)) {
                        ticCount=0;                        
                        currentPhase=FORWARD;
                    }     
                    break;                    
                case FORWARD: // pull onto charging station
                    drivetrain.drive((BALANCE_SPEED), 0.0, 0.0, false);
                    if(gyro.getPitch() <= UP_PITCH) {
                        currentPhase=SLOWROLL;
                    }
                    break;
                case SLOWROLL:
                    drivetrain.drive((RAMP_SPEED), 0.0, 0.0, false);
                    if(gyro.getPitch() >= TIP_PITCH) {
                        ticCount=0;
                        currentPhase=WAIT;
                    }
                    break;
                case WAIT:
                    ticCount++;
                    drivetrain.drive(0, 0.0, 0.0, false);   
                    if (ticCount>=(WAIT_TIME/FREQ)) {
                        ticCount=0;                        
                        currentPhase=CORRECT;
                    }     
                    break;           
                case CORRECT:
                    ticCount++;
                    drivetrain.drive((REVERSE_SPEED*-1), 0.0, 0.0, false);
                    if (ticCount>=(CORRECT_TIME/FREQ)) {
                        ticCount=0;                        
                        currentPhase=EXIT;
                    }  
                    break;
                case EXIT:
                    break;                    
            }
        }     
     }

     @Override
     public boolean isFinished() {
        if (currentPhase==EXIT) return true;
        else return false;
     }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {
        if(drivetrain != null)
        {
            drivetrain.drive(0, 0.0, 0.0, false);
            drivetrain.lockWheels();
            drivetrain.drive(0, 0.0, 0.0, false);
        }
    }

    @Override
    public String toString() { return "AutoBalance(drivetrain, gyro)"; }

}
