package org.universAAL.ui.ui.handler.web.html;

import java.util.Properties;

import junit.framework.TestCase;

import org.universAAL.ui.ui.handler.web.html.model.Model;

public class HTMLTest 
    extends TestCase
{


    public void testSimpleHTML()
    {
        StringBuffer html = Model.tag("html",
        		Model.tag("head",
        				Model.tag("title", "Testing Page", null)
        				, null)
        				.append(
        				Model.tag("body", 
        						Model.tag("h1", "Hello World!", null), null)
        		),null);
        assertEquals("<html><head><title>Testing Page</title></head><body><h1>Hello World!</h1></body></html>",
        		html.toString());
    }
    
    public void testProperties(){
    	
    	Properties p = new Properties();
    	p.put("type", "text");
    	p.put("name", "email");
    	p.put("size", "40");
    	p.put("maxlength", "50");
    	
    	StringBuffer html = Model.tag("input", "giveme Input", p);
    	
    	assertEquals("<input name=\"email\" maxlength=\"50\" size=\"40\" type=\"text\">giveme Input</input>",
    			html.toString());
    	
    }
}
