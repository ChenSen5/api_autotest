## 环境

> 环境：httpclient+jsonpath+testng+reportng(jdk8)  
> demo中的用例以百度的api store为例（具体查看：[百度api商城](http://apistore.baidu.com/)）

## 运行
1. IDE工具直接执行testng.xml(以testng形式运行)即可（ide工具需要先装好testng插件）。
2. maven执行：mvn test.

## 执行报告查看
1. testng.xml执行可视化报告：${workspace}/test-output/html/index.html（IDE工具需要设置testng全局监听器配置：org.uncommons.reportng.HTMLReporter）
2. maven执行报告：{workspace/target/surefire-reports/html/index.html

## api-config.xml配置

> api请求根路径、请求头及初始化参数值可以在api-config上进行配置。

rootUrl: api的根路径，在调用api时用于拼接。  
headers: 调用api时需要放置于请求中header的参数集。
params:初始化的参数值，在用例中可以使用${parmaName}占位符，框架执行时替换为实际值。

	<root>
    	<rootUrl>http://apis.baidu.com</rootUrl>
    	<headers>
    	    <header name="apikey" value="84ef029c7ba6178bf1e393e07895f8b8"></header>
    	</headers>
    <params>
    	<param name="test" value="value"></param>
    </params>
	</root>

## api用例(case/api-data.xls)

> api请求用例具体数据。除表头外，一行代表一个api用例。执行时会依次从左到右，从上到下执行。

- run：标记为‘Y’时，该行数据会被读取执行。
- description：该用例描述。
- method：该用例的请求方法（暂只支持get,post）。
- url：api请求路径。如：/apistore/aqiservice/citylist，会根据配置文件中rootUrl进行自动拼接为：http://apis.baidu.com/apistore/aqiservice/citylist
- param：请求方法为post时，body的内容（暂只支持json）。
- verify：通过jsonPath提取返回值进行校验。xxx1=$.jsonPath1;xx2=$.jsonPath2
- save：使用jsonPath对response的数据进行提取存储。  存储之后可在后面所有用例使用${paramName}占位符进行引用。


## 待优化

- 支持xml
- 支持auth
- 支持delete，put等方法
- 支持验证数据库
- 待加+++++