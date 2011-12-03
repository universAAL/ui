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
package org.universAAL.ui.handler.newGui.osgi;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
import org.universAAL.middleware.container.osgi.util.BundleConfigHome;
import org.universAAL.ui.handler.newGui.Renderer;

/**
 * OSGi Activator Class for SWING GUI HANDLER
 * @author <a href="mailto:amedrano@lst.tfo.upm.es>amedrano</a>
 *
 */
public class Activator implements BundleActivator {

    public static BundleContext context=null;

    public BundleConfigHome home = null;

    private Renderer render;


    public void start(BundleContext context) throws Exception {
        Activator.context=context;
        BundleContext[] bc = { context };
        Renderer.setModuleContext(uAALBundleContainer.THE_CONTAINER
                .registerModule(bc));
        render = Renderer.getInstance();
    }

    public void stop(BundleContext arg0) throws Exception {
        render.finish();
    }

}
