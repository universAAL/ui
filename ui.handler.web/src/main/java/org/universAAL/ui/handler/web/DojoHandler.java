/*
	Copyright 2008-2010 ITACA-TSB, http://www.tsb.upv.es
	Instituto Tecnologico de Aplicaciones de Comunicacion 
	Avanzadas - Grupo Tecnologias para la Salud y el 
	Bienestar (TSB)
	
	See the NOTICE file distributed with this work for additional 
	information regarding copyright ownership
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	  http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */
package org.universAAL.ui.handler.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.universAAL.middleware.input.DefaultInputPublisher;
import org.universAAL.middleware.input.InputEvent;
import org.universAAL.middleware.io.owl.Modality;
import org.universAAL.middleware.io.rdf.Form;
import org.universAAL.middleware.io.rdf.FormControl;
import org.universAAL.middleware.io.rdf.Group;
import org.universAAL.middleware.io.rdf.InputField;
import org.universAAL.middleware.io.rdf.Label;
import org.universAAL.middleware.io.rdf.MediaObject;
import org.universAAL.middleware.io.rdf.Range;
import org.universAAL.middleware.io.rdf.Repeat;
import org.universAAL.middleware.io.rdf.Select;
import org.universAAL.middleware.io.rdf.Select1;
import org.universAAL.middleware.io.rdf.SimpleOutput;
import org.universAAL.middleware.io.rdf.Submit;
import org.universAAL.middleware.io.rdf.TextArea;
import org.universAAL.middleware.output.OutputEvent;
import org.universAAL.middleware.output.OutputEventPattern;
import org.universAAL.middleware.owl.Restriction;
import org.universAAL.ontology.profile.User;

import org.universAAL.ri.servicegateway.GatewayPort;

/**
 * @author <a href="mailto:alfiva@itaca.upv.es">Alvaro Fides Valero</a>
 * 
 */
public class DojoHandler extends GatewayPort implements IWebHandler {
	private static final long serialVersionUID = -4986118000986648808L;
	public static final String PERSONA_ASSOCIATED_LABEL = "urn:org.persona.dilog:AssociatedLabel";
	public static final String PERSONA_CLOCK_THREAD = "urn:org.persona.dilog:TheClockThread";
	public static final String PERSONA_FORM_CONTROL = "urn:org.persona.dilog:FormControl";
	public static final String PERSONA_PANEL_COLUMNS = "urn:org.persona.dilog:PanelColumns";
	public static final String title = "PERSONA-GUI-IOHandler";

	private BundleContext context = null;
	private OSubscriber os;
	private DefaultInputPublisher ip;

	private Hashtable<String, Boolean> waitingInputs;
	private Hashtable<String, OutputEvent> readyOutputs;
	private Hashtable<String, WebIOSession> userSessions;

	private final static Logger log = LoggerFactory
			.getLogger(DojoHandler.class);

	public DojoHandler(BundleContext context) {
		super();
		waitingInputs = new Hashtable<String, Boolean>();
		readyOutputs = new Hashtable<String, OutputEvent>();
		userSessions = new Hashtable<String, WebIOSession>();
		this.context = context;
		os = new OSubscriber(this.context, getOutputSubscriptionParams(), this);
		ip = new DefaultInputPublisher(this.context);
	}

	private OutputEventPattern getOutputSubscriptionParams() {
		// I am interested in all events with following OutputEventPattern
		// restrictions
		OutputEventPattern oep = new OutputEventPattern();
		// oep.addRestriction(Restriction.getAllValuesRestriction(
		// OutputEvent.PROP_HAS_ACCESS_IMPAIRMENT, new Enumeration(
		// new AccessImpairment[] {
		// new HearingImpairment(LevelRating.low),
		// new HearingImpairment(LevelRating.middle),
		// new HearingImpairment(LevelRating.high),
		// new HearingImpairment(LevelRating.full),
		// new SightImpairment(LevelRating.low),
		// new PhysicalImpairment(LevelRating.low)})));
		oep.addRestriction(Restriction.getFixedValueRestriction(
				OutputEvent.PROP_OUTPUT_MODALITY, Modality.sms));
		// oep.addRestriction(Restriction.getFixedValueRestriction(
		// OutputEvent.PROP_PRESENTATION_LOCATION, new
		// PLocation(MiddlewareConstants.PERSONA_MIDDLEWARE_LOCAL_ID_PREFIX+"Internet")));
		return oep;
	}

