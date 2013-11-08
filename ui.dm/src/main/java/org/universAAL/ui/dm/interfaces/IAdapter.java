/*******************************************************************************
 * Copyright 2012 Universidad Politécnica de Madrid
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
package org.universAAL.ui.dm.interfaces;

import org.universAAL.middleware.ui.UIRequest;

/**
 * Interface for all Dialog Manager adaptations. Adaptations not only mean
 * adding parameters, but also these adapters can change the content of the
 * request, for example implementing externationalization or change relative
 * URLs to full URLs for seamless integration of handlers with resource server.
 * 
 * @author amedrano
 * 
 *         created: 26-sep-2012 13:03:50
 */
public interface IAdapter {

    /**
     * Perform an UI adaptation over {@link UIRequest}. This method may be
     * recalled several times, therefore it must always check that the
     * adaptation is in fact needed, and not already done.
     * 
     * @param request
     */
    public void adapt(final UIRequest request);

}
