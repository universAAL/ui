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

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.universAAL.middleware.io.rdf.Submit;
import org.universAAL.ui.handler.newGui.model.FormControl.SubmitModel;

public class SubmitLAF extends SubmitModel {
    Color BackSystem=new Color(213,237,247);
    Color BackLetter=new Color (64,111,129);
    Color OverSytem=new Color(255,255,255);
    Color SelectedLetter=new Color(238,124,34);
    Color borderLine=new Color(238,124,34);

    Color BackMM=new Color (8, 68, 92);
    Color BackMML=new Color(213,237,247);
    Color borderLineMM=new Color(102, 111, 127);
    public SubmitLAF(Submit control) {
        super(control);
    }

    public JComponent getComponent() {
        // TODO Auto-generated method stub
        final JComponent button = super.getComponent();
    
        button.setBorder(new CompoundBorder
                  (BorderFactory.createLineBorder(borderLine),
                   new EmptyBorder(10,10,10,10)));
        button.setForeground(BackLetter);
        button.setBackground(BackSystem);
    
        button.addMouseListener(new MouseAdapter() {
        
           
            public void mouseEntered(MouseEvent e) {
                button.setForeground(BackLetter);
                button.setBackground(OverSytem);
            }

          
            public void mouseExited(MouseEvent e) {
                button.setBorder(new CompoundBorder
                        (BorderFactory.createLineBorder(borderLine),
                         new EmptyBorder(10,10,10,10)));
              button.setForeground(BackLetter);
              button.setBackground(BackSystem);
              
            }
            public void mouseClicked(MouseEvent e) {
                button.setForeground(SelectedLetter);
                button.setBackground(BackSystem);
            
            }
        });
        if (this.isInMainMenu()) {
            /*
             * System Buttons
             */
            button.setBorder(new CompoundBorder
                      (BorderFactory.createLineBorder(borderLineMM),
                       new EmptyBorder(10,10,10,10)));
            button.setForeground(BackMML);
            button.setBackground(BackMM);
           
            if (this.isInStandardGroup()) {
                /*
                 * system buttons in main menu
                 */
            
            
            }
            else {
                /*
                 * service buttons
                 *
                 */
            
            
            }
        }
        else {
            if (this.isInStandardGroup()) {
                /*
                 * all submits
                 */
        
                button.setBorder(new CompoundBorder
                          (BorderFactory.createLineBorder(borderLineMM),
                           new EmptyBorder(10,10,10,10)));
                button.setForeground(BackMML);
                button.setBackground(BackMM);

                button.addMouseListener(new MouseAdapter() {
                
                   
                    public void mouseEntered(MouseEvent e) {
                        button.setForeground(BackLetter);
                        button.setBackground(OverSytem);
                    }

                  
                    public void mouseExited(MouseEvent e) {
                        button.setBorder(new CompoundBorder
                                  (BorderFactory.createLineBorder(borderLineMM),
                                   new EmptyBorder(10,10,10,10)));
                        button.setForeground(BackMML);
                        button.setBackground(BackMM);

                      
                    }
                    public void mousePressed(MouseEvent e) {
                        button.setForeground(BackLetter);
                        button.setBackground(OverSytem);
                    
                    }
                });

                
            }
            if (this.isInIOGroup()) {
                /*
                 * buttons inside IO
                 */
                button.setBorder(new CompoundBorder
                          (BorderFactory.createLineBorder(borderLineMM),
                           new EmptyBorder(10,10,10,10)));
                button.setForeground(BackMML);
                button.setBackground(BackMM);

                button.addMouseListener(new MouseAdapter() {
                
                   
                    public void mouseEntered(MouseEvent e) {
                        button.setForeground(BackLetter);
                        button.setBackground(OverSytem);
                    }

                  
                    public void mouseExited(MouseEvent e) {
                        button.setBorder(new CompoundBorder
                                  (BorderFactory.createLineBorder(borderLineMM),
                                   new EmptyBorder(10,10,10,10)));
                        button.setForeground(BackMML);
                        button.setBackground(BackMM);

                      
                    }
                    public void mousePressed(MouseEvent e) {
                        button.setForeground(BackLetter);
                        button.setBackground(OverSytem);
                    
                    }
                });
            
            }
        }
        return button;
    }

}
