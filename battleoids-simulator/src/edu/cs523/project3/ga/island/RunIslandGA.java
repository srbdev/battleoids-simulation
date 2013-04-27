package edu.cs523.project3.ga.island;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import edu.cs523.project3.model.Battlefield;
import edu.cs523.project3.model.Ship;

public class RunIslandGA {

	private final static int N_ISLANDS = 4;
	private final static int N_LOOPS = 10;
	private final static int N_GENERATIONS = 10;
	private final static int N_RUNS = 5;
	
	private final static int POPULATION_SIZE = 25;
	private final static int BATTLEFIELD_SIZE = 500;
	
	private IslandGA[] islands;
	
	private static BufferedWriter buffer;
	
	public RunIslandGA() 
	{
		this.islands = new IslandGA[4];
		
		for (int i = 0; i < N_ISLANDS; i++)
		{
			System.out.print("[INFO] Initialize GA " + i + "... ");
			this.islands[i] = new IslandGA(POPULATION_SIZE);
			System.out.println("Done.");
		}
		
		try 
		{
			File file = new File("islandGAdata.csv");
			
			if (file.exists())
				file.delete();
			
			file.createNewFile();
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			buffer = new BufferedWriter(fw);
			
			buffer.write("loop,ga,generation,average score,best score\n");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		
	}
	
	public IslandGA[] getIslandGAs() 
	{
		return this.islands;
	}
	
	public void setIslandGAs(IslandGA[] newIslands)
	{
		this.islands = newIslands;
	}
	
	
	public static void main(String[] args) 
	{
		System.out.println("[INFO] Starting simulation.");
		RunIslandGA run = new RunIslandGA();
		
		for (int i = 0; i < N_LOOPS; i++)
		{
			IslandGA[] currentGAs = run.getIslandGAs();
			Ship[] bestShips = new Ship[N_ISLANDS];
			double overallBestScore = 0.0;
			int bestIndex = 0;
			
			for (int j = 0; j < N_ISLANDS; j++)
			{
				IslandGA ga = currentGAs[j];
				double bestScore = 0.0;
				
				for (int k = 0; k < N_GENERATIONS; k++)
				{
					System.out.print("Running loop " + i + " GA No. " + j + " for generation " + k + "... ");
					
					Battlefield field = new Battlefield(ga.getShips(), N_RUNS, BATTLEFIELD_SIZE, 0);
					field.run();
					
					ga.evolve();
					
					try 
					{
						buffer.write(i + "," + j + "," + k + "," + ga.getAverageScore() + "," + ga.getBestScore() + "\n");
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
					
					if (ga.getBestScore() > bestScore)
					{
						bestShips[j] = ga.getFittestShip();
						bestScore = ga.getBestScore();
					}
					
					if (bestScore > overallBestScore)
					{
						overallBestScore = bestScore;
						bestIndex = j;
					}						
					
					System.out.println("Done.");
				}
			}
			
			for (int j = 0; j < N_ISLANDS; j++)
				currentGAs[j] = new IslandGA(POPULATION_SIZE, bestShips[bestIndex]);
		}
		
		try 
		{
			buffer.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

}
