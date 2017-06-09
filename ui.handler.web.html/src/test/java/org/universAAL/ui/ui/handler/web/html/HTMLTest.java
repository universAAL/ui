package org.universAAL.ui.ui.handler.web.html;

import java.util.Properties;

import junit.framework.TestCase;

import org.universAAL.ui.ui.handler.web.html.model.Model;

public class HTMLTest extends TestCase {

	public void testSimpleHTML() {
		StringBuffer html = Model.tag("html", Model.tag("head", Model.tag("title", "Testing Page", null), null)
				.append(Model.tag("body", Model.tag("h1", "Hello World!", null), null)), null);
		assertEquals("<HTML><HEAD><TITLE>Testing Page</TITLE></HEAD><BODY><H1>Hello World!</H1></BODY></HTML>",
				html.toString());
	}

	public void testProperties() {

		Properties p = new Properties();
		p.put("type", "text");
		p.put("name", "email");
		p.put("size", "40");
		p.put("maxlength", "50");

		StringBuffer html = Model.tag("input", "giveme Input", p);

		assertEquals("<INPUT name=\"email\" maxlength=\"50\" size=\"40\" type=\"text\">giveme Input</INPUT>",
				html.toString());

	}
}
