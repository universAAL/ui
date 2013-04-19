/*******************************************************************************
 * Copyright 2013 Universidad Politécnica de Madrid
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

package org.universAAL.ui.handler.gui.swing.model.FormControl;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Repeat;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.FormControl.support.RepeatSubdivider;

/**
 * An alternative to drawing Repeats, Specially indicated for editable repeats, since forms whould be modeled and rendered with in a 
 * {@link JPanel} fitted with a {@link GridLayout}.
 * @author amedrano
 *
 */
public class RepeatModelGrid extends RepeatModel {

	/**
	 * The {@link GridLayout}, useful for extenders of this class to access and change the v and h gaps.
	 */
	private GridLayout layout;
	
    /**
     * container for children of {@link #repeat}
     */
    private FormControl[] elems;

	private JPanel grid;

	private RepeatSubdivider subDivider;

	/**
	 * @param control
	 * @param render
	 */
	public RepeatModelGrid(Repeat control, Renderer render) {
		super(control, render);
	}

	/** {@ inheritDoc}	 */
	public JComponent getNewComponent() {
		Repeat r = (Repeat)fc;
		subDivider = new RepeatSubdivider(r);
		elems = subDivider.getElems();
		
		
		layout = new GridLayout(0,elems.length);
		grid = new JPanel(layout);
		return grid;
	}
	
	private void reDrawPanel(){
		Repeat r = (Repeat)fc;
		subDivider = new RepeatSubdivider(r);
		elems = subDivider.getElems();
		update();
		grid.revalidate();
	}

	/** {@ inheritDoc}	 */
	protected void update() {
		int ii = 0;
		List forms = subDivider.generateSubForms();
		for (Iterator i = forms.iterator(); i.hasNext();) {
			Form f = (Form) i.next();
			FormControl[] fcs = f.getIOControls().getChildren();
			for (int j = 0; j < fcs.length; j++) {
				JComponent jc = getRenderer()
						.getModelMapper().getModelFor(fcs[j]).getComponent();
				jc.addFocusListener(new RowChanger(ii));
				grid.add(jc);
			}
			ii++;
		}
	}

	/**
	 * See {@link RepeatModelGrid#layout}.
	 * @return The layout used for the JPanel containing the repeat sub components.
	 */
	GridLayout getLayout(){
		return layout;
	}
	
	protected JPanel getButtonPanel() {
		Repeat r = (Repeat)fc;

		JPanel buttonPanel = new JPanel();
		if (r.listAcceptsNewEntries()) {
			buttonPanel.add(new AddTableButton());
		}
		if (r.listEntriesDeletable()) {
			buttonPanel.add(new DeleteTableButton());
		}
		return buttonPanel;
	}
	
	/**
	 * Class representing the Add button for Tables.
	 * @author amedrano
	 */
	public class AddTableButton extends JButton implements ActionListener {

		public AddTableButton(Icon icon) {
			super(icon);
			this.addActionListener(this);
			this.setName(fc.getURI()+"_Add");
		}

		public AddTableButton(String text, Icon icon) {
			super(text, icon);
			this.addActionListener(this);
			this.setName(fc.getURI()+"_Add");
		}

		public AddTableButton(String text) {
			super(text);
			this.addActionListener(this);
			this.setName(fc.getURI()+"_Add");
		}

		/**
		 * Java Serializer Variable
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor for Add button
		 */
		public AddTableButton() {
			super();
			this.addActionListener(this);
			this.setName(fc.getURI()+"_Add");
		}

		/** {@inheritDoc}*/
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					if (((Repeat)fc).addValue()){
						// re draw the whole Panel.
						reDrawPanel();
					}
				}
			});
		}
	}

	/**
	 * Class representing the Delete button for Tables.
	 * @author amedrano
	 */
	public class DeleteTableButton extends JButton implements ActionListener {

		public DeleteTableButton(Icon icon) {
			super(icon);
			this.addActionListener(this);
			this.setName(fc.getURI()+"_Delete");
		}

		public DeleteTableButton(String text, Icon icon) {
			super(text, icon);
			this.addActionListener(this);
			this.setName(fc.getURI()+"_Delete");
		}

		public DeleteTableButton(String text) {
			super(text);
			this.addActionListener(this);
			this.setName(fc.getURI()+"_Delete");
		}

		/**
		 * Java Serializer Variable
		 */
		private static final long serialVersionUID = 1L;

		
		/**
		 * Constructor for Remove Button
		 */
		public DeleteTableButton() {
			super();
			this.addActionListener(this);
			this.setName(fc.getURI()+"_Delete");
		}
		
		/** {@inheritDoc}*/
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					if (((Repeat)fc).addValue()){
						// re draw the whole Panel.
						reDrawPanel();
					}
				}
			});
		}
	}
	
	private class RowChanger implements FocusListener {
		
		private int row;
		
		

		/**
		 * Create a Row listener that will change the selected row when the element is selected.
		 * @param row the row at which the listened element is at.
		 */
		public RowChanger(int row) {
			super();
			this.row = row;
		}

		/** {@ inheritDoc}	 */
		public void focusGained(FocusEvent e) {
			// change the repeat's lselected row to the row of this component.
			Repeat r = (Repeat) fc;
			r.setSelection(row);
		}

		/** {@ inheritDoc}	 */
		public void focusLost(FocusEvent e) {
			// Nothing, another component will be called to focusGained().
		}
		
	}
}
