package edu.cs523.project3.ga.fleet;

import java.util.ArrayList;
import java.util.Random;

import edu.cs523.project3.model.Battlefield;
import edu.cs523.project3.model.Score;
import edu.cs523.project3.model.Ship;

public class FleetRunner {

	
	private static int generations = 100;
	
	
	public static void main(String[] args) {
		
		/*** initialization of ships ***/
		ArrayList<Ship> ships = new ArrayList <Ship>();
		
		
		
		/* ship type 1
		 * default action move
		 */
		Ship aggressive1 = ShipType.newInstance(15, 1);
		ships.add(aggressive1);
		
		
		/* ship type 2
		 * default action turn left
		 */
		Ship aggressive2 = ShipType.newInstance(50, 8);
		ships.add(aggressive2);
		
		/* ship type 3
		 * no default action
		 */
		Ship passive = ShipType.newInstance(28673, 0);
		ships.add(passive);
		
		/* ship type 4
		 * avoidance
		 * no default move
		 */
		Ship avoidance = ShipType.newInstance(36800, 1);
		ships.add(avoidance);
		

		
		/* random ship
		 * random default
		 */
		Random r = new Random();
		Ship random = ShipType.newInstance(r.nextInt(65536), r.nextInt(16));
		ships.add(random);
		
		
		/**
		 * not sure what number of runs, size, and position refer to...
		 */
		Battlefield bf;
		ArrayList<ArrayList<Score>> matrix;
		
		/* evolve ship */
		for(int i = 0; i < generations; i++){
			
			bf = new Battlefield(ships, 1, 20, 1);
			matrix = bf.run();
			
			/***** todo: generate statistics *******/
			/** should be able to do this with bf **/
			ships = EvolveShip.evolve(ships, matrix);
		}

	}

}
