/*******************************************************************************
 * Copyright 2011 Universidad Politécnica de Madrid
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
package org.universAAL.ui.handler.gui.swing.defaultLookAndFeel;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.Layout.BorderedScrolPaneLayout;
import org.universAAL.ui.handler.gui.swing.model.FormModel;

/**
 * The Look and Feel for Forms.
 * 
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 * @author pabril
 * @see FormModel
 */
public class FormLAF extends FormModel {

	/**
	 * internal accounting for the frame being displayed.
	 */
	private JFrame frame = null;

	/**
	 * Constructor.
	 * 
	 * @param f
	 *            {@link Form} which to model.
	 * @param render the {@link Renderer} reference
	 */
	public FormLAF(Form f, Renderer render) {
		super(f, render);
	}

	/**
	 * get the io panel wrapped in a scroll pane.
	 * 
	 * @return the {@link FormModel#getIOPanel} wrapped in a {@link JScrollPane}
	 *         .
	 */
	protected JScrollPane getIOPanelScroll() {
		/*
		 * JPanel ioPanel = super.getIOPanel(); JScrollPane sp = new
		 * JScrollPane(ioPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		 * JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); //ioPanelDim =
		 * ioPanel.getPreferredSize(); ioPanel.setPreferredSize(new
		 * Dimension(sp.getSize().width, // ioPanel.getPreferredSize().height)
		 * ioPanel.getHeight())); //FIXME resize Layout+scroll
		 */
		JPanel ioPanel = super.getIOPanel();
		JScrollPane sp = new JScrollPane(ioPanel,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		return sp;
	}

	/**
	 * get the submit panel wrapped in a scroll pane.
	 * 
	 * @return the {@link FormModel#getSubmitPanel} wrapped in a
	 *         {@link JScrollPane}.
	 */
	protected JScrollPane getSubmitPanelScroll(int depth) {
		JPanel submit = super.getSubmitPanel(depth);
		JScrollPane sp = new JScrollPane(submit,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp.setLayout(new BorderedScrolPaneLayout());
		return sp;
	}

	/**
	 * get the system panel wrapped in a scroll pane.
	 * 
	 * @return the {@link FormModel#getSystemPanel} wrapped in a
	 *         {@link JScrollPane}.
	 */
	protected JScrollPane getSystemPanelScroll() {
		JScrollPane sp =new JScrollPane(super.getSystemPanel(),
				JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				sp.setLayout(new BorderedScrolPaneLayout());
		return sp;
	}

	/**
	 * generate the header panel.
	 * 
	 * @return a pannel with universAAL icon in it.
	 */
	protected JPanel getHeader() {
		JPanel header = new JPanel();
		ImageIcon icon = new ImageIcon(
				(getClass().getResource("/main/UniversAAl_logo.png")));
		icon.setDescription("UniversAAL Logo Image");
		JLabel logo = new JLabel(icon);
		logo.getAccessibleContext().setAccessibleName("UniversAAL Logo");
		header.add(logo);
		return (JPanel) header;
	}

	/**
	 * render the frame for the {@link Form}.
	 */
	public JFrame getFrame() {
		if (form.isMessage()) {
			frame = new JFrame(form.getTitle());
			frame.getAccessibleContext().setAccessibleName(form.getTitle());
			JScrollPane io = getIOPanelScroll();
			io.getAccessibleContext().setAccessibleName(IO_NAME);
			JScrollPane sub = new JScrollPane(super.getSubmitPanel(),
					JScrollPane.VERTICAL_SCROLLBAR_NEVER,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			sub.getAccessibleContext().setAccessibleName(SUB_NAME);
			frame.add(io, BorderLayout.CENTER);
			frame.add(sub, BorderLayout.SOUTH);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.pack();
		}
		if (form.isSystemMenu()) {
			frame = new JFrame(form.getTitle());
			frame.getAccessibleContext().setAccessibleName(form.getTitle());
			frame.add(getHeader(), BorderLayout.NORTH);
			frame.add(getIOPanel(), BorderLayout.CENTER);
			frame.add(getSystemPanelScroll(), BorderLayout.SOUTH);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setExtendedState(Frame.MAXIMIZED_BOTH);
			frame.setUndecorated(true);
			frame.pack();
			setFullScreen();
		}
		if (form.isStandardDialog()) {
			/*
			 * some further LAF can be done here: if only submints (no sub
			 * dialogs) and <4 (and priority hi?) then show like a popup.
			 */
			frame = new JFrame(form.getTitle());
			frame.getAccessibleContext().setAccessibleName(form.getTitle());
			frame.add(getHeader(), BorderLayout.NORTH);
			JScrollPane io = getIOPanelScroll();
			io.getAccessibleContext().setAccessibleName(IO_NAME);
			JScrollPane sub = getSubmitPanelScroll(0);
			sub.getAccessibleContext().setAccessibleName(SUB_NAME);
			JScrollPane sys = getSystemPanelScroll();
			sys.getAccessibleContext().setAccessibleName(SYS_NAME);
			frame.add(io, BorderLayout.CENTER);
			frame.add(sub, BorderLayout.EAST);
			frame.add(sys, BorderLayout.SOUTH);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setExtendedState(Frame.MAXIMIZED_BOTH);
			frame.setUndecorated(true);
			frame.pack();
			setFullScreen();
		}
		if (form.isSubdialog()) {
			frame = new JFrame(form.getTitle());
			frame.getAccessibleContext().setAccessibleName(form.getTitle());
			frame.add(getHeader(), BorderLayout.NORTH);
			JScrollPane sub = getSubmitPanelScroll(0);
			sub.getAccessibleContext().setAccessibleName(SUB_NAME);
			JScrollPane sys = getSystemPanelScroll();
			sys.getAccessibleContext().setAccessibleName(SYS_NAME);
			JPanel subpanel = new JPanel(new BorderLayout());
			subpanel.add(getIOPanelScroll(), BorderLayout.CENTER);
			for (int i = super.getSubdialogLevel(); i > 1; i--) {
				subpanel.add(getSubmitPanel(i), BorderLayout.EAST);
				JPanel tempanel = new JPanel(new BorderLayout());
				tempanel.add(subpanel, BorderLayout.CENTER);
				subpanel = tempanel;
			}
			frame.add(subpanel, BorderLayout.CENTER);
			frame.add(sub, BorderLayout.EAST);
			frame.add(sys, BorderLayout.SOUTH);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setExtendedState(Frame.MAXIMIZED_BOTH);
			frame.setUndecorated(true);
			frame.pack();
			setFullScreen();
		}
		return frame;
	}

	/**
	 * set the window to use full screen.
	 */
	private void setFullScreen() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		int xSize = ((int) tk.getScreenSize().getWidth());
		int ySize = ((int) tk.getScreenSize().getHeight());
		frame.setSize(xSize, ySize);
	}

	/** {@inheritDoc} */
	public void terminateDialog() {
		if (frame != null) {
			frame.dispose();
		}
	}
}
