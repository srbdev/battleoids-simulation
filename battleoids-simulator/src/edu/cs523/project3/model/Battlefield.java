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
	private int randomSeed, runs, size=500;
	private int count;
	private int mode=0;
	private int maxRange = 100;
	private double rotate = 0.01;
	private int distance = 2;
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
	/**
	 * Returns the minimum angle and distance between two points on the field of a given size.
	 * @param a
	 * @param b
	 * @return
	 */
	public double[] getMinAngleDistance(Point a, Point b){
		double[] retVal = new double[2];
		int x=0, y=0, dx, dy;
		double distance, angle;
		retVal[0] = this.size*2;
		//System.out.println(a.x + ", " + a.y + " " + b.x + ", " + b.y);
		//dx = b.x - a.x;
		//dy = a.y - b.y;
		//Iterate through quadrants for simulation wrap-around. All nine quadrants
		//need to be checked for shortest distance.
		for(int i=0;i<3;i++){
			int tdx=0, tdy=0;
			if(i==0) tdy = b.y-this.size;
			if(i==1) tdy = b.y;
			if(i==2) tdy = b.y+this.size;
			for(int j=0;j<3;j++){
				if(j==0) tdx = b.x-this.size;
				if(j==1) tdx = b.x;
				if(j==2) tdx = b.x+this.size;
				dx = tdx - a.x;
				dy = a.y - tdy;
				distance = Math.hypot(dx,  dy);
				angle = Math.atan2(dy, dx)/Math.PI;
				//System.out.println(dx + ":" + dy + " " + distance + " " + angle);
				//		System.out.println((tdx-a.x)+" "+(a.y - tdy));
				
				//Calculate delta distance
				//dx = a.x - x;
				//dy = a.y - y;
				//distance = Math.sqrt(dx^2+dy^2);
				//System.out.println(distance);
				if(distance < retVal[0]){
					retVal[0]=distance;
					retVal[1]=angle;
				}
			}
		}
		return retVal;
	}
	
	
	/**
	 * Function sense
	 * Returns boolean array of sensors triggered given angles and distances to other ships.
	 * @param s
	 * @param distances
	 * @param angles
	 * @param count
	 * @return
	 */
	public boolean[] sense(Ship s, double[] distances, double[] angles, int count){
		boolean[] retVal = new boolean[2];
		//todo: Run checks here
		
		return retVal;
	}
	
	public ArrayList<ArrayList<Score>> run(boolean log){
		int runs = this.runs;
		runs = 1;	//Override with a single run.
		
		//Run all simulations.
		//System.out.println("RUNNING...");	
		for(int i=0;i<this.count;i++){
			//Randomize the starting locations.
			if(this.mode==1){
				//Create random X, Y, and facing for ship.
			}
			//For each ship output identifier to the console.
			System.out.println((i+1) + " - X: " + this.ships.get(i).getLocation().x + " Y: " + this.ships.get(i).getLocation().y + " A: " + this.ships.get(i).getFacing() );
				
		}
		//Begin Program Run
		for(int r=0;r<runs;r++){				//For each time step
			double[][] distances 	= new double[this.count][this.count];	//2D array of shortest distances from ship to ship
			double[][] angles 		= new double[this.count][this.count];	//2D array of shortest distance angles from ship to ship
			double[] temp = new double[2];			//Temp variable for angle/distances between ships.
			int[] newX = new int[this.count];		//Array of new X coordinates for ships.
			int[] newY = new int[this.count];		//Array of new Y coordinates for ships.
			double[] newA = new double[this.count];	//Array of new angles for ships.
			
			for(int i=0;i<this.count;i++){//pre-ship iteration, setting ship flags.
				this.ships.get(i).setHit(false);		//Ships are not being hit anymore.
				this.ships.get(i).setActionsFalse();	//Set ship actions to false for this round.
			}
			
			for(int i=0;i<this.count;i++){		//For each ship, Update and Calculate
				//Begin ship-to-ship comparisons.
				for(int j=0;j<this.count;j++){	//For each other ship
					if((i!=j)&&(distances[i][j]==0)){	//Where the ship is not the same as current ship and value not already set.
						//System.out.println(i + " to " + j);
						temp = getMinAngleDistance(this.ships.get(i).getLocation(), this.ships.get(j).getLocation());
						distances[i][j] = temp[0];
						angles[i][j] = temp[1];
						distances[j][i] = temp[0];
						angles[j][i] = (1 + temp[1]) % 2;
						System.out.println((i+1) + " to " + (j+1) + " : Distance: " + temp[0] + " Angle: " + temp[1] + ", " + angles[j][i]);
						
					}
				}
				//Perform checks for each sensor. Set sensor flags within ships.	
				//This is as good a place to perform checks as any.
				this.ships.get(i).detect(distances[i], angles[i], this.ships.size(), i, this.maxRange);
				System.out.print("Ship " + (i+1) + ": ");
				if(this.ships.get(i).isMoving()) System.out.print("M"); 		else System.out.print("-");
				if(this.ships.get(i).isFiring()) System.out.print("F"); 		else System.out.print("-");
				if(this.ships.get(i).isTurningLeft()) System.out.print("L");	else System.out.print("-");
				if(this.ships.get(i).isTurningRight()) System.out.print("R"); 	else System.out.print("-");
				System.out.println("");
				
				//todo: If firing is triggered, get firing solution on any ships.
				
				//todo: Calculate if any ships are hit.
				
			}
			//todo: set new ship locations and facings based on sensor flags.
			for(int i=0;i<this.count;i++){
				double theta = this.ships.get(i).getFacing();
				System.out.print("Ship "+(i+1)+" "+theta+" : ");
				if(this.ships.get(i).isTurningLeft()){
					this.ships.get(i).setFacing(theta+this.rotate);
				}
				if(this.ships.get(i).isTurningRight()){
					this.ships.get(i).setFacing(theta-this.rotate);
				}
				theta = this.ships.get(i).getFacing();	//Update theta angle for new direction.
				if(this.ships.get(i).isMoving()){
					int x = (int)(this.distance*Math.cos(theta*Math.PI));
					int y = (int)(this.distance*Math.sin(theta*Math.PI));
					System.out.print("("+x+","+y+") : ");
					Point p = this.ships.get(i).getLocation();
					p.x = (p.x + x) % this.size;
					p.y = (p.y - y) % this.size;
					this.ships.get(i).setLocation(p);
				}
				System.out.print(this.ships.get(i).getFacing());
				System.out.println("");
			}			
		}
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
