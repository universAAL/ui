package org.universAAL.ui.dm.userInteraction.mainMenu.profilable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.sodapop.msg.MessageContentSerializer;
import org.universAAL.ontology.profile.ui.mainmenu.MenuEntry;
import org.universAAL.ontology.profile.ui.mainmenu.MenuProfile;
import org.universAAL.ui.dm.userInteraction.mainMenu.MainMenu;

public class RDFMainMenu extends MainMenu {

    private static final String UTF_8 = "utf-8";
    private ModuleContext context;

    public RDFMainMenu(ModuleContext ctxt, InputStream in) {
	this(ctxt);
	constructMenu(in);
    }
    
    public RDFMainMenu(ModuleContext ctxt) {
    	context=ctxt;
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
	BufferedReader br = new BufferedReader(new InputStreamReader(in,
		Charset.forName(UTF_8)));
	StringBuilder sb = new StringBuilder();
	try {
	    String line = br.readLine();
	    while (line != null) {
		sb.append(line);
		sb.append('\n');
		line = br.readLine();
	    }
	} catch (IOException e) {
	    LogUtils.logError(context, RDFMainMenu.class, "readMenu",
		    new Object[] { "cannot read the file" }, e);
	    return null;
	}
	String serialized = sb.toString();
	if (serialized.length() < 5) {
	    // just a small value to indicate that the file is empty
	    LogUtils.logError(context, RDFMainMenu.class, "readMenu",
			    new Object[] { "file is considered empty." }, null);
	    return null;
	}

	// deserialize
	MessageContentSerializer contentSerializer = (MessageContentSerializer) context
		.getContainer()
		.fetchSharedObject(
			context,
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
	    Resource r = readMenu(in);
	    in.close();
	    return r;
	} catch (Exception e) {
	     LogUtils
	     .logError(
	     context,
	     RDFMainMenu.class,
	     "readMenu",
	     new Object[] { "file: " + file.getName()
	    	 + "does not exist."
	     },
	     e);
	     throw new RuntimeException(e.getMessage());
	}
//	return null;
    }

    public void saveMenu(File file, MenuProfile mp) {
	try {
	    OutputStream out = new FileOutputStream(file);
	    saveMenu(out, mp);
	    out.close();
	} catch (Exception e) {
	    LogUtils.logError(context, RDFMainMenu.class, "saveMenu",
		    new Object[] { "" }, e);
	    throw new RuntimeException(e.getMessage());
	}
    }

    public void saveMenu(OutputStream out, MenuProfile mp) {
	// serialize
	MessageContentSerializer contentSerializer = (MessageContentSerializer) context
		.getContainer()
		.fetchSharedObject(
			context,
			new Object[] { MessageContentSerializer.class.getName() });
	if (contentSerializer == null) {
	    LogUtils.logError(context, RDFMainMenu.class, "saveMenu",
		    new Object[] { "no serializer found" }, null);
	    throw new RuntimeException("no serializer found");
	}
	String serialized = contentSerializer.serialize(mp);
	// write
	try {
	    out.write(serialized.getBytes(Charset.forName(UTF_8)));
	} catch (IOException e) {
	    LogUtils.logError(context, RDFMainMenu.class, "saveMenu",
		    new Object[] { "" }, e);
	    throw new RuntimeException("io exception");
	}
    }
}
