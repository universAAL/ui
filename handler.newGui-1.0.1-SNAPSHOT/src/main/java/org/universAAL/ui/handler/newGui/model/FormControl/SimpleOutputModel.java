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
package org.universAAL.ui.handler.newGui.model.FormControl;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.universAAL.middleware.io.rdf.FormControl;
import org.universAAL.middleware.io.rdf.SimpleOutput;
import org.universAAL.ui.handler.newGui.model.IconFactory;

/**
 *
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 * @see SimpleOutput
 */
public class SimpleOutputModel extends OutputModel {

    /**
     * threshold for selecting between a {@link JTextArea} and
     * a {@link JTextField}
     */
    protected static final int TOO_LONG = 20;

    /**
     * Constructor.
     * @param control
     *       the {@link FormControl} which to model.
     */
    public SimpleOutputModel(SimpleOutput control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     * @return
     *     either a {@link JTextArea} or a {@link JTextField} if output is a {@link String}.
     *     or it can be a {@link JCheckBox} if the output is a {@link Boolean}.
     *     all of them are uneditable by the user.
     */
    public JComponent getComponent() {
        /*
         *  TODO
         *   Examine value type and select the best component
         *   that represents it
         *   if string use
         *       (disabled) input field or label for short length
         *      (disabled) textarea for long length
         *   if boolean use
         *      (disabled) checkbock
         *   if Date
         *       ??
         *   if number use
         *       ??
         *   if ?? use
         *       progress bar
         *   if an ontology class do
         *       ??
         */
        Object content = ((SimpleOutput) fc).getContent();
        //if (fc.getTypeURI() == (TypeMapper.getDatatypeURI(String.class))) {
        // getTypeURI returns null
        if (content instanceof String) {
            if (((String) content).length() >= TOO_LONG) {
                JTextArea ta = new JTextArea();
                ta.setText((String) content);
                ta.setEditable(false);
                //ta.setEnabled(false);
                ta.setLineWrap(true);
                ta.setWrapStyleWord(true);
                ta.setName(fc.getURI());
                return ta;
            }
            else {
                JTextComponent tf = new JTextField();
                tf.setText((String) content);
                tf.setEditable(false);
                //tf.setEnabled(false);
                tf.setToolTipText(fc.getHelpString());
                tf.setName(fc.getURI());
                return tf;
            }
        }
        //if (fc.getTypeURI() == (TypeMapper.getDatatypeURI(Boolean.class))) {
        if (content instanceof Boolean) {
            JCheckBox cb = new JCheckBox(fc.getLabel().getText(),
                    IconFactory.getIcon(fc.getLabel().getIconURL()));
            needsLabel = false;
            cb.setSelected(((Boolean) content).booleanValue());
            cb.setEnabled(false);
            cb.setToolTipText(fc.getHelpString());
            cb.setName(fc.getURI());
            return cb;
        }
        return null;
    }

}
