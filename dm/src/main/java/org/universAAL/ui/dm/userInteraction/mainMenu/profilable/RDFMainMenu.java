package org.universAAL.ui.dm.userInteraction.mainMenu.profilable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.sodapop.msg.MessageContentSerializer;
import org.universAAL.ontology.profile.ui.mainmenu.MenuEntry;
import org.universAAL.ontology.profile.ui.mainmenu.MenuProfile;
import org.universAAL.ui.dm.userInteraction.mainMenu.MainMenu;

public class RDFMainMenu extends MainMenu {

    public RDFMainMenu(ModuleContext ctxt, InputStream in) {
	super(ctxt, in);
    }

    protected void constructMenu(InputStream in) {

	RDFMenuNode rdfroot = new RDFMenuNode(-1);
	root = rdfroot;
	selection = root;

	// Read the configuration file of the menu for this user which
	// contains a list of menu entries.
	Resource r = null;
	 try {
		r = readMenu(in);
	} catch (Exception e) {
		r = null;
	}
	if (r == null) {
	    LogUtils.logError(context, RDFMainMenu.class, "constructMenu",
		    new Object[] { "resource is null" }, null);
	    return;
	}
	if (!(r instanceof MenuProfile)) {
	    LogUtils.logError(context, RDFMainMenu.class, "constructMenu",
		    new Object[] { "resource is not a UIMainMenuProfile" },
		    null);
	    return;
	}

	MenuProfile mm = (MenuProfile) r;
	MenuEntry[] entries = mm.getMenuEntries();

	for (int i = 0; i < entries.length; i++)
	    rdfroot.add(entries[i]);
    }

    protected Resource readMenu(InputStream in) {
	// read the file
	BufferedReader br = new BufferedReader(new InputStreamReader(in));
	StringBuilder sb = new StringBuilder();
	try {
	    String line = br.readLine();
	    while (line != null) {
		sb.append(line);
		line = br.readLine();
	    }
	} catch (IOException e) {
	    LogUtils.logError(context, RDFMainMenu.class, "readMenu",
		    new Object[] { "cannot read the file" }, e);
	    return null;
	}
	String serialized = sb.toString();
	if (serialized.length() < 5)
	    // just a small value to indicate that the file is empty
	    return null;

	// deserialize
	MessageContentSerializer contentSerializer = (MessageContentSerializer) context
		.getContainer().fetchSharedObject(context,
			new Object[] { MessageContentSerializer.class.getName() });
	if (contentSerializer == null) {
	    LogUtils.logError(context, RDFMainMenu.class, "readMenu",
		    new Object[] { "no serializer found" }, null);
	    return null;
	}

	return (Resource) contentSerializer.deserialize(serialized);
    }

    public Resource readMenu(File file) {
	try {
		InputStream in = new FileInputStream(file);
	    return readMenu(in);
	} catch (Exception e) {
	    // LogUtils
	    // .logError(
	    // context,
	    // RDFMainMenu.class,
	    // "readMenu",
	    // new Object[] { FileMainMenuProvider.filePrefix
	    // + userID
	    // +
	    // ".txt does not exist so Main menu for logged user has to be made before running again!"
	    // },
	    // e);
	    // throw new RuntimeException(e.getMessage());
	}
	return null;
    }

    public void saveMenu(File file, MenuProfile mp) {
	try {
		OutputStream out = new FileOutputStream(file);
	    saveMenu(out, mp);
	} catch (Exception e) {
	    LogUtils.logError(context, RDFMainMenu.class, "saveMenu",
		    new Object[] { "" }, e);
	    throw new RuntimeException(e.getMessage());
	}
    }

    public void saveMenu(OutputStream out, MenuProfile mp) {
	// serialize
	MessageContentSerializer contentSerializer = (MessageContentSerializer) context
		.getContainer().fetchSharedObject(context,
			new Object[] { MessageContentSerializer.class.getName() });
	if (contentSerializer == null) {
	    LogUtils.logError(context, RDFMainMenu.class, "saveMenu",
		    new Object[] { "no serializer found" }, null);
	    throw new RuntimeException("no serializer found");
	}
	String serialized = contentSerializer.serialize(mp);
	// write
	try {
	    out.write(serialized.getBytes());
	} catch (IOException e) {
	    LogUtils.logError(context, RDFMainMenu.class, "saveMenu",
		    new Object[] { "" }, e);
	    throw new RuntimeException("io exception");
	}
    }
}
