package code;

public class Node implements Comparable<Node> {

	public int Agent_Capacity;
	public int Agent_X;
	public int Agent_Y;

	public Object[][] Grid;
	public int Passengers_On_CoastGuard;
	public int Stations_Dropped_Passengers;
	public int BlackBoxes_taken;
	public int peoplealive;
	public String state;
	public int numberofships;
	public int deathcount;
	public int retrived;
	public String path = "";
	public int time;
	public String shipsloc_summed ="";
	public String shipsloc = "";
	public String stationloc = "";

	public Node() {
		super();
	}

	public Node(Node other) {
		this.Agent_Capacity = other.Agent_Capacity;
		this.Agent_X = other.Agent_X;
		this.Agent_Y = other.Agent_Y;
		this.Grid = other.Grid;
		this.Passengers_On_CoastGuard = other.Passengers_On_CoastGuard;
		this.Stations_Dropped_Passengers = other.Stations_Dropped_Passengers;
		this.BlackBoxes_taken = other.BlackBoxes_taken;
		this.deathcount = other.deathcount;
		this.retrived = other.retrived;
		this.path = other.path;
		this.time = other.time;
		this.BlackBoxes_taken = other.BlackBoxes_taken;
	}

	public Node(int agent_Capacity, int agent_X, int agent_Y, Object[][] grid, int passengers_On_CoastGuard,
			int stations_Dropped_Passengers, int blackBoxes_taken) {
		super();
		Agent_Capacity = agent_Capacity;
		Agent_X = agent_X;
		Agent_Y = agent_Y;
		Grid = grid;
		Passengers_On_CoastGuard = passengers_On_CoastGuard;
		Stations_Dropped_Passengers = stations_Dropped_Passengers;
		BlackBoxes_taken = blackBoxes_taken;
	}

	public void setgrid(Object[][] x) {
		this.Grid = x;
	}

	public boolean isdone() {
		int people = 0;
		int boxes = 0;
		for (int i = 0; i < Grid.length; i++) {
			for (int j = 0; j < Grid[0].length; j++) {
				if (Grid[i][j] instanceof Ship) {

					people = people + ((Ship) Grid[i][j]).NumberOfPassengers;
					if (((Ship) Grid[i][j]).Blackbox_Available & !((Ship) Grid[i][j]).Blackbox_taken) {
						boxes++;
					}

				}
			}
		}
		if (people == 0 & Passengers_On_CoastGuard == 0 & boxes == 0) {
			return true;
		} else
			return false;

	}
	public boolean isdone2() {
		int people = 0;
		int boxes = 0;
		for (int i = 0; i < Grid.length; i++) {
			for (int j = 0; j < Grid[0].length; j++) {
				if (Grid[i][j] instanceof Ship) {

					people = people + ((Ship) Grid[i][j]).NumberOfPassengers;
					if (((Ship) Grid[i][j]).Blackbox_Available & !((Ship) Grid[i][j]).Blackbox_taken) {
						boxes++;
					}

				}
			}
		}
		if (people == 0) {
			return true;
		} else
			return false;

	}

	public int peopleretrived() {
		for (int i = 0; i < Grid.length; i++) {
			for (int j = 0; j < Grid[0].length; j++) {
				if (Grid[i][j] instanceof Station) {

					retrived = ((Station) Grid[i][j]).getPassengers_dropped();

				}
			}
		}
		return retrived;

	}

	public void kill() {
		boolean z = false;

		for (int i = 0; i < Grid.length; i++) {
			for (int j = 0; j < Grid[0].length; j++) {
				if (Grid[i][j] instanceof Ship) {

					z = ((Ship) Grid[i][j]).kill();
					if (z)
						deathcount++;

				}
			}
		}

	}

	public int getAgent_X() {
		return Agent_X;
	}

	public String getstate() {

		return Agent_X + "," + Agent_Y + "," + "," + Agent_Capacity + "," + Stations_Dropped_Passengers + ","
				+ Passengers_On_CoastGuard + BlackBoxes_taken;

	}

	public String shipsstatus() {
		String shipstatus = "";
		peoplealive = 0;
		numberofships=0;
		shipsloc_summed= "";
		shipsloc = "";
		stationloc = "";
		// numberofships= 5;
		for (int i = 0; i < Grid.length; i++) {
			for (int j = 0; j < Grid[0].length; j++) {
				if (Grid[i][j] instanceof Ship && ((Ship) Grid[i][j]).isWrecked == false) {
					peoplealive += ((Ship) Grid[i][j]).getNumberOfPassengers();
					numberofships++;
					shipstatus += ((Ship) Grid[i][j]).getNumberOfPassengers() + ",";
					shipsloc_summed += i + j + ",";
					shipsloc += i + "," + j + "," + ((Ship) Grid[i][j]).getNumberOfPassengers() + ",";

				} else if (Grid[i][j] instanceof Station) {
					
					stationloc += i + "," + j + ",";

				}

			}

		}


		shipsloc_summed.substring(0, shipsloc_summed.length() - 1);
		shipsloc.substring(0, shipsloc.length() - 1);
		stationloc.substring(0, stationloc.length() - 1);

		return shipstatus;
	}

	public int shipswrecked() {
		int shipstates = 0;
		for (int i = 0; i < Grid.length; i++) {
			for (int j = 0; j < Grid[0].length; j++) {
				if (Grid[i][j] instanceof Ship) {
					if (((Ship) Grid[i][j]).getNumberOfPassengers() == 0) {
						shipstates++;
					}

				}

			}

		}

		return shipstates;
	}

	public void setpeoplealive(int x) {
		peoplealive = x;
	}

	public void setAgent_X(int agent_X) {
		Agent_X = agent_X;
	}

	public int getAgent_Y() {
		return Agent_Y;
	}

	public void setAgent_Y(int agent_Y) {
		Agent_Y = agent_Y;
	}

	public Object[][] getGrid() {
		return Grid;
	}

	public void createGrid(int i, int j) {
		Grid = new Object[i][j];
	}

	public void setGrid(Object[][] grid) {
		Grid = grid;
	}

	public int getAgent_Capacity() {
		return Agent_Capacity;
	}

	public void setAgent_Capacity(int agent_Capacity) {
		Agent_Capacity = agent_Capacity;
	}

	public int getPassengers_On_CoastGuard() {
		return Passengers_On_CoastGuard;
	}

	public void setPassengers_On_CoastGuard(int passengers_On_CoastGuard) {
		Passengers_On_CoastGuard = passengers_On_CoastGuard;
	}

	public int getStations_Dropped_Passengers() {
		return Stations_Dropped_Passengers;
	}

	public void setStations_Dropped_Passengers(int stations_Dropped_Passengers) {
		Stations_Dropped_Passengers = stations_Dropped_Passengers;
	}

	public int getBlackBoxes_taken() {
		return BlackBoxes_taken;
	}

	public void setBlackBoxes_taken(int blackBoxes_taken) {
		BlackBoxes_taken = blackBoxes_taken;
	}

	@Override
	public int compareTo(Node o) {
		// TODO Auto-generated method stub

		if (this.time == o.time)
			return 0;
		else if (this.time > o.time)
			return 1;
		else
			return -1;

	}

}
