package gsim;

/**
 * Simulation Job struct for the graph simulation algorithm. 
 * @author brandon
 *
 */
public class SimJob  implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	public long id;

	public String jobName;
	public SimFunction fn;
	public Object[] args;

	/**
	 * Constructor
	 * @param id - id of the SimJob
	 * @param jobName - Name of job
	 * @param fn - Function to run
	 * @param args - Array of arguments to run with function fn
	 */
	public SimJob(long id, String jobName, SimFunction fn, Object[] args)
	{
		this.id = id;
		this.jobName = jobName;
		this.fn = fn;
		this.args = args;
	}
	/**
	 * toString function
	 */
	public String toString()
	{
		return "Job "+ id + ":" + jobName;
	}
}
