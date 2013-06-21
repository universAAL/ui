/*******************************************************************************
 * Copyright 2013 Universidad Politécnica de Madrid
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

package org.universAAL.ui.handler.gui.swing.model.FormControl.support;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.Repeat;
import org.universAAL.middleware.ui.rdf.SubdialogTrigger;

/**
 * This support class is used to generate a list of virtual forms (one per row) so each element of 
 * the repeat ({@link FormControl}) can be Modeled as the rest of {@link FormControl}.
 * @author amedrano
 * 
 */
public class RepeatSubdivider {

	/**
	 * {@link Repeat} object to be used.
	 */
	private Repeat repeat;

	/**
	 * container for children of {@link #repeat}
	 */
	private FormControl[] elems;

	/**
	 * Constructor
	 */
	public RepeatSubdivider(Repeat repeat) {
		this.repeat = repeat;
		elems = repeat.getChildren();
		if (elems == null || elems.length != 1) {
			throw new IllegalArgumentException("Malformed argument!");
		}
		if (elems[0] instanceof Group) {
			elems = ((Group) elems[0]).getChildren();
			if (elems == null || elems.length == 0)
				throw new IllegalArgumentException("Malformed argument!");
		} else if (elems[0] == null)
			throw new IllegalArgumentException("Malformed argument!");

	}

	public FormControl[] getElems(){
		return elems;
	}
	
	/**
	 * Generates a {@link List} of (virtual) {@link Form}s which each contains in its IOControls group
	 * the corresponding row of {@link FormControl}.
	 * This works because the dataRoot of each form is the corresponding for the row, so each {@link FormControl}
	 * can be modeled as usual.
	 * @return
	 */
	public List generateSubForms() {
		ArrayList formList = new ArrayList();
		Object repeatData = getValue(repeat.getReferencedPPath().getThePath(),
				repeat.getFormObject().getData());
		List repeatList = null;
		if (repeatData instanceof Resource) {
			repeatList = ((Resource) repeatData).asList();
		}
		if (repeatData instanceof List) {
			repeatList = (List) repeatData;
		}
		if (repeatList.isEmpty())
			throw new IllegalArgumentException(
					"Referenced Path for Repeat is not a list");
		int index = 0;
		for (Iterator i = repeatList.iterator(); i.hasNext();) {
			Resource res = (Resource) i.next();
			Form subForm = Form.newDialog("", res);
			for (int j = 0; j < elems.length; j++) {
				if (elems[j] != null) {
					FormControl nFC = (FormControl) softCopy(elems[j]);
					nFC.changeProperty(FormControl.PROP_PARENT_CONTROL,
							subForm.getIOControls());
					addChild((Group) subForm.getIOControls(), nFC);
					if (elems[j] instanceof SubdialogTrigger) {
						nFC.changeProperty(
								SubdialogTrigger.PROP_SUBMISSION_ID,
								nFC.getProperty(SubdialogTrigger.PROP_REPEATABLE_ID_PREFIX)
										+ Integer.toString(index));
					}
				}
			}
			formList.add(subForm);
			index++;
		}

		return formList;
	}

	private static Object getValue(String[] pp, Resource pr) {
		if (pp == null || pp.length == 0 || pr == null)
			return null;

		Object o = pr.getProperty(pp[0]);
		for (int i = 1; o != null && i < pp.length; i++) {
			if (!(o instanceof Resource))
				return null;
			pr = (Resource) o;
			o = pr.getProperty(pp[i]);
		}
		return o;
	}

	private Object softCopy(Resource res) {
		Resource newRes = Resource.getResource(res.getType(),
				Resource.generateAnonURI());
		Enumeration props = res.getPropertyURIs();
		// String[] types = res.getTypes();
		// for (int i = 0; i < types.length; i++) {
		// newRes.addType(types[i], false);
		// }
		while (props.hasMoreElements()) {
			String prop = (String) props.nextElement();
			newRes.setProperty(prop, res.getProperty(prop));
		}
		return newRes;
	}

	private static void addChild(Group parent, FormControl child) {
		List children = (List) parent.getProperty(Group.PROP_CHILDREN);
		if (children == null) {
			children = new ArrayList();
			parent.setProperty(Group.PROP_CHILDREN, children);
		}
		children.add(child);
	}
}
