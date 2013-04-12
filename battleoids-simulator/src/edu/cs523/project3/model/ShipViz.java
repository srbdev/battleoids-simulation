package edu.cs523.project3.model;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;


 /**
 * @author Nathan Rackley
 * Class ShipViz
 * 	Visualization class for the ships.
 */
public class ShipViz extends Frame {

	  public ShipViz() {
	     //constructor
	     super("Ship Sensor Visualization");
	     this.add(new myCustomCanvas());
	     this.setSize(506,529);
	     this.show();
	     this.setResizable(false);
	     addWindowListener(new WindowEventHandler());
	  }


	  class WindowEventHandler extends WindowAdapter {
		  public void windowClosing(WindowEvent e) {
			  System.exit(0);
		  }
	  }

	  public static void main(String[] args) {
		  new ShipViz();
	  }
	}

	//Part 2; Java 2D specific-extend the drawing Component -Canvas-
	// and override it's paint method.

	class myCustomCanvas extends Canvas {
		/**
		 * Draws a pie shaped outline.
		 * @param g
		 * @param x
		 * @param y
		 * @param radius
		 * @param startAngle
		 * @param endAngle
		 */
		public void drawPie(Graphics2D g, int x, int y, int radius, double startAngle, double endAngle){
		     //System.out.println("H: " + arcLength);
		     int x1, x2, y1, y2, x3, y3;
		     x1 = x;
		     y1 = y;

		     int xa = (int) (Math.cos(startAngle*Math.PI)*radius);
		     int ya = (int) (Math.sin(startAngle*Math.PI)*radius);
		     int xb = (int) (Math.cos(endAngle*Math.PI)*radius);
		     int yb = (int) (Math.sin(endAngle*Math.PI)*radius);
		     	     
		     x2 = x + xa;
		     y2 = y - ya;
		     x3 = x + xb;
		     y3 = y - yb;
		     
		     //Calculate and draw Firing Arc
		     g.draw(new Line2D.Double(x1, y1, x2, y2));
		     g.draw(new Line2D.Double(x1, y1, x3, y3));
		     double xold, yold, xnew, ynew;
		     xold = x2;
		     yold = y2;
		  // for(double angle=startAngle; angle>=endAngle;angle=angle-(1.0/180)){
		   for(double angle=startAngle; angle>=endAngle;angle=angle-0.001){
			    	 //System.out.println(shipArcStart + " " + shipArcEnd + " " + angle);
		    	 xnew = x + (int) (Math.cos(angle*Math.PI)*radius);
			     ynew = y - (int) (Math.sin(angle*Math.PI)*radius);
			     g.draw(new Line2D.Double(xold, yold, xnew, ynew));
		    	 xold = xnew;
		    	 yold = ynew;
		     }
		     g.draw(new Line2D.Double(xold, yold, x3, y3));

		}
		/**
		 * Function drawShipBody - Draws the ship at the given co-ordinates with given facing.
		 * @param g
		 * @param x
		 * @param y
		 * @param size
		 * @param facing
		 * @param label
		 */
		public void drawShipBody(Graphics2D g, int x, int y, int size, double facing, String label){
			double sX1, sX2, sX3, sY1, sY2, sY3;
			sX1 = (Math.cos(facing*Math.PI)*size*1.5);
		    sY1 = (Math.sin(facing*Math.PI)*size*1.5);
		    sX2 = (Math.cos((facing+2.0/3.0)*Math.PI)*size);
		    sY2 = (Math.sin((facing+2.0/3.0)*Math.PI)*size);
		    sX3 = (Math.cos((facing-2.0/3.0)*Math.PI)*size);
		    sY3 = (Math.sin((facing-2.0/3.0)*Math.PI)*size);
		    int[] xs = new int[] {(int) (x + sX1), (int) (x + sX2), (int) (x + sX3)};
		    int[] ys = new int[] {(int) (y - sY1), (int) (y - sY2), (int) (y - sY3)};
		    g.fill(new Polygon(xs, ys, 3));
		    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		    g.setColor(Color.red);
		    Font font = new Font("Courier New", Font.BOLD, size);
		    g.setFont(font);
		    g.drawString(label, (int) (x-2), (int) (y+2));
		}
		/**
		 * Function drawShip
		 * Draw a ship on the given Graphics2D object.
		 * @param g
		 * @param s
		 */
		public void drawShip(Graphics2D g, Ship s, String label, int boardSize){

		     //Board Declaration
		     int size = boardSize/2;
		     int maxRange = size - 50;
		     int shipSize = boardSize/50;
		     
		     //Loop through sensors and draw the arcs.
		     //Draw the gun range Arc
		     g.setColor(Color.red);
		     int gRange = (int)(s.getGun().getRange()*maxRange);
		     double gStartArc = ((s.getFacing() + s.getGun().getFacing()) + (s.getGun().getWidth()/2));
		     double gEndArc = ((s.getFacing() + s.getGun().getFacing()) - (s.getGun().getWidth()/2));
		     drawPie(g, s.getLocation().x, s.getLocation().y, gRange, gStartArc, gEndArc);
		     
		     //Draw Sensor Arcs
		     for(int scount = 0; scount < s.getSensors().size(); scount++){
			     g.setColor(Color.blue);
			     Sensor cs = s.getSensors().get(scount);
			     if(cs.isActive()){
			    	 //Set sensor color here. Will take care of this later.
			    	 //TODO 
			
			    	 int test = s.getSensors().get(scount).getMode();
				     if((test & Action.MOVE)==Action.MOVE)  g.setColor(Color.blue);
				     if((test & Action.FIRE)==Action.FIRE)  g.setColor(Color.green);
				     if((test & Action.LEFT)==Action.LEFT)  g.setColor(Color.yellow);
				     if((test & Action.RIGHT)==Action.RIGHT)  g.setColor(Color.orange);
				    
			    	// g.setColor(Color.blue);
			    	 
			    	 //draw sensor arc
			    	 int arcRange = (int)(cs.getArc().getRange()*maxRange);
				     double startArc = ((s.getFacing() + cs.getArc().getFacing()) + (cs.getArc().getWidth()/2));
				     double endArc = ((s.getFacing() + cs.getArc().getFacing()) - (cs.getArc().getWidth()/2));
				     drawPie(g, s.getLocation().x, s.getLocation().y, arcRange, startArc, endArc);
			     }
		     }
		     //Render the Ship
		     g.setColor(Color.white); //setting context
		     drawShipBody(g, s.getLocation().x, s.getLocation().y, shipSize, s.getFacing(), label);
		}
		
	   public void paint(Graphics g) {
	     
		 //Prepare Draw Window  
		 Graphics2D g2d = (Graphics2D) g;
		 RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		 g2d.setRenderingHints(rh);
		 
	     g2d.setColor(Color.black);
	     g2d.fillRect(0, 0, 500, 500);
	     

	     //Set the stroke width.
	     //g2d.setStroke(new BasicStroke(1));
	      
	     
	     //Define Ship
	     Ship s = new Ship();								//new ship
	     s.setFacing(0.5);									//Set ship Facing.
	     Point p = new Point(250, 250); 	//location is middle of current window.
	     s.setLocation(p);									//set location of ship
	     
	     Arc marc = new Arc(0, .25, 1.0);					//sensor arc defined
	     Sensor sens = new Sensor(2, marc, true);			//sensor defined
	     s.getSensors().add(sens);							//add sensor to ship
	     
	     Arc marc2 = new Arc(.5, 1.0, .75);					//sensor arc defined
	     Sensor sens2 = new Sensor(4, marc2, true);			//sensor defined
	     s.getSensors().add(sens2);							//add sensor to ship
	     
	     Arc marc3 = new Arc(1.5, 1.0, .75);					//sensor arc defined
	     Sensor sens3 = new Sensor(8, marc3, true);			//sensor defined
	     s.getSensors().add(sens3);							//add sensor to ship
	     
	     Arc marc4 = new Arc(0, .5, .875);					//sensor arc defined
	     Sensor sens4 = new Sensor(1, marc4, true);			//sensor defined
	     s.getSensors().add(sens4);							//add sensor to ship
	     
	     Ship s2 = new Ship(s, 100, 100, 0);
	     Ship s2b = new Ship(s, 600, 600, 0);
	     Ship s2c = new Ship(s, 100, 600, 0);
	     Ship s2d = new Ship(s, 600, 100, 0);
	     
	     //Draw Ship
	     drawShip(g2d, s, "1", 500);     
	     drawShip(g2d, s2, "2", 500);
	     drawShip(g2d, s2b, "2", 500);
	     drawShip(g2d, s2c, "2", 500);
	     drawShip(g2d, s2d, "2", 500);
	     
	     
	     
	   }
	}  