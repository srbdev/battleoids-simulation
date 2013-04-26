package edu.cs523.project3.model;

import java.awt.Point;
import java.util.ArrayList;

public class Ship {
	private Point location = new Point(0,0);
	public double x=0;
	public double y=0;
	private int velocity=0, defaultAction=0;
	private double vector=0, facing=0;
	private Arc gun = new Arc(0, 0.5, 1);
	//private boolean firing = false;
	private boolean hit = false;
	private Score score = new Score();
	private ArrayList<Sensor> sensors = new ArrayList<Sensor>();
	private boolean move = false;
	private boolean right = false;
	private boolean left = false;
	private boolean fire = false;
	public Ship(Ship s, int x, int y, double facing){
		this.sensors = s.getSensors();
		this.defaultAction = s.getDefaultAction();
		this.location.x = x;
		this.location.y = y;
		this.facing = facing;
		this.gun = s.gun;
	}
	
	public Ship(ArrayList<Sensor> sensors, int defaultAction, Point location, double facing, Arc gun){
		this.sensors = sensors;
		this.defaultAction = defaultAction;
		this.location = location;
		this.facing = facing;
		this.gun = gun;
	}
	
	public Ship(ArrayList<Sensor> sensors, int defaultAction, Point location, double facing){
		this.sensors = sensors;
		this.defaultAction = defaultAction;
		this.location = location;
		this.facing = facing;
	}
	
	public Ship(ArrayList<Sensor> sensors, int defaultAction){
		this.sensors = sensors;
		this.defaultAction = defaultAction;
	}
	
	public Ship(){}
	
	/**
	 * Function Detect
	 * Runs detection for given distances and ranges for all sensors.
	 * @param distances
	 * @param angles
	 * @param count
	 * @param ignore
	 */
	public void detect(double[] distances, double[] angles, boolean[] active, int count, int ignore, int maxRange){
		//System.out.println("Detect within ship sensors.");
		for(int i=0;i<this.sensors.size();i++){ //Iterate through all sensors.
			
			if(this.sensors.get(i).detect(distances, angles, active, count, this.facing, ignore, maxRange)==true){
				// Set the Sensor flags if the sensor is triggered.
				setActionFlags(this.sensors.get(i).getMode());
			}			
		}
		//IF no flags are triggered, set default action.
		if((this.fire==false)&&(this.fire==false)&&(this.fire==false)&&(this.fire==false)) setActionFlags(this.defaultAction);
	}
	
	/**
	 * Function setActionsFalse
	 * Sets all actions on this ship to false.
	 */
	public void setActionsFalse(){
		//Set ship sensor flags to false.
		this.fire = false;
		this.move = false;
		this.left = false;
		this.right = false;		
	}
	
	/**
	 * Sets the action flags based on the action integer.
	 * @param action
	 */
	public void setActionFlags(int action){
		if((action & Action.FIRE)==Action.FIRE) this.fire = true;
		if((action & Action.MOVE)==Action.MOVE) this.move = true;
		if((action & Action.LEFT)==Action.LEFT) this.left = true;
		if((action & Action.RIGHT)==Action.RIGHT) this.right = true;
	}
	
	public Point getStartingLocation(){
		return this.location;
	}
	
	public Point getLocation() {
		return new Point((int) this.x, (int) this.y);
	}
	public void setLocation(Point location) {
		this.location = location;
		this.x = location.x;
		this.y = location.y;
	}
	public void setLocation(double x, double y){
		this.location = new Point((int)x, (int)y);
		this.x = x;
		this.y = y;
	}
	public void setLocation(int x, int y){
		this.setLocation((double)x,(double)y);
	}
	public ArrayList<Sensor> getSensors() {
		return sensors;
	}
	public void setSensors(ArrayList<Sensor> sensors) {
		this.sensors = sensors;
	}

	public double getFacing() {
		return facing;
	}
	public void setFacing(double facing) {
		this.facing = facing;
	}
	public double getVector() {
		return vector;
	}
	public void setVector(double vector) {
		this.vector = vector;
	}
	public int getVelocity() {
		return velocity;
	}
	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}
	public Score getScore() {
		return score;
	}
	public void setScore(Score score) {
		this.score = score;
	}
	public Arc getGun() {
		return gun;
	}
	public void setGun(Arc gun) {
		this.gun = gun;
	}
	public int getDefaultAction() {
		return defaultAction;
	}
	public void setDefaultAction(int defaultAction) {
		this.defaultAction = defaultAction;
	}

	public boolean isHit() {
		return hit;
	}

	public void setHit(boolean hit) {
		this.hit = hit;
	}

	public boolean isMoving() {
		return move;
	}

	public void setMoving(boolean move) {
		this.move = move;
	}

	public boolean isTurningRight() {
		return right;
	}

	public void setTurningRight(boolean right) {
		this.right = right;
	}

	public boolean isTurningLeft() {
		return left;
	}

	public void setTurningLeft(boolean left) {
		this.left = left;
	}

	public boolean isFiring() {
		return fire;
	}

	public void setFiring(boolean fire) {
		this.fire = fire;
	}
}
