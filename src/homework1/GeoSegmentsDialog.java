package homework1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A JDailog GUI for choosing a GeoSegemnt and adding it to the route shown
 * by RoutDirectionGUI.
 * <p>
 * A figure showing this GUI can be found in homework assignment #1.
 */
public class GeoSegmentsDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	// the RouteDirectionsGUI that this JDialog was opened from
	private RouteFormatterGUI parent;
	
	// a control contained in this 
	private JList<GeoSegment> lstSegments;
	
	/**
	 * Creates a new GeoSegmentsDialog JDialog.
	 * @effects Creates a new GeoSegmentsDialog JDialog with owner-frame
	 * 			owner and parent pnlParent
	 */
	public GeoSegmentsDialog(Frame owner, RouteFormatterGUI pnlParent) {
		// create a modal JDialog with the an owner Frame (a modal window
		// in one that doesn't allow other windows to be active at the
		// same time).
		super(owner, "Please choose a GeoSegment", true);
		
		this.parent = pnlParent;
		
		// create components
		
		DefaultListModel<GeoSegment> listModel = new DefaultListModel<>();
		
		for (GeoSegment geo_segment : ExampleGeoSegments.segments) {
	        listModel.addElement(geo_segment);
		}
		
		lstSegments = new JList<>(listModel);
		lstSegments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrlSegments = new JScrollPane(lstSegments);
		scrlSegments.setPreferredSize(new Dimension(750, 150));
		
		JLabel lblSegments = new JLabel("GeoSegments:");
		lblSegments.setLabelFor(lstSegments);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.addSegment(lstSegments.getSelectedValue());
				setVisible(false);
			}
		});
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		
		
		// arrange components on grid
		JPanel panel = (JPanel) this.getContentPane();
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		this.setLayout(gridbag);

		c.fill = GridBagConstraints.HORIZONTAL;

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(0,0,0,0);
		
		gridbag.setConstraints(lblSegments, c);
		this.add(lblSegments);
		
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.insets = new Insets(0,0,0,0);

		gridbag.setConstraints(scrlSegments, c);
		this.add(scrlSegments);
		
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(20,0,0,20);

		gridbag.setConstraints(btnAdd, c);
		this.add(btnAdd);
		
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(20,0,0,0);

		gridbag.setConstraints(btnCancel, c);
		this.add(btnCancel);

	}
}
