/*
	Copyright 2008-2010 ITACA-TSB, http://www.tsb.upv.es
	Instituto Tecnologico de Aplicaciones de Comunicacion 
	Avanzadas - Grupo Tecnologias para la Salud y el 
	Bienestar (TSB)
	
	2010-2012 Ericsson Nikola Tesla d.d., www.ericsson.com/hr
	
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

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.middleware.ui.UIHandlerProfile;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.UIResponse;
import org.universAAL.middleware.ui.owl.Modality;
import org.universAAL.middleware.ui.rdf.Form;
import org.universAAL.middleware.ui.rdf.FormControl;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.InputField;
import org.universAAL.middleware.ui.rdf.Label;
import org.universAAL.middleware.ui.rdf.MediaObject;
import org.universAAL.middleware.ui.rdf.Range;
import org.universAAL.middleware.ui.rdf.Repeat;
import org.universAAL.middleware.ui.rdf.Select;
import org.universAAL.middleware.ui.rdf.Select1;
import org.universAAL.middleware.ui.rdf.SimpleOutput;
import org.universAAL.middleware.ui.rdf.Submit;
import org.universAAL.middleware.ui.rdf.TextArea;
import org.universAAL.middleware.util.Constants;
import org.universAAL.ontology.location.Location;
import org.universAAL.ontology.profile.Caregiver;
import org.universAAL.ontology.profile.User;
import org.universAAL.ri.servicegateway.GatewayPort;

/**
 * @author <a href="mailto:alfiva@itaca.upv.es">Alvaro Fides Valero</a>
 * @author eandgrg
 * 
 */
public class DojoRenderer extends GatewayPort implements IWebRenderer {
    private static final long serialVersionUID = -4986118000986648808L;
    public static final String RENDERER_NAME = "universAAL-Web-Dojo-UIHandler";

    private MyUIHandler myUIHandler;

    private Hashtable<String, Boolean> waitingInputs; // user, first
    private Hashtable<String, UIRequest> readyOutputs; // userUri, UIRequest
    private Hashtable<String, WebIOSession> userSessions; // user, web session

    private ModuleContext mContext;

    private VisualPreferencesHelper visualPreferencesHelper;

    public DojoRenderer(final ModuleContext mcontext) {
	super();
	mContext = mcontext;
	waitingInputs = new Hashtable<String, Boolean>(); // user, isFirst
	readyOutputs = new Hashtable<String, UIRequest>();
	userSessions = new Hashtable<String, WebIOSession>();
	myUIHandler = new MyUIHandler(mcontext, getHandlerSubscriptionParams(),
		this);
	visualPreferencesHelper = new VisualPreferencesHelper();
    }

    private UIHandlerProfile getHandlerSubscriptionParams() {
	// I am interested in all requests with following UIHandlerProfile
	// restrictions
	UIHandlerProfile oep = new UIHandlerProfile();
	oep.addRestriction(MergedRestriction.getFixedValueRestriction(
		UIRequest.PROP_PRESENTATION_MODALITY, Modality.web));
	oep.addRestriction(MergedRestriction
		.getFixedValueRestriction(UIRequest.PROP_PRESENTATION_LOCATION,
			new Location(Constants.uAAL_MIDDLEWARE_LOCAL_ID_PREFIX
				+ "Internet")));
	return oep;
    }

    /**
     * When any user has authenticated, this method will change the request
     * pattern to receive only addressed {@link UIRequest}.
     * 
     * @param user
     *            user for whom the {@link UIRequest} should be addressed to.
     */
    private void userAuthenticated(final User user) {
	UIHandlerProfile oep = getHandlerSubscriptionParams();
	oep.addRestriction(MergedRestriction.getFixedValueRestriction(
		UIRequest.PROP_ADDRESSED_USER, user));

	myUIHandler.addNewRegParams(oep);
    }

    public final void finish(final String userURI) {
	this.userSessions.remove(userURI);
	this.userURIs.remove(userURI);
	LogUtils
		.logInfo(mContext, this.getClass(), "finish",
			new Object[] { "Finished user session for userURI:"
				+ userURI }, null);

	myUIHandler.unSetCurrentUser(new Caregiver(userURI));
    }

    void popMessage(final Form f) {
	// TODO popup

    }

    // RENDERERS

