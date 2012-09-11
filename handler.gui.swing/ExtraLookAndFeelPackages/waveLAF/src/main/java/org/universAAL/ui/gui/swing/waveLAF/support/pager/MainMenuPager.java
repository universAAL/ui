package org.universAAL.ui.gui.swing.waveLAF.support.pager;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.universAAL.ui.gui.swing.waveLAF.support.GradientLAF;

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

	private JPanel pages;

	private BookMarker bm;
	
	
	/**
	 * Create the panel.
	 */
	public MainMenuPager() {
		setLayout(new BorderLayout(0, 0));
		
		bm = new BookMarker();
		add(bm,BorderLayout.SOUTH);
		
		// these buttons can be customized
		JButton btnPrev = new JButton("<-");
		add(btnPrev, BorderLayout.WEST);
		btnPrev.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) pages.getLayout();
				cl.previous(pages);
				currentPage = (currentPage - 1) % pages.getComponentCount();
				if (currentPage < 0) {
					currentPage = pages.getComponentCount() -1;
				}
				bm.update();
			}
		});
		
		JButton btnNext = new JButton("->");
		add(btnNext, BorderLayout.EAST);
		btnNext.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) pages.getLayout();
				cl.next(pages);		
				currentPage = (currentPage + 1) % pages.getComponentCount();
				bm.update();
			}
		});
		
		pages = new JPanel();
		add(pages, BorderLayout.CENTER);
		pages.setLayout(new CardLayout(space, space));
		pages.setOpaque(false);
		
		pages.addContainerListener(bm);
	}


	@Override
	public Component add(Component arg0) {
		if (comps.size() % (noCols * noRows) == 0) {
			JPanel page = new Page(noRows, noCols, space);
			pages.add(page, Integer.toString(pages.getComponentCount()));
		}
		comps.add(arg0);
		JPanel lastPage = (JPanel) pages.getComponent(pages.getComponentCount()-1);
		lastPage.add(arg0);
		lastPage.revalidate();
		return arg0;
	}
	
	@Override
	public void removeAll() {
		comps.clear();
		pages.removeAll();
		currentPage = 0;
	}

	class BookMarker extends JPanel implements ContainerListener{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public BookMarker() {
			this.setOpaque(false);
		}
		
		public void componentAdded(ContainerEvent e) {
			update();
			
		}

		public void componentRemoved(ContainerEvent e) {
			update();			
		}
		
		public void update() {
			this.removeAll();
			for (int i = 0; i < pages.getComponentCount(); i++) {
				JRadioButton jrb = new JRadioButton();
				// TODO beatyfy radiobuttons
				add(jrb);
				jrb.setOpaque(false);
				jrb.setName(Integer.toString(i));
				if (i == currentPage) {
					jrb.setSelected(true);
					jrb.setEnabled(false);
				}
				else {
					jrb.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e) {
							CardLayout cl = (CardLayout) pages.getLayout();
							String name = ((JRadioButton)e.getSource()).getName();
							cl.show(pages, name);
							currentPage = Integer.parseInt(name);
							bm.update();
						}
					});
				}
			}
		}
		
	}

}
