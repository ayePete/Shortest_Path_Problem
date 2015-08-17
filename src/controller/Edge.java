package controller;

public class Edge implements Cloneable, Comparable {
	private double cost;
	private int useFrequency;
	private int good;
	private int bad;
	private String from;
	private String to;
	private double probability;

	public Edge() {
		this(null, null);
	}

	public Edge(String from, String to) {
		this(from, to, 0);
	}

	public Edge(String from, String to, double cost) {
		useFrequency = 0;
		good = 0;
		bad = 0;
		this.from = from;
		this.to = to;
		this.cost = cost;
		probability = 0.0;
	}

	/**
	 * Method to get a copy of the calling edge
	 * 
	 * @return a copy of this edge
	 */
	@Override
	public Edge clone() {
		Edge dummy = new Edge();
		dummy.setFrom(from);
		dummy.setTo(to);
		dummy.setBad(bad);
		dummy.setCost(cost);
		dummy.setGood(good);
		dummy.setProbability(probability);
		dummy.setUseFrequency(useFrequency);
		return dummy;
	}

	/**
	 * Does transitive comparison of edges for symmetric graphs to see if they
	 * are equal
	 * 
	 * @param comparedTo
	 *            the second edge being compared to
	 * @return if the both edges being compared are equal
	 */
	public boolean equals(Edge comparedTo) {
		return ((from.equals(comparedTo.getFrom()) && to.equals(comparedTo
				.getTo())) || (to.equals(comparedTo.getFrom()) && comparedTo
						.getTo().equals(from)));
	}

	/**
	 * @return bad the 'badness' index of this Edge
	 */
	public int getBad() {
		return bad;
	}

	/**
	 * @return the cost of this Edge
	 */
	public double getCost() {
		return cost;
	}

	/**
	 * @return <b> {@code from:} </b> the source vertex of this Edge
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @return good the 'goodness' index of this Edge
	 */
	public int getGood() {
		return good;
	}

	/**
	 * 
	 * @return the probability associated with this edge
	 */
	public double getProbability() {
		return probability;
	}

	/**
	 * @return to the destination vertex of this edge
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @return useFrequency the number of particles that have used this edge
	 *         within one iteration
	 */
	public int getUseFrequency() {
		return useFrequency;
	}

	/**
	 * @param bad
	 *            the 'badness' index of this Edge
	 */
	public void setBad(int bad) {
		this.bad = bad;
	}

	/**
	 * @param cost
	 *            the cost of this Edge
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}

	/**
	 * @param from
	 *            the source vertex of this Edge
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @param good
	 *            the 'goodness' index of this Edge
	 */
	public void setGood(int good) {
		this.good = good;
	}

	/**
	 * @param the
	 *            probability to be set
	 */
	public void setProbability(double probability) {
		probability = Algorithm_Pro.round(probability, 4);
		if (probability < 0)
			this.probability = 0;
		else
			this.probability = probability;
	}

	/**
	 * @param to
	 *            set the destination vertex of this Edge
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @param useFrequency
	 *            set the use frequency of this Edge
	 */
	public void setUseFrequency(int useFrequency) {
		this.useFrequency = useFrequency;
	}

	@Override
	public String toString() {
		return "(" + from + ", " + to + ")";
	}

    @Override
    public int compareTo(Object o) {
        Edge comp = (Edge)o;
        if(this.cost > comp.cost)
            return 1;
        else if(this.cost < comp.cost)
            return -1;
        else
            return 0;
    }
}