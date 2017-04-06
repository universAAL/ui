/*******************************************************************************
 * Copyright 2012 Ericsson Nikola Tesla d.d.
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
package org.universAAL.ui.resource.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.util.BundleConfigHome;
import org.universAAL.middleware.container.utils.LogUtils;

/**
 * Resource Server receives GET and POST requests and exposes/serves requested
 * resources from the common resources storage.
 * 
 * One of the usage scenarios of this artefact is following: UI Handlers that
 * are not always on the same node can, by using this server, obtain common
 * (also application specific) resources. Since universAAL middleware does not
 * have the capability to transfer binary files UI Handlers can obtain icons and
 * other media types and connect them with the abstract UI representations
 * comming from the applications. This way application developers can store
 * specific resources into the common folder and then simply add their names to
 * the abstract UI representations. The job of the UI Handlers is to merge this
 * resources with the rendered user interfaces.
 * 
 * @author <a href="mailto:andrej.grguric@ericsson.com">Andrej Grguric</a>
 * 
 */
public class ResourceServer extends HttpServlet {

    private static final long serialVersionUID = -2375186843748499339L;
    private ModuleContext moduleContext;

    public static File confHome = Activator.mcontext.getConfigHome();

    // private Hashtable<String, String> allowedCredentials = new
    // Hashtable<String, String>();

    /**
     * Path to resources; e.g. ...runner\configurations\ResourceServer
     */
    public static String PATH_TO_RESOURCES = confHome.toString();

    public ResourceServer(ModuleContext mcontext) {
	moduleContext = mcontext;
	// allowedCredentials.put("handler", "password");
    }

    // control the mime type headers that are returned with the stream for a
    // particular resource.
    // see possible MIME types here:
    // http://www.iana.org/assignments/media-types/index.html
    /**
     * Determine mime type of the resource. If resource type is not recognized
     * (between gif, png, jpg, jpeg, xml, au, aa3, aac, aif, al, mp4, mpeg) null
     * is returned.
     * 
     * @param name
     *            resoruce name
     * @return mime type
     */
    public String getMimeType(String name) {
	if (name.endsWith(".jpg") || name.endsWith(".jpeg")) {
	    return "image/jpeg";
	} else if (name.endsWith(".gif")) {
	    return "image/gif";
	} else if (name.endsWith(".png")) {
	    return "image/png";
	} else if (name.endsWith(".xml")) {
	    return "text/xml";
	} else if (name.endsWith(".au")) {
	    return "audio/au";
	} else if (name.endsWith(".aa3")) {
	    return "audio/aa3";
	} else if (name.endsWith(".aac")) {
	    return "audio/aac";
	} else if (name.endsWith(".aif")) {
	    return "audio/aif";
	} else if (name.endsWith(".al")) {
	    return "audio/al";
	} else if (name.endsWith(".mp4")) {
	    return "video/mp4";
	} else if (name.endsWith(".mpeg")) {
	    return "video/mpeg";
	} else {
	    return null;
	}
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {
	doPost(req, resp);
    }

    /**
     * Checks if the resource Requester is authorized to access resource
     * 
     * 
     * @param req
     *            The request
     * @param resp
     *            The response
     */
    public boolean isAuthorized(HttpServletRequest req, HttpServletResponse resp) {
	// Get Authorization header
	// String auth = req.getHeader("Authorization");
	// if (auth == null) return false; // no auth

	return true;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {

	String reqURI = req.getRequestURI();

	// if nothing is passed for URI
	if (reqURI.length() == Activator.URI.length()) {
	    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

	    LogUtils.logInfo(moduleContext, this.getClass(), "doGet/Post",
		    new Object[] { "Request URI is null." }, null);
	    return;
	}

	// e.g. if reqURI is "/resources/button.png" then we need to remove
	// "/resources/" to get "button.png"
	String requestedResource = reqURI.substring(Activator.URI.length() + 1);

	LogUtils.logInfo(moduleContext, this.getClass(), "doGet/Post",
		new Object[] { "Request for a following resource received: ",
			requestedResource }, null);

	// check authorization
	if (!isAuthorized(req, resp)) {
	    LogUtils
		    .logInfo(
			    moduleContext,
			    this.getClass(),
			    "doGet/Post",
			    new Object[] { "Received unauthorized request for Resource." },
			    null);
	    resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	    return;
	}

	String mimeType = getMimeType(requestedResource);

	if (mimeType == null) {
	    LogUtils.logInfo(moduleContext, this.getClass(), "doGet/Post",
		    new Object[] { "Could not get MIME type of: ",
			    requestedResource }, null);
	    mimeType = "unrecognized";
	}

	// Set content type
	resp.setContentType(mimeType);

	// Set content status
	resp.setStatus(HttpServletResponse.SC_OK);

	String pathToRequestedResource = null;

	// if name of the requested resource does not start with "/", add it
	if (!requestedResource.startsWith(System.getProperty("file.separator"))) {
	    pathToRequestedResource = PATH_TO_RESOURCES
		    + System.getProperty("file.separator") + requestedResource;
	}

	// Obtain resource
	File file = new File(pathToRequestedResource);

	// if requested file does not exist return error
	if (!file.exists()) {
	    LogUtils.logError(moduleContext, this.getClass(), "doGet/Post",
		    new Object[] { "Requested file not found: ",
			    pathToRequestedResource }, null);

	    // set not FOUND status and delete content type
	    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
	    resp.setContentType(null);
	    return;
	}
	// Set content size
	resp.setContentLength((int) file.length());

	// Open the file
	FileInputStream in = new FileInputStream(file);

	// open output stream
	OutputStream out = resp.getOutputStream();

	// Copy the contents of the file to the output stream
	byte[] buf = new byte[1024];
	int count = 0;
	while ((count = in.read(buf)) >= 0) {
	    out.write(buf, 0, count);
	}

	LogUtils.logInfo(moduleContext, this.getClass(), "doGet/Post",
		new Object[] { "Following resource sent: ",
			pathToRequestedResource }, null);
	in.close();
	out.close();

    }

}
