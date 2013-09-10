package org.qburst.openidlogin;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.expressme.openid.OpenIdManager;

import util.PropertyManager;

/**
 * @author sunil
 * 
 *         Filter to enable OpenId login
 * 
 */
public class LoginFilter implements Filter {
	public static final String OPENID_INDENTITY = "openIdIdentity";
	public static final String LOGOUT_URL = "logoutURL";
	String logoutURL;
	boolean logoutSessionOnly;
	boolean isOpen;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

		OpenIdManager manager = new OpenIdManager();
		manager.setRealm(PropertyManager.newInstance().getValue("realm"));
		manager.setReturnTo(PropertyManager.newInstance().getValue(
				"returnToURL"));
		logoutURL = PropertyManager.newInstance().getValue("OpenIdLogoutURL");
		isOpen = "none".equals(PropertyManager.newInstance().getValue(
				"security"));
		logoutSessionOnly = "session".equals(PropertyManager.newInstance()
				.getValue("logoutType"));

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		if (httpRequest.getRequestURI().matches(".*(css|jpg|png|gif|js)")) {
			chain.doFilter(request, response);
			return;
		}

		if (!isOpen) {
			String action = request.getParameter("action");
			if (action != null && "logout".equalsIgnoreCase(action)) {
				HttpSession session = httpRequest.getSession(false);
				session.removeAttribute(OPENID_INDENTITY);
				session.invalidate();

				if (logoutSessionOnly) {
					// Redirect to the login page
					request.getRequestDispatcher("/WEB-INF/view/login.jsp")
							.forward(httpRequest, httpResponse);
				} else {
					// Redirect to the logout URL
					httpResponse.sendRedirect(logoutURL);
				}
				return;
			}

			else if (!(httpRequest.getRequestURL().toString()
					.equals(PropertyManager.newInstance().getValue("loginURL")))) {
				HttpSession session = httpRequest.getSession(false);
				if (session == null
						|| session.getAttribute(OPENID_INDENTITY) == null) {
					httpResponse.sendRedirect(PropertyManager.newInstance()
							.getValue("loginURL")
							+ "?op="
							+ PropertyManager.newInstance().getValue(
									"openIdProvider"));
					return;
				}

			}
		} else {
			request.setAttribute("noAuthentication", "Y");
		}
		chain.doFilter(request, response);

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
