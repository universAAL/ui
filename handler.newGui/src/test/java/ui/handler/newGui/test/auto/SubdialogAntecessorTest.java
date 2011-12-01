/*******************************************************************************
 * Copyright 2011 Universidad Politécnica de Madrid
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
package ui.handler.newGui.test.auto;

import org.universAAL.middleware.io.rdf.Form;
import org.universAAL.ui.handler.newGui.defaultLookAndFeel.FormLAF;
import org.universAAL.ui.handler.newGui.model.FormModelMapper;

import junit.framework.TestCase;

/**
 * Test the framework for locating form antecessors.
 * @author amedrano
 *
 */
public class SubdialogAntecessorTest extends TestCase {

	Form root, d1, d2, d11, d12, d111;
	
	public void setUp() {
		root = Form.newDialog("root", (String) null);
		d1 = Form.newSubdialog("Dialog 1", root.getDialogID());
		d2 = Form.newSubdialog("Dialog 2", root.getDialogID());
		d11 = Form.newSubdialog("Dialog 1.1", d1.getDialogID());
		d12 = Form.newSubdialog("Dialog 1.2", d1.getDialogID());
		d111 = Form.newSubdialog("Dialog 1.1.1", d11.getDialogID());
		
		new FormLAF(root);
		new FormLAF(d1);
		new FormLAF(d11);
		new FormLAF(d12);
		new FormLAF(d111);
		new FormLAF(d1);;
	}
	
	public void testAnt1() {
		assertTrue(new FormLAF(d1).isAntecessor(root.getDialogID()));
	}	
	public void testAnt2() {
		assertTrue(new FormLAF(d11).isAntecessor(root.getDialogID()));
	}
	public void testAnt3() {
		assertTrue(new FormLAF(d111).isAntecessor(root.getDialogID()));
	}
	public void testAnt4() {
		assertTrue(new FormLAF(d111).isAntecessor(d11.getDialogID()));
	}
	public void testAnt5() {
		assertTrue(new FormLAF(d111).isAntecessor(d1.getDialogID()));
	}
	public void testAnt6() {
		assertFalse(new FormLAF(d1).isAntecessor(d2.getDialogID()));
	}
	public void testAnt7() {
		assertFalse(new FormLAF(d111).isAntecessor(d12.getDialogID()));
	}
	public void testFMM1() {
		new FormLAF(d111).finalizeForm();
		assertFalse(FormModelMapper.isRegistered(d111.getDialogID()));
	}
	
	public void tearDown() {
		FormModelMapper.flush();
	}
}
