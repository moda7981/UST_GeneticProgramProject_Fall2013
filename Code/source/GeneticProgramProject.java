/*
 * SEIS 610 Software Engineering Project
 * This class uses the JGAP API to create a genetic program that
 * finds a solution for the target function (x*x-1)/2
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 *
 * Author: Pradip Modak
 */

import java.util.*;
import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.function.*;
import org.jgap.gp.impl.*;
import org.jgap.gp.terminal.*;
import java.io.*;

public class GeneticProgramProject
    extends GPProblem  {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.25 $";

  public static Variable vx;

  public static int trainingData=20;
  private static long runTime=0;


  protected static Float[] x = new Float[trainingData];
  protected static float[] y = new float[trainingData];

  public void setTD(int a)
  {
  	trainingData = a;
   	Float[] x = new Float[trainingData];
  	float[] y = new float[trainingData];
  }

  public GeneticProgramProject(GPConfiguration a_conf)
      throws InvalidConfigurationException {
    super(a_conf);
  }

  //public CommandGene applyMutation (int index, double a_percentage)
  	//throws InvalidConfigurationException {
	//		Multiply mutant = new Multiply (getGPConfiguration(), getReturnType());
	//		return mutant;
  //}

  /**
   * This method is used for setting up the commands and terminals that can be
   * used to solve the problem.
   * In this example an ADF (an automatically defined function) is used for
   * demonstration purpuses. Using an ADF is optional. If you want to use one,
   * care about the places marked with "ADF-relevant:" below. If you do not want
   * to use an ADF, please remove the below places (and reduce the outer size of
   * the arrays "types", "argTypes" and "nodeSets" to one).
   * Please notice, that the variables types, argTypes and nodeSets correspond
   * to each other: they have the same number of elements and the element at
   * the i'th index of each variable corresponds to the i'th index of the other
   * variables!
   *
   * @return GPGenotype
   * @throws InvalidConfigurationException
   */
  public GPGenotype create()
      throws InvalidConfigurationException {
    GPConfiguration conf = getGPConfiguration();
    // At first, we define the return type of the GP program.
    // ------------------------------------------------------
    Class[] types = {
        // Return type of result-producing chromosome
        CommandGene.FloatClass,
        // ADF-relevant:
        // Return type of ADF 1
        CommandGene.FloatClass};
    // Then, we define the arguments of the GP parts. Normally, only for ADF's
    // there is a specification here, otherwise it is empty as in first case.
    // -----------------------------------------------------------------------
    Class[][] argTypes = {
        // Arguments of result-producing chromosome: none
        {},
        // ADF-relevant:
        // Arguments of ADF1: all 3 are float
        {CommandGene.FloatClass, CommandGene.FloatClass, CommandGene.FloatClass}
    };
    // Next, we define the set of available GP commands and terminals to use.
    // Please see package org.jgap.gp.function and org.jgap.gp.terminal
    // You can easily add commands and terminals of your own.
    // ----------------------------------------------------------------------
    CommandGene[][] nodeSets = { {
        // We use a variable that can be set in the fitness function.
        // ----------------------------------------------------------
        vx = Variable.create(conf, "X", CommandGene.FloatClass),
        new Multiply(conf, CommandGene.FloatClass),
        new Add(conf, CommandGene.FloatClass),
        //new Multiply3(conf, CommandGene.FloatClass),
        new Divide(conf, CommandGene.FloatClass),
        //new Sine(conf, CommandGene.FloatClass),
        //new Exp(conf, CommandGene.FloatClass),
        new Subtract(conf, CommandGene.FloatClass),
        //new Pow(conf, CommandGene.FloatClass),
        new Terminal(conf, CommandGene.FloatClass, 1.0d, 10.0d, true),
        // ADF-relevant:
        // Construct a reference to the ADF defined in the second nodeset
        // which has index 1 (second parameter of ADF-constructor).
        // Furthermore, the ADF expects three input parameters (see argTypes[1])
        new ADF(conf, 1 , 3),
    },
        // ADF-relevant:
        // and now the definition of ADF(1)
        {
        new Add3(conf, CommandGene.FloatClass),
        //new Add(conf, CommandGene.FloatClass),  //gdw...already implented above
    }
    };
    // Here, we define the expected (optimal) output we want to achieve by the
    // function/formula to evolve by the GP.
    // -----------------------------------------------------------------------
    //Random random = new Random();
    // Randomly initialize function data (X-Y table) for x^4+x^3+x^2-x
    // ---------------------------------------------------------------
    /*  for (int i = 0; i < 20; i++) {
      float f = 8.0f * (random.nextFloat() - 0.3f);
      x[i] = new Float(f);
      y[i] = (f * f - 1)/2;
      System.out.println(i + ") " + x[i] + "   " + y[i]);
    }*/

    //trainingData=2;  //TESTTESTTEST

    //System.out.println("Default training data values = " + trainingData);
    //System.out.println("You have chosen to enter trainingData manually.");
	//System.out.println("How many pieces of training data do you have?");
	//Scanner clientInput = new Scanner(System.in);
	//trainingData = clientInput.nextInt();

	/*System.out.println("Before accepting input data.  Value of training data is: " + trainingData);

    for (int i = 0; i < trainingData; i++)
    {

		System.out.println("Please enter the x value for training data # " + i);

		x[i] = clientInput.nextFloat();

		System.out.println("Please enter the y value for training data # " + i);
		y[i] = clientInput.nextFloat();

		System.out.println(i + ") " + x[i] + "   " + y[i]);
	}
	*/

    // Create genotype with initial population. Here, we use the declarations
    // made above:
    // Use one result-producing chromosome (index 0) with return type float
    // (see types[0]), no argument (argTypes[0]) and several valid commands and
    // terminals (nodeSets[0]). Contained in the node set is an ADF at index 1
    // in the node set (as declared with the second parameter during
    // ADF-construction: new ADF(..,1,..)).
    // The ADF has return type float (types[1]), three input parameters of type
    // float (argTypes[1]) and exactly one function: Add3 (nodeSets[1]).
    // ------------------------------------------------------------------------
    return GPGenotype.randomInitialGenotype(conf, types, argTypes, nodeSets,
        trainingData, true);
  }

  /**
   * Starts the example.
   *
   * @param args ignored
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public static void main(String[] args)
      throws Exception {
    System.out.println("Formula to discover: (-3*X*X+7)/2");
    // Setup the algorithm's parameters.
    // ---------------------------------

    //System.out.println("How many pieces of training data do you have?  Please enter a number between 0 and 100.");
    //System.out.println("Initial value of trainingData = " + trainingData);
    Scanner trainingInput = new Scanner(System.in);
	//trainingData = trainingInput.nextInt();

	try {
		   //load a properties file

		String fileName = args[0];
		System.out.println( "Input file = " + fileName);
		Properties prop = new Properties();
		prop.load(new FileInputStream(fileName));

		//get the property value and print it out
		//System.out.println("Before getProperty x val");
		//System.out.println(prop.getProperty("x_value"));
		//System.out.println(prop.getProperty("y_value"));
		//System.out.println("After getProperty y val");

		//String numValues = prop.getProperty("num_values");
		//trainingData = Integer.parseInt(numValues);
		//System.out.println("trainingData = " + trainingData);

		String strRunTime = prop.getProperty("run_time");
		runTime = Long.parseLong(strRunTime);

		System.out.println( "Program run time set at " + runTime + " minutes.");

		String xValString = prop.getProperty("x_value");
		String yValString = prop.getProperty("y_value");

		String [] xValArray = xValString.split(",");
		String [] yValArray = yValString.split(",");

		for (int i = 0; i < xValArray.length; i++)
		{
			System.out.println( "x[i] = " + i);
				x[i] = Float.valueOf(xValArray[i]).floatValue();
		}

		for (int i = 0; i < yValArray.length; i++)
		{
				y[i] = Float.valueOf(yValArray[i]).floatValue();
		}


		System.out.println( "Finished reading training data");

		System.out.print("Values of x = ");
		for (int i = 0; i < xValArray.length; i++)
		{
			System.out.print(x[i] + " ");
		}
		System.out.println();

		System.out.print("Values of y = ");
		for (int i = 0; i < yValArray.length; i++)
		{
			System.out.print(y[i] + " ");
		}
		System.out.println();

   /* for (int i = 0; i < trainingData; i++)
    {

		System.out.println("Please enter the x value for training data # " + i);

		x[i] = clientInput.nextFloat();

		System.out.println("Please enter the y value for training data # " + i);
		y[i] = clientInput.nextFloat();

		System.out.println(i + ") " + x[i] + "   " + y[i]);
	}*/

	} catch (IOException ex) {
		ex.printStackTrace();
	}


	/*Scanner clientInput = new Scanner(System.in);
	while (trainingData > 100 || trainingData < 0)
	{
	   System.out.println("Your input does not match the criteria, please enter a number between 0 and 100");

	   while(!clientInput.hasNextInt())
	   {
	       clientInput.next() ;
	   }
	   trainingData = clientInput.nextInt();
    }*/

    GPConfiguration config = new GPConfiguration();
    // We use a delta fitness evaluator because we compute a defect rate, not
    // a point score!
    // ----------------------------------------------------------------------
    config.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
    config.setMaxInitDepth(8);
    config.setPopulationSize(100);
    config.setMaxCrossoverDepth(8);
    config.setFitnessFunction(new GeneticProgramProject.FormulaFitnessFunction());
    config.setStrictProgramCreation(true);
    GPProblem problem = new GeneticProgramProject(config);
    // Create the genotype of the problem, i.e., define the GP commands and
    // terminals that can be used, and constrain the structure of the GP
    // program.
    // --------------------------------------------------------------------
    GPGenotype gp = problem.create();
    gp.setVerboseOutput(true);
    // Start the computation with maximum 800 evolutions.
    // if a satisfying result is found (fitness value almost 0), JGAP stops
    // earlier automatically.
    // --------------------------------------------------------------------

    Date currDate = new Date();
    long currTime = currDate.getTime();
    long endTime = currTime + (long) (runTime*60*1000);
    while (currTime < endTime) {
    	gp.evolve(1);
    	IGPProgram fittestProg = gp.getFittestProgram();
    	double fitness = fittestProg.getFitnessValue();
    	System.out.println( "fitness = " + fitness );
    	currDate = new Date();
    	currTime = currDate.getTime();
	}
    // gp.applyMutation(1,.08);
    // Print the best solution so far to the console.
    // ----------------------------------------------
    gp.outputSolution(gp.getAllTimeBest());
    // Create a graphical tree of the best solution's program and write it to
    // a PNG file.
    // ----------------------------------------------------------------------
    problem.showTree(gp.getAllTimeBest(), "GeneticProgramTree.png");
  }

  /**
   * Fitness function for evaluating the produced fomulas, represented as GP
   * programs. The fitness is computed by calculating the result (Y) of the
   * function/formula for integer inputs 0 to 20 (X). The sum of the differences
   * between expected Y and actual Y is the fitness, the lower the better (as
   * it is a defect rate here).
   */
  public static class FormulaFitnessFunction
      extends GPFitnessFunction {
    protected double evaluate(final IGPProgram a_subject) {
      return computeRawFitness(a_subject);
    }

    public double computeRawFitness(final IGPProgram ind) {
      double error = 0.0f;
      Object[] noargs = new Object[0];
      // Evaluate function for input numbers 0 to 10.
      // --------------------------------------------
      for (int i = 0; i < trainingData; i++) {
        // Provide the variable X with the input number.
        // See method create(), declaration of "nodeSets" for where X is
        // defined.
        // -------------------------------------------------------------
        vx.set(x[i]);
        try {
          // Execute the GP program representing the function to be evolved.
          // As in method create(), the return type is declared as float (see
          // declaration of array "types").
          // ----------------------------------------------------------------
          double result = ind.execute_float(0, noargs);
          // Sum up the error between actual and expected result to get a defect
          // rate.
          // -------------------------------------------------------------------
          error += Math.abs(result - y[i]);
          // If the error is too high, stop evlauation and return worst error
          // possible.
          // ----------------------------------------------------------------
          if (Double.isInfinite(error)) {
            return Double.MAX_VALUE;
          }
        } catch (ArithmeticException ex) {
          // This should not happen, some illegal operation was executed.
          // ------------------------------------------------------------
          System.out.println("x = " + x[i].floatValue());
          System.out.println(ind);
          throw ex;
        }
      }
      // In case the error is small enough, consider it perfect.
      // -------------------------------------------------------
      if (error < 0.001) {
        error = 0.0d;
      }
      return error;
    }
  }
}
