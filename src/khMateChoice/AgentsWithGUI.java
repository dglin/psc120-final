package khMateChoice; 

import sim.engine.*;
import sim.display.*;
import sim.portrayal.grid.*;
import java.awt.*;
import javax.swing.*;


public class AgentsWithGUI extends GUIState {
	public Display2D display;
	public JFrame displayFrame;
	SparseGridPortrayal2D agentsPortrayal = 
                             new SparseGridPortrayal2D();

	public static void main(String[] args) {
		//Create the environment in the main method and pass it the Experimenter class for creation:
		Environment e = new Environment(System.currentTimeMillis(), Experimenter.class);
		//Create the AgentsWithGUI state and pass it the environment
		AgentsWithGUI ex = new AgentsWithGUI(e);
		e.setGui(ex); //pass the AgentsWithGUI to the Environment
		Console c = new Console(ex);
		c.setVisible(true);
		System.out.println("Start Simulation");
	}

	public AgentsWithGUI() {
		super(new Environment(System.currentTimeMillis()));
	}
	
	
	public AgentsWithGUI (SimState state){ //Constructor method for handling Environments
		super(state);
		Environment e = (Environment)state;
		((Environment)state).agentsPortrayal = agentsPortrayal;
	}
	
	public void quit() {
		super.quit(); 

		if (displayFrame!=null) displayFrame.dispose();
		displayFrame = null;
		display = null;
	}

	public void start() {
		super.start();
		setupPortrayals();
	}

	public void load(SimState state) {
		super.load(state);
		setupPortrayals();
	}

	public void setupPortrayals() {
        Environment se = (Environment)state;
		agentsPortrayal.setField(se.space);
        /*OvalPortrayal2D o = new OvalPortrayal2D(Color.red);
		agentsPortrayal.setPortrayalForAll(o);*/
		display.reset();
		display.repaint();
	}

	public void init(Controller c){
		super.init(c);
		display = new Display2D(400,400,this);
		displayFrame = display.createFrame();
		c.registerFrame(displayFrame);
		displayFrame.setVisible(true);
		display.setBackdrop(Color.WHITE);
		display.attach(agentsPortrayal,"Agents");
	}

	public Object getSimulationInspectedObject() {
		return state;
	}
}
