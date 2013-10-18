/*******************************************************************************
 * Copyright 2013 Universidad Politécnica de Madrid - Life Supporting Technologies
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

package org.universAAL.ui.ui.handler.web.html.model;


import java.util.Properties;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.ui.rdf.InputField;
import org.universAAL.ui.ui.handler.web.html.HTMLUserGenerator;

/**
 * @author amedrano
 * 
 */
public class InputFieldModel extends InputModel {

	/**
	 * @param fe
	 * @param render
	 */
	public InputFieldModel(InputField fe, HTMLUserGenerator render) {
		super(fe, render);
	}

	/** {@ inheritDoc} */
	public boolean updateInput(String[] strings) {
		boolean res = false;
		try {
			InputField i = (InputField) fe;
			if ( strings != null && strings.length > 0) {
				Object val = i.getValue();
				if (i.isOfBooleanType()){
					res = i.storeUserInput(Boolean.valueOf(strings[0]));
				}
				if (val instanceof Integer) {
					res = i.storeUserInput(Integer.decode(strings[0]));
				}
				if (val instanceof Long){
					res = i.storeUserInput(Long.decode(strings[0]));				
				}
				if (val instanceof Float){
					res = i.storeUserInput(new Float(Float.parseFloat(strings[0])));
					
				}
				if (val instanceof Double){
					res = i.storeUserInput(new Double(Double.parseDouble(strings[0])));
				}
				if (val instanceof XMLGregorianCalendar){
					try {
						val = DatatypeFactory.newInstance().newXMLGregorianCalendar(strings[0]);
						res = i.storeUserInput(val);
					} catch (DatatypeConfigurationException e1) {
						LogUtils.logError(getRenderer().getModuleContext(),
								getClass(), 
								"updateInput", "unable to convert \"" 
								+ strings[0]+ "\" to XMLGregorianCalendar");
					}
				}
				if(val == null || !res){
					res=i.storeUserInput(strings[0]);
				}
			}
		} catch (Exception e) {
			LogUtils.logInfo(getRenderer().getModuleContext(), 
					getClass(), "updateImput", new String[]{"unable to update input "}, e);
		}
		return res;
	}

	/**
	 * the representation for InputField can either be
	 * <ul>
	 * <li>a checkbox if the {@link InputField#getValue()} is a boolean type
	 * <li>a text if the {@link InputField#getValue()} is a String and not
	 * secret
	 * <li>a password if the {@link InputField#getValue()} is String and it is
	 * secret
	 * <li>a datetime-local if the {@link InputField#getValue()} is a
	 * XMLGregorianCalendar
	 * <li>a ?? if the {@link InputField#getValue()} is a Duration
	 * <li>a number if the {@link InputField#getValue()} is a Integer
	 * <li>a number if the {@link InputField#getValue()} is a Long
	 * <li>a number if the {@link InputField#getValue()} is a Float
	 * <li>a number if the {@link InputField#getValue()} is a Double
	 * <li>a ?? if the {@link InputField#getValue()} is a Locale
	 * </ul>
	 * 
	 * @return {@inheritDoc}
	 */
	public StringBuffer generateInputHTML() {
		InputField i = (InputField) fe;
		Object val = i.getValue();
		setInputTypeProperties(fcProps, val);
		if (i.isSecret()){
			fcProps.put("type", "password");
		} 
		StringBuffer defaultInput = new StringBuffer();
		if (i.isOfBooleanType()){
			Properties defInProp = new Properties();
			defInProp.put("type", "hidden");
			defInProp.put("name", fcProps.get("name"));
			defInProp.put("value", "false");
			defaultInput.append(singleTag("input",defInProp ));
		}
		return singleTag("input", fcProps).append(defaultInput);
	}

	/**
	 * Add properties to input properties. According to type of val
	 * the type and value of the input will be generated.
	 * @param prop the properties to edit
	 * @param val the value to work with.
	 */
	public static void setInputTypeProperties(Properties prop, Object val) {
		// Default.
		prop.put("type", "text");
		if (val != null
				&& !val.toString().isEmpty()){
			prop.put("value", val.toString());
		}

		if (val instanceof Boolean) {
			prop.put("type", "checkbox");
			prop.put("value", "true");
			if (((Boolean)val).booleanValue()){
				prop.put("checked", "");
			}
		}
		if (val instanceof XMLGregorianCalendar){
			QName type = ((XMLGregorianCalendar) val).getXMLSchemaType();

			if (type.equals(DatatypeConstants.DATETIME)){
				prop.put("type", "datetime-local");
				prop.put("value", ((XMLGregorianCalendar) val).toString());
			}
			if (type.equals(DatatypeConstants.TIME)){
				prop.put("type", "time");
				prop.put("value", ((XMLGregorianCalendar) val).toString());
			}
			if (type.equals(DatatypeConstants.DATE)){
				prop.put("type", "date");
				prop.put("value", ((XMLGregorianCalendar) val).toString());
			}
		}
		if (val instanceof Integer
				|| val instanceof Long){
			prop.put("type", "number");
			prop.put("step", "1");
			prop.put("pattern", "[-+]?[0-9]*");
		}
		if (val instanceof Float
				|| val instanceof Double){
			prop.put("type", "number");
			prop.put("step", "any");
			prop.put("pattern", "[-+]?[0-9]*\\.?[0-9]+");
		}
	}
}
