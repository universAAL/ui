package org.universAAL.ui.gui.swing.waveLAF.support.pager;

import org.universAAL.ui.gui.swing.waveLAF.support.GradientLAF;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

public class MainMenuPager extends GradientLAF {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int currentPage = 0;
	
	private int noCols = 3;
	private int noRows = 2;
	private int space = 5;
	
	private ArrayList<Component> comps = new ArrayList<Component>();
	
	private JPanel kikerPanel;
	
	
	/**
	 * Create the panel.
	 */
	public MainMenuPager() {
		setLayout(new BorderLayout(0, 0));
		
		// these buttons can be customized
		JButton btnPrev = new JButton("<-");
		add(btnPrev, BorderLayout.WEST);
		//TODO: add press callback
		
		JButton btnNext = new JButton("->");
		add(btnNext, BorderLayout.EAST);
		//TODO: add press callback
		
		kikerPanel = new JPanel();
		add(kikerPanel, BorderLayout.CENTER);
		kikerPanel.setLayout(new GridLayout(noRows, noCols, space, space));
		kikerPanel.setOpaque(false);
	}


	@Override
	public Component add(Component arg0) {
		comps.add(arg0);
		return arg0;
	}


	@Override
	public void paint(Graphics g) {
		int firstOfPage = currentPage*noCols*noRows;
		int endOfPage = firstOfPage + (noCols*noRows) -1;
		int realEndOfPage = endOfPage >= comps.size()? comps.size() -1: endOfPage;
		List<Component> page = comps.subList(firstOfPage, realEndOfPage);
		kikerPanel.removeAll();
		for (Component c : page) {
			kikerPanel.add(c);
		}
		super.paint(g);
	}

}
