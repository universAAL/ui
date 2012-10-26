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
package org.universAAL.ui.handler.gui.swing.classic;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.plaf.FontUIResource;

import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.defaultLookAndFeel.Layout.BorderedScrolPaneLayout;
import org.universAAL.ui.handler.gui.swing.model.FormModel;

/**
 * The Look and Feel for Forms
 * 
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 * @author pabril
 * @see FormModel
 */
public class FormLAF extends FormModel {
    
    private static final String PROP_LAYOUT_HINT = "http://ontology.itaca.es/ClassicGUI.owl#layout";

    /**
     * internal accounting for the frame being displayed.
     */
    private JFrame frame = null;

    public static boolean vertical=false;
    public static final int H_CENTER=0;
    public static final int H_LEFT=1;
    public static final int H_RIGHT=2;
    public static int vGroupHalign=H_CENTER;
    public static int hGroupHalign=FlowLayout.CENTER;
    public static boolean constant=false;
    public static final int hgap=20;
    public static final int vgap=20;

    /**
     * Constructor.
     * 
     * @param f
     *            {@link Form} which to model.
     */
    public FormLAF(Form f, Renderer render) {
	super(f, render);
	Object value=f.getProperty(PROP_LAYOUT_HINT);
	if(value!=null && value instanceof String){
	    String hint=(String)value;
	    vertical=hint.toLowerCase().contains("vertical");
	    constant=hint.toLowerCase().contains("constant");
	    if(hint.toLowerCase().contains("left")){
		vGroupHalign=H_LEFT;
		hGroupHalign=FlowLayout.LEADING;
	    }
	    if(hint.toLowerCase().contains("rigth")){
		vGroupHalign=H_RIGHT;
		hGroupHalign=FlowLayout.TRAILING;
	    }
	    if(hint.toLowerCase().contains("center")){
		vGroupHalign=H_CENTER;
		hGroupHalign=FlowLayout.CENTER;
	    }
	}
    }

