/*
	Copyright 2007-2014 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institut für Graphische Datenverarbeitung

	See the NOTICE file distributed with this work for additional
	information regarding copyright ownership

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	  http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */
package org.universAAL.ui.dm.userInteraction.mainMenu.profilable;

import java.util.List;

import org.universAAL.middleware.rdf.Resource;
import org.universAAL.ontology.profile.ui.mainmenu.MenuEntry;
import org.universAAL.ui.dm.userInteraction.mainMenu.file.MenuNode;

public class RDFMenuNode extends MenuNode {

	public RDFMenuNode(int level) {
		super(level);
	}

	public void add(MenuEntry entry) {
		// simple method: transform entry to old style

		StringBuffer path = new StringBuffer();
		List<?> l = entry.getPath();
		Resource lastPathElement = null;
		for (Object o : l) {
			Resource r = (Resource) o;
			path.append("/" + r.getResourceLabel()); // TODO: get label
														// according to
			// language
			lastPathElement = r;
		}
		add(path.toString(), entry.getVendor().getURI(), entry.getServiceClass().getURI(), lastPathElement.getURI());
	}

	protected MenuNode newMenuNode(int level) {
		return new RDFMenuNode(level);
	}
}
