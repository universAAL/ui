/**
 * 
 */
package org.universAAL.ui.dm.mobile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import org.universAAL.middleware.container.osgi.util.BundleConfigHome;
import org.universAAL.middleware.io.rdf.Group;
import org.universAAL.middleware.io.rdf.Label;
import org.universAAL.middleware.io.rdf.SimpleOutput;
import org.universAAL.middleware.io.rdf.Submit;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.ServiceRequest;

/**
 * @author mtazari
 * 
 */
public class MainMenu {
	private static final Hashtable<String, MainMenu> userMenus = new Hashtable<String, MainMenu>();

	private MenuNode root, selection;
	private String lastLanguage = null, thisUser = null;
	private Resource currentUser = null;

	static ServiceRequest updateMenu = new ServiceRequest();
	private static File menuHome = new File(new File(BundleConfigHome.uAAL_CONF_ROOT_DIR), "ui.dm.mobile");

	private MainMenu(String user, Resource currentUser) {
		thisUser = user;
		this.currentUser = currentUser;
		constructMenu(true);
	}

	private void constructMenu(boolean refreshMenu) {
		String lang = Locale.getDefault().getLanguage();

		if (lang.equals(lastLanguage) && !refreshMenu)
			return;

		System.out.println("Build menu...");
		lastLanguage = lang;
		Vector<String[]> entries = new Vector<String[]>();
		try {
			System.out.println("Refresh Menu from file \"" + "main_menu_"
					+ lang + ".txt\"");
			InputStream in = Activator.getConfFileReader().getConfFileAsStream(
					"main_menu_" + thisUser + lang + ".txt");
			// if (in == null)
			// in = MainMenu.class.getResourceAsStream("main_menu.txt");

			byte[] buf = new byte[2048];
			int n = 0, s = 0;
			String[] line = new String[3];
			String rest = "";
			int col = 0;
			while ((n = in.read(buf)) > -1) {
				for (int i = 0;; i++)
					if (i == n) {
						rest = rest + new String(buf, s, i - s);
						break;
					} else if (buf[i] == '|') {
						line[col++] = (rest + new String(buf, s, i - s)).trim();
						s = i + 1;
						rest = "";
					} else if (buf[i] == '\r' || buf[i] == '\n') {
						if (col > 0) {
							line[col] = rest + new String(buf, s, i - s);
							col = 0;
							entries.add(line);
							line = new String[3];
							rest = "";
							s = i + 1;
						} else {
							rest = rest + new String(buf, s, i - s);
							s = i + 1;
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
			if (oldSelection != null && oldSelection.hasService()
					&& oldSelection.getVendor().equals(entry[1])
					&& oldSelection.getServiceClass().equals(entry[2]))
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
				new Submit(rg, new Label(child.getLabel(), null), child
						.getPath());
		else {
			MenuNode pathEnd = selection.hasChild() ? selection : selection
					.getParent();
			if (pathEnd == null)
				for (MenuNode child : root.children())
					new Submit(rg, new Label(child.getLabel(), null), child
							.getPath());
			else {
				String path = pathEnd.getPath();
				Group g = new Group(rg, new Label("Selection", null), null,
						null, null);
				if (path != null && path.startsWith("/")) {
					int i = 1;
					for (int j = path.indexOf('/', i); j > 0; j = path.indexOf(
							'/', i)) {
						new Submit(g, new Label(path.substring(i, j), null),
								path.substring(0, j));
						new SimpleOutput(g, null, null, "->");
						i = j + 1;
					}
					new Submit(g, new Label(path.substring(i), null), path);
					g = new Group(rg, new Label("Options", null), null, null,
							null);
				}
				for (MenuNode child : pathEnd.children())
					new Submit(g, new Label(child.getLabel(), null), child
							.getPath());
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
		return (oldSelection == selection) ? null : updateMenu;
	}

	public static MainMenu getMenuInstance(Resource user) {
		return MainMenu.getMenuInstance(user, false);
	}

	public static MainMenu getMenuInstance(Resource user, boolean refreshMenu) {
		String ln = getUserLocalName(user);
		MainMenu mm = userMenus.get(ln);
		if (mm == null) {
			mm = new MainMenu(ln, user);
			userMenus.put(ln, mm);
		}
		mm.constructMenu(refreshMenu);
		return mm;
	}

	public void addEntryToMenue(String caption, String provider, String call) {
		String lang = Locale.getDefault().getLanguage();
		appendEntryToMenue(lang, caption, provider, call);
	}

	public void appendEntryToMenue(String language, String caption,
			String provider, String call) {
		List<String> lines = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(
					menuHome, "main_menu_" + thisUser
							+ Locale.getDefault().getLanguage() + ".txt")));
			String line;
			while ((line = in.readLine()) != null)
				lines.add(line);
			in.close();

			String newLine = "/" + caption + "|" + provider + "|" + call;
			if (lines.contains(newLine))
				return;

			BufferedWriter out = new BufferedWriter(new FileWriter(new File(
					menuHome, "main_menu_" + thisUser
							+ Locale.getDefault().getLanguage() + ".txt")));
			for (int index = 0; index < lines.size(); index++) {
				if (index == lines.size() - 1)
					out.write(newLine + System.getProperty("line.separator"));
				out.write(lines.get(index)
						+ System.getProperty("line.separator"));
			}
			out.close();

			System.out.println("Main-menu file \"" + "main_menu_" + thisUser
					+ Locale.getDefault().getLanguage() + ".txt"
					+ "\" has been appended!");
		} catch (IOException e) {
			System.out.println("File not found: " + "main_menu_" + thisUser
					+ Locale.getDefault().getLanguage() + ".txt");
			e.printStackTrace();
		}
		Activator.getOutputPublisher().showMenu(currentUser, true);
	}

	private MenuNode getNode(String nodePath) {
		if (nodePath != null && nodePath.startsWith("/")) {
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

	MenuNode getSelectedNode() {
		return selection;
	}

	private static String getUserLocalName(Resource u) {
		if (u == null)
			return "";
		String ln = u.getLocalName();
		return (ln == null) ? "" : ln + "_";
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
