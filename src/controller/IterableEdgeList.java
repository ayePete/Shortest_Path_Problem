package controller;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import controller.Edge;

public class IterableEdgeList implements Iterator<Edge> {
	private List<Edge> list;
	private int index;

	public IterableEdgeList(List<Edge> listOfEdges) {
		list = new LinkedList<Edge>();
		for (int i = 0; i < listOfEdges.size(); i++) {
			list.add(listOfEdges.get(i));
		}
		index = 0;
	}

	@Override
	public boolean hasNext() {
		return (list.size() != 0);
	}

	@Override
	public Edge next() {
		if (index > list.size() - 1)
			index = 0;
		Edge dummy = list.get(index);
		index++;
		return dummy;

	}

	@Override
	public void remove() {
		index--;
		if (index > list.size() - 1)
			index = 0;
		list.remove(index);
	}

	public int size() {
		return list.size();
	}

}
