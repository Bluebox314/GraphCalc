package graphingCalc;

public class Solver {
	
	//Main class method, using all below methods to interpret and solve String equation.
	public static String interpretAndSolve(String in, boolean doDebug, boolean recursiveCall) {
		if(in.isEmpty()) return "";
		if(!recursiveCall) {
			System.out.println("==========EQUATION START==========");
			in = in.replaceAll(" ", "");
		}
		
		int chunks = countEquationChunks(in);
		for(int i=0; i<chunks && chunks>1; i++) {
			if(doDebug)	System.out.println("CHUNK "+(i+1));
			if(doDebug)	System.out.println("Chunking: "+in);
			String chunk = getFirstEquationChunk(in);
			if(doDebug) System.out.println("Solving Chunk: "+chunk);
			in = replaceFirst(in, "("+chunk+")", interpretAndSolve(chunk, doDebug, true));
			if(contains(in, "ERROR")) return "ERROR";
		}
		
		if(numOfCharacter(in, "(")>0) {
			if(doDebug) System.out.println("Recursion Trigger: "+in);
			String insideParenthesis = in.substring(in.indexOf("(")+1, in.lastIndexOf(")"));
			if(doDebug) System.out.println("Interior To Solve: "+insideParenthesis);
			String parenthesisSolution = interpretAndSolve(insideParenthesis, doDebug, true);
			if(contains(parenthesisSolution, "ERROR")) return "ERROR";
			in = replaceFirst(in, "("+insideParenthesis+")", parenthesisSolution);
			if(doDebug) System.out.println("Recursion Ended As: "+in);
		}
		
		if(doDebug) System.out.println("Eval on: "+in);
		
		double evaluation;
		
		try {
			evaluation = evaluateString(in, doDebug);
		} catch(Exception e) {
			System.out.println("ERROR ON: "+in);
			if(!contains(in, "ERROR")) 
				return in+"ERROR";
			else
				return in;
		}
		
		if(doDebug) System.out.println("Returning: "+evaluation+"\n");
		
		return evaluation+"";
	}

