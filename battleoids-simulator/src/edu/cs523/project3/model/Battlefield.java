package edu.cs523.project3.model;

import java.util.ArrayList;

/**
 * @author Nathan Rackley
 * The Battlefield Class handles the simulation of the entire battlefield.
 */
public class Battlefield {
	public final int RANDOM = 0;
	public final int SEEDED = 1;
	private ArrayList<Ship> ships = new ArrayList<Ship>();
	private int randomSeed, runs, size, maxRange;
	private boolean threaded=false; //Threading, if we get to that.
	private double shotDepletionRate, hitDepletionRate, hitRegenerationRate;
	private ArrayList<ArrayList<Score>> scores = new ArrayList<ArrayList<Score>>();
	
	public Battlefield(ArrayList<Ship> ships, int runs, int size, int maxRange, double shotDepletionRate, double hitDepletionRate, double hitRegenerationRate, int positioning, int randomSeed){
		
	}

	public Battlefield(ArrayList<Ship> ships, int runs, int size, int positioning){
		
	}

	
	public Battlefield(){
		
	}
	
	/**
	 * Function run()
	 * 	Runs the current number of battlefield simulations.
	 * @return scores A 2d array of the scores of all the runs, indexed by run, then by ship.
	 */
	public ArrayList<ArrayList<Score>> run(){
		//Run all simulations.
		
		//Return all scores.
		return scores;
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
