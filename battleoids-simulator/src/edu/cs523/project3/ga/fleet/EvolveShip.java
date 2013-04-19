package edu.cs523.project3.ga.fleet;

import java.util.ArrayList;
import java.util.Random;

import edu.cs523.project3.model.Arc;
import edu.cs523.project3.model.Score;
import edu.cs523.project3.model.Sensor;
import edu.cs523.project3.model.Ship;

public class EvolveShip {
	
	private static final double chanceOfMutation = .5;
	private static final double fractionOfValue = .1;
    private static final int numOfSensorsToSwitch = 2;
    private static final Random r = new Random();
    
	private EvolveShip(){
		
	}
	
	
	/** many create many random ships - based on passed ship**/
	public static ArrayList <Ship> shuffle(Ship s, int numberOfShipsToGenerate){
		
		ArrayList <Ship> ships = new ArrayList <Ship>();
		ships.add(0, s);
		
		/* get ship type number */
		int shipTypeNumber = ShipType.getShipTypeNumber(s);
		

		
		
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
		
		int numberOfShips = scoreMatrix.get(0).size() - 1;
		int [] shipScore = new  int [numberOfShips + 1];
		int indexOfFittestShip = 0;
		int highestScore = 0;
		
		ArrayList <Ship> newFleet = new ArrayList <Ship> (); 
		
		
		/* indexed by run, then by ship */
		for(ArrayList <Score> run : scoreMatrix){
			for(int i = 0; i < shipScore.length; i++){
				shipScore[i] += run.get(i).energy;
			}
		}
		
		/* find fittest ship */
		for(int i = 0; i < shipScore.length; i++){
			if(shipScore[i] > highestScore){
				highestScore = shipScore[i];
				indexOfFittestShip = i;
			}
		}
		
		/* elitism : add to list */
		newFleet.add(0, ships.get(indexOfFittestShip));
		
		
		/* mutated elite: add to list */
		/* get ship type number */
		/* create clone of elitie */
		int shipTypeNumber = ShipType.getShipTypeNumber(ships.get(indexOfFittestShip));
		Ship newShip = ShipType.newInstance(shipTypeNumber, ships.get(indexOfFittestShip).getDefaultAction());
		for(int j = 0; j < numOfSensorsToSwitch; j++){
			Sensor sensor = newShip.getSensors().get(r.nextInt(16));
			mutateSensor(sensor);
		}
		
		
		/* 3 parent crossover - randomly select 3 parents */
		/* crossover at 1 points between 0 and 15 sensors */
		/* mutate child */
		/* redo population - 2 times */
		for(int i = 2; i < numberOfShips; i++){
			
			int indexParent1 = r.nextInt(numberOfShips + 1);
			int indexParent2 = r.nextInt(numberOfShips + 1);
			int indexParent3 = r.nextInt(numberOfShips + 1);
			
			int point1 = r.nextInt(16);
			int point2 = r.nextInt(16);
			
			Ship child = new Ship();
			ArrayList <Sensor> sensors = new ArrayList <Sensor> ();
			
			
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
