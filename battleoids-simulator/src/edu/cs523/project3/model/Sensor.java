package edu.cs523.project3.model;

public class Sensor {
	private int mode;
	private Arc arc = new Arc();
	private boolean active;
	
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public Arc getArc() {
		return arc;
	}
	public void setArc(Arc arc) {
		this.arc = arc;
	}	
}
