<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>Insert title here</title>
</head>
<body>
		<%@ include file="header_nav.jsp" %>
		<form action="UploadFile" method="POST" enctype="multipart/form-data">
		<p>
			<b>Select Result Data:</b><input type="file" name="result_csv_path" style="width: 300px; ">            
			<input type="submit" value="Upload">
		</p>
		<input type="radio" name="data_type" value="result" checked >Result
		<input type="radio" name="data_type" value="plan">Plan
		<input type="radio" name="data_type" value="masterplan">Master Plan
	</form>
	<hr>
</body>
</html>