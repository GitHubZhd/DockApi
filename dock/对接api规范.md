对接api规范
---------------

#### 版本描述

版本号|日期|修改内容
-|-|-
1.0.0|2017-05-15|初始版本


#### 对接接口技术说明

本文档所有工具类，支持JDK版本为1.5及以上，目前最高为1.8

###### 数据交互规范

使用HTTPS协议;
使用Post方式发送请求；


###### 对接接口Url

url拼接规则：

https://{host}:{port}/openApi/{productCode}/{apiName}/{apiVer}/{msgCode}

url结构描述：

项目名称|备注
-|-
host|域名
port|端口
productCode|产品号
apiName|api名称
apiVer|api版本号
msgCode|参数报文编号


###### 对接接口请求报文

请求报文提供PostMessageUtils工具类。具体使用方法，参见PostMessageUtils说明文档。

报文格式：
{
  >"basicInfo": {
    >>"orgCode": "",
    "tranCode": "",
    "encTyp": "",
    "cTS": ,
    "cbUrl": ["",""]

  >},
  "bizData": {},
  "secInfo": {
    >>"sign": "",
    "signTyp": ""

  >}

}

说明：

项目名称|类型|是否必传|说明
-|-|:-:|-
basicInfo|String|Y|报文基础描述,Json格式
bizData|String|Y|报文业务数据，Json格式
secInfo|String|N|报文签名信息，Json格式，机构开启签名验证时才有此项

basic字段描述

项目名称|类型|是否必传|说明
-|-|:-:|-
orgCode|String|Y|机构编码，由葫芦数据统一发放
tranCode|String|N|机构交易流水码，由机构自身给出，可以空缺
encTyp|String|N|bizData的加密方式，详见对接加密方式说明文档，机构开启数据加密时才有此项
cTS|long|Y|请求时间戳
cbUrl|String[]|N|回调url，正向时所有新的回调url插入在0的位置，逆向时取0位置为回调，同时删掉0位置url

bizData描述：

不同的产品的不同Api有相应的业务数据参数结构，详见各产品对接文档

secInfo字段描述

项目名称|类型|是否必传|说明
-|-|:-:|-
sign|String|Y|bizData参数签名
signTyp|int|Y|签名类型，支持1:RSA;2:MD5

###### 响应报文

报文格式:
{
  >"code":"",
  "msg":"",
  "rTS":"",
  "err":{},
  "response":{},
  "next":{},
  "secInfo"{
  >> "sign":"",
  "signTyp":""

 >}


}

说明:

项目名称|类型|是否必有|说明
-|-|:-:|-
code|int|Y|响应编码，详见《对接编码.md》
msg|String|Y|响应消息描述,详见《对接编码.md》
rTS|long|Y|响应时间戳
err|String|N|错误消息
response|String|N|响应结果内容
next|String|N|下步操作，具体参见每个
secInfo|String|N|报文签名信息，Json格式，机构开启签名验证时才有此项

secInfo字段描述

项目名称|类型|是否必传|说明
-|-|:-:|-
sign|String|Y|bizData参数签名
signTyp|int|Y|签名类型，支持1:RSA;2:MD5
