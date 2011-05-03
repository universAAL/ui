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

import java.util.Iterator;

import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.owl.InitialServiceDialog;

/**
 * A node of the main menu. The main menu can be hierarchically. Thus, a node
 * can have a parent and multiple children (currently, a maximum of 15
 * children is allowed). The leaf node of this tree should have a service
 * class and vendor associated.
 * 
 * @author mtazari
 * @author cstockloew
 */
public class MenuNode {

	/**
	 * An iterator for children.
	 */
	private class NodeChildren implements Iterable<MenuNode>,
			Iterator<MenuNode> {
		int i = 0;

		public boolean hasNext() {
			return i < MenuNode.this.child.length
					&& MenuNode.this.child[i] != null;
		}

		public Iterator<MenuNode> iterator() {
			return this;
		}

		public MenuNode next() {
			return MenuNode.this.child[i++];
		}

		public void remove() {
			throw new RuntimeException("Operation not supported!");
		}
	}

	/**
	 * The parent node, or null if root node.
	 */
	private MenuNode parent;

	/**
	 * The set of children.
	 */
	private MenuNode[] child;


	/**
	 * The label of the node. This must be a simple human-readable text
	 * because it is directly shown in the user interface.
	 */
	private String label;
	
	/**
	 * The vendor. Multiple implementations of the same service class can
	 * be distinguished by the vendor name.
	 */
	private String vendor;
	
	/**
	 * The service class for this service.
	 */
	private String serviceClass;

	/**
	 * The level for hierarchical menus. The root node is at level -1, so that
	 * the main list of menu entries is at level 0.
	 */
	private int level;

//	private boolean visible = false;
//	private boolean bActive = false;


	MenuNode(int level) {
		child = new MenuNode[] { null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null };
		label = "";
		this.level = level;
	}

	/**
	 * Add a new node to the menu. The argument 'path' can consist of multiple
	 * labels (separated by the symbol '/') that will result in multiple
	 * levels of the menu. Nodes are automatically created if they do not
	 * exist. The leaf node is associated with the given parameters for
	 * vendor and service class.
	 * @param path The path of possibly multiple labels according to the
	 * 		different levels in the menu.
	 * @param vendor The vendor of the service.
	 * @param serviceClass The class the service implements.
	 * @return The number of newly created nodes.
	 */
	int add(String path, String vendor, String serviceClass) {
		MenuNode aux, cur = this;
		String[] pathArr = path.split("/");
		// the first path elem is normally ""
		int offset = ("".equals(pathArr[0]) ? 1 : 0);
		// skip existing nodes
		int i = offset;
		while (i < pathArr.length) {
			aux = cur.getChild(pathArr[i]);
			if (aux == null)
				break;
			i++;
			cur = aux;
		}
		// create missing nodes
		int createdNodes = 0;
		while (i < pathArr.length) {
			aux = new MenuNode(i - offset);
			aux.label = pathArr[i++];
			cur.addChild(aux);
			cur = aux;
			createdNodes++;
		}
		// cur is now a leaf of the tree that must be associated with a service
		cur.vendor = vendor;
		cur.serviceClass = serviceClass;
		// return the number of newly created nodes
		return createdNodes;
	}

	/**
	 * Add a single child to this node and set correctly the parent of
	 * the newly created child.
	 * @param c The child.
	 */
	private void addChild(MenuNode c) {
		for (int i = 0; i < child.length; i++)
			if (child[i] == null) {
				child[i] = c;
				c.parent = this;
				break;
			}
	}

	/**
	 * Get an iterator for all children of this node.
	 * @return The iterator.
	 */
	public Iterable<MenuNode> children() {
		return new NodeChildren();
	}

//	void closeSiblings() {
//		for (MenuNode sibling : parent.children()) {
//			if (sibling == this)
//				continue;
////			sibling.setVisibility(true);
////			for (MenuNode nephew : sibling.children())
////				nephew.setVisibility(false);
//		}
//	}

//	public String getAbbreviatedPath(String lang) {
//		if (level < 1)
//			return "";
//		String res = label;
//		MenuNode p = parent;
//		while (p.level > 0) {
//			res = p.label + "  >>  " + res;
//			p = p.parent;
//		}
//		return res;
//	}

	/**
	 * Get a child with a given label. Only considers direct children.
	 * @param label The label.
	 * @return The child, or null if no child with this label is available.
	 */
	MenuNode getChild(String label) {
		for (MenuNode n : child)
			if (n == null)
				return null;
			else if (label.equals(n.label))
				return n;
		return null;
	}