    /**
     * get the io panel wrapped in a scroll pane.
     * 
     * @return the {@link FormModel#getIOPanel} wrapped in a {@link JScrollPane}
     *         .
     */
    protected JScrollPane getIOPanelScroll() {
	JPanel ioPanel = super.getIOPanel();
	if(vertical){
	    ioPanel.setLayout(new MyVerticalFlowLayout(MyVerticalFlowLayout.CENTER,hgap,vgap));
	}else{
	    ioPanel.setLayout(new FlowLayout(hGroupHalign,hgap,vgap));
	}
	JScrollPane sp = new JScrollPane(ioPanel,
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	sp.getVerticalScrollBar().setPreferredSize(new Dimension(30, 30));
	sp.getHorizontalScrollBar().setPreferredSize(new Dimension(30, 30));
	sp.setBorder(BorderFactory.createLineBorder(ColorLAF.WHITE_BRIGHT));
	return sp;
    }

    /**
     * get the submit panel wrapped in a scroll pane.
     * 
     * @return the {@link FormModel#getSubmitPanel} wrapped in a
     *         {@link JScrollPane}.
     */
    protected JScrollPane getSubmitPanelScroll(int depth, boolean vertical) {
	JPanel submit = super.getSubmitPanel(depth);
	submit.setLayout(new BoxLayout(submit, vertical?BoxLayout.Y_AXIS:BoxLayout.X_AXIS));
	JScrollPane sp = new JScrollPane(submit,
		vertical?JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED:JScrollPane.VERTICAL_SCROLLBAR_NEVER,
		vertical?JScrollPane.HORIZONTAL_SCROLLBAR_NEVER:JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	sp.getVerticalScrollBar().setPreferredSize(new Dimension(30, 30));
	sp.getHorizontalScrollBar().setPreferredSize(new Dimension(30, 30));
	sp.setLayout(new BorderedScrolPaneLayout());
	sp.setBorder(BorderFactory.createLineBorder(ColorLAF.WHITE_BRIGHT));
	return sp;
    }

    /**
     * get the system panel wrapped in a scroll pane.
     * 
     * @return the {@link FormModel#getSystemPanel} wrapped in a
     *         {@link JScrollPane}.
     */
    protected JScrollPane getSystemPanelScroll() {
	JPanel submit = super.getSystemPanel();
	submit.setLayout(new BoxLayout(submit, BoxLayout.X_AXIS));
	JScrollPane sp = new JScrollPane(submit,
		JScrollPane.VERTICAL_SCROLLBAR_NEVER,
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	sp.getVerticalScrollBar().setPreferredSize(new Dimension(30, 30));
	sp.getHorizontalScrollBar().setPreferredSize(new Dimension(30, 30));
	sp.setLayout(new BorderedScrolPaneLayout());
	sp.setBorder(BorderFactory.createLineBorder(ColorLAF.WHITE_BRIGHT));
	return sp;
    }

    /**
     * generate the header panel.
     * 
     * @return a pannel with universAAL icon in it.
     */
    protected JPanel getHeader(String title) {
	JPanel header = new JPanel();
	ImageIcon icon = new ImageIcon(
		(getClass().getResource(title==null?"/main/uaal.png":"/main/uAALmicro.png")));
	icon.setDescription("UniversAAL Logo Image");
	JLabel logo = new JLabel(title!=null?" "+title+" ":"", icon, SwingConstants.LEFT);
	logo.setFont(new FontUIResource("Corbel", Font.BOLD, 42));
	logo.getAccessibleContext().setAccessibleName("UniversAAL Logo");
	header.add(logo);
	return (JPanel) header;
    }

    /**
     * render the frame for the {@link Form}.
     */
    public void showForm() {
	frame = new JFrame(form.getTitle());
	frame.getAccessibleContext().setAccessibleName(form.getTitle());
	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	frame.setUndecorated(true);
	if (form.isMessage()) {
	    JScrollPane io = getIOPanelScroll();
	    io.getAccessibleContext().setAccessibleName(IO_NAME);
	    JScrollPane sub = getSubmitPanelScroll(0,false);
	    sub.getAccessibleContext().setAccessibleName(SUB_NAME);
	    JPanel border=new JPanel(new BorderLayout());
	    border.setBorder(BorderFactory.createLineBorder(ColorLAF.BLACK_DARK));
	    border.add(getHeader(form.getTitle()), BorderLayout.NORTH);
	    border.add(io, BorderLayout.CENTER);
	    border.add(sub, BorderLayout.SOUTH);
	    frame.add(border);
	    frame.setAlwaysOnTop(true);
	    frame.pack();
	    setHalfScreen();
	}else if (form.isSystemMenu()) {
	    JScrollPane io = getIOPanelScroll();
	    io.getAccessibleContext().setAccessibleName(IO_NAME);
	    JScrollPane sys = getSystemPanelScroll();
	    sys.getAccessibleContext().setAccessibleName(SYS_NAME);
	    frame.add(getHeader(null), BorderLayout.NORTH);
	    frame.add(io, BorderLayout.CENTER);
	    frame.add(sys, BorderLayout.SOUTH);
	    frame.setExtendedState(Frame.MAXIMIZED_BOTH);
	    frame.pack();
	    setFullScreen();
	}else if (form.isStandardDialog()) {
	    JScrollPane io = getIOPanelScroll();
	    io.getAccessibleContext().setAccessibleName(IO_NAME);
	    JScrollPane sub = getSubmitPanelScroll(0,true);
	    sub.getAccessibleContext().setAccessibleName(SUB_NAME);
	    JScrollPane sys = getSystemPanelScroll();
	    sys.getAccessibleContext().setAccessibleName(SYS_NAME);
	    frame.add(getHeader(form.getTitle()), BorderLayout.NORTH);
	    frame.add(io, BorderLayout.CENTER);
	    frame.add(sub, BorderLayout.EAST);
	    frame.add(sys, BorderLayout.SOUTH);
	    frame.setExtendedState(Frame.MAXIMIZED_BOTH);
	    frame.pack();
	    setFullScreen();
	}else if (form.isSubdialog()) {
	    JScrollPane sub = getSubmitPanelScroll(0,true);
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
	    frame.add(getHeader(form.getTitle()), BorderLayout.NORTH);
	    frame.add(subpanel, BorderLayout.CENTER);
	    frame.add(sub, BorderLayout.EAST);
	    frame.add(sys, BorderLayout.SOUTH);
	    frame.setExtendedState(Frame.MAXIMIZED_BOTH);
	    frame.pack();
	    setFullScreen();
	}
	frame.setVisible(true);
    }

    public void terminateDialog() {
	if (frame != null) {
	    frame.setVisible(false);
	    frame.removeAll();
	    frame=null;
//	    frame.dispose();
	}
    }
    
    private void setFullScreen() {
	Toolkit tk = Toolkit.getDefaultToolkit();
	int xSize = ((int) tk.getScreenSize().getWidth());
	int ySize = ((int) tk.getScreenSize().getHeight());
	frame.setSize(xSize, ySize);
    }
    
    private void setHalfScreen(){
    	Toolkit tk = Toolkit.getDefaultToolkit();  
    	int xHalf = ((int) tk.getScreenSize().getWidth())/2;  
    	int yHalf = ((int) tk.getScreenSize().getHeight())/2;   
    	frame.setLocation(xHalf - (xHalf / 2),
    		yHalf - (yHalf / 2));
    	frame.setSize(xHalf,yHalf);
    }

}
