package khMateChoice;


import basicMovement.BasicEnvironment;
import sim.field.grid.SparseGrid2D;
import sim.portrayal.grid.SparseGridPortrayal2D;

public class Environment extends BasicEnvironment {
	public SparseGridPortrayal2D agentsPortrayal;
//	public AgentsWithGUI gui;
	Correlation correlation;
	public int females = 1000;
	public int males = 1000;
	public boolean similar = true;
	public double scaleK = 10;
	public double exponentN = 3;
	public double maxD = 50;
	public boolean findLocalDate = false;
	public int dateSearchRadius = 1;
	public boolean replacement = false;
	public boolean oneDate = false;
	
	public boolean isOneDate() {
		return oneDate;
	}

	public void setOneDate(boolean oneDate) {
		this.oneDate = oneDate;
	}
	
	public Environment(long seed) {
		super(seed);
	}
	
	public Environment(long seed,  Class c) {
		super(seed, c);
	}

//	public void setGui(AgentsWithGUI gui) {
//		this.gui = gui;
//	}
//	
	public boolean isFindLocalDate() {
		return findLocalDate;
	}

	public void setFindLocalDate(boolean findLocalDate) {
		this.findLocalDate = findLocalDate;
	}

	public int getDateSearchRadius() {
		return dateSearchRadius;
	}

	public void setDateSearchRadius(int dateSearchRadius) {
		this.dateSearchRadius = dateSearchRadius;
	}

	public int getFemales() {
		return females;
	}

	public void setFemales(int females) {
		this.females = females;
	}

	public int getMales() {
		return males;
	}

	public void setMales(int males) {
		this.males = males;
	}

	public boolean isSimilar() {
		return similar;
	}

	public void setSimilar(boolean similar) {
		this.similar = similar;
	}

	public double getScaleK() {
		return scaleK;
	}

	public void setScaleK(double scaleK) {
		this.scaleK = scaleK;
	}

	public double getExponentN() {
		return exponentN;
	}

	public void setExponentN(double exponentN) {
		this.exponentN = exponentN;
	}

	public double getMaxD() {
		return maxD;
	}

	public void setMaxD(double maxD) {
		this.maxD = maxD;
	}
	
	public boolean isReplacement() {
		return replacement;
	}

	public void setReplacement(boolean replacement) {
		this.replacement = replacement;
	}


	public void placeAgent(boolean female){
		int x = random.nextInt(gridWidth);
		int y = random.nextInt(gridHeight);
		int xdir =  random.nextInt(3)-1;
		int ydir =  random.nextInt(3)-1;
		double attractiveness = (double)random.nextInt((int)scaleK) + 1;
		Agent a = new Agent(this, x, y,xdir,ydir, female, attractiveness);
		place(a);
		a.stop = schedule.scheduleRepeating(a);
	}

	public void placeAgents(){
		for(int i = 0; i<females;i++){
			placeAgent(true);
		}
		for(int i = 0; i<males;i++){
			placeAgent(false);
		}
	}

	public void start(){
		super.start();
		space = new SparseGrid2D(gridWidth,gridHeight);
		correlation = new Correlation();
		placeAgents();
		if(observer != null){
			observer.init(space);
			((Experimenter)observer).setCorrelation(correlation);
		}
		else{
			System.out.println("Observer is null");
		}
		
	}

}
