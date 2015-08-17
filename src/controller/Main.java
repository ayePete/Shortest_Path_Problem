/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import java.util.Random;
import com.rits.cloning.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 *
 * @author Peter
 */

class Data implements Comparable<Data> {
	public final int index;
	public final int priority;
 
 
	public Data(int index, int priority) {
		this.index = index;
		this.priority = priority;
	} 
 
 
	@Override 
	public int compareTo(Data other) {
		return Integer.valueOf(priority).compareTo(other.priority);
	} 
	 
	public boolean equals(Data other) {
		return priority == other.priority;
	} 
 
 
	// also implement equals() and hashCode() 
} 

public class Main {
    
    

    public static final int GRAPHSIZE = 80;
    public static final int EDGE_NO = 232;
    public static final int STARTNODE = 25;
    public static final int ENDNODE = 1;
    public static final int DP = 4;
    public static final int NEIGHBORHOOD_SIZE = 5;
    public static Cloner cloner = new Cloner();

    public static double c1 = 2.05;
    public static double c2 = 2.05;
    public static ArrayList<Particle> swarm;
    public static ArrayList<Double> gbest;
    protected static ArrayList<Particle> nbests;
    static int MAX_VERTS = 50000;
    public static int[][] GRAPH = new int[][]{
        {0, 2, 4, 0, 0, 0, 0},
        {2, 0, 0, 8, 3, 0, 0},
        {4, 0, 0, 6, 0, 9, 0},
        {0, 8, 6, 0, 5, 0, 10},
        {0, 3, 0, 5, 0, 0, 7},
        {0, 0, 9, 0, 0, 0, 2},
        {0, 0, 0, 10, 7, 2, 0}
    };

    public static void main(String[] args) {
        GRAPH = randGraph();
        System.out.println("PSO: ");
        long startTime = System.currentTimeMillis();
        psoSPP();
        long endTime = System.currentTimeMillis();
        double totalTime = (endTime-startTime)/1000.0;
        double averageTime = totalTime/50.0;
        System.out.println("Total time: " + totalTime);
        System.out.println("Average time: " + averageTime);
        
        System.out.println("\nDijkstra: ");
        startTime = System.currentTimeMillis();
        System.out.println(dijkstra(GRAPH, STARTNODE, ENDNODE));
        endTime = System.currentTimeMillis();
        totalTime = (endTime-startTime)/1000.0;
        averageTime = totalTime/50.0;
        System.out.println("Total time: " + totalTime);
        System.out.println("Average time: " + averageTime);
        
       
    }
    
    static void psoSPP(){
        int errorCount = 0;
        Random rand = new Random();
        double r1, r2;
        ArrayList<String> gBests = new ArrayList<>();
        for (int k = 0; k < 100; k++) {
            init();
            int groupCounter = -1;
            for (int t = 0; t < 10; t++) {
                for (int i = 0; i < swarm.size(); i++) {
                    r1 = round(rand.nextDouble(), DP);
                    r2 = round(rand.nextDouble(), DP);

                    // Get differences between X and pBest and X and gBest
                    ArrayList<Double[]> pDiff = Particle.subtractPositions(swarm.get(i).getPBest(), swarm.get(i).getPosition());
                                       
                    if(i%NEIGHBORHOOD_SIZE == 0){
                        groupCounter++;
                    }
                    ArrayList<Double[]> gDiff = Particle.subtractPositions(nbests.get(groupCounter).getPosition(), swarm.get(i).getPosition());

                    // Get magnitude of each difference
                    int pDiffMagnitude = (int) round(c1 * r1 * pDiff.size(), 0);
                    int gDiffMagnitude = (int) round(c2 * r2 * gDiff.size(), 0);

                    // Generate new velocity
                    Double[] newPosition = new Double[GRAPHSIZE];
                    Arrays.fill(newPosition, -1.0);
                    for (int j = 0; j < gDiffMagnitude; j++) {
                        Double[] diffVal = gDiff.get(rand.nextInt(gDiff.size()));
                        newPosition[diffVal[0].intValue()] = diffVal[1];
                    }
                    for (int j = 0; j < pDiffMagnitude; j++) {
                        Double[] diffVal = pDiff.get(rand.nextInt(pDiff.size()));
                        int pDiffIndex = diffVal[0].intValue();
                        if (newPosition[pDiffIndex] == 0) {
                            newPosition[pDiffIndex] = diffVal[1];
                        }
                    }
                    ArrayList<Integer> emptyIndices = new ArrayList<>();
                    for (int j = 0; j < newPosition.length; j++) {
                        if (newPosition[j] < 0) {
                            emptyIndices.add(j);
                        }
                    }
                    for (Integer j : emptyIndices) {
                        newPosition[j] = swarm.get(i).getVelocity().get(j);
                    }
                    double prevFitness = swarm.get(i).getFitness();
                    ArrayList<Double> newPositionList = new ArrayList<>(Arrays.asList(newPosition));
                    swarm.get(i).setPosition(newPositionList);
                    if (swarm.get(i).getFitness() < prevFitness) {
                        swarm.get(i).setPBest(newPositionList);
                    }
                }
                computeNBests();
            }
            computeGBest();
            Stack<Integer> bestPath = Particle.decodePath(gbest);
            String output = "Path: " + bestPath + " Fitness: " + Particle.getPathCost(bestPath);
            int pso = (int) Particle.getPathCost(bestPath);
            int dijkstra = dijkstra(GRAPH, STARTNODE, ENDNODE);
            System.out.println("PSO: " + pso);
                    
            System.out.println("Dijkstra: " + dijkstra);
            if(dijkstra != pso)
                errorCount++;
            gBests.add(output);
        }
//        for(String out: gBests){
//            System.out.println(out);
//        }
           System.out.println("Error count: " + errorCount);     
        
    }


