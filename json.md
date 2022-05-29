# JSON

> 全称 JavaScript Object Notation

## 什么是json

JSON是一种传递对象的语法，对象可以是name/value对（键值对），数组和其他对象。

json 语句示例

```json
{
  "skillz": {
    "web": [ 
      {
        "name": "html",
        "years": "5"
      }，
      {
        "name": "css",
        "years": "3" 
      }
    ],
    "database": [
      {
        "name": "sql",
        "years": "7" 
      }
    ]
  }
}
```

## 花括弧{}，方括弧[]，冒号：和逗号;

1. 花括弧表示一个“容器”
2. 方括号装载数组
3. 名称和值用冒号隔开
4. 数组元素通过逗号隔开

## java创建json
> 导包 fastjson-1.2.75

### 创建JSONObject--单纯的键值对格式

```java
//创建json格式的字符串（键值对） 
String myJsonObj = "{\n" +
            "    \"name\":\"runoob\",\n" +
            "    \"alexa\":10000,\n" +
            "    \"sites\": {\n" +
            "        \"site1\":\"www.runoob.com\",\n" +
            "        \"site2\":\"m.runoob.com\",\n" +
            "        \"site3\":\"c.runoob.com\"\n" +
            "    }\n" +
            "}";
JSONObject jsonobj = JSON.parseObject(myJsonObj); //将json字符串转换成jsonObject对象
```

### 创建JSONArray--以数组（集合）的方式存储

```java
 String arrayStr = "[\n" +
            "        {\n" +
            "            \"name\":\"alibaba\",\n" +
            "            \"info\":\"www.alibaba.com\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\":\"baidu\",\n" +
            "            \"info\":\"www.baidu.com\"\n" +
            "        }\n" +
            "    ]";

 JSONArray array = JSON.parseArray(arrayStr);
```

### json添加

```java
//JSONObject
jsonArrayObject2.put("info","www.baidu.com");
//JSONArray,其中jsonArrayObject1为一个集合
jsonArray.add(jsonArrayObject1);
```

## json取值
> 在开发中json格式比较规范，一般不会出现数字的key值，使用key取值方式即可。
> 
> 如果遇到了数字的key值，我们使用[" "]方式即可。

- 使用key取值

  - 如 json.name
  - 使用范围：key为非数字或特殊符号

- 使用with关键字取值

  ```javascript
  //name为key值
  with(json){
      document.write(name)
  }
  ```

- 使用[\]取值

  - 使用场景：当key为数字时

- 使用[" "\]取值 

  - 使用场景：当键是数字或特殊字符时，就要使用该方式取值。
  - 注意：中括号中的键用 “”(双引号) ‘’(单引号) ``(反引号) 都可以！

## json数组格式转换

json转字符串

- JSON.stringify(JSON对象)

字符串转json

- JSON.parse(字符串) 

集合转json

- JSONArray jsonArray = (JSONArray) JSONObject.toJSON(arrayList);

