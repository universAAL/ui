/*******************************************************************************
 * Copyright 2011 Universidad Politécnica de Madrid
 * Copyright 2008-2014 ITACA-TSB, http://www.tsb.upv.es
 *	Instituto Tecnologico de Aplicaciones de Comunicacion 
 *	Avanzadas - Grupo Tecnologias para la Salud y el 
 *	Bienestar (TSB)
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

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.universAAL.middleware.ui.rdf.Range;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.model.FormControl.RangeModel;

/**
 * @author pabril
 *
 */
public class RangeLAF extends RangeModel {

    /**
     * Constructor.
     * @param control the {@link Range} which to model.
     */
    public RangeLAF(Range control, Renderer render) {
        super(control, render);
    }

    @Override
    public JComponent getNewComponent() {
	Range form = (Range) fc;
	needsLabel = false;
	JComponent center = super.getNewComponent();
	JPanel combined=new JPanel(new BorderLayout(5,5));
//	combined.add(new JLabel(" "), BorderLayout.EAST);
//	combined.add(new JLabel(" "), BorderLayout.NORTH);
//	combined.add(new JLabel(" "), BorderLayout.SOUTH);
	combined.add(center, BorderLayout.CENTER);
	if (form.getLabel()!=null){
	    String title=form.getLabel().getText();
	    if(title!=null && !title.isEmpty()){
		combined.add(new JLabel(title), BorderLayout.WEST);
	    }/*else{
		combined.add(new JLabel(" "), BorderLayout.WEST);
	    }*/
	}
	return combined;
    }

	@Override
	public void updateAsMissing() {
		// TODO Auto-generated method stub
		
	}

}
