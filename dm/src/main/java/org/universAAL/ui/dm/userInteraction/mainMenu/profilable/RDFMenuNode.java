package org.universAAL.ui.dm.userInteraction.mainMenu.profilable;

import java.util.List;

import org.universAAL.middleware.rdf.Resource;
import org.universAAL.ontology.profile.ui.mainmenu.MenuEntry;
import org.universAAL.ui.dm.userInteraction.mainMenu.MenuNode;

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
	    path.append("/" + r.getResourceLabel()); // TODO: get label according to
						// language
	    lastPathElement = r;
	}
	add(path.toString(), entry.getVendor().getURI(), entry.getServiceClass().getURI(),
		lastPathElement.getURI());
    }

    protected MenuNode newMenuNode(int level) {
	return new RDFMenuNode(level);
    }
}
