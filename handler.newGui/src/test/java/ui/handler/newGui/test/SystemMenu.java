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
package ui.handler.newGui.test;

import javax.swing.JFrame;

import org.universAAL.middleware.io.rdf.Form;
import org.universAAL.middleware.io.rdf.Label;
import org.universAAL.middleware.io.rdf.Submit;
import org.universAAL.ui.handler.newGui.ModelMapper;
import org.universAAL.ui.handler.newGui.defaultLookAndFeel.Init;

public class SystemMenu {

    private static final int SYS_BTN_NO = 5;
    private static final int IO_BTN_NO = 16;

    /**
     * @param args
     */
    public static void main(String[] args) {
        /*
         * generating a RDF IO Representation
         */    
        Form sys = Form.newSystemMenu("System Dialog Test");
        for (int i = 0; i < SYS_BTN_NO; i++) {
            String s = "Sys "+Integer.toString(i);
            new Submit(sys.getStandardButtons(),
                    new Label(s, null),
                    s.toLowerCase().replace(" ", "_"));
        }
        for (int i = 0; i < IO_BTN_NO; i++) {
            String s = "Service "+Integer.toString(i);
            new Submit(sys.getIOControls(),
                    new Label(s, null),
                    s.toLowerCase().replace(" ", "_"));
        }
    
        /*
         * using GUI.Model to render
         */
        new Init().install();
        JFrame jfm = ModelMapper.getModelFor(sys).getFrame();
        jfm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jfm.setVisible(true);
    }

}
