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
package org.universAAL.kinect.adapter.IMessageBroker;

import java.util.Collection;

/**
 * This is the interface of the MessageBrokers, which interpret and forward
 * messages to specific objects, and send back any return values.
 * 
 * 
 */
public interface IMessageBroker {
	/**
	 * This method sends a new message to the broker.
	 * 
	 * @param type
	 *            Type of the message, for example ServiceCall or ContextPublish
	 * @param key
	 *            Key to identify the concrete object
	 * @param args
	 *            Arguments forwarded to universAAl
	 * @return Collection of any return values that may occur
	 * @throws AdapterException
	 */
	Collection<?> SendNewMessage(Object type, Object key, Collection<?> args) throws AdapterException;

}
