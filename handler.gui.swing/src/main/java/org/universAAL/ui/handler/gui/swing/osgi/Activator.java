
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

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
import org.universAAL.middleware.container.osgi.util.BundleConfigHome;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.ui.handler.gui.swing.Renderer;
import org.universAAL.ui.handler.gui.swing.Renderer.RenderStarter;

/**
 * OSGi Activator Class for SWING GUI HANDLER
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 *
 */
public final class Activator implements BundleActivator {

    /**
     * the {@link BundleContext} for the handler.newGui bundle
     */
    static ModuleContext context = null;

    /**
     * the home directory where to store the config files.
     */
    public BundleConfigHome home = null;

    private Renderer.RenderStarter[] renderers;

    /** {@inheritDoc} */
    public void start(BundleContext context) throws Exception {
        home = new BundleConfigHome(context.getBundle().getSymbolicName());
        BundleContext[] bc = { context };
        Activator.context = uAALBundleContainer.THE_CONTAINER
                .registerModule(bc);
        Renderer.setHome(home.getAbsolutePath());
        File dir = new File(home.getAbsolutePath());
        File[] props = dir.listFiles(new FileFilter() {
	    
	    public boolean accept(File pathname) {
		return pathname.getName().endsWith(".properties");
	    }
	});
        
        if (props.length > 0) {
            renderers = new Renderer.RenderStarter[props.length];
            for (int i = 0; i < props.length; i++) {
		renderers[i] = new RenderStarter(Activator.context,props[i]);
	    }
        }
        else {
            renderers = new Renderer.RenderStarter[1];
            renderers[0] = new Renderer.RenderStarter(Activator.context);
        }
        /*
         *  Vadim - put renderer creation in a separate thread,
         *  see a discussion here - 
         *  http://forge.universaal.org/gf/project/uaal_ui/forum/?_forum_action=ForumMessageBrowse&thread_id=91&action=ForumBrowse&forum_id=89
         */
        for (int i = 0; i < renderers.length; i++) {
	    new Thread(renderers[i]).start();
	}
    }

    /** {@inheritDoc} */
    public void stop(BundleContext arg0) throws Exception {
	for (int i = 0; i < renderers.length; i++) {
	    renderers[i].stop();
	}
    }

    public static void logDebug(Class claz, String text, Throwable e) {
    	if (Activator.context != null) {
    		LogUtils.logDebug(context, claz, "logDebug", new String[] {text}, e);
    	}
    	else {
    		System.err.println("[Debug]" + text);
    		System.err.print(e);
    	}
    }
    
}
