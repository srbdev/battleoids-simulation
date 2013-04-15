package edu.cs523.project3.utils;

import edu.cs523.project3.model.Sensor;
import edu.cs523.project3.model.Ship;

public class ShipInitializer 
{
	private final double SENSOR_ON_PROB = 0.35; // Probability that a sensor is initialize to ON
	
	public ShipInitializer() {}
	
	public void initialize(Ship ship)
	{
		for (Sensor sensor : ship.getSensors())
		{
			boolean set = Math.random() > SENSOR_ON_PROB ? false : true;
			
			sensor.setActive(set);
			
			mutate(sensor);
		}
	}

	private void mutate(Sensor sensor) 
	{
		
	}
}
