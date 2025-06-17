package graphingCalc;

import java.util.ArrayList;
import java.util.Arrays;

public class Graph {
	private double xMax;
	private double xMin;
	private double yMax;
	private double yMin;
	private double xStep;
	private double yStep;
	private String equation;
	private String graph;
	private String symbol;
	
	//constructor; defaults graph domain/range
	public Graph(double xStep, double yStep, String equation, String symbol) {
		this.xStep = xStep;
		this.yStep = yStep;
		this.xMax = 10;
		this.xMin = -10;
		this.yMax= 10;
		this.yMin = -10;
		this.equation = equation;
		this.symbol = symbol;
	}

	public Double getXMax() {
		return xMax;
	}
	public void setXMax(double xMax) {
		this.xMax = xMax;
	}
	public double getXMin() {
		return xMin;
	}
	public void setXMin(double xMin) {
		this.xMin = xMin;
	}
	public double getYMax() {
		return yMax;
	}
	public void setYMax(double yMax) {
		this.yMax = yMax;
	}
	public double getYMin() {
		return yMin;
	}
	public void setYMin(double yMin) {
		this.yMin = yMin;
	}
	public double getXStep() {
		return xStep;
	}
	public void setXStep(double xStep) {
		this.xStep = xStep;
	}
	public double getYStep() {
		return yStep;
	}
	public void setYStep(double yStep) {
		this.yStep = yStep;
	}
	public String getEquation() {
		return equation;
	}
	public void setEquation(String equation) {
		this.equation = equation;
	}
 	public String getGraph() {
		return graph;
	}
	public void setGraph(String graph) {
		this.graph = graph;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String toString() {
		return "xMax: "+xMax+"\n"
			 + "yMax: "+yMax+"\n"
			 + "xMin: "+xMin+"\n"
			 + "yMin: "+yMin+"\n"
			 + "xStep: "+xStep+"\n"
			 + "yStep: "+yStep+"\n"
			 + "equation: "+equation+"\n"
			 + "symbol: "+symbol;
	}
	
	//General graphing
	public void makeGraph() {
		//List of graph slices
		ArrayList<String> transform = new ArrayList<String>();
		
		//Round all bounds variables to multiples of the graph's step
		yMax = roundTo(yMax, yStep);
		yMin = roundTo(yMin, yStep);
		xMax = roundTo(xMax, xStep);
		xMin = roundTo(xMin, xStep);
		
		//Empty object of old stored graph
		graph = "";
		
		
		//Loop through each slice of the y axis
		for(double x=xMin; x<=xMax; x+=xStep) {
			//Round this x value to a multiple of xStep (avoids .00000001 off errors)
			x = roundTo(x, xStep);
			
			//String used to hold one slice of the y axis
			String slice = "";
			
			//Substitute all x's in equation with the x value for this slice of the y axis
			String subbedEquation = equation.replaceAll("x", "("+x+")");
			
			//Solve equation with substituted x's
			double equationSolution = Double.valueOf(Solver.interpretAndSolve(subbedEquation, false, false));
			
			//Y value of equation on this y slice (x value)
			double mark;
			
			//Make sure value is a real number, allowing graphs with asymptotes
			if(!(equationSolution+"").equals("NaN")) {
				mark = roundTo((equationSolution), yStep); 
			} else {
				mark = Double.MIN_VALUE;
			}
			//end local variable declaration
			
			//Loop through each x slice of this y slice
			for(double y=yMin; y<=yMax; y+=yStep) {
				//Round this y value to a multiple of yStep (avoids .00000001 off errors)
				y = roundTo(y, yStep);
				
				//when y == the mark, add symbol; y == axis line, add axis line; y == nothing, add period
				if(mark==y) {
					slice += symbol;
				} else if(y==0) {
					slice += "_";
				} else if(x==0) { 
					slice += "|";
				} else {
					slice += ".";
				}
				
			} //End inner loop
			
			//Add this completed slice of the y axis to an ArrayList to be transformed later
			transform.add(slice);
			
		} //End outer loop
		
		
		//Slices of y axis are combined to create slices of x axis, which are added to the graph String
		for(int i=transform.get(0).length()-1; i>=0; i--) {
			String slice = "";
			for(int k=0; k<transform.size(); k++) {
				slice += transform.get(k).substring(i,i+1)+" ";
			}
			graph += slice+"\n";
		}
		
	} //End method
	private void makeEmptyGraph() {
		ArrayList<String> transform = new ArrayList<String>();
		yMax = roundTo(yMax, yStep);
		yMin = roundTo(yMin, yStep);
		xMax = roundTo(xMax, xStep);
		xMin = roundTo(xMin, xStep);
		graph = "";
		
		for(double x=xMin; x<=xMax; x+=xStep) {
			x = roundTo(x, xStep);
			String slice = "";
			for(double y=yMin; y<=yMax; y+=yStep) {
				y = roundTo(y, yStep);
				if(y==0) {
					slice += "_";
				} else if(x==0) { 
					slice += "|";
				} else {
					slice += ".";
				}
			}
			transform.add(slice);
		}
		for(int i=transform.get(0).length()-1; i>=0; i--) {
			String slice = "";
			for(int k=0; k<transform.size(); k++) {
				slice += transform.get(k).substring(i,i+1)+" ";
			}
			graph += slice+"\n";
		}
	}
	public static String overlayGraphs(Graph[] graphsList) {
		ArrayList<Graph> graphs = new ArrayList<Graph>(Arrays.asList(graphsList));
		for(int i=graphs.size()-1; i>=0; i--) {
			try {
				graphs.get(i).makeGraph();
			} catch(Exception e) {
				graphs.remove(i);
			}
		}
		
		if(graphs.isEmpty()) {
			graphsList[0].makeEmptyGraph();
			String temp = graphsList[0].graph;
			graphsList[0].graph = "";
			return temp;
		}
		
		String baseGraph = graphs.get(0).getGraph();
		for(int i=1; i<graphs.size(); i++) {
			String overlay = graphs.get(i).getGraph();
			for(int k=0; k<baseGraph.length(); k++) {
				String curOver = overlay.substring(k,k+1);
				if(curOver.equals(graphs.get(i).getSymbol())) {
					baseGraph = replaceAt(baseGraph, graphs.get(i).getSymbol(), k);
				}
			}
		}
		
		return baseGraph;
	}
	public void makeAdvancedGraph(double tolerance) {
		//List of graph slices
		ArrayList<String> transform = new ArrayList<String>();
		
		//Round all bounds variables to multiples of the graph's step
		yMax = roundTo(yMax, yStep);
		yMin = roundTo(yMin, yStep);
		xMax = roundTo(xMax, xStep);
		xMin = roundTo(xMin, xStep);
				
		//Empty object of old stored graph
		graph = "";
				
		//Loop through each slice of the y axis
		for(double x=xMin; x<=xMax; x+=xStep) {
			//Round this x value to a multiple of xStep (avoids .00000001 off errors)
			x = roundTo(x, xStep);
			
			//String used to hold one slice of the y axis
			String slice = "";
			
			//Loop through each x slice of this y slice
			for(double y=yMin; y<=yMax; y+=yStep) {
				//Round this y value to a multiple of yStep (avoids .00000001 off errors)
				y = roundTo(y, yStep);
				
				//take both sides
				String left = equation.substring(0, equation.indexOf("="));
				String right = equation.substring(equation.indexOf("=")+1);
				
				//substitute
				left = left.replaceAll("x", "("+x+")");
				left = left.replaceAll("y", "("+y+")");
				right = right.replaceAll("x", "("+x+")");
				right = right.replaceAll("y", "("+y+")");
				
				//evaluate
				double leftMark = Double.valueOf(Solver.interpretAndSolve(left, false, false));
				double rightMark = Double.valueOf(Solver.interpretAndSolve(right, true, false));
				
				//when mark==mark, add symbol; y == axis line, add axis line; y == nothing, add period
				if(getDifference(leftMark, rightMark)<tolerance) {
					slice += symbol;
				} else if(y==0) {
					slice += "_";
				} else if(x==0) { 
					slice += "|";
				} else {
					slice += ".";
				}
			} //End inner loop
		
			//Add this completed slice of the y axis to an ArrayList to be transformed later
			transform.add(slice);
			
		} //End outer loop
		
		//Slices of y axis are combined to create slices of x axis, which are added to the graph String
		for(int i=transform.get(0).length()-1; i>=0; i--) {
			String slice = "";
			for(int k=0; k<transform.size(); k++) {
				slice += transform.get(k).substring(i,i+1)+" ";
			}
			graph += slice+"\n";
		}
	}
	
	//Utility methods
	private static String replaceAt(String str, String insertion, int index) {
		String left = str.substring(0, index);
		String right = str.substring(index+1);
		return left+insertion+right;
	}
	private static double roundTo(double in, double to) {
		return Math.round(in/to)*to;
	}
	private static double getDifference(double x, double y) {
		return Math.abs(x-y);
	}
	
	//Utility for outside classes
	public boolean hasGraph() {
		return graph.isEmpty();
	}
	
	//DEPRECATED
	public void graphWithTolerance(int tolerance) {
			ArrayList<String> transform = new ArrayList<String>();
			yMax = roundTo(yMax, yStep);
			yMin = roundTo(yMin, yStep);
			xMax = roundTo(xMax, xStep);
			xMin = roundTo(xMin, xStep);
			graph = "";
			for(double x=xMin; x<=xMax; x+=xStep) {
				x = roundTo(x, xStep);
				String slice = "";
				double smallestDif = Double.MAX_VALUE;
				double closestY = 0;
				
				String subbedEquation = equation.replaceAll("x", "("+x+")");
				double equationSolution = Double.valueOf(Solver.interpretAndSolve(subbedEquation, false, false));
				double mark = roundTo((equationSolution), xStep); 
				
				System.out.println("Val to match: "+mark);
				
				for(double y=yMin; y<=yMax; y+=yStep) {
					y = roundTo(y, yStep);
					if(Math.abs(y-mark)<smallestDif) {
						smallestDif = Math.abs(y-mark);
						closestY = y;
					}
					if(y==0) {
						slice += "_";
					} else if(x==0) { 
						slice += "|";
					} else {
						slice += ".";
					}
				}
				
				System.out.println("Lowest dif was: "+smallestDif+" with y="+closestY);
				int indexOfClosest = (int)(roundTo(closestY/yStep, 1)+(yMin/yStep * -1));
				System.out.println("Index of closestY: "+indexOfClosest);
				if(smallestDif<=tolerance) {
					slice = replaceAt(slice, symbol, indexOfClosest);
				}
				
				System.out.println();
				transform.add(slice);
			}
			
			for(int i=transform.get(0).length()-1; i>=0; i--) {
				String slice = "";
				for(int k=0; k<transform.size(); k++) {
					slice += transform.get(k).substring(i,i+1)+" ";
				}
				graph += slice+"\n";
			}
			
			
		}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
