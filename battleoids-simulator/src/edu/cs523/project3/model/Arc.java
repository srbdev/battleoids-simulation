package edu.cs523.project3.model;

import java.io.Serializable;

public class Arc implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2549257527285324633L;
	private double width=0, facing=0, range=0;
	private double rangeError=0;
	public Arc(double facing, double width, double range) {
		this.setWidth(width);
		this.setFacing(facing);
		this.setRange(range);
	}
	public Arc() {
		this.setWidth(0);
		this.setFacing(0);
		this.setRange(0);
	}
	
	/**
	 * Function inRange
	 * Determines if a given distance/angle location is within the range of the sensor.
	 * @param distance
	 * @param angle
	 * @return
	 */
	public boolean inRange(double distance, double angle, int maxRange ){
		//System.out.print("Ranging ");
		if(distance <= this.range*maxRange){
			double start = (this.facing - this.width/2)-this.rangeError;
			double end   = (this.facing + this.width/2)+this.rangeError;
			double a1 = ((angle-4)%2);
			double a2 = ((angle+4)%2);
			//System.out.print("In Range " + a1 + "," + a2 + " "+start+":"+end+" ");
			if(((a1 >= start) && (a1 <= end))||((a2 >= start) && (a2 <= end))){ 
				return true;
			}
		}
		return false;
	}
	
	
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		if(width > 1) this.width = 1;
		else if(width < 0) this.width = 0;
		else this.width = width;		
	}
	
	public double getFacing() {
		return facing;
	}
	public void setFacing(double facing) {
		if(facing <0){
			this.facing = (facing % 2) + 2;
		}else this.facing = facing % 2;
	}
	public double getRange() {
		return range;
	}
	public void setRange(double range) {
		/*if(range > 1 ) this.range = range % 1;
		else if(range < 0){
			this.range = Math.abs(range % 1);
			this.facing = (this.facing + 1) % 2;
		}*/
		this.range = range;
	}
	public double getRangeError() {
		return rangeError;
	}
	public void setRangeError(double rangeError) {
		this.rangeError = rangeError;
	}
	
}
