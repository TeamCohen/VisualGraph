package gsim;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.swingViewer.Viewer;

import style.StyleImporter;

import java.io.*;

import javax.swing.text.View;

/**
 * The Graph Simulator object. This should be the object that the caller
 * should interface with.
 * 
 * To use this class:
 * 1) create an object
 * 2) (optional) importGraph - import a graph 
 * 3) (optional) display - display graph
 * 4) (optional) compute - Starts worker thread to simulate graph animations
 * 5) (optional) importEvents/exportEvents - import/export events for worker
 * thread
 * 
 * 
 * 
 * *) Run actions
 * - addVertex
 * - addEdge
 * - ppr_teleport
 * - ppr_go
 * - etc.
 * 
 * @author brandon
 *
 */
public class GraphSim { 
	private Graph graph;
	private GraphSimAlgorithm galgo;


	/**
	 * Creates a new Graph Simulator Object
	 * @param graph_name
	 */
	public GraphSim(String graph_name) {
		this.graph = new SingleGraph(graph_name);
	}


	/**
	 * display() displays the graph by rendering the UI with the graph from the
	 * graphstream library
	 */
	public void display() {
		Viewer vwr = graph.display();

		// Set it so program doesnt abort when close window 
		vwr.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);

	}

	public Viewer get_display()
	{	

		Viewer viewer = new Viewer(graph,Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		viewer.enableAutoLayout();
		//return null;
		return viewer;
		//graph.display(); 
	}

	/**
	 * Initializes the simulation
	 */
	public void initSim() {
		// Set Graph Style
		String style = StyleImporter.getStyle("graph_style.css");
		graph.setAttribute("ui.stylesheet", style);

		// Set edge to autocreate
		graph.setAutoCreate(true);

		// Jumpstart algo
		galgo = new GraphSimAlgorithm();
		galgo.init(graph);

	}

	/**
	 * Processes the animation queue. If not run, the queue is not
	 * served and the user can store the queue at the end of performing
	 * all the actions.
	 */
	public void process() {
		galgo.compute();
	}

	/**
	 * Import events from a serialized SimJob Array file.
	 * @param f - File to import events from. Should be a serialized 
	 * format
	 */
	public void importEvents(File f) {
		SimJob[] jobList = readSimJobsFromFile(f);
		galgo.importJobList(jobList);
	}

	/**
	 * Exports events from the SimJob Queue to a serialized file
	 * @param f - File to export events the events to
	 */
	public void exportEvents(File f) {

		SimJob[] jobList = galgo.getJobQStrings();
		if (jobList != null) {
			writeSimJobsToFile(jobList,f);
		}
	}

	/**
	 * Reads the SimJob Array from a serialized file
	 * @param f - the file that contains the serialized SimJobs
	 * @return The array of SimJobs read from file 
	 */
	private SimJob[] readSimJobsFromFile(File f)
	{

		SimJob[] jobList;

		try
		{
			FileInputStream fileIn = new FileInputStream(f);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			jobList = (SimJob[]) in.readObject();
			in.close();
			fileIn.close();
		}catch(IOException i)
		{
			i.printStackTrace();
			return null;
		}catch(ClassNotFoundException c)
		{
			System.out.println("SimJob[] class not found");
			c.printStackTrace();
			return null;
		}
		return jobList;
	}

	/**
	 * Takes a SimJob array and serializes it and stores it in a file
	 * @param jobList - The SimJob Array to store
	 * @param f - The file to store the serialized data in
	 */
	private void writeSimJobsToFile(SimJob[] jobList, File f) {
		try
		{
			FileOutputStream fileOut =
					new FileOutputStream(f);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(jobList);
			out.close();
			fileOut.close();
			System.out.printf("Serialized data is saved in " + f.toString());
		}catch(IOException i)
		{
			i.printStackTrace();
		}
	}


	/**
	 * Add an event to add a vertex v 
	 * @param v - id of the vertex
	 */
	public void addVertex(String v) {
		galgo.addVertex(v);
	}

	/**
	 * Add an event to add an edge e
	 * @param e - id of edge
	 * @param v1 - id of from vertex
	 * @param v2 - id of to vertex
	 * @param directed - indication of a directed edge
	 */
	public void addEdge(String e, String v1, String v2, boolean directed) {
		galgo.addEdge(e, v1, v2, directed);
	}

	/**
	 * Takes a file f and adds the vertices/edges to the graph
	 * @param f - File containing vertex information
	 */
	public void importGraph(File f) {

		String graphdata = getStringFromFile(f);
		ImportEdgeDetails[] ied = getImportEdgeDetails(graphdata);
		addImportEdgeDetailsToGraph(ied);
		return;
	}
	/**
	 * Gets contents of file and returns a String with the content
	 * @param f - File to get string from
	 * @return string of file content
	 */
	private String getStringFromFile(File f)
	{

		BufferedReader br = null;
		String graphdata="";

		try
		{
			FileReader fileIn = new FileReader(f);
			br = new BufferedReader(fileIn);
			while (br.ready())
			{
				graphdata += br.readLine();
			}
			br.close();
			fileIn.close();
		}catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return graphdata;
	}

	final String edge_delimeter=">>>";
	final String ve_delimeter = ">>";
	final String in_delimeter = ">";
	/**
	 * Given a graph data string, extract an array of edge details
	 * @param graphdata - graph data string 
	 * @return array of edge details to import
	 */
	private ImportEdgeDetails[] getImportEdgeDetails(String graphdata)
	{
		String[] edges = graphdata.split(edge_delimeter);

		ImportEdgeDetails[] ied = new ImportEdgeDetails[edges.length];

		for (int i=0; i<edges.length;i++)
		{
			String[] edges_details = edges[i].split(ve_delimeter);
			String v_string = edges_details[0];
			String e_string = edges_details[1];

			// Get Vertex Details
			String[] vertices = v_string.split(in_delimeter);
			String v1 = vertices[0];
			String v2 = vertices[1];
			// Get Edge Details
			String[] e_functions = e_string.split(in_delimeter);
			String e="";
			for (String ef : e_functions)
			{
				e += ef + "#";
			}
			e = e.substring(0, e.length()-1);
			ied[i] =  new ImportEdgeDetails(v1, v2, e);
		}

		return ied;
	}
	/**
	 * This just gets a edge label and checks to see if that label
	 * is useful. All of the strings in this method are considered useless
	 * and thus are edges with these strings are not added. 
	 * @param e
	 * @return
	 */
	private boolean isEdgePermitted(String e){
		if(e.equals("id(trueLoop)")){
			return false;
		}
		else if(e.equals("id(trueLoopRestart)")){
			return false;
		}
		else if(e.equals("id(defaultRestart)")){
			return false;
		}
		else if(e.equals("id(alphaBooster)")){
			return false;
		}
		else{
			return true;
		}
	}

	/**
	 * Given an array of edges to import, add it to the graph
	 * including adding vertices if required
	 * @param ied - Array of edge details to import
	 */
	private void addImportEdgeDetailsToGraph(ImportEdgeDetails[] ied)
	{

		for (ImportEdgeDetails ed : ied){

			//System.out.println(ed.toString());
			String v1 = ed.v1;
			String v2 = ed.v2;
			String e = ed.e;

			// Check if vertices are present and add if not
			// _ to hide
			if (graph.getNode(v1)==null) { 
				graph.addNode(v1);
				Node n = graph.getNode(v1);
				n.setAttribute("_ui.label", v1);
			}
			if (graph.getNode(v2)==null) { 
				graph.addNode(v2);
				Node n = graph.getNode(v2);
				n.setAttribute("_ui.label", v2);
			}
			if(!v1.equals(v2)){//This code stops from adding loops
				// Bug - add edge
				if(isEdgePermitted(e)){
					if (graph.getEdge(ed.toString()) != null) {
						System.err.println("Duplicate edge "+ed.toString());
						continue;
					}
					graph.addEdge(ed.toString(), v1, v2, true);
					Edge edge = graph.getEdge(ed.toString());
					edge.setAttribute("_ui.label", e);
				}
			}
		}
	}

	/**
	 * Get the graph object of the current graph 
	 * @return graph object of the current graph 
	 */
	public Graph getGraph()
	{
		return graph;
	}

}
