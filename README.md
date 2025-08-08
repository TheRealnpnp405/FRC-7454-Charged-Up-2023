# FRC 7454 Huskies on Hogs – Charged Up 2023

This repo contains the full Java code I developed for the 2023 FIRST Robotics Competition season whilst on team 7454 Huskies on Hogs.

## Why I’m sharing this code

My experience with FRC helped me discover that I want to be a computer engineer. Along the way, I built friendships with other FIRST programmers, sharing questions, support, and ideas. I strive to show gracious professionalism and coopertition in all parts of my life.

I’m sharing this code to support the FIRST community and help other teams learn, grow, and innovate. FIRST is about discovery, innovation, impact, inclusion, teamwork, and fun—and sharing what we learn is a big part of that.

## How to use

- Feel free to study, use, and adapt this code for your own team’s needs.
- Please credit Nicholas Pfeiffer if you reuse significant parts.
- This code is provided as-is, so test thoroughly in your own environment.

## About me and the team

Over my four years on the team, I was able to teach STEAM skills to over 10,000 students. FIRST helped me discover my passion for computer engineering and gave me the chance to make meaningful connections with mentors and peers. It taught me the value of teamwork, gracious professionalism, and coopertition. I mentored a junior high VEX team and worked to spread awareness of FIRST through tours, presentations, and community events. Being part of this team shaped who I am, and I hope this code helps others find their own path and inspiration.

Thank you FIRST.




# ADDITIONAL INFO

7454 code for 2023 Charged Up.  Swerve flavor

## Hardware Assumptions
* SDS MK4i 8.14:1 L1 swerve modules
* CTR CANCoders
* SparkMax Neo motors
* CTR Pegion 2 IMU

## CAN Assignments
0 SparkMax Power Distribution<br>
1 SparkMax Front Left Steer<br>
2 SparkMax Front Left Drive<br>
3 SparkMax Front Right Steer<br>
4 SparkMax Front Right Drive<br>
5 SparkMax Back Right Steer<br>
6 SparkMax Back Right Drive<br>
7 SparkMax Back Left Steer<br>
8 SparkMax Back Left Drive<br>
<br>
21 Cancoder Front Left<br>
22 Cancoder Front Right<br>
23 Cancoder Back Right<br>
24 Cancoder Back Left<br>
<br>
25 Pigeon 2 IMU<br>

## Controller Mappings
This code is natively setup to use a xbox controller to control the swerve drive.
* Left Stick: Translation Control (forwards and sideways movement)
* Right Stick: Rotation Control
* Y button: Zero Gyro (useful if the gyro drifts mid match, just rotate the robot forwards, and press Y to rezero)
* Left Bumper: Switches To Robot Centric Control while held

## Swerve Background
* Special thanks to 4272 for help with parts list and SparkMax mount 3D prints.
* Team 2059 for the Swerve Documentation. https://docs.google.com/document/d/1VO_HjHx0AQW0lgfmrxxUV-ULtsHZ8Dktv3-T3pGWbyM/edit?pli=1
* Sean Sun Team 6814 for the excellent 0 to Autonomous tutorials. https://www.youtube.com/@0ToAuto 
* This code was a combination of Team 3512 https://github.com/frc3512/SwerveBot-2022 and Team 364 https://github.com/Team364/BaseFalconSwerve 
* Follow the 364 config instructions https://github.com/Team364/BaseFalconSwerve/blob/main/README.md
