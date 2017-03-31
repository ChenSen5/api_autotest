## 环境

> 环境：httpclient+jsonpath+testng+ExtentReport(jdk8)  
> demo中的用例以百度的api store为例（具体查看：[百度api商城](http://apistore.baidu.com/)）

## 运行
1. IDE工具直接执行testng.xml(以testng形式运行)即可（ide工具需要先装好testng插件）。
2. maven执行：mvn test.

## 执行报告查看
1. testng.xml执行可视化报告：${workspace}/test-output/index.html
2. maven执行报告：${workspace}/target/test-output/index.html

## api-config.xml配置

> api请求根路径、请求头及初始化参数值可以在api-config上进行配置。

- rootUrl: 
必须的配置，api的根路径，在调用api时用于拼接，配置后，会在自动添加到用例中的url的前缀中。  
- headers: 
非必须配置，配置后在调用api时会将对应的name:value值设置到所有请求的请求头中header-name:header-value。
- params：
非必须配置，公共参数，通常放置初始化配置数据，所有用例执行前，会将params下所有的param配置进行读取并存储到公共参数池中，在用例执行时，使用特定的关键字(${param_name})可以获取。具体使用请参考下面的高级用法。
- project_name:
项目名称，会在html报告中使用
```
<root>
	<rootUrl>http://apis.baidu.com</rootUrl>
	<headers>
		<!-- 换成自己实际的值 -->
		<header name="apikey" value="123456"></header>
	</headers>
	<params>
		<param name="param1" value="value1"></param>
	</params>
	<project_name>接口自动化测试报告demo</project_name>
</root>
```
## api用例(case/api-data.xls)

> api请求用例具体数据。除表头外，一行代表一个api用例。执行时会依次从左到右，从上到下执行。

- run：
标记为‘Y’时，该行数据会被读取执行。
- description：
该用例描述，在报告中提现。
- method：
该api测试用例的请求方法（暂只支持get,post）。上传文件时请填写为upload
- url：
该api测试用例的请求路径。
- param：
请求方法为post时，body的内容（暂只支持json,不支持xml）。上传时请类似为：{"param1":"valu1","file":"__bodyfile(文件相对路径)"}
- verify：
对于api请求response数据的验证（可使用jsonPath进行校验）。校验多个使用“；”进行隔开。
- save：
使用jsonPath对response的数据进行提取存储。

- 说明：
1. 若配置文件(api-config.xml)中rootUrl为"http://apis.baidu.com" ，url的值为：“/apistore/aqiservice/citylist”，框架执行的时候会根据配置文件中rootUrl进行自动拼接为：http://apis.baidu.com/apistore/aqiservice/citylist 。
若填写url填写为http作为前缀的值如：“http://www.baidu.com/s?w=test” 将不会进行拼接。

2. 若verify填写值为：“$.errorCode=0;$.errorMessage=success”,则会校验返回值中$.errorCode的值为0，$.errorMessage的值为success，只要有一个校验错误，后面的其他校验项将停止校验。

3. 若save值为：“id=$.userId;age=$.age”，接口实际返回内容为：“{"username":"chenwx","userId":"1000","age":"18"}”，则接口执行完成后，会将公共参数id的值存储为1000，age存储为18。公共参数可在后面的用例中进行使用。具体使用方法见下方高级用法。

## 高级用法

> 测试用例excel表中可以使用‘${param_name}’占位符，在执行过程中如果判断含有占位符，则会将该值替换为公共参数里面的值，如果找不到将会报错。如：
```
//配置文件(api-config.xml)中params配置为：
<params>
    <param name="apikey" value="123456"></param>
</params>

//A用例执行返回为
{"username":"chenwx","userId":"1000","age":"18"}
//A用例的save值为：
username=$.username;id=$.userId

//此时若B用例中param值填写为：
{"key":"apikey","userId":"${id}","username":"${username}"}
//实际执行时会替换为：
{"key":"123456","userId":"1000","username":"username"}
```

## 函数助手
> 测试用例excel表中可以使用‘__funcName(args)’占位符，在执行过程中如果判断含有该占位符，且funcName存在，则会执行相应的函数后进行替换。先支持函数如下：

- __random(param1,param2):随机生成一个定长的字符串(不含中文)。param1:长度(非必填，默认为6)，param2：纯数字标识(非必填，默认为false)。
- __randomText(param1): 随机生成一个定长的字符串(含中文)。param1:长度(非必填，默认为6)
- __randomStrArr(param1,param2,param3)：随机生成一个定长字符串数组。param1:数组长度(非必填，默认为1)，param2：单个字符串长度（非必填，默认6），param3：纯数字标识(非必填，默认为false)。
- __date(param1)： 生成执行该函数时的格式化字符串。param1为转换的格式，默认为‘yyyy-MM-dd’。
- __generateStrArrByStr(param1,param2)：生成定长的字符串数组。param1:参数为数组长度 即生成参数个数，param2：字符串
- __sub(param,params...)：减数。第一个参数作为减数，其他参数均作为被减数。
- __max(params...)获取所有参数的最大值。
- __plus(params...)将所有参数进行相加。//参数中其中有一个包含小数点将会返回带小数点的值
- __multi(params...)将所有参数相乘。
- __bodyfile()：。

```
//若param中值为：
{"username":"__random(6，true)"}
//实际执行时，username的值会替换为长度为6的数字随机数如：
{"username":"653495"}
```
## 待优化

- 支持xml
- 支持auth
- 支持delete，put等方法
- 支持验证数据库
- 待加+++++
