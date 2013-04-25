/*******************************************************************************
 * Copyright 2013 2011 Universidad Polit�cnica de Madrid
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
package org.universAAL.ui.dm.dialogManagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.serialization.MessageContentSerializer;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.ui.dm.interfaces.IUIRequestPool;
import org.universAAL.ui.dm.interfaces.IUIRequestStore;
import org.universAAL.ui.dm.userInteraction.mainMenu.profilable.RDFMainMenu;

/**
 * Serialize/Deserialze a whole {@link IUIRequestPool} into/form a file.
 * @author amedrano
 *
 */
public class DialogPoolFileStorage implements IUIRequestStore {

	private static final String UTF_8 = "utf-8";
	private static final String NAMESPACE = "http://ui.universAAL.org/dmFileStorage.owl#";
	private static final String PROP_ACTIVE = NAMESPACE + "active";
	private static final String PROP_SUSPENDED = NAMESPACE + "suspended";
	
	private MessageContentSerializer contentSerializer;
	private File file;

	public DialogPoolFileStorage(ModuleContext context, File file){
		this.file = file;
		contentSerializer = (MessageContentSerializer) context
				.getContainer()
				.fetchSharedObject(
					context,
					new Object[] { MessageContentSerializer.class.getName() });
			if (contentSerializer == null) {
			    LogUtils.logError(context, getClass(), "Constructor",
				    new Object[] { "no serializer found" }, null);
			    throw new IllegalArgumentException("unable to Initialize ContentSerializer");
			}
			if (!file.exists()){
			    LogUtils.logWarn(context, getClass(), "Constructor",
					    new Object[] { "File doesn't exist, creating it." }, null);
				    try {
						file.createNewFile();
					} catch (IOException e) {
					    LogUtils.logError(context, getClass(), "Constructor",
							    new Object[] { "Could not create file." }, null);
					}
				
			}
	}
	
	/** {@inheritDoc} */
	public void save(IUIRequestPool pool){
		Resource root = new Resource();
		List<UIRequest> active = new ArrayList<UIRequest>(pool.listAllActive());
		List<UIRequest> suspended = new ArrayList<UIRequest>(pool.listAllSuspended());
		root.setProperty(PROP_ACTIVE, active);
		root.setProperty(PROP_SUSPENDED, suspended);
		String serialized = contentSerializer.serialize(root);
		
		//wirting
		OutputStreamWriter osw;
		try {
			osw = new OutputStreamWriter(new FileOutputStream(file), Charset.forName(UTF_8));
			osw.write(serialized);
			osw.close();
		} catch (FileNotFoundException e) {
			// Highly unprobable.
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** {@inheritDoc} */
	public void read(IUIRequestPool target){
		String serialized = "";
		try {
			serialized = new Scanner(file).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			// Highly unprobable.
		}
		
		Resource root = (Resource) contentSerializer.deserialize(serialized);
		if (serialized.length() > 5){
			List<UIRequest> suspended = (List<UIRequest>) root.getProperty(PROP_SUSPENDED);
			for (UIRequest uiRequest : suspended) {
				target.add(uiRequest);
				target.suspend(uiRequest.getDialogID());
			}

			List<UIRequest> active = (List<UIRequest>) root.getProperty(PROP_ACTIVE);
			for (UIRequest uiRequest : active) {
				target.add(uiRequest);
			}
		}
	}
	
}