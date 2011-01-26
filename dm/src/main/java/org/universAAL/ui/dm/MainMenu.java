/*
	Copyright 2008-2010 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institute of Computer Graphics Research 
	
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
package org.universAAL.ui.dm;

import java.io.InputStream;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.io.rdf.Group;
import org.universAAL.middleware.io.rdf.Label;
import org.universAAL.middleware.io.rdf.Submit;
import org.universAAL.middleware.io.rdf.SimpleOutput;
import org.universAAL.middleware.service.ServiceRequest;

/**
 * @author mtazari
 *
 */
public class MainMenu {
	private static final Hashtable<String, MainMenu> userMenus= new Hashtable<String, MainMenu>();
	
	private MenuNode root, selection;
	private String lastLanguage = null, thisUser = null;
	
	static ServiceRequest updateMenu = new ServiceRequest();
	
	private MainMenu(String user) {
		thisUser = user;
	}
	
	private void constructMenu() {
		String lang = Locale.getDefault().getLanguage();
		
		if (lang.equals(lastLanguage))
			return;
		
		lastLanguage = lang;
		Vector<String[]> entries = new Vector<String[]>();
		try {
			InputStream in = Activator.getConfFileReader().getConfFileAsStream("main_menu_"+thisUser+lang+".txt");
//			if (in == null)
//				in = MainMenu.class.getResourceAsStream("main_menu.txt");

			byte[] buf = new byte[2048];
			int n = 0, s = 0;
			String[] line = new String[3];
			String rest = "";
			int col = 0;
			while ((n = in.read(buf)) > -1) {
				for (int i=0; ; i++)
					if (i == n) {
						rest = rest + new String(buf, s, i-s);
						break;
					} else if (buf[i] == '|') {
						line[col++] = (rest + new String(buf, s, i-s)).trim();
						s = i+1;
						rest = "";
					} else if (buf[i] == '\r'  ||  buf[i] == '\n') {
						if (col > 0) {
							line[col] = rest + new String(buf, s, i-s);
							col = 0;
							entries.add(line);
							line = new String[3];
							rest = "";
							s = i+1;
						} else {
							rest = rest + new String(buf, s, i-s);
							s = i+1;
						}
					}
				s = 0;
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		
		MenuNode oldSelection = selection;
		selection = null;
		root = new MenuNode(-1);
		int j = 0;
		for (String[] entry : entries) {
			j += root.add(entry[0], entry[1], entry[2]);
			if (oldSelection != null  &&  oldSelection.hasService()
					&&  oldSelection.getVendor().equals(entry[1])
					&&  oldSelection.getServiceClass().equals(entry[2]))
				selection = getNode(entry[0]);
		}

		// init the navigation list
		j = 0;
		for (MenuNode child : root.children())
			child.setVisibility(true);
	}
	
	void addMenuRepresentation(Group rg) {
		if (selection == null)
			for (MenuNode child : root.children())
				new Submit(rg, new Label(child.getLabel(), null), child.getPath());
		else {
			MenuNode pathEnd = selection.hasChild()? selection : selection.getParent();
			if (pathEnd == null)
				for (MenuNode child : root.children())
					new Submit(rg, new Label(child.getLabel(), null), child.getPath());
			else {
				String path = pathEnd.getPath();
				Group g = new Group(rg, new Label("Selection", null), null, null, null);
				if (path != null  &&  path.startsWith("/")) {
					int i = 1;
					for (int j = path.indexOf('/', i); j>0; j = path.indexOf('/', i)) {
						new Submit(g, new Label(path.substring(i, j), null), path.substring(0, j));
						new SimpleOutput(g, null, null, "->");
						i = j+1;
					}
					new Submit(g, new Label(path.substring(i), null), path);
					g = new Group(rg, new Label("Options", null), null, null, null);
				}
				for (MenuNode child : pathEnd.children())
					new Submit(g, new Label(child.getLabel(), null), child.getPath());
			}
		}
	}
	
	ServiceRequest getAssociatedServiceRequest(String nodePath, Resource user) {
		MenuNode oldSelection = selection, node = getNode(nodePath);
		if (node != null)
			if (node.hasChild())
				selection = node;
			else
				return node.getService(user);
		return (oldSelection == selection)? null : updateMenu;
	}
	
	static MainMenu getMenuInstance(Resource user) {
		String ln = getUserLocalName(user);
		MainMenu mm = userMenus.get(ln);
		if (mm == null) {
			mm = new MainMenu(ln);
			userMenus.put(ln, mm);
		}
		mm.constructMenu();
		return mm;
	}
	
	private MenuNode getNode(String nodePath) {
		if (nodePath != null  &&  nodePath.startsWith("/")) {
			int i = 1;
			MenuNode node = root;
			for (int j = nodePath.indexOf('/', i); j>0; j = nodePath.indexOf('/', i)) {
				node = node.getChild(nodePath.substring(i, j));
				if (node == null)
					return null;
				i = j+1;
			}
			return node.getChild(nodePath.substring(i));
		}
		return null;
	}

	MenuNode getSelectedNode() {
		return selection;
	}
	
	private static String getUserLocalName(Resource u) {
		if (u == null)
			return "";
		String ln = u.getLocalName();
		return (ln == null)? "" : ln+"_";
	}

//	private synchronized void update(MenuNode selection) {
//		if (selection != this.selection) {
//			this.selection = selection;
//			if (selection != null)
//				if (selection.hasChild()) {
//					// this selection is an inner node
//					// inner nodes may only be activated via menu, so they are already visible
//					selection.closeSiblings();
//					for (MenuNode child : selection.children())
//						child.setVisibility(true);
//				} else if (!selection.isVisible()) {
//					// make this and all its siblings visible
//					for (MenuNode child : selection.getParent().children())
//						child.setVisibility(true);
//					// make all the nodes on the path visible
//					while (selection.getLevel() > 0) {
//						selection = selection.getParent();
//						selection.closeSiblings();
//						selection.setVisibility(true);
//					}
//				}
//		}
//
//	}
}
