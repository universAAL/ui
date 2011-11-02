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
public class HTMLHandler extends GatewayPort implements IWebHandler {

    private static final long serialVersionUID = -4986118000986648808L;
    public static final String UNIVERSAAL_ASSOCIATED_LABEL = "urn:org.universAAL.dialog:AssociatedLabel";
    public static final String UNIVERSAAL_CLOCK_THREAD = "urn:org.universAAL.dialog:TheClockThread";
    public static final String UNIVERSAAL_FORM_CONTROL = "urn:org.universAAL.dialog:FormControl";
    public static final String UNIVERSAAL_PANEL_COLUMNS = "urn:org.universAAL.dialog:PanelColumns";
    public static final String title = "universAAL-Web-IOHandler";

    private BundleContext context = null;
    private OSubscriber os;
    private DefaultInputPublisher ip;

    private Hashtable<String, Boolean> waitingInputs;
    private Hashtable<String, OutputEvent> readyOutputs;
    private Hashtable<String, WebIOSession> userSessions;

    private final static Logger log = LoggerFactory
	    .getLogger(HTMLHandler.class);

    public HTMLHandler(BundleContext context) {
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
	    Hashtable<String, FormControl> assoc, boolean repeat) {
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
		html.append("<fieldset>");
		if (((Group) children[i]).getLabel() != null) {
		    html.append("<legend>" + ((Group) children[i]).getLabel()
			    + "</legend>");
		}
		html.append(renderGroupControl((Group) children[i], assoc));
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
		    + (repeat ? "</td>" : "<br>"));
	}
	return html.toString();
    }

    private String renderGroupControl(Group group,
	    Hashtable<String, FormControl> assoc) {
	return renderGroupControl(group, assoc, false);
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
	    html
		    .append("<input type=\"checkbox\" name=\""
			    + ctrl.getURI()
			    + "\" value=\""
			    + ((Boolean) initVal).booleanValue()
			    + "\" "
			    + ((((Boolean) initVal).booleanValue()) ? " checked=\"checked\""
				    : "\"\"") + " />");
	} else {
	    String type = "text";
	    if (((InputField) ctrl).isSecret())
		type = "password";
	    html.append("<input type=\"" + type + "\" name=\"" + ctrl.getURI()
		    + "\" value=\""
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
	html.append("<select name=\"" + ctrl.getURI() + "\" >");
	html.append(System.getProperty("line.separator"));
	Label[] labels = ctrl.getChoices();
	for (int i = 0; i < labels.length; i++) {
	    html.append("<option>");
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
	html.append("<select name=\"" + ctrl.getURI()
		+ "\" size=\"4\" multiple=\"multiple\">");
	html.append(System.getProperty("line.separator"));
	Label[] labels = ctrl.getChoices();
	for (int i = 0; i < labels.length; i++) {
	    html.append("<option>");
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
	    html.append("<input type=\"submit\" ");
	} else {
	    html.append("<input type=\"submit\" ");
	}
	html.append(" name=\"submit_"
		+ ctrl.getID()
		+ "\" value=\""
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
		    (groupflag ? (Group) elems[0] : ctrl), assoc, true));
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
	html.append("<textarea name=\"" + ctrl.getURI()
		+ "\" rows=\"4\" cols=\"20\">");
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
	// TODO Specific control of range (slider)
	return renderSpinnerControl(ctrl, assoc, mute);
    }

    private String renderSpinnerControl(Range ctrl,
	    Hashtable<String, FormControl> assoc, boolean mute) {
	// TODO Specific control of range (spinner)
	Label cl = ctrl.getLabel();
	StringBuilder html = new StringBuilder();
	if (cl != null && !mute)
	    if (cl.getText() != null)
		html.append("<label><b>" + cl.getText() + " </b>");
	int max = (Integer) ctrl.getMaxValue();
	int min = (Integer) ctrl.getMinValue();
	Integer initVal = (Integer) ctrl.getValue();
	int val = (initVal != null) ? initVal : min;
	html.append("<select name=\"" + ctrl.getURI() + "\" tabindex=\""
		+ (val - min) + "\">");
	html.append(System.getProperty("line.separator"));
	for (int i = 0; i < (max - min + 1); i++) {
	    html.append("<option>");
	    html.append(min + i);
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

    // --GW PORT (HANDLER)--
    @Override
    public String dataDir() {
	return "/webhandler";
    }

    @Override
    public String url() {
	return "/universAAL";
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
	// Check if it is the first time
	if (!userSessions.containsKey(userURI)) {
	    // Start and request main menu
	    log.info("Starting interaction and session with {} ", userURI);
	    userSessions.put(userURI, ses);
	    event = new InputEvent(new User(userURI), null,
		    InputEvent.uAAL_MAIN_MENU_REQUEST);
	    o = publish(event, Boolean.TRUE);// Sends the INPUT ev and wait for
	    // OUTPUT ev
	    ses.setCurrentOutputEvent(o);
	} else {
	    ses = (WebIOSession) userSessions.get(userURI);
	    // Fill the form inputs with the request data
	    java.util.Enumeration names = req.getParameterNames();
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
		.getResourceAsStream("webhandler/htmlbase.html");
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
		line = "<title>" + f.getTitle() + "</title>";
	    } else if (line.contains("<!-- Title -->")) {
		line = "<h2>" + f.getTitle() + "</h2>";
	    } else if (line.contains("<!-- Left -->")) {
		// if(f.getStandardButtons()!=null){
		// line=this.renderGroupControl(f.getStandardButtons(), assoc);
		// }else{
		// line = "Standard Buttons";//Only for reference
		// }
		line = "";// TODO: Do not render standard buttons
	    } else if (line.contains("<!-- Center -->")) {
		if (f.getIOControls() != null) {
		    line = this.renderGroupControl(f.getIOControls(), assoc);
		} else {
		    line = "";// "IO Controls";//Only for reference
		}
	    } else if (line.contains("<!-- Right -->")) {
		if (f.getSubmits() != null) {
		    line = this.renderGroupControl(f.getSubmits(), assoc);
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
