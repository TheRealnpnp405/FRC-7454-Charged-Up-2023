// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.LEDConstants;
import frc.robot.led.Chase;
import frc.robot.led.Rainbow;
import frc.robot.led.SolidColor;
import frc.robot.led.LEDz;
import frc.robot.led.LEDzPattern;
//import frc.robot.led.Blink;
//import frc.robot.led.Intensity;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class LEDSubsystem extends SubsystemBase {
	
	private LEDz m_led = 
		new LEDz(LEDConstants.LED_PWM_PORT, LEDConstants.LED_PWM_COUNT);
	
	// Pattern Definitions
	private Color[] ChaseArray = {Color.kGreen, Color.kLimeGreen};
	// don't love but need new pattern.  Gradient fade or random sparkel.	
	private LEDzPattern m_greenChase = new Chase(ChaseArray, 20);  
    private LEDzPattern m_blue = new SolidColor(Color.kDarkBlue);
	private LEDzPattern m_red = new SolidColor(Color.kRed);
	private LEDzPattern m_yellow = new SolidColor(Color.kGold);
	private LEDzPattern m_off = new SolidColor(Color.kBlack);
	private LEDzPattern m_rainbow = new Rainbow();
	//private LEDzPattern m_intensity = new Intensity(Color.kRed, 49);
	//private LEDzPattern m_blink = new Blink(Color.kRed,50) ;
	public int ColorMode = 0;
	public int FunColorMode = 0;
	
	/** Creates a new StatusLED. */
	public LEDSubsystem() {
		super();
	}

	public void setColor() {
		m_led.setPattern(
			DriverStation.getAlliance() == Alliance.Blue ? m_blue : m_red);
	}

	/**
	* Cycle through Blue, Yellow, Off; used to indicate what cargo we need.
	*/
	public CommandBase cycleLEDs() {
	return runOnce(
		() -> {
			if (ColorMode == 0){
				m_led.setPattern(m_blue); 
				ColorMode = 1;
			} else if (ColorMode == 1){
				m_led.setPattern(m_yellow);
				ColorMode = 2;
			} else {
				m_led.setPattern(m_off);
				ColorMode = 0;
			}
		});
	}

	/**
	* Fun LED patterns.
	*/
	public CommandBase cycleFunLEDs() {
		return runOnce(
			() -> {
				if (FunColorMode == 0){
					m_led.setPattern(m_greenChase);
					FunColorMode = 1;
				} else if (FunColorMode == 1){
					m_led.setPattern(m_rainbow);
					FunColorMode = 2;
				} else {
					m_led.setPattern(m_off);
					FunColorMode = 0;
				}
			});
		}	

	@Override
	public void periodic() {
		/* ALD If you want to set a pattern when robot comes online.  
		 * Some cool flash, might be cool. 
		 */
		//m_led.setPattern(m_greenChase);
		//m_led.setPattern(m_intensity);
		//m_led.setPattern(m_blink);
	}

	/* ALD left this so you can see the structure of the function when 
	 * making the lamda call from the RobotContainer. 
	 */
	// public void setColor(){
	// 	if (FunColorMode == 0){
	// 		m_led.setPattern(m_green);
	// 		FunColorMode = 1;
	// 	} else if (FunColorMode == 1){
	// 		m_led.setPattern(m_rainbow);
	// 		FunColorMode = 2;
	// 	} else {
	// 		m_led.setPattern(m_off);
	// 		FunColorMode = 0;
	// 	}
	// }	
	
}
