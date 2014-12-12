package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.EventObject;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.ViewerListener;
import org.graphstream.ui.swingViewer.ViewerPipe;


/**
 * UI Class that contains all the UI functions
 * @author brandon
 *
 */
public class ControlUI extends JFrame implements ActionListener, MouseWheelListener, MouseMotionListener {


	// Logic Variables
	private MainLogic MLogic;
	private 	File graph_file = null;
	private 	File anim_file = null;

	private  GraphUIProperty gUIProp;

	// UI Components

	private  int frm_width, frm_height;
	private  int ctrl_width, ctrl_height;

	private JFileChooser fc;
	private JFrame  jfrm;

	private JLabel lbl_header;
	private JLabel lbl_graphPath;
	private JButton btn_chooseGraph;

	private JButton btn_loadGraph;

	private JPanel ctrl_panel;

	private JTextField txt_highlight;
	private JButton btn_highlight;

	private View vw=null;

	// Set % of span
	private double btn_chooseGraph_h = .1;


	/**
	 * Constructor, inits and starts UI
	 */
	public ControlUI ()
	{
		//sets the values in the fields dealing with the screen
		init ();
		//Creates the GUI
		startUI();
		System.setProperty("gs.ui.renderer",
				"org.graphstream.ui.j2dviewer.J2DGraphRenderer");

	}

	/**
	 * initializes height, width variables, etc. 
	 * and creates main logic object for use by UI
	 */
	private void init ()
	{

		// Init GUI Settings
		Dimension screen_dim = Toolkit.getDefaultToolkit().getScreenSize();
		frm_width = screen_dim.width;
		frm_height = screen_dim.height;

		ctrl_width = (int)(0.2 * (double)screen_dim.width);
		ctrl_height = screen_dim.height;

		//System.out.println(screen_dim.height);

		// Initialize graph UI property object to pass to sim
		gUIProp = new GraphUIProperty();
		gUIProp.height = screen_dim.height - 100;
		gUIProp.width = screen_dim.width - ctrl_width;
		gUIProp.posx = 0;
		gUIProp.posy = 0;

		MLogic= new MainLogic();
	}

	/**
	 * Adds objects to UI, prepares UI window
	 */
	private  void startUI(){

		jfrm = new JFrame();
		jfrm.setBounds(0, 0, frm_width, frm_height);
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jfrm.getContentPane().setLayout(null);

		//This is the code that places the the new pannel
		//This may be where I need to shift everything to make it straight
		ctrl_panel = new JPanel();
		ctrl_panel.setBounds(frm_width - ctrl_width, 0, ctrl_width, frm_height);
		jfrm.getContentPane().add(ctrl_panel);
		ctrl_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		try{

			ControlUI.class.getResourceAsStream("images/ProPPR.png");
			BufferedImage myPicture = ImageIO.read(ControlUI.class.getResourceAsStream("images/ProPPR.png"));
			JLabel picLabel = new JLabel(new ImageIcon(myPicture));
			ctrl_panel.add(picLabel);
		}
		catch(Exception e) { e.printStackTrace(); }

		lbl_graphPath = new JLabel("<No Graph>");
		ctrl_panel.add(lbl_graphPath);

		btn_chooseGraph = new JButton("Choose Graph");
		btn_chooseGraph.setSize(100,100);
		ctrl_panel.add(btn_chooseGraph);

		// File Chooser Component
		// Create a file chooser
		fc = new JFileChooser();
		btn_chooseGraph.addActionListener(this);


		btn_loadGraph = new JButton("Load Graph");
		ctrl_panel.add(btn_loadGraph);
		btn_loadGraph.addActionListener(this);

		// Features to add
		// Block Label
		JLabel lbl_div1 = new JLabel("-----------------------------------");
		ctrl_panel.add(lbl_div1);	

		jfrm.setVisible(true);



		// We connect back the viewer to the graph,
		// the graph becomes a sink for the viewer.
		// We also install us as a viewer listener to
		// intercept the graphic events.
		//ViewerPipe fromViewer = vwr.newViewerPipe();
		//fromViewer.addViewerListener((ViewerListener) new NodeClickListener());
		//fromViewer.addSink(graph);


	}




