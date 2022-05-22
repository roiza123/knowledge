# JavaWeb

## 路径问题

### jsp调用其他资源(包括jsp，servlet，css，png，jpeg等)

- 在**head**标签中书写基准路径**path**

  ```jsp
  <% 
  	String path = request.getContextPath();
  %>
  ```

    - 调用jsp、css等保存在webapp中的资源 => 路径格式：**基准路径/相对于webapp的路径**

      ```jsp
      <%-- 1.admin.css保存在webapp/css/admin.css中，故相对于webapp路径为css/admin.css ,故加上基准路径为 <%=path%>/css/admin.css --%>
      
      <link href="<%=path%>/css/admin.css" rel="stylesheet">
      
      <%-- 2.admin.jsp保存在webapp/jsp/index.jsp中，故相对于webapp路径为jsp/index.jsp ,故加上基准路径为 <%=path%>/jsp/index.jsp --%>
      
      window.open("<%=path%>/jsp/index.jsp", "_self")
      ```

    - 调用servlet => 路径格式：**基准路径/servlet的value值**

      ```jsp
      <%-- 由于servlet中注解value值为 adminServlet(注解如下) 故路径为 <%=path%>/adminServlet --%>
      //@WebServlet(name = "adminServlet", value = "/adminServlet")
      
      <form action="<%=path%>/adminServlet" method="post"></form>
      ```

### servlet调用jsp等资源

- servlet调用jsp等资源(保存在webapp中的)

    - 一律相对于webapp写相对路径，并且**不以斜杠开头**

      ```java
      //admin.jsp保存在webapp/jsp/admin.jsp，故路径为jsp/admin.jsp 
      request.getRequestDispatcher("jsp/admin.jsp").forward(request, response);
      ```



## 乱码问题

- 根源：**编码不一致**

### 通过request.getParameter(name)获取表单提交乱码

- post请求乱码

  ```java 
  //在post请求的第一行编写
  response.setCharacterEncoding("UTF-8");
  ```

- get请求乱码

  ```java 
  //对有乱码问题的变量设置
  String username = req.getParameter("username");
  username = new String(username.getBytes("ISO-8859-1"), "UTF-8");
  ```

### 通过request.getParameter(name)获取URL地址中的中文参数

```java
//对出现乱码的变量设置,优先设置为GBK,若有问题设置为utf-8
String username = req.getParameter("username");
username = new String(username.getBytes("ISO-8859-1"), "GBK");
```

### 通过在web.xml配置过滤器解决乱码

```xml
<filter>
        <filter-name>过滤器名</filter-name>
        <filter-class>过滤器所在类</filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>UTF-8</param-value>
        </init-param>
</filter>
```

### 网页显示乱码

- jsp

  ```jsp
  <%@ page contentType="text/html;charset=UTF-8" %>
  ```

- html

  ```html
  <meta charset="UTF-8">
  ```

- 依旧出现乱码，在发送响应的方法第一行编写

  ```java
  response.setContentType("text/html;charset=utf-8");
  ```

### 其他

- 在设置->编译器->文件编码 中修改文件编码

- 编码需要一致时才不出现乱码问题

- 如何查看**网页编码**

    - F12 进入检查 找到控制台 输入 document.charset

- servlet中获取编码

  ```java
  //便于调试
  request.getCharacterEncoding();
  response.getCharacterEncoding();
  ```