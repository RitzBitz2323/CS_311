import java.io.File;
import java.io.IOException;
import java.util.*;

class Node
{
	private Node parent;
	private int[] currentState;
	public Node(int[] currentState)
	{
		this.parent = null;
		this.currentState = currentState;
	}
	public Node(Node parent, int[] currentState)
	{
		this.parent = parent;
		this.currentState = currentState;
	}
	public Node getParentNode()
	{
		return parent;
	}
	public int[] getNode()
	{
		return currentState;
	}
}


public class FrogGame
{
	private ArrayList<Node> endStates;
	private Map<Node, List<Node>> plansTree;
    private int[] board;
	private int sizeOfBoard;
	
	public FrogGame()
	{
		sizeOfBoard = -1;
		plansTree = new HashMap<>();
		endStates = new ArrayList<>();;
		board = null;
	}
	public void readInput(String FileName) throws IOException
	{
      Scanner reader = new Scanner(new File(FileName));
      sizeOfBoard = reader.nextInt();

      ArrayList<Integer> frogLocs = new ArrayList<>();
      while(reader.hasNextInt())
      {
      	frogLocs.add(reader.nextInt());
      }
      board = new int[sizeOfBoard*sizeOfBoard];
      for(int i = 0; i<sizeOfBoard*sizeOfBoard; i++)
      {
      	if(frogLocs.contains(i+1))
      	{
      		board[i] = 1;
      	}
      	else
      	{
      		board[i] = 0;
      	}
      }
      reader.close();
	}


    private void addEdge(Node parent, int[] child)
	{
		if(!plansTree.containsKey(parent))
		{
			plansTree.put(parent, new LinkedList<>());
		}
		plansTree.get(parent).add(new Node(parent, child));
	}



	private int getRowPosition(int i)
	{
		return i%sizeOfBoard;
	}
	private int getColPosition(int i)
	{
		return (i-(i%sizeOfBoard))/sizeOfBoard;
	}
	private int getIndex(int x, int y)
	{
		return (y*sizeOfBoard)+x;
	}

    private void sequenceOfBoard(Node board, Stack<int[]> stack)
	{
		if(board.getParentNode() == null)
		{
			stack.push(board.getNode());
			return;
		}
		else
		{
			stack.push(board.getNode());
			sequenceOfBoard(board.getParentNode(), stack);
		}
	}
	private int frogsCount(int[] board)
	{
		int count = 0;
		for(int i = 0; i<board.length; i++)
		{
			if(board[i] == 1)
			{
				count++;
			}
		}
		return count;
	}
    
	public ArrayList<GamePlan> getGamePlans()
	{
		Node root = new Node(board);
		getMoves(root);
		ArrayList<GamePlan> allPlans = new ArrayList<>();
		for(Node end: endStates)
		{
			Stack<int[]> stack = new Stack<>();
			sequenceOfBoard(end, stack);
			int size = stack.size();
			ArrayList<int[]> plan = new ArrayList<>();
			for(int i = 0; i<size; i++)
			{
				int[] state = stack.pop();
				int numFrogs = frogsCount(state);
				int[] toGp = new int[numFrogs];
				int index = 0;
				for(int j = 0; j<state.length; j++)
				{
					if(state[j] == 1)
					{
						toGp[index] = j+1;
						index++;
					}
				}
				plan.add(toGp);
			}
			int goal = frogFinalLocation(end.getNode())+1;
			boolean found = false;
			for(GamePlan gp: allPlans)
			{
				if(gp.getgoalLoc() == goal)
				{
					gp.addPlan();
					found = true;
				}
			}
			if(!found)
			{
				GamePlan gp = new GamePlan(plan, goal, 1);
				allPlans.add(gp);
			}
		}
		if(allPlans.size() == 0)
		{
			return null;
		}
		return allPlans;
	}
	
	private int frogFinalLocation(int[] board)
	{
		for(int i = 0; i<board.length; i++)
		{
			if(board[i] == 1)
			{
				return i;
			}
		}
		return -1;
	}
	
	private void getMoves(Node board)
	{
		if(frogsCount(board.getNode()) == 1)
		{
			endStates.add(board);
			return;
		}
		ArrayList<int[]> moveList = getAllMoves(board.getNode());
		if(moveList.size() == 0) //check this
		{
			plansTree.remove(board);
			return;
		}
		for(int[] move: moveList)
		{
			addEdge(board, move);
		}
		LinkedList<Node> list= (LinkedList<Node>) plansTree.get(board);
		for(Node node: list)
		{
			getMoves(node);
		}
	}
	
