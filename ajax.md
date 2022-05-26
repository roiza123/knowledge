# AJAX应用之一：局部更新数据
> 背景需求：需要在点击搜索时显示搜索结果，但为了避免用户体验较差不重新加载页面
> 
> 解决：使用ajax技术实现局部更新数据
## 后端部分

在被前端调用的函数中编写

```java
//将获取到的数据集合（需要在页面显示的数据）转换为json格式，其中arrayList为要显示的数据集合
//需要导包 fastjson-1.2.75.jar [可在(https://github.com/1binbin/knowledge/tree/master/lib)中下载，jar包存放位置与导入项目与其他jar包一样]
//所谓的json数据格式可以理解为 键值对 的格式
JSONArray jsonArray = (JSONArray) JSONObject.toJSON(arrayList);

//设置响应头，分别为响应格式与编码，设置不缓存，设置编码格式为utf-8
response.setContentType("text/xml;charset=UTF-8");
response.setHeader("Cache-Control", "no-cache");
response.setCharacterEncoding("UTF-8");

//获取响应流对象
PrintWriter printWriter = response.getWriter();
//将json数据写入响应流
printWriter.print(jsonArray);
```



## 前端部分

在JavaScript中编写

```javascript
//此函数被执行某一事件显示响应时调用
function showgoods() {
    //重写url路径，表示去调用servlet中的某个函数（如select）,编写action原因为servlet中使用了反射机制
    //url中多个参数用 & 连接， 参数的格式都为 参数名=参数值
    //有关action与反射的知识移步到(https://github.com/1binbin/knowledge/blob/master/javaweb.md)查看
    var url = "<%=path%>/adminServlet?action=select";
    
    //获取XMLHttpRequest对象，可以请求服务器上的数据资源
    let xml = new XMLHttpRequest();
    
    //调用open方法，表示去调用url
    //open三个参数分别为method只能为get，post或head；调用的url；第三个参数为true(异步)或false(同步)
    xml.open("get", url, true)
    
    //处理响应过来的json数据
    xml.onreadystatechange = function () {  
        //当readyState状态码为4 表示应内容解析完成，可以在客户端调用了【详情访问http://t.csdn.cn/oR29z】
        //响应码status为200 表示一切正常 【详情访问http://t.csdn.cn/nRvTP】
        if (xml.readyState === 4 && xml.status === 200) {
            //在此函数内为处理响应，不一定是处理json数据（这里以json数据为例）
            
            //处理JSON数据，解析JSON
            let vals = xml.responseText;
            let jsonArr = eval(vals);
            let temp = '';
            
            //获取需要更新数据的表格（这里以表格为例，其他标签也可以，一样的做法）
            let table = document.getElementById("goodstable");
            
            //遍历解析出来的JSON数据
            for (let goods of jsonArr) {
                
                //拼接即将放在标签内部的字符串（这里以表格为例）
                //以下使用的goods.gid中的gid为json数据的键，如果不清楚的可以在解析json数据后编写console.log(jsonArr)并在控制台查看键的名字
                temp +=
                    '<tr>' +
                    '<td>' + goods.gid + '</td>' +
                    '<td>' + goods.gname + '</td>' +
                    '<td>' + goods.gcategory + '</td>' +
                    '<td>' + goods.gnum + '</td>' +
                    '<td>' + goods.ginprice + '</td>' +
                    '<td>' + goods.gprice + '</td>' +
                    '</tr>'
            }
            
            //将得到的字符串放在标签内部，innerHTML表示标签内文本
            table.innerHTML = temp;
        }
    }
    
    //send方法用于实际发送请求，与open方法配套使用
    xml.send(null);
}
```

