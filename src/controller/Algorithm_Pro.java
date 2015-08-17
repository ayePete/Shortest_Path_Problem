//package controller;
//
///**
// * The class that brings everything together, and executes the ECDPSO algorithm
// *
// * @author Popoola Peter Ayokunle
// */
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.nio.charset.Charset;
//import java.nio.file.Files;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Random;
//import java.util.Set;
//
//import javax.swing.SwingWorker;
//
//import model.Properties;
//
//import exception.EdgeSetException;
//import exception.InvalidFileException;
//import exception.SizeExceededException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class Algorithm_Pro extends SwingWorker<EdgeSet, EdgeSet> implements Subject {
//
//    private static List<Double> checkResults = new ArrayList<Double>();
//    private static List<Particle> swarm;
//    private static List<EdgeSet> bests;
//    private static List<Double> costList = new ArrayList<Double>();
//    private static File costFile = new File("c:costs.txt");
//    private static double c1, c2;
//    private static double w;
//    private static int numberOfRuns = 50;
//    private static final int COSTLIMIT = 50;
//    private static EdgeSet edgeBank;
//    private volatile boolean running = true;
//    private volatile int speed = 50;
//    private volatile boolean paused = false;
//
//    /**
//     * @return {@code boolean} the paused
//     */
//    public boolean isPaused() {
//        return paused;
//    }
//
//    /**
//     * @param <b>paused</b> the {@code boolean} paused to set
//     */
//    public void setPaused(boolean paused) {
//        this.paused = paused;
//    }
//
//    /**
//     * @return {@code boolean} the running
//     */
//    public boolean isRunning() {
//        return running;
//    }
//
//    /**
//     * @param <b>running</b> the {@code boolean} running to set
//     */
//    public void setRunning(boolean running) {
//        this.running = running;
//    }
//
//    /**
//     * @return {@code int} the speed
//     */
//    public int getSpeed() {
//        return speed;
//    }
//
//    /**
//     * @param <b>speed</b> the {@code int} speed to set
//     */
//    public void setSpeed(int speed) {
//        this.speed = speed;
//    }
//
//    private final ArrayList<Observer> observers = new ArrayList<Observer>();
//
//    /**
//     * @return {@code EdgeSet} the edgeBank
//     */
//    public static EdgeSet getEdgeBank() {
//        return edgeBank;
//    }
//
//    /**
//     * @param <b>edgeBank</b> the {@code EdgeSet} edgeBank to set
//     */
//    public static void setEdgeBank(EdgeSet edgeBank) {
//        Algorithm_Pro.edgeBank = edgeBank;
//    }
//
//    private static EdgeSet gbest;
//    private static double edgeBankSize;
//    private static double T;
//
//    private static double Q;
//
//    private static double R;
//
//    private static double K;
//    private static double graphSize;
//    private static final int DP4 = 4;
//    public Particle currentParticle;
//    private int progress;
//    private static String[] values;
//
//    private boolean checkTermination(double newResult) {
//        int resultSize = checkResults.size();
//        if (resultSize <= 0) {
//            checkResults.add(newResult);
//            return false;
//        }
//        if (checkResults.get(resultSize - 1) == newResult) {
//            checkResults.add(newResult);
//        } else {
//            checkResults.clear();
//            return false;
//        }
//        if (resultSize > (graphSize * 10)) {
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * Compute the gbest of particles by comparing their pbests
//     *
//     * @return the {@code Particle} gbest
//     */
//    private void computeGBest() {
//        double maxFitness = Integer.MAX_VALUE;
//        int maxFitnessIndex = 0;
//        for (int i = 0; i < swarm.size(); i++) {
//            double fitness = swarm.get(i).getPbest().fitness();
//            if (maxFitness > fitness) {
//                maxFitnessIndex = i;
//                maxFitness = fitness;
//            }
//        }
//        gbest = swarm.get(maxFitnessIndex).getPbest().clone();
//    }
//
//    /**
//     * Method that computes a new Position for the supplied particle, according
//     * to the ECDPSO Algorithm, assuming that its <br>
//     * velocity has already been computed and updated.
//     *
//     * @param pt the {@code Particle} particle for which a new position is to be
//     * computed
//     * @param diff1 the {@code List<Integer>} value of pbest-x
//     * @param scalarDiff1 the {@code int} value of c1r1(pbest-x)
//     * @param diff2 the {@code List<Integer>} value of gbest-x
//     * @param scalarDiff2 the {@code int} value of c2r2(gbest-x)
//     * @return dummyEdgeSet the new {@code EdgeSet} position.
//     */
//    private static EdgeSet computeNewPosition(Particle pt, List<Integer> diff1,
//            int scalarDiff1, List<Integer> diff2, int scalarDiff2) {
//        EdgeSet staticEdgeSet = pt.getPosition().clone();
//        EdgeSet dummyEdgeSet = pt.getPosition().clone();
//        Random rand = new Random();
//        // Retrieve velocity(i)
//        int velocity = pt.getVelocity();
//        if (velocity <= 2) {
//            return dummyEdgeSet;
//        }
//        /*--- Edge Removal ---*/
//        int randomIndex;
//        // Randomly remove edges according to value of c1r1(pbest-x)
//        if (!diff1.isEmpty()) {
//            for (int i = 0; i < scalarDiff1; i++) {
//                if (diff1.isEmpty()) {
//                    break;
//                }
//                randomIndex = rand.nextInt(diff1.size());
//                Edge removedEdge = staticEdgeSet
//                        .getEdge(diff1.get(randomIndex));
//                dummyEdgeSet.removeEdge(removedEdge);
//                diff1.remove(randomIndex);
//            }
//        }
//        // Randomly remove edges according to value of c2r2(gbest-x)
//        if (!diff2.isEmpty()) {
//            for (int i = 0; i < scalarDiff2; i++) {
//                if (diff2.isEmpty()) {
//                    break;
//                }
//                randomIndex = rand.nextInt(diff2.size());
//                Edge removedEdge = staticEdgeSet
//                        .getEdge(diff2.get(randomIndex));
//                dummyEdgeSet.removeEdge(removedEdge);
//                diff2.remove(randomIndex);
//            }
//        }
//
//        // Randomly remove remaining edges if the scalarDiff1+scalarDiff2 is not
//        // up to the required velocity
//        if (pt.getVelocity() > scalarDiff1 + scalarDiff2) {
//            int velDiff = pt.getVelocity() - (scalarDiff1 + scalarDiff2);
//            for (int i = 0; i < velDiff; i++) {
//                dummyEdgeSet.removeIndex(rand.nextInt(dummyEdgeSet.size()));
//            }
//        }
//        /*--- Edge Addition ---*/
//        int listIndex;
//        dummyEdgeSet.init();
//        List<Integer> greaterThanT = new ArrayList<>(); // List of edges
//        // whose probabilities are greater than K
//        List<Integer> greaterThanR = new ArrayList<>(); // List of edges
//        // whose probabilities are greater than R
//        R = round(rand.nextDouble() * T, DP4);
//
//        for (int i = 0; i < edgeBank.size(); i++) {
//            if (edgeBank.getEdge(i).getProbability() >= K) {
//                greaterThanT.add(i); // else
//            }
//            if (edgeBank.getEdge(i).getProbability() >= R) {
//                greaterThanR.add(i);
//            }
//        }
//
//        int kDiff = 0;
//        int kNum = 0;
//        if (greaterThanT.size() > pt.getVelocity()) {
//            kDiff = 0;
//            kNum = pt.getVelocity();
//        } else {
//            kDiff = pt.getVelocity() - greaterThanT.size();
//            kNum = greaterThanT.size();
//        }
//        int count = 0;
//        for (int i = 0; i < kNum; i++) {
//            if (greaterThanT.isEmpty()) {
//                break;
//            }
//            if (count > greaterThanT.size()) {
//                break;
//            }
//            listIndex = rand.nextInt(greaterThanT.size());
//            randomIndex = greaterThanT.get(listIndex);
//            try {
//                if (!(dummyEdgeSet.add(edgeBank.getEdge(randomIndex)))) {
//                    i--;
//                    count++;
//                } else {
//                    greaterThanT.remove(listIndex);
//                    count = 0;
//                }
//            } catch (EdgeSetException e) {
//            }
//        }
//        int rDiff = 0;
//        int rNum = 0;
//        if (greaterThanR.size() > kDiff) {
//            rNum = kDiff;
//            rDiff = 0;
//        } else {
//            rNum = greaterThanR.size();
//            rDiff = kDiff - greaterThanR.size();
//        }
//        count = 0;
//        for (int i = 0; i < rNum; i++) {
//            if (dummyEdgeSet.size() == graphSize) {
//                break;
//            }
//            if (greaterThanR.isEmpty()) {
//                break;
//            }
//            if (count > greaterThanR.size()) {
//                break;
//            }
//            listIndex = rand.nextInt(greaterThanR.size());
//            randomIndex = greaterThanR.get(listIndex);
//            try {
//                if (!(dummyEdgeSet.add(edgeBank.getEdge(randomIndex)))) {
//                    i--;
//                    count++;
//                } else {
//                    greaterThanR.remove(listIndex);
//                    count = 0;
//                }
//            } catch (EdgeSetException e) {
//            }
//        }
//
//        while (dummyEdgeSet.size() < graphSize) {
//            try {
//                dummyEdgeSet.add(edgeBank.getEdge(rand
//                        .nextInt((int) edgeBankSize)));
//            } catch (EdgeSetException e) {
//            }
//        }
//        return dummyEdgeSet;
//    }
//
//    public static void defaultData(int n) {
//        Properties.setCityNumber(n);
//        Properties.setPrecision(10);
//        graphSize = Properties.getCityNumber();
//        double costs[] = {26, 95, 38, 74, 80, 73, 73, 92, 22, 97, 13, 81, 41, 17, 4, 2, 47, 54, 21, 68, 78, 4, 77, 3, 66, 55, 99, 42, 62, 39, 8,
//            36, 53, 74, 26, 8, 42, 66, 30, 58, 69, 14, 49, 39, 85, 98, 72, 3, 18, 99, 96, 66, 64, 36, 17, 44, 70, 0, 8, 14, 62,
//            41, 84, 59, 94, 27, 5, 27, 96, 10, 15, 52, 43, 20, 2, 86, 45, 43, 32, 17, 49, 92, 9, 15, 6, 49, 72, 7, 51, 21, 2,
//            26, 63, 82, 98, 48, 21, 96, 16, 83, 20, 37, 28, 45, 76, 9, 40, 52, 36, 40, 98, 35, 77, 26, 7, 39, 21, 4, 68, 67, 58,
//            46, 88, 78, 33, 85, 64, 56, 34, 50, 19, 96, 3, 84, 33, 41, 13, 35, 30, 11, 95, 22, 57, 57, 17, 36, 63, 22, 9, 15, 15,
//            89, 57, 98, 83, 22, 34, 2, 89, 50, 74, 40, 9, 2, 4, 29, 98, 65, 56, 46, 13, 56, 78, 11, 20, 64, 81, 36, 81, 44, 57,
//            28, 83, 12, 14, 42, 54, 5, 5, 9, 80, 75, 36, 3, 83, 37, 9, 78, 75, 72, 56, 52, 96, 79, 21, 13, 85, 20, 30, 23, 48,
//            92, 15, 26, 78, 4, 97, 94, 95, 25, 45, 98, 27, 82, 36, 83, 44, 84, 8, 94, 0, 27, 70, 79, 43, 92, 68, 43, 87, 83, 67,
//            65, 20, 28, 33, 93, 91, 50, 36, 17, 94, 56, 47, 86, 49, 51, 26, 67, 5, 69, 26, 89, 77, 41, 68, 75, 8, 79, 54, 73, 42,
//            15, 28, 37, 98, 78, 72, 49, 93, 66, 57, 0, 4, 48, 82, 83, 6, 15, 27, 36, 53, 46, 99, 19, 23, 35, 69, 10, 52, 92, 52,
//            31, 17, 84, 9, 32, 77, 24, 32, 90, 30, 98, 11, 27, 41, 99, 80, 91, 50, 81, 38, 58, 97, 45, 22, 25, 68, 15, 69, 58, 34,
//            3, 42, 17, 78, 17, 35, 29, 2, 12, 96, 5, 33, 48, 46, 41, 36, 30, 19, 81, 72, 81, 88, 24, 68, 24, 45, 81, 93, 12, 84,
//            83, 31, 15, 80, 72, 24, 50, 13, 57, 37, 39, 49, 92, 89, 20, 3, 99, 73, 53, 4, 21, 46, 72, 95, 15, 31, 56, 70, 93, 90,
//            91, 7, 14, 94, 42, 37, 50, 15, 75, 91, 64, 93, 77, 15, 73, 96, 9, 17, 32, 81, 40, 55, 47, 19, 94, 25, 7, 81, 33, 22,
//            4, 0, 83, 13, 23, 77, 70, 99, 69, 11, 90, 94, 25, 71};
//        // System.out.println(costs.length);
//        edgeBank = generateGraph((int) graphSize,
//                (int) ((graphSize * (graphSize - 1)) / 2), costs);
//
//        // System.out.println(edgeBank);
//    }
//
//    /**
//     * A method to automatically generate a graph
//     *
//     * @param size The number of nodes in the graph to be generated
//     * @param edgeNumber The number of edges to make up the graph
//     * @param costs An array of the costs of the edges numbered in order from
//     * edge (1,2), (1,3), (1,4)... Provide an array whose first element is
//     * negative if random costs are to be generated for the graph
//     * @return graph The generated graph
//     */
//    public static EdgeSet generateGraph(int size, int edgeNumber,
//            double... costs) {
//        EdgeSet graph = new EdgeSet(edgeNumber, EdgeSet.EDGEBANK);
//        int countCosts = 0;
//        Random rand = new Random();
//        bigFor:
//        for (int i = 1; i < size; i++) {
//            for (int j = 1; j <= size; j++) {
//                if (countCosts == edgeNumber) {
//                    break bigFor;
//                }
//                if (i < j) {
//                    String from = "" + i;
//                    String to = "" + j;
//                    if (costs[0] < 0) {
//                        double randCost = 1 + 100 * rand.nextDouble();
//                        if (graph.add(new Edge(from, to, randCost))) {
//                            countCosts++;
//                        }
//                    } else {
//                        if (graph.add(new Edge(from, to, costs[countCosts]))) {
//                            countCosts++;
//                        }
//                    }
//                }
//            }
//        }
//        return graph;
//    }
//
//    /**
//     * Method to create a graph from a text file that contains the edges and
//     * their costs, as well as the number of edges and nodes
//     *
//     * @param f The file to be read
//     * @return Input graph
//     * @throws InvalidFileException if the file does not conform to the expected
//     * format
//     * @throws FileNotFoundException if the file does not exist
//     */
//    public static EdgeSet generateInputGraph(File f)
//            throws InvalidFileException, FileNotFoundException {
//        BufferedReader br;
//        EdgeSet data = null;
//        try {
//            br = new BufferedReader(new FileReader(f));
//            String line = "";
//            Set<String> cityNames = new HashSet<String>();
//            try {
//                graphSize = Properties.getCityNumber();
//                data = new EdgeSet(Properties.getEdgeNumber(), EdgeSet.EDGEBANK);
//            } catch (NumberFormatException e) {
//                br.close();
//                throw new InvalidFileException("Invalid file content format!");
//            } catch (ArrayIndexOutOfBoundsException e) {
//                br.close();
//                throw new InvalidFileException("Invalid file content format!");
//            }
//            while ((line = br.readLine()) != null) {
//                line = line.trim();
//                if (line.equals("<inputGraph>")) {
//                    continue;
//                } else if (line.equals("</inputGraph>")) {
//                    break;
//                } else if (!line.matches(".+:-.+=\\d+\\.*\\d*")) {
//                    System.out.println(line);
//                    br.close();
//                    throw new InvalidFileException("Invalid file contents!");
//                }
//                ArrayList<String> dummyList = new ArrayList<>();
//                int dashIndex = line.indexOf(":-");
//                String from = line.substring(0, dashIndex);
//                int equalIndex = line.indexOf("=");
//                String to = line.substring(dashIndex + 2, equalIndex);
//                double cost = Double
//                        .parseDouble(line.substring(equalIndex + 1));
//                dummyList.add(from);
//                dummyList.add(to);
//                cityNames.add(from);
//                cityNames.add(to);
//                dummyList.add("" + cost);
//                Edge dummyEdge = new Edge(from, to, cost);
//                data.add(dummyEdge);
//            }
//            Properties.setCityNames(cityNames);
//            br.close();
//        } catch (FileNotFoundException e) {
//            throw new FileNotFoundException("File does not exist!");
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (EdgeSetException e) {
//            throw new SizeExceededException("Size Exceeded!");
//        }
//        // System.out.println(data);
//
//        edgeBank = data;
//        return data;
//
//    }
//
//    /**
//     * Method to initialize the swarm, randomly generating {@code int}
//     * <b>particleNo</b> particles, compute the values of {@code double}
//     * <b>Q</b>, {@code double} <b>K</b> and {@code double} <b>T</b>, and
//     * generate random velocities for each particle
//     *
//     * @param swarmSize The {@code int} number of particles to consist the
//     * swarm
//     * @param graphSize The {@code double} size of the graph under
//     * consideration, that is, the number of nodes
//     * @throws EdgeSetException if the edgeBank has not been successfully
//     * created and populated
//     */
//    private void init(int swarmSize) throws EdgeSetException {
//        swarm = new ArrayList<>();
//        Random rand = new Random();
//        edgeBankSize = edgeBank.size();
//        Q = round(1.0 / (edgeBankSize), DP4) * 0.2;
//        T = Q;
//        w = 4;
//        K = round(graphSize / edgeBankSize, DP4);
//        c1 = 2.05;
//        c2 = 2.05;
//        for (int i = 0; i < swarmSize; i++) {
//            EdgeSet dummyEdgeSet = new EdgeSet((int) graphSize,
//                    EdgeSet.PARTICLE);
//            int count = 0;
//            EdgeSet dummyEdgeBank = edgeBank.clone();
//            dummyEdgeBank.sort();
//            int addIndex = 0;
//            while (count < graphSize) {
//                addIndex = rand.nextInt(dummyEdgeBank.getSize());
//                Edge dummyEdge = dummyEdgeBank.getEdge(addIndex);
//                if (dummyEdgeSet.add(dummyEdge)) {
//                    count++;
//                    dummyEdgeBank.removeIndex(addIndex);
//                    //addIndex--;
//                }
//                //addIndex++;
//            }
//            //System.out.println(dummyEdgeSet);
//            //System.out.println("Cost: " + dummyEdgeSet.fitness());
//            swarm
//                    .add(new Particle(dummyEdgeSet, rand.nextInt((int) graphSize)));
//        }
//        computeGBest();
//    }
//
//    // public static void main(String[] args) {
//    // defaultData(4);
//    // Algorithm simulate = new Algorithm();
//    // simulate.doInBackground();
//    // }
//    /**
//     * Method to approximate a given {@code double} value to a given number of
//     * decimal places
//     *
//     * @param d the number to be approximated
//     * @param numbersAfterDecimalPoint the number of decimal places to be
//     * approximated to
//     * @return the approximate value of <b>d</b> to
//     * <b>numbersAfterDecimalPoint</b> decimal places
//     */
//    public static double round(double d, int numbersAfterDecimalPoint) {
//        double n = Math.pow(10, numbersAfterDecimalPoint);
//        double d2 = d * n;
//        long lon = (long) d2;
//        lon = ((long) (d2 + 0.5) > lon) ? lon + 1 : lon;
//        return (lon) / n;
//    }
//
//    /**
//     * The method which runs the algorithm, creating a random complete graph of
//     * n nodes, and running the algorithm 50 times so as to see the
//     * validity/distribution of the gbests that were gotten each time.
//     *
//     * @return
//     */
//    // @Override
//    @Override
//    public EdgeSet doInBackground() {
//        long inStartTime = System.currentTimeMillis();
//        progress = 0;
//        double minGbest = Double.MAX_VALUE;
//        bests = new ArrayList<>();
//        int noOfIterations = 0;
//        numberOfRuns = Properties.getPrecision();
//        for (int j = 0; j < numberOfRuns; j++) {
//            edgeBank.reset();
//            System.out.println("Iteration: " + j);
//            double swarmSize = round((graphSize), 0);
//            init((int) swarmSize);
//            int k = 0;
//            do {
//                if (!running) {
//                    break;
//                }
//                synchronized (this) {
//                    while (isPaused()) {
//                        try {
//                            wait();
//                        } catch (InterruptedException e1) {
//                            e1.printStackTrace();
//                        }
//                    }
//                }
//                noOfIterations++;
//                k++;
//                double r1 = round(Math.random(), 1), r2 = round(Math.random(),
//                        1);
//                /** Implementing Linear Decreasing Inertia Weight **/
//                double initW = 5;
//                double finalW = 1;
//                double maxT = graphSize * 23;
//                
//                //w -= 1 / (graphSize * 30);
//                for (Particle swarm1 : swarm) {
//                    currentParticle = swarm1;
//                    List<Integer> diff1 = Particle.subtractPositions(
//                            currentParticle.getPbest(), currentParticle
//                            .getPosition()); // X(i) - pbest(i)
//                    List<Integer> diff2 = Particle.subtractPositions(gbest,
//                            currentParticle.getPosition());// X(i) - gbest
//                    int scalarDiff1 = (int) round(c1 * r1 * diff1.size(), 0); // approximate
//                    // result of c1*r1*(X(i) - pbest(i))
//                    int scalarDiff2 = (int) round(c2 * r2 * diff2.size(), 0); // approximate
//                    // result of c2*r2*(X(i) - gbest)
//                    int prevVelocity = currentParticle.getVelocity();
//                    
//                    w = (initW - finalW) * (maxT - k)/maxT + finalW; // Linear Decreasing Inertia Weight
//                    double newVelocity = w * prevVelocity + scalarDiff1
//                            + scalarDiff2; // Computing V(i+1)
//                    
//                    newVelocity *= 0.729; // Constriction Coefficient
//                    currentParticle.setVelocity((int) round(newVelocity, 0)); // Setting
//                    // V(i+1) as particle i's new velocity
//                    /*--- If newVelocity exceeds the limit of graphSize, do a little intelligent velocity clamping ---*/
//                    EdgeSet g = computeNewPosition(currentParticle, diff1,
//                            scalarDiff1, diff2, scalarDiff2); // X(i+1) = X(i) +
//                    double newFitness = g.fitness();
//                    if (newFitness < currentParticle.fitness()) {
//                        for (Edge e : g) {
//                            e.setGood(e.getGood() + 1);
//                        }
//                    }
//                    if (newFitness > currentParticle.fitness()) {
//                        for (Edge e : g) {
//                            e.setBad(e.getBad() + 1);
//                        }
//                    }
//                    for (Edge e : g) {
//                        e.setUseFrequency(e.getUseFrequency() + 1);
//                    }
//                    currentParticle.setPosition(g);
//                    if (newFitness < currentParticle.getPbest().fitness()) {
//                        currentParticle.setPbest(g);
//                    }
//                }
//                /*--- Communicate: ---*/
//                double max = 0.0;
//                for (Edge e : edgeBank) {
//                    if (e.getGood() >= (round(e.getUseFrequency() / 2.0, 0))
//                            && e.getUseFrequency() != 0) {
//                        e.setProbability(e.getProbability() + Q);
//                    } else if (e.getBad() >= (round(e.getUseFrequency() / 2.0, 0))
//                            && e.getUseFrequency() != 0) {
//                        e.setProbability(e.getProbability() - Q);
//                    }
//                    if (max < e.getProbability()) {
//                        max = e.getProbability();
//                    }
//                }
//                T = max;
//                computeGBest();
//            } while (!checkTermination(gbest.fitness()));
//            
//            checkResults.clear();
//            if (gbest.fitness() < minGbest) {
//                minGbest = gbest.fitness();
//            }
//            bests.add(gbest);
//        }
//       int bestCount = 0;
//        int bestIndex = 0;
//        double diffSum = 0;
//        double averageBest = 0;
//        double worstBest = Double.MIN_VALUE;
//        double bestSum = 0;
//        for (int i = 0; i < 50; i++) {
//            double currentBest = bests.get(i).fitness();
//            if (currentBest == minGbest) {
//                bestCount++;
//                bestIndex = i;
//            }
//            if (currentBest > worstBest) {
//                worstBest = currentBest;
//            }
//            diffSum += bests.get(i).fitness() - minGbest;
//            bestSum += bests.get(i).fitness();
//            System.out.println(bests.get(i).fitness());
//        }
//
//        averageBest = bestSum / 50;
//        double minDev = diffSum / 50;
//        double totalTime = round(
//                ((System.currentTimeMillis() - inStartTime) / 1000.0), 4);
//        double avgTime = round(totalTime / numberOfRuns, DP4);
//        System.out.println(avgTime);
//        System.out.println(bestCount);
//        double avgIterations = round(noOfIterations / numberOfRuns, DP4);
//        System.out.println(minDev);
//        System.out.println(bests.get(bestIndex).fitness());
//        System.out.println(worstBest);
//        System.out.println(averageBest);
//        System.out.println(avgIterations);
//
//        return bests.get(bestIndex);
//    }
//        
//    public static EdgeSet printResults(File f, double minGbest, long inStartTime, int noOfIterations){
//        Charset charset = Charset.forName("US-ASCII");
//        File inFile = new File(f.getPath());
//        String s = "";
//        int bestIndex = 0;
//        try {
//            BufferedWriter writer = Files.newBufferedWriter(inFile.toPath(),
//                    charset);
//            int bestCount = 0;
//            
//            double diffSum = 0;
//            double averageBest = 0;
//            double worstBest = Double.MIN_VALUE;
//            double bestSum = 0;
//            for (int i = 0; i < 50; i++) {
//                double currentBest = bests.get(i).fitness();
//                if (currentBest == minGbest) {
//                    bestCount++;
//                    bestIndex = i;
//                }
//                if (currentBest > worstBest) {
//                    worstBest = currentBest;
//                }
//                diffSum += bests.get(i).fitness() - minGbest;
//                bestSum += bests.get(i).fitness();
//                System.out.println(bests.get(i).fitness());
//            }
//            averageBest = bestSum / 50;
//            double minDev = diffSum / 50;
//            double totalTime = round(
//                    ((System.currentTimeMillis() - inStartTime) / 1000.0), 4);
//            double avgTime = round(totalTime / numberOfRuns, DP4);
//            writer.write("Average time: " + avgTime);
//            writer.newLine();
//            writer.write("Number of bests: " + bestCount);
//            writer.newLine();
//            double avgIterations = round(noOfIterations / numberOfRuns, DP4);
//            writer.write("Minimum Deviation: " + minDev);
//            writer.newLine();
//            writer.write("Best: " + bests.get(bestIndex).fitness());
//            writer.newLine();
//            writer.write("Worst: " + worstBest);
//            writer.newLine();
//            writer.write("Mean: " + averageBest);
//            writer.newLine();
//            writer.write("Average number of Iterations: " + avgIterations);
//            writer.newLine();
//            writer.newLine();
//        } catch (IOException ex) {
//            Logger.getLogger(Algorithm_Pro.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return bests.get(bestIndex);
//    }
//
//    @Override
//    public void registerInterest(Observer obs) {
//        observers.add(obs);
//    }
//
//    public void suspend() {
//        setPaused(true);
//    }
//
//    public void resume() {
//        setPaused(false);
//        notify();
//    }
//
//    public static void saveGraph(EdgeSet graph, EdgeSet best, File f) {
//        Charset charset = Charset.forName("US-ASCII");
//        File inFile = new File(f.getPath());
//        String s = "";
//        try {
//            BufferedWriter writer = Files.newBufferedWriter(inFile.toPath(),
//                    charset);
//            writer.write("<InputGraph>");
//            writer.newLine();
//            for (Edge e : graph) {
//                s = e.getFrom() + ":-" + e.getTo() + "=" + e.getCost();
//                writer.write(s);
//                writer.newLine();
//            }
//            writer.write("</InputGraph>");
//            writer.newLine();
//            writer.write("<OutputGraph>\n");
//            writer.newLine();
//            for (Edge e : best) {
//                s = e.getFrom() + ":-" + e.getTo() + "=" + e.getCost();
//                writer.write(s);
//                writer.newLine();
//            }
//            writer.write("</OutputGraph>");
//            writer.newLine();
//            writer.write("<SimulationData>\n");
//            writer.newLine();
//            for (String d : values) {
//                writer.write(d);
//                writer.newLine();
//            }
//            writer.write("</SimulationData>");
//            writer.newLine();
//
//        } catch (IOException x) {
//            System.err.format("IOException: %s%n", x);
//        }
//    }
//
//    public static void main(String args[]) {
//       // Algorithm_Pro proc = new Algorithm_Pro();
//        //edgeBank = generateGraphXML(new File("C:\\Users\\Peter\\Documents\\NetBeansProjects\\Prospective_ECDPSO\\src\\controller\\eil51.xml"));
//        //graphSize = 51;
//        //System.out.println(bank);
//        //proc.doInBackground();
//        //generateRandomInput(30);
//        Random r = new Random();
//        for (int i = 0; i < 50; i++) {
//            System.out.println(round(Math.pow(2.5, r.nextGaussian()), 1));
//        }
//    }
//
//    public static void generateRandomInput(int n) {
//        Random rand = new Random(System.currentTimeMillis());
//        int limit = (n * (n - 1)) / 2;
//        int newLine = 30;
//        for (int i = 0; i < limit; i++) {
//            System.out.print(rand.nextInt(100) + ", ");
//            if ((i % newLine) == 0) {
//                System.out.println();
//            }
//        }
//    }
//
//    @SuppressWarnings("empty-statement")
//    public static EdgeSet generateGraphXML(File xmlFile) {
//        BufferedReader br;
//        EdgeSet data = null;
//        try {
//            br = new BufferedReader(new FileReader(xmlFile));
//            String line = "";
//            try {
//                graphSize = Properties.getCityNumber();
//                data = new EdgeSet((51*50 / 2), EdgeSet.EDGEBANK);
//                while (!(line = br.readLine().trim()).contains("graph")) {
//                }
//                int i = 0;
//                while ((line = br.readLine()).trim().equals("<vertex>")) {
//                    if (i == 0) {
//                        line = br.readLine();
//                    }
//                    if (line.trim().equals("<vertex>")) {
//                        line = br.readLine();
//                    }
//                    for (; (!line.contains("</vertex>"));) {
//                        Pattern costPattern = Pattern.compile("\\d+\\..+\"", Pattern.CASE_INSENSITIVE);
//                        Matcher costMatcher = costPattern.matcher(line);
//                        costMatcher.find();
//                        String costString = costMatcher.group();
//                        costString = costString.substring(0, costString.length() - 1);
//                        double cost = Double.parseDouble(costString);
//                        Pattern cityNamePattern = Pattern.compile(">\\d+");
//                        Matcher cityNameMatcher = cityNamePattern.matcher(line);
//                        cityNameMatcher.find();
//                        String toCity = cityNameMatcher.group();
//                        toCity = toCity.substring(1);
//                        Edge toAdd = new Edge(i + "", toCity, cost);
//                        data.add(toAdd);
//                        line = br.readLine();
//                    }
//                    i++;
//                }
//            } catch (NumberFormatException | IOException e) {
//                Logger.getLogger(Algorithm_Pro.class.getName()).log(Level.SEVERE, null, e);
//            }
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(Algorithm_Pro.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return data;
//    }
//}