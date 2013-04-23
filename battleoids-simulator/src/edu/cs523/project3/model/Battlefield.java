package edu.cs523.project3.model;

import java.awt.Point;
import java.util.ArrayList;

/**
 * @author Nathan Rackley
 * The Battlefield Class handles the simulation of the entire battlefield.
 */
public class Battlefield {
	private ArrayList<ArrayList<ShipLog>> log = new ArrayList<ArrayList<ShipLog>>();
	public final int RANDOM = 0;
	public final int SEEDED = 1;
	private ArrayList<Ship> ships = new ArrayList<Ship>();
	private int randomSeed, runs, size;
	private int count;
	private int mode=0;
	private int maxRange = 100;
	private double rotate = 0.01;
	private int distance = 5;
	private boolean threaded=false; //Threading, if we get to that.
	private double shotDepletionRate, hitDepletionRate, hitRegenerationRate;
	private ArrayList<ArrayList<Score>> scores = new ArrayList<ArrayList<Score>>();
	
	/*
	public Battlefield(ArrayList<Ship> ships, int runs, int size, int maxRange, double shotDepletionRate, double hitDepletionRate, double hitRegenerationRate, int positioning, int randomSeed){
		
	}*/
	/**
	 * The constructor with overriding starter coordinates.
	 * @param ships
	 * @param x
	 * @param y
	 * @param angle
	 * @param count
	 * @param runs
	 * @param size
	 */
	public Battlefield(ArrayList<Ship> ships, int[] x, int[] y, double[] angle, int count, int runs, int size){
		this.ships = ships;
		this.runs = runs;
		this.size = size;
		this.count = count;
		for(int i=0;i<count;i++){
			this.ships.get(i).setLocation(new Point(x[i], y[i]));
			this.ships.get(i).setFacing(angle[i]);
			System.out.println("Ship X: " + this.ships.get(i).getLocation().x + " Ship Y: " + this.ships.get(i).getLocation().y);
		}
	}

	public Battlefield(ArrayList<Ship> ships, int runs, int size, int positioning){
		this.ships = ships;
		this.runs = runs;
		this.size = size;
		this.count = this.ships.size();
		//Here we locate the positioning information.
		if(positioning==this.RANDOM){
			this.mode = 1;
		}
	}

	
	public Battlefield(){
		System.out.println("Initializing generic battlefield...");
	}
	
	public int getCount(){
		return this.count;
	}
	
	public void setCount(int count){
		this.count = count;
	}
	
	public ArrayList<ArrayList<Score>> run(boolean log){
		int runs = this.runs;
		runs = 1;	//Override with a single run.
		
		//Run all simulations.
	//System.out.println("RUNNING...");	
		for(int i=0;i<this.count;i++){
			//Randomize the starting locations.
			
			//For each ship....
			//System.out.println(i);
			System.out.println(i + " - X: " + this.ships.get(i).getLocation().x + " Y: " + this.ships.get(i).getLocation().y + " A: " + this.ships.get(i).getFacing() );
				
		}
		//Begin Program Run
		
		//Store Scores as new Score objects in scores Array
		
		
		
		//Return all scores.
		return scores;
	}
	
	/**
	 * Function run()
	 * 	Initializes, then runs the current number of battlefield simulations.
	 * @return scores A 2d array of the scores of all the runs, indexed by run, then by ship.
	 */
	public ArrayList<ArrayList<Score>> run(){
		return this.run(false);
	}

	public ArrayList<Ship> getShips() {
		return ships;
	}

	public void setShips(ArrayList<Ship> ships) {
		this.ships = ships;
	}

	public int getRandomSeed() {
		return randomSeed;
	}

	public void setRandomSeed(int randomSeed) {
		this.randomSeed = randomSeed;
	}

	public boolean isThreaded() {
		return threaded;
	}

	public void setThreaded(boolean threaded) {
		this.threaded = threaded;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getRuns() {
		return runs;
	}

	public void setRuns(int runs) {
		this.runs = runs;
	}

	public int getMaxRange() {
		return maxRange;
	}

	public void setMaxRange(int maxRange) {
		this.maxRange = maxRange;
	}

	public double getShotDepletionRate() {
		return shotDepletionRate;
	}

	public void setShotDepletionRate(double shotDepletionRate) {
		this.shotDepletionRate = shotDepletionRate;
	}

	public double getHitDepletionRate() {
		return hitDepletionRate;
	}

	public void setHitDepletionRate(double hitDepletionRate) {
		this.hitDepletionRate = hitDepletionRate;
	}

	public double getHitRegenerationRate() {
		return hitRegenerationRate;
	}

	public void setHitRegenerationRate(double hitRegenerationRate) {
		this.hitRegenerationRate = hitRegenerationRate;
	}
	
	
	
}