	public void finish(String userURI) {
		this.userSessions.remove(userURI);
		this.userURIs.remove(userURI);
		log.info("Finished user session for {} ", userURI);
	}

	void popMessage(Form f) {
		// TODO popup

	}

	public void updateScreenResolution(int max_x, int max_y, int min_x,
			int min_y) {
		// TODO Auto-generated method stub
		// Is this necessary?
	}

	// --RENDERERS-- //Maybe in the future will be moved to other class
	private String renderGroupControl(Group ctrl,
			Hashtable<String, FormControl> assoc, boolean repeat,
			boolean vertical) {
		StringBuilder html = new StringBuilder();
		FormControl[] children = ctrl.getChildren();
		if (children == null || children.length == 0)
			return null;

		for (int i = 0; i < children.length; i++) {
			if (repeat)
				html.append("<td>");
			if (children[i] instanceof InputField) {
				html.append(renderInputControl((InputField) children[i], assoc,
						repeat));
			} else if (children[i] instanceof SimpleOutput) {
				html.append(renderOutputControl((SimpleOutput) children[i],
						repeat));
			} else if (children[i] instanceof Select1) {
				html.append(renderSelect1Control((Select1) children[i], assoc,
						repeat));
			} else if (children[i] instanceof Select) {
				html.append(renderSelectControl((Select) children[i], assoc,
						repeat));
			} else if (children[i] instanceof Repeat) {
				html.append("<fieldset>");
				if (((Group) children[i]).getLabel() != null) {
					html.append("<legend>" + ((Group) children[i]).getLabel()
							+ "</legend>");
				}
				html.append(renderRepeat((Repeat) children[i], assoc));
				html.append("</fieldset>");
			} else if (children[i] instanceof Group) {
				boolean display = (((Group) children[i]).getLabel() != null);
				if (display) {
					html.append("<fieldset>");
					html.append("<legend>"
							+ ((Group) children[i]).getLabel().getText()
							+ "</legend>");
				}
				String content = renderGroupControl((Group) children[i], assoc,
						!vertical);
				if (content != null)
					html.append(content);
				if (display)
					html.append("</fieldset>");
			} else if (children[i] instanceof Submit) {
				// also instances of SubdialogTrigger can be treated the same
				html.append(renderSubmitControl((Submit) children[i], assoc));
			} else if (children[i] instanceof MediaObject) {
				html
						.append(renderMediaObject((MediaObject) children[i],
								repeat));
			} else if (children[i] instanceof TextArea) {
				html.append(renderTextArea((TextArea) children[i], assoc,
						repeat));
			} else if (children[i] instanceof Range) {
				Range tempRng = (Range) children[i];
				if ((Integer) tempRng.getMaxValue() > 10)
					html.append(renderSpinnerControl((Range) children[i],
							assoc, repeat));
				else
					html.append(renderRangeControl((Range) children[i], assoc,
							repeat));
			}
			html.append(System.getProperty("line.separator")
					+ (repeat ? "</td>" : (vertical ? "<br>" : " ")));
		}
		return html.toString();
	}

	private String renderGroupControl(Group group,
			Hashtable<String, FormControl> assoc, boolean vertical) {
		return renderGroupControl(group, assoc, false, vertical);
	}

