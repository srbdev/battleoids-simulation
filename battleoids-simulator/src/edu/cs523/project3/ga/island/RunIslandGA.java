package edu.cs523.project3.ga.island;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import edu.cs523.project3.model.Battlefield;
import edu.cs523.project3.model.Score;
import edu.cs523.project3.model.Ship;

public class RunIslandGA {

	private final static int N_ISLANDS = 4;
	private final static int N_LOOPS = 10;
	private final static int N_GENERATIONS = 10;
	private final static int N_RUNS = 1;
	
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
		
		Ship simulationBestShip = null;
		double simulationBestScore = 0.0;
		
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
					
					double gaTotalScore = 0.0;
					double gaBestScore = 0.0;
					double gaAverageScore = 0.0;
					Ship gaFittestShip = null;
					
					if (ga.getShips().size() == 0) System.err.println("[ERROR] NO SHIPS IN GA!");
					for (Ship s : ga.getShips())
					{
						if (s == null)
							System.err.println("[ERROR] NULL SHIPS IN GA " + j);
					}
					
					Battlefield field = new Battlefield(ga.getShips(), N_RUNS, BATTLEFIELD_SIZE, 0);
					ArrayList<ArrayList<Score>> results = field.run();
					
					for (ArrayList<Score> r : results) // for each runs...
					{
						int shipIndex = 0;
						
						for (Score s : r) // for each score/ship in current run...
						{
							gaTotalScore += s.energy;
							
							if (s.energy > gaBestScore) 
							{
								gaBestScore = s.energy;
								gaFittestShip = ga.getShip(shipIndex);
							}
							
							ga.getShip(shipIndex++).setScore(s);
						}
					}
					
					gaAverageScore = gaTotalScore / (POPULATION_SIZE * results.size());
					
					ga.evolve();
					
					try 
					{
						buffer.write(i + "," + j + "," + k + "," + String.format("%.2f", gaAverageScore) + "," + String.format("%.2f", gaBestScore) + "\n");
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
					
					if (gaBestScore > bestScore)
					{
						bestShips[j] = gaFittestShip;
						bestScore = gaBestScore;
					}
					
					if (gaBestScore > overallBestScore)
					{
						overallBestScore = gaBestScore;
						bestIndex = j;
					}	
					
					if (gaBestScore < simulationBestScore)
					{
						simulationBestScore = gaBestScore;
						simulationBestShip = gaFittestShip;
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
