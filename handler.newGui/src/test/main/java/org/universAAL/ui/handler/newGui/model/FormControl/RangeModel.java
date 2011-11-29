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
/**
 * 
 */
package org.universAAL.ui.handler.newGui.model.FormControl;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.universAAL.middleware.io.rdf.Range;

/**
 * @author <a href="mailto:amedrano@lst.tfo.upm.es>amedrano</a>
 * @see Range
 */
public class RangeModel extends InputModel
implements ChangeListener {
	
	private static final int SPINNER_SLIDER_THRESHOLD = 25;

	public RangeModel(Range control) {
		super(control);
	}

	/**
	 * Ranges can yield a {@link JSpinner} if the specified range
	 * is less than a threshold, or it can also be {@link JSlider}.
	 */
	public JComponent getComponent() {
		Comparable min_Value = ((Range) fc).getMinValue();
		int mnValue = ((Integer) min_Value).intValue();
		Comparable max_Value = ((Range) fc).getMaxValue();
		int mxValue = ((Integer) max_Value).intValue();
		int initValue = ((Integer) fc.getValue()).intValue();

		if ((mxValue - mnValue) < SPINNER_SLIDER_THRESHOLD){
			SpinnerModel sModel = new SpinnerNumberModel(initValue, 
					mnValue, 
					mxValue, 
					((Range) fc).getStep().intValue());
			JSpinner spinner = new JSpinner(sModel);
			spinner.addChangeListener(this);
			spinner.setName(fc.getURI());
			return spinner;
		}
		else{
			JSlider slider = new JSlider(mnValue, mxValue, initValue);
			slider.addChangeListener(this);
			slider.setName(fc.getURI());
			return slider;
		}
	}
	
	public boolean isValid(JComponent component) {
		// Swing makes sure it's all ways valid
		// TODO check the above affirmation
		return true;
	}

	/**
	 * when a range is change, it will produce an input event
	 */
	public void stateChanged(ChangeEvent e) {
		int value;
		//TODO Check UserInput Type is Integer!
		if (e.getSource() instanceof JSpinner) {
			/*
			 *  TODO: Check if JPspiner.getValue in this context
			 *   will return a int.
			 */
			value = ((Integer)((JSpinner)e.getSource()).getValue()).intValue();
		}
		else {
			value = ((JSlider)e.getSource()).getValue();
		}
		((Range) fc).storeUserInput(new Integer(value));
	}

}
