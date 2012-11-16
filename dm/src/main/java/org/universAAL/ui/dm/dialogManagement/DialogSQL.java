/*******************************************************************************
 * Copyright 2012 Universidad Politécnica de Madrid
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
package org.universAAL.ui.dm.dialogManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.universAAL.middleware.container.utils.LogUtils;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.sodapop.msg.MessageContentSerializer;
import org.universAAL.middleware.ui.UIRequest;
import org.universAAL.ui.dm.DialogManagerImpl;
import org.universAAL.ui.dm.interfaces.SQLStoreProvider;
import org.universAAL.ui.dm.interfaces.UIRequestPool;

/**
 * A {@link UIRequestPool} that stores (and retieves) the dialogs from a 
 * DataBase, making dialogs managed with this pool automatically persistent.
 * 
 * @author amedrano
 *
 */
public class DialogSQL implements UIRequestPool {

    private static final String DIALOGS_TABLE = "Dialogs";
    private String userURI;
    private static MessageContentSerializer contentSerializer = null;
    private String connectionURL = null;
	private UIRequest currentReq;
    
    public static void InitDB(String connectionURL) throws Exception {
	    Connection connection = DriverManager.getConnection(connectionURL);
	    Statement statement = connection.createStatement();
	    statement.setQueryTimeout(30);  // set timeout to 30 sec.

//	    statement.executeUpdate("create table if not exists User ("+
//		    "id integer primary key AUTOINCREMENT," +
//		    "uri string not NULL,"+
//		    ")");
	    statement.executeUpdate("create table if not exists" + DIALOGS_TABLE + "("+
		    "user string,"+
		    "uri string primary key,"+
		    "date integer,"+
		    "active integer," +
		    "content blob"
//		    + ",foreing key (user) references User(id))"
		    );
	    connection.close();
    }
    
    private MessageContentSerializer getSerializer() throws Exception{
	if (contentSerializer == null){
	    contentSerializer = (MessageContentSerializer) DialogManagerImpl.getModuleContext()
		    .getContainer().fetchSharedObject(DialogManagerImpl.getModuleContext(),
			    new Object[] { MessageContentSerializer.class.getName() });
	    if (contentSerializer == null) {
		Exception e = new RuntimeException("no serializer found");
		LogUtils.logError(DialogManagerImpl.getModuleContext(),
				getClass(),
				"getSerializer",
				new String[] {"no serializer found"}, e);
		throw e;
	    }
	}
	return contentSerializer;
    }
        
    public  DialogSQL(Resource user, SQLStoreProvider storeprov) {
	userURI = user.getURI();
	if (storeprov == null) {
		return;
	}
	connectionURL = storeprov.getJCDBURL();
	if (connectionURL != null) {
	try {
		InitDB(connectionURL);
	} catch (Exception e) {
		LogUtils.logError(
				DialogManagerImpl.getModuleContext(), getClass(),
				"Constructor", new String[] {"unable to initialize DB"}, e);
	}
    }
    }
    