	//Seq (short equation) and eval code
	public static double evaluateString(String in, boolean doDebug) {
		while(unfinishedEval(in)) {
			while(in.indexOf("E")>-1) {
				in = removeScientificNotation(in);
				if(doDebug) System.out.println("Removed scientific: "+in);
			}
			
			if(hasFunctions(in)) { //functions (i.e. sin, cos, abs, etc.)
				String seq = getShortFunctionEquation(in);
				if(doDebug) System.out.println("Short equation: "+seq);
				String function = seq.substring(0, 3);
				double functionInterior = Double.valueOf(seq.substring(3));
				if(function.equals("abs")) {
					in = replaceFirst(in, seq, Math.abs(functionInterior)+"");
				} else if(function.equals("sin")) {
					functionInterior = functionInterior*0.017453292519943295;
					in = replaceFirst(in, seq, Math.sin(functionInterior)+"");
				} else if(function.equals("cos")) {
					functionInterior = functionInterior*0.017453292519943295;
					in = replaceFirst(in, seq, Math.cos(functionInterior)+"");
				} else if(function.equals("tan")) {
					functionInterior = functionInterior*0.017453292519943295;
					in = replaceFirst(in, seq, Math.tan(functionInterior)+"");
				} else if(function.equals("csc")) {
					in = replaceFirst(in, seq, Math.asin(functionInterior)+"");
				} else if(function.equals("sec")) {
					in = replaceFirst(in, seq, Math.acos(functionInterior)+"");
				} else if(function.equals("cot")) {
					in = replaceFirst(in, seq, Math.atan(functionInterior)+"");
				} else if(function.equals("log")) {
					in = replaceFirst(in, seq, Math.log(functionInterior)+"");
				} else if(function.equals("sqr")) {
					in = replaceFirst(in, seq, Math.sqrt(functionInterior)+"");
				}
				
				
			} else if(contains(in, "^")) { //Exponents
				if(doDebug) System.out.println("Short equation: "+getShortEquation(in, "^"));
				in = doSeqEval(in, getShortEquation(in, "^"), "^");
				
			} else if(contains(in, "*") || contains(in, "/")) { //Mult/Division
				
				if(contains(in, "*") && (!contains(in, "/") || in.indexOf("*")<in.indexOf("/"))) {
					if(doDebug) System.out.println("Short equation: "+getShortEquation(in, "*"));
					in = doSeqEval(in, getShortEquation(in, "*"), "*");
				} else {
					if(doDebug) System.out.println("Short equation: "+getShortEquation(in, "/"));
					in = doSeqEval(in, getShortEquation(in, "/"), "/");
				}
				
			} else if(contains(in, "+") || contains(in, "--")) { //Addition
				in = removeSubtractionNegativeSigns(in);
				if(doDebug) System.out.println("Reformated to: "+in);
				
				if(doDebug) System.out.println("Short equation: "+getShortEquation(in, "+"));
				in = doSeqEval(in, getShortEquation(in, "+"), "+");
				
			}
			
			if(doDebug) System.out.println("New equation: "+in);
			if(in.indexOf("NaN")>-1 || in.indexOf("Infinity")>-1) return Math.log(-1);
		}
		return Double.valueOf(in);
	}
	public static String doSeqEval(String in, String seq, String type) {
		double left = Double.valueOf(seq.substring(0, seq.indexOf(type)));
		double right = Double.valueOf(seq.substring(seq.indexOf(type)+1, seq.length()));
		if(type.equals("+")) {
			return replaceFirst(in, seq, (left+right)+"");
		} else if(type.equals("/")) {
			return replaceFirst(in, seq, (left/right)+"");
		} else if(type.equals("*")) {
			return replaceFirst(in, seq, (left*right)+"");
		} else if(type.equals("^")) {
			return replaceFirst(in, seq, Math.pow(left, right)+"");
		}
		return "ERROR IN SEQ EVAL";
	}
	public static String getShortFunctionEquation(String in) {
		int functionIndex = findFirstFunction(in);
		String str = in.substring(functionIndex, functionIndex+3);
		for(int i=functionIndex+3; i<in.length(); i++) {
			if(i==in.length()-1 || !isNumber(in.substring(i+1,i+2))) {
				str += in.substring(functionIndex+3, i+1);
				break;
			}
		}
		return str;
	}
	public static String getShortEquation(String in, String type) {
		int center = in.indexOf(type);
		String str = ""+type;
		for(int i=center+1; i<in.length(); i++) {
			if(i==in.length()-1 || !isNumber(in.substring(i+1,i+2))) {
				str += in.substring(center+1, i+1);
				break;
			}
		}
		for(int i=center-1; i>=0; i--) {
			if(i==0 || !isNumber(in.substring(i-1, i))) {
				if(i!=0 && in.substring(i-1, i).equals("-")) {
					str = in.substring(i-1, center)+str;
					break;
				} else {
					str = in.substring(i, center)+str;
					break;
				}
			}
		}
		return str;
	}
	
