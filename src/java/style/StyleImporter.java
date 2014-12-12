package style;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class StyleImporter { 

	/* getStyle (String stylesheet)
	 * 
	 * 
	 * 
	 * 
	 * RETN: 
	 * string of contents of file or null on failure
	 * 
	 */
	/**
	 * Returns the string of the contents of the file 'stylesheet'
	 * 
	 * @param stylesheet - string of the stylesheet (not null)
	 * @return CSS String to import into graph
	 */
	public static String getStyle(String stylesheet)
	{		
		InputStream input;
		BufferedReader reader;
		StringBuilder output;


		// Initialize Streams and Readers
		try{
			input = StyleImporter.class.getResourceAsStream(stylesheet);
		} catch(Exception e) { e.printStackTrace(); return null; }

		try{
			reader = new BufferedReader(new InputStreamReader(input));
		} catch(Exception e) { e.printStackTrace(); return null; }


		// Build String
		try{
			output = new StringBuilder();

			// Create String
			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line);
			}
		}
		catch (Exception e) { e.printStackTrace(); return null; }


		// Close Reader
		finally
		{
			try{
				reader.close();
			}
			catch(Exception e)
			{ e.printStackTrace(); return null; }
		}


		return output.toString();

	}

}
