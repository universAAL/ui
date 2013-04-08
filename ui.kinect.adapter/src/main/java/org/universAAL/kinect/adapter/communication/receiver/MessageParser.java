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
package org.universAAL.kinect.adapter.communication.receiver;

import java.util.Collection;
import java.util.LinkedList;

import org.universAAL.kinect.adapter.IMessageBroker.AdapterException;
import org.universAAL.kinect.adapter.IMessageBroker.IMessageBroker;

/**
 * This class parses the messages received from the network, and forwards them
 * to a message broker.
 * 
 */
public class MessageParser {

    /**
     * Separator string for parsing the input.
     */
    String separator = "<<>>";

    /**
     * broker where the messages are forwarded
     */
    IMessageBroker broker;

    /**
     * Constructor
     * 
     * @param broker
     *            where the parsed messages are forwarded.
     */
    public MessageParser(final IMessageBroker broker) {
	super();
	this.broker = broker;
    }

    /**
     * This method parses the input string. Then calls the broker and return
     * with the return value from the worker. Message structure:
     * "type<<>>>message<<>>args" example
     * message:"1<<>>msg2<<>>[http://ontology.igd
     * .fhg.de/LightingConsumer.owl#controlledLamps, XXX, YASDAS, XXXYYY, 1, 2,
     * 3]"
     * 
     * @param o
     *            Object to be parsed
     * @return
     * @throws AdapterException
     */
    public final Object parse(final Object o) throws AdapterException {
	String[] message = ((String) o).split(separator);
	Collection<?> ret = null;
	// removing square bracket from args part.
	String square_free_args = message[2].substring(1,
		message[2].length() - 1);
	String[] splitted_square_free_args = square_free_args.split(",");// splitting
	// by the commas
	Collection<String> parsedargs = new LinkedList<String>();
	// adding 0.th element with no change, because there is no space to
	// remove
	parsedargs.add(splitted_square_free_args[0]);
	// removing the spaces after the commas, for example " xxx" --> "xxx"
	for (int i = 1; i < splitted_square_free_args.length; i++) {
	    parsedargs.add(splitted_square_free_args[i].substring(1));
	}
	ret = broker.SendNewMessage(message[0], message[1], parsedargs);
	return ret;
    }
}