    // Maybe in the future will be moved to other class
    private String renderGroupControl(final Group ctrl,
	    final Hashtable<String, FormControl> assoc, final boolean repeat,
	    final boolean vertical) {
	StringBuilder html = new StringBuilder();
	FormControl[] children = ctrl.getChildren();
	if (children == null || children.length == 0) {
	    return null;
	}

	for (int i = 0; i < children.length; i++) {
	    if (repeat) {
		html.append("<td>");
	    }
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
		if (content != null) {
		    html.append(content);
		}
		if (display) {
		    html.append("</fieldset>");
		}
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
		if ((Integer) tempRng.getMaxValue() > 10) {
		    html.append(renderSpinnerControl((Range) children[i],
			    assoc, repeat));
		} else {
		    html.append(renderRangeControl((Range) children[i], assoc,
			    repeat));
		}
	    }
	    html.append(System.getProperty("line.separator")
		    + (repeat ? "</td>" : (vertical ? "<br>" : " ")));
	}
	return html.toString();
    }

    private String renderGroupControl(final Group group,
	    final Hashtable<String, FormControl> assoc, final boolean vertical) {
	return renderGroupControl(group, assoc, false, vertical);
    }

    private String renderOutputControl(final FormControl ctrl,
	    final boolean mute) {
	Label cl = ctrl.getLabel();
	StringBuilder html = new StringBuilder();
	if (cl != null && !mute) {
	    if (cl.getText() != null) {
		html.append("<b>" + cl.getText() + " </b>");
	    }
	}
	Object initVal = ctrl.getValue();
	if (initVal != null) {
	    html.append(initVal.toString().replace("\n", "<br>"));
	}
	return html.toString();
    }

    private String renderInputControl(final FormControl ctrl,
	    final Hashtable<String, FormControl> assoc, final boolean mute) {
	StringBuilder html = new StringBuilder();
	Label cl = ctrl.getLabel();
	Object initVal = ctrl.getValue();
	if (cl != null && !mute) {
	    if (cl.getText() != null) {
		html.append("<label><b>" + cl.getText() + " </b>");
	    }
	}
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
	    if (((InputField) ctrl).isSecret()) {
		type = "password";
	    }
	    html.append("<input dojotype=\"dijit.form.TextBox\" type=\"" + type
		    + "\" name=\"" + ctrl.getURI() + "\" value=\""
		    + ((initVal != null) ? initVal.toString() : "")
		    + "\" size=\" "
		    + ((initVal != null) ? initVal.toString().length() : "")
		    + "\" />");
	}
	if (cl != null && !mute) {
	    if (cl.getText() != null) {
		html.append("</label>");
	    }
	}
	assoc.put(ctrl.getURI(), ctrl);
	return html.toString();
    }

    private Object renderSelect1Control(final Select1 ctrl,
	    final Hashtable<String, FormControl> assoc, final boolean mute) {
	Label cl = ctrl.getLabel();
	StringBuilder html = new StringBuilder();
	if (cl != null && !mute) {
	    if (cl.getText() != null) {
		html.append("<label><b>" + cl.getText() + " </b>");
	    }
	}
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
	if (cl != null && !mute) {
	    if (cl.getText() != null) {
		html.append("</label>");
	    }
	}
	assoc.put(ctrl.getURI(), ctrl);
	return html.toString();
    }

    private Object renderSelectControl(final Select ctrl,
	    final Hashtable<String, FormControl> assoc, final boolean mute) {
	Label cl = ctrl.getLabel();
	StringBuilder html = new StringBuilder();
	if (cl != null && !mute) {
	    if (cl.getText() != null) {
		html.append("<label><b>" + cl.getText() + " </b>");
	    }
	}
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
	if (cl != null && !mute) {
	    if (cl.getText() != null) {
		html.append("</label>");
	    }
	}
	assoc.put(ctrl.getURI(), ctrl);
	return html.toString();
    }

    private Object renderSubmitControl(final Submit ctrl,
	    final Hashtable<String, FormControl> assoc) {
	Label cl = ctrl.getLabel();
	StringBuilder html = new StringBuilder();
	String imageURL = null;
	if (cl != null) {
	    imageURL = cl.getIconURL();
	}
	if (imageURL != null) {
	    // html.append("<input type=\"image\" src=\""+imageURL+"\" ");//TODO
	    // doesn't work like submit

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

    private Object renderRepeat(final Repeat ctrl,
	    final Hashtable<String, FormControl> assoc) {
	StringBuilder html = new StringBuilder();
	FormControl[] elems = ctrl.getChildren();
	boolean groupflag = false;
	html.append("<table border=\"1\" cellspacing=\"0\" ><thead>");
	if (elems == null || elems.length != 1) {
	    throw new IllegalArgumentException("Malformed argument!");
	}
	if (elems[0] instanceof Group) {
	    groupflag = true;
	    FormControl[] elems2 = ((Group) elems[0]).getChildren();
	    if (elems2 == null || elems2.length == 0) {
		throw new IllegalArgumentException("Malformed argument!");
	    }
	    for (int i = 0; i < elems2.length; i++) {
		if (elems2[i].getLabel() != null) {
		    html.append("<th>" + elems2[i].getLabel() + "</th>");
		} else {
		    html.append("<th>" + elems2[i].getType() + "</th>");
		}
	    }
	} else if (elems[0] == null) {
	    throw new IllegalArgumentException("Malformed argument!");
	}
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

    private String renderMediaObject(final MediaObject ctrl, final boolean mute) {
	Label cl = ctrl.getLabel();
	StringBuilder html = new StringBuilder();
	if (cl != null && !mute) {
	    if (cl.getText() != null) {
		html.append("<b>" + cl.getText() + " </b>");
	    }
	}
	String src = ctrl.getContentURL();
	String alt = ctrl.getHintString();
	int w = ctrl.getResolutionPreferredX();
	int h = ctrl.getResolutionPreferredY();
	if (src != null) {
	    html.append("<img src=\"" + src + "\"");
	    if (alt != null) {
		html.append(" alt=\"" + alt + "\"");
	    }
	    if (w > 0 && h > 0) {
		html.append(" width=\"" + w + "\" height=\"" + h + "\" ");
	    }
	    html.append(" />");
	}
	return html.toString();
    }

    private String renderTextArea(final TextArea ctrl,
	    final Hashtable<String, FormControl> assoc, final boolean mute) {
	Label cl = ctrl.getLabel();
	StringBuilder html = new StringBuilder();
	if (cl != null && !mute) {
	    if (cl.getText() != null) {
		html.append("<label><b>" + cl.getText() + " </b>");
	    }
	}
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
	if (cl != null && !mute) {
	    if (cl.getText() != null) {
		html.append("</label>");
	    }
	}
	assoc.put(ctrl.getURI(), ctrl);
	return html.toString();
    }

    private String renderRangeControl(final Range ctrl,
	    final Hashtable<String, FormControl> assoc, final boolean mute) {
	Label cl = ctrl.getLabel();
	StringBuilder html = new StringBuilder();
	if (cl != null && !mute) {
	    if (cl.getText() != null) {
		html.append("<label><b>" + cl.getText() + " </b>");
	    }
	}
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
	if (cl != null && !mute) {
	    if (cl.getText() != null) {
		html.append("</div></label>");
	    }
	}
	assoc.put(ctrl.getURI(), ctrl);
	return html.toString();
    }

    private String renderSpinnerControl(final Range ctrl,
	    final Hashtable<String, FormControl> assoc, final boolean mute) {
	Label cl = ctrl.getLabel();
	StringBuilder html = new StringBuilder();
	if (cl != null && !mute) {
	    if (cl.getText() != null) {
		html.append("<label><b>" + cl.getText() + " </b>");
	    }
	}
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
	if (cl != null && !mute) {
	    if (cl.getText() != null) {
		html.append("</label>");
	    }
	}
	assoc.put(ctrl.getURI(), ctrl);
	return html.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.universAAL.ri.servicegateway.GatewayPort#dataDir()
     */
    @Override
    public final String dataDir() {
	return "/webhandler";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.universAAL.ri.servicegateway.GatewayPort#url()
     */
    @Override
    public final String url() {
	return "/universAAL";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
     * , javax.servlet.http.HttpServletResponse)
     */
    public final void doPost(final HttpServletRequest req,
	    final HttpServletResponse resp) throws ServletException,
	    IOException {
	UIRequest uiReqst = null;
	WebIOSession ses;
	// BEGIN AUTHENTICATION BLOCK
	// Check if user is authorized
	if (!handleAuthorization(req, resp)) {
	    LogUtils
		    .logInfo(
			    mContext,
			    this.getClass(),
			    "doPost",
			    new Object[] { "Received unauthorized HTTP request. Now requesting credentials!" },
			    null);
	    return;
	}
	String[] userAndPass = getUserAndPass(req.getHeader("Authorization"));
	String userURI = userURIs.get(userAndPass[0]);
	// END AUTHENTICATION BLOCK, this block can be replaced by below
	// hardcoded line/user for e.g. testing purposes
	// String userURI = Constants.uAAL_MIDDLEWARE_LOCAL_ID_PREFIX
	// + "remoteUser";
	LogUtils.logInfo(mContext, this.getClass(), "doPost",
		new Object[] { "Received HTTP request from user: " + userURI },
		null);
	// Check if it is the first time
	if (!userSessions.containsKey(userURI)) {
	    // Start session with user and request main menu
	    LogUtils
		    .logInfo(
			    mContext,
			    this.getClass(),
			    "doPost",
			    new Object[] { "Starting interaction and session with user: "
				    + userURI }, null);
	    ses = new WebIOSession();
	    userSessions.put(userURI, ses);

	    // Caregiver is connected with modality web in dm initializator of
	    // UIPreferences so instead User class, class Caregiver is used
	    User loggedUser = new Caregiver(userURI);

	    myUIHandler.userLoggedIn(loggedUser, null); // requesting main menu

	    // add logged user to subscription parameters of the UI handler
	    // (only
	    // UIRequests targeting web+logged_user will be delivered to this
	    // handler)
	    userAuthenticated(loggedUser);

	    uiReqst = waitForOutput(userURI, false); // waiting for
	    // main menu

	    ses.setCurrentUIRequest(uiReqst);

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
	    }// end for loop

	    uiReqst = dialogFinished(selectedSubmit, userURI);
	    ses.setCurrentUIRequest(uiReqst);

	}

	// Output processing
	// Load the template
	InputStream pageReader = this.getClass().getClassLoader()
		.getResourceAsStream("webhandler/dojobase.html");
	BufferedReader reader = new BufferedReader(new InputStreamReader(
		pageReader));
	StringBuilder html = new StringBuilder();
	String line;
	// Build the html
	Hashtable<String, FormControl> assoc = new Hashtable<String, FormControl>();
	// This hashtable will replace the old one containing the past input

	Form f = null;
	if (uiReqst == null) {
	    f = getNoUICallerNotificationForm();
	} else {
	    f = uiReqst.getDialogForm();
	}

	while ((line = reader.readLine()) != null) {
	    if (line.contains("<!-- BackgroundColor -->")) {
		line = visualPreferencesHelper
			.determineBackgroundColor(uiReqst);
	    } else if (line.contains("<!-- Generic-font-family -->")) {
		line = visualPreferencesHelper.determineFontFamily(uiReqst);
	    } else if (line.contains("<!-- Font-color -->")) {
		line = visualPreferencesHelper.determineFontColor(uiReqst);
	    } else if (line.contains("<!-- Font-size -->")) {
		line = visualPreferencesHelper.determineFontSize(uiReqst);
	    } else if (line.contains("<!-- Page title -->")) {
		line = f.getTitle();
	    } else if (line.contains("<!-- Title -->")) {
		line = f.getTitle();
	    } else if (line.contains("<!-- Left -->")) {
		line = "";
	    } else if (line.contains("<!-- Center -->")) {
		if (f.getIOControls() != null) {
		    line = this.renderGroupControl(f.getIOControls(), assoc,
			    true);
		} else {
		    line = "";// "IO Controls";//Only for reference
		}
	    } else if (line.contains("<!-- Right -->")) {
		// render standard (system) buttons on top of other submits
		if (f.getStandardButtons() != null) {
		    line = this.renderGroupControl(f.getStandardButtons(),
			    assoc, true);
		    html.append(line);
		    html.append("<hr>");// add line between standard buttons and
		    // submits
		}

		if (f.getSubmits() != null) {
		    line = this.renderGroupControl(f.getSubmits(), assoc, true);
		} else {
		    line = "";// "Submits";//Only for reference
		}
		// add main menu request button on the right side above submits
		// html
		// .append("<button dojotype=\"dijit.form.Button\" type=\"submit\" name=\"submit_MainMenuScreen\" value=\"Main menu\" label=\"Main menu\" title=\"Main menu\"> <img src=\"/webhandler/img/home_icon_small.png\" /></button><br>");
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
	LogUtils.logInfo(mContext, this.getClass(), "doPost",
		new Object[] { "HTML response page rendered." }, null);

	// LogUtils.logDebug(mContext, this.getClass(), "doPost",
	// new Object[] { "HTML page: \n" + html}, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
     * , javax.servlet.http.HttpServletResponse)
     */
    public final void doGet(final HttpServletRequest req,
	    final HttpServletResponse resp) throws ServletException,
	    IOException {
	doPost(req, resp);
    }

    /**
     * Waits for output {@link UIRequest} that comes to be rendered (showed on
     * the screen to a user).
     * 
     * @param userUri
     *            user
     * @param first
     * @return {@link UIRequest}
     */
    public final UIRequest waitForOutput(final String userUri,
	    final Boolean first) {
	UIRequest o = null;
	synchronized (waitingInputs) {
	    waitingInputs.put(userUri, first);

	    while (o == null) {
		try {
		    LogUtils.logDebug(mContext, this.getClass(),
			    "waitForOutput",
			    new Object[] { "Waiting for UIRequest." }, null);
		    // wait only if readyOutputs is empty. There may be a case
		    // when some (quick) output was received in meantime
		    if (readyOutputs.isEmpty()) {
			waitingInputs.wait();
		    }
		    o = (UIRequest) readyOutputs.remove(userUri);

		} catch (InterruptedException e) {
		    LogUtils
			    .logError(
				    mContext,
				    this.getClass(),
				    "waitForOutput",
				    new Object[] { "Exception while waiting for UIRequest." },
				    e);
		}
	    }
	}
	LogUtils.logInfo(mContext, this.getClass(), "waitForOutput",
		new Object[] { "Got UIRequest of type: " + o.getDialogType()
			+ " carrying form with title: "
			+ o.getDialogForm().getTitle() }, null);
	return o;
    }

    /**
     * 
     * @param s
     *            submit
     * @param userURI
     *            user uri
     * @return output or more specifically {@link UIRequest}
     */
    public final UIRequest dialogFinished(final Submit s, final String userURI) {
	LogUtils.logInfo(mContext, this.getClass(), "dialogFinished",
		new Object[] { "Dialog finished. User: " + userURI
			+ " pressed a button :" + s.getLocalName() }, null);
	if (s == null) {
	    LogUtils
		    .logDebug(
			    mContext,
			    this.getClass(),
			    "dialogFinished",
			    new Object[] { "Selected submit is null - could not be obtained from current sesion-Form association. Returning current UIRequest." },
			    null);
	    return this.userSessions.get(userURI).getCurrentUIRequest();
	}

	Object o = s.getFormObject().getProperty(UIRequest.MY_URI);
	if (o instanceof UIRequest) {
	    // is UIRequest
	    // a popup action is being finished
	    UIResponse uiResp = new UIResponse(((UIRequest) o)
		    .getAddressedUser(), ((UIRequest) o)
		    .getPresentationLocation(), s);
	    myUIHandler.dialogFinished(uiResp);
	    return this.waitForOutput(((User) uiResp.getUser()).getURI(),
		    Boolean.FALSE);
	} else {
	    // else is UIResponse
	    synchronized (myUIHandler) {
		UIResponse uiResponse = new UIResponse(
			((WebIOSession) this.userSessions.get(userURI))
				.getCurrentUIRequest().getAddressedUser(),
			((WebIOSession) this.userSessions.get(userURI))
				.getCurrentUIRequest()
				.getPresentationLocation(), s);
		if (s.getDialogID().equals(myUIHandler.currentDialogID)) {
		    ((WebIOSession) this.userSessions.get(userURI))
			    .setCurrentUIRequest(null);
		}
		myUIHandler.dialogFinished(uiResponse);
		return this.waitForOutput(((User) uiResponse.getUser())
			.getURI(), Boolean.FALSE);
	    }
	}
    }

    @Override
    public final Hashtable<String, UIRequest> getReadyOutputs() {
	return this.readyOutputs;
    }

    @Override
    public final Hashtable<String, WebIOSession> getUserSessions() {
	return this.userSessions;
    }

    @Override
    public final Hashtable<String, Boolean> getWaitingInputs() {
	return this.waitingInputs;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.universAAL.ui.handler.web.IWebRenderer#getRendererName()
     */
    public final String getRendererName() {
	return RENDERER_NAME;
    }

    public void dispose() {
	myUIHandler.close();
	mContext = null;
    }

    /**
     * 
     * @return notification that no Form is given by DM or the application for
     *         this handler to render
     */
    private final Form getNoUICallerNotificationForm() {
	Form f = Form.newDialog("No UI Provider ", (String) null);

	new SimpleOutput(f.getIOControls(), null, null,
		"There is no application offering remote access in the current configuration!");
	return f;
    }
}
