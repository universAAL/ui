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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import org.universAAL.middleware.io.rdf.Submit;
import org.universAAL.ui.handler.newGui.model.FormControl.SubmitModel;

public class SubmitLAF extends SubmitModel {

	public SubmitLAF(Submit control) {
		super(control);
	}

	public JComponent getComponent() {
		// TODO Auto-generated method stub
		final JComponent button = super.getComponent();
		
		button.setBorder(new CompoundBorder
			      (BorderFactory.createLineBorder(ColorLAF.getborderLine()),
			       new EmptyBorder(10,10,10,10)));
		button.setForeground(ColorLAF.getBackLetter());
        button.setBackground(ColorLAF.getBackSystem());
		
		button.addMouseListener(new MouseAdapter() {
			
            
            public void mouseEntered(MouseEvent e) {
            	button.setForeground(ColorLAF.getBackLetter()); 
            	button.setBackground(ColorLAF.getOverSytem());
            }

           
            public void mouseExited(MouseEvent e) {
            	button.setBorder(new CompoundBorder
      			      (BorderFactory.createLineBorder(ColorLAF.getborderLine()),
      			       new EmptyBorder(10,10,10,10)));
      		button.setForeground(ColorLAF.getBackLetter());
              button.setBackground(ColorLAF.getBackSystem());
               
            }
            public void mouseClicked(MouseEvent e){
            	button.setForeground(ColorLAF.getSelectedLetter());
            	button.setBackground(ColorLAF.getBackSystem());
            	
            }
        });
		if (this.isInMainMenu()) {
			/*
			 * System Buttons 
			 */
			button.setBorder(new CompoundBorder
				      (BorderFactory.createLineBorder(ColorLAF.getborderLineMM()),
				       new EmptyBorder(10,10,10,10)));
			button.setForeground(ColorLAF.getBackMML());
	        button.setBackground(ColorLAF.getBackMM());

	        button.addMouseListener(new MouseAdapter() {
				
	            
	            public void mouseEntered(MouseEvent e) {
	            	button.setForeground(ColorLAF.getBackLetter()); 
	            	button.setBackground(ColorLAF.getOverSytem());
	            }

	           
	            public void mouseExited(MouseEvent e) {
	            	button.setBorder(new CompoundBorder
						      (BorderFactory.createLineBorder(ColorLAF.getborderLineMM()),
						       new EmptyBorder(10,10,10,10)));
					button.setForeground(ColorLAF.getBackMML());
			        button.setBackground(ColorLAF.getBackMM());

	               
	            }
	            public void mousePressed(MouseEvent e){
	            	button.setForeground(ColorLAF.getBackLetter());
	            	button.setBackground(ColorLAF.getOverSytem());
	            	
	            }
	        });

	        
			if (this.isInStandardGroup()) {
				/*
				 * system buttons in main menu
				 */
				button.setBorder(new CompoundBorder
					      (BorderFactory.createLineBorder(ColorLAF.getborderLineMM()),
					       new EmptyBorder(10,10,10,10)));
				button.setForeground(ColorLAF.getBackMML());
		        button.setBackground(ColorLAF.getBackMM());

		        button.addMouseListener(new MouseAdapter() {
					
		            
		            public void mouseEntered(MouseEvent e) {
		            	button.setForeground(ColorLAF.getBackLetter()); 
		            	button.setBackground(ColorLAF.getOverSytem());
		            }

		           
		            public void mouseExited(MouseEvent e) {
		            	button.setBorder(new CompoundBorder
							      (BorderFactory.createLineBorder(ColorLAF.getborderLineMM()),
							       new EmptyBorder(10,10,10,10)));
						button.setForeground(ColorLAF.getBackMML());
				        button.setBackground(ColorLAF.getBackMM());

		               
		            }
		            public void mousePressed(MouseEvent e){
		            	button.setForeground(ColorLAF.getBackLetter());
		            	button.setBackground(ColorLAF.getOverSytem());
		            	
		            }
		        });

				
			}
			else {
				/*
				 * service buttons
				 * 
				 */
				button.setBorder(new CompoundBorder
					      (BorderFactory.createLineBorder(ColorLAF.getborderLine()),
					       new EmptyBorder(10,10,10,10)));
				button.setForeground(ColorLAF.getBackLetter());
		        button.setBackground(ColorLAF.getBackSystem());
				
				button.addMouseListener(new MouseAdapter() {
					
		            
		            public void mouseEntered(MouseEvent e) {
		            	button.setForeground(ColorLAF.getBackLetter()); 
		            	button.setBackground(ColorLAF.getOverSytem());
		            }

		           
		            public void mouseExited(MouseEvent e) {
		            	button.setBorder(new CompoundBorder
		      			      (BorderFactory.createLineBorder(ColorLAF.getborderLine()),
		      			       new EmptyBorder(10,10,10,10)));
		      		button.setForeground(ColorLAF.getBackLetter());
		              button.setBackground(ColorLAF.getBackSystem());
		               
		            }
		            public void mouseClicked(MouseEvent e){
		            	button.setForeground(ColorLAF.getSelectedLetter());
		            	button.setBackground(ColorLAF.getBackSystem());
		            	
		            }
		        });
				
			}
		} 
		else {
			if (this.isInStandardGroup()) {
				/*
				 * all submits
				 */
			
				button.setBorder(new CompoundBorder
					      (BorderFactory.createLineBorder(ColorLAF.getborderLineMM()),
					       new EmptyBorder(10,10,10,10)));
				button.setForeground(ColorLAF.getBackMML());
		        button.setBackground(ColorLAF.getBackMM());

		        button.addMouseListener(new MouseAdapter() {
					
		            
		            public void mouseEntered(MouseEvent e) {
		            	button.setForeground(ColorLAF.getBackLetter()); 
		            	button.setBackground(ColorLAF.getOverSytem());
		            }

		           
		            public void mouseExited(MouseEvent e) {
		            	button.setBorder(new CompoundBorder
							      (BorderFactory.createLineBorder(ColorLAF.getborderLineMM()),
							       new EmptyBorder(10,10,10,10)));
						button.setForeground(ColorLAF.getBackMML());
				        button.setBackground(ColorLAF.getBackMM());

		               
		            }
		            public void mousePressed(MouseEvent e){
		            	button.setForeground(ColorLAF.getBackLetter());
		            	button.setBackground(ColorLAF.getOverSytem());
		            	
		            }
		        });

					
			}
			if (this.isInIOGroup()) {
				/*
				 * buttons inside IO
				 */
				button.setBorder(new CompoundBorder
					      (BorderFactory.createLineBorder(ColorLAF.getborderLine()),
					       new EmptyBorder(10,10,10,10)));
				button.setForeground(ColorLAF.getBackLetter());
		        button.setBackground(ColorLAF.getBackSystem());
				
				button.addMouseListener(new MouseAdapter() {
					
		            
		            public void mouseEntered(MouseEvent e) {
		            	button.setForeground(ColorLAF.getBackLetter()); 
		            	button.setBackground(ColorLAF.getOverSytem());
		            }

		           
		            public void mouseExited(MouseEvent e) {
		            	button.setBorder(new CompoundBorder
		      			      (BorderFactory.createLineBorder(ColorLAF.getborderLine()),
		      			       new EmptyBorder(10,10,10,10)));
		      		button.setForeground(ColorLAF.getBackLetter());
		              button.setBackground(ColorLAF.getBackSystem());
		               
		            }
		            public void mouseClicked(MouseEvent e){
		            	button.setForeground(ColorLAF.getSelectedLetter());
		            	button.setBackground(ColorLAF.getBackSystem());
		            	
		            }
		        });
				
			}
		}
		return button;
	}

}
