package main;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.ViewerListener;
import org.graphstream.ui.swingViewer.ViewerPipe;
/**
 * Listener to handle click of nodes.
 * The custom graph stream library uses a tight loop to pump mouse click events
 * to the graph stream lib, but we take a signal based approach to this by using hte native java 
 * mouse listener, and on each event trigger the pump to the graph stream library
 * @author brandon
 *
 */
public class NodeClickListener implements ViewerListener , MouseInputListener{
	public boolean loop = true;
	private ViewerPipe vpipe = null;
	private View vw = null;
	private Graph graph = null;

	/**
	 * Constructor
	 * @param vpipe - Viewer Pipe of the graph UI
	 * @param vw - View of the current graph in swing
	 * @param g - graph object for the current graph in use
	 */
	public NodeClickListener(ViewerPipe vpipe, View vw, Graph g) {
		this.loop=true;
		this.vpipe = vpipe;
		this.vw = vw;
		this.graph = g;
		// Keep piping back while grph is out to hook mouse clicks
		this.vw.addMouseListener(this);

	}


	/**
	 * Close the view when graph is no longer needed and detach all listners
	 * @param id - not used, but inherited by interface
	 */
	public void viewClosed(String id) {
		loop = false;
		vw.removeMouseListener(this);


	}
	/**
	 * Button push hook to label nodes/edges
	 * @param id - string id of node
	 */
	public void buttonPushed(String id) {
		System.out.println("Button pushed on node "+id);
		Node n = graph.getNode(id);
		String _ui_label = n.getAttribute("_ui.label");
		String ui_label = n.getAttribute("ui.label"); 

		// if not set toggle on node and adj edges
		if (ui_label==null || ui_label.equals(""))
		{
			n.setAttribute("ui.label", _ui_label);

			// Label adjacent edges
			labelAdjacentEdges(n);

		}
		else // Toggle node off and adj unbound edges
		{
			n.setAttribute("ui.label", "");
			unlabelAdjacentEdges(n);
		}
	}

	/**
	 * labels adjacent edges of the given node 
	 * @param n - node to label adjacent edges of
	 */
	private void labelAdjacentEdges(Node n)
	{
		for (Edge e : n.getEdgeSet()){
			String _ui_label = e.getAttribute("_ui.label");
			String ui_label = e.getAttribute("ui.label"); 

			if (ui_label==null || ui_label.equals(""))
			{
				e.setAttribute("ui.label", _ui_label);

			}
		}
	}

	/**
	 * unlabels all edges connected to node which are no longer
	 * connected to any other labeled node
	 * @param n - the node to apply to adjacent edges
	 */
	private void unlabelAdjacentEdges(Node n)
	{
		for (Edge e : n.getEdgeSet()){
			Node v1 = e.getNode0();
			Node v2 = e.getNode1();

			String ui_label_v1 = v1.getAttribute("ui.label");
			String ui_label_v2 = v2.getAttribute("ui.label");


			if ((ui_label_v1==null || ui_label_v1.equals(""))
					&& (ui_label_v2==null || ui_label_v2.equals("")))
			{
				e.setAttribute("ui.label", "");
			}
		}

	}



	@Override
	/**
	 * Mouse release event to pump on release
	 */
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		vpipe.pump();
	}


	/**
	 * Inherited function unused
	 */
	public void buttonReleased(String id) {

	}




	/**
	 * Inherited function unused
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * Inherited function unused
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * Inherited function unused
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * Inherited function unused
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		//vpipe.pump();
		//System.out.println("Pump it!");
	}


	/**
	 * Inherited function unused
	 */
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * Inherited function unused
	 */
	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}


}
