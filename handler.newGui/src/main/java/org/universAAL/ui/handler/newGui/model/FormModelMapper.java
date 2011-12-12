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
package org.universAAL.ui.handler.newGui.model;

import java.util.TreeMap;

/**
 * a {@link FormModel} reference keeper.
 * Maps Forms' URI to their representative Model
 *
 * @author <a href="mailto:amedrano@lst.tfo.upm.es">amedrano</a>
 */
public class FormModelMapper {

    /**
     * The internal Map used
     */
    static private TreeMap formMap = new TreeMap();

    /**
     * register a {@link FormModel}
     * @param fm 
     * 		{@link FormModel} to register
     */
    public static void register(FormModel fm) {
        formMap.put(fm.getForm().getDialogID(), fm);
    }

    /**
     * get a {@link FormModel} from a given formID.
     * @param formID
     * 		Retrieve {@link FormModel} corresponding to this ID
     * @return
     * 		the {@link FormModel} match
     */
    public static FormModel getFromURI(String formID) {
        return (FormModel) formMap.get(formID);
    }

    /**
     * Test whether the formID's {@link FormModel} is
     * registered.
     * @param formID
     * 			Form Id to be tested
     * @return
     * 			true if is registered
     */
    public static boolean isRegistered(String formID) {
        return formMap.containsKey(formID);
    }

    /**
     * Delete a {@link FormModel} from the map.
     * @param fm
     * 		{@link FormModel} to be unregistered
     */
    public static void unRegister(FormModel fm) {
        unRegister(fm.getForm().getURI());
    }

    /**
     * same as {@link FormModelMapper#unRegister(FormModel)},
     * but using the form's ID.
     * @param formID
     * 		Id of the form to be unregistered
     */
    public static void unRegister(String formID) {
        formMap.remove(formID);
    }

    /**
     * completely clean the map, remove all entries.
     */
    public static void flush() {
        formMap.clear();
    }
}