	private String renderOutputControl(final FormControl ctrl, boolean mute) {
		Label cl = ctrl.getLabel();
		StringBuilder html = new StringBuilder();
		if (cl != null && !mute)
			if (cl.getText() != null)
				html.append("<b>" + cl.getText() + " </b>");
		Object initVal = ctrl.getValue();
		if (initVal != null) {
			html.append(initVal.toString().replace("\n", "<br>"));
		}
		return html.toString();
	}

	private String renderInputControl(final FormControl ctrl,
			Hashtable<String, FormControl> assoc, boolean mute) {
		StringBuilder html = new StringBuilder();
		Label cl = ctrl.getLabel();
		Object initVal = ctrl.getValue();
		if (cl != null && !mute)
			if (cl.getText() != null)
				html.append("<label><b>" + cl.getText() + " </b>");
		if (ctrl.isOfBooleanType() && initVal instanceof Boolean) {
			html.append("<input type=\"hidden\" name=\"" + ctrl.getURI()
					+ "\" value=\"false" + "\" />");
			html
					.append("<input dojotype=\"dijit.form.CheckBox\" type=\"checkbox\" name=\""
							+ ctrl.getURI()
							+ "\" value=\"true"
							+ "\" "
							+ ((((Boolean) initVal).booleanValue()) ? " checked=\"checked\""
									: "\"\"") + " />");
		} else {
			String type = "text";
			if (((InputField) ctrl).isSecret())
				type = "password";
			html.append("<input dojotype=\"dijit.form.TextBox\" type=\"" + type
					+ "\" name=\"" + ctrl.getURI() + "\" value=\""
					+ ((initVal != null) ? initVal.toString() : "")
					+ "\" size=\" "
					+ ((initVal != null) ? initVal.toString().length() : "")
					+ "\" />");
		}
		if (cl != null && !mute)
			if (cl.getText() != null)
				html.append("</label>");
		assoc.put(ctrl.getURI(), ctrl);
		return html.toString();
	}

	private Object renderSelect1Control(Select1 ctrl,
			Hashtable<String, FormControl> assoc, boolean mute) {
		Label cl = ctrl.getLabel();
		StringBuilder html = new StringBuilder();
		if (cl != null && !mute)
			if (cl.getText() != null)
				html.append("<label><b>" + cl.getText() + " </b>");
		html.append("<select dojotype=\"dijit.form.FilteringSelect\" name=\""
				+ ctrl.getURI() + "\" " + "autoComplete=\"true\" " +
				// "invalidMessage=\"Invalid value\" " +
				"value=");
		html.append(System.getProperty("line.separator"));
		Label[] labels = ctrl.getChoices();
		boolean firstOpt = true;
		for (int i = 0; i < labels.length; i++) {
			if (firstOpt) {
				html.append("\"" + labels[i].getText() + "\" >"
						+ "<option selected=\"selected\" value=\""
						+ labels[i].getText() + "\">");
				firstOpt = false;
			} else {
				html.append("<option value=\"" + labels[i].getText() + "\">");
			}
			html.append(labels[i].getText());
			html.append("</option>");
			html.append(System.getProperty("line.separator"));
		}
		html.append("</select>");
		if (cl != null && !mute)
			if (cl.getText() != null)
				html.append("</label>");
		assoc.put(ctrl.getURI(), ctrl);
		return html.toString();
	}

	private Object renderSelectControl(Select ctrl,
			Hashtable<String, FormControl> assoc, boolean mute) {
		Label cl = ctrl.getLabel();
		StringBuilder html = new StringBuilder();
		if (cl != null && !mute)
			if (cl.getText() != null)
				html.append("<label><b>" + cl.getText() + " </b>");
		html.append("<select dojotype=\"dijit.form.MultiSelect\" name=\""
				+ ctrl.getURI() + "\" " + "size=\"4\" multiple=\"multiple\">");
		html.append(System.getProperty("line.separator"));
		Label[] labels = ctrl.getChoices();
		for (int i = 0; i < labels.length; i++) {
			html.append("<option value=\"" + labels[i].getText() + "\">");
			html.append(labels[i].getText());
			html.append("</option>");
			html.append(System.getProperty("line.separator"));
		}
		html.append("</select>");
		if (cl != null && !mute)
			if (cl.getText() != null)
				html.append("</label>");
		assoc.put(ctrl.getURI(), ctrl);
		return html.toString();
	}

