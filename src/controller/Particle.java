package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;
import com.rits.cloning.Cloner;

public class Particle {
	
    private ArrayList<Double> position;
    private ArrayList<Double> pBest;
    Random rand = Main.rand;
    ArrayList<Double> velocity;
    private double fitness = 0;
    private double pBestFitness;
    Cloner cloner = new Cloner();


    public ArrayList<Double> getVelocity() {
        return velocity;
    }

    public void setVelocity(ArrayList<Double> velocity) {
        this.velocity = velocity;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
    
    public ArrayList<Double> getPosition() {
        return position;
    }

    public void setPosition(ArrayList<Double> position) {
        this.position = cloner.deepClone(position);
        computeFitness();
    }

    public ArrayList<Double> getPBest() {
        return pBest;
    }

    public void setPBest(ArrayList<Double> pBest) {
        this.pBest = cloner.deepClone(pBest);
        pBestFitness = fitness;
    }
    
    public Particle(int n){
        position = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            double value = Main.round(Math.pow(2.5, rand.nextGaussian()), 1);
            if(value > 3.0)
                value = 3.0;
            position.add(value);
        }
        pBest = (ArrayList) position.clone();
        velocity  = new ArrayList<>();
        generateVelocity();
        computeFitness();
        pBestFitness = fitness;
    }
    
    public Particle (){
        position = new ArrayList<>();
        pBest = (ArrayList) position.clone();
        velocity  = new ArrayList<>();
        generateVelocity();
    }
    
    public ArrayList<Double[]> subtractPosition(ArrayList<Double> p1){
        ArrayList<Double[]> difference = new ArrayList<>();
        double incr = 0.05;
        for(int i = 0; i < p1.size(); i++){
            Double pVal = p1.get(i);
            if(!pVal.equals(position.get(i))){
//                double prevVelVal = velocity.get(i);
//                if(prevVelVal > pVal){
//                    velocity.set(i, prevVelVal - incr);
//                } else {
//                    velocity.set(i, prevVelVal + incr);
//                }
                Double[] value = new Double[2];
                value[0] = Double.valueOf(i);
                value[1] = p1.get(i);
                difference.add(value);
            }
        }
        return difference;
    }
    
    public final void generateVelocity() {
        velocity.clear();
        for (int i = 0; i < position.size(); i++) {
            // Raidl and Bryant (2000)'s multiplicative scheme of bias based 
            // normal distribution, with biasing strength set at 1.5 as they 
            // recommended
            double bias = Main.round(Math.pow(2.5, rand.nextGaussian()), 1);
            
            // Clamp bias at 3.0
            if (bias > 3.0) {
                bias = 3.0;
            }
            velocity.add(bias);
        }
    }
    
    /**
     * Method which decodes the current position into its corresponding path
     * @return the cost of the constructed path
     */
    private double computeFitness(){
        /** First, we build the path according to position's biases **/
        path = decodePath(position);
        /** Then, we compute its cost **/
        fitness = getPathCost(path);
        return fitness;
    }
    
    public double getPathCost(Stack<Integer> path){
        double pathCost = 0;
        Iterator<Integer> i = path.iterator();
        int from = i.next();
        while (i.hasNext()){
            int to = i.next();
            pathCost += Main.GRAPH[from][to];
            from = to;
        }
        return pathCost;
    }
    public Stack<Integer> path;
    
    public Stack<Integer> decodePath(ArrayList<Double> biases){
        int start = Main.STARTNODE;
        int end = Main.ENDNODE;
        Integer[][] graph = new Integer[Main.GRAPHSIZE][Main.GRAPHSIZE];
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                graph[i][j] = Main.GRAPH[i][j];
            }
        }
        path = new Stack<>();
        int currNode = start;
        path.push(currNode);
        for (int i = 0; i < graph.length; i++) {
            graph[i][currNode] = 0;
        }
        while(true){
            double minBiasedCost = Double.MAX_VALUE;
            int nextNode = currNode;
            
            // Select node with least biased cost from adjacent nodes
            for (int i = 0; i < graph[currNode].length; i++) {
                if (graph[currNode][i] == 0)
                    continue;
                // Bias edge cost by weight values of both nodes adjacent to it
                double biasedCost = biases.get(currNode) * biases.get(i) * graph[currNode][i];
                // Get edge with minimum biased cost
                if(biasedCost < minBiasedCost){
                    nextNode = i;
                    minBiasedCost = biasedCost;
                }
            }
            
            // Check if destination node has been reached
            if (nextNode == end){
                path.push(nextNode);
                break;
            }
            
            // Check for pedantic (dead end) node and backtrack if true
            if(minBiasedCost == Double.MAX_VALUE){
                int prevNode = path.pop();
                graph[prevNode][currNode] = 0;
                graph[currNode][prevNode] = 0;
                currNode = path.peek();
                continue;
            }
            
            // Clear out the costs at nextNode's column of the adj matrix to 
            // prevent duplication of nodes in path
            for (int i = 0; i < graph.length; i++) {
                graph[i][nextNode] = 0;
            }
            graph[nextNode][currNode] = 0;
            
            currNode = nextNode;
            path.push(currNode);
        }
        return path;
    }
    @Override
    public String toString(){
        return position.toString();
    }

    /**
     * @return the pBestFitness
     */
    public double getPBestFitness() {
        return pBestFitness;
    }

    /**
     * @param pBestFitness the pBestFitness to set
     */
    public void setPBestFitness(double pBestFitness) {
        this.pBestFitness = pBestFitness;
    }
    
    public void updatePBest(){
        pBest = (ArrayList) position.clone();
        pBestFitness = fitness;
    }
    
    /** @Override
    public Particle clone(){
        Particle p = new Particle(Main.GRAPHSIZE);
        p.setFitness(this.fitness);
        
    }
    **/
}