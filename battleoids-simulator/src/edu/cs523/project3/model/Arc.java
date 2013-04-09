package edu.cs523.project3.model;

public class Arc {
	private double width=0, facing=0, range=0;
	
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
		if(range >= 0 ) this.range = range % 1;
		else{
			this.range = Math.abs(range % 1);
			this.facing = (this.facing + 1) % 2;
		}
	}
	
}
