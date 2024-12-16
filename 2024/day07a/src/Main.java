import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reads equations from a file, evaluates which can be solved using addition and multiplication,
 * and calculates the total of all valid left-hand side values.
 */
public class Main {

    public static final String INPUT_FILE_LOCATION = "../input.txt";

    public static final String EQUATION_PATTERN_REGEX = "([0-9]+):([ 0-9]+)";

    /**
     * Builds an abstract syntax tree (AST) for a given list of values.
     * 
     * @param rhsValues List of right-hand side values to include in the AST.
     * @return The constructed AbstractSyntaxTree.
     */
    public static AbstractSyntaxTree buildAST(List<Long> rhsValues) {
        AbstractSyntaxTree ast = new AbstractSyntaxTree(rhsValues.get(0));

        for (int valuePosition = 1; valuePosition < rhsValues.size(); ++valuePosition) {
            long value = rhsValues.get(valuePosition);
            for (Node leaf : ast.getAllLeafs()) {
                leaf.setNodeToAdd(new Node(value));
                leaf.setNodeToMultiply(new Node(value));
            }
        }
        return ast;
    }

    /**
     * Checks if an equation can be solved by applying addition and multiplication between right-hand side values.
     * (Checks all the possible permutation of allowed operators).
     * 
     * @param lhsValue Left-hand side value of the equation.
     * @param rhsValues Right-hand side values of the equation.
     * @return true if the equation can be solved, false otherwise.
     * @throws AssertionError if rhsValues is empty.
     */    
    public static boolean canEquationBeSolved(long lhsValue, List<Long> rhsValues) throws AssertionError {

        assert rhsValues.size() > 0;

        AbstractSyntaxTree ast = buildAST(rhsValues);
        Node root = ast.getRoot();
        List<Long> resultsList = ast.getAllPossibleEquationsResults(0, root);

        if (resultsList.contains(lhsValue))
            return true;

        return false;
    }

    public static void main(String args[]) {
        long result = 0;

        File file = new File(INPUT_FILE_LOCATION);

        try {     
            BufferedReader br = new BufferedReader(new FileReader(file));
            
            String inputLine;

            Pattern pattern = Pattern.compile(EQUATION_PATTERN_REGEX);

            List<Long> rhsValues;
            long lhsValue;
            while ((inputLine = br.readLine()) != null) {

                rhsValues = new ArrayList<>();

                Matcher matcher = pattern.matcher(inputLine);

                if (matcher.find()) {

                    lhsValue = Long.parseLong(matcher.group(1));

                    String[] values = matcher.group(2).strip().split(" ");
                    
                    for (int i = 0; i < values.length; ++i)
                        rhsValues.add(Long.parseLong(values[i]));

                    if (canEquationBeSolved(lhsValue, rhsValues)) {
                        result += lhsValue;
                    }
                }
            }
            br.close();

            System.out.println(String.format("The sum of the lhs of the total possible equations is: %s", result));

        } catch (FileNotFoundException e) {
            System.err.println(String.format("Error: could not find the input in the specified location: %s", e.toString()));
            return;
        } catch (IOException e) {
            System.err.println(String.format("Error: IOException while reading file: %s", e.toString()));
            return;
        } catch (AssertionError e) {
            System.err.println(String.format("Error: right hand side values must be at least two: %s", e.toString()));
            return;
        }
    }
}