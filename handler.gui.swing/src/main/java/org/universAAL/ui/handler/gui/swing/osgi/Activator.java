
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

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
import org.universAAL.middleware.container.osgi.util.BundleConfigHome;
import org.universAAL.ui.handler.gui.swing.Renderer;

/**
 * OSGi Activator Class for SWING GUI HANDLER
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 *
 */
public class Activator implements BundleActivator {

    /**
     * the {@link BundleContext} for the handler.newGui bundle
     */
    public static ModuleContext context = null;

    /**
     * the home directory where to store the config files.
     */
    public BundleConfigHome home = null;

    public static Renderer render;

    /** {@inheritDoc} */
    public void start(BundleContext context) throws Exception {
        home = new BundleConfigHome(context.getBundle().getSymbolicName());
        BundleContext[] bc = { context };
        Activator.context = uAALBundleContainer.THE_CONTAINER
                .registerModule(bc);
        // Vadim - put renderer creation in a separate thread,
        // see a discussion here - http://forge.universaal.org/gf/project/uaal_ui/forum/?_forum_action=ForumMessageBrowse&thread_id=91&action=ForumBrowse&forum_id=89
    	(new Thread() { 
    		public void run(){
		    Renderer.setHome(home.getAbsolutePath());
		    render = new Renderer(Activator.context);
		    render.start();
		}
	    }).start();
    }

    /** {@inheritDoc} */
    public void stop(BundleContext arg0) throws Exception {
        render.finish();
    }

    public static void logDebug(String text, Throwable e) {
    	if (Activator.context != null) {
    		Activator.context.logDebug("org.universAAL.ui.handler.gui.swing.osgi.Activator",text, e);
    	}
    	else {
    		System.err.println("[Debug]" + text);
    		System.err.print(e);
    	}
    }
    
}
