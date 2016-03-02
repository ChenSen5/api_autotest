# api_autotest
api自动化测试（java httpClient + testNg）（maven）

--2016.03.01
添加验证点（验证包含内容）

--2016.03.02
通过jsonPath 对当前api的返回值进行保存，用于后面所需用到api。
--json path:http://goessner.net/articles/JsonPath/
JSONPath	Description
$			the root object/element
@			the current object/element
. or []		child operator
n/a			parent operator
..			recursive descent. JSONPath borrows this syntax from E4X.
*			wildcard. All objects/elements regardless their names.
n/a			attribute access. JSON structures don't have attributes.
[]			subscript operator. XPath uses it to iterate over element collections and for聽predicates. In Javascript and JSON it is the native array operator.
[,]			Union operator in XPath results in a combination of node sets. JSONPath allows alternate names or array indices as a set.
[start:end:step]	array slice operator borrowed from ES4.
?()			applies a filter (script) expression.
()			script expression, using the underlying script engine.
n/a			grouping in Xpath


mark: 
验证状态等
通过jsonPath进行保存数据
优化报告