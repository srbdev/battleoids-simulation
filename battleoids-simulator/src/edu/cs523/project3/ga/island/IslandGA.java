package edu.cs523.project3.ga.island;

import java.util.ArrayList;

import edu.cs523.project3.model.Ship;

public class IslandGA 
{
	private double crossoverRate; // from 0 to 1
	private double mutationRange;
	
	private ArrayList<Ship> ships;
	private int populationSize;
	
	
	public IslandGA(int populationSize)
	{
		ships = new ArrayList<Ship>();
		this.populationSize = populationSize;
		
		this.crossoverRate = 0.1;
		this.mutationRange = 0.1; // equivalent to about 6 degrees mutation
		
		init(); // initializes the GA
	}
	
	public IslandGA(int populationSize, double crossoverRate, double mutationRange)
	{
		ships = new ArrayList<Ship>();
		
		this.populationSize = populationSize;
		this.crossoverRate = crossoverRate;
		this.mutationRange = mutationRange;
		
		init(); // initializes the GA
	}
	
	
	public double getCrossoverRate()
	{
		return this.crossoverRate;
	}
	
	public double getMutationRange()
	{
		return this.mutationRange;
	}
	
	public Ship getShip(int index)
	{
		return ships.get(index);
	}
	
	public void evolve() 
	{
		ArrayList<Ship> newShips = new ArrayList<Ship>();
		
		for (Ship ship : ships)
		{
			int[] parents = new int[2];
			int[] candidates = new int[2];
			
			for (int i = 0; i < parents.length; i++)
			{
				for (int j = 0; j < candidates.length; j++)
					candidates[j] = (int)(Math.random() * populationSize);
					
				while (candidates[0] == candidates[1])
					candidates[1] = (int)(Math.random() * populationSize);
				
				if (ships.get(candidates[0]).getScore().energy > ships.get(candidates[1]).getScore().energy)
					parents[i] = candidates[0];
				else
					parents[i] = candidates[1];
			}
			
			//TODO crossover
			//TODO mutation
		}
		
		//TODO collect statistics...
		
		this.ships = newShips;
	}
	
	
	private void init() 
	{
		for (int i = 0; i < populationSize; i++)
			ships.add(IslandShipInitializer.getInitializedShip());
	}
}
