package edu.cs523.project3.model;

import java.util.ArrayList;

public class Score {
	public double energy=0.5, hits=0, misses=0;
	public int time=0;
	public ArrayList<Integer> ship = new ArrayList<Integer>();
	
	public Score(double energy){
		this.energy = energy;
	}
	public Score(){}
}
