package application;


import gripper.robotiq;

import javax.inject.Inject;
import javax.inject.Named;

import com.kuka.common.ThreadUtil;
import com.kuka.roboticsAPI.applicationModel.RoboticsAPIApplication;
import static com.kuka.roboticsAPI.motionModel.BasicMotions.*;

import com.kuka.roboticsAPI.controllerModel.Controller;
import com.kuka.roboticsAPI.deviceModel.LBR;
import com.kuka.roboticsAPI.geometricModel.Frame;
import com.kuka.roboticsAPI.geometricModel.ObjectFrame;
import com.kuka.roboticsAPI.geometricModel.Tool;
import com.kuka.task.ITaskLogger;

/**
 * Implementation of a robot application.
 * <p>
 * The application provides a {@link RoboticsAPITask#initialize()} and a 
 * {@link RoboticsAPITask#run()} method, which will be called successively in 
 * the application lifecycle. The application will terminate automatically after 
 * the {@link RoboticsAPITask#run()} method has finished or after stopping the 
 * task. The {@link RoboticsAPITask#dispose()} method will be called, even if an 
 * exception is thrown during initialization or run. 
 * <p>
 * <b>It is imperative to call <code>super.dispose()</code> when overriding the 
 * {@link RoboticsAPITask#dispose()} method.</b> 
 * 
 * @see UseRoboticsAPIContext
 * @see #initialize()
 * @see #run()
 * @see #dispose()
 */
public class Kalidescope extends RoboticsAPIApplication {
@Inject ITaskLogger log;
	
	@Inject
	private LBR robot;
	private Controller KRC_sunrise;
	
	@Inject
	@Named ("Robotiq_2F85")
	private Tool tl_Grip;
	
	private robotiq gripper;
	
	int gripOpen = 35, gripClose = 24; //in millimeters
	int gripSpeed = 50, gripForce = 25;
	double override = 0.3;
	
	ObjectFrame home = getApplicationData().getFrame("/Kalidescope/Home");

	ObjectFrame place;
	Frame out;
	Frame outspin;
	Frame placespin;
	double distance = 65;
	double spin120 = Math.toRadians(120);
	double spin240 = Math.toRadians(240);
		
	@Override
	public void initialize() {
		// initialize your application here
		KRC_sunrise = getController("KUKA_Sunrise_Cabinet_1");
		robot = (LBR) KRC_sunrise.getDevices().toArray()[0];
		gripper = new robotiq(KRC_sunrise, "Robotiq 2F-85", 85);
		
		getApplicationControl().setApplicationOverride(override);
		
		tl_Grip.getFrame("/TCP");
		tl_Grip.attachTo(robot.getFlange());
	}
	
	public void gripinit() {
		gripper.deactivate();
		ThreadUtil.milliSleep(100);
		gripper.activate();
		gripper.move(gripOpen, gripSpeed, gripForce, false);
	}
	
	@Override
	public void run() {
		// your application execution starts here
		gripinit();
		tl_Grip.move(ptp(home));
		log.info("...intialization complete");
		Boolean running = true;
		while (running){
			log.info("currently in linear stage");
			ThreadUtil.milliSleep(10000); // wait ten seconds
			log.info("starting transition to colour stage");
			colour();
			log.info("completed transition to colour stage");
			log.info("currently in colour stage");
			ThreadUtil.milliSleep(10000); // wait ten seconds
			log.info("starting transition to hexagon stage");
			hexagon();
			log.info("completed transition to hexagon stage");
			log.info("currently in hexagon stage");
			ThreadUtil.milliSleep(10000); // wait ten seconds
			log.info("starting transition to linear stage");
			linear();
			log.info("completed transition to linear stage");
		}

	}
	
	public void linear(){ // turning one hexagon at a time

		picknturn120("/Kalidescope/P3");
		picknturn240("/Kalidescope/P4");
		picknturn240("/Kalidescope/P18");
		picknturn120("/Kalidescope/P19");
		
		picknturn120("/Kalidescope/P7");
		picknturn240("/Kalidescope/P8");
		picknturn240("/Kalidescope/P22");
		picknturn120("/Kalidescope/P23");
		
		picknturn120("/Kalidescope/P11");
		picknturn240("/Kalidescope/P12");
		picknturn240("/Kalidescope/P26");
		picknturn120("/Kalidescope/P27");
		
		picknturn120("/Kalidescope/P15");
		picknturn240("/Kalidescope/P16");
		picknturn240("/Kalidescope/P30");
		picknturn120("/Kalidescope/P31");
		
		picknturn120("/Kalidescope/P35");
		picknturn240("/Kalidescope/P36");
		picknturn240("/Kalidescope/P46");
		picknturn120("/Kalidescope/P47");
		
		picknturn120("/Kalidescope/P39");
		picknturn240("/Kalidescope/P40");
		picknturn240("/Kalidescope/P50");
		picknturn120("/Kalidescope/P51");
		
		picknturn120("/Kalidescope/P43");
		picknturn240("/Kalidescope/P44");
		picknturn240("/Kalidescope/P54");
		picknturn120("/Kalidescope/P55");
		
		picknturn120("/Kalidescope/P59");
		picknturn240("/Kalidescope/P60");
		picknturn240("/Kalidescope/P66");
		picknturn120("/Kalidescope/P67");
		
		picknturn120("/Kalidescope/P63");
		picknturn240("/Kalidescope/P64");
		picknturn240("/Kalidescope/P70");
		picknturn120("/Kalidescope/P71");
		
		picknturn120("/Kalidescope/P75");
		picknturn240("/Kalidescope/P76");
		picknturn240("/Kalidescope/P78");
		picknturn120("/Kalidescope/P89");
		
	}
	
