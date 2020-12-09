import java.util.ArrayList;
import java.util.Collections;

public class Cell
{
	public int row;
	public int column;
	public boolean visited;
	public boolean[] partitions; // {N, E, S, W}
	public double[] q = {0, 0, 0, 0}; // Q values for {N, E, S, W}
	public static final int[][] DIRECTIONS =
		{
				{0, -1}, //S
				{1, 0}, //W
				{0, 1}, //N
				{-1, 0} //E
		};
	
	public Cell(int row, int column)
	{
		this.row = row;
		this.column = column;
		this.visited = false;
		this.partitions = new boolean[]{true, true, true, true};
		this.q = new double[4];
	}
	
	public ArrayList<Cell> neighbors(Cell[][] input)
	{
		ArrayList<Cell> output = new ArrayList<>();
		
		for (int dirIndex = 0; dirIndex < DIRECTIONS.length; dirIndex++)
		{
			try
			{
				Cell potentialNeighbor = input[this.row + DIRECTIONS[dirIndex][0]]
						[this.column + DIRECTIONS[dirIndex][1]];
				output.add(potentialNeighbor);
			}
			catch (ArrayIndexOutOfBoundsException e) {continue;}
		}

		Collections.shuffle(output);
		return output;
	}
	
	public char charRep()
	{
		int value = (partitions[0] ? 8 : 0) +
				(partitions[1] ? 4 : 0) +
				(partitions[2] ? 2 : 0) +
				(partitions[3] ? 1 : 0);
		char rep = 'x';
		
		switch(value)
		{
			case 0: //FFFF
				rep = '╋';
				break;
			case 1: //FFFT
				rep = '┳';
				break;
			case 2: //FFTF
				rep = '┫';
				break;
			case 3: //FFTT
				rep = '┓';
				break;
			case 4: //FTFF
				rep = '┻';
				break;
			case 5: //FTFT
				rep = '━';
				break;
			case 6: //FTTF
				rep = '┛';
				break;
			case 7: //FTTT
				rep = '╸';
				break;
			case 8: //TFFF - ESW
				rep = '┣';
				break;
			case 9: //TFFT
				rep = '┏';
				break;
			case 10: //TFTF
				rep = '┃';
				break;
			case 11: //TFTT
				rep = '╻';
				break;
			case 12: //TTFF
				rep = '┗';
				break;
			case 13: //TTFT
				rep = '╺';
				break;
			case 14: //TTTF
				rep = '╹';
				break;
			case 15: //TTTT
				rep = '·';
				break;
		}
		
		return rep;
	}
	
	@Override
	public String toString()
	{
		return "(" + row + ", " + column + ")";
	}
}
