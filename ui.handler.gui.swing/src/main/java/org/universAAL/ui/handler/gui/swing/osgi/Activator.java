
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
package org.universAAL.ui.handler.gui.swing.osgi;

import java.io.File;
import java.io.FileFilter;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.ui.handler.gui.swing.IContainerManager;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.Renderer.RenderStarter;

/**
 * OSGi Activator Class for SWING GUI HANDLER
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 *
 */
public final class Activator implements BundleActivator, IContainerManager {

	/**
	 * the {@link BundleContext} for the handler.newGui bundle
	 */
	public static ModuleContext context = null;

	private Renderer.RenderStarter[] renderers;

	private ThreadGroup tGroup;

	static private BundleContext bundleContext;

	/** {@inheritDoc} */
	public void start(BundleContext context) throws Exception {
		tGroup = new ThreadGroup("Swing Handler Threads");
		bundleContext =  context;
		BundleContext[] bc = { bundleContext };
		Activator.context = uAALBundleContainer.THE_CONTAINER
				.registerModule(bc);
		File dir = Activator.context.getConfigHome();
		File[] props = dir.listFiles(new FileFilter() {

			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".properties");
			}
		});

		if (props != null && props.length > 0) {
			renderers = new Renderer.RenderStarter[props.length];
			for (int i = 0; i < props.length; i++) {
				renderers[i] = new RenderStarter(Activator.context,props[i],this);
//				Activator.context.registerConfigFile(
//						new Object[] {
//								getPropName(props[i]),
//								"Configure and instance in the UI.Handler.gui.swing component",
//								Activator.getDescription()});
			}
		}
		else {
			renderers = new Renderer.RenderStarter[1];
			renderers[0] = new Renderer.RenderStarter(Activator.context,null,this);
//			Activator.context.registerConfigFile(
//						new Object[] {
//								"renderer",
//								"Configure the UI.Handler.gui.swing component",
//								Activator.getDescription()});
		}
		/*
		 *  Vadim - put renderer creation in a separate thread,
		 *  see a discussion here - 
		 *  http://forge.universaal.org/gf/project/uaal_ui/forum/?_forum_action=ForumMessageBrowse&thread_id=91&action=ForumBrowse&forum_id=89
		 */
		for (int i = 0; i < renderers.length; i++) {
			new Thread(tGroup, renderers[i]).start();
		}
	}
	
//	private static Hashtable getDescription(){
//		Hashtable d = new Hashtable();
//		d.put("gui.location", "the location of the handler itself. it is a URI");
//		d.put("queued.forms",
//				"The Class which handles incoming dialogs. It can be either of these three options:\n"
//						+ "org.universAAL.ui.handler.gui.swing.formManagement.SimpleFormManager"+
//						"(default) first come, first serve, only one dialog at a time.\n"
//						+ "org.universAAL.ui.handler.gui.swing.formManagement.QueuedFormManager " + 
//						"the next dialog is the one with the most priority (this is handled by DM "+
//						"so this might cause problems).\n"
//						+ "org.universAAL.ui.handler.gui.swing.formManagement.HierarchicalFormManager" +
//						" dialogs are processed one by one, but kept when subdialogs come in so the full hierarchy of dialogs is available.");
//		d.put("LookandFeel.package", "The look and feel package to be used (if not found, defaultLAF package will be used)");
//		d.put("demo.mode", "Configures the Handler in demo Mode, disables Impairment register restrictions (default=true).");
//		d.put("default.user", " if set, the handler automatically logs in for this user, if not (default) user will be asked to provide credentials.");
//		return d;
//	}
//	
//	private String getPropName(File file){
//		int start = 0;
//		if (file.getName().contains(File.pathSeparator)){
//			start = file.getName().indexOf(File.pathSeparator) +1;
//		}
//		return file.getName().substring(start, file.getName().indexOf('.')-1);
//	}

	/** {@inheritDoc} */
	public void stop(BundleContext arg0) throws Exception {
		for (int i = 0; i < renderers.length; i++) {
			renderers[i].stop();
		}
	}

	public void shutdownContainer(){
		try {
			bundleContext.getBundle(0).stop();
		} catch (BundleException e) {
			LogUtils.logWarn(context, getClass(),
					"shutdownContainer", new String[]{"Unable to shutdown container."},e);
		}
	}
}
