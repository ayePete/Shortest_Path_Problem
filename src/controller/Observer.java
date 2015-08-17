package controller;

import java.util.ArrayList;

public interface Observer {
	//Tell the subject you are interested in changes
	public void sendNotify(ArrayList<EdgeSet> e);

	public void done(String[] values);
}
