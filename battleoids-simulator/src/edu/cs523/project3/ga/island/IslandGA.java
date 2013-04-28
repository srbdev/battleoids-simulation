package edu.cs523.project3.ga.island;

import java.util.ArrayList;

import edu.cs523.project3.model.Ship;

public class IslandGA 
{
	private double crossoverRate; // from 0 to 1
	private double mutationRate;  // from 0 to 1
	
	private ArrayList<Ship> ships;
	private int populationSize;
	
	public IslandGA(int populationSize)
	{
		ships = new ArrayList<Ship>();
		this.populationSize = populationSize;
		
		this.crossoverRate = 0.1;
		this.mutationRate = 0.05;
		
		init(); // initializes the GA
	}
	
	public IslandGA(int populationSize, Ship seed)
	{
		ships = new ArrayList<Ship>();
		this.populationSize = populationSize;
		
		this.crossoverRate = 0.1;
		this.mutationRate = 0.1;
		
		initWithSeed(seed); // initializes the GA
	}
	
	public IslandGA(int populationSize, double crossoverRate, double mutationRate)
	{
		ships = new ArrayList<Ship>();
		
		this.populationSize = populationSize;
		this.crossoverRate = crossoverRate;
		this.mutationRate = mutationRate;
		
		init(); // initializes the GA
	}
	
	
	public double getCrossoverRate()
	{
		return this.crossoverRate;
	}
	
	public Ship getShip(int index)
	{
		return ships.get(index);
	}
	
	public void evolve() 
	{
		ArrayList<Ship> newShips = new ArrayList<Ship>();
		
		// evolution step
		for (int i = 0; i < ships.size(); i++)
		{
			int[] parents = new int[2];
			int[] candidates = new int[2];
			
			for (int j = 0; j < parents.length; j++)
			{
				for (int k = 0; k < candidates.length; k++)
					candidates[k] = (int)(Math.random() * populationSize);
					
				while (candidates[0] == candidates[1])
					candidates[1] = (int)(Math.random() * populationSize);
				
				if (ships.get(candidates[0]).getScore().energy > ships.get(candidates[1]).getScore().energy)
					parents[j] = candidates[0];
				else
					parents[j] = candidates[1];
			}
			
			Ship child = ships.get(parents[1]);
			for (int j = 0; j < IslandShipInitializer.getNumberOfSensors(); j++)
			{
				if (Math.random() < this.crossoverRate)
					child.getSensors().set(j, ships.get(parents[0]).getSensors().get(j));
			}
			
			mutate(child);
			
			newShips.add(child);
		}
		
		this.ships = newShips;
	}
	
	
	public ArrayList<Ship> getShips()
	{
		return this.ships;
	}
	
	
	private void init() 
	{
		for (int i = 0; i < populationSize; i++)
			ships.add(IslandShipInitializer.getInitializedShip());
	}
	
	private void initWithSeed(Ship seed)
	{
		for (int i = 0; i < populationSize; i++)
			ships.add(IslandShipInitializer.getInitializedShipFromSeed(seed));
	}
	
	private void mutate(Ship ship)
	{
		for (int i = 0; i < IslandShipInitializer.getNumberOfSensors(); i++)
		{
			if (Math.random() < this.mutationRate)
			{
				double die = Math.random();
				
				if (die <= 1/3)
					ship.getSensors().get(i).setMode(IslandShipInitializer.getRandomMode());
				else if (die > 1/3 && die <= 2/3)
					ship.getSensors().get(i).setArc(IslandShipInitializer.getRandomArc());
				else
					ship.getSensors().get(i).setActive(ship.getSensors().get(i).isActive() ? false : true);
			}
		}
	}
}