    public static void printVelocity(ArrayList<Double[]> v) {
        System.out.print("[");
        for (int i = 0; i < v.size() - 1; i++) {
            System.out.print(v.get(i)[0] + "(" + v.get(i)[1] + "), ");
        }
        System.out.println(v.get(v.size() - 1)[0] + "(" + v.get(v.size() - 1)[1] + ")]");
    }

    private static void init() {
        int groupCounter = -1;
        GRAPH = randGraph();
        swarm = new ArrayList<>();
        nbests = new ArrayList<>();
        for (int i = 0; i < GRAPHSIZE / 2; i++) {
            Particle p = new Particle(GRAPHSIZE);
            swarm.add(p);
            if (i % NEIGHBORHOOD_SIZE == 0) {
                groupCounter++;
                nbests.add(p);
            }
            if (p.getFitness() < nbests.get(groupCounter).getFitness()) {
                nbests.set(groupCounter, p);
            }
        }

    }
    
    private static void computeNBests(){
        int groupCounter = -1;
        for (int i = 0; i < swarm.size(); i++) {
            Particle p = swarm.get(i);
            if (i % NEIGHBORHOOD_SIZE == 0) {
                groupCounter++;
            }
            if (p.getFitness() < nbests.get(groupCounter).getFitness()) {
                nbests.set(groupCounter, p);
            }
        }
    }
    

    private static void computeGBest() {
        double minFitness = Double.MAX_VALUE;
        int minIndex = 0;
        for (int i = 0; i < swarm.size(); i++) {
            Particle p = swarm.get(i);
            if (p.getFitness() < minFitness) {
                minFitness = p.getFitness();
                minIndex = i;
            }
        }
        gbest = cloner.deepClone(swarm.get(minIndex).getPosition());
    }

    public static double round(double d, int numbersAfterDecimalPoint) {
        double n = Math.pow(10, numbersAfterDecimalPoint);
        double d2 = d * n;
        long lon = (long) d2;
        lon = ((long) (d2 + 0.5) > lon) ? lon + 1 : lon;
        return (lon) / n;
    }
    
    public static int[][] randGraph(){
        int [][] g = new int[GRAPHSIZE][GRAPHSIZE];
        Random rand = new Random();
        int edgeCount = 0;
        int counter = 0;
        for(; ; ){
            int i = rand.nextInt(GRAPHSIZE);
            int j = rand.nextInt(GRAPHSIZE);
            if (g[i][j] == 0){
                int cost = 10 + rand.nextInt(1000);
                g[i][j] = cost;
                g[j][i] = cost;
                counter++;
            }
            if (counter == EDGE_NO)
                break;
        }
        System.out.println("Done");
        return g;
    }
    
    static int dijkstra(int[][] G, int i, int j){
		//Get the number of vertices in G 
		int n = G.length;
		 
		/* ... Your code here ... */ 
		int[] distance = new int[G.length];
		PriorityQueue<Data> PQ = new PriorityQueue<Data>();
		boolean[] inTree = new boolean[G.length];
		 
		for (int index = 0; index < G.length; index++) {
			if (index == i) {
				distance[index] = 0;
			} 
			else { 
				distance[index] = Integer.MAX_VALUE;
				PQ.add(new Data(index, distance[index]));
				inTree[index] = true; 
			} 
                        
		} 
		 
		for (int index = 0; index < G.length; index++) { // for each edge (v,z) do
			if (G[i][index] != 0) { // There is an edge
				if (distance[i] + G[i][index] < distance[index]) { // if D[v] + w((v,z)) < D[z] then 
					int oldIndex = distance[index];
					distance[index] = distance[i] + G[i][index]; // D[z] ← D[v] + w((v,z))  
                                        Data t = new Data(index, oldIndex);
					PQ.remove(new Data(index, oldIndex));
					PQ.add(new Data(index, distance[index])); // update PQ wrt D[z] 
				} 
			} 
		} 
		 
			 
		while (PQ.peek() != null) { // If PQ isn't empty
			Data vertex = PQ.poll(); // RemoveMin
			for (int index = 0; index < G.length; index++) { // for each edge (u,z) with z ∈ PQ do
				if (G[vertex.index][index] != 0 && inTree[index] == true) { // z ∈ PQ
					if (distance[vertex.index] + G[vertex.index][index] < distance[index]) { // if D[v] + w((v,z)) < D[z] then 
						int oldIndex = distance[index];
						distance[index] = distance[vertex.index] + G[vertex.index][index]; // D[z] ← D[v] + w((v,z)) 
						PQ.remove(new Data(index, oldIndex));
						PQ.add(new Data(index, distance[index])); // update PQ wrt D[z] 
					} 
				} 
			 
			} 
		} 
		if (distance[j] == Integer.MAX_VALUE || distance[j] < 0) {
			return -1; 
		} 
		else { 
			return distance[j];
		} 
    }
}
    
//    public static void dijkstra(){
//        Integer[][] distanceGraph = new Integer[GRAPHSIZE][GRAPHSIZE];
//        int start = STARTNODE;
//        int end = ENDNODE;
//        int cummCost = 0;
//        int currNode = start;
//        int nextNode = currNode;
//        int minTotalCost = 0;
//        while (true){
//            int minPartialCost = Integer.MAX_VALUE;
//            for (int i = 0; i < distanceGraph[nextNode].length; i++) {
//                int partialCost = minTotalCost + GRAPH[currNode][i];
//                if(partialCost < minPartialCost){
//                    minPartialCost = partialCost;
//                    nextNode = i;
//                }
//            }
//            minTotalCost = minPartialCost;
//            if (nextNode == end)
//                break;
//            currNode = nextNode;
//        }
//    }
