package graphingCalc;

import java.util.Scanner;

public class GraphingCalc {
	
	private static boolean debugging = false;
	private static Scanner scan = new Scanner(System.in);
	private static String currentScan = "";
	private static Graph[] graphers = {new Graph(.1,.1, "", "X"), new Graph(.1,.1, "", "C"), new Graph(.1,.1, "", "O"),
									   new Graph(.1,.1, "", "U"), new Graph(.1,.1, "", "D")};

	public static void main(String[] args) {
		
		//doPrintedUI();

		String equation = "y = ((x*cos(45) - y*sin(45))/1.7321)^2 + ((x*sin(45) + y*cos(45))/3.1622)^2";
		
		Solver.interpretAndSolve("(((-2.0)*cos(45)-(0.0)*sin(45))/1.7321)^2+(((-2.0)*sin(45)+(0.0)*cos(45))/3.1622)^2", true, false);
		
		/*
		Graph advanTest = new Graph(.05, .05, equation, "X");
		advanTest.setXMax(4);
		advanTest.setXMin(-2);
		advanTest.setYMax(8);
		advanTest.setYMin(-2);
		
		advanTest.makeAdvancedGraph(0.0065);
		
		System.out.println(advanTest.getGraph());
		*/
		
		scan.close();
	}
	
	public static void doPrintedUI() {
		System.out.println("Welcome to my knock-off TI-84");
		System.out.println("Type \"manual\" for instructions");
		
		while(true) {
			//take the user input
			currentScan = scan.nextLine();
			
			//complete respective action for user input
			if(currentScan.equals("debug"))
				activateDebugMode(); //show solve process for all equations completed
			
			else if(currentScan.equals("manual")) 
				printManual(); //print a user manual
			
			else if(currentScan.equals("y=")) 
				doYEquals(); //go into graph equation input
			
			else if(currentScan.equals("window"))
				doWindow(); //go into window mode to change graph bounds
			
			else if(currentScan.equals("graph"))
				doGraph(); //print the current graph
			
			else if(currentScan.equals("close")) 
				return; //end method and subsequently the program
			
			else if(currentScan.equals("quit"));
				//blank condition allowing quit to do nothing
			
			else 
				//if not a command, try to solve input string as an equation
				doEquationSolve();
			
		} //end while
	} //end method
	
