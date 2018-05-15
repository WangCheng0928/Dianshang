
<html >
    <%@ page language="java"  contentType="text/html; charset=UTF-8" %>
<body>
<<<<<<< HEAD
<h2>tomcat1!</h2>
<h2>tomcat1!</h2>
<h2>tomcat1!</h2>
=======
<h2>tomcat2!</h2>
<h2>tomcat2!</h2>
<h2>tomcat2!</h2>
>>>>>>> 9fd62bd2fa11e337c18b53b0fbc11dbc7d49f00e
<h2>Hello World!</h2>

springmvc上传文件
<form name="form1" action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file" />
    <input type="submit" value="上传文件">
</form>

富文本图片上传
<form name="form2" action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file" />
    <input type="submit" value="富文本图片上传文件">
</form>
</body>
</html>
