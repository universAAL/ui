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
package org.universAAL.ui.gui.swing.waveLAF;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;

import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.ui.gui.swing.waveLAF.support.ColorBorder;
import org.universAAL.ui.gui.swing.waveLAF.support.GradientLAF;
import org.universAAL.ui.gui.swing.waveLAF.support.ShadowBorder;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.FormModel;

/**
 * The Look and Feel for Forms
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 * @author pabril
 * @see FormModel
 */
public class FormLAF extends FormModel  {
	   private static final Color GRAY = new Color(0, 51, 255);
    /**
     * internal accounting for the frame being displayed.
     */
    private JFrame frame = null;

    /**
     * Constructor.
     * @param f
     *     {@link Form} which to model.
     */
    public FormLAF(Form f, Renderer render) {
        super(f, render);
    }

    /**
     * get the io panel wrapped in a scroll pane.
     * @return
     *         the {@link FormModel#getIOPanel} wrapped in a {@link JScrollPane}.
     */
    protected JComponent getIOPanelScroll() {
    	/*
        JPanel ioPanel = super.getIOPanel();
        JScrollPane sp = new JScrollPane(ioPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //ioPanelDim = ioPanel.getPreferredSize();
        ioPanel.setPreferredSize(new Dimension(sp.getSize().width,
        //        ioPanel.getPreferredSize().height)
                ioPanel.getHeight()));
        //FIXME resize Layout+scroll
         */
    	
    	JPanel ioPanel = super.getIOPanel();
    	ioPanel.setOpaque(false);
        JScrollPane sp = new JScrollPane(ioPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    	sp.getVerticalScrollBar().setPreferredSize(new Dimension(50, 50));
    	sp.getHorizontalScrollBar().setPreferredSize(new Dimension(50, 50));
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        return  sp;
    }

    /**
     * get the submit panel wrapped in a scroll pane.
     * @return
     *         the {@link FormModel#getSubmitPanel} wrapped in a {@link JScrollPane}.
     */
    protected JScrollPane getSubmitPanelScroll(int depth) {
        JPanel submit = super.getSubmitPanel(depth);
        submit.setOpaque(false);
        JScrollPane sp = new JScrollPane(submit,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        return sp;
    }

    /**
     * get the system panel wrapped in a scroll pane.
     * @return
     *         the {@link FormModel#getSystemPanel} wrapped in a {@link JScrollPane}.
     */
    protected JScrollPane getSystemPanelScroll() {
    	JPanel system = super.getSystemPanel();
        system.setLayout(new BoxLayout(system, BoxLayout.X_AXIS));
        system.setOpaque(false);
        JScrollPane sp = new JScrollPane(super.getSystemPanel(),
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        //TODO set transparent borders
        return sp;
    }

    /**
     * generate the header panel.
     * @return
     *         a pannel with universAAL icon in it.
     */
    protected JPanel getHeader() {
    	JPanel header = new JPanel();//new GradientLAF();
    	header.setOpaque(false);
    	ImageIcon icon = new ImageIcon(
    			(getClass().getResource("/images/Banner.png")));
    	icon.setDescription("UniversAAL Logo Image");
    	JLabel logo = new JLabel(icon);
    	logo.getAccessibleContext().setAccessibleName("UniversAAL Logo");
    	//JComponent nuevo=new GradientLAF(); 
    	header.add(logo);
    	//header.add(nuevo);

    	return (JPanel) header;
    }

    /**
     * render the frame for the {@link Form}.
     */
    public JFrame getFrame() {
    	if (frame == null) {
            frame = new JFrame();
            JPanel content = new GradientLAF();
            content.setLayout(new BorderLayout());
            frame.setContentPane(content);
    	}
    	frame.setTitle(form.getTitle());
        if (form.isMessage()) {
            frame.getAccessibleContext().setAccessibleName(form.getTitle());
            
             JScrollPane io = (JScrollPane) getIOPanelScroll();
      
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
        if (form.isStandardDialog() ) {
            /*
             *  some further LAF can be done here:
             *   if only submints (no sub dialogs)
             *     and <4 (and priority hi?)
             *        then show like a popup.
             */
            frame.getAccessibleContext().setAccessibleName(form.getTitle());
            frame.add(getHeader(), BorderLayout.NORTH);
           
            JScrollPane io = (JScrollPane) getIOPanelScroll();
           
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
            frame.getAccessibleContext().setAccessibleName(form.getTitle());
            frame.add(getHeader(), BorderLayout.NORTH);
            JScrollPane sub = getSubmitPanelScroll(0);
            sub.getAccessibleContext().setAccessibleName(SUB_NAME);
            JScrollPane sys = getSystemPanelScroll();
            sys.getAccessibleContext().setAccessibleName(SYS_NAME);
            JPanel subpanel = new JPanel(new BorderLayout());
            subpanel.add(getIOPanelScroll(), BorderLayout.CENTER);
            
            subpanel.setBorder(new CompoundBorder(new ColorBorder(GRAY, 0, 12, 0, 12),
					  new ShadowBorder()));
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
    
    private void setFullScreen(){
    	frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
//    	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();  
//   		Dimension window = frame.getSize(); 
//   		frame.setLocation(
//            (screen.width - window.width) / 2,
//            (screen.height - window.height) / 2);	
    }

    /** {@inheritDoc} */
    public void terminateDialog() {
    	if (frame != null) {
    		frame.dispose();
    		frame = null;
    	}
       //frame.getContentPane().removeAll();
    }
}
