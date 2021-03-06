/*******************************************************************************
 * Copyright 2013 Ericsson Nikola Tesla d.d.
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.universAAL.ui.dm.ui.preferences.editor;

import java.util.Hashtable;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.middleware.owl.SimpleOntology;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.rdf.ResourceFactory;
import org.universAAL.middleware.service.owl.InitialServiceDialog;
import org.universAAL.middleware.service.owls.profile.ServiceProfile;
import org.universAAL.ontology.ui.preferences.service.UIPreferencesService;

/**
 * Provides the service for starting the UI Preferences Editor Dialog.
 *
 * @author eandgrg
 */
public class UIPreferencesProvidedService extends UIPreferencesService {

	/**
	 * Default constructor.
	 *
	 * @param uri
	 *            Instance URI
	 */
	private UIPreferencesProvidedService(String uri) {
		super(uri);
	}

	public static final String NAMESPACE = "http://ontology.universAAL.org/UIPreferencesConsumer.owl#";

	public static final String MY_URI = NAMESPACE + "UIPreferencesProvidedService";

	static final String START_UI = NAMESPACE + "startUserInterface";

	/** The number of services provided by this class. */
	private static final int PROVIDED_SERVICES = 1;

	private static Hashtable<?, ?> serverPEditorRestrictions = new Hashtable();

	/**
	 *
	 * @return restrictions
	 */
	protected Hashtable<?, ?> getClassLevelRestrictions() {
		return serverPEditorRestrictions;
	}

	/**
	 * @param mcontext
	 * @return
	 */
	public static ServiceProfile[] getProfiles(ModuleContext mcontext) {
		ServiceProfile[] profiles = new ServiceProfile[PROVIDED_SERVICES];
		OntologyManagement.getInstance().register(mcontext,
				new SimpleOntology(MY_URI, UIPreferencesService.MY_URI, new ResourceFactory() {
					public Resource createInstance(String classURI, String instanceURI, int factoryIndex) {
						return new UIPreferencesProvidedService(instanceURI);
					}
				}));

		addRestriction((MergedRestriction) UIPreferencesService
				.getClassRestrictionsOnProperty(UIPreferencesService.MY_URI, UIPreferencesService.PROP_CONTROLS).copy(),
				new String[] { UIPreferencesService.PROP_CONTROLS }, serverPEditorRestrictions);
		// Url to be added to configurations/ui.dm/main menu: /UI Preferences
		// Editor|http://www.ent.hr|http://ontology.universaal.org/InteractionPreferencesProfile.owl#UIPreferencesService
		profiles[0] = InitialServiceDialog.createInitialDialogProfile(UIPreferencesService.MY_URI, "http://www.ent.hr",
				"UI Preferences dialog", START_UI);
		return profiles;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.universAAL.middleware.owl.ManagedIndividual#getClassURI()
	 */
	@Override
	public String getClassURI() {
		return MY_URI;
	}

}
