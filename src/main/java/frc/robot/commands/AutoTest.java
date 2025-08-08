package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Swerve;
import static frc.robot.commands.AutoTest.Phases.BACK;
import static frc.robot.commands.AutoTest.Phases.FORWARD;
import static frc.robot.commands.AutoTest.Phases.WAIT;
import static frc.robot.commands.AutoTest.Phases.EXIT;

public class AutoTest extends CommandBase
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName); 
    }

    public enum Phases { BACK, FORWARD, WAIT, EXIT }

    // *** CLASS AND INSTANCE VARIABLES ***
    private final Swerve drivetrain;
    
    // private final double CS_BALANCE_GOAL_DEGREES = 0.0;
    private final double OVER_SPEED= .5;
    private final double OVER_TIME = 3000; // in millis
    private final double WAIT_TIME = 3000; // in millis
    private final double FREQ = 20; // Tics

    private int ticCount = 0;
    private Phases currentPhase = BACK;

    public AutoTest(Swerve drivetrain) 
    {
        this.drivetrain = drivetrain;
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
                case BACK:             
                    ticCount++;
                    drivetrain.drive((OVER_SPEED*-1), 0.0, 0.0, false);
                    if (ticCount>=(OVER_TIME/FREQ)) {
                        ticCount=0;                        
                        currentPhase=WAIT; 
                    }    
                    break;   
                case WAIT:
                    ticCount++;
                    drivetrain.drive(0, 0.0, 0.0, false);   
                    if (ticCount>=(WAIT_TIME/FREQ)) {
                        ticCount=0;               
                        currentPhase=FORWARD;
                    }     
                    break;                       
                case FORWARD:                 
                    ticCount++;
                    drivetrain.drive(OVER_SPEED, 0.0, 0.0, false);
                    if (ticCount>=(OVER_TIME/FREQ)) {
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
    public String toString() { return "AutoTest(drivetrain)"; }

}