	private Object renderSubmitControl(Submit ctrl,
			Hashtable<String, FormControl> assoc) {
		Label cl = ctrl.getLabel();
		StringBuilder html = new StringBuilder();
		String imageURL = null;
		if (cl != null)
			imageURL = cl.getIconURL();
		if (imageURL != null) {
			// html.append("<input type=\"image\" src=\""+imageURL+"\" ");//TODO
			// doesnt work like submit
			html
					.append("<input dojotype=\"dijit.form.Button\" type=\"submit\" ");
		} else {
			html
					.append("<input dojotype=\"dijit.form.Button\" type=\"submit\" ");
		}
		html.append(" name=\"submit_"
				+ ctrl.getID()
				+ "\" value=\""
				+ (cl != null ? (cl.getText() != null ? cl.getText() : ctrl
						.getID()) : (ctrl.getID()))
				+ "\" label=\""
				+ (cl != null ? (cl.getText() != null ? cl.getText() : ctrl
						.getID()) : (ctrl.getID())) + "\" />");
		assoc.put(ctrl.getID(), ctrl);
		return html.toString();
	}

	private Object renderRepeat(Repeat ctrl,
			Hashtable<String, FormControl> assoc) {
		StringBuilder html = new StringBuilder();
		FormControl[] elems = ctrl.getChildren();
		boolean groupflag = false;
		html.append("<table border=\"1\" cellspacing=\"0\" ><thead>");
		if (elems == null || elems.length != 1)
			throw new IllegalArgumentException("Malformed argument!");
		if (elems[0] instanceof Group) {
			groupflag = true;
			FormControl[] elems2 = ((Group) elems[0]).getChildren();
			if (elems2 == null || elems2.length == 0)
				throw new IllegalArgumentException("Malformed argument!");
			for (int i = 0; i < elems2.length; i++) {
				if (elems2[i].getLabel() != null) {
					html.append("<th>" + elems2[i].getLabel() + "</th>");
				} else {
					html.append("<th>" + elems2[i].getType() + "</th>");
				}
			}
		} else if (elems[0] == null)
			throw new IllegalArgumentException("Malformed argument!");
		html.append("</thead><tbody>");
		for (int i = 0; i < ctrl.getNumberOfValues(); i++) {
			html.append("<tr>");
			ctrl.setSelection(i);
			html.append(renderGroupControl(
					(groupflag ? (Group) elems[0] : ctrl), assoc, true, true));
			assoc.put(ctrl.getURI(), ctrl);
			html.append("</tr>");
		}
		html.append("</tbody></table>");
		return html.toString();
	}

	private String renderMediaObject(MediaObject ctrl, boolean mute) {
		Label cl = ctrl.getLabel();
		StringBuilder html = new StringBuilder();
		if (cl != null && !mute)
			if (cl.getText() != null)
				html.append("<b>" + cl.getText() + " </b>");
		String src = ctrl.getContentURL();
		String alt = ctrl.getHintString();
		int w = ctrl.getResolutionPreferredX();
		int h = ctrl.getResolutionPreferredY();
		if (src != null) {
			html.append("<img src=\"" + src + "\"");
			if (alt != null) {
				html.append(" alt=\"" + alt + "\"");
			}
			if (w > 0 && h > 0)
				html.append(" width=\"" + w + "\" height=\"" + h + "\" ");
			html.append(" />");
		}
		return html.toString();
	}

