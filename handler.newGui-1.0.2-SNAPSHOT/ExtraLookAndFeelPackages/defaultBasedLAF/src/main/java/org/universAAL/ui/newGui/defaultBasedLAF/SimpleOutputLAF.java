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
package org.universAAL.ui.newGui.defaultBasedLAF;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

import org.universAAL.middleware.ui.rdf.SimpleOutput;
import org.universAAL.ui.handler.newGui.model.FormControl.SimpleOutputModel;

/**
 * @author pabril
 *
 */
public class SimpleOutputLAF extends SimpleOutputModel {

    /**
     * Constructor.
     * @param control the {@link SimpleOutput} which to model.
     */
    public SimpleOutputLAF(SimpleOutput control) {
        super(control);

    }

    /** {@inheritDoc} */
    public JComponent getComponent() {
        Object content = ((SimpleOutput) fc).getContent();
        JComponent comp = super.getComponent();
        if (content instanceof String) {
            if (((String) content).length() >= TOO_LONG) {
                JTextArea ta = (JTextArea) comp;
                ta.getAccessibleContext().setAccessibleName(ta.getName());
                ta.setLineWrap(true);
                ta.setWrapStyleWord(true);
                ta.getAccessibleContext();
                ta.setFont(ColorLAF.getplain());
                ta.setLineWrap(true);
                ta.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
                ta.setForeground(ColorLAF.getfont());
                JScrollPane sp = new JScrollPane(ta);
                sp.getAccessibleContext();
                return sp;
            }
            else {
                JTextComponent tf = (JTextComponent) comp;
                tf.getAccessibleContext().setAccessibleName(tf.getText());
                tf.setFont(ColorLAF.getplain());
                tf.setPreferredSize(new Dimension(150, 30));
                tf.setForeground(ColorLAF.getBackMM());
                return tf;
            }
        }
        if (content instanceof Boolean) {
            JCheckBox cb = (JCheckBox) comp;
            cb.getAccessibleContext().setAccessibleName(cb.getName());
            return cb;
        }
        return comp;
    }


}
