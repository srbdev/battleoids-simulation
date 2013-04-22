package edu.cs523.project3.ga.fleet;

import edu.cs523.project3.model.Ship;

public class FleetRunner {

	
	private static int generations = 100;
	
	
	public static void main(String[] args) {
		
		/*** initialization of ships ***/
		
		/* ship type 1
		 * default action move
		 */
		Ship aggressive1 = ShipType.newInstance(15, 1);
		
		
		
		/* ship type 2
		 * default action turn left
		 */
		Ship aggressive2 = ShipType.newInstance(50, 8);
		
		/* ship type 3
		 * no default action
		 */
		Ship passive = ShipType.newInstance(28673, 0);
		
		
		/* avoidance
		 * no default move
		 */
		Ship avoidance = ShipType.newInstance(36800, 1);
		
		/* evolve ship */
		for(int i = 0; i < generations; i++){
			
		}

	}

}
