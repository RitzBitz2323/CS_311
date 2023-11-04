import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PathFinder
{
	private Map<Node, List<Node>> city;
	private int numNodes;
	private Node[] nodes;

	public PathFinder()
	{
		city = new HashMap<>();
		nodes = null;
		numNodes = 0;
	}

	public void readInput(String filename) throws FileNotFoundException
	{
		Scanner scan = new Scanner(new File(filename));

		numNodes = scan.nextInt();
		int numEdges = scan.nextInt();

		nodes = new Node[numNodes];

		for (int i = 0; i < numNodes; i++) {
			int data = scan.nextInt();
			int x = scan.nextInt();
			int y = scan.nextInt();
			Node n = new Node(data, x, y);
			nodes[data] = n;
			city.put(n, new LinkedList<Node>());
		}

		for (int i = 0; i < numEdges; i++) {
			int from = scan.nextInt();
			int to = scan.nextInt();
			city.get(nodes[from]).add(nodes[to]);
			city.get(nodes[to]).add(nodes[from]);
		}

	}

	public double distToDest(int srcId, int destId, int k)
	{
		MinHeap heap = new MinHeap(k, numNodes);
		double[] distance = new double[nodes.length];
    for (int i = 0; i < nodes.length; i++)
    {
      distance[i] = Double.MAX_VALUE;
    }
    distance[srcId] = 0;
    
    
    heap.insert(new Pair(nodes[srcId].getData(), 0));

    while (heap.getSize() > 0) {
        Node current = nodes[heap.extractMin().getNode()];
        for (Node n :city.get(current)) 
        {
        	double dist = PathFinder.distance(current.getX(), current.getY(), n.getX(),n.getY());
          dist += distance[current.getData()];
        		if (dist < distance[n.getData()]) 
            {
                distance[n.getData()]= dist;
                heap.insert(new Pair(n.getData(), dist));             
            }
        }
    }
    
    double distToDest = distance[destId];
    if (distToDest == Double.MAX_VALUE)
    {
    	distToDest = -1;
    }
    return distToDest;
	}

	public int noOfShortestPaths(int srcId, int destId, int k)
	{
		MinHeap heap = new MinHeap(k, numNodes);
		double[] distance = new double[nodes.length];
    Arrays.fill(distance, Double.MAX_VALUE);
    distance[srcId] = 0;
    
    int[] numPaths = new int[nodes.length];
    Arrays.fill(numPaths, 0);
    
    heap.insert(new Pair(nodes[srcId].getData(), 0));

    while (heap.getSize() > 0) {
        Node current = nodes[heap.extractMin().getNode()];
        for (Node n :city.get(current)) 
        {
        	double dist = PathFinder.distance(current.getX(), current.getY(), n.getX(),n.getY());
          dist += distance[current.getData()];
        		if (dist < distance[n.getData()]) 
            {
                distance[n.getData()]= dist;
                heap.insert(new Pair(n.getData(), dist)); 
                numPaths[n.getData()] = 1;
            }
        		else if(dist == distance[n.getData()])
        		{
        			numPaths[n.getData()]++;
        		}
        }
    }
		return numPaths[destId];
	}

	public ArrayList<Integer> fromSrcToDest(int srcId, int destId, int k)
	{
		MinHeap heap = new MinHeap(k, numNodes);
		double[] distance = new double[nodes.length];
    for (int i = 0; i < nodes.length; i++)
    {
      distance[i] = Double.MAX_VALUE;
    }
    distance[srcId] = 0;

    heap.insert(new Pair(nodes[srcId].getData(), 0));

    while (heap.getSize() > 0) {
        Node current = nodes[heap.extractMin().getNode()];
        for (Node n :city.get(current)) 
        {
        	double dist = PathFinder.distance(current.getX(), current.getY(), n.getX(),n.getY());
          dist += distance[current.getData()];
        		if (dist < distance[n.getData()]) 
            {
                distance[n.getData()]= dist;
                heap.insert(new Pair(n.getData(), dist));             
            }
        }
    }
    if(distance[destId] == Double.MAX_VALUE)
    {
    	return null;
    }
    
    Stack<Integer> s = new Stack<>();
    Integer current = destId;
    while(current != srcId)
    {
    	s.push(current);
    	double low = Integer.MAX_VALUE;
    	Integer lowId = -1;
    	for(Node n:city.get(nodes[current]))
    	{
    		double dist = distance[n.getData()];
    		if(dist < low)	
    		{
    			low = dist;
    			lowId = n.getData();
    		}
    	}
    	current = lowId;
    }
    s.push(srcId);
    
    ArrayList<Integer> arr = new ArrayList<>();
    int stackSize = s.size();
    for(int i = 0; i<stackSize; i++)
    {
    	arr.add(s.pop());
    }
    return arr;
	}

	public static double distance(int x1, int y1, int x2, int y2)
	{
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
}

class MinHeap
{
	private int k;
	private int size;
	private Pair[] heap;
	private int index;

	public MinHeap(int k, int maxSize)
	{
		size = maxSize;
		this.k = k;
		heap = new Pair[size];
		index = 0;
		// fills heap with max int so anything that is added is the new min
		Arrays.fill(heap, new Pair(-1, Integer.MAX_VALUE));
	}

	private int parent(int i)
	{
		return (i - 1) / k;
	}

	private void swap(int x, int y)
	{
		Pair tmp;
		tmp = heap[x];
		heap[x] = heap[y];
		heap[y] = tmp;
	}

	private int getChild(int i, int child)
	{
		return k * i + child;
	}
	public Pair[] getHeap()
	{
		return heap;
	}
	private boolean containsPair(Pair pair)
	{
		for(Pair p: heap)
		{
			if(p.getNode() == pair.getNode())
			{
				return true;
			}
		}
		return false;
	}
	
	public void insert(Pair num)
	{
		
		if(containsPair(num))
		{
			for(int i = 0; i<size; i++)
			{
				if(heap[i].getNode() == num.getNode())
				{
					heap[i] = num;
					index = i;
			    break;
				}
			}
			
		}
		else if (index >= size) {
			return;
		}
		else
		{
			heap[index] = num;
		}
		
		int current = index;

		while (heap[current].getDistance() < heap[parent(current)].getDistance()) {
			swap(current, parent(current));
			current = parent(current);
		}
		index++;
	}

	public Pair extractMin()
	{
		// since its a min heap, so root = minimum
		Pair popped = heap[0];
		heap[0] = new Pair(-1, Integer.MAX_VALUE);
		size--;
		heapifyDown(0);
		return popped;
	}

	private void heapifyDown(int ind)
	{
		int child;
		Pair tmp = heap[ind];
		while (getChild(ind, 1) < size) {
			child = minChild(ind);
			if (heap[child].getDistance() < tmp.getDistance()) {
				heap[ind] = heap[child];
			} else {
				break;
			}
			ind = child;
		}
		heap[ind] = tmp;
	}

	private int minChild(int ind)
	{
		int bestChild = getChild(ind, 1);
		int i = 2;
		int pos = getChild(ind, i);
		while ((i <= k) && (pos < size)) {
			if (heap[pos].getDistance() < heap[bestChild].getDistance())
				bestChild = pos;
			pos = getChild(ind, i++);
		}
		return bestChild;
	}

	public int getSize()
	{
		for(int i = 0; i<heap.length; i++)
		{
			if(heap[i].getNode() == -1)
			{
				return i;
			}
		}
		return size;
	}
}

class Node
{
	private int x;
	private int y;
	private int data; // basically name

	public Node(int data, int x, int y)
	{
		this.data = data;
		this.x = x;
		this.y = y;
	}

	public int getData()
	{
		return data;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}
}

class Pair
{
	private int node;
	private double dist;

	public Pair(int node, double dist)
	{
		this.node = node;
		this.dist = dist;
	}

	public double getDistance()
	{
		return dist;
	}

	public int getNode()
	{
		return node;
	}
}