	private ArrayList<int[]> getAllMoves(int[] pos)
	{
		ArrayList<int[]> allMoves = new ArrayList<>();
		for(int i = 0; i<sizeOfBoard*sizeOfBoard; i++)
		{
			if(pos[i] == 1)
			{
				ArrayList<int[]> moves= getValidMoves(pos, getRowPosition(i), getColPosition(i));
				for(int[] move: moves)
				{
					allMoves.add(move);
				}
			}
		}
		return allMoves;
	}
	private ArrayList<int[]> getValidMoves(int[] pos, int x, int y)
	{
		ArrayList<int[]> moveList = new ArrayList<>();
		//no frog there
		if(pos[getIndex(x,y)]!= 1)
		{
			return null;
		}
		
		if(x-1 > -1 && y-1 > -1 && pos[getIndex(x-1,y-1)] == 1) //diag to top left
		{
			if(x-2 > -1 && y-2 > -1 && pos[getIndex(x-2,y-2)] == 0)
			{
				int[] newPos = new int[sizeOfBoard * sizeOfBoard];
				for(int i = 0; i<sizeOfBoard*sizeOfBoard; i++)
				{
					newPos[i] = pos[i];
				}
				newPos[getIndex(x,y)] = 0;
				newPos[getIndex(x-1,y-1)] = 0;
				newPos[getIndex(x-2,y-2)]= 1;
				moveList.add(newPos);
			}
		}
		
		if(y-1 > -1 && pos[getIndex(x,y-1)] == 1) //above
		{
			if(y-2 > -1 && pos[getIndex(x,y-2)] == 0)
			{
				int[] newPos = new int[sizeOfBoard * sizeOfBoard];
				for(int i = 0; i<sizeOfBoard*sizeOfBoard; i++)
				{
					newPos[i] = pos[i];
				}
				newPos[getIndex(x,y)] = 0;
				newPos[getIndex(x,y-1)] = 0;
				newPos[getIndex(x,y-2)]= 1;
				moveList.add(newPos);
			}
		}
		
		if(y-1 > -1 && x+1 < sizeOfBoard && pos[getIndex(x+1,y-1)] == 1) //diag top right
		{
			if(y-2 > -1 && x+2 < sizeOfBoard && pos[getIndex(x+2,y-2)] == 0)
			{
				int[] newPos = new int[sizeOfBoard * sizeOfBoard];
				for(int i = 0; i<sizeOfBoard*sizeOfBoard; i++)
				{
					newPos[i] = pos[i];
				}
				newPos[getIndex(x,y)] = 0;
				newPos[getIndex(x+1,y-1)] = 0;
				newPos[getIndex(x+2,y-2)]= 1;
				moveList.add(newPos);
			}
		}
		
		if(x-1>-1 && pos[getIndex(x-1,y)] == 1)
		{
			if(x-2>-1 && pos[getIndex(x-2, y)] == 0)
			{
				int[] newPos = new int[sizeOfBoard * sizeOfBoard];
				for(int i = 0; i<sizeOfBoard*sizeOfBoard; i++)
				{
					newPos[i] = pos[i];
				}
				newPos[getIndex(x,y)] = 0;
				newPos[getIndex(x-1,y)] = 0;
				newPos[getIndex(x-2,y)]= 1;
				moveList.add(newPos);
			}
		}
		
		if(x+1 < sizeOfBoard && pos[getIndex(x+1, y)] == 1)
		{
			if(x+2 <sizeOfBoard && pos[getIndex(x+2, y)] == 0)
			{
				int[] newPos = new int[sizeOfBoard * sizeOfBoard];
				for(int i = 0; i<sizeOfBoard*sizeOfBoard; i++)
				{
					newPos[i] = pos[i];
				}
				newPos[getIndex(x,y)] = 0;
				newPos[getIndex(x+1,y)] = 0;
				newPos[getIndex(x+2,y)]= 1;
				moveList.add(newPos);
			}
		}
		
		if(y+1 < sizeOfBoard && x-1 > -1 && pos[getIndex(x-1,y+1)] == 1)
		{
			if(y+2< sizeOfBoard && x-2 > -1 && pos[getIndex(x-2,y+2)] == 0)
			{
				int[] newPos = new int[sizeOfBoard * sizeOfBoard];
				for(int i = 0; i<sizeOfBoard*sizeOfBoard; i++)
				{
					newPos[i] = pos[i];
				}
				newPos[getIndex(x,y)] = 0;
				newPos[getIndex(x-1,y+1)] = 0;
				newPos[getIndex(x-2,y+2)]= 1;
				moveList.add(newPos);
			}
		}
		
		if(y+1 < sizeOfBoard && pos[getIndex(x,y+1)] == 1)
		{
			if(y+2 < sizeOfBoard && pos[getIndex(x,y+2)] == 0)
			{
				int[] newPos = new int[sizeOfBoard * sizeOfBoard];
				for(int i = 0; i<sizeOfBoard*sizeOfBoard; i++)
				{
					newPos[i] = pos[i];
				}
				newPos[getIndex(x,y)] = 0;
				newPos[getIndex(x,y+1)] = 0;
				newPos[getIndex(x,y+2)]= 1;
				moveList.add(newPos);
			}
		}
		
		if(y+1 < sizeOfBoard && x+1 < sizeOfBoard && pos[getIndex(x+1,y+1)] == 1)
		{
			if(y+2 <sizeOfBoard && x+2 < sizeOfBoard && pos[getIndex(x+2,y+2)] == 0)
			{
				int[] newPos = new int[sizeOfBoard * sizeOfBoard];
				for(int i = 0; i<sizeOfBoard*sizeOfBoard; i++)
				{
					newPos[i] = pos[i];
				}
				newPos[getIndex(x,y)] = 0;
				newPos[getIndex(x+1,y+1)] = 0;
				newPos[getIndex(x+2,y+2)]= 1;
				moveList.add(newPos);
			}
		}
		return moveList;
	}
	
}
class GamePlan
{
	private int goalLoc;
	private int numOfPlans;
	private ArrayList<int[]> plan;
	public GamePlan(ArrayList<int[]> plan, int endLoc, int numPlans)
	{
		goalLoc = endLoc;
		this.plan = plan;
		numOfPlans = numPlans;
	}
	public void addPlan()
	{
		numOfPlans++;
	}
    public int getgoalLoc() 
	{
		return goalLoc;
	}
	public int getnumOfPlans() 
	{
		return numOfPlans;
	}
	public ArrayList<int[]> getPlan() 
	{
		return plan;
	}
}



