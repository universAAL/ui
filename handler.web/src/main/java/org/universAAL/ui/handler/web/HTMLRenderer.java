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
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;
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
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.middleware.ui.UIHandlerProfile;
import org.universAAL.middleware.util.Constants;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.ontology.location.Location;
import org.universAAL.ontology.profile.User;
import org.universAAL.ri.servicegateway.GatewayPort;

/**
 * @author <a href="mailto:alfiva@itaca.upv.es">Alvaro Fides Valero</a>
 * 
 */
public class HTMLRenderer extends GatewayPort implements IWebRenderer {

    private static final long serialVersionUID = -4986118000986648808L;
//    public static final String UNIVERSAAL_ASSOCIATED_LABEL = "urn:org.universAAL.dialog:AssociatedLabel";
//    public static final String UNIVERSAAL_CLOCK_THREAD = "urn:org.universAAL.dialog:TheClockThread";
//    public static final String UNIVERSAAL_FORM_CONTROL = "urn:org.universAAL.dialog:FormControl";
//    public static final String UNIVERSAAL_PANEL_COLUMNS = "urn:org.universAAL.dialog:PanelColumns";
    public static final String RENDERER_NAME = "universAAL-Web-HTML-UIHandler";
   
 
    private MyUIHandler myUIHandler;

    private Hashtable<String, Boolean> waitingInputs;
    private Hashtable<String, UIRequest> readyOutputs; //userUri, UIRequest
    private Hashtable<String, WebIOSession> userSessions; //user, web session

    
    private ModuleContext mContext; 

    public HTMLRenderer(ModuleContext mcontext) {
	super();
	mContext=mcontext;
	waitingInputs = new Hashtable<String, Boolean>();
	readyOutputs = new Hashtable<String, UIRequest>();
	userSessions = new Hashtable<String, WebIOSession>();
	myUIHandler = new MyUIHandler(mcontext, getOutputSubscriptionParams(), this);

    }