    /** {@inheritDoc} */
    public void add(UIRequest UIReq) {
    	Connection c = null;
	try {
	    c = DriverManager.getConnection(connectionURL);
	    c.createStatement().executeUpdate("INSERT INTO" + DIALOGS_TABLE 
		    + "VALUES ('" +
		    userURI + "','" +
		    UIReq.getDialogID() + "'," +
		    System.currentTimeMillis() + "," +
		    "1,'" +
		    getSerializer().serialize(UIReq) +
		    "');" );
	    c.close();
	} catch (Exception e) {
		LogUtils.logError(
				DialogManagerImpl.getModuleContext(), getClass(),
				"add", 
				new String[] {"unable to perform job, probably there is no connection with DB engine."},
				e);
	    try {
			c.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
    }

    /** {@inheritDoc} */
    public void close(String UIReqID) {
	// remove entry
    	Connection c = null;
	try {
	    c = DriverManager.getConnection(connectionURL);
	    c.createStatement().executeUpdate("DELETE FROM" + DIALOGS_TABLE 
		    + "WHERE uri='" + UIReqID +
		    "');" );
	    c.close();
	} catch (Exception e) {
		LogUtils.logError(
				DialogManagerImpl.getModuleContext(), getClass(),
				"add", 
				new String[] {"unable to perform job, probably there is no connection with DB engine."},
				e);
	    try {
			c.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
    }

    /** {@inheritDoc} */
    public UIRequest getCurrent() {
	return currentReq;
    }

    /** {@inheritDoc} */
    public UIRequest getNextUIRequest() {
	List<UIRequest> active = (List<UIRequest>) listAllActive();
	if (active.size() > 0) {
		currentReq = active.get(0);
	} else {
		currentReq =null;
	}
	return currentReq;
    }

    /** {@inheritDoc} */
    public boolean hasToChange() {
    	List<UIRequest> active = (List<UIRequest>) listAllActive();
    	if (active.size() > 0) {
    		return currentReq != active.get(0);
    	} else {
    		//shouldn't reach here...
    		return true;
    	}
    }

    /** {@inheritDoc} */
    public Collection<UIRequest> listAllActive() {
	// search
    	Connection c = null;
	try {
	    c = DriverManager.getConnection(connectionURL);
	    ResultSet r = c.createStatement().executeQuery("SELECT content FROM" + DIALOGS_TABLE 
		    + "WHERE userURI='" + userURI + 
		    "' AND active=1" +
		    ");" );
	    ArrayList<UIRequest> result = new ArrayList<UIRequest>();
	    while (r.next()) {
		UIRequest req = (UIRequest) getSerializer()
			.deserialize(r.getBlob("content").toString());
		result.add(req);
	    }
	    c.close();
	} catch (Exception e) {
		LogUtils.logError(
				DialogManagerImpl.getModuleContext(), getClass(),
				"add", 
				new String[] {"unable to perform job, probably there is no connection with DB engine."},
				e);
	    try {
			c.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	return null;
    }

    /** {@inheritDoc} */
    public Collection<UIRequest> listAllSuspended() {
	// search
    	Connection c = null;
	try {
	    c = DriverManager.getConnection(connectionURL);
	    ResultSet r = c.createStatement().executeQuery("SELECT content FROM" + DIALOGS_TABLE 
		    + "WHERE userURI='" + userURI + 
		    "' AND active=0" +
		    ");" );
	    ArrayList<UIRequest> result = new ArrayList<UIRequest>();
	    while (r.next()) {
		UIRequest req = (UIRequest) getSerializer()
			.deserialize(r.getBlob("content").toString());
		result.add(req);
	    }
	    c.close();
	} catch (Exception e) {
		LogUtils.logError(
				DialogManagerImpl.getModuleContext(), getClass(),
				"add", 
				new String[] {"unable to perform job, probably there is no connection with DB engine."},
				e);
	    try {
			c.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	return null;
    }

    /** {@inheritDoc} */
    public void removeAll() {
	// remove all corresponding to the userId
    	Connection c = null;
	try {
	    c = DriverManager.getConnection(connectionURL);
	    c.createStatement().executeUpdate("DELETE FROM" + DIALOGS_TABLE 
		    + "WHERE user='" + userURI +
		    "');" );
	    c.close();
	} catch (Exception e) {
		LogUtils.logError(
				DialogManagerImpl.getModuleContext(), getClass(),
				"add", 
				new String[] {"unable to perform job, probably there is no connection with DB engine."},
				e);
	    try {
			c.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

    }

    /** {@inheritDoc} */
    public void suspend(String UIReqID) {
	// update status
    	Connection c = null;
	try {
	    c = DriverManager.getConnection(connectionURL);
	    c.createStatement().executeUpdate("UPDATE " + DIALOGS_TABLE 
		    + "SET active=0"
		    + " WHERE uri='"+ UIReqID +
		    "');" );
	    c.close();
	} catch (Exception e) {
		LogUtils.logError(
				DialogManagerImpl.getModuleContext(), getClass(),
				"add", 
				new String[] {"unable to perform job, probably there is no connection with DB engine."},
				e);
	    try {
			c.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
    }

    /** {@inheritDoc} */
    public void unsuspend(String UIReqID) {
	// update status
    	Connection c = null;
	try {
	    c = DriverManager.getConnection(connectionURL);
	    c.createStatement().executeUpdate("UPDATE " + DIALOGS_TABLE 
		    + "SET active=1"
		    + " WHERE uri='"+ UIReqID +
		    "');" );
	    c.close();
	} catch (Exception e) {
		LogUtils.logError(
				DialogManagerImpl.getModuleContext(), getClass(),
				"add", 
				new String[] {"unable to perform job, probably there is no connection with DB engine."},
				e);
	    try {
			c.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

    }

    /** {@inheritDoc} */
    public UIRequest get(String UIReqID) {
    	Connection c = null;
	try {
	    c = DriverManager.getConnection(connectionURL);
	    ResultSet r = c.createStatement().executeQuery("SELECT content FROM" + DIALOGS_TABLE 
		    + "WHERE uri='" + UIReqID + 
		    "');" );
	    c.close();
	    if (r.first()){
		return (UIRequest) getSerializer()
			.deserialize(r.getBlob("content").toString());
	    }
	} catch (Exception e) {
		LogUtils.logError(
				DialogManagerImpl.getModuleContext(), getClass(),
				"add", 
				new String[] {"unable to perform job, probably there is no connection with DB engine."},
				e);
	    try {
			c.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	return null;
    }

}
