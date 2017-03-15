package khMateChoice;

import sim.engine.SimState;
import sweep.Observer;
import sweep.ParameterSweeper;
import sweep.SimStateSweep;

public class Experimenter extends Observer {
	Correlation correlation = null; //variable for correlation probe

	public Correlation getCorrelation() {
		return correlation;
	}

	public void setCorrelation(Correlation correlation) {
		this.correlation = correlation;
	}
        
        /**
         We add data in the order we wish it to appear our saved data file.
        **/
	public boolean nextInterval(){
		if(correlation.n > 1){
			data.add(correlation.n);
			data.add(correlation.sX/correlation.n);
			data.add(correlation.sY/correlation.n);
			data.add(correlation.correlation());
		}
		else{
			data.add(0);
			data.add(0);
			data.add(0);
			data.add(0);
		}
		
		return true;
	}
	public Experimenter(String fileName, String folderName, SimStateSweep state, ParameterSweeper sweeper,
			String precision, String[] headers) {
		super(fileName, folderName, state, sweeper, precision, headers);
		// TODO Auto-generated constructor stub
	}
	
	public void step(SimState state){
		super.step(state);
		if(getdata){ //Get data is true at each collection interval
			nextInterval();
		}
		for(int i=0;i<agents.numObjs;i++){
			Agent a = (Agent)agents.objs[i];
			a.dated = false;//set them to false
		}
	}

}