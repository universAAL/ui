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
package org.universAAL.ui.handler.gui.swing.model.FormControl;

import java.util.Locale;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.SimpleOutput;
import org.universAAL.ui.handler.gui.swing.model.IconFactory;

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
     * the representation for {@link SimpleOutput} can either be
     * <ul>
     * <li>a {@link JCheckBox} if the {@link SimpleOutput#getContent()} is a boolean
     * type
     * <li>a {@link JTextField} if the {@link SimpleOutput#getContent()} is a String
     * and it is not {@link SimpleOutputModel#TOO_LONG}
     * <li>a {@link JTextArea} if the {@link SimpleOutput#getContent()} is
     * String and it is {@link SimpleOutputModel#TOO_LONG}
     * <li>a ?? if the {@link SimpleOutput#getContent()} is a XMLGregorianCalendar
     * <li>a ?? if the {@link SimpleOutput#getContent()} is a Duration
     * <li>a ?? if the {@link SimpleOutput#getContent()} is a Integer
     * <li>a ?? if the {@link SimpleOutput#getContent()} is a Long
     * <li>a ?? if the {@link SimpleOutput#getContent()} is a Float
     * <li>a ?? if the {@link SimpleOutput#getContent()} is a Double
     * <li>a {@link JTextField} if the {@link SimpleOutput#getContent()} is a Locale
     * </ul>
     * all of them are uneditable by the user.
     */
    public JComponent getNewComponent() {
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
         *       
         *   Primitive Types are:
         *   Boolean
		 *   XMLGregorianCalendar
		 *   Double
		 *   Duration
		 *   Float
		 *   Integer
		 *   Locale
		 *   Long
		 *   String
         */
        Object content = ((SimpleOutput) fc).getContent();
        //if (fc.getTypeURI() == (TypeMapper.getDatatypeURI(String.class))) {
        // getTypeURI returns null
        if (content instanceof String) {
            if (((String) content).length() >= TOO_LONG) {
                return new JTextArea();
            }
            else {
                return new JTextField();
            }
        }
        //if (fc.getTypeURI() == (TypeMapper.getDatatypeURI(Boolean.class))) {
        if (content instanceof Boolean) {
            needsLabel = false;
            return new JCheckBox(fc.getLabel().getText(),
                    IconFactory.getIcon(fc.getLabel().getIconURL()));
        }
		//if (inFi.getValue() instanceof XMLGregorianCalendar) {}
		//if (inFi.getValue() instanceof Duration) {}
/*			if (inFi.getValue() instanceof Integer 
				|| inFi.getValue() instanceof Long) {
			
		}
		if (inFi.getValue() instanceof Float
				|| inFi.getValue() instanceof Double) {
			
		}*/
		if (content instanceof Locale) {
			return new JTextField();
		}
        return null;
    }
    
    /**
     * Updating the {@link JComponent} with info
     */
    protected void update() {
    	Object content = ((SimpleOutput) fc).getContent();
        //if (fc.getTypeURI() == (TypeMapper.getDatatypeURI(String.class))) {
        // getTypeURI returns null
        if (content instanceof String) {
            if (((String) content).length() >= TOO_LONG) {
                JTextArea ta = (JTextArea) jc;
                ta.setText((String) content);
                ta.setEditable(false);
                //ta.setEnabled(false);
                ta.setLineWrap(true);
                ta.setWrapStyleWord(true);
            }
            else {
                JTextComponent tf = (JTextComponent) jc;
                tf.setText((String) content);
                tf.setEditable(false);
                //tf.setEnabled(false);
            }
        }
        //if (fc.getTypeURI() == (TypeMapper.getDatatypeURI(Boolean.class))) {
        if (content instanceof Boolean) {
            JCheckBox cb = (JCheckBox) jc;
            cb.setSelected(((Boolean) content).booleanValue());
            cb.setEnabled(false);
        }
		//if (inFi.getValue() instanceof XMLGregorianCalendar) {}
		//if (inFi.getValue() instanceof Duration) {}
/*			if (inFi.getValue() instanceof Integer 
				|| inFi.getValue() instanceof Long) {
			
		}
		if (inFi.getValue() instanceof Float
				|| inFi.getValue() instanceof Double) {
			
		}*/
		if (content instanceof Locale) {
			JTextField tf = (JTextField) jc;
			tf.setText(((Locale) content).getDisplayLanguage());
			tf.setEditable(false);
		}
        super.update();
    }

}
