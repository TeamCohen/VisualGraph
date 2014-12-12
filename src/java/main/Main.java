package main;

import style.*;
import gsim.GraphSim;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;




public class Main {

	public static void main(String[] args) {
		//This is what is run when the program
		//All of the code for the GUI is in ControlUI
		ControlUI mainUI = new ControlUI();

	}


	public static void misc()
	{
		// TODO Auto-generated method stub
		//This code does not run
		//The only reason I didn't delete it was in case I wanted to go back and
		//look at the code as an example for future code

		Graph graph = new SingleGraph("Tutorial 1");

		System.out.println(graph.isAutoCreationEnabled());

		graph.setAutoCreate(true);
		System.out.println(graph.isAutoCreationEnabled());
		graph.addNode("A");

		graph.addEdge("AB",1, 2 ,true);
		//graph.addEdge( "BC", "B", "C" );
		//graph.addEdge( "CA", "C", "A" );
		System.exit(0);



		/*
				Graph graph = new SingleGraph("Tutorial 1");
				graph.addNode("A" );
				graph.addNode("B" );
				graph.addNode("C" );


				graph.addEdge( "AB", "A", "B" );
				graph.addEdge( "BC", "B", "C" );
				graph.addEdge( "CA", "C", "A" );



			        graph.setAutoCreate(true);
			        graph.setStrict(false);
			        graph.display();

			        graph.addEdge("AB", "A", "B");
			        graph.addEdge("BC", "B", "C");
			        graph.addEdge("CA", "C", "A");
			        graph.addEdge("AD", "A", "D");
			        graph.addEdge("DE", "D", "E");
			        graph.addEdge("DF", "D", "F");
			        graph.addEdge("EF", "E", "F");


				//graph.display();

		 */
		/* Node Properties */
		Node A = graph.getNode("A");
		A.setAttribute("weight",5);


		System.out.println(A.getAttribute("weight"));

		A = graph.getNode("A");		
		System.out.println(A.getAttribute("weight"));


		Edge AB = graph.getEdge("AB");


		System.out.println("Degree of A: " + A.getDegree());



		//A.setAttribute("ui.class", "marked");

		/* Node Iteration */
		Iterator<Node> nodes_iter = graph.getNodeIterator();

		while(nodes_iter.hasNext())
		{

			Node n = nodes_iter.next();
			System.out.println(n);
			n.addAttribute("ui.label", "LL_"+ n.toString());
		}
		Collection<Node> nodes = graph.getNodeSet();


		/* Styling */		
		String style = StyleImporter.getStyle("graph_style.css");
		graph.setAttribute("ui.stylesheet", style);
		//System.out.println("Graph Style: " + graph.getAttribute("ui.stylesheet"));



		// Graph display and explore
		graph.display();

	}

}