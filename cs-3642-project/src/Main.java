import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main 
{
	public static void main(String[] args) 
	{
		Scanner scan = new Scanner(System.in);
		System.out.println("Solving a maze using Q-Learning");
		System.out.print("Enter the side length of the maze here (recommended: 15)... ");
		Maze maze = new Maze(scan.nextInt());
		System.out.print("Enter the timestep limit (recommended: 1000)... ");
		int timeLimit = scan.nextInt();
		scan.close();
		
		maze.recursBackMazeGen();
		maze.toSysOut();
		
		Cell startingCell = maze.randomCell();
		Cell terminalCell = maze.randomCell();
		System.out.println(maze.toString(startingCell, terminalCell));
		qFindPath(maze, startingCell, terminalCell, timeLimit);
		
		System.out.println("Main.main: Program terminating, terminal will close in 5 seconds...");
		try
		{
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) { }
	}
	
	public static void qFindPath(Maze maze, Cell start, Cell end, int timeLimit)
	/*
	 *  Q-learning, at its core, is the process of iteratively transitioning
	 *  to different states and learning how to predict the following reward.
	 *  Over time, the AI will transition from exploring the environment to
	 *  actively attempting to exploit it in order to reach its goal.
	 *  
	 *  Some important notes:
	 *  	Reward function - The reward for a state transition is the
	 *  Manhattan distance between the new state and the goal state. The AI
	 *  aims to minimize this value, and will terminate when it discovers a
	 *  reward of 0.
	 *  	Learning rate - A value between 0 and 1 that determines how new
	 *  information will override old information. In a fully deterministic
	 *  environment, such as this one, a learning rate of 1 is optimal.
	 *  	Discount factor - A value between 0 and 1 that determines how
	 *  much the AI will value long-term rewards compared to short-term ones.
	 *  	Q initialization - All values for Q, represented as attributes
	 *  within each Cell object, are initialized as 0.
	 */
	{
		System.out.println("Main.qFindPath: Attempting to find a path from " +
				start.toString() + " to " + end.toString());

		Cell currentLoc = start;
		Cell prevLoc = start;
		
		for (int time = 0; time < timeLimit; time++)
		{
			try
			{
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) { }
			
			System.out.println("Maze.qFindPath: iteration " + (time + 1) + " beginning, solution is " + end.toString());
			System.out.println("Maze.qFindPath: " + currentLoc.toString() + ".q is " + Arrays.toString(start.q));
			
			ArrayList<Integer> validActions = new ArrayList<>();
			for (int dirIndex = 0; dirIndex < currentLoc.partitions.length; dirIndex++)
				if (!currentLoc.partitions[dirIndex])
					validActions.add(dirIndex);
			System.out.println("Main.qFindPath: validActions for " + currentLoc.toString() + " is " + validActions.toString());
			//gathering list of valid potential actions
			
			double epsilon = 1 - ((double)time / timeLimit);
			System.out.println("Main.qFindPath: epsilon is now " + epsilon);
			//100% exploration at start -> 100% exploitation by end, linearly
			
			int moveToTake = 4; //initialized with invalid move
			
			if (Math.random() < epsilon)
			{
				System.out.println("Main.qFindPath: exploring!");
				moveToTake = validActions.get((int)(Math.random() * validActions.size()));
			}
			else
			{
				System.out.println("Main.qFindPath: exploiting!");
				double bestScore = Double.MAX_VALUE;
				for (int dirIndex = 0; dirIndex < currentLoc.partitions.length; dirIndex++)
				//standard minimum-value function
				{
					if (currentLoc.q[dirIndex] < bestScore && !currentLoc.partitions[dirIndex])
						{
							bestScore = currentLoc.q[dirIndex];
							moveToTake = dirIndex;
						}
				}
			}
			
			System.out.println("Maze.qFindPath: taking move " + moveToTake + ", " + Arrays.toString(Cell.DIRECTIONS[moveToTake]));
			
			prevLoc = currentLoc;
			currentLoc = maze.move(currentLoc, moveToTake);
			System.out.println("Main.qFindPath: currentLoc is now " + currentLoc.toString());
			
			if (Maze.manhattanDist(currentLoc, end) == 0)
			{
				System.out.println("Main.qFindPath: solution found!");
				System.out.println(maze.toString(currentLoc, end));
				break;
			}
			
			int reward = Maze.manhattanDist(currentLoc, end) + time;
			System.out.println("Main.qFindPath: reward for moving (" + 
					Cell.DIRECTIONS[moveToTake][0] + ", " + 
					Cell.DIRECTIONS[moveToTake][1] + ") from " + 
					prevLoc.toString() + " is " + reward);
			double learningRate = 0.1;
			
			prevLoc.q[moveToTake] += learningRate * reward; //update Q table
			
			System.out.println(maze.toString(currentLoc, end));
		}
	}
}
