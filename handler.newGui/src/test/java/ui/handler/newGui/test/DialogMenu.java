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

import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.middleware.rdf.PropertyPath;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.rdf.TypeMapper;
import org.universAAL.middleware.owl.IntRestriction;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.InputField;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.Range;
import org.universAAL.middleware.ui.rdf.Select;
import org.universAAL.middleware.ui.rdf.Select1;
import org.universAAL.middleware.ui.rdf.SimpleOutput;
import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.middleware.ui.rdf.TextArea;
import org.universAAL.ui.handler.newGui.ModelMapper;
import org.universAAL.ui.handler.newGui.Renderer;
import org.universAAL.ui.handler.newGui.defaultLookAndFeel.Init;

public class DialogMenu {

    private static final int SYS_BTN_NO = 5;
    private static final int SUB_BTN_NO = 7;

    /**
     * @param args
     */
    public static void main(String[] args) {
        /*
         * generating a RDF IO Representation
         */

        Form dlg = Form.newDialog("System Dialog Test",(Resource)null);
        for (int i = 0; i < SYS_BTN_NO; i++) {
            String s = "Sys "+Integer.toString(i);
            new Submit(dlg.getStandardButtons(),
                    new Label(s, null),
                    s.toLowerCase().replace(" ", "_"));
        }
        for (int i = 0; i < SUB_BTN_NO; i++) {
            String s = "Submit "+Integer.toString(i);
            new Submit(dlg.getSubmits(),
                    new Label(s, null),
                    s.toLowerCase().replace(" ", "_"));
        }

        Group g1 = new Group(dlg.getIOControls(), new Label("Group 1", ""), null, null, null);
        Group g2 = new Group(dlg.getIOControls(), new Label("Group 2", ""), null, null, null);
        Group g3 = new Group(g2, new Label("Selects", ""), null, null, null);
        new InputField(
                g1,
                new Label("Input 1", (String) null),
                new PropertyPath(
                        null,
                        false,
                        new String[] { "http://ontology.aal-persona.org/Tests.owl#input1" }),
                        MergedRestriction.getAllValuesRestrictionWithCardinality(
                                "http://ontology.aal-persona.org/Tests.owl#input1",
                                TypeMapper.getDatatypeURI(Boolean.class), 1, 1),
                Boolean.TRUE);

        new InputField(
                g1,
                new Label("Input 2", (String) null),
                new PropertyPath(
                        null,
                        false,
                        new String[] { "http://ontology.aal-persona.org/Tests.owl#input2" }),
                        MergedRestriction.getAllValuesRestrictionWithCardinality(
                        		"http://ontology.aal-persona.org/Tests.owl#input2",
                        		TypeMapper.getDatatypeURI(Boolean.class), 1, 1),
                        		Boolean.FALSE);

        new InputField(g1,
                new Label("Input 3",""),
                new PropertyPath(
                        null,
                        false,
                        new String[] { "http://ontology.aal-persona.org/Tests.owl#input3" }),
                null,
                new String("give me (earned) Value"));

        new InputField(g1,
                new Label("Input 4",""),
                new PropertyPath(
                        null,
                        false,
                        new String[] { "http://ontology.aal-persona.org/Tests.owl#input4" }),
                null,
                "").setSecret();

        new Range(
                g2,
                new Label("Range 1", (String) null),
                new PropertyPath(
                        null,
                        false,
                        new String[] { "http://ontology.aal-persona.org/Tests.owl#input5" }),
                        MergedRestriction
                        .getAllValuesRestrictionWithCardinality("http://ontology.aal-persona.org/Tests.owl#input6", 
                        		new IntRestriction(3, true, 12, true), 1, 1),
                new Integer(5));
        new Range(
                g2,
                new Label("Range 2", (String) null),
                new PropertyPath(
                        null,
                        false,
                        new String[] { "http://ontology.aal-persona.org/Tests.owl#input6" }),
                        MergedRestriction
                        .getAllValuesRestrictionWithCardinality("http://ontology.aal-persona.org/Tests.owl#input6", 
                        		new IntRestriction(1, true, 100, true), 1, 1),
                new Integer(50));


         String [] selection1 = {"1","2","3"};
        Select s1 =new Select(g3,
                new Label("select strings", ""),
                new PropertyPath(
                        null,
                        false,
                        new String[] { "http://ontology.aal-persona.org/Tests.owl#input7" }),
                null, null);
        s1.generateChoices(selection1);

        Integer[] ss2 = {new Integer(10), new Integer(20), new Integer(30)};
        Select s2 =new Select(g3,
                new Label("select Integer", ""),
                new PropertyPath(
                        null,
                        false,
                        new String[] { "http://ontology.aal-persona.org/Tests.owl#input8" }),
                null, new Integer(20));
        s2.generateChoices(ss2);

        Select1 s11 = new Select1(
                g3,
                new Label("Select only 1", (String) null),
                new PropertyPath(
                        null,
                        false,
                        new String[] { "http://ontology.aal-persona.org/Tests.owl#input9" }),
                null, "Opt2");
        s11.generateChoices(new String[] { "Opt1", "Opt2", "Opt3" });

        new TextArea(
                g2,
                new Label("Text Area", (String) null),
                new PropertyPath(
                        null,
                        false,
                        new String[] { "http://ontology.aal-persona.org/Tests.owl#input10" }),
                null, "nyan");

        new SimpleOutput(dlg.getIOControls(),
                new Label("this is what ive got to say: ",null),
                null,
                (Object) "Hello World!");

        new SimpleOutput(dlg.getIOControls(),
                new Label("",null),
                null,
                (Object) "In some village in La Mancha, whose name I do not care to recall, there dwelt not so long ago a gentleman of the type wont to keep an unused lance, an old shield, a skinny old horse, and a greyhound for racing.");

        new SimpleOutput(dlg.getIOControls(),
                new Label("like it",null),
                null,
                Boolean.TRUE);
        /*
         * using GUI.Model to render
         */
        new Init().install();
        Renderer.getInstance();
        JFrame jfm = ModelMapper.getModelFor(dlg).getFrame();
        jfm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jfm.setVisible(true);
    }

}