	public static void activateDebugMode() {
		debugging = true;
		System.out.println("debug mode active");
	}
	public static void doEquationSolve() {
		if(debugging) System.out.println("["+currentScan+"]");
		
		try {
			String solution;
			if(debugging) 
				solution = Solver.interpretAndSolve(currentScan, true, false);
			else 
				solution = Solver.interpretAndSolve(currentScan, false, false);
			
			System.out.println("-------------------------------------------\n"
							   +solution);
			
		} catch(Exception E) {
			System.out.println("SYNTAX ERROR\n"+
							   "-------------------------------------------");
		}
	}
	public static void doWindow() {
		while(!currentScan.equals("quit")) {
			System.out.println(graphers[0]);
			System.out.println("Changing:");
			currentScan = scan.nextLine();
			try {
				if(currentScan.equals("xMax")) {
					System.out.println("to:");
					currentScan = scan.nextLine();
					for(Graph g: graphers) g.setXMax(Double.valueOf(currentScan));
					
				} else if(currentScan.equals("xMin")) {
					System.out.println("to:");
					currentScan = scan.nextLine();

					for(Graph g: graphers) g.setXMin(Double.valueOf(currentScan));
					
				} else if(currentScan.equals("yMax")) {
					System.out.println("to:");
					currentScan = scan.nextLine();
					for(Graph g: graphers) g.setYMax(Double.valueOf(currentScan));
					
				} else if(currentScan.equals("yMin")) {
					System.out.println("to:");
					currentScan = scan.nextLine();
					for(Graph g: graphers) g.setYMin(Double.valueOf(currentScan));
					
				} else if(currentScan.equals("xStep")) {
					System.out.println("to:");
					currentScan = scan.nextLine();
					for(Graph g: graphers) g.setXStep(Double.valueOf(currentScan));
					
				} else if(currentScan.equals("yStep")) {
					System.out.println("to:");
					currentScan = scan.nextLine();
					for(Graph g: graphers) g.setYStep(Double.valueOf(currentScan));
					
				} else if(!currentScan.equals("quit")){
					System.out.println("Invalid variable name");
				}
			} catch(Exception e) {
				System.out.println("Invalid number");
			}
			
		} for(int i=0;i<42;i++) System.out.println("");
	}
	public static void doGraph() {
		try {
			System.out.println(Graph.overlayGraphs(graphers));
		} catch(Exception e) {
			System.out.println("GRAPHING ERROR");
			System.out.println("-------------------------------------------");
		}
	}
	public static void doYEquals() {
		boolean printStatus = true;
		while(!currentScan.equals("quit")) {
			if(printStatus) {
				System.out.println("Graph equation:\n"
						  		+"X: y= "+graphers[0].getEquation()+"\n"
						  		+"C: y= "+graphers[1].getEquation()+"\n"
						  		+"O: y= "+graphers[2].getEquation()+"\n"
						  		+"U: y= "+graphers[3].getEquation()+"\n"
						  		+"D: y= "+graphers[4].getEquation()+"\n"
						  		+"Choose graph to alter:");
				printStatus = false;
			}
			System.out.println("Graph on:");
			currentScan = scan.nextLine();
			
			if(currentScan.equals("X")) {
				System.out.println("Give equation:");
				currentScan = scan.nextLine();
				graphers[0].setEquation(currentScan);
				printStatus = true;
				
			} else if(currentScan.equals("C")) {
				System.out.println("Give equation:");
				currentScan = scan.nextLine();
				graphers[1].setEquation(currentScan);
				printStatus = true;

			} else if(currentScan.equals("O")) {
				System.out.println("Give equation:");
				currentScan = scan.nextLine();
				graphers[2].setEquation(currentScan);
				printStatus = true;

			} else if(currentScan.equals("U")) {
				System.out.println("Give equation:");
				currentScan = scan.nextLine();
				graphers[3].setEquation(currentScan);
				printStatus = true;

			} else if(currentScan.equals("D")) {
				System.out.println("Give equation:");
				currentScan = scan.nextLine();
				graphers[4].setEquation(currentScan);
				printStatus = true;

			} else if(!currentScan.equals("quit")){
				System.out.println("Invalid variable name");
			}
		} for(int i=0;i<42;i++) System.out.println("");
	}
 	public static void printManual() {
		System.out.println("\nINSTRUCTIONS FOR USE:\n"
				 + "Type an equation to solve it, though it may not\n"
				 + "hold anyvariables. Equations must be formatted\n"
				 + "properly to work, or a syntax error will be\n"
				 + "thrown and the program must be restarted.\n"
				 + "\n"
				 + "EQUATION RULES\n"
				 + "-Common operators are +,-,*,/, and ^\n"
				 + "-All terms must be split by an operator\n"
				 + "Correct:\n"
				 + "- 15+24, 10*2, 15-20^3/5\n"
				 + "Incorrect\n"
				 + "- 15+, *25, 10^10^10^, etc.\n"
				 + "Exceptions\n"
				 + "- 5(10), (10)5\n"
				 + "\n"
				 + "-Operations go in the following order:\n"
				 + "-^'s, from left to right\n"
				 + "-* or /'s from left to right\n"
				 + "-+ or -'s from left to right\n"
				 + "\n"
				 + "-Negatives are the \"-\" sign, and are\n"
				 + " considered part of their term\n"
				 + "\n"
				 + "-Functions like sin or cos are written out as\n"
				 + " such with parenthesis after their name. These\n"
				 + " functions are evaluated before other operators\n"
				 + " from left to right, regardless of function type\n"
				 + "-Absolute value is a function called \"abs\"\n"
				 + "-Square root is a function called \"sqr\"\n"
				 + "-Trig function inverses are abreviated normally \n"
				 + " as csc, sec, and cot\n"
				 + "\n"
				 + "-You may swap calculator modes by typing the\n"
				 + " mode's name, which are as follows:\n"
				 + "-y=, to write graphing equations\n"
				 + "-window, to specify viewing parameters\n"
				 + "-graph, to view graphed equations\n"
				 + "-quit, to return to normal calculating\n"
				 + "-close, to end the program");
	}
	
}

