package edu.cs523.project3.ga.island;

import java.util.ArrayList;

import edu.cs523.project3.model.Arc;
import edu.cs523.project3.model.Sensor;
import edu.cs523.project3.model.Ship;


public class IslandShipInitializer 
{
	private final double ON_SENSOR_PROB = 0.35; // Probability that a sensor is initialize to ON
	private final int N_SENSORS = 10;			// Number of sensors for the ship
	private final int DEFAULT_ACTION = 1;		// Default action for the ships is to move.
	
	public IslandShipInitializer() {}
	
	public Ship getInitializedShip()
	{
		Ship ship = new Ship();
		ArrayList<Sensor> sensors = new ArrayList<Sensor>();
		
		for (int i = 0; i < N_SENSORS; i++)
		{
			boolean turnOn = Math.random() > ON_SENSOR_PROB ? false : true;
			int mode = getRandomMode();
			Arc arc = getRandomArc();
			
			sensors.add(new Sensor(mode, arc, turnOn));
		}
		
		ship.setSensors(sensors);
		ship.setDefaultAction(DEFAULT_ACTION);
		
		return ship;
	}
	
	private Arc getRandomArc()
	{
		double facing = Math.random() * (2 * Math.PI);
		double width = Math.random();
		double range = Math.random();
		
		return new Arc(facing, width, range);
	}
	
	private int getRandomMode()
	{
		double die = Math.random();
		
		if (die <= 0.25)
			return 1;
		else if (die > 0.25 && die <= 0.5) 
			return 2;
		else if (die > 0.5 && die <= 0.75) 
			return 4;
		else 
			return 8;
	}
}
