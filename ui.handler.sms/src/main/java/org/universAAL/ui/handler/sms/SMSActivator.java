/*******************************************************************************
 * Copyright 2013 Universidad Politécnica de Madrid
 * Copyright 2013 Fraunhofer-Gesellschaft - Institute for Computer Graphics Research
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

package org.universAAL.ui.handler.sms;

import net.vsms.bulksms.SmsSender;

import org.universAAL.middleware.container.ModuleActivator;
import org.universAAL.middleware.container.ModuleContext;

/**
 * @author amedrano
 *
 */
public class SMSActivator implements ModuleActivator {

    /** The mcontext. {@link ModuleContext} */
    private static ModuleContext mcontext;
    private SmsUIHandler handler;

    
    /** {@ inheritDoc}	 */
    public void start(ModuleContext mc) throws Exception {
	mcontext = mc;
	handler = new SmsUIHandler(mcontext, new SmsSender());
	//TODO update this so backend sms sender can be configured.
    }

    /** {@ inheritDoc}	 */
    public void stop(ModuleContext mc) throws Exception {
	handler.close();
    }

    /**
     * @return
     */
    public static ModuleContext getModuleContext() {
	return mcontext;
    }

}
