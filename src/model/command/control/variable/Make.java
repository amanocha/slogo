/**
 * This is the class for the MAKE/SET command
 * 
 * @author Aninda Manocha
 */

package model.command.control.variable;

import Controller.Data;
import Parsing.expression.ConstantExpression;
import Parsing.expression.ExpressionTree;
import Parsing.expression.VariableExpression;
import model.animal.Animal;
import model.command.Command;
import model.command.Parameter;
import model.variable.Variable;

public class Make extends Command {
	private final double paramCount;
	
	public Make() {
		super();
		numParams = 3;
		paramCount = 2;
	}
	
	/**
	 * Assigns a value to a variable or creates the variable if it doesn't already exist
	 * @param params - array of parameters
	 * @return the value of the variable
	 */
	@Override
	public double run(Parameter[] params) {
		Animal turtle = params[0].getAnimal();
		String variableName = params[1].getNode().toString();
		double expression = ExpressionTree.getInstance().process(turtle, params[2].getNode());
		Data data = Data.getInstance();
		if (data.containsVariable(variableName)) {
			if (data.getVariable(variableName).getValue() != expression) {
				data.changeVariable(variableName, expression);
			}
		} else {
			data.addVariable(new Variable(variableName, expression, false));
		}
		return expression;
	}
}