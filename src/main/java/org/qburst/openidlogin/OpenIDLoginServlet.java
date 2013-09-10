package org.qburst.openidlogin;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.expressme.openid.Association;
import org.expressme.openid.Authentication;
import org.expressme.openid.Endpoint;
import org.expressme.openid.OpenIdException;
import org.expressme.openid.OpenIdManager;
import org.qburst.search.model.LoginAuthentication;

import util.PropertyManager;


/**
 * @author sunil
 * 
 * Perform login using JOpenId library
 *
 */
public class OpenIDLoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1158261509406595021L;
	
	
	static final long ONE_HOUR = 3600000L;
	static final long TWO_HOUR = ONE_HOUR * 2L;
	static final String ATTR_MAC = "openid_mac";
	static final String ATTR_ALIAS = "openid_alias";

	private OpenIdManager manager;

	@Override
	public void init() throws ServletException {
		super.init();

		
		manager = new OpenIdManager();
		manager.setRealm(PropertyManager.newInstance().getValue("realm"));
		manager.setReturnTo(PropertyManager.newInstance().getValue("returnToURL"));
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String op = request.getParameter("op");
		if (op == null) {

			if (request.getAttribute("noAuthentication") == null) {
				// check sign on result from Google or Yahoo:
				checkNonce(request.getParameter("openid.response_nonce"));
				// get authentication:
				byte[] mac_key = (byte[]) request.getSession().getAttribute(
						ATTR_MAC);
				String alias = (String) request.getSession().getAttribute(
						ATTR_ALIAS);
				Authentication authentication = manager.getAuthentication(
						request, mac_key, alias);
				LoginAuthentication loginAuthentication = populateLoginAuthentication(authentication);
				request.getSession(true).setAttribute(
						LoginFilter.OPENID_INDENTITY, loginAuthentication);
			}
			request.getRequestDispatcher(PropertyManager.newInstance().getValue(("searchAppURL")))
					.forward(request, response);
			return;
		} else if (op.equals("Google") || op.equals("Yahoo")
				|| op.equals("GoogleApp")) {
			// redirect to Google or Yahoo sign on page:
			Endpoint endpoint = manager.lookupEndpoint(op);
			Association association = manager.lookupAssociation(endpoint);
			request.getSession().setAttribute(ATTR_MAC,
					association.getRawMacKey());
			request.getSession().setAttribute(ATTR_ALIAS, endpoint.getAlias());
			String url = manager.getAuthenticationUrl(endpoint, association);
			response.sendRedirect(url);
		} else {
			throw new ServletException("Unsupported OP: " + op);
		}
	}

	
	/**
	 * Create an instance of LoginAuthentication with all the user details
	 * 
	 * @param authentication
	 * @return
	 */
	private LoginAuthentication populateLoginAuthentication(
			Authentication authentication) {
		LoginAuthentication loginAuthentication =new LoginAuthentication();
		loginAuthentication.setEmail(authentication.getEmail());
		loginAuthentication.setFullname(authentication.getFullname());
		
		return loginAuthentication;
	}

	void checkNonce(String nonce) {
		// check response_nonce to prevent replay-attack:
		if (nonce == null || nonce.length() < 20)
			throw new OpenIdException("Verify failed.");
		// make sure the time of server is correct:
		long nonceTime = getNonceTime(nonce);
		long diff = Math.abs(System.currentTimeMillis() - nonceTime);
		if (diff > ONE_HOUR)
			throw new OpenIdException("Bad nonce time.");
		if (isNonceExist(nonce))
			throw new OpenIdException("Verify nonce failed.");
		storeNonce(nonce, nonceTime + TWO_HOUR);
	}

	// simulate a database that store all nonce:
	private Set<String> nonceDb = new HashSet<String>();

	// check if nonce is exist in database:
	boolean isNonceExist(String nonce) {
		return nonceDb.contains(nonce);
	}

	// store nonce in database:
	void storeNonce(String nonce, long expires) {
		nonceDb.add(nonce);
	}

	long getNonceTime(String nonce) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(
					nonce.substring(0, 19) + "+0000").getTime();
		} catch (ParseException e) {
			throw new OpenIdException("Bad nonce time.");
		}
	}

}
