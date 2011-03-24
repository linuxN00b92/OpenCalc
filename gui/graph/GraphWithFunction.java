package gui.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

import tree.Decimal;
import tree.Number;
import tree.ExpressionParser;
import tree.Value;
import tree.Var;

public class GraphWithFunction extends SingleGraph{

/**
 * A Function stores all of the data necessary for graphing.
 * 
 * @author jason
 *
 */
	private String funcEqtn;
	private boolean takeIntegral;
	private boolean tracingPt;
	private boolean graphing;
	private boolean connected;
	private double point2Trace;
	private double startIntegral;
	private double endIntegral;
	private double derivative;
	private boolean deriving;
	private Color color;
	private Var independentVar;
	private Var dependentVar;
	private ExpressionParser parser;
	
	/**
	 * The default constructor, set the equation equal to an empty string,
	 * makes it not currently graphing, integral and tracing values are
	 * false.
	 */
	public GraphWithFunction(ExpressionParser ep, Graph g) {
		super(g);
		setParser(ep);
		funcEqtn = "";
		graphing = false;
		connected = true;
		setIsTakingIntegral(false);
		setTracingPt(false);
		point2Trace = 0;
		setStartIntegral(0);
		setEndIntegral(0);
		setColor(Color.blue);
		setIndependentVar("x");
		setDependentVar("y");
	}
	
	public GraphWithFunction(String s, ExpressionParser ep, Graph g, Color c) {
		super(g);
		setParser(ep);
		funcEqtn = s;
		graphing = false;
		connected = true;
		setIsTakingIntegral(false);
		setTracingPt(false);
		point2Trace = 0;
		setStartIntegral(0);
		setEndIntegral(0);
		setColor(c);
		setIndependentVar("x");
		setDependentVar("y");
	}
	
	/**
	 * Constructor that takes all attributes of a function.
	 * 
	 * @param exParser- associated 
	 * @param eqtn - string of equation
	 * @param ind - string of independent var
	 * @param dep - string of dependent var
	 * @param connected - boolean for points to be connected when graphed
	 * @param trace - boolean for tracing
	 * @param tracePt - double for value to trace
	 * @param integral - boolean for taking an integral
	 * @param startInt - double for start of integral
	 * @param endInt - double for end of integral
	 * @param derive - boolean for deriving
	 * @param dervative - the point to derive at
	 * @param c - a color to display the function with
	 */
	public GraphWithFunction(ExpressionParser exParser, Graph g, String eqtn, String ind, String dep, 
			boolean connected, boolean trace, double tracePt, boolean integral,
			double startInt, double endInt, boolean derive, double derivative, Color c) {
		super(g);
		setParser(exParser);
		setIndependentVar(ind);
		setDependentVar(dep);
		graphing = true;
		this.connected = connected;
		funcEqtn = eqtn;
		setTracingPt(trace);
		point2Trace = tracePt;
		setDeriving(true);
		setDerivative(derivative);
		setIsTakingIntegral(integral);
		setStartIntegral(startInt);
		setEndIntegral(endInt);
		setColor(c);
	}

	public void setFuncEqtn(String s) {
		funcEqtn = s;
	}

	public String getFuncEqtn() {
		return funcEqtn;
	}

	/**
	 * Sets the values for integral. If both params are zero then set to false.
	 * Else, values are set and integral is set to true.
	 * @param a
	 * @param b
	 */
	public void setIntegral(double a, double b) {
		if (a == 0 && b == 0) {
			setIsTakingIntegral(false);
		} else {
			setStartIntegral(a);
			setEndIntegral(b);
			setIsTakingIntegral(true);
		}
	}

	/**
	 * Sets the value to trace. If Double.MAX_VALUE passed, makes false.
	 * Else, double is stored and tracing is true.
	 * @param val - the value to be traced.
	 */
	public void setTrace(double val) {
		if (val == Double.MAX_VALUE) {
			setTracingPt(false);
		} else {
			setTracingPt(true);
			point2Trace = val;
		}
	}

	public double getTraceVal() {
		return point2Trace;
	}

	public void setIsTakingIntegral(boolean takeIntegral) {
		this.takeIntegral = takeIntegral;
	}

	public boolean isTakingIntegral() {
		return takeIntegral;
	}

	public void setTracingPt(boolean tracingPt) {
		this.tracingPt = tracingPt;
	}

	public boolean isTracingPt() {
		return tracingPt;
	}

	public void setGraphing(boolean graphing) {
		this.graphing = graphing;
	}

	public boolean isGraphing() {
		return graphing;
	}

	public void setStartIntegral(double startIntegral) {
		this.startIntegral = startIntegral;
	}

	public double getStartIntegral() {
		return startIntegral;
	}

	public void setEndIntegral(double endIntegral) {
		this.endIntegral = endIntegral;
	}

	public double getEndIntegral() {
		return endIntegral;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public void setIndependentVar(Var independentVar) {
		this.independentVar = independentVar;
	}
	
	public void setIndependentVar(String varName) {
		independentVar = getParser().getVarList().storeVar(varName);
	}
	
	public void setDependentVar(String varName) {
		dependentVar = getParser().getVarList().storeVar(varName);
	}


	public Var getIndependentVar() {
		return independentVar;
	}

	public void setDependentVar(Var dependentVar) {
		this.dependentVar = dependentVar;
	}

	public Var getDependentVar() {
		return dependentVar;
	}

	public void setConneted(boolean conneted) {
		this.connected = conneted;
	}

	public boolean isConnected() {
		return connected;
	}
	
	public void setDerivative(double derivative) {
		this.derivative = derivative;
	}

	public double getDerivative() {
		return derivative;
	}

	public void setDeriving(boolean deriving) {
		this.deriving = deriving;
	}

	public boolean isDeriving() {
		return deriving;
	}

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		
	}

	public void setParser(ExpressionParser parser) {
		this.parser = parser;
	}

	public ExpressionParser getParser() {
		return parser;
	}
}