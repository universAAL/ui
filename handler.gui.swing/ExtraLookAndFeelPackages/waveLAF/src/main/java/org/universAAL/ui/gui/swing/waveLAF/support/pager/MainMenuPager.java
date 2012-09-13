/*******************************************************************************
 * Copyright 2012 Universidad Politécnica de Madrid
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.universAAL.ui.gui.swing.waveLAF.support.pager;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.universAAL.ui.gui.swing.waveLAF.support.GradientLAF;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

/**
 * A Jcomponent that will display components in {@link Page}s. 
 * These pages usually have low number of components, 
 * arranged in {@link GridLayout}; the {@link MainMenuPager} will 
 * create the necessary pages to fit all the components, and manage
 * the user input to change between pages
 * @author amedrano
 *
 */
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
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (pages.getComponentCount() > 0) {
					currentPage = (currentPage + e.getWheelRotation()) % pages.getComponentCount();
					if (currentPage < 0) {
						currentPage = pages.getComponentCount() + currentPage;
					}
					((CardLayout) pages.getLayout()).show(pages, Integer.toString(currentPage));
					bm.update();
				}
			}
		});
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
