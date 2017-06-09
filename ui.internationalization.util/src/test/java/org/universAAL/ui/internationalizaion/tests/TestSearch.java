/*******************************************************************************
 * Copyright 2013 Universidad Politécnica de Madrid
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

package org.universAAL.ui.internationalizaion.tests;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.universAAL.container.JUnit.JUnitModuleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.owl.DataRepOntology;
import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.service.owl.ServiceBusOntology;
import org.universAAL.middleware.ui.owl.UIBusOntology;
import org.universAAL.ontology.language.Language;
import org.universAAL.ontology.language.LanguageOntology;
import org.universAAL.ontology.location.LocationOntology;
import org.universAAL.ontology.phThing.PhThingOntology;
import org.universAAL.ontology.profile.ProfileOntology;
import org.universAAL.ontology.shape.ShapeOntology;
import org.universAAL.ontology.space.SpaceOntology;
import org.universAAL.ontology.ui.preferences.GeneralInteractionPreferences;
import org.universAAL.ontology.ui.preferences.UIPreferencesProfileOntology;
import org.universAAL.ontology.ui.preferences.UIPreferencesSubProfile;
import org.universAAL.ontology.vcard.VCardOntology;
import org.universAAL.ui.internationalization.util.MessageLocaleHelper;

/**
 * @author amedrano
 *
 */
public class TestSearch extends TestCase {

	private static final String LOC = "loc";
	private static final String LANG = "lang";

	static ModuleContext mc = new JUnitModuleContext();

	static {
		OntologyManagement.getInstance().register(mc, new DataRepOntology());
		OntologyManagement.getInstance().register(mc, new ServiceBusOntology());
		OntologyManagement.getInstance().register(mc, new UIBusOntology());
		OntologyManagement.getInstance().register(mc, new LocationOntology());
		OntologyManagement.getInstance().register(mc, new ShapeOntology());
		OntologyManagement.getInstance().register(mc, new PhThingOntology());
		OntologyManagement.getInstance().register(mc, new SpaceOntology());
		OntologyManagement.getInstance().register(mc, new VCardOntology());
		OntologyManagement.getInstance().register(mc, new ProfileOntology());
		OntologyManagement.getInstance().register(mc, new LanguageOntology());
		OntologyManagement.getInstance().register(mc, new UIPreferencesProfileOntology());
	}

	private static Language getLanguageFromIso639(String code) {
		Set allLang = OntologyManagement.getInstance().getNamedSubClasses(Language.MY_URI, true, false);
		for (Iterator i = allLang.iterator(); i.hasNext();) {
			String uri = (String) i.next();
			Language l = (Language) Resource.getResource(uri, uri.toLowerCase());
			if (l.getIso639code().equals(code)) {
				return l;
			}
		}
		return null;
	}

	private UIPreferencesSubProfile getProfile() {
		GeneralInteractionPreferences gip = new GeneralInteractionPreferences();
		gip.setPreferredLanguage(getLanguageFromIso639("en"));
		gip.setSecondaryLanguage(getLanguageFromIso639("es"));

		assertNotNull(gip.getPreferredLanguage());
		assertNotNull(gip.getSecondaryLanguage());

		UIPreferencesSubProfile ps = new UIPreferencesSubProfile();
		ps.setInteractionPreferences(gip);
		return ps;
	}

	private UIPreferencesSubProfile getProfile2() {
		GeneralInteractionPreferences gip = new GeneralInteractionPreferences();
		gip.setPreferredLanguage(getLanguageFromIso639("de"));
		gip.setSecondaryLanguage(getLanguageFromIso639("de"));

		assertNotNull(gip.getPreferredLanguage());
		assertNotNull(gip.getSecondaryLanguage());

		UIPreferencesSubProfile ps = new UIPreferencesSubProfile();
		ps.setInteractionPreferences(gip);
		return ps;
	}

	private URL getLoc(String loc) {
		URL url = getClass().getClassLoader().getResource(loc + "/messages.properties");
		assertNotNull(url);
		return url;
	}

	public void test1() {
		// Normal usage, primary found
		List<URL> l = new ArrayList<URL>();
		l.add(getLoc("loc1"));
		MessageLocaleHelper mlh = null;
		try {
			mlh = new MessageLocaleHelper(mc, getProfile(), l);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		assertEquals("en", mlh.getString(LANG));
		assertEquals("loc1", mlh.getString(LOC));
	}

	public void test2() {
		// primary not found, but secondary is
		List<URL> l = new ArrayList<URL>();
		l.add(getLoc("loc2"));
		MessageLocaleHelper mlh = null;
		try {
			mlh = new MessageLocaleHelper(mc, getProfile(), l);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		assertEquals("es", mlh.getString(LANG));
		assertEquals("loc2", mlh.getString(LOC));
	}

	public void test3() {
		// only default
		List<URL> l = new ArrayList<URL>();
		l.add(getLoc("loc3"));
		MessageLocaleHelper mlh = null;
		try {
			mlh = new MessageLocaleHelper(mc, getProfile(), l);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		assertEquals("def", mlh.getString(LANG));
		assertEquals("loc3", mlh.getString(LOC));
	}

	public void test4() {
		// long search
		List<URL> l = new ArrayList<URL>();
		l.add(getLoc("loc3"));
		l.add(getLoc("loc2"));
		l.add(getLoc("loc1"));
		MessageLocaleHelper mlh = null;
		try {
			mlh = new MessageLocaleHelper(mc, getProfile(), l);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		assertEquals("en", mlh.getString(LANG));
		assertEquals("loc1", mlh.getString(LOC));
	}

	public void test5() {
		// long search, only secondary Lang
		List<URL> l = new ArrayList<URL>();
		l.add(getLoc("loc3"));
		l.add(getLoc("loc2"));
		l.add(getLoc("loc3"));
		l.add(getLoc("loc2"));
		l.add(getLoc("loc3"));
		l.add(getLoc("loc2"));
		MessageLocaleHelper mlh = null;
		try {
			mlh = new MessageLocaleHelper(mc, getProfile(), l);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		assertEquals("es", mlh.getString(LANG));
		assertEquals("loc2", mlh.getString(LOC));
	}

	public void test6() {
		// primary == secondary, only default
		List<URL> l = new ArrayList<URL>();
		l.add(getLoc("loc3"));
		MessageLocaleHelper mlh = null;
		try {
			mlh = new MessageLocaleHelper(mc, getProfile2(), l);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		assertEquals("def", mlh.getString(LANG));
		assertEquals("loc3", mlh.getString(LOC));
	}

	public void test7() {
		List<URL> l = new ArrayList<URL>();
		l.add(getLoc("loc3"));
		MessageLocaleHelper mlh = null;
		try {
			mlh = new MessageLocaleHelper(mc, getProfile2(), l);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		assertEquals("page 1 of 10", mlh.getString("key", new String[] { "1", "10" }));
	}
}