	public static String removeScientificNotation(String in) {
		String seq = getShortEquation(in, "E");
		int eIndex = seq.indexOf("E");
		String left = seq.substring(0, eIndex);
		String right = seq.substring(eIndex+1);
		boolean negative = false;
		
		if(contains(left, ".")) 
			left = removeChar(left, left.indexOf("."));
		if(contains(left, "-")) {
			left = removeChar(left, left.indexOf("-"));
			negative = true;
		}
			
		if(contains(right, "-")) {
			int zeros = Math.abs(Integer.valueOf(right))-1;
			String zeroString = "";
			for(int i=0; i<zeros; i++) zeroString += "0";
			if(negative)
				return "-."+zeroString+left;
			else
				return "."+zeroString+left;
		} else {
			int lastSpot = 0;
			for(int i=0; i<Math.abs(Integer.valueOf(right))+1; i++) {
				if(i>left.length()) left += "0";
				lastSpot = i;
			}
			if(negative)
				return "-"+insertAt(left, ".", lastSpot+1);
			else
				return insertAt(left, ".", lastSpot+1);
		}
	}
	public static int findFirstFunction(String in) {
		int index = 0;
		for(int i=0; i<in.length()-3; i++) {
			if(isFunction(in.substring(i,i+3))) {
				return i;
			}
		}
		return index;
	}
	public static int countEquationChunks(String in) {
		int count = 0;
		int nested = 0;
		boolean finding = true;
		for(int i=0; i<in.length(); i++) {
			String s = in.substring(i,i+1);
			if(finding) {
				if(s.equals("(")) {
					finding = false;
				}
			} else {
				if(s.equals("(")) {
					nested++;
				} else if(s.equals(")")) {
					nested--;
				}
			}
			
			if(nested==-1) {
				count++;
				finding = true;
				nested = 0;
			}
		}
		return count;
	}
	public static String getFirstEquationChunk(String in) {
		int nested = 0;
		int start = -2;
		int end = -1;
		for(int i=0; i<in.length(); i++) {
			String s =in.substring(i,i+1);
			if(start==-2 && s.equals("(")) {
				start = i;
			} else {
				if(s.equals("(")) {
					nested++;
				} else if(s.equals(")")) {
					nested--;
				}
			}
			if(nested==-1) {
				end = i;
				break;
			}
		}
		return in.substring(start+1, end);
	}
	public static String removeSubtractionNegativeSigns(String in) {
		in = in.replaceAll("--", "+");
		
		for(int i=1; i<in.length(); i++) {
			String last = in.substring(i-1, i);
			String current = in.substring(i, i+1);
			if(current.equals("-") && isNumber(last)) {
				in = removeChar(in, i);
				in = insertAt(in, "+-", i);
				i+=2;
			}
		}
		
		return in;
	}
	
	//Utility boolean methods used on String equations
	public static boolean isNegativeSign(String in) {
		int negativeSignIndex = in.indexOf("-");
		if(negativeSignIndex==0) return true;
		if(!isNumber(in.substring(negativeSignIndex-1,negativeSignIndex))) return true;
		return false;
	}
	public static boolean unfinishedEval(String in) {
		return contains(in, "*") || contains(in, "/") || contains(in, "+") || contains(in, "--") || contains(in, "^") || hasFunctions(in);
	}
	public static boolean isNumber(String in) {
		return in.equals("0") || in.equals("1") || in.equals("2") || in.equals("3") || in.equals("4") || in.equals("5") || in.equals("6") || in.equals("7") || in.equals("8") || in.equals("9") || in.equals(".");
	}
	public static boolean hasFunctions(String in) {
		return contains(in,"sin") || contains(in,"cos") || contains(in,"tan") || contains(in,"abs") || contains(in,"csc") || contains(in,"sec") || contains(in,"cot") || contains(in, "log") || contains(in, "sqr");
	}
	public static boolean isFunction(String in) {
		return in.equals("sin") || in.equals("cos") || in.equals("tan") || in.equals("abs") || in.equals("csc") || in.equals("sec") || in.equals("cot")
				|| in.equals("log") || in.equals("sqr");
	}
	
	//String utility methods used in above code
	private static String replaceFirst(String str, String remove, String replacement) {
		for(int i=0; i<str.length()-remove.length()+1; i++) {
			String s = str.substring(i,i+remove.length());
			if(s.equals(remove)) {
				str = replace(str, remove, replacement);
				return str;
			}
		}
		
		return str;
	}
	private static String replace(String str, String r, String replacement) {
		String start = str.substring(0,str.indexOf(r));
		String end = "";
		if(str.indexOf(r)+r.length()<str.length()+1) {
			end = str.substring(str.indexOf(r)+r.length());
		}
		
		return start+replacement+end;
	}
	private static boolean contains(String str, String thing) {
		return str.indexOf(thing)>-1;
	}
	private static String insertAt(String str, String insertion, int index) {
		String left = str.substring(0, index);
		String right = str.substring(index);
		return left+insertion+right;
	}
	private static int numOfCharacter(String str, String substring) {
		int digitCount = 0;
		for(int i=0; i<str.length(); i++) {
			if(str.substring(i,i+1).equals(substring)) {
				digitCount++;
			}
		}
		return digitCount;
	}
	private static String removeChar(String str, int index) {
		String left = str.substring(0, index);
		String right = str.substring(index+1);
		return left+right;
	}
}











