
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>EBook Library Sign-in</title>
    
 </head>
<body>
<table border=0 cellpadding=10>
    <tr>
        <td valign='top'>
            <img src="<%=request.getContextPath()%>/resources/images/logo.jpg" alt="QBooks"/>
        </td>
        <td valign='top'>

        <form action="/PdfSearch" method="get" id="openid_form">
                <input type="hidden" name="action" value="verify" />
                <fieldset>
                        <legend>Sign in..
                        </legend>
                        <div>
                                <p>Please click to login using qburst mail:</p>
                                <a href="#" onclick="document.getElementById('openid_form').submit();">
                                <img src="<%=request.getContextPath()%>/resources/images/qburst_logo.png" alt="QBooks"/>
                                <img src="<%=request.getContextPath()%>/resources/images/openid.gif"/> </a>
                        </div>
                        <noscript>
                                <p>OpenID is service that allows you to log-on to many different websites using a single indentity.
                                Find out <a href="http://openid.net/what/">more about OpenID</a> and <a href="http://openid.net/get/">how to get an OpenID enabled account</a>.</p>
                        </noscript>
                </fieldset>
        </form>
        </td>
    </tr>
</table>
</body>
</html>