	/**
	 * Action listener for click of buttons
	 * This 
	 */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		//If the choose graph button is pressed
		//this opens a window so that at a file can be selected
		if (source == btn_chooseGraph)
		{
			int returnVal = fc.showOpenDialog(jfrm);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				graph_file = fc.getSelectedFile();
				lbl_graphPath.setText(graph_file.getName());
				//This is where a real application would open the file.
				System.out.println("Opening: " + graph_file.getName() + "." + "\n");
			} else {
				System.out.println("Open command cancelled by user." + "\n");
			}
			return;
		}
		//If the load graph button is pressed
		if (source == btn_loadGraph)
		{
			loadGraph();
		}
	} 

	/**
	 * Action listener for when the mouse wheel is moved. 
	 * Depending on if the mouse wheel is moved up or down the
	 * graph zooms in our out. Also, it zooms in on a section of the graph 
	 * where the mouse is. It does this by dividing the graph
	 * into a three by three grid. I have it hard coded for values that I
	 * believe are specific for my computer. One of the things I wanted to
	 * change was get it so that the value are based of the fields that get 
	 * the screen size.
	 */
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(vw != null){
			int notches = e.getWheelRotation();
			Point point = e.getPoint();
			double i = vw.getCamera().getViewPercent();
			if(i < 1){
				if(point.getX() < 400){
					//400 is an example of a hardcode value to change
					if(point.getY() < 300){
						vw.getCamera().getViewCenter().move(-1, 1);
					}
					else if(point.getY() < 600){
						vw.getCamera().getViewCenter().move(-1, 0);
					}
					else{
						vw.getCamera().getViewCenter().move(-1, -1);
					}
				}
				else if(point.getX() < 800){
					if(point.getY() < 300){
						vw.getCamera().getViewCenter().move(0, 1);
					}
					else if(point.getY() < 600){
						vw.getCamera().getViewCenter().move(0, 0);
					}
					else{
						vw.getCamera().getViewCenter().move(0, -1);
					}
				}
				else{
					if(point.getY() < 300){
						vw.getCamera().getViewCenter().move(1, 1);
					}
					else if(point.getY() < 600){
						vw.getCamera().getViewCenter().move(1, 0);
					}
					else{
						vw.getCamera().getViewCenter().move(1, -1);
					}
				}
			}
			else{
				vw.getCamera().resetView();
			}


			if(notches > 0){
				vw.getCamera().setViewPercent(i * 1.1);
			}
			else{

				vw.getCamera().setViewPercent(i * 0.9);
			}
		}
	}
	/**
	 * Function to load graph and create graph listener
	 */
	NodeClickListener clisten=null;
	private void loadGraph()
	{
		if (graph_file != null)
		{
			// close event listener for mouse first before removing view 
			// in next step
			if (clisten!=null)
			{
				clisten.viewClosed(null);
			}
			// Remove view if exists
			if (vw!=null)
			{
				jfrm.remove(vw);
			}

			//This is a sort of wrapper class which calls all 
			//the other methods in GraphSims and GraphSimsAlgorithm
			//the actually creates the graph and animates it
			Viewer vwr = MLogic.simulate_graph(graph_file);

			vw = vwr.addDefaultView(false);


			vw.setSize(gUIProp.width,gUIProp.height);
			vw.setLocation(gUIProp.posx, gUIProp.posy);



			// We connect back the viewer to the graph,
			// the graph becomes a sink for the viewer.
			// We also install us as a viewer listener to
			// intercept the graphic events.
			ViewerPipe fromViewer = vwr.newViewerPipe();
			clisten = new NodeClickListener(fromViewer, vw, MLogic.getGraph());
			fromViewer.addViewerListener((ViewerListener) clisten);
			vw.addMouseWheelListener(this);
			vw.addMouseMotionListener(this);


			// Add in frame
			jfrm.add(vw,BorderLayout.LINE_START);


		} 
		else
		{
			JOptionPane.showMessageDialog(jfrm,
					"Please select a graph!",
					"No graph selected!",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(vw != null){

		}
	}

}
