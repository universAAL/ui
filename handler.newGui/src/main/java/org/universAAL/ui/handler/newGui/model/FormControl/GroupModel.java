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
package org.universAAL.ui.handler.newGui.model.FormControl;


import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.owl.supply.LevelRating;
import org.universAAL.ui.handler.newGui.ModelMapper;
import org.universAAL.ui.handler.newGui.model.IconFactory;
import org.universAAL.ui.handler.newGui.model.LabelModel;
import org.universAAL.ui.handler.newGui.model.Model;

/**
 *
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 * @see Group
 */
public class GroupModel extends Model {


    /**
     * Constructor.
     * @param control the {@link Group} which to model.
     */
    public GroupModel(Group control) {
        super(control);
    }

    /** The {@link JComponent} returned may be either
     * <ul>
     * <li> a {@link JPanel}
     * <li> a {@link JTabbedPane}
     * </ul>
     *
     *  depending on the complexity (and other factors) of the group
     *
     * @return {@inheritDoc}
     * */
    public JComponent getComponent() {
        LevelRating complexity = ((Group) fc).getComplexity();
        if (complexity == LevelRating.none
                || ((Group) fc).isRootGroup()) {
            return simplePannel();
        }
        if (complexity == LevelRating.low ) {
            return simplePannel();
        }
        if (complexity == LevelRating.middle ) {
            return tabbedPanel();
        }
        if (complexity == LevelRating.high ) {
            return tabbedPanel();
        }
        if (complexity == LevelRating.full) {
            return tabbedPanel();
        }
        return null;
    }

    /** {@inheritDoc}*/
    public boolean isValid(JComponent component) {
        // Always valid
        return true;
    }

    /**
     * create a simple panel with the children in it
     * @return
     *         a {@link JPanel} with all the group's children.
     */
    protected JPanel simplePannel() {
        /*
         * a Simple Group containing FormControls
         * or one of the main Groups
         * go into simple panes
         */
        JPanel pane = new JPanel();
        pane.setName(fc.getURI());
        FormControl[] children = ((Group) fc).getChildren();
        for (int i = 0; i < children.length; i++) {
            addComponentTo(children[i], pane);
        }
        pane.setName(fc.getURI());
        return pane;
    }

    /**
     * create a tabbed panel with diferent groups
     * in different pannels.
     * @return
     *         a {@link JTabbedPane} with children groups
     * as panels
     */
    protected JTabbedPane tabbedPanel() {
        JTabbedPane tp = new JTabbedPane();
        FormControl[] children = ((Group) fc).getChildren();
        JPanel pane;
        for (int i = 0; i < children.length; i++) {
            if (children[i] instanceof Group) {
                pane = (JPanel) getComponentFrom(children[i]);
            }
            else {
                pane = new JPanel(false);
                addComponentTo(children[i], pane);
            }
            tp.addTab(children[i].getLabel().getText(),
                    IconFactory.getIcon(children[i].getLabel().getIconURL()),
                    pane);
        }
        tp.setName(fc.getURI());
        return tp;
    }

    /**
     * check whether it is the submit root group.
     * @return
     *         true is it is.
     */
    public boolean isTheSubmitGroup() {
        return ((Group) fc).isRootGroup() && isInSubmitGroup();
    }

    /**
     * check whether it is the system root group.
     * @return
     *         true is it is.
     */
    public boolean isTheMainGroup() {
        return ((Group) fc).isRootGroup() && isInStandardGroup();
    }

    /**
     * check whether it is the io root group.
     * @return
     *         true is it is.
     */
    public boolean isTheIOGroup() {
        return ((Group) fc).isRootGroup() && isInIOGroup();
    }

    /**
     * Access to the Model mapper.
     * it will load the {@link JComponent} for a given {@link FormControl}
     * which is a child of the current group.
     * @param fc
     *         the child from which to obtain it's model and {@link JComponent}
     * @return
     *         the {@link JComponent} build by the {@link Model} of the child
     */
    private JComponent getComponentFrom(FormControl fc) {

        return ModelMapper.getModelFor(fc).getComponent();
    }

    /**
     * Access to the Model mapper.
     * it will load the {@link JComponent} for a given {@link FormControl}
     * which is a child of the current group, and add it to a {@link Container}.
     * @param fc
     *         the child from which to obtain it's model and {@link JComponent}
     * @param c
     *         the {@link Container} to which to add the {@link JComponent}
     */
    private void addComponentTo(FormControl fc, Container c) {
        Model m = ModelMapper.getModelFor(fc);
        JComponent jc = m.getComponent();
        if (jc != null  ) {
            if (fc.getLabel() != null) {
                LabelModel label = ModelMapper.getModelFor(fc.getLabel());
                if (label.hasInfo() && m.needsLabel()) {
                    JLabel l = label.getComponent();
                    l.setLabelFor(jc);
                    c.add(l);
                }
            }
            c.add(jc);
        }
    }

}
