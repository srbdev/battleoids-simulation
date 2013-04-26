package edu.cs523.project3.ga.fleet;

import java.util.ArrayList;
import java.util.Collections;

import edu.cs523.project3.model.Arc;
import edu.cs523.project3.model.Sensor;
import edu.cs523.project3.model.Ship;

public class ShipType {
	
	public static void main(String[] args){
		
		createSensors(36800);
	}
	
	private ShipType(){

	}
	
	public static Ship newInstance(int typeNumber, int defaultAction){
		
		Ship ship = new Ship();
		
		/* check for over flow */
		typeNumber = (typeNumber > 65535) ? 65535 : typeNumber; 
		defaultAction = (defaultAction > 15) ? 15 : defaultAction;
		
		/* create ship from typeNumber */
		ship.setSensors(createSensors(typeNumber));
		ship.setDefaultAction(defaultAction);
		
		return ship;
		
		
	}
	
	static int getShipTypeNumber(Ship s){
		int tn = 0;
		
		for(int i = 0; i < s.getSensors().size(); i++){
			
			if(s.getSensors().get(i).isActive())
				tn += 2^i;
			
		}
		
		return tn;
	}
	
	static ArrayList <Sensor> createSensors(int typeNumber){

		
		String s = Integer.toBinaryString(typeNumber);
		boolean [] active = new boolean[16];
		
		/* figure out which sensors to activate */
		for(int i = 0; i < s.length(); i++){
			
			if(i >= s.length()){
				active[s.length() - (1+i)] = false;
				System.out.println("sensor " + (s.length() - (1+i)) + " not activated");
			}else if(s.charAt(i) == '1'){
				active[s.length() - (1+i)] = true;
				System.out.println("sensor " + (s.length() - (1+i)) + " activated");
			}else{
				System.out.println("sensor " + (s.length() - (1+i)) + " not activated");
				active[s.length() - (1+i)] = false;
			}
			
		}
		
		ArrayList <Sensor> sensors = new ArrayList <Sensor>();
		
		/****** type 1 ******/
		/**** aggressive  *****/
		/*sensor 1 fire- when a ship is in front*/
		Sensor s0 = new Sensor(2, new Arc(0, 1, 1), active[0]);
		sensors.add(s0);
		
		/*sensor 2 accelerate - when a ship is behind*/
		Sensor s1 = new Sensor(1, new Arc(1, 1, 1), active[1]);
		sensors.add(s1);
		
		/*sensor 3 turn left - if ship is to the front & left*/
		Sensor s2 = new Sensor(4, new Arc(.25, .5, 1), active[2]);
		sensors.add(s2);
		
		/*sensor 4 turn right - if ship is to front & right turn right*/
		Sensor s3 = new Sensor(8, new Arc(1.75, .5, 1), active[3]);
		sensors.add(s3);
		
		
		/**** type 2  *****/
		/**** with sensor 0 ****/
		/**** no acceleration ****/
		/**** aggressive ****/
		/*sensor 5 turn right - if ship is anywhere on right */
		Sensor s4 = new Sensor(8, new Arc(1.5, 1, 1), active[4]);
		sensors.add(s4);
		
		/*sensor 6 turn left - if ship is anywhere on left */
		Sensor s5 = new Sensor(4, new Arc(.5, 1, 1), active[5]);
		sensors.add(s5);
		
		
		/**** type 3 ****/
		/** with sensor 0, 1 **/
		/***  passive ***/
		/*sensor 7 turn left - when ship is front right (unless ship is in front (45 degree))  */
		Sensor s6 = new Sensor(4, new Arc(1.625, .25, 1), active[6]);
		sensors.add(s6);
		
		/*sensor 8 turn left - when ship is back right */
		Sensor s7 = new Sensor(4, new Arc(1.25, .5, 1), active[7]);
		sensors.add(s7);
		
		/*sensor 9 turn right - when ship is front left (unless ship is in front (45 degree))  */
		Sensor s8 = new Sensor(8, new Arc(.375, .25, 1), active[8]);
		sensors.add(s8);
		
		/*sensor 10 turn right - when ship is back right */
		Sensor s9 = new Sensor(8, new Arc(.75, .5, 1), active[9]);
		sensors.add(s9);
		
		/*sensor 11 accelerate - when ship is front right (unless ship is in front (45 degree))  */
		Sensor s10 = new Sensor(1, new Arc(.375, .25, 1), active[10]);
		sensors.add(s10);
		
		
		/*sensor 12 accelerate - when ship is front left (unless ship is in front (45 degree))  */
		Sensor s11 = new Sensor(1, new Arc(1.625, .25, 1), active[11]);
		sensors.add(s11);
		
		
		
		/*** type 4 ***/
		/*** with sensor 0 ***/
		/*** Always firing and spinning right***/
		/**** defensive ****/
		/*sensor 13 - fire behind - hoping to get lucky some how */
		Sensor s12 = new Sensor(2, new Arc(1, 1, 1), active[12]);
		sensors.add(s12);
		
		/*sensor 14 - spin right if something is in front */
		Sensor s13 = new Sensor(8, new Arc(0, 1, 1), active[13]);
		sensors.add(s13);
		
		/*sensor 15 - spin right if something is behind*/
		Sensor s14 = new Sensor(8, new Arc(1, 1, 1), active[14]);
		sensors.add(s14);
		
		
		/** type 5 **/
		/** avoidance - always accelerate **/
		/** with sensors 6, 7, 8, 9, 10, 11 **/
		/*sensor 16 - accelerate if something is in front */
		Sensor s15 = new Sensor(1, new Arc(0, .5, 1), active[15]);
		sensors.add(s15);
		
		
		return sensors;
		
	}

}
