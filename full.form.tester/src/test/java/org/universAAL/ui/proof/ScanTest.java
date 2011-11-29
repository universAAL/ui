package org.universAAL.ui.proof;

import java.io.IOException;

import org.universAAL.ui.full.test.ISubscriber;

public class ScanTest {

	public static void main(String[] args) {
		try {
			String[] a = ISubscriber.getClassNamesFromPackage("org.universAAL.ui.full.test.forms");
			for (int i=0; i< a.length; i++)
				System.out.println(a[i]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