	private String renderTextArea(TextArea ctrl,
			Hashtable<String, FormControl> assoc, boolean mute) {
		Label cl = ctrl.getLabel();
		StringBuilder html = new StringBuilder();
		if (cl != null && !mute)
			if (cl.getText() != null)
				html.append("<label><b>" + cl.getText() + " </b>");
		String initVal = (String) ctrl.getValue();
		html
				.append("<textarea dojotype=\"dijit.form.SimpleTextarea\" name=\""
						+ ctrl.getURI()
						+ "\" "
						+ "style=\"width:300px;height:100px\">");// ;min-height:100px;max-height:400px;overflow-y:auto\" >");
		if (initVal != null) {
			html.append(initVal.toString());
		}
		html.append("</textarea>");
		if (cl != null && !mute)
			if (cl.getText() != null)
				html.append("</label>");
		assoc.put(ctrl.getURI(), ctrl);
		return html.toString();
	}

	private String renderRangeControl(Range ctrl,
			Hashtable<String, FormControl> assoc, boolean mute) {
		Label cl = ctrl.getLabel();
		StringBuilder html = new StringBuilder();
		if (cl != null && !mute)
			if (cl.getText() != null)
				html.append("<label><b>" + cl.getText() + " </b>");
		int max = (Integer) ctrl.getMaxValue();
		int min = (Integer) ctrl.getMinValue();
		Integer initVal = (Integer) ctrl.getValue();
		int val = (initVal != null) ? initVal : min;
		html.append("<div dojotype=\"dijit.form.HorizontalSlider\" name=\""
				+ ctrl.getURI() + "\" " + "value=\"" + val + "\" "
				+ "discreteValues=\"" + (max - min + 1) + "\" " + "minimum=\""
				+ min + "\" " + "maximum=\"" + max + "\">");
		html.append(System.getProperty("line.separator"));
		html.append("<div dojotype=\"dijit.form.HorizontalRule\" "
				+ "container=\"rule\" count=\"" + (max - min + 1)
				+ "\" style=\"height=5px\"></div>");
		html
				.append("<ol dojotype=\"dijit.form.HorizontalRuleLabels\" "
						+ "container=\"rule\" style=\"height:1em;font-size:75%;color:gray;\">");
		for (int i = 0; i < (max - min + 1); i++) {
			html.append("<li>" + (i + min) + "</li>");
		}
		html.append("</ol>");
		if (cl != null && !mute)
			if (cl.getText() != null)
				html.append("</div></label>");
		assoc.put(ctrl.getURI(), ctrl);
		return html.toString();
	}

	private String renderSpinnerControl(Range ctrl,
			Hashtable<String, FormControl> assoc, boolean mute) {
		Label cl = ctrl.getLabel();
		StringBuilder html = new StringBuilder();
		if (cl != null && !mute)
			if (cl.getText() != null)
				html.append("<label><b>" + cl.getText() + " </b>");
		int max = (Integer) ctrl.getMaxValue();
		int min = (Integer) ctrl.getMinValue();
		int delta = Integer.parseInt(ctrl.getStep().toString());
		Integer initVal = (Integer) ctrl.getValue();
		int val = (initVal != null) ? initVal : min;
		html
				.append("<input dojotype=\"dijit.form.NumberSpinner\" name=\""
						+ ctrl.getURI() + "\" " + "value=\"" + val + "\" "
						+ "smallDelta=\"" + delta + "\" "
						+
						// "rangeMessage=\"Out of range\" " +
						"constraints=\"{min:" + min + ",max:" + max
						+ ",places:0}\" />");
		html.append(System.getProperty("line.separator"));
		if (cl != null && !mute)
			if (cl.getText() != null)
				html.append("</label>");
		assoc.put(ctrl.getURI(), ctrl);
		return html.toString();
	}

	// --GW PORT (HANDLER)--
	@Override
	public String dataDir() {
		return "/webhandler";
	}

