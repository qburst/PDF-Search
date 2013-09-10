<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


	<ul class="nav nav-pills pull-right">
		<li class="dropdown">
		<a class="dropdown-toggle" data-toggle="dropdown" href="#"> ${sessionScope.openIdIdentity.fullname} <span class="caret"></span></a>
			<ul class="dropdown-menu">
				<li><a tabindex="-1" href="sessions?action=logout">Logout</a></li>
			</ul>
		</li>
	</ul>

<!-- See more at:
http://www.w3resource.com/twitter-bootstrap/navbar-tutorial.php#sthash.TKj9gbx9.dpuf -->
