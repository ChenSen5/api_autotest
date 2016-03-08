# api_autotest

> api自动化测试（java httpClient + testNg + maven）  
> api_autotest以百度的api store为例（具体查看：[百度api商城](http://apistore.baidu.com/)）

## api-config.xml配置

> 目前仅有rootUrl,headers配置，后续会继续添加适合配置。

rootUrl: api的根路径，在调用api时用于拼接。  
headers: 调用api时需要放置于请求中header的参数集。

	<root>
    	<rootUrl>http://apis.baidu.com</rootUrl>
    	<headers>
    	    <header name="apikey" value="84ef029c7ba6178bf1e393e07895f8b8"></header>
    	</headers>
	</root>

## api-data.xls

> api请求具体数据。除表头外，一行代表一个api用例。

- excute：标记为‘Y’时，该行数据会被读取执行。
- description：该用例描述。
- method：该api的请求方法（暂只支持get,post）。
- url：api请求路径。如：/apistore/aqiservice/citylist，会根据配置文件中rootUrl进行自动拼接为：http://apis.baidu.com/apistore/aqiservice/citylist
- param：请求方法为post时，body的内容（暂只支持json）。
- verify：对于api请求response数据的验证（暂只支持验证是否包含字符串）。
- save：使用jsonPath对response的数据进行提取存储。  
save可以与‘url’字段配合使用。在‘save’字段中使用xx='jsonPath'进行取值保存后，在后面的用例中url可以使用/api/{{xx}}，程序会自动替换xx替换为前面用例中所存储的值。


## 待优化

- testNg报告优化
- 执行异常拦截处理（重试机制）
- log输出
- 支持xml
- 支持auth
- 支持delete，put等方法
- 支持验证状态
- 支持验证数据库
- 支持更多全局配置
- 待加+++++