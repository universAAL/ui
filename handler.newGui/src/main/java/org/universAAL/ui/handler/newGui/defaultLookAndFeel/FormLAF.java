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
package org.universAAL.ui.handler.newGui.defaultLookAndFeel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.universAAL.middleware.io.rdf.Form;
import org.universAAL.ui.handler.newGui.model.FormModel;

/**
 * The Look and Feel for Forms
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 * @author pabril
 * @see FormModel
 */
public class FormLAF extends FormModel  {

    /**
     * internal accounting for the frame being displayed.
     */
    private JFrame frame = null;

    /**
     * Constructor.
     * @param f
     *     {@link Form} which to model.
     */
    public FormLAF(Form f) {
        super(f);
    }

    /**
     * get the io panel wrapped in a scroll pane.
     * @return
     *         the {@link FormModel#getIOPanel} wrapped in a {@link JScrollPane}.
     */
    protected JScrollPane getIOPanelScroll() {
        JPanel ioPanel = super.getIOPanel();
        JScrollPane sp = new JScrollPane(ioPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //ioPanelDim = ioPanel.getPreferredSize();
        ioPanel.setPreferredSize(new Dimension(sp.getSize().width,
        //        ioPanel.getPreferredSize().height)
                ioPanel.getHeight()));
        //FIXME resize Layout+scroll
        return sp;
    }

    /**
     * get the submit panel wrapped in a scroll pane.
     * @return
     *         the {@link FormModel#getSubmitPanel} wrapped in a {@link JScrollPane}.
     */
    protected JScrollPane getSubmitPanelScroll(int depth) {
        JPanel submit = super.getSubmitPanel(depth);
        submit.setLayout(new BoxLayout(submit, BoxLayout.Y_AXIS));
        return new JScrollPane(submit,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    /**
     * get the system panel wrapped in a scroll pane.
     * @return
     *         the {@link FormModel#getSystemPanel} wrapped in a {@link JScrollPane}.
     */
    protected JScrollPane getSystemPanelScroll() {
        return new JScrollPane(super.getSystemPanel(),
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    /**
     * generate the header panel.
     * @return
     *         a pannel with universAAL icon in it.
     */
    protected JPanel getHeader() {
            JPanel header = new JPanel();
            ImageIcon icon = new ImageIcon(
                    (getClass().getResource("/main/UniversAAl_logo.png")));
            JLabel logo = new JLabel(icon);
            header.add(logo);
            return (JPanel) header;
        }

    /**
     * render the frame for the {@link Form}.
     */
    public JFrame getFrame() {
        if (form.isMessage()) {
            frame = new JFrame(form.getTitle());
            JScrollPane io = getIOPanelScroll();
            JScrollPane sub = new JScrollPane(super.getSubmitPanel(),
                    JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            frame.add(io, BorderLayout.CENTER);
            frame.add(sub, BorderLayout.SOUTH);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack();
        }
        if (form.isSystemMenu()) {
            frame = new JFrame(form.getTitle());
            frame.add(getHeader(), BorderLayout.NORTH);
            frame.add(getIOPanel(), BorderLayout.CENTER);
            frame.add(getSystemPanelScroll(), BorderLayout.SOUTH);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
            //f.setUndecorated(true);
            frame.pack();
        }
        if (form.isStandardDialog()) {
            /*
             *  some further LAF can be done here:
             *   if only submints (no sub dialogs)
             *     and <4 (and priority hi?)
             *        then show like a popup.
             */
            frame = new JFrame(form.getTitle());
            frame.add(getHeader(), BorderLayout.NORTH);
            JScrollPane io = getIOPanelScroll();
            JScrollPane sub = getSubmitPanelScroll(0);
            JScrollPane sys = getSystemPanelScroll();
            frame.add(io, BorderLayout.CENTER);
            frame.add(sub, BorderLayout.EAST);
            frame.add(sys, BorderLayout.SOUTH);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
            //f.setUndecorated(true);
            frame.pack();
        }
        if (form.isSubdialog()) {
            frame = new JFrame(form.getTitle());
            frame.add(getHeader(), BorderLayout.NORTH);
            JScrollPane sub = getSubmitPanelScroll(0);
            JScrollPane sys = getSystemPanelScroll();
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
            //f.setUndecorated(true);
            frame.pack();
        }
        return frame;
    }

    /** {@inheritDoc} */
    public void terminateDialog() {
        if(frame != null){
            frame.dispose();
        }
    }
}
