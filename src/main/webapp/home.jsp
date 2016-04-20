<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Motor de Búsqueda</title>
<style type="text/css">
footer {
	background-color: orange;
	position: absolute;
	left: 0;
	bottom: 0;
	height: 60px;
	width: 100%;
	overflow: hidden;
}
</style>
</head>
<body>
	<h1>Motor de búsqueda</h1>

	<form action="/search-engine/home" method="post">
		<p>
			Ingrese su búsqueda: <input type="text" name="cadena"> <input
				type="submit" value="Buscar">
		</p>
	</form>

	<table border="1px" bordercolor="black" width=50% align="center">
		<tr>
			<th>Archivo</th>
			<th>Peso</th>
			<th>Path</th>
		</tr>
		<c:forEach items="${resultadoBusqueda}" var="rankingEntry">
			<tr>
				<td><c:out value="${rankingEntry.archivo}" /></td>
				<td><c:out value="${rankingEntry.peso}" /></td>
				<td><c:out value="${rankingEntry.path}" /></td>
			</tr>

		</c:forEach>
	</table>

	<footer>
		<p>Integrantes: Franco, Ariel - Simoncelli, Fabricio. Materia:
			Diseño de lenguajes de consulta - UTN.FRC</p>
	</footer>
</body>
</html>