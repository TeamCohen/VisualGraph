package gsim;

/**http://graphstream-project.org/doc/Tutorials/Storing-retrieving-and-displaying-data-in-graphs_1.0/
 * Edge Details Struct for importing of graph
 * @author brandon
 *
 */
public class ImportEdgeDetails { 
	public String v1;
	public String v2;
	public String e;

	/**
	 * Constructor
	 * @param v1 - From vertex id
	 * @param v2 - To vertex id
	 * @param e - Edge id
	 */
	public ImportEdgeDetails(String v1, String v2, String e)
	{
		this.v1 = v1;
		this.v2 = v2;
		this.e = e;
	}

	/**
	 * toString function
	 */
	public String toString()
	{
		return v1 + "->" + v2 + ":" + e;
	}
}

