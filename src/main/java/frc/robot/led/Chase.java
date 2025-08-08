// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.led;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;

/** Add your docs here. */
public class Chase implements LEDzPattern {
	private Color[] m_Colors;
	private int m_SegmentWidth;
	private int m_offset;

	/**
	* Colors chase each other.  
	*
	* @param  colors  Array of JAVA colors
	* @param  segmentWidth number of leds to light up for segment
	*/	
	public Chase(Color[] colors, int segmentWidth){
		super();
		m_Colors = colors;
		m_SegmentWidth = segmentWidth;
	}

	@Override
	public void setLEDs(AddressableLEDBuffer buffer) {
		int numberOfColors = m_Colors.length;
		int effectiveIndex;
		int colorIndex;
		int bufferLength = buffer.getLength();
		for (int index = 0; index < bufferLength; index++){
			effectiveIndex = (index + m_offset) % bufferLength;
			colorIndex =( index /m_SegmentWidth )% numberOfColors;
			buffer.setLED(effectiveIndex, m_Colors[colorIndex]);
		}

		m_offset =(m_offset+1) %bufferLength;
	}
	public boolean isAnimated(){
		return true;
	}
}
