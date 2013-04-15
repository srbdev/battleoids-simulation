package edu.cs523.project3.model;

import java.awt.Point;
import java.util.ArrayList;

public class Ship {
	private Point location = new Point(0,0);
	private int velocity=0, defaultAction=0;
	private double vector=0, facing=0;
	private Arc gun = new Arc(0, 0.01, 1);
	private Score score = new Score();
	private ArrayList<Sensor> sensors = new ArrayList<Sensor>();
	
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
	


	public Point getLocation() {
		return location;
	}
	public void setLocation(Point location) {
		this.location = location;
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


}
