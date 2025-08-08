// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.led;

import java.util.concurrent.ThreadLocalRandom;

//import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
//import edu.wpi.first.wpilibj.util.Color;

/** Add your docs here. */
public class Chaos implements LEDzPattern {
	private boolean m_firstTime = true;
	/**
	 * Purely random pattern.  No parmmenters.
	 */
	public Chaos() {
		super();
	}

	@Override
	public void setLEDs(AddressableLEDBuffer buffer) {
		if (m_firstTime){
			for (int index = 0; index < buffer.getLength(); index++) {
				buffer.setHSV(index, ThreadLocalRandom.current().nextInt(1, 255), ThreadLocalRandom.current().nextInt(1, 100), ThreadLocalRandom.current().nextInt(1, 100));
			}
			m_firstTime = false;
		}
		for (int index = 0; index < buffer.getLength(); index++) {
			buffer.setHSV(index, ThreadLocalRandom.current().nextInt(1, 255), ThreadLocalRandom.current().nextInt(1, 100), ThreadLocalRandom.current().nextInt(1, 100));

		}

	}
	// ALD commented out to make the chaos truely random
	// private Color randomColorShift(Color aColor){
	// 	return new Color(randomShift(aColor.red),randomShift(aColor.green),randomShift(aColor.blue));
	// }

	// private double randomShift(double value){
	// 	double sign = Math.random() >= 0.5 ? 1.0 : -1.0;
	// 	double amount = Math.random() / 10;
	// 	return MathUtil.clamp(value + sign * amount, 0, 1);
	// }
	public boolean isAnimated() {
		return true;
	}
}
