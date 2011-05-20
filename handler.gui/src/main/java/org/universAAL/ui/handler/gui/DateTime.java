/*
	Copyright 2008-2010 SPIRIT, http://www.spirit-intl.com/
	SPIRIT S.A. E-BUSINESS AND COMMUNICATIONS ENGINEERING 
	
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
package org.universAAL.ui.handler.gui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
/**
 * Keeping time, and displaying it on the screen
 * @author phamisu
 *
 */
public class DateTime {

	/**
	 * A thread for the clock.
	 */
	class DateTimeThread extends Thread {
		private int refreshRate = 5000; // in milliseconds
		private boolean stop = false;

		/**
		 * Work for the thread: wait and then update 
		 * @see DateTime#updateTime()
		 */
		public void run() {
			while (!stop) {
				updateTime();
				try {
					Thread.sleep(refreshRate);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			}
		}
		
		public void interrupt() {
			stop = true;
		}
	}

	JComponent _comp = null;

	private GregorianCalendar calendar;
	private SimpleDateFormat dateFormat;
	
	/**
	 * Constructor.
	 * @param comp
	 */
	public DateTime(JComponent comp) {
		super();
		_comp = comp;
		createCalendar();
	}

	/**
	 * Create the GregorianCalendar that keeps track of the time.
	 * 
	 * TODO: add option to set different date formats.
	 */
	private void createCalendar() {
		calendar = new GregorianCalendar();
		dateFormat = new SimpleDateFormat("d MMM yyyy",
				Locale.US);
		dateFormat.setCalendar(calendar);
	}

	/**
	 * Sets the time label.
	 *
	 * @param timeLabel 
	 * 			label into which set the time
	 * @param time 
	 * 			time the time to set
	 */
	private void setTimeLabel(final JButton timeLabel, final String time) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				timeLabel.setText(time);
			}
		});
	};

	/**
	 * Start running the clock.
	 */
	public Thread runTheClock() {
		DateTimeThread time = new DateTimeThread();
		time.start();
		return time;
	}

	/**
	 * Updates the calendar and the display with the current time.
	 */
	private void updateTime() {
		Date currentTime = new Date();
		calendar.setTime(currentTime);
		String c_time = dateFormat.format(currentTime);
		String[]values = c_time.split(" ");
		String date="<html><table><tr><td align=center valign=middle> <font face=Myriad Pro size=7 color=#565656>"+ values[0]+ "</font>" +
		"</td><td><font face=Myriad Pro size=4 color=#565656>" + values[1]+ "</font><br><font face=Myriad Pro size=3 color=#00a77f>" +
		""+ values[2]+ "</font></td><tr><td><font face=Myriad Pro size=4 color=#00a77f>Sound</font></td><td><font face=Myriad Pro size=4 color=#565656>Off</font></td></tr></table>";

		setTimeLabel((JButton) _comp, date);
	}

}
