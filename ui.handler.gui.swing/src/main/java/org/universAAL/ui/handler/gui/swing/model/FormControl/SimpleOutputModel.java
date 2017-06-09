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
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.SimpleOutput;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.IconFactory;

/**
 *
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 * @see SimpleOutput
 */
public class SimpleOutputModel extends OutputModel {

	/**
	 * threshold for selecting between a {@link JTextArea} and a
	 * {@link JTextField}
	 */
	protected static final int TOO_LONG = 50;

	/**
	 * Constructor.
	 * 
	 * @param control
	 *            the {@link FormControl} which to model.
	 */
	public SimpleOutputModel(SimpleOutput control, Renderer render) {
		super(control, render);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return the representation for {@link SimpleOutput} can either be
	 *         <ul>
	 *         <li>a {@link JCheckBox} if the {@link SimpleOutput#getContent()}
	 *         is a boolean type
	 *         <li>a {@link JLabel} if the {@link SimpleOutput#getContent()} is
	 *         a String, it is not {@link SimpleOutputModel#TOO_LONG}, and it
	 *         has no label.
	 *         <li>a {@link JTextField} if the {@link SimpleOutput#getContent()}
	 *         is a String and it is not {@link SimpleOutputModel#TOO_LONG}
	 *         <li>a {@link JTextArea} if the {@link SimpleOutput#getContent()}
	 *         is String and it is {@link SimpleOutputModel#TOO_LONG}
	 *         <li>a ?? if the {@link SimpleOutput#getContent()} is a
	 *         XMLGregorianCalendar
	 *         <li>a ?? if the {@link SimpleOutput#getContent()} is a Duration
	 *         <li>a ?? if the {@link SimpleOutput#getContent()} is a Integer
	 *         <li>a ?? if the {@link SimpleOutput#getContent()} is a Long
	 *         <li>a ?? if the {@link SimpleOutput#getContent()} is a Float
	 *         <li>a ?? if the {@link SimpleOutput#getContent()} is a Double
	 *         <li>a {@link JTextField} if the {@link SimpleOutput#getContent()}
	 *         is a Locale
	 *         </ul>
	 *         all of them are uneditable by the user.
	 */
	public JComponent getNewComponent() {
		/*
		 * TODO Examine value type and select the best component that represents
		 * it if string use (disabled) input field or label for short length
		 * (disabled) textarea for long length if boolean use (disabled)
		 * checkbock if Date ?? if number use ?? if ?? use progress bar if an
		 * ontology class do ??
		 * 
		 * Primitive Types are: Boolean XMLGregorianCalendar Double Duration
		 * Float Integer Locale Long String
		 */
		Object content = ((SimpleOutput) fc).getContent();
		// if (fc.getTypeURI() == (TypeMapper.getDatatypeURI(String.class))) {
		// getTypeURI returns null
		if (content instanceof String) {
			if (((String) content).length() >= TOO_LONG) {
				return new JTextArea();
			} else if (fc.getLabel() == null || fc.getLabel().getText() == null || fc.getLabel().getText().isEmpty()) {
				return new JLabel();
			} else {
				return new JTextField();
			}
		}
		// if (fc.getTypeURI() == (TypeMapper.getDatatypeURI(Boolean.class))) {
		if (content instanceof Boolean) {
			needsLabel = false;
			return new JCheckBox(fc.getLabel().getText(), IconFactory.getIcon(fc.getLabel().getIconURL()));
		}
		// if (content instanceof XMLGregorianCalendar) {
		// return new JTextField(10);
		// }
		// if (content instanceof Duration) {}
		if (content instanceof Integer || content instanceof Long) {
			return new JTextField(6);
		}
		if (content instanceof Float || content instanceof Double) {
			return new JTextField(6);
		}
		if (content instanceof Locale) {
			return new JTextField();
		}
		return null;
	}

	/**
	 * Updating the {@link JComponent} with info
	 */
	public void update() {
		Object content = ((SimpleOutput) fc).getContent();
		// if (fc.getTypeURI() == (TypeMapper.getDatatypeURI(String.class))) {
		// getTypeURI returns null
		if (jc instanceof JTextComponent && content instanceof String) {
			JTextComponent ta = (JTextComponent) jc;
			ta.setText((String) content);
			ta.setEditable(false);
		}
		if (jc instanceof JLabel) {
			((JLabel) jc).setText((String) content);
		}
		// if (fc.getTypeURI() == (TypeMapper.getDatatypeURI(Boolean.class))) {
		if (jc instanceof JCheckBox && content instanceof Boolean) {
			JCheckBox cb = (JCheckBox) jc;
			cb.setSelected(((Boolean) content).booleanValue());
			cb.setEnabled(false);
		}
		// if (content instanceof XMLGregorianCalendar) {}
		// if (content instanceof Duration) {}
		if (jc instanceof JTextField && content instanceof Integer) {
			JTextField tf = (JTextField) jc;
			tf.setText(((Integer) content).toString());
			tf.setEditable(false);
		}
		if (jc instanceof JTextField && content instanceof Long) {
			JTextField tf = (JTextField) jc;
			tf.setText(((Long) content).toString());
			tf.setEditable(false);
		}
		if (jc instanceof JTextField && content instanceof Float) {
			JTextField tf = (JTextField) jc;
			tf.setText(((Float) content).toString());
			tf.setEditable(false);
		}
		if (jc instanceof JTextField && content instanceof Double) {
			JTextField tf = (JTextField) jc;
			tf.setText(((Double) content).toString());
			tf.setEditable(false);
		}
		if (jc instanceof JTextField && content instanceof Locale) {
			JTextField tf = (JTextField) jc;
			tf.setText(((Locale) content).getDisplayLanguage());
			tf.setEditable(false);
		}
		super.update();
	}

}
