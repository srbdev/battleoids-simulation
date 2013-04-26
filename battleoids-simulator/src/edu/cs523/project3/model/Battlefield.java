package edu.cs523.project3.model;

import java.awt.Point;
import java.util.ArrayList;

/**
 * @author Nathan Rackley
 * The Battlefield Class handles the simulation of the entire battlefield.
 */
public class Battlefield {
	public final int RANDOM = 0, SEEDED = 1;
	private int randomSeed, runs=1, size=500, count, steps = 1000, mode=-1, maxRange = 100;
	private double startX[], startY[], startA[], rotate = 0.02, distance = 4,shotDepletionRate=0.0, hitDepletionRate=0.01, hitRegenerationRate=0.01;;
	private boolean threaded=false; //Threading, if we get to that.
	private ArrayList<Ship> ships = new ArrayList<Ship>();
	private ArrayList<ArrayList<Score>> scores = new ArrayList<ArrayList<Score>>();
	private Score[] startingScores;
	private ShipLog[][][] log;
	
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
		double[] nx = new double[this.count];
		double[] ny = new double[this.count];
		for(int i=0;i<this.count;i++){
			nx[i] = (double) x[i];
			ny[i] = (double) y[i];
		}
		this.bfSet(ships,  nx,  ny,  angle,  count,  runs,  size);
	}

	/**
	 * Declare battlefield and initialize randomized positioning.
	 * @param ships
	 * @param runs
	 * @param size
	 * @param positioning
	 */
	public Battlefield(ArrayList<Ship> ships, int runs, int size, int positioning){
		double[] xs = new double[this.count];
		double[] ys = new double[this.count];
		double[] as = new double[this.count];
		for(int i=0;i<ships.size();i++){
			xs[i] = ships.get(i).x;
			ys[i] = ships.get(i).y;
			as[i] = ships.get(i).getFacing();
		}	
		//Here we locate the positioning information.
		if(positioning==this.RANDOM) this.mode = 1;
		this.bfSet(ships, xs, ys, as, ships.size(), runs, size);
	}
	
	/**
	 * Function Battlefield
	 * Initialize generic battlefield.
	 */
	public Battlefield(){
		System.out.println("Initializing Generic Battlefield...");
		this.mode = -1;
	}
	
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
		this.bfSet(ships, x, y, angle, count, runs, size);
	}
	
	/**
	 * Set the battlefield variables that are in most variants of the constructor.
	 * @param ships
	 * @param x
	 * @param y
	 * @param angle
	 * @param count
	 * @param runs
	 * @param size
	 */
	public void bfSet(ArrayList<Ship> ships, double[] x, double[] y, double[] angle, int count, int runs, int size){
		this.ships = ships;
		this.runs = runs;
		this.size = size;
		this.count = count;
		this.startA = angle;
		this.startingScores = new Score[this.count];
		this.startX = x;
		this.startY = y;		
		for(int i=0;i<count;i++){
			this.ships.get(i).setLocation(x[i], y[i]);
			this.ships.get(i).setFacing(angle[i]);
			Score sc = ships.get(i).getScore();	//Deep copy score
			this.startingScores[i] = new Score(sc.energy, sc.hits, sc.misses, sc.time, sc.ship, sc.count);
			System.out.println("Ship X: " + this.ships.get(i).getLocation().x + " Ship Y: " + this.ships.get(i).getLocation().y);
		}	
	}	
	
	/**
	 * Returns the minimum angle and distance between two points on the field of a given size.
	 * @param a
	 * @param b
	 * @return
	 */
	public double[] getMinAngleDistance(Point a, Point b){
		double[] retVal = new double[2];
		double dx, dy;
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
	 * Basic firing solution given probability p.
	 * @param p
	 * @return
	 */
	public boolean firing(double p){
		double r = Math.random();
		if(r < p/(Math.sqrt(1+p*p))) return true;
		return false;
	}
	
	/**
	 * Dump the values of the ship sensors and ship energy to console.
	 * @param s The ship to output.
	 * @param i The identifying id of the ship.
	 */
	public void dumpShipSensors(Ship s, int i){
		System.out.print("Ship " + (i+1) + ": ");
		if(s.isMoving()) 		System.out.print("M"); 	else System.out.print("-");
		if(s.isFiring()) 		System.out.print("F"); 	else System.out.print("-");
		if(s.isTurningLeft()) 	System.out.print("L");	else System.out.print("-");
		if(s.isTurningRight())	System.out.print("R"); 	else System.out.print("-");
		System.out.println(" Energy: " + s.getScore().energy);
	}
	
	/**
	 * Function run
	 * Run the simulation.
	 * @param log Store the value in a log or not.
	 * @return
	 */
	public ArrayList<ArrayList<Score>> run(boolean log){
		if(log==true){
			this.log = new ShipLog[this.runs][this.steps][this.count];
			for(int q=0;q<this.runs;q++)
				for(int k=0; k<this.steps;k++)
					for(int i=0;i<this.count;i++)
						this.log[q][k][i] = new ShipLog();
		}
		int winner, alive;
		if(this.count==0) this.count = this.ships.size();		//Set ship count if unset.
		if(this.mode==-1){
			this.startX = new double[this.count];
			this.startY = new double[this.count];
			this.startA = new double[this.count];
			this.startingScores = new Score[this.count];
			for(int i=0;i<this.count;i++){	
				startX[i] = this.ships.get(i).x;
				startY[i] = this.ships.get(i).y;
				startA[i] = this.ships.get(i).getFacing();
				Score sc = ships.get(i).getScore();				//Deep copy score
				this.startingScores[i] = new Score(sc.energy, sc.hits, sc.misses, sc.time, sc.ship, sc.count);
			}
			this.mode=2;
		}		
		//Run all simulations.
		for(int q=0;q<runs;q++){
			winner=-1;
			alive = this.count;
			System.out.println("SIMULATION " + (q+1));	
			for(int i=0;i<this.count;i++){
				//Randomize the starting locations.
				if(this.mode==1){
					//Create random X, Y, and facing for ship.
					int nx = (int)(Math.random()*this.size);
					int ny = (int)(Math.random()*this.size);
					this.ships.get(i).x = nx;
					this.ships.get(i).y = ny;
					this.ships.get(i).setLocation(nx, ny);
					this.ships.get(i).setFacing(Math.random()*2);
				}else{
					this.ships.get(i).x = startX[i];
					this.ships.get(i).y = startY[i];
					this.ships.get(i).setLocation(startX[i], startY[i]);
					this.ships.get(i).setFacing(startA[i]);
				}
				Score sc2 = new Score(0.5, 0, 0, 0, new int[this.count], this.count);
				this.ships.get(i).setScore(sc2);
			}
			//Begin Program Run
			for(int r=0;r<steps;r++){												//For each time step
				if(winner==-1){
					double[][] distances 	= new double[this.count][this.count];	//2D array of shortest distances from ship to ship
					double[][] angles 		= new double[this.count][this.count];	//2D array of shortest distance angles from ship to ship
					double[] temp = new double[2];									//Temp variable for angle/distances between ships.
					for(int i=0;i<this.count;i++){									//pre-ship iteration, setting ship flags.
						this.ships.get(i).setHit(false);							//Ships are not being hit anymore.
						this.ships.get(i).setActionsFalse();						//Set ship actions to false for this round.
					}			
					for(int i=0;i<this.count;i++){									//For each ship, Update and Calculate
						//Begin ship-to-ship comparisons.
						boolean[] activeShips = new boolean[this.count];
						if(log==true) this.log[q][r][i] = new ShipLog(this.ships.get(i));	//Log if logging.
						alive = 0;
						for(int j=0;j<this.count;j++){								//For each other ship
							if((i!=j)&&(distances[i][j]==0)){						//Where the ship is not the same as current ship and value not already set.
								temp = getMinAngleDistance(this.ships.get(i).getLocation(), this.ships.get(j).getLocation());
								distances[i][j] = temp[0];
								angles[i][j] = temp[1];
								distances[j][i] = temp[0];
								angles[j][i] = (1 + temp[1]) % 2;
							}
							if(this.ships.get(j).getScore().energy>0){
								activeShips[j]=true; 
								alive++;
							} else activeShips[j]=false;
						}
						if((this.ships.get(i).getScore().energy>0)&&(alive>1)){  	//If the ship is out of energy, it does not target or fire.							
							//Perform checks for each sensor. Set sensor flags within ships.
							this.ships.get(i).detect(distances[i], angles[i], activeShips, this.ships.size(), i, this.maxRange);
							//dumpShipSensors(this.ships.get(i), i);
							boolean[] attack = new boolean[this.count];				//If firing is triggered, get firing solution on any ships.
							if(this.ships.get(i).isFiring()){
								this.ships.get(i).getScore().energy -= shotDepletionRate;
								for(int j=0;j<this.count;j++){
									if(i!=j){
										attack[j] = this.ships.get(i).getGun().inRange(distances[i][j], angles[i][j]-this.ships.get(i).getFacing(), (this.maxRange));
										//Calculate if any ships are hit.
										if(activeShips[j]==true){
											if(attack[j]==true){
												if(this.firing(this.ships.get(i).getScore().energy)==true){ //Calculate firing solution based upon energy levels.
													this.ships.get(i).getScore().hits++;
													this.ships.get(i).getScore().energy += hitRegenerationRate;
													this.ships.get(j).getScore().energy -= hitDepletionRate;
													this.ships.get(j).setHit(true);
													this.ships.get(i).getScore().ship[j]++;
												} else this.ships.get(i).getScore().misses++;
											}
										}
									}
								}
							}					
						} else if(alive==1) for(int k=0;k<this.count;k++) if(activeShips[k]==true) winner=k; //Get the winner's index
					}				
					//Set new ship locations and facings based on sensor flags.
					for(int i=0;i<this.count;i++){
						if(this.ships.get(i).getScore().energy>0){  		//If the ship is out of energy, it does not move.
							double theta = this.ships.get(i).getFacing();
							if(this.ships.get(i).isTurningLeft())  this.ships.get(i).setFacing(theta+this.rotate);
							if(this.ships.get(i).isTurningRight()) this.ships.get(i).setFacing(theta-this.rotate);
							theta = this.ships.get(i).getFacing();			//Update theta angle for new direction.
							if(this.ships.get(i).isMoving()){
								double x = (this.distance*Math.cos(theta*Math.PI));
								double y = (this.distance*Math.sin(theta*Math.PI));
								double nx = this.ships.get(i).x;
								double ny = this.ships.get(i).y;
								nx = ((nx + x)+(2*this.size)) % this.size;
								ny = ((ny - y)+(2*this.size)) % this.size;
								this.ships.get(i).setLocation(nx, ny);
							}
							this.ships.get(i).getScore().time++;
						}
					}
				}
			}
			System.out.println("Winner: "+(winner+1));
			ArrayList<Score> scList = new ArrayList<Score>();
			for(int i=0;i<this.count;i++){ 							//Store Scores as new Score objects in scores Array
				Score sc = this.ships.get(i).getScore();
				Score sc2 = new Score(sc.energy, sc.hits, sc.misses, sc.time, sc.ship, sc.count);
				scList.add(sc2);
			}
			scores.add(scList);
		}
		dumpScores();
		dumpLog();
		return scores;  //Return all scores.
	}
	
	/**
	 * Function run()
	 * 	Initializes, then runs the current number of battlefield simulations.
	 * @return scores A 2d array of the scores of all the runs, indexed by run, then by ship.
	 */
	public ArrayList<ArrayList<Score>> run(){
		return this.run(true);
	}

	/**
	 * Function dumpLog
	 * Output the log energy to the console.
	 */
	public void dumpLog(){
		for(int q=0;q<this.runs;q++){
			boolean out = true;
			for(int k=0; (k<this.steps);k++){
				for(int i=0;i<this.count;i++){
					if(this.log[q][k][i].set==true) System.out.printf("%.2f ", this.log[q][k][i].score.energy);
					else out = false;
				}
				if(out==true) System.out.printf("\n");
			}
		}		
	}
	
	/**
	 * Function dumpScores()
	 * Outputs the scores after a run to the console.
	 */
	public void dumpScores(){
		for(int i=0;i<scores.size();i++){
			ArrayList<Score> scList = scores.get(i);
			System.out.println("Run "+(i+1));
			for(int j=0;j<scList.size();j++){
				Score sc = scList.get(j);
				System.out.printf("  Ship %d - T: %d E: %.2f H: %.0f M: %.0f\n    ", (j+1), sc.time, Math.abs(sc.energy), sc.hits, sc.misses);
				int[] ships = sc.ship;
				for(int k=0;k<this.count;k++) System.out.print(ships[k]+" ");
				System.out.println("");
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
	
	public int getCount(){
		return this.count;
	}
	
	public void setCount(int count){
		this.count = count;
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
	
	public ShipLog[][][] getLog() {
		return log;
	}
	
	public void setLog(ShipLog[][][] log) {
		this.log = log;
	}
}
