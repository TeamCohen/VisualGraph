package gsim;




import java.util.LinkedList;
import java.util.Queue;

import org.graphstream.algorithm.DynamicAlgorithm;
import org.graphstream.graph.*;
import org.graphstream.stream.SinkAdapter;


/* Notes:
 * Possibly optimizations:
 * - keep a list of all marked nodes/vertices for ease of unmarking O(k) 
 * instead of O(n) unmarking.
 */

/**
 * This class is the Simulation algorithm, which is an interface used
 * to communicate between the GraphSim object and the graph stream API to
 * display animations.
 * 
 * This handles the simulation job queue. This class should only be called
 * from GraphSim.
 * @author brandon
 *
 */
public class GraphSimAlgorithm extends SinkAdapter implements DynamicAlgorithm {

	private int delay = 750;
	private int flash_delay = 50;
	private String markedNode = null;
	private String markedEdge = null;

	private String graph_bgcolor = "#FFFFCC";
	private String graph_bgflashcolor = "#FFCC66";

	private Graph graph;

	private long running_jid;
	private volatile boolean terminate;
	private volatile Queue<SimJob> jobQ;

	/**
	 * Initializes the graph sim algorithm with a given graph
	 * @param graph - graph to use for the graph sim (not null) 
	 */
	public void init(Graph graph) {
		this.graph = graph;
		this.running_jid = 0;
		this.jobQ = new LinkedList<SimJob>();
		this.terminate = false;
		//graph.addSink(this);
	}

	/**
	 * Starts a working thread to serve SimJob queue events
	 */
	public void compute() {
		//(new jobOracleThread()).start();  //go through and capitalize the class
	}



	/**
	 * Thread class for worker thread to serve SimJob Queue 
	 * @author Brandon Lum
	 *
	 */
	public class jobOracleThread extends Thread {

		/**
		 * Generic sleep function that sleeps for (delay) ms
		 */
		private void sleep()
		{
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/**
		 * Runs Job Oracle that polls the queue and processes the
		 * SimJobs
		 */
		private void jobOracle() 
		{
			while (!terminate)
			{	
				SimJob sjob;

				synchronized(jobQ)
				{
					sjob = jobQ.poll();
				}

				if (sjob!=null)
				{
					processJob(sjob);
				}
				// Sleep
				sleep();
			}
		}

		/**
		 * Runs the job oracle
		 */
		public void run() {
			jobOracle();
		}


	}



	/**
	 * Terminates the Algorithm if required
	 */
	public void terminate() {
		this.terminate=true;
		//graph.removeSink(this);
	}


	/**
	 * Add an event to add Vertex v
	 * @param v - vertex id 
	 */
	public void addVertex(final String v)
	{

		long id = nextId();
		String jobName = "Add Vertex: " + v;
		SimFunction fn = SimFunction.addVertex;
		Object[] args = { v };


		SimJob simjob = new SimJob(id, jobName, fn, args);
		addToJobQ(simjob);
	}

	/**
	 * Adds the vertex to the graph
	 * @param v - vertex id
	 */
	private void addVertex_helper(String v)
	{
		graph.addNode(v);
		Node gnode = graph.getNode(v);
		gnode.addAttribute("_ui.label", v);
	}

	/**
	 * Adds an event to add an edge
	 * @param e - edge id
	 * @param v1 - from vertex
	 * @param v2 - to vertex
	 * @param directed - if the vertex is directed
	 */
	public void addEdge(final String e, final String v1, 
			final String v2, final boolean directed)
	{

		long id = nextId();
		String jobName = "Add Edge: " + e + "(" + v1 + 
				(directed ? "->" : "<->") + v2 + ")";
		SimFunction fn = SimFunction.addEdge;

		Object[] args = { e, v1, v2, directed };


		SimJob simjob = new SimJob(id, jobName, fn, args);
		addToJobQ(simjob);

	}
	/**
	 * Adds an edge to the graph
	 * @param e - edge id
	 * @param v1 - from vertex
	 * @param v2 - to vertex
	 * @param directed - if the vertex is directed
	 */
	private void addEdge_helper(String e, String v1, String v2, boolean directed)
	{
		graph.addEdge(e,v1,v2,directed);
		Edge gedge = graph.getEdge(e);
		gedge.addAttribute("_ui.label", e);
	}	

	/**
	 * Flashes the background of the UI
	 */
	private void flashBackground()
	{
		graph.addAttribute("ui.stylesheet", "graph { fill-color: "+graph_bgflashcolor + "; }");
		try {
			Thread.sleep(flash_delay);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		graph.addAttribute("ui.stylesheet", "graph { fill-color: "+ graph_bgcolor + "; }");
	}



	/* Misc util methods */
	/**
	 * Retrieves the next job id based on running number
	 * @return the next job id
	 */
	private long nextId()
	{
		return running_jid++;
	}

	/**
	 * Processes a simjob by running its function and printing to console
	 * @param sjob - the job to process
	 */
	private void processJob(SimJob sjob)
	{
		System.out.println(sjob.toString());

		Object[] args = sjob.args;

		switch(sjob.fn)
		{
		case addVertex: 
			addVertex_helper((String)args[0]); 
			break;

		case addEdge: 
			addEdge_helper((String)args[0],
					(String)args[1],
					(String)args[2],
					(Boolean)args[3]); 
			break;

		}		
	}

	/**
	 * Adds a new job to the job queue
	 * @param sjob - job to add
	 */
	private void addToJobQ(SimJob sjob)
	{
		synchronized(jobQ)
		{
			jobQ.add(sjob);
		}

	}



	// Export Functions
	/**
	 * Get the array of SimJobs from the current job queue
	 * @return array of SimJobs from the current job queue
	 */
	public SimJob[] getJobQStrings()
	{
		return jobQ.toArray(new SimJob[jobQ.size()]);
	}
	/**
	 * Takes an array of SimJobs and imports them into the queue
	 * @param jobList - array of SimJobs
	 */
	public void importJobList(SimJob[] jobList)
	{
		for (SimJob sjob : jobList)
		{
			jobQ.add(sjob);
		}
	}
}