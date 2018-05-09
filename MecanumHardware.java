package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;


/**
 * Created by Phoebe on 10/20/2017.
 * Code for a mecanum drive train plus a rack and pinion lift (motor, limit switch and 180 servo). Also set up
 * a horizontal lift with two 180 servos and a motor, and a color sensor with its respective 180 servo
 */

public class MecanumHardware {

    /*public OpMode Members */
    //variables for drive train
    public DcMotor frontRight = null;
    public DcMotor backRight = null;
    public DcMotor frontLeft = null;
    public DcMotor backLeft = null;
    //variables for vertical lift
    public DcMotor lift = null;
    public Servo left_grab = null;
    public Servo right_grab = null;
    public Servo top_left = null;
    public Servo top_right = null;
    public DigitalChannel bottom_sensor;
    //variables for the horizontal lift
    public DcMotor horizontal_lift = null;
    public DcMotor back_lift = null;
    public Servo elbow_joint = null;
    public Servo claw = null;
    //variables for color sensor
    public NormalizedColorSensor colorSensor = null;
    public Servo jewel_arm = null;



    // local OpMode members
    HardwareMap hwMap = null;

    // run loop
    public void init (HardwareMap ahwMap){
        hwMap = ahwMap;

        /*map motors for the configuration file */
        //drive train mapping
        frontRight = hwMap.dcMotor.get("front_right");
        backRight = hwMap.dcMotor.get("back_right");
        frontLeft = hwMap.dcMotor.get("front_left");
        backLeft = hwMap.dcMotor.get("back_left");

        //vertical lift mapping
        lift = hwMap.dcMotor.get("lift");
        left_grab = hwMap.servo.get("left_grab");
        right_grab = hwMap.servo.get("right_grab");
        top_left = hwMap.servo.get("top_left");
        top_right = hwMap.servo.get("top_right");

        //horizontal lift mapping
        horizontal_lift = hwMap.dcMotor.get("horizontal");
        back_lift = hwMap.dcMotor.get("back_lift");
        elbow_joint = hwMap.servo.get("elbow");
        claw = hwMap.servo.get("claw");

        //map the touch sensor
        bottom_sensor = hwMap.digitalChannel.get("limit_switch");

        //map the color sensor parts
        colorSensor = hwMap.get(NormalizedColorSensor.class, "sensor_color");
        jewel_arm = hwMap.servo.get("jewel_arm");


        // set motor default power to 0 for the motors and default position to .5 for the servo
        frontRight.setPower(0);
        backRight.setPower(0);
        frontLeft.setPower(0);
        backLeft.setPower(0);

        lift.setPower(0);
        back_lift.setPower(0);
        //grabbers are set to open at init so that they will not extend past the 18''
        left_grab.setPosition(0.005); //0 is open bottom //was 0.01 last change
        right_grab.setPosition(0.87); //1 is open bottom //was .85 last change
        top_left.setPosition(0.01); //0 is open
        top_right.setPosition(0.99); //1 is open

        horizontal_lift.setPower(0.0);
        elbow_joint.setPosition(0.99);
        claw.setPosition(0.60);
        jewel_arm.setPosition(0.94); //was .96s


        // just to make sure all the motors are running forward
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        lift.setDirection(DcMotor.Direction.FORWARD);
        horizontal_lift.setDirection(DcMotor.Direction.FORWARD);
        back_lift.setDirection(DcMotor.Direction.FORWARD);


        // set all to run without encoder
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        horizontal_lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        back_lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


    }
}
