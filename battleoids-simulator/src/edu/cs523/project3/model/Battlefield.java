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
	private Score[] startingScores;
	private int randomSeed, runs=1, size=500;
	private double startX[], startY[], startA[];
	private int count;
	private int steps = 1000;
	private int mode=-1;
	private int maxRange = 100;
	private double rotate = 0.02;
	private double distance = 4;
	private boolean threaded=false; //Threading, if we get to that.
	private double shotDepletionRate=0.0, hitDepletionRate=0.01, hitRegenerationRate=0.01;
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
	 * @param runs Independent Simulation Runs
	 * @param size
	 */
	public Battlefield(ArrayList<Ship> ships, double[] x, double[] y, double[] angle, int count, int runs, int size){
		this.ships = ships;
		this.runs = runs;
		this.size = size;
		this.count = count;
		this.startingScores = new Score[this.count];
		this.startX = x;
		this.startY = y;
		this.startA = angle;
		for(int i=0;i<count;i++){
			this.ships.get(i).setLocation(x[i], y[i]);
			this.ships.get(i).setFacing(angle[i]);
			
			Score sc = ships.get(i).getScore();	//Deep copy score
			this.startingScores[i] = new Score(sc.energy, sc.hits, sc.misses, sc.time, sc.ship, sc.count);
			
			System.out.println("Ship X: " + this.ships.get(i).getLocation().x + " Ship Y: " + this.ships.get(i).getLocation().y);
		}
	}
	/**
	 * The constructor with overriding starter coordinates.
	 * @param ships
	 * @param x
	 * @param y
	 * @param angle
	 * @param count
	 * @param runs Time steps
	 * @param size
	 */
	public Battlefield(ArrayList<Ship> ships, int[] x, int[] y, double[] angle, int count, int runs, int size){
		this.ships = ships;
		this.runs = runs;
		this.size = size;
		this.count = count;
		this.startA = angle;
		this.startingScores = new Score[this.count];
		for(int i=0;i<count;i++){
			this.startX[i] = (double) x[i];
			this.startY[i] = (double) y[i];
			this.ships.get(i).setLocation(x[i], y[i]);
			this.ships.get(i).setFacing(angle[i]);
			
			Score sc = ships.get(i).getScore();	//Deep copy score
			this.startingScores[i] = new Score(sc.energy, sc.hits, sc.misses, sc.time, sc.ship, sc.count);
			
			//this.startingScores[i] = new Score(ships.get(i).getScore());
			System.out.println("Ship X: " + this.ships.get(i).getLocation().x + " Ship Y: " + this.ships.get(i).getLocation().y);
		}
	}

	public Battlefield(ArrayList<Ship> ships, int runs, int size, int positioning){
		this.ships = ships;
		this.runs = runs;
		this.size = size;
		this.count = this.ships.size();
		this.startX = new double[this.count];
		this.startY = new double[this.count];
		this.startingScores = new Score[this.count];
		
		//Here we locate the positioning information.
		if(positioning==this.RANDOM){
			this.mode = 1;
		}else{
			for(int i=0;i<ships.size();i++){
				this.startX[i] = ships.get(i).x;
				this.startY[i] = ships.get(i).y;
				this.startA[i] = ships.get(i).getFacing();
				
				Score sc = ships.get(i).getScore();	//Deep copy score
				this.startingScores[i] = new Score(sc.energy, sc.hits, sc.misses, sc.time, sc.ship, sc.count);
				
				//this.startingScores[i] = new Score(ships.get(i).getScore());
			}
		}
	}

	
	public Battlefield(){
		System.out.println("Initializing Generic Battlefield...");
		this.mode = -1;
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
		double x=0, y=0, dx, dy;
		double distance, angle;
		retVal[0] = this.size*2;
		//Iterate through quadrants for simulation wrap-around. All nine quadrants
		//need to be checked for shortest distance.
		for(int i=0;i<3;i++){
			double tdx=0, tdy=0;
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
	
	/**
	 * Basic firing solution given probability p.
	 * @param p
	 * @return
	 */
	public boolean firing(double p){
		double r = Math.random();
		if(r < p/(Math.sqrt(1+p*p))) return true;
		return false;
	}
	
	public ArrayList<ArrayList<Score>> run(boolean log){
		//int steps = this.steps;
		int winner, alive;
		if(this.count==0){
			this.count = this.ships.size();	//Set ship count if unset.
		}
		//steps = 10;	//Override with a single run.
		if(this.mode==-1){
			this.startX = new double[this.count];
			this.startY = new double[this.count];
			this.startA = new double[this.count];
			this.startingScores = new Score[this.count];
			for(int i=0;i<this.count;i++){	
				startX[i] = this.ships.get(i).x;
				startY[i] = this.ships.get(i).y;
				startA[i] = this.ships.get(i).getFacing();
				//startingScores[i] = new Score(this.ships.get(i).getScore());
				Score sc = ships.get(i).getScore();	//Deep copy score
				this.startingScores[i] = new Score(sc.energy, sc.hits, sc.misses, sc.time, sc.ship, sc.count);
				
				//System.out.println((i+1)+": "+this.ships.get(i).x+","+this.ships.get(i).y+" "+this.ships.get(i).getFacing());
			}
			this.mode=2;
		}
		/*for(int i=0;i<this.count;i++){
			System.out.println((i+1)+" "+startX[i]+","+startY[i]+" "+startA[i]);
		}*/
		
		//Run all simulations.
		for(int q=0;q<runs;q++){
			winner=-1;
			alive = this.count;
			System.out.println("SIMULATION " + (q+1));	
			for(int i=0;i<this.count;i++){
				//Randomize the starting locations.
				if(this.mode==1){
					//todo: Create random X, Y, and facing for ship.
					int nx = (int)(Math.random()*this.size);
					int ny = (int)(Math.random()*this.size);
					
					this.ships.get(i).x = nx;
					this.ships.get(i).y = ny;
					this.ships.get(i).setLocation(nx, ny);
					this.ships.get(i).setFacing(Math.random()*2);
					
				}else{
					//todo: grab starting locations if this is first run and they weren't set.
					this.ships.get(i).x = startX[i];
					this.ships.get(i).y = startY[i];
					this.ships.get(i).setLocation(startX[i], startY[i]);
					this.ships.get(i).setFacing(startA[i]);
					
					
					//this.ships.get(i).setScore(new Score(startingScores[i]));
				}
				//Score sc = this.startingScores[i];	//Deep copy score
				//Score sc2 = new Score(sc.energy, sc.hits, sc.misses, sc.time, sc.ship, sc.count);
				//reset all scores.
				Score sc2 = new Score(0.5, 0, 0, 0, new int[this.count], this.count);
				this.ships.get(i).setScore(sc2);
				
				//For each ship output identifier to the console.
				//System.out.println((i+1) + " - X: " + this.ships.get(i).getLocation().x + " Y: " + this.ships.get(i).getLocation().y + " A: " + this.ships.get(i).getFacing() );
				//System.out.println((i+1)+" "+startX[i]+","+startY[i]+" "+startA[i]);
				//System.out.println((i+1)+": "+this.ships.get(i).x+","+this.ships.get(i).y+" "+this.ships.get(i).getFacing());
			}
			//Begin Program Run
			for(int r=0;r<steps;r++){				//For each time step
				if(winner==-1){
					double[][] distances 	= new double[this.count][this.count];	//2D array of shortest distances from ship to ship
					double[][] angles 		= new double[this.count][this.count];	//2D array of shortest distance angles from ship to ship
					double[] temp = new double[2];			//Temp variable for angle/distances between ships.
					
					for(int i=0;i<this.count;i++){//pre-ship iteration, setting ship flags.
						this.ships.get(i).setHit(false);		//Ships are not being hit anymore.
						this.ships.get(i).setActionsFalse();	//Set ship actions to false for this round.
					}
					
					for(int i=0;i<this.count;i++){		//For each ship, Update and Calculate
						//Begin ship-to-ship comparisons.
						boolean[] activeShips = new boolean[this.count];
						alive = 0;
						for(int j=0;j<this.count;j++){	//For each other ship
							if((i!=j)&&(distances[i][j]==0)){	//Where the ship is not the same as current ship and value not already set.
								//System.out.println(i + " to " + j);
								temp = getMinAngleDistance(this.ships.get(i).getLocation(), this.ships.get(j).getLocation());
								distances[i][j] = temp[0];
								angles[i][j] = temp[1];
								distances[j][i] = temp[0];
								angles[j][i] = (1 + temp[1]) % 2;
		//						System.out.println((i+1) + " to " + (j+1) + " : Distance: " + temp[0] + " Angle: " + temp[1] + ", " + angles[j][i]);		
							}
							if(this.ships.get(j).getScore().energy>0){
								activeShips[j]=true; 
								alive++;
							} else activeShips[j]=false;
						}
						//System.out.println(alive);
						if((this.ships.get(i).getScore().energy>0)&&(alive>1)){  //If the ship is out of energy, it does not target or fire.
							
							//Perform checks for each sensor. Set sensor flags within ships.	
							//This is as good a place to perform checks as any.
							this.ships.get(i).detect(distances[i], angles[i], activeShips, this.ships.size(), i, this.maxRange);
						/*	System.out.print("Ship " + (i+1) + ": ");
							if(this.ships.get(i).isMoving()) System.out.print("M"); 		else System.out.print("-");
							if(this.ships.get(i).isFiring()) System.out.print("F"); 		else System.out.print("-");
							if(this.ships.get(i).isTurningLeft()) System.out.print("L");	else System.out.print("-");
							if(this.ships.get(i).isTurningRight()) System.out.print("R"); 	else System.out.print("-");
							System.out.println(" Energy:" + this.ships.get(i).getScore().energy);*/
							
							//If firing is triggered, get firing solution on any ships.
							boolean[] attack = new boolean[this.count];
							if(this.ships.get(i).isFiring()){
								this.ships.get(i).getScore().energy -= shotDepletionRate;
								for(int j=0;j<this.count;j++){
									if(i!=j){
										attack[j] = this.ships.get(i).getGun().inRange(distances[i][j], angles[i][j]-this.ships.get(i).getFacing(), (this.maxRange));
										//System.out.println((i+1)+" vs " + (j+1) + " : " + attack[j]);
										//todo: Calculate if any ships are hit.
										if(activeShips[j]==true){
											if(attack[j]==true){
												if(this.firing(this.ships.get(i).getScore().energy)==true){ //Calculate firing solution based upon energy levels.
													this.ships.get(i).getScore().hits++;
													//System.out.println((i+1) + " vs "+(j+1)+ " HIT");
													this.ships.get(i).getScore().energy += hitRegenerationRate;
													this.ships.get(j).getScore().energy -= hitDepletionRate;
													this.ships.get(j).setHit(true);
													this.ships.get(i).getScore().ship[j]++;
												} else {
													this.ships.get(i).getScore().misses++;
													//System.out.println((i+1) + " vs "+(j+1)+ " MISS");
												}
											}
										}
									}
								}
							}					
						} else if(alive==1) for(int k=0;k<this.count;k++) if(activeShips[k]==true) winner=k; //Get the winner's index
					}
				
					//Set new ship locations and facings based on sensor flags.
					for(int i=0;i<this.count;i++){
						if(this.ships.get(i).getScore().energy>0){  //If the ship is out of energy, it does not move.
							double theta = this.ships.get(i).getFacing();
							//System.out.print("Ship "+(i+1)+" "+theta+" : ");
							if(this.ships.get(i).isTurningLeft())  this.ships.get(i).setFacing(theta+this.rotate);
							if(this.ships.get(i).isTurningRight()) this.ships.get(i).setFacing(theta-this.rotate);
							theta = this.ships.get(i).getFacing();	//Update theta angle for new direction.
							if(this.ships.get(i).isMoving()){
								double x = (this.distance*Math.cos(theta*Math.PI));
								double y = (this.distance*Math.sin(theta*Math.PI));
								double nx = this.ships.get(i).x;
								double ny = this.ships.get(i).y;
								nx = ((nx + x)+(2*this.size)) % this.size;
								ny = ((ny - y)+(2*this.size)) % this.size;
								this.ships.get(i).setLocation(nx, ny);
								//System.out.print("("+(int)nx+","+(int)ny+") : ");
							}
							this.ships.get(i).getScore().time++;
						}
						//System.out.print(this.ships.get(i).getFacing());
						//System.out.println("");
					}
				}
			}
			System.out.println("Winner: "+(winner+1));
			//Store Scores as new Score objects in scores Array
			ArrayList<Score> scList = new ArrayList<Score>();
			for(int i=0;i<this.count;i++){
				//Copy the score.
				Score sc = this.ships.get(i).getScore();
				Score sc2 = new Score(sc.energy, sc.hits, sc.misses, sc.time, sc.ship, sc.count);
				scList.add(sc2);
			}
			scores.add(scList);
		}		
		dumpScores();
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

	public void dumpScores(){
		for(int i=0;i<scores.size();i++){
			ArrayList<Score> scList = scores.get(i);
			System.out.println("Run "+(i+1));
			for(int j=0;j<scList.size();j++){
				Score sc = scList.get(j);
				System.out.println("  Ship "+(j+1)+" - T:"+sc.time+" E:"+sc.energy+" H:"+sc.hits+" M:"+sc.misses);
			}
		}
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
	public int getSteps() {
		return steps;
	}
	public void setSteps(int steps) {
		this.steps = steps;
	}
	
	
	
}
