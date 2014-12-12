Welcome to the Visual ProPPR Tool
=====================================

This tool is used to visualize ProPPR proof graphs.

About this package
-----------------

Visual ProPPR is built in Java SDK 6 and uses a graph visualization library called "Graph Stream", which can be viewed at http://graphstream-project.org/

The workspace of this project is in Eclipse. 


About #visual ProPPR
-----------------
The main features of this include the GUI which allows you to take in a graph is a specified format and import it.

Running the App
--------------
Besides getting all the relative file paths set in the eclipse workspace, the application should work out of the box by running the Main class.

Importing a graph
-----------------
To import a graph, once the UI is loaded, just click "Browse Graph" and select the graph and select "Load Graph" to load it into the UI. 

We note that the graph has to be in the format specified in the program. Convert a grounded file using src/scripts/parse_graph.py. Each example is placed in a separate graph file for display in Visual ProPPR.

Documentation
-----------------

For details of the graphstream API, you may view the documents either through their java docs or online at http://graphstream-project.org/doc/.
