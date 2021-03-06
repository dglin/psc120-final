package khMateChoice;

import java.awt.Color;

import basicMovement.BasicAgent;
import basicMovement.BasicEnvironment;
import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.util.Bag;

// Each Agent has age and environment has a set age range. Done
// Personality, shy and outgoing, 
// Gay agent*

public class Agent extends BasicAgent {
	boolean female;
	double attractivenessA;
	double scaleK;
	double exponentN;
	double maxD;
	double d=0;
	int age;
	boolean similar;
	Stoppable stop;
	boolean findLocalDate;
	int dateSearchRadius;
	boolean replacement;
	public boolean oneDate;
	boolean dated = false;
	boolean personality; // If true then shy otherwise bold.
	boolean sexuality; // defined by the double value of ratio of agents passed from environment.

	
	public Agent(BasicEnvironment state, int x, int y, int xdir,int ydir, 
			boolean female, double attractivenessA, double sexuality) {
		super(state,x,y);
		this.x = x;
		this.y = y;
		this.female = female;
		this.attractivenessA = attractivenessA;
		Environment e = (Environment)state;
		this.scaleK = e.scaleK;
		float value =(float)(this.attractivenessA/this.scaleK);
		if(female)
			setColor(e, (float)1, (float)0, (float)0, value);
		else
			setColor(e, (float)0, (float)0, (float)1, value);
		this.similar = e.similar;
		this.exponentN = e.exponentN;
		this.maxD = e.maxD;
		this.findLocalDate = e.findLocalDate;
		this.dateSearchRadius = e.dateSearchRadius;
		this.replacement = e.replacement;
		this.age = 0;
		this.personality = Math.random() < 0.5;
		this.personality = Math.random() < sexuality;
	}
	
	public Agent setColor(Environment state, float red, float green, float blue, float opacity){
		Color c = new Color(red,green,blue,opacity);
		OvalPortrayal2D o = new OvalPortrayal2D(c);
		AgentsWithGUI guiState = (AgentsWithGUI)state.gui;
		guiState.agentsPortrayal.setPortrayalForObject(this, o);
		return this;
	}
	
	public double chooseTheBest(Agent other){
		return  Math.pow(other.attractivenessA, exponentN)/ Math.pow(scaleK, exponentN);
	}
	
	public double chooseSimilar(Agent other){
		return Math.pow(scaleK - Math.abs(other.attractivenessA - this.attractivenessA),exponentN)/Math.pow(scaleK, exponentN);
	}

	public double closingTimeRule(double p){
		if(maxD <= d){
			return 1; //no more choosiness
		}
		else{
			return Math.pow(p, (maxD-d)/(maxD));
		}
	}
	
	public void removeSelf(Environment state){
		stop.stop(); //remove the agent from the schedule
		state.space.remove(this); //take it out of space, it ends up in the garbage
	}
	
	public void findDate(Environment state){
		Bag dates;
		if(findLocalDate){
			dates = state.space.getMooreNeighbors(x, y, dateSearchRadius, mode, false);
		}
		else{
			dates = state.space.getAllObjects();
		}
		
		if(dates.numObjs == 0){
			return; //stop method since there are no possible agents to date.
		}
		
		int r = state.random.nextInt(dates.numObjs);//start search
		Agent other = null;
		/**
		 * Depending on whether the agent is "shy" or bold" search through half the neighbors
		 * or all of them.
		 */
		for(int i = r;i < dates.numObjs / ((this.personality) ? 2 : 1);i++){
			other = (Agent)dates.objs[i];
			/** Condition for when found dates are of inappropriate age. **/
			if((oneDate && !dated && !other.dated) || other.female != this.female)
                break;
			else
				other = null;
		}
		if(other == null){
			for(int i = 0;i< r;i++){
				other = (Agent)dates.objs[i];
				if((oneDate && !dated && !other.dated) || other.female != this.female)
					break;
				else
					other = null;
			}
		}
		
		double p1 = 0; //choosing agent
		double p2 = 0; //selected agent
		this.dated = true; //dated this time step
		
		if(other != null){
			other.dated = true;
			/** If the agent is deemed homosexual, then they are not considered successful dates.
			 * 	Serves as a false agent, one which never successfully dates.
			 */
			if (other.sexuality)
				return;
			
			if(similar){
				p1 = chooseSimilar(other);
				p2 = other.chooseSimilar(this);
			}
			else{
				p1 = chooseTheBest(other);
				p2 = other.chooseTheBest(this);
			}
			p1 = closingTimeRule(p1); //correct for closing time rule
			p2 = other.closingTimeRule(p2);
			d++;// increment d
			other.d++;// increment d
			
			//make joint decision
			double p = p1 * p2; //joint probability
			if(state.random.nextBoolean(p)){
				if(this.female){ //(female, male)
					state.correlation.getData(this.attractivenessA, other.attractivenessA);
				}
				else{
					state.correlation.getData(other.attractivenessA, this.attractivenessA);
				}
				this.removeSelf(state);
				other.removeSelf(state);
				if(replacement){
					replicate(state,this.female);
					replicate(state,other.female);
				}
				//state.correlation.printData();
			}
		}
		this.age++;
	}
	
	
	public void replicate (Environment state, boolean gender){
		state.placeAgent(gender); 
	}

	public void step(SimState state){
		super.step(state);
		findDate((Environment) state);
	}

}
