package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;


/**
 * Created by Phoebe Taylor on 10/23/2017.
 * Program is for running a mecanum drive train with a lift, which consists of a motor and 2 180
 * servos for grabbing the blocks. The horizontal lift for the relic will consist of another motor
 * and two more 180 servos.
 * right and left dpad will open and close the grabbers on the vertical lift
 * The down button on the dpad will open the top grabbers, up dpad will close top grabbers
 * 'Y' will raise the vertical lift, and 'X' will lower it
 * 'B' will extend the horizontal lift and 'A' will retract it
 * left bumper will will increment the elbow up, right will increment it down
 * TODO: FIX COMMENTS FOR CONTROLS
 */

@TeleOp(name="With horizontal lift", group = "mecanum")
public class HorizontalMecanumDriveClass extends LinearOpMode{

    /*Declare controller variables */
    public double x_axis;
    public double y_axis;
    public double z_axis;
    /*The following variables are used to gradually increment up or down the values of the servos so
    that you no longer have to set a specific position for open/closed. Just hold down the button and
    it will close or open gradually
    0.0 is open left bottom servo grabber
    1.0 is open right bottom servo grabber
    0.0 is open left top servo grabber
    1.0 is open right top servo grabber
     */
    public double elbow_value = 0.99;
    //the next two variables will serve as the running value for the incrementation of the grabbers open/closed
    public double bottom_left_grab = 0.01; //open (is 0)
    public double bottom_right_grab = 0.80; //open (is 1)
    //the following two variables will be the close value limits for the bottom two grabbers
    public double close_bottom_left = 0.47; //was .48 //closed is 1
    public double close_bottom_right = 0.46; //was .48 //closed is 0

    //the next two variables will serve as the running value for the incrementation of the grabbers open/closed
    public double top_left_grab = 0.01; //open (is 0)
    public double top_right_grab = 0.80; //open (is 1)
    //the following two variables will be the open value limits for the top two grabbers
    public double close_top_left = 0.47; // closed is 1 //was 0.45
    public double close_top_right = 0.46; //closed is 0 //was 0.48


    public boolean buttonPressed = true;



    //make object of the hardware class so we can use its elements
    MecanumHardware robot = new MecanumHardware();



    @Override
    public void runOpMode () {
        robot.init(hardwareMap);


        waitForStart();
        while (opModeIsActive()) {
            /** following code is for the drive train */
            //buffer for y axis of steering
            if (gamepad1.left_stick_y < -0.01 || gamepad1.left_stick_y > 0.01) {
                y_axis = -gamepad1.left_stick_y;
            } else {
                y_axis = 0;
            }
            //buffer for x axis of steering
            if (gamepad1.left_stick_x < -0.01 || gamepad1.left_stick_x > 0.01) {
                x_axis = gamepad1.left_stick_x;
            } else {
                x_axis = 0;
            }
            //buffer for z axis (turning)
            if (gamepad1.right_stick_x < -0.001 || gamepad1.right_stick_x > 0.001) {
                z_axis = gamepad1.right_stick_x;
            } else {
                z_axis = 0;
            }

            //logic for setting power to correct motors
            robot.frontRight.setPower(x_axis - y_axis + z_axis);
            robot.frontLeft.setPower(x_axis + y_axis + z_axis);
            robot.backRight.setPower(-x_axis - y_axis + z_axis);
            robot.backLeft.setPower(-x_axis + y_axis + z_axis);

            /**end code for drive train, below is the code for the vertical lift */
            /*code for grabber, right bumper for open, right bumper for open/drop
            * the first while loop will increment the grabber closed (left dpad). The second while
            * loop will increment the grabber open (right dpad) */
            //close
            while (gamepad1.dpad_left){
                    if (bottom_left_grab < close_bottom_left) {
                        //bottom pair of grabbers
                        robot.left_grab.setPosition(bottom_left_grab);
                        //increment closed
                        bottom_left_grab += 0.006;
                    }
                    if (bottom_right_grab > close_bottom_right) {
                        robot.right_grab.setPosition(bottom_right_grab);
                        bottom_right_grab -= 0.006;
                    }
            }
            //open
            while (gamepad1.dpad_right && bottom_left_grab > 0.05 && bottom_right_grab < 0.8) {
                //bottom pair of grabbers
                robot.left_grab.setPosition(bottom_left_grab);
                robot.right_grab.setPosition(bottom_right_grab);
                //increment open
                bottom_left_grab -= 0.006;
                bottom_right_grab += 0.006;
            }
            //close
            while (gamepad1.dpad_down) {
                if (top_left_grab < close_top_left) {
                    //top pair of grabbers
                    robot.top_left.setPosition(top_left_grab);
                    //increment closed
                    top_left_grab += 0.006;
                }
                if (top_right_grab > close_top_right) {
                    robot.top_right.setPosition(top_right_grab);
                    top_right_grab -= 0.006;

                }
            }
            //open

            while (gamepad1.dpad_up) {
                //top pair of grabbers
                if (top_left_grab > 0.01){
                    robot.top_left.setPosition(top_left_grab);
                    top_left_grab -= 0.006;

                }
                if (top_right_grab < 0.99) {
                    robot.top_right.setPosition(top_right_grab);
                    //increment open
                    top_right_grab += 0.006;
                }
            }

            //code for the lift motor. Y button will go up, X will go down, and if the limit switch
            //and the X button are pressed simultaneously, the motor will not move
            buttonPressed = robot.bottom_sensor.getState();
            telemetry.addData("touch sensor is:", buttonPressed);
            telemetry.update();

            if (gamepad1.y) {
                robot.lift.setPower(-0.32);

            }
            else if (gamepad1.x) {
                if (robot.bottom_sensor.getState() == false) {
                    robot.lift.setPower(0);
                }
                else if (robot.bottom_sensor.getState() == true) {
                    robot.lift.setPower(0.3);
                }
            }
            else {
                robot.lift.setPower(0);
            }

            /**end code for vertical lift, below is code for the horizontal lift */
            //while the 'b' button is pressed, arm will extend, while 'a' is pressed it will come back in
            if (gamepad1.b) {
                robot.horizontal_lift.setPower(-0.3);
                robot.back_lift.setPower(-0.3);
            }
            else if (gamepad1.a) {
                robot.horizontal_lift.setPower(0.3);
                robot.back_lift.setPower(0.3);
            }
            else {
                robot.horizontal_lift.setPower(0);
                robot.back_lift.setPower(0);
            }
            //left trigger opens the claw, right trigger closes
            while (gamepad1.right_trigger > 0.1 && gamepad1.left_trigger < 0.1) {
                robot.claw.setPosition(0.13);
            }
            //close
            while (gamepad1.left_trigger > 0.1 && gamepad1.right_trigger < 0.1) {
                robot.claw.setPosition(0.62);
            }

            //the down button on the dpad will open the horizontal grabber, the up will close it
            while (gamepad1.left_bumper && elbow_value < 0.99) {
                robot.elbow_joint.setPosition(elbow_value);
                elbow_value += 0.001;
            }
            while (gamepad1.right_bumper && elbow_value > 0.01) {
                robot.elbow_joint.setPosition(elbow_value);
                elbow_value -= 0.001;
            }

            robot.jewel_arm.setPosition(0.94);
            telemetry.update();
        }

    }

}
