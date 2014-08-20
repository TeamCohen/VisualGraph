<<<<<<< HEAD
Welcome to the Visual ProPPR Tool
=====================================

This tool is used to visualize ProPPR proof graphs and show the personalized page rank algorithm.

About this package
-----------------

Visual ProPPR is built in Java SDK 7 and uses a graph visualization library called "Graph Stream", which can be viewed at http://graphstream-project.org/

The workspace of this project is in Eclipse. 


About #visual ProPPR
-----------------
The main features of this include the GUI which allows you to take in a graph is a specified format and import it. There is a public interfacing class called GraphSim, which is located in the 'sim' package. This is the public interface that should be used to run visualization of the page rank process/adding/deleting vertices. 

For each action taken, it is put into an action queue of tasks to visualize.

A Job Oracle is spawned by GraphSimAlgorithm, which does all the heavy lifting. It takes this queue and, according to the set delay, dequeues the queue and runs the simulated action on the displayed graph. 


Running the App
--------------
Besides getting all the relative file paths set in the eclipse workspace, the application should work out of the box by running the Main class.

Importing a graph
-----------------
To import a graph, once the UI is loaded, just click "Browse Graph" and select the graph and select "Load Graph" to load it into the UI. 

We note that the graph has to be in the format specified in the program. To take a graph which is in the format of ProPPR's cooked format and use the "Visual Graph Parser", which is a python project that takes in a cooked file and makes them baked - i.e. suitable for use in Visual ProPPR. 

Documentation
-----------------
The documentation can be found in the docs folder, which contains the Java docs.

For details of the graphstream API, you may view the documents either through their java docs or online at http://graphstream-project.org/doc/.
=======
Visual-Graph
============

Repository for the Visual Graph program.
>>>>>>> dc50deb7bf5243502148375bfe96874137d9c013
