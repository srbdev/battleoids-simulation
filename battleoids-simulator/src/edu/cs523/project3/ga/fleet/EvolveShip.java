package edu.cs523.project3.ga.fleet;

import java.util.ArrayList;
import java.util.Random;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import edu.cs523.project3.model.Arc;
import edu.cs523.project3.model.Score;
import edu.cs523.project3.model.Sensor;
import edu.cs523.project3.model.Ship;

public class EvolveShip {
	
	private static final double chanceOfMutation = .5;
	private static final double fractionOfValue = .1;
    private static final int numOfSensorsToSwitch = 2;
    private static final Random r = new Random();
    private static File file;
    private static FileWriter fw;
    private static BufferedWriter bw;
    
    
    private static File best_file;
    private static FileWriter best_fw;
    private static BufferedWriter best_bw;
    
    
	private EvolveShip(){
		
	}
	
	
	
	public static void printBest(Ship s){
		
		try {
			
			best_fw = new FileWriter("C:\\Users\\mantogn\\Desktop\\cs523\\final project\\best.csv", true);
			best_bw = new BufferedWriter(best_fw);
			
			best_bw.write("type, " + ShipType.getShipTypeNumber(s) + "\n");
			best_bw.write(",,default," + s.getDefaultAction() + "\n");
			
			for(Sensor sen : s.getSensors()){
				if(sen.isActive()){
					best_bw.write(",,mode," + sen.getMode() + "\n");
					best_bw.write(",,facing," + sen.getArc().getFacing() + "\n");
					best_bw.write(",,range," + sen.getArc().getRange() + "\n");
					best_bw.write(",,width," + sen.getArc().getWidth() + "\n");
				}
			}
			
			best_bw.write("\n");
			
			best_bw.flush();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void createFileHeaders(String fileName){
		
		//create file
		file = new File("C:\\Users\\mantogn\\Desktop\\cs523\\final project\\" + fileName + ".csv");
		
		try {
			 fw = new FileWriter(file.getAbsoluteFile());
			 bw = new BufferedWriter(fw);
			 
			 
			 /* write headers */
				bw.write("ship type,");
				bw.write("ship energy,");
				bw.write("ship hits,");
				bw.write("ship misses,");
				bw.write("ship time,");
				bw.write("\n");
			    bw.flush();
			    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void closeWriters(){
		try {
			
			bw.close();
			best_bw.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	
	/** many create many random ships - based on passed ship**/
	public static ArrayList <Ship> shuffle(Ship s, int numberOfShipsToGenerate){
		
		ArrayList <Ship> ships = new ArrayList <Ship>();
		ships.add(0, s);
		
		/* get ship type number */
		double shipTypeNumber = ShipType.getShipTypeNumber(s);
		

		
		
		for(int i = 1; i < numberOfShipsToGenerate; i++){
			
			
			
			/* create new ship */
			Ship newShip = ShipType.newInstance(shipTypeNumber, s.getDefaultAction());
			
			/* modify sensors */
			for(int j = 0; j < numOfSensorsToSwitch; j++){
				
				Sensor sensor = newShip.getSensors().get(r.nextInt(16));
				changeActivationOfSensor(sensor);
				mutateSensor(sensor);
			}
			
			/* change default action */
			changeDefaultActions(s);
			
			/* add ship to fleet */
			ships.add(i, newShip);
		}
		
		return ships;
	}
	
	
	public static ArrayList <Ship> evolve(ArrayList <Ship> ships, ArrayList<ArrayList<Score>> scoreMatrix){
		
		int numberOfShips = ships.size();
		int [] shipScore = new  int [numberOfShips];
		int indexOfFittestShip = -1;
		int indexOfNextFittestShip = -1;
		int highestScore = -100;
		int nextHighestScore = -100;
		
		ArrayList <Ship> newFleet = new ArrayList <Ship> (); 
		
		
		/* indexed by run, then by ship */
		for(ArrayList <Score> run : scoreMatrix){
			for(int i = 0; i < shipScore.length; i++){
				shipScore[i] += run.get(i).energy;
				
				
				/*** print statistics ***/
				try {
					
					bw.write(ShipType.getShipTypeNumber(ships.get(i)) + ",");
					bw.write(ships.get(i).getScore().energy + ",");
					bw.write(ships.get(i).getScore().hits + ",");
					bw.write(ships.get(i).getScore().misses + ",");
					bw.write(ships.get(i).getScore().time + ",");
					bw.write("\n");
					
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				
			}
		}
		
		try {
			
			bw.write("\n\n");
			bw.flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/* find fittest ship */
		for(int i = 0; i < shipScore.length; i++){
			
			
			
			if(shipScore[i] > highestScore){
				highestScore = shipScore[i];
				indexOfNextFittestShip = indexOfFittestShip;
				indexOfFittestShip = i;
			}else if(shipScore[i] <= highestScore && shipScore[i] > nextHighestScore){
				nextHighestScore = shipScore[i];
				indexOfNextFittestShip = i;
			}
			
			
		}
		
		
		/* elitism : add to list */
		newFleet.add(ships.get(indexOfFittestShip));
		
		
		/* mutated elite: add to list */
		/* get ship type number */
		/* create clone of elitie */
		int shipTypeNumber = ShipType.getShipTypeNumber(ships.get(indexOfFittestShip));
		Ship newShip = ShipType.newInstance(shipTypeNumber, ships.get(indexOfFittestShip).getDefaultAction());
		for(int j = 0; j < numOfSensorsToSwitch; j++){
			Sensor sensor = newShip.getSensors().get(r.nextInt(16));
			mutateSensor(sensor);
		}
		newFleet.add(newShip);
		
		newFleet.add(ships.get(indexOfNextFittestShip));
		
		/* 3 parent crossover - randomly select 3 parents */
		/* crossover at 1 points between 0 and 15 sensors */
		/* mutate child */
		/* redo population - 2 times */
		for(int i = 3; i < numberOfShips; i++){
			
			int indexParent1 = r.nextInt(numberOfShips);
			int indexParent2 = r.nextInt(numberOfShips);
			int indexParent3 = r.nextInt(numberOfShips);
			
			int point1=0;
			int point2=0;
			
			while(point1 >= point2){
				point1= r.nextInt(16);
				point2= r.nextInt(16);
			}
			
			
			
			
			ArrayList <Sensor> sensors = new ArrayList <Sensor> ();
			Ship child = new Ship();
			
			/* get parent 1 sensors */
			for(int j = 0; j < point1; j++){
				Sensor s = new Sensor();
				
				/*Active*/
				s.setActive(ships.get(indexParent1).getSensors().get(j).isActive());
				
				/* mode */
				s.setMode(ships.get(indexParent1).getSensors().get(j).getMode());
				
				/* Arc */
				s.setArc(new Arc(ships.get(indexParent1).getSensors().get(j).getArc().getFacing(), 
						         ships.get(indexParent1).getSensors().get(j).getArc().getWidth(), 
						         ships.get(indexParent1).getSensors().get(j).getArc().getRange()));
				
				sensors.add(s);
			}
			
			/* get parent 2 sensors */
			for(int j = point1; j < point2; j++){
				Sensor s = new Sensor();
				
				/*Active*/
				s.setActive(ships.get(indexParent2).getSensors().get(j).isActive());
				
				/* mode */
				s.setMode(ships.get(indexParent2).getSensors().get(j).getMode());
				
				/* Arc */
				s.setArc(new Arc(ships.get(indexParent2).getSensors().get(j).getArc().getFacing(), 
						         ships.get(indexParent2).getSensors().get(j).getArc().getWidth(), 
						         ships.get(indexParent2).getSensors().get(j).getArc().getRange()));
				
				sensors.add(s);
			}
			
			/* get parent 3 sensors */
			for(int j = point2; j < ships.get(0).getSensors().size(); j++){
				Sensor s = new Sensor();
				
				/*Active*/
				s.setActive(ships.get(indexParent3).getSensors().get(j).isActive());
				
				/* mode */
				s.setMode(ships.get(indexParent3).getSensors().get(j).getMode());
				
				/* Arc */
				s.setArc(new Arc(ships.get(indexParent3).getSensors().get(j).getArc().getFacing(), 
						         ships.get(indexParent3).getSensors().get(j).getArc().getWidth(), 
						         ships.get(indexParent3).getSensors().get(j).getArc().getRange()));
				
				sensors.add(s);
			}
			
			child.setSensors(sensors);
			
			
			//make sure all atleast one fire sensor is active
			if(!child.getSensors().get(0).isActive() && !child.getSensors().get(12).isActive())
				child.getSensors().get(0).setActive(true);
			
			child.setDefaultAction(r.nextInt(16));
			newFleet.add(child);
		}
		
		
		/* return new population */
		return newFleet;
	}
	
	/******************************************************/
	/**************** private methods *********************/
	/******************************************************/
	
	
	private static void changeDefaultActions(Ship s){
		Random r = new Random();
		
		if(r.nextDouble() < chanceOfMutation)
			s.setDefaultAction(r.nextInt(16));
		
	}
	
	
	private static void  changeActivationOfSensor(Sensor s){
		s.setActive(!s.isActive());
	}
	
	
	private static void mutateSensor(Sensor s){
		double modificationAmount = 0;
		
		/** arc - facing**/
		if(r.nextDouble() <= chanceOfMutation )
			modificationAmount = fractionOfValue * s.getArc().getFacing(); 
		if(r.nextBoolean()) /* true = positive*/
			s.getArc().setFacing(modificationAmount);
		else
			s.getArc().setFacing(-1*modificationAmount);
		
		/** arc - range**/
		if(r.nextDouble() <= chanceOfMutation )
			modificationAmount = fractionOfValue * s.getArc().getRange(); 
		if(r.nextBoolean()) /* true = positive*/
			s.getArc().setRange(modificationAmount);
		else
			s.getArc().setRange(-1*modificationAmount);
		
		/** arc - width**/
		if(r.nextDouble() <= chanceOfMutation )
			modificationAmount = fractionOfValue * s.getArc().getWidth(); 
		if(r.nextBoolean()) /* true = positive*/
			s.getArc().setWidth(modificationAmount);
		else
			s.getArc().setWidth(-1*modificationAmount);
			
	}
	
	
	
	
}