	@Override
	public String url() {
		return "/persona";
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		InputEvent event;
		OutputEvent o;
		WebIOSession ses = new WebIOSession();

		// Check if user is authorized
		if (!handleAuthorization(req, resp)) {
			log.info("Received unauthorized HTTP request");
			return;
		}
		// --Input processing--
		String[] userPass = getUserPass(req.getHeader("Authorization"));
		String userURI = userURIs.get(userPass[0]);
		log.info("Received HTTP request from user {} ", userURI);
		// Check if it�s the first time
		if (!userSessions.containsKey(userURI)) {
			// Start and request main menu
			log.info("Starting interaction and session with {} ", userURI);
			userSessions.put(userURI, ses);
			event = new InputEvent(new User(userURI), null/*
														 * newPLocation(
														 * MiddlewareConstants.
														 * PERSONA_MIDDLEWARE_LOCAL_ID_PREFIX
														 * +"Internet")
														 */,
					InputEvent.uAAL_MAIN_MENU_REQUEST);
			o = publish(event, Boolean.TRUE);// Sends the INPUT ev and wait for
												// OUTPUT ev
			ses.setCurrentOutputEvent(o);
		} else {
			ses = (WebIOSession) userSessions.get(userURI);
			// Fill the form inputs with the request data
			Enumeration names = req.getParameterNames();
			Submit selectedSubmit = null;
			for (; names.hasMoreElements();) {
				String name = (String) names.nextElement();
				if (name.startsWith("submit_")) {
					String submit = name.substring(7);
					selectedSubmit = (Submit) ses.getCurrentFormAssociation()
							.get(submit);
				} else {
					FormControl ctrl = (FormControl) ses
							.getCurrentFormAssociation().get(name);
					if (ctrl instanceof InputField) {
						if (ctrl.isOfBooleanType()) {
							String[] values = req.getParameterValues(name);
							Boolean value = (values.length > 1) ? true
									: Boolean.parseBoolean(values[0]);
							((InputField) ctrl).storeUserInput(value);
						} else {
							((InputField) ctrl).storeUserInput(req
									.getParameter(name));
						}
					} else if (ctrl instanceof Select) {
						if (ctrl instanceof Select1) {
							if (ctrl.isOfBooleanType()) {
								((Select1) ctrl).storeUserInput(Boolean
										.parseBoolean(req.getParameter(name)));
							} else {
								((Select1) ctrl)
										.storeUserInputByLabelString(req
												.getParameter(name));
							}
						} else {
							if (ctrl.isOfBooleanType()) {
								String[] values = req.getParameterValues(name);
								ArrayList<Boolean> list = new ArrayList<Boolean>(
										values.length);
								for (int i = 0; i < values.length; i++) {
									list.add(Boolean.parseBoolean(values[i]));
								}
								((Select) ctrl).storeUserInput(list);
							} else {
								String[] values = req.getParameterValues(name);
								ArrayList<String> list = new ArrayList<String>(
										values.length);
								for (int i = 0; i < values.length; i++) {
									list.add(values[i]);
								}
								((Select) ctrl).storeUserInput(list);
							}
						}

					} else if (ctrl instanceof TextArea) {
						((TextArea) ctrl)
								.storeUserInput(req.getParameter(name));
					} else if (ctrl instanceof Range) {
						((Range) ctrl).storeUserInput(Integer.parseInt(req
								.getParameter(name)));
					}
				}
			}
			// build and send the InputEvent
			o = dialogFinished(selectedSubmit, userURI);// Sends the INPUT ev
														// and wait for OUTPUT
														// ev
			ses.setCurrentOutputEvent(o);
		}

		// --Output processing--
		// Load the template
		InputStream pageReader = this.getClass().getClassLoader()
				.getResourceAsStream("webhandler/dojobase.html");
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				pageReader));
		StringBuilder html = new StringBuilder();
		String line;
		// Build the html
		Hashtable<String, FormControl> assoc = new Hashtable<String, FormControl>();// This
																					// hashtable
																					// will
																					// replace
																					// the
																					// old
																					// one
																					// containing
																					// the
																					// past
																					// input
		Form f = o.getDialogForm();
		while ((line = reader.readLine()) != null) {
			if (line.contains("<!-- Page title -->")) {
				line = f.getTitle();
			} else if (line.contains("<!-- Title -->")) {
				line = f.getTitle();
			} else if (line.contains("<!-- Left -->")) {
				// if(f.getStandardButtons()!=null){
				// line=this.renderGroupControl(f.getStandardButtons(), assoc,
				// true);
				// }else{
				// line = "Standard Buttons";//Only for reference
				// }
				line = "";// TODO: Do not render standard buttons
			} else if (line.contains("<!-- Center -->")) {
				if (f.getIOControls() != null) {
					line = this.renderGroupControl(f.getIOControls(), assoc,
							true);
				} else {
					line = "";// "IO Controls";//Only for reference
				}
			} else if (line.contains("<!-- Right -->")) {
				if (f.getSubmits() != null) {
					line = this.renderGroupControl(f.getSubmits(), assoc, true);
				} else {
					line = "";// "Submits";//Only for reference
				}
			}
			html.append(line);
			html.append(System.getProperty("line.separator"));
		}
		// Set the current form in session and send response
		ses.setCurrentFormAssociation(assoc);
		PrintWriter out = resp.getWriter();
		resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		resp.setContentType("text/html");
		out.println(html.toString());
		log.info("Rendered HTML response page");
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	public final OutputEvent publish(InputEvent event, Boolean first) {
		OutputEvent o = null;
		synchronized (waitingInputs) {
			String user = ((User) event.getUser()).getURI();
			waitingInputs.put(user, first);
			log.info("Publishing Input event for user {} ", user);
			ip.publish(event);
			while (o == null) {
				try {
					log.info("Waiting for Outputs");
					waitingInputs.wait();
					o = (OutputEvent) readyOutputs.remove(user);
					log.info("Got Output");
				} catch (InterruptedException e) {
					log.error("Exception while waiting for Outputs: {} ", e);
					e.printStackTrace();
				}
			}
		}
		return o;
	}

	public OutputEvent dialogFinished(Submit s, String userURI) {
		log.info("User {} pressed a button", userURI);
		if (s == null)
			return this.userSessions.get(userURI).getCurrentOutputEvent();
		// for the next line, see the comment within handleOutputEvent() above
		// [preserved from SwingRenderer]
		Object o = s.getFormObject().getProperty(OutputEvent.MY_URI);
		if (o instanceof OutputEvent) {
			// a popup action is being finished
			os.dialogFinished(s, true);
			return this.publish(new InputEvent(((OutputEvent) o)
					.getAddressedUser(), ((OutputEvent) o)
					.getPresentationAbsLocation(), s), Boolean.FALSE);
		} else {
			os.dialogFinished(s, false);
			synchronized (os) {
				InputEvent ie = new InputEvent(
						((WebIOSession) this.userSessions.get(userURI))
								.getCurrentOutputEvent().getAddressedUser(),
						((WebIOSession) this.userSessions.get(userURI))
								.getCurrentOutputEvent()
								.getPresentationAbsLocation(), s);
				if (s.getDialogID().equals(os.dialogID))
					((WebIOSession) this.userSessions.get(userURI))
							.setCurrentOutputEvent(null);
				return this.publish(ie, Boolean.FALSE);
			}
		}
	}

	@Override
	public Hashtable<String, OutputEvent> getReadyOutputs() {
		return this.readyOutputs;
	}

	@Override
	public Hashtable<String, WebIOSession> getUserSessions() {
		return this.userSessions;
	}

	@Override
	public Hashtable<String, Boolean> getWaitingInputs() {
		return this.waitingInputs;
	}

}