    private UIHandlerProfile getOutputSubscriptionParams() {
	// I am interested in all events with following UIHandlerProfile
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

    public void finish(String userURI) {
	this.userSessions.remove(userURI);
	this.userURIs.remove(userURI);
	LogUtils.logInfo(mContext, this.getClass(), "finish",
		new Object[] { "Finished user session for userURI:" +userURI}, null);
    }

    void popMessage(Form f) {
	// TODO popup

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.universAAL.ui.handler.web.IWebRenderer#updateScreenResolution(int,
     * int, int, int)
     */
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
    /*
     * (non-Javadoc)
     * 
     * @see org.universAAL.ri.servicegateway.GatewayPort#dataDir()
     */
    @Override
    public String dataDir() {
	return "/webhandler";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.universAAL.ri.servicegateway.GatewayPort#url()
     */
    @Override
    public String url() {
	return "/universAAL";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
     * , javax.servlet.http.HttpServletResponse)
     */
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {

    UIRequest o;
	WebIOSession ses = new WebIOSession();
	LogUtils.logInfo(mContext, this.getClass(), "doPost",
		new Object[] { "received HTTP Servlet Request " +req }, null);
	// BEGIN AUTHENTICATION BLOCK
	// Check if user is authorized
	if (!handleAuthorization(req, resp)) {
	    LogUtils.logInfo(mContext, this.getClass(), "doPost",
			new Object[] { "Received unauthorized HTTP request!"}, null);
	    
	    return;
	}
	String[] userAndPass = getUserAndPass(req.getHeader("Authorization"));
	String userURI = userURIs.get(userAndPass[0]);
	// END AUTHENTICATION BLOCK, this block can be replaced by below
	// hardcoded line/user for e.g. testing purposes
	// String userURI = Constants.uAAL_MIDDLEWARE_LOCAL_ID_PREFIX
	// + "remoteUser";
	LogUtils.logInfo(mContext, this.getClass(), "doPost",
		new Object[] { "Received HTTP request from user: "+ userURI}, null);
	// Check if it is the first time
	if (!userSessions.containsKey(userURI)) {
	    // Start and request main menu
	    LogUtils.logInfo(mContext, this.getClass(), "doPost",
			new Object[] { "Starting interaction and session with user: "+ userURI}, null);
	    
	    userSessions.put(userURI, ses);

	    // FIXME was in version with IO bus:
	    // event = new InputEvent(new User(userURI), null,
	    // InputEvent.uAAL_MAIN_MENU_REQUEST);
	    // o = publish(event, Boolean.TRUE);

	    //added instead above 2 rows when movin to UI bus
	    //important that following 2 rows are not switched
	    myUIHandler.userLoggedIn(new User(userURI), null);
	    o = (UIRequest) readyOutputs.remove(userURI);
	    
	    ses.setCurrentUIRequest(o);
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
	    // build and send the UIResponse
	    o = dialogFinished(selectedSubmit, userURI);
	    ses.setCurrentUIRequest(o);
	}

	// Output processing
	// Load the template
	InputStream pageReader = this.getClass().getClassLoader()
		.getResourceAsStream("webhandler/htmlbase.html");
	BufferedReader reader = new BufferedReader(new InputStreamReader(
		pageReader));
	StringBuilder html = new StringBuilder();
	String line;
	// Build the html
	Hashtable<String, FormControl> assoc = new Hashtable<String, FormControl>();
	// This hashtable will replace the old one containing the past input

	Form f = null;
	if (o == null)
	    f = getNoUICallerNotificationForm();
	else
	    f = o.getDialogForm();

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
	LogUtils.logInfo(mContext, this.getClass(), "doPost",
		new Object[] { "HTML response page rendered."}, null);
    
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
     * , javax.servlet.http.HttpServletResponse)
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {
	doPost(req, resp);
    }

    public final UIRequest publish(UIResponse event, Boolean first) {
	UIRequest o = null;
	synchronized (waitingInputs) {
	    String user = ((User) event.getUser()).getURI();
	    waitingInputs.put(user, first);
	    LogUtils.logInfo(mContext, this.getClass(), "publish",
			new Object[] { "Making UIRequest for user: "+ user}, null);
	    

	    while (o == null) {
		try {
		    LogUtils.logInfo(mContext, this.getClass(), "publish",
				new Object[] { "Waiting for Outputs.."}, null);
		    waitingInputs.wait();
		    o = (UIRequest) readyOutputs.remove(user);
		    LogUtils.logInfo(mContext, this.getClass(), "publish",
				new Object[] { "Got outputs."}, null);
		} catch (InterruptedException e) {
		    LogUtils.logError(mContext, this.getClass(), "publish",
				new Object[] { "Exception while waiting for Outputs"}, e);
		    	    
		}
	    }
	}
	return o;
    }

    public UIRequest dialogFinished(Submit s, String userURI) {
	LogUtils.logInfo(mContext, this.getClass(), "dialogFinished",
		new Object[] { "User "+ userURI + " pressed a button."}, null);
	// for the next line, see the comment within handleUIRequest() above
	// [preserved from SwingRenderer]
	Object o = s.getFormObject().getProperty(UIRequest.MY_URI);
	if (o instanceof UIRequest) {
	    // a popup action is being finished
	    UIResponse uiResp = new UIResponse(((UIRequest) o)
		    .getAddressedUser(), ((UIRequest) o)
		    .getPresentationLocation(), s);
	    myUIHandler.dialogFinished(uiResp);
	    return this.publish(uiResp, Boolean.FALSE);
	} else {
	    synchronized (myUIHandler) {
		UIResponse ie = new UIResponse(
			((WebIOSession) this.userSessions.get(userURI))
				.getCurrentUIRequest().getAddressedUser(),
			((WebIOSession) this.userSessions.get(userURI))
				.getCurrentUIRequest()
				.getPresentationLocation(), s);
		if (s.getDialogID().equals(myUIHandler.dialogID))
		    ((WebIOSession) this.userSessions.get(userURI))
			    .setCurrentUIRequest(null);
		myUIHandler.dialogFinished(ie);
		return this.publish(ie, Boolean.FALSE);
	    }
	}
    }

    @Override
    public Hashtable<String, UIRequest> getReadyOutputs() {
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
    
    /* (non-Javadoc)
     * @see org.universAAL.ui.handler.web.IWebRenderer#getRendererName()
     */
    public String getRendererName() {
        return RENDERER_NAME;
    }

    /**
     * 
     * @return notification that no Form is given by the application for this
     *         handler to render
     */
    Form getNoUICallerNotificationForm() {
	Form f = Form.newDialog("No UI Provider", (String) null);

	new SimpleOutput(f.getIOControls(), null, null,
		"There is no application offering remote access in the current configuration!");
	return f;
    }
}
