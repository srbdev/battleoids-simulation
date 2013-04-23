package edu.cs523.project3.model;

public class Sensor {
	private int mode;
	private Arc arc = new Arc();
	private boolean active;
	private boolean triggered = false;
	
	public Sensor(){
		this.mode = 0;
		this.arc = new Arc();
		this.active = false;
	}
	
	public Sensor(int mode, Arc arc, boolean active){
		this.mode = mode;
		this.arc = arc;
		this.active = active;
	}
	
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

	public boolean isTriggered() {
		return triggered;
	}

	public void setTriggered(boolean triggered) {
		this.triggered = triggered;
	}	
}
