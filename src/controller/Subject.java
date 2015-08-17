package controller;

public interface Subject {
	//Notify the Observers that a change has taken place
	public void registerInterest(Observer obs);
}
