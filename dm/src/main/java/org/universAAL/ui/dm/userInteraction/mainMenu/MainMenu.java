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
package org.universAAL.ui.dm.userInteraction.mainMenu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.Submit;

/**
 * A set of main menus. The main menu of the Dialog Manager represents a list of
 * services installed in the system that can be called by the user.
 * Additionally, the Dialog Manager provides methods to search for services and
 * to show pending messages and dialogs.<br>
 * There is one main menu for every user of the system. The content is
 * determined by a configuration file where each line contains 3 values
 * (separated by '|'):
 * <ol>
 * <li>list of labels starting with '/' (multiple labels for hierarchical menus
 * are separated by '/')<br>
 * <li>vendor<br>
 * <li>service class<br>
 * </ol>
 * Examples:<br>
 * <code>
 * /Light Control|myVendor|LightingURI<br>
 * /Home Control/Light Control|myVendor|LightingURI
 * </code>
 * 
 * @author mtazari
 * @author cstockloew
 * @author eandgrg
 * @author amedrano
 */
public class MainMenu {

    /**
     * The root node.
     */
    private MenuNode root;

    /**
     * The node that is currently selected.
     */
    private MenuNode selection;

    private ModuleContext context;
    
    /**
     * constructor. Creates the menu.
     * 
     * @param user
     *            The user.
     */
    public MainMenu(ModuleContext ctxt, InputStream in) {
	context=ctxt;
    	constructMenu(in);
    }

    /**
     * Create the menu. Reads a configuration file with menu entries and creates
     * a tree of menu nodes where leaf nodes have a vendor and service class
     * associated.
     * @param userID 
     */
    private void constructMenu(InputStream in) {
	
	root = new MenuNode(-1);
	selection = root;
	
	// Read the configuration file of the menu for this user which
	// contains a list of menu entries. Entries are stored in 'entries'
	// as a list where each line contains 3 values:
	// 1. list of labels (multiple labels for hierarchical menus)
	// 2. vendor
	// 3. service class


	BufferedReader br = new BufferedReader(new InputStreamReader(in, Charset.forName("cp1252")));
	
	String line;
	try {
		line = br.readLine();
		while (line != null) {
			if (!line.startsWith("#")) {
				String[] cols = line.split("\\|");
				if (cols.length == 3) {
					root.add(cols[0], cols[1], cols[2]);
				}
				else if (cols.length == 4) {
					root.add(cols[0], cols[1], cols[2], cols[3]);
				}
				else {
					StringBuffer s = new StringBuffer();
					for (int i = 0; i < cols.length; i++) {
						s.append(" [ " + cols[i] + " ]");
					}
					context.logError("Main Menu File",
							"unable to parse " + cols.length + "columns : " +s.toString() , null);
				}
			}
			line = br.readLine();
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }

    /**
     * Adds this menu to a dialog presented to the user. The dialog can contain
     * other information like pending messages or a functionality to search for
     * services. The services that are added in this method are added to a
     * special group and then the whole dialog is sent to the output bus.
     * 
     * @param rg
     *            The group to add the menu.
     */
    void addMenuRepresentation(Group rg) {
	if (selection == null || selection == root)
	    // add children of root
	    for (MenuNode child : root.children()) {
		new Submit(rg, new Label(child.getLabel(), child.getIconURL()), child
			.getPath());
		System.out.println("Menu child: " + child.getPath());
	    }
	else {
	    // add children of an inner node
	    Group g;

	    // selection: the currently selected node, acts as possibility to
	    // go to higher level
	    g = new Group(rg, new Label("Selection", null), null, null, null);
	    MenuNode parent = selection.getParent();
	    if (parent == null)
		parent = root;
	    new Submit(g, new Label(selection.getLabel(), selection.getIconURL()), parent
		    .getPath());
	    // System.out.println("Menu Parent: "+parent.getPath());

	    // options: the children of the currently selected node
	    g = new Group(rg, new Label("Options", null), null, null, null);
	    for (MenuNode child : selection.children()) {
		new Submit(g, new Label(child.getLabel(), child.getIconURL()), child
			.getPath());
		// System.out.println("Menu child: "+child.getPath());
	    }
	}
    }

    /**
     * Select a specific node given its node path.
     * 
     * @param nodePath
     *            The path to the node.
     * @return true, iff selection has changed.
     */
    boolean setSelection(String nodePath) {
	MenuNode node = getNode(nodePath);
	if (node == null)
	    return false;

	MenuNode oldSelection = selection;
	selection = node;
	return oldSelection != selection;
    }
    
    /**
     * Reset the selection to be root.
     */
    void resetSelection() {
	selection = root;
    }

    /**
     * Get a service request for a specific menu node. If the node is a leaf
     * node, it creates the service request according to the service class and
     * vendor of this node. If the node is not a leaf node and the selection has
     * changed, the update service is returned.
     * 
     * @param nodePath
     *            Determines the menu node. Can be a set of labels for
     *            hierarchical menus.
     * @param user
     *            Use the main menu from thus user.
     * @return The service for a leaf node, the pre-defined update service for
     *         an inner node.
     */
    ServiceRequest getAssociatedServiceRequest(String nodePath, Resource user) {
	MenuNode node = getNode(nodePath);
	if (node == null)
	    return null;

	if (!node.hasChild())
	    return node.getService(user);

	return null;
    }

    /**
     * Get a specific menu node.
     * 
     * @param nodePath
     *            Determines the menu node. Can be a set of labels for
     *            hierarchical menus.
     * @return The menu node.
     */
    private MenuNode getNode(String nodePath) {
	if (nodePath != null && nodePath.startsWith("/")) {
	    if (nodePath.equals("/"))
		return root;

	    int i = 1;
	    MenuNode node = root;
	    for (int j = nodePath.indexOf('/', i); j > 0; j = nodePath.indexOf(
		    '/', i)) {
		node = node.getChild(nodePath.substring(i, j));
		if (node == null)
		    return null;
		i = j + 1;
	    }
	    return node.getChild(nodePath.substring(i));
	}
	return null;
    }

    
    /**
     * Get all the entries for the (sub)menu.
     * @return
     */
    public Iterable<MenuNode> entries(){
	return selection.children();
    }

    // private synchronized void update(MenuNode selection) {
    // if (selection != this.selection) {
    // this.selection = selection;
    // if (selection != null)
    // if (selection.hasChild()) {
    // // this selection is an inner node
    // // inner nodes may only be activated via menu, so they are already
    // visible
    // selection.closeSiblings();
    // for (MenuNode child : selection.children())
    // child.setVisibility(true);
    // } else if (!selection.isVisible()) {
    // // make this and all its siblings visible
    // for (MenuNode child : selection.getParent().children())
    // child.setVisibility(true);
    // // make all the nodes on the path visible
    // while (selection.getLevel() > 0) {
    // selection = selection.getParent();
    // selection.closeSiblings();
    // selection.setVisibility(true);
    // }
    // }
    // }
    //
    // }
}
