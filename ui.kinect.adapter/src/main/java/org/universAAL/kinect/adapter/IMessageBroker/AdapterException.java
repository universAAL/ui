/*******************************************************************************
 * Copyright 2013 Ericsson Nikola Tesla d.d.
 *
 * Licensed under both Apache License, Version 2.0 and MIT License.
 *
 * See the NOTICE file distributed with this work for additional 
 * information regarding copyright ownership
 *	
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.universAAL.kinect.adapter.IMessageBroker;

/**
 * This class stand for any exception that could occur in the adapter.
 * 
 * 
 */
public class AdapterException extends Exception {

    private static final long serialVersionUID = 1L;

    public AdapterException() {
	super();
	// TODO Auto-generated constructor stub
    }

    public AdapterException(final String message, final Throwable cause) {
	super(message, cause);
	// TODO Auto-generated constructor stub
    }

    public AdapterException(final String message) {
	super(message);
	// TODO Auto-generated constructor stub
    }

    public AdapterException(final Throwable cause) {
	super(cause);
	// TODO Auto-generated constructor stub
    }

}
