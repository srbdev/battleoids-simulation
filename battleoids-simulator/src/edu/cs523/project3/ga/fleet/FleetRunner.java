package edu.cs523.project3.ga.fleet;

import java.util.ArrayList;
import java.util.Random;

import edu.cs523.project3.model.Battlefield;
import edu.cs523.project3.model.Score;
import edu.cs523.project3.model.Ship;

public class FleetRunner {

	
	private static int generations = 100;
	private static final int BATTLEFIELD_SIZE = 2000;
	
	public static void main(String[] args) {
		
		/*** initialization of ships ***/
		ArrayList<Ship> ships = new ArrayList <Ship>();
		Random r = new Random();
		
		
		/* ship type 1
		 * default action move
		 */
		Ship aggressive1 = ShipType.newInstance(15, 1);
		aggressive1.x = r.nextDouble() * BATTLEFIELD_SIZE * 100;
		aggressive1.y =  r.nextDouble() * BATTLEFIELD_SIZE * 100;
		ships.add(aggressive1);
		
		
		/* ship type 2
		 * default action turn left
		 */
		Ship aggressive2 = ShipType.newInstance(50, 8);
		aggressive2.x = r.nextDouble() * BATTLEFIELD_SIZE * 100;
		aggressive2.y =  r.nextDouble() * BATTLEFIELD_SIZE * 100;
		ships.add(aggressive2);
		
		/* ship type 3
		 * no default action
		 */
		Ship passive = ShipType.newInstance(28673, 0);
		passive.x = r.nextDouble() * BATTLEFIELD_SIZE * 100;
		passive.y =  r.nextDouble() * BATTLEFIELD_SIZE * 100;
		ships.add(passive);
		
		/* ship type 4
		 * avoidance
		 * no default move
		 */
		Ship avoidance = ShipType.newInstance(36800, 1);
		avoidance.x = r.nextDouble() * BATTLEFIELD_SIZE * 100;
		avoidance.y =  r.nextDouble() * BATTLEFIELD_SIZE * 100;
		ships.add(avoidance);
		
		/* ship - previous winner
		 * Ship type 51
		 */
		Ship prev1 = ShipType.newInstance(51, 1);
		prev1.x = r.nextDouble() * BATTLEFIELD_SIZE * 100;
		prev1.y =  r.nextDouble() * BATTLEFIELD_SIZE * 100;
		ships.add(prev1);
		
		/* ship - previous winner
		 * Ship type 59
		 */
		Ship prev2 = ShipType.newInstance(59, 13);
		prev2.x = r.nextDouble() * BATTLEFIELD_SIZE * 100;
		prev2.y =  r.nextDouble() * BATTLEFIELD_SIZE * 100;
		ships.add(prev2);
		
		
		/* ship - previous winner
		 * Ship type 575
		 */
		Ship prev5 = ShipType.newInstance(2063, 7);
		prev5.x = r.nextDouble() * BATTLEFIELD_SIZE * 100;
		prev5.y =  r.nextDouble() * BATTLEFIELD_SIZE * 100;
		ships.add(prev5);
		
		
		/* random ship
		 * random default
		 */
		Ship random = ShipType.newInstance(r.nextInt(65535), r.nextInt(16));
		random.x = r.nextDouble() * BATTLEFIELD_SIZE * 100;
		random.y =  r.nextDouble() * BATTLEFIELD_SIZE * 100;
		ships.add(random);
		
		
		/**
		 * ships, runs, size, and position
		 */
		Battlefield bf;
		ArrayList<ArrayList<Score>> matrix;
		
		EvolveShip.createFileHeaders(args[0]);
		
		
		/* evolve ship */
		for(int i = 0; i < generations; i++){
			
			bf = new Battlefield(ships, 1, 1000, Battlefield.SEEDED);
			
			matrix = bf.run();
			
			/***** todo: generate statistics *******/
			/** should be able to do this with bf **/
			ships = EvolveShip.evolve(ships, matrix);
		}
		
		EvolveShip.printBest(ships.get(0));
		EvolveShip.closeWriters();

	}

}
