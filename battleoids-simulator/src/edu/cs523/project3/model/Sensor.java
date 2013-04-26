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
	
	/**
	 * Detects whether one of the given coordinates triggers the sensor. Only returns true on active sensors.
	 * @param distances
	 * @param angles
	 * @param count
	 * @return True if triggered, False if not.
	 */
	public boolean detect(double[] distances, double[] angles, boolean[] active, int count, double offset, int ignore, int maxRange){
		//System.out.print("Sensor ");
		this.triggered = false;
		if(this.active = true){
			//System.out.print("Active ");
			for(int i=0;i<count;i++){//Iterate through all coordinates
				if(i != ignore){ //Ignore current ship coordinate distance measure
					//System.out.print("Ship "+i+" ");
					if(active[i]==true){
						if(this.arc.inRange(distances[i], (angles[i]-offset), maxRange)==true) this.triggered = true;
					}
								
				}
			}
		}
		//System.out.println(this.triggered);
		return this.triggered;
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