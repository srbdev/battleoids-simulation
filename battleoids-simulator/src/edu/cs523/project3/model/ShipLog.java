package edu.cs523.project3.model;

import java.io.Serializable;

/**
 * ShipLog Class
 * @author Droakir
 * This class is designed for the storing of ship location data for later rendering and analysis.
 */
public class ShipLog implements Serializable{
	private static final long serialVersionUID = 2970105520627637406L;
	public int x=0;
	public int y=0;
	public double facing=0;
	public boolean firing=false;
	public boolean hit=false;
	public int sensors=0;
	public Score score=new Score();
	public boolean set=false;
	
	/**
	 * Function ShipLog
	 * Extracts the location state values for a ship in it's current state.
	 * @param s The ship to extract from.
	 */
	public ShipLog(Ship s){
		this.x = s.getLocation().x;
		this.y = s.getLocation().y;
		this.facing = s.getFacing();
		this.firing = s.isFiring();
		this.hit = s.isHit();
		Score sc = s.getScore();
		this.set = true;
		this.score = new Score(sc.energy, sc.hits, sc.misses, sc.time, sc.ship, sc.count);
		for(int i=0;i<s.getSensors().size();i++){
			if(s.getSensors().get(i).isTriggered()){
				this.sensors = this.sensors | (1<<i);
			}
		}		
	}
	/**
	 * Empty constructor.
	 */
	public ShipLog(){
		
	}
}
