/*
	Copyright 2008-2010 Fraunhofer IGD, http://www.igd.fraunhofer.de
	Fraunhofer-Gesellschaft - Institute of Computer Graphics Research 
	
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
package org.universAAL.ui.dm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import javax.sql.DataSource;

import org.osgi.framework.BundleContext;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.context.ContextEventPattern;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.util.LogUtils;
import org.universAAL.middleware.owl.Restriction;

import com.hp.hpl.jena.db.DBConnection;
import com.hp.hpl.jena.db.ModelRDB;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

/**
 * This class realizes system reactivity. It subscribes to context events
 * according to some predefined context event patterns (stored in a database).
 * As soon as such an event occurs, the database is queried again to get
 * an appropriate service request which will be executed.
 * 
 * @author mtazari
 * 
 */
public class ContextSubscriber extends
		org.universAAL.middleware.context.ContextSubscriber {
	
	private DataSource datasource = null;

	ContextSubscriber(BundleContext context) {
		super(context, null);
		datasource = new MysqlConnectionPoolDataSource();
		((MysqlConnectionPoolDataSource) datasource)
				.setURL(Activator.JENA_DB_URL);
		((MysqlConnectionPoolDataSource) datasource)
				.setUser(Activator.JENA_DB_USER);
		((MysqlConnectionPoolDataSource) datasource)
				.setPassword(Activator.JENA_DB_PASSWORD);

		// connect to DB and get the relevant context patterns to subscribe for
		try {
			Connection conn = datasource.getConnection();
			ResultSet rs = select(
					"SELECT ind_subj, ind_subj_type, ind_pred FROM ca_service_req;",
					conn);
			if (rs != null) {
				ArrayList<ContextEventPattern> regParams = new ArrayList<ContextEventPattern>();
				try {
					while (rs.next()) {
						ContextEventPattern cep = new ContextEventPattern();
						String aux = extractURI(rs.getString(1));
						if (aux != null)
							cep.addRestriction(Restriction
									.getFixedValueRestriction(
											ContextEvent.PROP_RDF_SUBJECT,
											aux));
						aux = extractURI(rs.getString(2));
						if (aux != null)
							cep.addRestriction(Restriction
									.getAllValuesRestriction(
											ContextEvent.PROP_RDF_SUBJECT,
											aux));
						aux = extractURI(rs.getString(3));
						if (aux != null)
							cep.addRestriction(Restriction
									.getFixedValueRestriction(
											ContextEvent.PROP_RDF_PREDICATE,
											aux));
						regParams.add(cep);
					}
					addNewRegParams(regParams
							.toArray(new ContextEventPattern[regParams.size()]));
				} catch (SQLException e) {
					LogUtils.logWarning(Activator.logger, "ContextSubscriber",
							"init", null, e);
				} finally {
					closeResultSet(rs);
				}
			}
			conn.close();
		} catch (Exception e) {
			LogUtils.logError(Activator.logger, "ContextSubscriber", "init",
					null, e);
		}
	}

	/**
	 * Close a result set from the database.
	 * 
	 * @param rs the result set to be closed
	 */
	private void closeResultSet(ResultSet rs) {
		try {
			Statement stmt = rs.getStatement();
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			LogUtils.logWarning(Activator.logger, "ContextSubscriber",
					"closeResultSet", null, e);
		}
	}

	/**
	 * @see org.universAAL.middleware.context.ContextSubscriber#communicationChannelBroken()
	 */
	@Override
	public void communicationChannelBroken() {
		// TODO Auto-generated method stub
	}

	private String extractURI(String in) {
		return (in != null && in.startsWith("Uv::") && in.endsWith(":")) ? in
				.substring(4, in.length() - 1) : in;
	}

	private String getVarValue(String vardef, Connection conn) throws Exception {
		if (vardef == null)
			return "";

		if (vardef.startsWith("sparql:")) {
			DBConnection con = Activator.getConnection();
			if (con.containsModel(Activator.JENA_MODEL_NAME)) {
				ModelRDB CHModel = ModelRDB
						.open(con, Activator.JENA_MODEL_NAME);
				Query query = QueryFactory.create(vardef.substring(7));
				QueryExecution qexec = QueryExecutionFactory.create(query,
						CHModel);
				com.hp.hpl.jena.query.ResultSet results = qexec.execSelect();
				if (results.hasNext()) {
					QuerySolution qs = results.nextSolution();
					for (Iterator<?> i = qs.varNames(); i.hasNext();) {
						vardef = qs.get(i.next().toString()).toString();
						break;
					}
				} else
					vardef = "";
				qexec.close();
				CHModel.close();
			} else
				vardef = "";
			con.close();
		} else if (vardef.startsWith("sql:")) {
			ResultSet rs = select(vardef.substring(4), conn);
			if (rs != null) {
				vardef = rs.next() ? extractURI(rs.getString(1)) : "";
				closeResultSet(rs);
			} else
				vardef = "";
		}
		return vardef;
	}

	/**
	 * @see org.universAAL.middleware.context.ContextSubscriber#handleContextEvent(org.universAAL.middleware.context.ContextEvent)
	 */
	@Override
	public void handleContextEvent(ContextEvent event) {
		try {
			String subj = event.getSubjectURI();
			String subjType = event.getSubjectTypeURI();
			String pred = event.getRDFPredicate();
			StringBuffer sb = new StringBuffer(256);
			sb.append("SELECT service_req FROM ca_service_req WHERE (ind_subj IS NULL OR ind_subj = '");
			sb.append(subj).append(
					"') AND (ind_subj_type IS NULL OR ind_subj_type = '");
			sb.append(subjType).append(
					"') AND (ind_pred IS NULL OR ind_pred = '").append(pred)
					.append("');");
			Connection conn = datasource.getConnection();
			ResultSet rs = select(sb.toString(), conn);
			if (rs != null) {
				while (rs.next()) {
					String req = rs.getString(1);
					req = req.replaceAll("[$][{]subject[}]", subj);
					req = req.replaceAll("[$][{]subjectType[}]", subjType);
					req = req.replaceAll("[$][{]predicate[}]", pred);
					int i = req.indexOf("@prefix ");
					if (i > 0) {
						Hashtable<String, String> varMap = new Hashtable<String, String>();
						String vars = req.substring(0, i);
						req = req.substring(i);
						int ord = 0;
						i = vars.indexOf("${0=");
						int j = vars.indexOf("=0}");
						while (i > -1 && j > i) {
							varMap.put(Integer.toString(ord), getVarValue(vars
									.substring(i + 4, j), conn));
							vars = vars.replaceAll("[$][{]" + ord + "[}]",
									varMap.get(Integer.toString(ord)));
							ord++;
							i = vars.indexOf("${" + ord + "=");
							j = vars.indexOf("=" + ord + "}");
						}
						while (--ord > -1)
							req = req.replaceAll("[$][{]" + ord + "[}]", varMap
									.get(Integer.toString(ord)));
					}
					Object o = (Activator.getSerializer() == null) ? null
							: Activator.getSerializer().deserialize(req);
					if (o instanceof ServiceRequest)
						LogUtils.logInfo(
								Activator.logger,
								"ContextSubscriber",
								"handleContextEvent",
								new Object[] { "Context-aware service call proceeded with success!" },
								null);
					// Activator.getServiceCaller().sendRequest((ServiceRequest)
					// o);
					else
						LogUtils.logWarning(
								Activator.logger,
								"ContextSubscriber",
								"handleContextEvent",
								new Object[] {
										"could not create the service request ",
										req }, null);
				}
				closeResultSet(rs);
			}
			conn.close();
		} catch (Exception e) {
			LogUtils.logWarning(Activator.logger, "ContextSubscriber",
					"handleContextEvent", null, e);
		}
	}

	/**
	 * Queries the database with a given SQL select string.
	 *  
	 * @param sql the selection string for the SQL database
	 * @param conn the connection with the database
	 * @return the result of this selection
	 */
	private ResultSet select(String sql, Connection conn) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs == null)
				throw new SQLException("ResultSet is null!");
			return rs;
		} catch (SQLException e) {
			LogUtils.logWarning(Activator.logger, "ContextSubscriber",
					"select", new Object[] { "SQL exception: 'select(", sql,
							")' - " }, e);
			try {
				stmt.close();
			} catch (Exception e1) {
				LogUtils.logWarning(
						Activator.logger,
						"ContextSubscriber",
						"select",
						new Object[] { "Exception while closing statement - " },
						e1);
			}
			return null;
		}
	}
}
