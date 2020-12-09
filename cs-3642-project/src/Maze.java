import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Maze 
{
	public Cell[][] maze;
	public Maze(int size)
	{
		maze = initMaze(size);
	}
	
	public static Cell[][] initMaze(int size)
	//returns a two-dimensional array of Cells with all partitions true.
	{
		Cell[][] maze = new Cell[size][size];
		
		for (int row = 0; row < size; row++)
			for (int col = 0; col < size; col++)
				maze[row][col] = new Cell(row, col);
		
		return maze;
	}
	
	public static Cell[][] testMaze(int size)
	//returns a two-dimensional array of Cells with all partitions
	//EXCEPT the north partitions as true.
	{
		Cell[][] maze = new Cell[size][size];
		
		for (int row = 0; row < size; row++)
			for (int col = 0; col < size; col++)
			{
				maze[row][col] = new Cell(row, col);
				maze[row][col].partitions[0] = false;
			}
		
		return maze;
	}
	
	public static int manhattanDist(Cell start, Cell end)
	{
		return Math.abs(start.row - end.row) + Math.abs(start.column - end.column);
	}
	
	public Cell move(Cell start, int direction)
	//takes a Cell and an index of Cell.DIRECTIONS, then returns the Cell
	//when moving in that direction
	{
		int rowMove = 999;
		int colMove = 999;
		
		try
		{
			rowMove = Cell.DIRECTIONS[direction][0];
			colMove = Cell.DIRECTIONS[direction][1];
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			System.out.println("Maze.move: " + e.getClass() + ", passed invalid direction");
			return start;
		}
		
		try
		{
			return maze[start.row + rowMove]
					[start.column + colMove];
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			System.out.println("Maze.move: " + e.getClass() + ", can't move this way");
			return start;
		}
	}
	
	public Cell randomCell()
	{
		return this.maze[(int)(Math.random() * this.maze.length)]
				[(int)(Math.random() * this.maze[0].length)];
	}
	
	public void recursBackMazeGen()
	//starts the maze generation with a random cell as a seed.
	{
		recursBackMazeGen(this.randomCell());
	}
	
	public void recursBackMazeGen(Cell input)
	/* 
	 * heavily references the recursive backtrack maze generation method.
	 * takes any cell as input, marks the current cell as visited, then
	 * checks if any of its neighbors are unvisited. if they are, remove
	 * the walls between the two cells and call this method recursively on
	 * the unvisited neighbor.
	 * since this is a recursive call, the size of the maze is limited to
	 * the maximum size of the runtime stack. ive seen it generate mazes
	 * with sizes upwards of 100.
	 */
	{
		input.visited = true;
		
		if (!input.neighbors(maze).isEmpty())
		{
			for (Iterator<Cell> neighborIterator = input.neighbors(maze).iterator(); neighborIterator.hasNext();)
			{
				Cell neighbor = neighborIterator.next();
				
				if (neighbor.visited)
					continue;
				
				List<int[]> dirs = Arrays.asList(Cell.DIRECTIONS);
				int[] coordInRelToNeighbor = {input.row - neighbor.row, input.column - neighbor.column};
				int[] coordNeighborRelToIn = {neighbor.row - input.row, neighbor.column - input.column};
				
				int intIndex = 0;
				for (Iterator<int[]> dirIterator = dirs.iterator(); dirIterator.hasNext();)
				{
					int[] coord = dirIterator.next();
					
					if (coord[0] == coordInRelToNeighbor[0] && coord[1] == coordInRelToNeighbor[1])
						neighbor.partitions[intIndex] = false;
					if (coord[0] == coordNeighborRelToIn[0] && coord[1] == coordNeighborRelToIn[1])
						input.partitions[intIndex] = false;
					intIndex++;
				}
				
				recursBackMazeGen(neighbor);
			}
		}
		
	}
	
	public void toSysOut()
	{
		System.out.println(this.toString());
	}
	
	public String toString(Cell loc, Cell goal)
	{
		String output = "";
		
		for (int row = 0; row < maze.length; row++)
		{
			for (int col = 0; col < maze[row].length; col++)
			{
				if (row == loc.row && col == loc.column)
					output = output.concat("☺");
				else if (row == goal.row && col == goal.column)
					output = output.concat("G");
				else
					output = output.concat("" + maze[row][col].charRep());
			}
			
			output = output.concat("\n");
		}
		
		return output;
	}
	
	@Override
	public String toString()
	{
		String output = "";
		
		for (int row = 0; row < maze.length; row++)
		{
			for (int col = 0; col < maze[row].length; col++)
				output = output.concat("" + maze[row][col].charRep());
			
			output = output.concat("\n");
		}
		
		return output;
	}
}
