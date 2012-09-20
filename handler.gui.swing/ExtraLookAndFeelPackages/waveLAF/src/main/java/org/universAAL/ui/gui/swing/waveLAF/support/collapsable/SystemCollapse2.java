package org.universAAL.ui.gui.swing.waveLAF.support.collapsable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.jdesktop.swingx.JXCollapsiblePane;
import org.universAAL.ui.gui.swing.waveLAF.support.MyScrolPaneLayout;

public class SystemCollapse2 extends JPanel implements ComponentListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JXCollapsiblePane collapsablePanel;
	private JButton tglbtnSystem;
	private JPanel systemPanel;
	private Color backgroundColor;

	/**
	 * Create the panel.
	 */
	public SystemCollapse2() {
		backgroundColor = new Color(204, 204, 204);
		
		setOpaque(false);
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		add(panel, BorderLayout.NORTH);
		
		tglbtnSystem = new TriangularButton();
		tglbtnSystem.setForeground(backgroundColor);
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
		systemPanel.setBackground(backgroundColor);
		systemPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JScrollPane scrollPane = new JScrollPane(systemPanel);
		scrollPane.setLayout(new MyScrolPaneLayout());
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(null);
		//scrollPane.setPreferredSize(systemPanel.getSize());
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



	/**
	 * @return the backgroundColor
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}



	/**
	 * @param backgroundColor the backgroundColor to set
	 */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

}
