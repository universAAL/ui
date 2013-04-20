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
package org.universAAL.ui.handler.gui.swing.model.FormControl;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.universAAL.middleware.ui.rdf.Range;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.FormControl.support.TaskQueue;

/**
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 * @see Range
 */
public class RangeModel extends InputModel
implements ChangeListener {

	/**
	 * Threshold where to decide if it should be rendenred as {@link JSpinner} or
	 * as {@link JSlider}.
	 */
    private static final int SPINNER_SLIDER_THRESHOLD = 25;
    
    /**
     * Minimum value by the model.
     */
	private int mnValue;
	
	/**
     * Maximum value by the model.
     */
	private int mxValue;
	
	/**
     * Actual value by the model.
     */
	private int initValue;

    /**
     * Constructor.
     * @param control the {@link Range} which to model.
     */
    public RangeModel(Range control, Renderer render) {
        super(control, render);
        Comparable min_Value = ((Range) fc).getMinValue();
        mnValue = ((Integer) min_Value).intValue();
        Comparable max_Value = ((Range) fc).getMaxValue();
        mxValue = ((Integer) max_Value).intValue();
        initValue = ((Integer) fc.getValue()).intValue();
    }

    /**
     * Ranges can yield a {@link JSpinner} if the specified range
     * is less than a threshold, or it can also be {@link JSlider}.
     * @return {@inheritDoc}
     */
    public JComponent getNewComponent() {
        if ((mxValue - mnValue) < SPINNER_SLIDER_THRESHOLD) {
            JSpinner spinner = new JSpinner(getSpinnerModel());
            spinner.addChangeListener(this);
            return spinner;
        }
        else {
            JSlider slider = new JSlider(mnValue, mxValue, initValue);
            slider.addChangeListener(this);
            return slider;
        }
    }
    
    /**
     * Create a {@link SpinnerModel} based on the values obtained from model
     * @return
     */
    private SpinnerModel getSpinnerModel() {
        return new SpinnerNumberModel(initValue,
                mnValue,
                mxValue,
                ((Range) fc).getStep().intValue());
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isValid(JComponent component) {
        // Swing makes sure it's all ways valid
        // XXX check the above affirmation
        return true;
    }

    /**
     * When a range is change, the input will be stored
     * {@inheritDoc}
     */
    public void stateChanged(final ChangeEvent e) {
    	TaskQueue.addTask(new Runnable() {
    		public void run() {
    			int value;
    			// Check UserInput Type is Integer!
    			if (e.getSource() instanceof JSpinner) {
    				value = ((Integer) ((JSpinner) e.getSource()).getValue()).intValue();
    			}
    			else {
    				value = ((JSlider) e.getSource()).getValue();
    			}
    			((Range) fc).storeUserInput(new Integer(value));
    		}
    	});
    }

}
