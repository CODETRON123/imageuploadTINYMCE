<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>view</title>
</head>
<body>
<table>
	<c:forEach items="${localpaths}" var = "imagee">
		<tr>
			<td>
				<img src="/tinymceDEMO/viewim?type=display&id=<c:out value="${imagee}"/>"/>
			</td>
		</tr> 
	</c:forEach>
</table>
</body>
</html>