	public void colour(){ // turning by row
		
		for (int piece = 2; piece <= 17; piece +=2){
			picknturn120("/Kalidescope/P" + piece);
		}
		
		for (int piece = 19; piece <= 32; piece +=2){
			picknturn120("/Kalidescope/P" + piece);
		}
		
		for (int piece = 34; piece <= 45; piece +=2){
			picknturn120("/Kalidescope/P" + piece);
		}
		
		for (int piece = 47; piece <= 56; piece +=2){
			picknturn120("/Kalidescope/P" + piece);
		}
		
		for (int piece = 58; piece <= 65; piece +=2){
			picknturn120("/Kalidescope/P" + piece);
		}
		
		for (int piece = 67; piece <= 72; piece +=2){
			picknturn120("/Kalidescope/P" + piece);
		}
		
		picknturn120("/Kalidescope/P74");
		picknturn120("/Kalidescope/P76");
		picknturn120("/Kalidescope/P79");
		
	}
	
	public void hexagon(){ // making one hexagon at a time
		
		picknturn120("/Kalidescope/P2");
		picknturn120("/Kalidescope/P3");
		picknturn240("/Kalidescope/P4");
		picknturn240("/Kalidescope/P18");
		picknturn120("/Kalidescope/P19");
		
		picknturn120("/Kalidescope/P6");
		picknturn120("/Kalidescope/P7");
		picknturn240("/Kalidescope/P8");
		picknturn240("/Kalidescope/P22");
		picknturn120("/Kalidescope/P23");
		
		picknturn120("/Kalidescope/P10");
		picknturn120("/Kalidescope/P11");
		picknturn240("/Kalidescope/P12");
		picknturn240("/Kalidescope/P26");
		picknturn120("/Kalidescope/P27");
		
		picknturn120("/Kalidescope/P14");
		picknturn120("/Kalidescope/P15");
		picknturn240("/Kalidescope/P16");
		picknturn240("/Kalidescope/P30");
		picknturn120("/Kalidescope/P31");

		picknturn120("/Kalidescope/P34");
		picknturn120("/Kalidescope/P35");
		picknturn240("/Kalidescope/P36");
		picknturn240("/Kalidescope/P46");
		picknturn120("/Kalidescope/P47");
		
		picknturn120("/Kalidescope/P38");
		picknturn120("/Kalidescope/P39");
		picknturn240("/Kalidescope/P40");
		picknturn240("/Kalidescope/P50");
		picknturn120("/Kalidescope/P51");
		
		picknturn120("/Kalidescope/P42");
		picknturn120("/Kalidescope/P43");
		picknturn240("/Kalidescope/P44");
		picknturn240("/Kalidescope/P54");
		picknturn120("/Kalidescope/P55");
		
		picknturn120("/Kalidescope/P58");
		picknturn120("/Kalidescope/P59");
		picknturn240("/Kalidescope/P60");
		picknturn240("/Kalidescope/P66");
		picknturn120("/Kalidescope/P67");
		
		picknturn120("/Kalidescope/P62");
		picknturn120("/Kalidescope/P63");
		picknturn240("/Kalidescope/P64");
		picknturn240("/Kalidescope/P70");
		picknturn120("/Kalidescope/P71");
		
		picknturn120("/Kalidescope/P74");
		picknturn120("/Kalidescope/P75");
		picknturn240("/Kalidescope/P76");
		picknturn240("/Kalidescope/P78");
		picknturn120("/Kalidescope/P79");
		
	}
	
	public void picknturn120(String location){
		log.info("starting pick and turn");
		place = getApplicationData().getFrame(location);
		out = place.copy();
		out.setY(out.getY() - distance);
		outspin = out.copy();
		outspin.setBetaRad(outspin.getBetaRad() - spin120);
		placespin = outspin.copy();
		placespin.setY(placespin.getY() + distance);

		gripper.move(gripOpen, gripSpeed, gripForce, false);
		tl_Grip.move(ptp(out));
		tl_Grip.move(lin(place));
		gripper.move(gripClose, gripSpeed, gripForce, false);
		tl_Grip.move(lin(out));
		tl_Grip.move(lin(outspin));
		tl_Grip.move(lin(placespin));
		gripper.move(gripOpen, gripSpeed, gripForce, false);
		tl_Grip.move(lin(outspin));
		log.info("completed pick and turn 120 degrees");
	}
	
	public void picknturn240(String location){
		log.info("starting pick and turn");
		place = getApplicationData().getFrame(location);
		out = place.copy();
		out.setY(out.getY() - distance);
		outspin = out.copy();
		outspin.setBetaRad(outspin.getBetaRad() - spin240);
		placespin = outspin.copy();
		placespin.setY(placespin.getY() + distance);

		gripper.move(gripOpen, gripSpeed, gripForce, false);
		tl_Grip.move(ptp(out));
		tl_Grip.move(lin(place));
		gripper.move(gripClose, gripSpeed, gripForce, false);
		tl_Grip.move(lin(out));
		tl_Grip.move(lin(outspin));
		tl_Grip.move(lin(placespin));
		gripper.move(gripOpen, gripSpeed, gripForce, false);
		tl_Grip.move(lin(outspin));
		log.info("completed pick and turn 240 degrees");
	}
}
