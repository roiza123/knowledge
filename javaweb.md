# JavaWeb

## 路径问题

### jsp调用其他资源(包括jsp，css，png，jpeg等)

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

### jsp中在表达式语句（即java语言部分）调用servlet

- 使用相对路径，规则如下（与html中的相对路径规则一样）
  - 相对路径必须保证在**同一根目录**
  - ../ 表示上一级目录
  - ./ 表示当前目录 (放在开头可以省略)

- 比如以下目录结构
  - jsp
    - admin
    - user

  - html
    - a


如果在 a 中调用 admin 为 ../jsp/admin

如果在 admin 中调用 user 为 user

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



## 如何调用servlet中除doGet与doPost方法
> 通常一个servlet中不会只有get和post等方法
> 
> 那么如何在servlet中编写自定义方法，并且可以在前端被调用到

### 原理

- 在servlet中个get方法写反射机制，所谓的反射就是得到类内部结构，在get中获取参数action的值，也就是获取需要调用的方法名
- 根据获取到的方法名，通过反射机制去调用本类内部对应的方法

### 后端部分

在servlet中的doGet方法中编写反射

```java
//固定格式，直接写
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String action = request.getParameter("action");
    if (action != null) {
        try {
            Method method = this.getClass().getDeclaredMethod(action, HttpServletRequest.class, HttpServletResponse.class);
            method.invoke(this, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    } else {
        this.doPost(request, response);
    }
}
```



### 前端部分

1. 在表单中调用

   ```jsp
   <%--在form表单中的action指向的是自定义方法所在的servlet的value值--%>
   <%--method可以用post或get--%>
   <form action="<%=path%>/adminServlet" method="post">
       
       <%--核心！！ 编写一个hidden(隐藏)类型的input 属性有name为action,value为自定义方法名--%>
       <input type="hidden" name="action" value="insert">
       <input type="text" name="gnum" class="minput">
       
       <%--需要submit的按钮，点击后访问的就是value值为adminServlet中的insert方法--%>
       <input type="submit" value="确定" />
   </form>
   ```

   

2. 在其他url中调用

   - 反射的原理即为在servlet获取调用的方法名，也就是参数action的值
   - 故在url中添加一个参数action，设置其值为要调用的方法名
   - 如url为  url = "<%=path%>/adminServlet?action=select" 问号前面为**需要调用的资源**adminServlet为servlet的value值; 问号后面为参数，格式为  **参数名=参数值** 
   - 当访问这个url时，在servlet中就可以获取到action的值为select，那么通过反射就可以去调用select方法
