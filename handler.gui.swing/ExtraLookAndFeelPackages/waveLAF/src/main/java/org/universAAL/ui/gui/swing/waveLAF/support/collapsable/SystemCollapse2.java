package org.universAAL.ui.gui.swing.waveLAF.support.collapsable;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JButton;

import org.jdesktop.swingx.JXCollapsiblePane;
import javax.swing.JToggleButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class SystemCollapse2 extends JPanel implements ComponentListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JXCollapsiblePane collapsablePanel;
	private JButton tglbtnSystem;
	private JPanel systemPanel;

	/**
	 * Create the panel.
	 */
	public SystemCollapse2() {
		setOpaque(false);
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		add(panel, BorderLayout.NORTH);
		
		tglbtnSystem = new TriangularButton();
		tglbtnSystem.setForeground(new Color(204, 204, 204));
		tglbtnSystem.setBorder(null);
		tglbtnSystem.setOpaque(false);
		tglbtnSystem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (collapsablePanel.isCollapsed()) {
					collapsablePanel.setCollapsed(false);
				}
				else {
					collapsablePanel.setCollapsed(true);
				}
			}
		});
		panel.add(tglbtnSystem);
		
		collapsablePanel = new JXCollapsiblePane();
		collapsablePanel.setOpaque(false);
		add(collapsablePanel, BorderLayout.CENTER);
		collapsablePanel.setCollapsed(true);
		
		systemPanel = new JPanel();
		systemPanel.setBackground(new Color(204, 204, 204));
		systemPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JScrollPane scrollPane = new JScrollPane(systemPanel);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(null);
		collapsablePanel.getContentPane().add(scrollPane);
		systemPanel.addComponentListener(this);
	}



	public Component add(Component comp) {
		return systemPanel.add(comp);
	}

	public void removeAll() {
		systemPanel.removeAll();
	}

	private void resizeTriangle() {
		int width = getSize().width / 7;
		//System.out.println(width);
		Dimension d = new Dimension(width, width/4);
		tglbtnSystem.setSize(d);
		tglbtnSystem.setPreferredSize(d);
		tglbtnSystem.revalidate();
		((JPanel)this.getParent()).revalidate();
	}
	
	public void componentResized(ComponentEvent e) {
		resizeTriangle();
	}

	public void componentMoved(ComponentEvent e) {
		resizeTriangle();
	}

	public void componentShown(ComponentEvent e) {
		resizeTriangle();
	}

	public void componentHidden(ComponentEvent e) {
		resizeTriangle();
	}

}