	/**
	 * Get the label of this node.
	 * @return The label.
	 */
	public String getLabel() {
		return label;
	}

//	public int getLevel() {
//		return level;
//	}

	/**
	 * Get the parent of this node.
	 * @return The parent.
	 */
	public MenuNode getParent() {
		if (level > 0)
			return parent;
		return null;
	}

	/**
	 * Get the path of this node. The path is a string containing the labels
	 * of all nodes from this node up to the root node. It starts with '/'
	 * and the labels are separated by '/'.
	 * @return The path.
	 */
	public String getPath() {
		if (parent == null)
			return "/";		// root node
		
		String res = label;
		if (res == null)
			res = "";
		if (parent.parent == null)
			return "/" + res;	// parent is root node
		return parent.getPath() + "/" + res;
	}

	/**
	 * Create a service request with the user as well as service class and
	 * vendor of this node (if this node a leaf node and has an associated
	 * service).
	 * @param user The user.
	 * @return The service request.
	 */
	public ServiceRequest getService(Resource user) {
		return hasService() ? InitialServiceDialog.getInitialDialogRequest(
				serviceClass, vendor, user) : null;
	}

	/**
	 * Get the service class associated with this node.
	 * @return The service class, or null if no service class available.
	 */
	String getServiceClass() {
		return serviceClass;
	}

	/**
	 * Get the vendor associated with this node.
	 * @return The vendor, or null if no vendor available.
	 */
	String getVendor() {
		return vendor;
	}

	/**
	 * Determines whether this node has children.
	 * @return true, if this node has children.
	 */
	public boolean hasChild() {
		return child[0] != null;
	}

	/**
	 * Determines whether a service (and a vendor) is bound to this node.
	 * @return true, if this node has a service.
	 */
	public boolean hasService() {
		return !isEmptyString(serviceClass) && !isEmptyString(vendor);
	}

	private boolean isEmptyString(String str) {
		return str == null || "".equals(str);
	}

//	/**
//	 * determines if a node has at least one visible child.
//	 * 
//	 * @return
//	 */
//	public boolean hasVisibleChild() {
//		return child[0] != null && child[0].isVisible();
//	}

//	/**
//	 * Convenience method to avoid rightmanager handling in JSPs Is this Node
//	 * active, has the user sufficient rights to use the Service represented by
//	 * this node
//	 * 
//	 * Warning! only valid if set in advance. it can return null if not set.
//	 * 
//	 * @return
//	 */
//	public boolean isActive() {
//		return bActive;
//	}

	// public boolean isActive(PortletRequest request) {
	// return !hasService()
	// ||
	// Portal.getRightManager(request).hasRight(service.getRightCode().getPortalGroup(),
	// true);
	// }

	///**
	// * Determines if this node is Active
	// * 
	// * @param rightManager
	// * @return
	// */
	// public boolean isActive(RightManager rightManager) {
	// return !hasService()
	// || rightManager.hasRight(service.getRightCode().getPortalGroup(), true);
	// }

//	public boolean isSelected(MainMenu navList) {
//		return this == navList.getSelectedNode();
//	}

//	public boolean isVisible() {
//		return visible;
//	}

//	public boolean pathMatches(String[] keywords) {
//		String path = getPath();
//		for (String keyword : keywords)
//			if (path.contains(keyword))
//				return true;
//		return false;
//	}

//	/**
//	 * Sets the active flag
//	 * 
//	 * @param isActive
//	 */
//	public void setActive(boolean isActive) {
//		bActive = isActive;
//	}

//	void setVisibility(boolean value) {
//		if (visible != value) {
//			// first check the old value
//			if (visible)
//				// if this node shouldn't be visible any more, then no child of
//				// it can be visible --> this is done recursively
//				for (MenuNode chld : children())
//					chld.setVisibility(false);
//			else if (parent != null)
//				// if this node should be visible from now on,
//				// then all nodes on the path from the root to this node must
//				// also be visible --> this is done recursively
//				parent.setVisibility(true);
//			visible = value;
//		} else if (visible && parent != null)
//			// if this node should remain visible,
//			// make sure that all nodes on the path from the root to this node
//			// are also visible --> this is done recursively
//			parent.setVisibility(true);
//	}

//	public void setLabel(String label) {
//		if (label != null)
//			if (this.label == null)
//				this.label = label;
//			else if (label.equals(this.label))
//				throw new RuntimeException("Cannot change the label!");
//	}
}
