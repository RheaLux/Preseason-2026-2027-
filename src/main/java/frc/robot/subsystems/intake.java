package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
private final TalonFX rollerMotor; 
private final TalonFX pivotMotor; 
private final VoltageOut rollerVoltageRequest = new VoltageOut(0.0); 
private final MotionMagicVoltage pivotPositionRequest = new MotionMagicVoltage(0.0).withSlot(0); 
private final VoltageOut pivotVoltageRequest = new VoltageOut(0.0);
private static final double METERS_PER_MOTOR_ROTATION = 2 * Math.PI / 20; 
private static final double MAX_POSTION = Math.PI/2; 

  public Intake() {
    rollerMotor = new TalonFX(123);
    pivotMotor = new TalonFX(124);

    TalonFXConfiguration rollerConfig = new TalonFXConfiguration(); 
    rollerConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;

    TalonFXConfiguration pivotConfig = new TalonFXConfiguration();
    pivotConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast; 

    rollerConfig.CurrentLimits.StatorCurrentLimit = 40.0;
    pivotConfig.CurrentLimits.StatorCurrentLimit = 40.0;

    rollerConfig.CurrentLimits.StatorCurrentLimitEnable = true; 
    pivotConfig.CurrentLimits.StatorCurrentLimitEnable = true; 

    rollerConfig.Voltage.PeakForwardVoltage = 12.0; 
    rollerConfig.Voltage.PeakReverseVoltage = -12.0; 

    pivotConfig.Voltage.PeakForwardVoltage = 12.0; 
    pivotConfig.Voltage.PeakReverseVoltage = -12.0;

    pivotConfig.Slot().kP = 0.83 * 12.0 * METERS_PER_MOTOR_ROTATION; 
    pivotConfig.Slot().kI = 0.0;
    pivotConfig.Slot().kD = 0.0; 

    pivotConfig.Slot().kS = 0.584; 
    pivotConfig.Slot().kG = 0.366; 
    pivotConfig.Slot().kV = 2.15 * METERS_PER_MOTOR_ROTATION;
    pivotConfig.Slot().kA = 0.05 * METERS_PER_MOTOR_ROTATION;
    
    rollerMotor.getConfigurator().apply(rollerConfig); 
    pivotMotor.getConfigurator().apply(pivotConfig);
    }

public double getpivotPostion(){
    return pivotMotor.getVelocity().getValueAsDouble() * METERS_PER_MOTOR_ROTATION; 
}

public double getpivotVoltage(){
     return pivotMotor.getVoltage().getValueAsDouble(); 
}
public double getrollerVoltage(){
    return rollerMotor.getVoltage().getValueAsDouble()
}

public void setDesiredPostion(double positionMeters){
    setpointPosition = MathUtil.clamp(positionMeters, 0.0, MAX_POSITION); 
    rollerMotor.setControl(pivotPositionRequest.withPosition (setpointPosition/ METERS_PER_MOTOR_ROTATION));  
}
public void setRollerVoltage(double voltage){
    rollerMotor.setControl(rollerVoltageRequest.withOutput(MathUtil.clamp(voltage, -12.0, 12.0))); 
}

public void setPivotVoltage(double voltage){
    pivotMotor.setControl(pivotVoltageRequest.withOutput(MathUtil.clamp(voltage, -12.0, 12.0))); 
}

public void pivotStop(){
    pivotMotor.stopMotor(); 
}

public void rollerStop(){
    rollerMotor.stopMotor();
}
public void zeroEncoderPivot(){
    stop();
    pivotMotor.setPosition(0.0);
    setpointHeight = 0.0; 
}



  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    // Not needed for now
  }

}

