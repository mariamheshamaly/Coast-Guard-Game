package code;
import java.awt.print.Printable;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class CoastGuard{

	public static Node Initial_Node;
	public static Queue<Node> Queue;
	public static int numberofpeople;
	public static boolean Targetfound = false;
	public static boolean estimationdone = false;
	public static boolean visual = false;
	
	
		
	

	public static void GenGrid() {

		String grid = "";
		Random random = new Random();

		int i = random.nextInt(11) + 5;
		int j = random.nextInt(11) + 5;
		// i = 3;
		// j = 3;
		int[][] TestGrid = new int[i][j];
		grid = grid + Integer.toString(i) + "," + Integer.toString(j) + ";";
		int NumberofPassengers = random.nextInt(71) + 30;
		// NumberofPassengers = 50;
		grid = grid + Integer.toString(NumberofPassengers) + ";";
		int x = random.nextInt(i);
		int y = random.nextInt(j);

		grid = grid + Integer.toString(x) + "," + Integer.toString(y) + ";";
		TestGrid[x][y] = 1;
		// adding stations

		int NumberofShips = random.nextInt(i * j - 2) + 1;
		// NumberofShips = 2;
		int NumberofStations = random.nextInt((i * j) - NumberofShips - 1) + 1;
		// NumberofStations = 1;

		// System.out.println(NumberofStations);
		int temp = NumberofStations;

		while (temp != 0) {

			int Station_X = random.nextInt(i);
			int Station_Y = random.nextInt(j);
			if (TestGrid[Station_X][Station_Y] != 1) {

				grid = grid + Integer.toString(Station_X) + "," + Integer.toString(Station_Y) + ",";
				temp--;
				TestGrid[Station_X][Station_Y] = 1;
			} else {
				continue;

			}

		}
		grid = grid.substring(0, grid.length() - 1);
		grid = grid + ";";

		// adding ships

		// System.out.println(NumberofShips);
		int temp2 = NumberofShips;

		while (temp2 != 0) {
			int Ship_X = random.nextInt(i);
			int Ship_Y = random.nextInt(j);
			if (TestGrid[Ship_X][Ship_Y] != 1) {
				int ShipsPassengers = random.nextInt(101);
				numberofpeople += ShipsPassengers;
				// ShipsPassengers = 100;
				grid = grid + Integer.toString(Ship_X) + "," + Integer.toString(Ship_Y) + "," + ShipsPassengers + ",";
				temp2--;
				TestGrid[Ship_X][Ship_Y] = 1;
			} else {
				Ship_X = random.nextInt(i);
				Ship_Y = random.nextInt(j);

			}

		}
		grid = grid.substring(0, grid.length() - 1);
		grid = grid + ";";

		Node Initial_Node = new Node();
		UnderstandIndex(grid, Initial_Node);
	}

	public static void UnderstandIndex(String grid, Node current_node) {
		// grid = "2,2;50;0,0;1,0;1,1,100,0,1,100;";
		// grid = "2,2;50;0,0;1,1;0,1,50,1,0,100;";

		String[] array = grid.split(";");
		// System.out.println(grid);

		// System.out.println("output array after splitting with . : " +
		// Arrays.toString(array));
		String[] GridSize = array[0].split(",");

		int i = Integer.parseInt(GridSize[0]);
		int j = Integer.parseInt(GridSize[1]);
		// System.out.println("Grid rows= "+ i + " Grid Columns= " +j);
		int maxPassengers = Integer.parseInt(array[1]);

		String[] AgentPosition = array[2].split(",");
		int x = Integer.parseInt(AgentPosition[0]);
		int y = Integer.parseInt(AgentPosition[1]);
		current_node.setAgent_X(x);
		current_node.setAgent_Y(y);
		// System.out.println("Max number of passengers= " + maxPassengers);
		// System.out.println("position x of agent= "+ x + " position y of agent= " +y);
		String[] StationsPosition_String = array[3].split(",");
		int[] StationsPositions = new int[StationsPosition_String.length];
		for (int k = 0; k < StationsPosition_String.length; k++) {

			StationsPositions[k] = Integer.parseInt(StationsPosition_String[k]);
		}
		String[] ShipsPosition_String = array[4].split(",");
		int[] ShipsPositions = new int[ShipsPosition_String.length];
		for (int m = 0; m < ShipsPosition_String.length; m++) {

			ShipsPositions[m] = Integer.parseInt(ShipsPosition_String[m]);
		}

		current_node.createGrid(i, j);
		current_node.setAgent_Capacity(maxPassengers);
		ShipsAllocation(ShipsPositions, current_node);
		StationsAllocation(StationsPositions, current_node);
		Queue = new LinkedList<Node>();
		Initial_Node = current_node;

	}

	public static void ShipsAllocation(int[] Positions, Node current_node) {
//		System.out.println(Positions.length);
//	    int test=0;
		int NumberofPassengers;
		// System.out.println(Arrays.toString(Positions));
		for (int i = 0; i < Positions.length; i += 3) {

			NumberofPassengers = Positions[i + 2];
			Ship temp = new Ship(NumberofPassengers);
			Object[][] Grid = current_node.getGrid();

			// System.out.println(Grid[2][2] instanceof Ship);
			Grid[Positions[i]][Positions[i + 1]] = temp;
			current_node.setGrid(Grid);
			// System.out.println(Grid[2][2] instanceof Ship);

		}

	}

	public static void StationsAllocation(int[] Stations, Node current_node) {

		// System.out.println(Stations.length);
		// int test=0;
		for (int i = 0; i < Stations.length; i += 2) {
			Station temp = new Station();
			Object[][] Grid = current_node.getGrid();
			Grid[Stations[i]][Stations[i + 1]] = temp;
			current_node.setGrid(Grid);

		}

	}

	public static void PickUp(Node current_node) {

		Object[][] Grid = current_node.getGrid();

		int Agent_X = current_node.getAgent_X();
		int Agent_Y = current_node.getAgent_Y();

		if (Grid[Agent_X][Agent_Y] instanceof Ship) {

			int Agent_Capacity = current_node.getAgent_Capacity();

			int Passengers_Saved = ((Ship) Grid[Agent_X][Agent_Y]).pickup(Agent_Capacity);

			Agent_Capacity = Agent_Capacity - Passengers_Saved;
			current_node.setAgent_Capacity(Agent_Capacity);
			// System.out.println("final capacity " + current_node.getAgent_Capacity());
			int Passengers_On_CoastGuard = current_node.getPassengers_On_CoastGuard();
			Passengers_On_CoastGuard += Passengers_Saved;
			current_node.setPassengers_On_CoastGuard(Passengers_On_CoastGuard);
			// System.out.println("Passner on coast_guard afterwards "+
			// current_node.getPassengers_On_CoastGuard());

		}
	}

	public static void Retrive(Node current_node) {

		Object[][] Grid = current_node.getGrid();
		int Agent_X = current_node.getAgent_X();
		int Agent_Y = current_node.getAgent_Y();
		if (Grid[Agent_X][Agent_Y] instanceof Ship) {
			// System.out.println("Before "+ temp.Blackbox_taken);
			if (((Ship) Grid[Agent_X][Agent_Y]).Blackbox_taken == false
					&& ((Ship) Grid[Agent_X][Agent_Y]).isWrecked == true
					&& ((Ship) Grid[Agent_X][Agent_Y]).Blackbox_Available == true) {
				((Ship) Grid[Agent_X][Agent_Y]).Takebox();
				int BlackBoxes_taken = current_node.getBlackBoxes_taken();
				BlackBoxes_taken += 1;
				current_node.setBlackBoxes_taken(BlackBoxes_taken);
				// System.out.println("AFter " +((Ship) Grid[Agent_X][Agent_Y]).Blackbox_taken);
				// System.out.println(BlackBoxes_taken);
			}

		}
	}

	public static void Up(Node current_node) {
		int Agent_X = current_node.getAgent_X();
		if (Agent_X != 0) {
			Agent_X -= 1;
		}
		current_node.setAgent_X(Agent_X);

	}

	public static void Down(Node current_node) {
		Object[][] Grid = current_node.getGrid();
		int Agent_X = current_node.getAgent_X();
		int z = Grid.length;
		if (Agent_X != z) {
			Agent_X += 1;
		}
		current_node.setAgent_X(Agent_X);

	}

	public static void Left(Node current_node) {
		int Agent_Y = current_node.getAgent_Y();
		if (Agent_Y != 0) {
			Agent_Y -= 1;
		}
		current_node.setAgent_Y(Agent_Y);

	}

	public static void Right(Node current_node) {
		Object[][] Grid = current_node.getGrid();
		int z = Grid[0].length;
		int Agent_Y = current_node.getAgent_Y();
		if (Agent_Y != z) {
			Agent_Y += 1;
		}
		current_node.setAgent_Y(Agent_Y);

	}

	public static void Drop(Node current_node) {

		Object[][] Grid = current_node.getGrid();
		int Passengers_On_CoastGuard = current_node.getPassengers_On_CoastGuard();

		int Agent_X = current_node.getAgent_X();
		int Agent_Y = current_node.getAgent_Y();

		if (Grid[Agent_X][Agent_Y] instanceof Station) {
			int x = ((Station) Grid[Agent_X][Agent_Y]).getPassengers_dropped();
			((Station) Grid[Agent_X][Agent_Y]).setPassengers_dropped(x + Passengers_On_CoastGuard);
			int Stations_Dropped_Passengers = current_node.getStations_Dropped_Passengers();
			Stations_Dropped_Passengers += Passengers_On_CoastGuard;
			current_node.setStations_Dropped_Passengers(Stations_Dropped_Passengers);
			int Agent_Capacity = current_node.getAgent_Capacity();
			Agent_Capacity += Passengers_On_CoastGuard;
			current_node.setAgent_Capacity(Agent_Capacity);
			current_node.setPassengers_On_CoastGuard(0);
			// System.out.println("Stations Dropped passenger sofar " +
			// Stations_Dropped_Passengers);
			// System.out.println("Capacity after dropping " +Agent_Capacity);

		}

	}

	public static void visualize(Node x) {
		

	}

	public static ArrayList<String> ExpandHelper(Node current_node) {
		// System.out.println("m12");
//		printing(current_node);
		// System.out.println("m13");

		int Agent_X = current_node.getAgent_X();
		int Agent_Y = current_node.getAgent_Y();

		// System.out.println("Node in the expandhelper " + Agent_X + " , " + Agent_Y);
		// System.out.println("Size of the node grid " + current_node.getGrid().length);
		Object[][] Grid = current_node.getGrid();

		int Agent_Capacity = current_node.getAgent_Capacity();

		ArrayList<String> Possible_Actions = new ArrayList<String>();
		// Agent_X = Grid.length - 1;
		// Agent_Y = Grid[0].length - 1;

		// top left corner
		boolean Instanceof_Ship = Grid[Agent_X][Agent_Y] instanceof Ship;

		if (Instanceof_Ship) {
			Ship current_ship = ((Ship) Grid[Agent_X][Agent_Y]);

			if (current_ship.isWrecked & current_ship.Blackbox_Available & current_ship.Blackbox_taken == false
					& current_ship.NumberOfPassengers <= 0) {
				Possible_Actions.add("Retrive");
			}
		}
//path -> right,pickup,retrive,down,drop,left,pickup,right,drop,left,pickup,right,drop,left
		if (Instanceof_Ship && Agent_Capacity != 0 && ((Ship) Grid[Agent_X][Agent_Y]).NumberOfPassengers != 0) {
			Ship current_ship = ((Ship) Grid[Agent_X][Agent_Y]);
			if (current_ship.NumberOfPassengers > 0) {
				Possible_Actions.add("PickUp");
			}
		}

		boolean Instanceof_Station = Grid[Agent_X][Agent_Y] instanceof Station;
		// a check en m3aya nas

		if (Instanceof_Station && current_node.getPassengers_On_CoastGuard() != 0) {

			Possible_Actions.add("Drop");
		}

		if (Agent_X == 0 & Agent_Y == 0) {
			Possible_Actions.add("Right");
			Possible_Actions.add("Down");
		}
		// bottom left corner
		if (Agent_X == Grid.length - 1 && Agent_Y == 0) {
			Possible_Actions.add("Right");
			Possible_Actions.add("Up");
		}
		// top right corner
		if (Agent_X == 0 && Agent_Y == Grid[0].length - 1) {
			Possible_Actions.add("Left");
			Possible_Actions.add("Down");
		}
		// bottom right corner
		if (Agent_X == Grid.length - 1 && Agent_Y == Grid[0].length - 1) {
			Possible_Actions.add("Left");
			Possible_Actions.add("Up");
			// System.out.println("HIIIIIIIIII");

		}
		// first row but not a corner
		if (Agent_X == 0 && Agent_Y != 0 && Agent_Y != Grid[0].length - 1) {

			Possible_Actions.add("Left");
			Possible_Actions.add("Right");
			Possible_Actions.add("Down");

		}
		// first col but not corners
		if (Agent_X != 0 && Agent_X != Grid.length - 1 && Agent_Y == 0) {
			Possible_Actions.add("Up");
			Possible_Actions.add("Right");
			Possible_Actions.add("Down");
		}
		// last col but not corner
		if (Agent_X != 0 && Agent_X != Grid.length - 1 && Agent_Y == Grid[0].length - 1) {
			Possible_Actions.add("Up");
			Possible_Actions.add("Left");
			Possible_Actions.add("Down");
		}
		// last row but not corners
		if (Agent_X == Grid.length - 1 && Agent_Y != 0 && Agent_Y != Grid[0].length - 1) {
			Possible_Actions.add("Up");
			Possible_Actions.add("Left");
			Possible_Actions.add("Right");
		}
		// anywhere else
		if (Agent_X != 0 && Agent_X != Grid.length - 1 && Agent_Y != 0 && Agent_Y != Grid[0].length - 1) {

			Possible_Actions.add("Up");
			Possible_Actions.add("Left");
			Possible_Actions.add("Right");
			Possible_Actions.add("Down");
		}
//		boolean haspickup = false;
//		for (int i = 0; i < Possible_Actions.size(); i++) {
//			if (Possible_Actions.get(i).equals("PickUp")) {
//				haspickup = true;
//
//			}
//		}
//
//		if (haspickup) {
//			Possible_Actions.removeAll(Possible_Actions);
//			Possible_Actions.add("PickUp");
//		}
//
//		// ___________
//		boolean hasdrop = false;
//		for (int i = 0; i < Possible_Actions.size(); i++) {
//			if (Possible_Actions.get(i).equals("Drop")) {
//				hasdrop = true;
//
//			}
//		}
//
//		if (hasdrop) {
//			Possible_Actions.removeAll(Possible_Actions);
//			Possible_Actions.add("Drop");
//		}

		// System.out.println(Possible_Actions);

		return Possible_Actions;
		// Perform_PossibleActions(test, current_node);

	}

	public static Object[][] newgrid(Object[][] x) {
		Object[][] newobject = new Object[x.length][x[0].length];
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				if (x[i][j] instanceof Ship) {

					Ship temp = new Ship((Ship) x[i][j]);
					newobject[i][j] = temp;

				} else if (x[i][j] instanceof Station) {

					Station temp = new Station((Station) x[i][j]);
					newobject[i][j] = temp;

				}

			}
		}

		return newobject;

	}

	public static void boxtime(Node x) {

		int Agent_X = x.getAgent_X();
		int Agent_Y = x.getAgent_Y();
		boolean Instanceof_Ship = x.Grid[Agent_X][Agent_Y] instanceof Ship;

		if (Instanceof_Ship) {
			((Ship) x.Grid[Agent_X][Agent_Y]).boxhelper();
		}

	}

	public static ArrayList<Node> Perform_PossibleActions(ArrayList Possible_Actions, Node Parent_Node) {
		ArrayList<Node> result = new ArrayList<Node>();

		for (int i = 0; i < Possible_Actions.size(); i++) {
			Object[][] Grid = newgrid(Parent_Node.getGrid());

			String action = ((String) Possible_Actions.get(i));

			switch (action) {
			case "Up":
				Node new_node = new Node(Parent_Node);
				new_node.setgrid(Grid);

				Up(new_node);
				// boxtime(new_node);
				new_node.kill();
				new_node.path += "up,";

				result.add(new_node);
				break;
			case "Down":
				Node new_node2 = new Node(Parent_Node);
				new_node2.setgrid(Grid);
				// System.out.println(new_node2.getAgent_X() + " , " + new_node2.getAgent_Y());
				Down(new_node2);
				// boxtime(new_node2);
				new_node2.kill();
				new_node2.path += "down,";

				// System.out.println("after performing Down " + new_node2.getAgent_X() + " , "
				// + new_node2.getAgent_Y());

				// ExpandHelper(new_node2);
				result.add(new_node2);
				break;
			case "Right":

				Node new_node3 = new Node(Parent_Node);
				new_node3.setgrid(Grid);
				// System.out.println(new_node3.getAgent_X() + " , " + new_node3.getAgent_Y());
				Right(new_node3);
				// boxtime(new_node3);
				new_node3.kill();
				new_node3.path += "right,";

				// System.out.println("after performing Right " + new_node3.getAgent_X() + " , "
				// + new_node3.getAgent_Y());
				// ExpandHelper(new_node3);
				result.add(new_node3);
				break;
			case "Left":
				Node new_node4 = new Node(Parent_Node);
				new_node4.setgrid(Grid);
				// System.out.println(new_node4.getAgent_X() + " , " + new_node4.getAgent_Y());
				Left(new_node4);
				// boxtime(new_node4);
				new_node4.kill();
				new_node4.path += "left,";

				// System.out.println("after performing Left " + new_node4.getAgent_X() + " , "
				// + new_node4.getAgent_Y());
				// ExpandHelper(new_node4);
				result.add(new_node4);
				break;
			case "PickUp":

				Node new_node5 = new Node(Parent_Node);
				new_node5.setgrid(Grid);

				// System.out.println(new_node5.getAgent_Capacity());
				PickUp(new_node5);
				// boxtime(new_node5);
				new_node5.kill();
				new_node5.path += "pickup,";

				// System.out.println("Capacity after performing Pickup " +
				// new_node5.getAgent_Capacity());
				// System.out.println("passengers on coast guard after performing Pickup " +
				// new_node5.getPassengers_On_CoastGuard());
				// ExpandHelper(new_node5);
				result.add(new_node5);
				break;
			case "Drop":
				Node new_node6 = new Node(Parent_Node);

				new_node6.setgrid(Grid);

				// System.out.println(new_node6.getPassengers_On_CoastGuard());
				Drop(new_node6);
				// boxtime(new_node6);
				new_node6.kill();
				new_node6.path += "drop,";
				// System.out.println("after performing Drop " +
				// new_node6.getPassengers_On_CoastGuard());
				// ExpandHelper(new_node6);
				result.add(new_node6);
				break;
			case "Retrive":
				// System.out.println("55555555555555555555555555555555555555555");
				Node new_node7 = new Node(Parent_Node);
				new_node7.setgrid(Grid);
				Retrive(new_node7);
				// boxtime(new_node7);
				new_node7.kill();
				new_node7.path += "retrieve,";

				// ExpandHelper(new_node7);

				result.add(new_node7);
				// System.out.println("ATHENA "+resultformat(new_node7) );
				// printing(new_node7);
				break;

			}
			// break;
			// printing(result.get(i));

		}

		return result;

	}

	public static String BFS() {
		String result = "";
		Node initial = Initial_Node;
		ArrayList<Node> Nodes_to_expand;
		ArrayList<String> Possible_Action;
		ArrayList<String> states = new ArrayList<String>();
		states.add(initial.getstate());
		Queue.add(initial);

		while (!Queue.isEmpty()) {

			Node deque = Queue.remove();

			if(visual==true) {
				printing(deque);
			}

//				    Possible_Action = ExpandHelper(deque,false);
//				if(Possible_Action.equals("Drop") || Possible_Action.equals("PickUp")) {
//					 Nodes_to_expand = Perform_PossibleActions(Possible_Action, deque);
//					 Possible_Action = ExpandHelper(deque,true);
//					 Nodes_to_expand = Perform_PossibleActions(Possible_Action, deque);
//				}else {
			Possible_Action = ExpandHelper(deque);
			Nodes_to_expand = Perform_PossibleActions(Possible_Action, deque);

			// }

			for (int i = 0; i < Nodes_to_expand.size(); i++) {

				if (!states.contains(Nodes_to_expand.get(i).getstate())) {

					states.add(Nodes_to_expand.get(i).getstate());
					if (Nodes_to_expand.get(i).isdone()) {
						// printing(Nodes_to_expand.get(i));
						result = resultformat(Nodes_to_expand.get(i));
						return result;
					}
					result = resultformat(Nodes_to_expand.get(i));
					Queue.add(Nodes_to_expand.get(i));

				}
				// printing(Nodes_to_expand.get(i));

			}

//	}

		}

		return result;
	}

	public static String resultformat(Node deque) {
		String result = "";
		result = deque.path.substring(0, deque.path.length() - 1);
		int nodes = (deque.path.split(",")).length;
		return result + ";" + deque.deathcount + ";" + deque.BlackBoxes_taken + ";" + nodes;
	}

	public static int depth(Node x) {

		int nodes = (x.path.split(",")).length;

		return nodes;

	}

	public static void printing(Node x) {
		
		for (int i = 0; i < x.getGrid().length; i++) {
			for (int j = 0; j < x.getGrid()[0].length; j++) {
				if (x.getGrid()[i][j] instanceof Ship) {
					System.out.println("_____________Ship_______________");
					System.out.println("Ship location ->" + i + "," + j);
					System.out.println("Number of passengers-> " +((Ship) x.getGrid()[i][j]).getNumberOfPassengers());
					

					

				} else if (x.getGrid()[i][j] instanceof Station) {
					
					System.out.println("_____________Station_______________");
					System.out.println("Station location ->" + i + "," + j);
					System.out.println("Number of survivors on station-> " +((Station) x.getGrid()[i][j]).getPassengers_dropped());
					

				}

			}
		}
		
		System.out.println("__________my node information_________");

		System.out.println("location now ->" + x.Agent_X + "," + x.Agent_Y);
		System.out.println("capacity ->" + x.getAgent_Capacity());
		System.out.println("on guard ->" + x.Passengers_On_CoastGuard );
		System.out.println("boxes retrived ->" + x.BlackBoxes_taken);
		System.out.println("numberofdeaths ->" + x.deathcount);

	}

	static String DFS() {
		String result = "";
		Node initial = Initial_Node;
		ArrayList<Node> Nodes_to_expand;
		ArrayList<String> Possible_Action;
		ArrayList<String> states = new ArrayList<String>();
		states.add(initial.getstate());
		Stack<Node> st = new Stack<Node>();
		st.push(initial);

		while (!st.empty()) {

			Node current = st.pop();
			if(visual==true) {
				printing(current);
			}

			Possible_Action = ExpandHelper(current);
			Nodes_to_expand = Perform_PossibleActions(Possible_Action, current);

			// }

			for (int i = 0; i < Nodes_to_expand.size(); i++) {

				if (!states.contains(Nodes_to_expand.get(i).getstate())) {

					states.add(Nodes_to_expand.get(i).getstate());
					if (Nodes_to_expand.get(i).isdone()) {
						// printing(Nodes_to_expand.get(i));
						result = resultformat(Nodes_to_expand.get(i));
						return result;
					}
					result = resultformat(Nodes_to_expand.get(i));
					st.push(Nodes_to_expand.get(i));

				}
				// printing(Nodes_to_expand.get(i));

			}

//}

		}

		return result;

	}

	public static String ID() {
		int depth = -1;
		Targetfound = false;

		while (!Targetfound) {
			// Node temp = new Node(Initial_Node);
			// temp.setgrid(temp.getGrid());
			depth++;
			IDhelper(Initial_Node, depth);

			// System.out.println(depth);

		}

		// depth++;
		// depth++;
		return IDhelper(Initial_Node, depth);
	}

	public static String IDhelper(Node initial, int depth) {

		String result = "";
		// initial = Initial_Node;
		ArrayList<Node> Nodes_to_expand;
		ArrayList<String> Possible_Action;
		ArrayList<String> states = new ArrayList<String>();
		states.add(initial.getstate());
		Stack<Node> st = new Stack<Node>();
		st.push(initial);

		while (!st.empty()) {

			Node current = st.pop();

			if (depth(current) >= depth)
				continue;

			if(visual==true) {
				printing(current);
			}

			Possible_Action = ExpandHelper(current);
			Nodes_to_expand = Perform_PossibleActions(Possible_Action, current);

			// }

			for (int i = 0; i < Nodes_to_expand.size(); i++) {

				if (!states.contains(Nodes_to_expand.get(i).getstate())) {

					states.add(Nodes_to_expand.get(i).getstate());
					if (Nodes_to_expand.get(i).isdone()) {
						// printing(Nodes_to_expand.get(i));
						result = resultformat(Nodes_to_expand.get(i));
						// printing(Nodes_to_expand.get(i));
						Targetfound = true;
						return result;
					}
					result = resultformat(Nodes_to_expand.get(i));
					st.push(Nodes_to_expand.get(i));

				}
				// printing(Nodes_to_expand.get(i));

			}

//}
			// printing(current);
		}

		return result;

	}

	public static String GR1() {

		String result = "";
		Node initial = Initial_Node;
		ArrayList<Node> Nodes_to_expand;
		ArrayList<String> Possible_Action;
		ArrayList<String> states = new ArrayList<String>();
		states.add(initial.getstate());
		// Queue.add(initial);
		Comparator<Node> boxescomparator = new Comparator<Node>() {

			@Override
			public int compare(Node left, Node right) {
				return comparehelperGR1(left, right);
			}
		};

		Queue<Node> qe = new PriorityQueue<Node>(boxescomparator);
		// Queue<Node> qe = new PriorityQueue<Node>();
		qe.add(initial);

		while (!qe.isEmpty()) {

			Node deque = qe.remove();

			if(visual==true) {
				printing(deque);
			}

//				    Possible_Action = ExpandHelper(deque,false);
//				if(Possible_Action.equals("Drop") || Possible_Action.equals("PickUp")) {
//					 Nodes_to_expand = Perform_PossibleActions(Possible_Action, deque);
//					 Possible_Action = ExpandHelper(deque,true);
//					 Nodes_to_expand = Perform_PossibleActions(Possible_Action, deque);
//				}else {
			Possible_Action = ExpandHelper(deque);
			Nodes_to_expand = Perform_PossibleActions(Possible_Action, deque);

			// }

			for (int i = 0; i < Nodes_to_expand.size(); i++) {

				if (!states.contains(Nodes_to_expand.get(i).getstate())) {

					states.add(Nodes_to_expand.get(i).getstate());
					if (Nodes_to_expand.get(i).isdone()) {
						// printing(Nodes_to_expand.get(i));
						result = resultformat(Nodes_to_expand.get(i));
						return result;
					}
					result = resultformat(Nodes_to_expand.get(i));
					qe.add(Nodes_to_expand.get(i));

				}
				// printing(Nodes_to_expand.get(i));

			}

//	}

		}

		return result;

	}

	public static String GR2() {

		String result = "";
		Node initial = Initial_Node;
		ArrayList<Node> Nodes_to_expand;
		ArrayList<String> Possible_Action;
		ArrayList<String> states = new ArrayList<String>();
		states.add(initial.getstate());
		// Queue.add(initial);
		Comparator<Node> boxescomparator = new Comparator<Node>() {

			@Override
			public int compare(Node left, Node right) {
				return comparehelperGR2(left, right);
			}
		};

		Queue<Node> qe = new PriorityQueue<Node>(boxescomparator);
		qe.add(initial);

		while (!qe.isEmpty()) {

			Node deque = qe.remove();

			if(visual==true) {
				printing(deque);
			}

//				    Possible_Action = ExpandHelper(deque,false);
//				if(Possible_Action.equals("Drop") || Possible_Action.equals("PickUp")) {
//					 Nodes_to_expand = Perform_PossibleActions(Possible_Action, deque);
//					 Possible_Action = ExpandHelper(deque,true);
//					 Nodes_to_expand = Perform_PossibleActions(Possible_Action, deque);
//				}else {
			Possible_Action = ExpandHelper(deque);
			Nodes_to_expand = Perform_PossibleActions(Possible_Action, deque);

			// }

			for (int i = 0; i < Nodes_to_expand.size(); i++) {

				if (!states.contains(Nodes_to_expand.get(i).getstate())) {

					states.add(Nodes_to_expand.get(i).getstate());
					if (Nodes_to_expand.get(i).isdone()) {
						// printing(Nodes_to_expand.get(i));
						result = resultformat(Nodes_to_expand.get(i));
						return result;
					}
					result = resultformat(Nodes_to_expand.get(i));
					qe.add(Nodes_to_expand.get(i));

				}
				// printing(Nodes_to_expand.get(i));

			}

//	}

		}

		return result;

	}

	public static String AS1() {

		String result = "";
		Node initial = Initial_Node;
		ArrayList<Node> Nodes_to_expand;
		ArrayList<String> Possible_Action;
		ArrayList<String> states = new ArrayList<String>();
		states.add(initial.getstate());
		// Queue.add(initial);
		Comparator<Node> boxescomparator = new Comparator<Node>() {

			@Override
			public int compare(Node left, Node right) {

				return comparehelperAS1(left, right);

			}
		};

		Queue<Node> qe = new PriorityQueue<Node>(boxescomparator);
		qe.add(initial);

		while (!qe.isEmpty()) {

			Node deque = qe.remove();
			if(visual==true) {
				printing(deque);
			}
			// System.out.println(deque.path);
			Possible_Action = ExpandHelper(deque);
			Nodes_to_expand = Perform_PossibleActions(Possible_Action, deque);

			for (int i = 0; i < Nodes_to_expand.size(); i++) {

				if (!states.contains(Nodes_to_expand.get(i).getstate())) {

					states.add(Nodes_to_expand.get(i).getstate());
					if (Nodes_to_expand.get(i).isdone()) {
						// printing(Nodes_to_expand.get(i));
						result = resultformat(Nodes_to_expand.get(i));
						return result;
					}
					result = resultformat(Nodes_to_expand.get(i));
					qe.add(Nodes_to_expand.get(i));

				}
				// printing(Nodes_to_expand.get(i));

			}

		}

		return result;

	}

	public static String AS2() {

		String result = "";
		Node initial = Initial_Node;
		ArrayList<Node> Nodes_to_expand;
		ArrayList<String> Possible_Action;
		ArrayList<String> states = new ArrayList<String>();
		states.add(initial.getstate());
		// Queue.add(initial);
		Comparator<Node> boxescomparator = new Comparator<Node>() {

			@Override
			public int compare(Node left, Node right) {

				return comparehelperAS2(left, right);

			}
		};

		Queue<Node> qe = new PriorityQueue<Node>(boxescomparator);
		qe.add(initial);

		while (!qe.isEmpty()) {

			Node deque = qe.remove();
			if(visual==true) {
				printing(deque);
			}
			// System.out.println(deque.path);
			Possible_Action = ExpandHelper(deque);
			Nodes_to_expand = Perform_PossibleActions(Possible_Action, deque);

			for (int i = 0; i < Nodes_to_expand.size(); i++) {

				if (!states.contains(Nodes_to_expand.get(i).getstate())) {

					states.add(Nodes_to_expand.get(i).getstate());
					if (Nodes_to_expand.get(i).isdone()) {
						// printing(Nodes_to_expand.get(i));
						result = resultformat(Nodes_to_expand.get(i));
						return result;
					}
					result = resultformat(Nodes_to_expand.get(i));
					qe.add(Nodes_to_expand.get(i));

				}
				// printing(Nodes_to_expand.get(i));

			}

		}

		return result;

	}

	public static int comparehelperAS1(Node left, Node right) {
		if (estimation(left) > estimation(right))
			return 1;
		else if (estimation(left) < estimation(right))
			return -1;
		else if (estimation(left) == estimation(right)) {
			if (left.getBlackBoxes_taken() < right.BlackBoxes_taken)
				return -1;
			else
				return 1;

		}

		return 1;

	}

	public static int comparehelperGR1(Node left, Node right) {
		if (estimation(left) > estimation(right))
			return 1;
		else if (estimation(left) < estimation(right))
			return -1;
		else if (estimation(left) == estimation(right)) {
			return 0;

		}

		return 1;

	}

	public static int comparehelperGR2(Node left, Node right) {
		if (estimation2(left) > estimation2(right))
			return 1;
		else if (estimation2(left) < estimation2(right))
			return -1;
		else if (estimation2(left) == estimation2(right)) {
			return 0;

		}

		return 1;

	}

	public static int comparehelperAS2(Node left, Node right) {
		if (estimation2(left) > estimation2(right))
			return 1;
		else if (estimation2(left) < estimation2(right))
			return -1;
		else if (estimation2(left) == estimation2(right)) {
			if (left.deathcount < right.deathcount)
				return 1;
			else
				return -1;

		}

		return 1;

	}

	public static int estimation(Node x) {
		HashMap<Ship, ArrayList<Integer>> myships = new HashMap<Ship, ArrayList<Integer>>();
		Node z = new Node(x);
		z.setGrid(newgrid(x.getGrid()));
		// z.shipsstatus();
		int estimateddeath = 0;
		// String state = "";
		ArrayList<String> shipsnotvalid = new ArrayList<String>();
		int mylocationX = z.getAgent_X();
		int mylocationY = z.getAgent_Y();
		int peoplebefore = 0;
		int pickedup = 0;
		int dropped_people = 0;
		int agentcapacity = z.Agent_Capacity;

		for (int i = 0; i < z.Grid.length; i++) {
			for (int j = 0; j < z.Grid[0].length; j++) {
				if (z.Grid[i][j] instanceof Ship) {
					ArrayList<Integer> shipinfo = new ArrayList<Integer>();
					Ship thisship = (Ship) z.Grid[i][j];
					shipinfo.add(i);
					shipinfo.add(j);
					shipinfo.add(thisship.NumberOfPassengers);

					myships.put((Ship) z.Grid[i][j], shipinfo);

				}
			}
		}
		for (ArrayList<Integer> i : myships.values()) {
			peoplebefore += i.get(2);
		}

		HashMap<Station, ArrayList<Integer>> mystations = new HashMap<Station, ArrayList<Integer>>();

		for (int i = 0; i < z.Grid.length; i++) {
			for (int j = 0; j < z.Grid[0].length; j++) {
				if (z.Grid[i][j] instanceof Station) {
					ArrayList<Integer> stationinfo = new ArrayList<Integer>();
					Station thisstation = (Station) z.Grid[i][j];
					stationinfo.add(i);
					stationinfo.add(j);

					mystations.put((Station) z.Grid[i][j], stationinfo);
				}
			}
		}

		while (done(myships) == true) {
			// System.out.println("hi");

			int[] newloc = nearestShiptome(myships, mylocationX, mylocationY);
			// System.out.println(newloc[0]);
			if (newloc[0] == -1) {

				break;
			}
			// assuming the agent stands at the ship now
			z.setAgent_X(newloc[0]);
			z.setAgent_Y(newloc[1]);

			estimateddeath += newloc[2];

			// pickup
			for (ArrayList<Integer> i : myships.values()) {
				if (i.get(0) == newloc[0] & i.get(1) == newloc[1]) {
					int passg = i.get(2);
					if (passg < z.Agent_Capacity) {
						i.set(2, 0);
						z.Agent_Capacity -= passg;
					} else {
						i.set(2, passg - z.Agent_Capacity);
						z.Agent_Capacity = 0;

					}

				}

			}

			// pickupdone
			newloc[2] = 0;
			// System.out.println(z.Agent_Capacity);
			if (z.Agent_Capacity > 0) {

				continue;

			} else {
				// the agent is at the ship but needs to go drop passengers
				int[] newloc2 = neareststationtome(mystations, myships, z.Agent_X, z.Agent_Y);

				z.setAgent_X(newloc2[0]);
				z.setAgent_Y(newloc2[1]);
				// z.Agent_Capacity+= newloc2[2];
				estimateddeath += newloc2[2];
				dropped_people += agentcapacity;
				z.Agent_Capacity = agentcapacity;
				newloc2[2] = 0;
				// dropped_people += newloc2[2];

				// System.out.println(newloc[2]);

			}
		}

		return estimateddeath;
	}

	public static int estimation2(Node x) {
		HashMap<Ship, ArrayList<Integer>> myships = new HashMap<Ship, ArrayList<Integer>>();
		Node z = new Node(x);
		z.setGrid(newgrid(x.getGrid()));
		// z.shipsstatus();
		int estimateddeath = 0;
		// String state = "";
		ArrayList<String> shipsnotvalid = new ArrayList<String>();
		int mylocationX = z.getAgent_X();
		int mylocationY = z.getAgent_Y();
		int peoplebefore = 0;
		int pickedup = 0;
		int dropped_people = 0;
		int agentcapacity = z.Agent_Capacity;
		int boxes = 0;

		for (int i = 0; i < z.Grid.length; i++) {
			for (int j = 0; j < z.Grid[0].length; j++) {
				if (z.Grid[i][j] instanceof Ship) {
					ArrayList<Integer> shipinfo = new ArrayList<Integer>();
					Ship thisship = (Ship) z.Grid[i][j];
					shipinfo.add(i);
					shipinfo.add(j);
					shipinfo.add(thisship.NumberOfPassengers);

					myships.put((Ship) z.Grid[i][j], shipinfo);

				}
			}
		}
		for (ArrayList<Integer> i : myships.values()) {
			peoplebefore += i.get(2);
		}

		HashMap<Station, ArrayList<Integer>> mystations = new HashMap<Station, ArrayList<Integer>>();

		for (int i = 0; i < z.Grid.length; i++) {
			for (int j = 0; j < z.Grid[0].length; j++) {
				if (z.Grid[i][j] instanceof Station) {
					ArrayList<Integer> stationinfo = new ArrayList<Integer>();
					Station thisstation = (Station) z.Grid[i][j];
					stationinfo.add(i);
					stationinfo.add(j);

					mystations.put((Station) z.Grid[i][j], stationinfo);
				}
			}
		}

		while (done(myships) == true) {
			// System.out.println("hi");

			int[] newloc = nearestShiptome(myships, mylocationX, mylocationY);
			// System.out.println(newloc[0]);
			if (newloc[0] == -1) {

				break;
			}
			// assuming the agent stands at the ship now
			z.setAgent_X(newloc[0]);
			z.setAgent_Y(newloc[1]);

			estimateddeath += newloc[2];

			// pickup
			for (ArrayList<Integer> i : myships.values()) {
				if (i.get(0) == newloc[0] & i.get(1) == newloc[1]) {
					int passg = i.get(2);
					if (passg < z.Agent_Capacity) {
						i.set(2, 0);
						z.Agent_Capacity -= passg;
					} else {
						i.set(2, passg - z.Agent_Capacity);
						z.Agent_Capacity = 0;
						boxes++;

					}

				}

			}

			// pickupdone
			newloc[2] = 0;
			// System.out.println(z.Agent_Capacity);
			if (z.Agent_Capacity > 0) {

				continue;

			} else {
				// the agent is at the ship but needs to go drop passengers
				int[] newloc2 = neareststationtome(mystations, myships, z.Agent_X, z.Agent_Y);

				z.setAgent_X(newloc2[0]);
				z.setAgent_Y(newloc2[1]);
				// z.Agent_Capacity+= newloc2[2];
				estimateddeath += newloc2[2];
				dropped_people += agentcapacity;
				z.Agent_Capacity = agentcapacity;
				newloc2[2] = 0;
				// dropped_people += newloc2[2];

				// System.out.println(newloc[2]);

			}
		}

		return boxes;
	}

	public static int[] nearestShiptome(HashMap<Ship, ArrayList<Integer>> myships, int x, int y) {

		int mylocationX = x;
		int mylocationY = y;
		int min = 9999;
		int nearestX = -1;
		int nearestY = -1;
		int distance = 0;
		int[] newloc = new int[3];
		newloc[2] = 0;

		for (ArrayList<Integer> i : myships.values()) {

//System.out.println(shipsinfo[0]);
			int shipx = i.get(0);
			int shipy = i.get(1);
			distance = getdistance(mylocationX, mylocationY, shipx, shipy);

			if (distance < min & distance < i.get(2)) {
				min = distance;
				nearestX = shipx;
				nearestY = shipy;
			}

		}
		for (ArrayList<Integer> i : myships.values()) {
			if (i.get(2) < distance) {

				i.set(2, 0);
				// newloc[2] += i.get(2);

			} else {
				i.set(2, i.get(2) - distance);

				newloc[2] += distance;

			}

		}
		newloc[2] -= 1;
		newloc[0] = nearestX;
		newloc[1] = nearestY;
		return newloc;

	}

	public static int[] neareststationtome(HashMap<Station, ArrayList<Integer>> mystations,
			HashMap<Ship, ArrayList<Integer>> myships, int x, int y) {

		int mylocationX = x;
		int mylocationY = y;
		int min = 9999;
		int nearestX = -1;
		int nearestY = -1;
		int distance = 0;
		int[] newloc = new int[3];
		newloc[2] = 0;

		for (ArrayList<Integer> i : mystations.values()) {

//System.out.println(shipsinfo[0]);
			int shipx = i.get(0);
			int shipy = i.get(1);
			distance = getdistance(mylocationX, mylocationY, shipx, shipy);

			if (distance < min) {
				min = distance;
				nearestX = shipx;
				nearestY = shipy;
			}

		}

		for (ArrayList<Integer> i : myships.values()) {
			if (i.get(2) < distance) {
				i.set(2, 0);
				// newloc[2] += i.get(2);
			} else {
				i.set(2, i.get(2) - distance);
				newloc[2] += distance - 1;

			}

		}

		newloc[0] = nearestX;
		newloc[1] = nearestY;
		return newloc;

	}

	public static int getdistance(int mylocationX, int mylocationY, int goX, int goY) {
		return Math.abs((mylocationX + mylocationY) - (goX + goY));

	}

	public static boolean done(HashMap<Ship, ArrayList<Integer>> myships) {

		for (ArrayList<Integer> i : myships.values()) {

			if (i.get(2) > 0) {
				i.set(2, i.get(2) - 1);
				return true;
			}
		}

		return false;

	}

	public static String solve(String grid, String Strategy, boolean visualize) {
		GenGrid();
		int x = 0;
		int y = 0;
		visual =false;
		visual =visualize;
		

		for (int i = 0; i < grid.length(); i++) {
			if (grid.charAt(i) == (',')) {
				x = i;
				break;
			}

		}
		for (int i = 0; i < grid.length(); i++) {
			if (grid.charAt(i) == (';')) {
				y = i;
				break;
			}

		}

		grid = grid.substring(x + 1, y) + "," + grid.substring(0, x) + grid.substring(y, grid.length());

		UnderstandIndex(grid, Initial_Node);

		if (Strategy.equals("BF")) {

			return BFS();
		}
		if (Strategy.equals("DF")) {

			return DFS();
		}
		if (Strategy.equals("ID")) {

			return ID();
		}
		if (Strategy.equals("GR1")) {

			return GR1();
		}
		if (Strategy.equals("GR2")) {

			return GR2();
		}
		if (Strategy.equals("AS1")) {

			return AS1();
		}

		if (Strategy.equals("AS2")) {

			return AS2();
		}

		return "";

	}

	public static void main(String[] args) {

		// "7,5;100;3,4;2,6,3,5;0,0,4,0,1,8,1,4,77,1,5,1,3,2,94,4,3,46;"
		// System.out.println(solve("7,5;100;3,4;2,6,3,5;0,0,4,0,1,8,1,4,77,1,5,1,3,2,94,4,3,46;", "GR1", false));
		// System.out.println(solve("8,5;60;4,6;2,7;3,4,37,3,5,93,4,0,40;",
		// "AS1",false));
		// false));
		// GenGrid();
		// UnderstandIndex("5,8;60;4,6;2,7;3,4,37,3,5,93,4,0,40;", Initial_Node);
		// System.out.println(estimation2(Initial_Node));
		// BFS();

	}

}