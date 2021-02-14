
import java.util.ArrayList;
import java.util.EmptyStackException;

import org.junit.platform.commons.util.StringUtils;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

// TODO: Auto-generated Javadoc
/**
 * The Class ExpressionEvaluator.
 */
public class ExpressionEvaluator extends Application {

	/** The data stack. */
	private GenericStack<Double> dataStack = new GenericStack<Double>();;

	/** The operator stack. */
	private GenericStack<String> operStack = new GenericStack<String>();;

	/** The oper list. */
	private ArrayList<Character> operList = new ArrayList<Character>() {
		{
			add('+');
			add('-');
			add('*');
			add('/');
			add('(');
			add(')');
		}
	};

	/** The main. */
	private BorderPane main = new BorderPane();

	/**
	 * Start.
	 *
	 * @param primaryStage the primary stage
	 * @throws Exception the exception
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("Expression Evaluator");
        Scene scene = new Scene(main, 500, 250);
        TextField userInputField = new TextField("Enter an expression.");
        
        Label answer = new Label();
        answer.setVisible(false);
        VBox parent = new VBox(40);
        
        Button calculateBtn = new Button("Evaluate");
        calculateBtn.setOnAction(e -> {
        	if (userInputField.getText().isEmpty()) {
        		Alert alert = new Alert(AlertType.WARNING);
				alert.setHeaderText("Evaluation Failed!");
				alert.setContentText("Please enter an expression");
				alert.showAndWait();
        	} else {
        		answer.setText(evaluateExpression(userInputField.getText()));
            	answer.setVisible(true);
        	}
        });

        parent.getChildren().addAll(userInputField, calculateBtn, answer);
        parent.setSpacing(10);
        main.setCenter(parent);

        primaryStage.setScene(scene);
        primaryStage.show();
	}

	/**
	 * Evaluates the expression by first massaging the string, and then splitting it
	 * into "tokens" that are either operations or data operands. 
	 *
	 * @param str this is the string that the user typed in the text field
	 * @return the string that is the result of the evaluation. It should include the original
	 *         expression and the value that it equals, or indicate if some error occurred.
	 */
	protected String evaluateExpression(String str) {
		
		for (int i = 0; i < str.length(); i++) {
			if (i < str.length() - 2 && str.charAt(i) <= '9' 
				&& str.charAt(i) >= '0' && str.charAt(i + 1) == ' ' && str.charAt(i + 2) <= '9' 
				&& str.charAt(i + 2) >= '0') {
				return "Data Error: " + str;
			}
			
			if (!operList.contains(str.charAt(i)) && !(str.charAt(i) <= '9' 
				&& str.charAt(i) >= '0') && !(str.charAt(i) == '.' || str.charAt(i) == ' ')) {
				
				return "Data Error: " + str;
			}
		}
		
		str = str.replaceAll("\\s", "");
		
		if (str.contains("---")) {
			return "Op Error: " + str;
		}
		
		int parenCount1 = str.length() - str.replace("(", "").length();
		int parenCount2 = str.length() - str.replace(")", "").length();
		
		if (!(parenCount1 == parenCount2) || (str.charAt(0) == ')') || (str.charAt(str.length() - 1) == '(')) {
			return "Paren Error: " + str;
		} else if (operList.contains(str.charAt(0)) && str.charAt(0) != '(' && str.charAt(0) != '-') {
			return "Op Error: " + str;
		} else if (str.charAt(0) == '-') {
			str = " " + str;
		} else if (operList.contains(str.charAt(str.length() - 1)) && str.charAt(str.length() - 1) != ')') {
			return "Op Error: " + str;
		}
		
		for (int i = 0; i < str.length() - 1; i++) {
			if ((!operList.contains(str.charAt(i)) && str.charAt(i + 1) == '(') 
				|| (str.charAt(i) == ')' && (str.charAt(i + 1) == '(' || !operList.contains(str.charAt(i + 1))))) {
				str = str.substring(0, i + 1) + "*" + str.substring(i + 1, str.length());
				i++;
			} else if (operList.contains(str.charAt(i)) && str.charAt(i + 1) == '-' && str.charAt(i) != ')') {
				str = str.substring(0, i + 1) + " " + str.substring(i + 1, str.length());
				i++;
			} else if ((operList.contains(str.charAt(i)) && operList.contains(str.charAt(i + 1))
					    && str.charAt(i) != ' ' && str.charAt(i) != ')' && str.charAt(i + 1) != '-' && str.charAt(i + 1) != '(')) {
				return "Op Error: " + str;
			}
		}
		
		for (int i = 0; i < str.length(); i++) {
			char token = str.charAt(i);
			if (operList.contains(token)) {
				if (token == '(') {
					if (i < str.length() - 1 && str.charAt(i + 1) == ')') {
						return "Op Error: " + str;
					}
					operStack.push(Character.toString(token));
				} else if (token == ')') {
					try {
						while (!operStack.peek().equals("(")) {
							dataStack.push(evaluatePart(dataStack.pop(), dataStack.pop(), operStack.pop()));
						}
						operStack.pop();
					} catch (ArithmeticException e) {
						return "Div0 Error: " + str;
					}
				} else {
					// could also use (operList.indexOf(token) <= operList.indexOf(operStack.peek().charAt(0))) instead of method
					try {
						while (operStack.getSize() > 0 && hasGreaterPriority(operStack.peek().charAt(0), token)) {
							dataStack.push(evaluatePart(dataStack.pop(), dataStack.pop(), operStack.pop()));
						}
						operStack.push(Character.toString(token));
					} catch (ArithmeticException e) {
						return "Div0 Error: " + str;
					}
				}
			} else {
				String num = "";
				if (token == ' ') {
					num = "-";
					i += 2;
				}
				
				while (i < str.length() && (Character.isDigit(str.charAt(i)) || str.charAt(i) == '.')) {
					num += Character.toString(str.charAt(i++));
				}
				dataStack.push(Double.parseDouble(num));
				i--;
			}
		}
		
		try {
			while (operStack.getSize() > 0) {
				dataStack.push(evaluatePart(dataStack.pop(), dataStack.pop(), operStack.pop()));
			}
		} catch (ArithmeticException e) {
			return "Div0 Error: " + str;
		}
		
		str = str.replaceAll("\\s", "");
		return str + "=" + dataStack.pop();
	}

	/**
	 * Checks for greater priority.
	 *
	 * @param operator1 the operator 1
	 * @param operator2 the operator 2
	 * @return true, if successful
	 */
	private boolean hasGreaterPriority(char operator1, char operator2) {
		return !((operator2 == '*' || operator2 == '/') 
				&& (operator1 == '+' || operator1 == '-') || (operator1 == '(' || operator1 == ')'));
	}

	/**
	 * Evaluate part.
	 *
	 * @param num1 the num 1
	 * @param num2 the num 2
	 * @param operator the operator
	 * @return the int
	 */
	private double evaluatePart(double num1, double num2, String operator) {
		switch(operator) {
		case "+":
			return num1 + num2;
		case "-":
			return num2 - num1;
		case "*":
			return num1 * num2;
		default:
			if (num1 == 0) {
				throw new ArithmeticException();
			} else {
				return num2 / num1;
			}
		}
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		Application.launch(args);

	}

}
