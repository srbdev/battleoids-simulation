package edu.cs523.project3.model;

import java.io.Serializable;

public class Score implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8778558076808991721L;
	public double energy=0.5, hits=0, misses=0;
	public int time=0, count=0;
	//public ArrayList<Integer> ship = new ArrayList<Integer>();
	public int[] ship;
	/**
	 * Constructor for deep copying score object.
	 * @param s The score to copy into this one.
	 */
	/*public Score(Score s){
		this.energy = s.energy;
		this.hits = s.hits;
		this.misses = s.misses;
		this.ship = s.ship;
	}*/
	
	public void setCount(int count){
		this.count = count;
		this.ship = new int[count];
	}
	
	public Score(double energy, double hits, double misses, int time, int[] ship, int count){
		this.energy = energy;
		this.hits = hits;
		this.misses = misses;
		this.time = time;
		this.ship = ship;
		this.count = count;
	}
	/**
	 * Create a new score with the given energy value
	 * @param energy
	 */
	public Score(double energy){
		this.energy = energy;
	}
	public Score(){}
}

