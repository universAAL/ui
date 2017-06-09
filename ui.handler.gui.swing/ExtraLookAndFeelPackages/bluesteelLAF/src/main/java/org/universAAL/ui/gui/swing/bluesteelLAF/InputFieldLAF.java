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
package org.universAAL.ui.gui.swing.bluesteelLAF;

import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.jdesktop.swingx.JXDatePicker;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.ui.rdf.Input;
import org.universAAL.middleware.ui.rdf.InputField;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.FormControl.InputFieldModel;

/**
 * @author pabril
 *
 */
public class InputFieldLAF extends InputFieldModel {

	/**
	 * Constructor
	 * 
	 * @param control
	 *            the {@link InputField} which to model.
	 */
	public InputFieldLAF(InputField control, Renderer render) {
		super(control, render);
	}

	/** {@inheritDoc} */
	public void update() {
		if (jc != null) {
			super.update();
			jc.setFont(Init.getInstance(getRenderer()).getColorLAF().getplain());
			if (jc instanceof JTextComponent) {
				((JTextComponent) jc).setSelectionColor(Init.getInstance(getRenderer()).getColorLAF().getOrange());
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public JComponent getNewComponent() {
		InputField inFi = (InputField) fc;
		if (inFi.getValue() instanceof XMLGregorianCalendar) {
			Date d = ((XMLGregorianCalendar) (inFi.getValue())).toGregorianCalendar().getTime();
			JXDatePicker dp = new JXDatePicker(d);
			return dp;
		}
		return super.getNewComponent();
	}

	@Override
	public void updateAsMissing() {
		JLabel l = getLabelModel().getComponent();
		l.setForeground(Init.getInstance(getRenderer()).getColorLAF().getAlert());
		l.setText(getAlertString());
	}

	/** {@ inheritDoc} */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JXDatePicker) {
			JXDatePicker dp = (JXDatePicker) e.getSource();
			try {
				GregorianCalendar c = new GregorianCalendar();
				c.setTime(dp.getDate());
				XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
				((Input) fc).storeUserInput(date);
			} catch (DatatypeConfigurationException e1) {
				LogUtils.logWarn(getRenderer().getModuleContext(), getClass(), "actionPerformed",
						new String[] { "could not translate date to XMLGregorianCalendar" }, e1);
			}
		} else {
			super.actionPerformed(e);
		}
	}

}
