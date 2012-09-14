package org.universAAL.ui.gui.swing.waveLAF.support.collapsable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.PopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXCollapsiblePane;

public class SystemCollapse extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JXCollapsiblePane collapsable;
	protected long lastInCollapsable;
	protected boolean deployed;
	private JPanel head;

	/**
	 * Create the panel.
	 */
	public SystemCollapse() {
		
		setLayout(new BorderLayout(0, 0));
		
		head = new JPanel();
		head.setBorder(null);
		
		head.setBackground(Color.RED);
		super.add(head, BorderLayout.NORTH);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		head.add(verticalStrut);
		
		collapsable = new JXCollapsiblePane();
		collapsable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				deployed = true;
			}
		});
		super.add(collapsable, BorderLayout.CENTER);
		collapsable.setCollapsed(true);
		collapsable.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		//this.setOpaque(false);
		
		//root.getGlassPane().
		head.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (collapsable.isCollapsed() 
						&& System.currentTimeMillis() - lastInCollapsable > 500) {
					collapsable.setCollapsed(false);
					System.out.println("uncollapsing");
					System.out.println(System.currentTimeMillis() - lastInCollapsable);
				}
			}
				@Override
				public void mouseExited(MouseEvent e) {
					if (!collapsable.isCollapsed() 
							&& deployed) {
						collapsable.setCollapsed(true);
						deployed = false;
						lastInCollapsable = System.currentTimeMillis();
						System.out.println("collapsing");
					}
				}
		});
		
	}

	/**
	 * @return
	 * @see java.awt.Container#getComponentCount()
	 */
	public int getComponentCount() {
		return collapsable.getComponentCount();
	}

	/**
	 * @param n
	 * @return
	 * @see java.awt.Container#getComponent(int)
	 */
	public Component getComponent(int n) {
		return collapsable.getComponent(n);
	}

	/**
	 * @return
	 * @see java.awt.Container#getComponents()
	 */
	public Component[] getComponents() {
		return collapsable.getComponents();
	}

	/**
	 * @param comp
	 * @return
	 * @see java.awt.Container#add(java.awt.Component)
	 */
	public Component add(Component comp) {
		return collapsable.add(comp);
	}




	/**
	 * @param popup
	 * @see java.awt.Component#add(java.awt.PopupMenu)
	 */
	public void add(PopupMenu popup) {
		collapsable.add(popup);
	}

	/**
	 * @param comp
	 * @see org.jdesktop.swingx.JXCollapsiblePane#remove(java.awt.Component)
	 */
	public void remove(Component comp) {
		collapsable.remove(comp);
	}

	/**
	 * @param index
	 * @see org.jdesktop.swingx.JXCollapsiblePane#remove(int)
	 */
	public void remove(int index) {
		collapsable.remove(index);
	}

	/**
	 * 
	 * @see org.jdesktop.swingx.JXCollapsiblePane#removeAll()
	 */
	public void removeAll() {
		collapsable.removeAll();
	}

	
}
