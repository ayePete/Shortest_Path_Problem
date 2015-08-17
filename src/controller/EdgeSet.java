package controller;

/**
 * @author Peter Popoola
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import exception.EdgeSetException;
import exception.InconsistentStateException;
import exception.SizeExceededException;

public class EdgeSet implements Iterator<Edge>, Iterable<Edge> {
	private List<Edge> edgeList;
	private final List<Edge> incompleteList;
	private final int type;
	public static final int PARTICLE = 0;
	public static final int EDGEBANK = 1;
	public static final int MAX_VALUE = 1000000;
	
	public static boolean isHamiltonianCycle(List<Edge> edges, Edge dum) {
		Edge dummy = dum.clone();
		IterableEdgeList dummyList = new IterableEdgeList(edges);
		String dummyString = "";
		String destination = "";
		Edge nextEdge = dummyList.next();
		int count = 0;
		boolean found = false;
		while (dummyList.hasNext()) {
			if (!found) {
				if (count > dummyList.size())
					break;
				if (dummy.getFrom().equals(nextEdge.getFrom())) {
					dummyString = nextEdge.getTo();
					destination = dummy.getTo();
					dummyList.remove();
					found = true;
					count = 0;
				} else if (dummy.getFrom().equals(nextEdge.getTo())) {
					dummyString = nextEdge.getFrom();
					destination = dummy.getTo();
					dummyList.remove();
					found = true;
					count = 0;
				} else if (dummy.getTo().equals(nextEdge.getFrom())) {
					dummyString = nextEdge.getTo();
					destination = dummy.getFrom();
					dummyList.remove();
					found = true;
					count = 0;
				} else if (dummy.getTo().equals(nextEdge.getTo())) {
					dummyString = nextEdge.getFrom();
					destination = dummy.getFrom();
					dummyList.remove();
					found = true;
					count = 0;
				}
				nextEdge = dummyList.next();
				count++;
			} else {
				if (count > dummyList.size())
					break;
				if (nextEdge.getFrom().equals(dummyString)) {
					dummyString = nextEdge.getTo();
					dummyList.remove();
					count = 0;
				} else if (nextEdge.getTo().equals(dummyString)) {
					dummyString = nextEdge.getFrom();
					dummyList.remove();
					count = 0;
				}
				if (dummyString.equals(destination))
					return true;
				if (!dummyList.hasNext())
					break;
				nextEdge = dummyList.next();
				count++;
			}
		}
		return false;
	}
	
	private int expectedSize;
	
	/**
	 * @return {@code int} the expectedSize
	 */
	public int getExpectedSize() {
		return expectedSize;
	}
	
	private int index;
	private double averageCost;
	
	/**
	 * @return {@code double} the averageCost
	 */
	public double getAverageCost() {
		return averageCost;
	}
	
	/**
	 * @param <b>averageCost</b> the {@code double} averageCost to set
	 */
	public void setAverageCost(double averageCost) {
		this.averageCost = averageCost;
	}
	
	/**
	 * EdgeSet Constructor.
	 * 
     * @param expectedSize
     * @param type
	 * @param {@code int} <b>expectedSize:</b> The expected size of the EdgeSet
	 * @param {@code int} <b>type:</b> The type of EdgeSet, EDGEBANK or PARTICLE
	 */
	public EdgeSet(int expectedSize, int type) {
		incompleteList = new ArrayList<>();
		if (expectedSize == MAX_VALUE)
			edgeList = new ArrayList<>();
		this.expectedSize = expectedSize;
		this.type = type;
		index = 0;
	}
	
	public EdgeSet() {
		this(MAX_VALUE, EDGEBANK);
	}
	
	/**
	 * Method to add an edge to this EdgeSet, preventing duplicate edges, and in
	 * the case of <br>
	 * a PARTICLE, more than 2 nodes being in the edgeSet, as well as premature
	 * Hamiltonian cycles.
	 * 
	 * @param e
	 *            The edge to be added.
	 * @return {@code false}, if the addition is not successful, and
	 *         {@code true} if it is.
	 * @throws SizeExceededException
	 *             if the attempted addition will result in the specified
	 *             expectedSize being exceeded
	 */
	public boolean add(Edge e) throws EdgeSetException {
		int from = 0;
		int to = 0;
		for (Edge e1 : incompleteList) {
			if (e1.equals(e))
				return false;
			if (e.getFrom().equals(e1.getFrom())) {
				from++;
			}
			if (e.getFrom().equals(e1.getTo())) {
				from++;
			}
			if (e.getTo().equals(e1.getFrom())) {
				to++;
			}
			if (e.getTo().equals(e1.getTo())) {
				to++;
			}
		}
		
		if ((from >= 2 || to >= 2) && type == PARTICLE)
			return false;
		if (incompleteList.size() >= expectedSize)
			throw new SizeExceededException(
				"The specified expected size has been exceeded!");
		if (incompleteList.size() < expectedSize - 1
			&& incompleteList.size() > 1 && type != EDGEBANK) {
			if (isHamiltonianCycle(incompleteList, e)) {
				return false;
			}
		}
		
		incompleteList.add(e);
		if (expectedSize == MAX_VALUE)
			edgeList.add(e);
		else {
			if (incompleteList.size() == expectedSize) {
				edgeList = new ArrayList<>();
				for (Edge e1 : incompleteList) {
					edgeList.add(e1);
				}
			}
		}
		return true;
	}
	
	/**
	 * Method to remove all edges in this EdgeSet
	 */
	public void clearAll() {
		edgeList.clear();
		incompleteList.clear();
	}
	
	@Override
	/**
	 * Method that returns a copy of this edgeSet
	 * @throws InconsistentStateException if the edgeSet has not been successfully created, that is, <br>
	 * if the addition of edges has not been sufficient to make the size of this <br>
	 * EdgeSet to be up to the specified expectedSize
	 */
	public EdgeSet clone() throws InconsistentStateException {
		validate();
		EdgeSet pos = new EdgeSet(expectedSize, type);
		for (Edge e : edgeList) {
			try {
				pos.add(e);
			} catch (EdgeSetException e1) {
				e1.printStackTrace();
			}
		}
		return pos;
	}
	
	/**
	 * Method to compute the fitness value of <b>{@code this}</b> EdgeSet
	 * 
	 * @return {@code double} fitness value of this edgeSet
	 * @throws InconsistentStateException
	 *             if the edgeSet has not been successfully created, that is, <br>
	 *             if the addition of edges has not been sufficient to make the
	 *             size of this <br>
	 *             EdgeSet to be up to the specified expectedSize
	 */
	public double fitness() throws InconsistentStateException {
		validate();
		double sum = 0.0;
		for (Edge e : edgeList) {
			sum += e.getCost();
		}
		return sum;
	}
	
	/**
	 * Method to return the ith edge in this edgeSet
	 * 
	 * @param {@code int} <b>i:</b> index of the edge to be returned
	 * @return {@code Edge} the ith edge
	 */
	public Edge getEdge(int i) {
		return edgeList.get(i);
	}
	
	public List<Edge> getList() {
		return edgeList;
	}
	
	@Override
	public boolean hasNext() {
		return (incompleteList.size() != 0);
	}
	
	public void init() {
		incompleteList.clear();
		for (Edge e1 : edgeList) {
			incompleteList.add(e1);
		}
	}
	
	@Override
	public Iterator<Edge> iterator() throws InconsistentStateException {
		validate();
		return edgeList.iterator();
	}
	
	@Override
	public Edge next() {
		if (index > expectedSize)
			index = 0;
		Edge dummy = incompleteList.get(index);
		index++;
		return dummy;
	}
	
	@Override
	public void remove() {
		edgeList.remove(index);
	}
	
	public void removeEdge(Edge e) {
		edgeList.remove(e);
		
	}
	
	public boolean removeIndex(int i) {
		edgeList.remove(i);
		return true;
	}
	
	public void reset() {
		double totalCost = 0;
		for (Edge e : edgeList) {
			totalCost += e.getCost();
			e.setBad(0);
			e.setGood(0);
			e.setUseFrequency(0);
			e.setProbability(0.0);
		}
		
		averageCost = totalCost / expectedSize;
		
	}
	
	/**
	 * Method to return the size of this EdgeSet.
	 * 
	 * @return the {@code int} size of this EdgeSet if it has been successfully
	 *         created, and 0 otherwise.
	 */
	public int size() {
		if (edgeList == null)
			return 0;
		return edgeList.size();
	}
	
	@Override
	public String toString() {
		if (expectedSize != MAX_VALUE)
			validate();
		String out = "{";
		for (Edge e : edgeList) {
			out += e.getCost() + e.toString() + ", ";
		}
		out = out.substring(0, out.length() - 2);
		out += "}";
		return out;
	}
	
	/**
	 * Method that ensures that the EdgeSet is in a consistent state
	 * 
	 * @throws InconsistentStateException
	 *             if the edgeSet has not been successfully created, that is, if
	 *             the addition of edges has not been sufficient to make the
	 *             size of this EdgeSet to be up to the specified expectedSize
	 */
	private void validate() throws EdgeSetException {
		try {
			// for(Edge e: edgeList)
			// System.out.print(e + " ");
			if (edgeList.size() < expectedSize)
				throw new InconsistentStateException(
					"Number of edges not equal to required. Expected: "
						+ expectedSize + ". Current size: " + edgeList.size()
						+ ".");
		} catch (NullPointerException e) {
			throw new InconsistentStateException(
				"Invalid EdgeSet. The edgeList has not been successfully created. Expected: "
					+ expectedSize + ". Current size: " + incompleteList.size()
					+ ".");
		}
	}
	
	public int getSize() {
		if (edgeList == null) {
			return incompleteList.size();
		}
		return edgeList.size();
	}
	
	public void setExpectedSize(int expect) {
		expectedSize = expect;
	}
        
        public void sort(){
            edgeList.sort(null);
        }
}
