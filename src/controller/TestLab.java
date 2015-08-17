//package controller;
//
//import java.awt.Color;
//import java.awt.Graphics2D;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//
//import javax.imageio.ImageIO;
//import javax.swing.BorderFactory;
//import javax.swing.JButton;
//
//import exception.InvalidFileException;
//
//public class TestLab {
//	
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		
//		String path = "C:\\Users\\PETER\\Desktop\\bloat.png";
//		BufferedImage outImage = new BufferedImage(250, 250,
//			BufferedImage.TYPE_INT_ARGB);
//		JButton jap = new JButton("Start");
//		jap.setBackground(Color.WHITE);
//		jap.setBorder(BorderFactory.createLineBorder(Color.BLUE));
//		Graphics2D imgGraphic = outImage.createGraphics();
//		jap.paint(outImage.getGraphics());
//		try {
//			ImageIO.write(outImage, "png", new File(path));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		System.out.println("Done!");
//		/*
//		 * EdgeSet edges1 = new EdgeSet(3); Edge e1 = new Edge("v1", "v2", 3);
//		 * edges1.add(e1);
//		 * 
//		 * Edge e4 = new Edge("v1", "v3", 1); edges1.add(e4);
//		 * 
//		 * Edge e2 = new Edge("v2", "v3", 2); edges1.add(e2);
//		 * 
//		 * Edge e3 = new Edge("v3", "v1", 1); edges1.add(e3);
//		 * 
//		 * System.out.println("Edges 1: \n" + edges1);
//		 */
//		// System.out.printf("Number in 2 dp: %.2f\n", 3.3984);
//		// EdgeSet graph = new EdgeSet(10, EdgeSet.EDGEBANK);
//		/*
//		 * Random rand = new Random(); for(int i = 1; i < 6; i++){ for(int j =
//		 * 1; j < 6; j++){ if(i < j){ String from = "" + i; String to = "" + j;
//		 * try { graph.add(new Edge(from, to, rand.nextInt(10))); } catch
//		 * (EdgeSetException e) { // TODO Auto-generated catch block
//		 * e.printStackTrace(); } catch (UnconnectedEdgeException e) { // TODO
//		 * Auto-generated catch block e.printStackTrace(); } } } }
//		 * 
//		 * //System.out.println("Graph: \n" + graph);
//		 * //System.out.println(graph.size()); EdgeSet particle1 = new
//		 * EdgeSet(5, EdgeSet.PARTICLE); while(particle1.size() < 5){ try{ try {
//		 * particle1.add(graph.getEdge(rand.nextInt(10))); } catch
//		 * (UnconnectedEdgeException e) { // TODO Auto-generated catch block
//		 * e.printStackTrace(); } } catch(EdgeSetException e){ break; } }
//		 * 
//		 * //System.out.println("Particle 1: " + particle1);
//		 * System.out.println((int)((double)5 * 3/4.0));
//		 */
//	}
//	
//	public static void testGen(File f) {
//		try {
//			Algorithm_Pro.generateInputGraph(f);
//		} catch (InvalidFileException e) {
//			e.printStackTrace();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//	}
//}
