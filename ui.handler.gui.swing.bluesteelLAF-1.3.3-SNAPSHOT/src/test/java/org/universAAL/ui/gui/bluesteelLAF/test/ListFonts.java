/*******************************************************************************
 * Copyright 2013 2011 Universidad Polit�cnica de Madrid
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
package org.universAAL.ui.gui.bluesteelLAF.test;

import java.awt.GraphicsEnvironment;

/**
 * @author amedrano
 *
 */
public class ListFonts {

	/**
	 * @param args
	 */
	public static void main(String[] args) {String fonts[] = 
		      GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

		    for ( int i = 0; i < fonts.length; i++ )
		    {
		      System.out.println(fonts[i]);
		    }
		  }

	}


