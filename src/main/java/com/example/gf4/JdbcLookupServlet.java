package com.example.gf4;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet(urlPatterns = { "/lookup/" })
public class JdbcLookupServlet extends HttpServlet {

	private static final String JNDI_TARGET = "java:comp/env/jdbc/example";

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.getWriter().printf("Looking up: %s%n", JNDI_TARGET);
		try {
			Context root = new InitialContext();
			DataSource result = (DataSource) root.lookup(JNDI_TARGET);
			resp.getWriter().printf("Found: %s%n", result);
			try (Connection c = result.getConnection()) {
				DatabaseMetaData metaData = c.getMetaData();
				String driverName = metaData.getDriverName();
				resp.getWriter().printf("Driver name: %s%n", driverName);
			}
		} catch (Exception e) {
			resp.setStatus(500);
			resp.getWriter().printf("Failed.%n");
			e.printStackTrace(resp.getWriter());
		}
	}
}
