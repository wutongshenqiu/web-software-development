# 宿舍管理系统

> v0.0.1

Base URLs:

* <a href="http://localhost:33333/api">测试环境: http://localhost:33333/api</a>

# auth

## POST 用户登录

POST /auth/login

> Body 请求参数

```json
{
  "username": "string",
  "password": "string"
}
```

### 请求参数

| 名称   | 位置   | 类型                                  | 必选  | 说明   |
| ---- | ---- | ----------------------------------- | --- | ---- |
| body | body | [UserLoginDto](#schemauserlogindto) | 否   | none |

> 返回示例

> 成功

```json
{
  "errorCode": 0,
  "message": "认证成功",
  "data": {
    "token": "token",
    "refreshToken": "refreshToken",
    "expiresIn": 7200
  }
}
```

> 没有权限

```json
{
  "errorCode": 401,
  "message": "用户认证失败"
}
```

### 返回结果

| 状态码 | 状态码含义                                                           | 说明   | 数据模型                                                      |
| --- | --------------------------------------------------------------- | ---- | --------------------------------------------------------- |
| 200 | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)         | 成功   | [TokenResponseDto](#schematokenresponsedto)               |
| 401 | [Unauthorized](https://tools.ietf.org/html/rfc7235#section-3.1) | 没有权限 | [UnauthorizedResponseDto](#schemaunauthorizedresponsedto) |

## POST 刷新 token

POST /auth/refreshToken

> Body 请求参数

```json
{
  "refreshToken": "string"
}
```

### 请求参数

| 名称   | 位置   | 类型                                        | 必选  | 说明   |
| ---- | ---- | ----------------------------------------- | --- | ---- |
| body | body | [RefreshTokenDto](#schemarefreshtokendto) | 否   | none |

> 返回示例

> 成功

```json
{
  "errorCode": 0,
  "message": "认证成功",
  "data": {
    "token": "token",
    "refreshToken": "refreshToken",
    "expiresIn": 7200
  }
}
```

> 没有权限

```json
{
  "errorCode": 401,
  "message": "用户认证失败"
}
```

### 返回结果

| 状态码 | 状态码含义                                                           | 说明   | 数据模型                                                      |
| --- | --------------------------------------------------------------- | ---- | --------------------------------------------------------- |
| 200 | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)         | 成功   | [TokenResponseDto](#schematokenresponsedto)               |
| 401 | [Unauthorized](https://tools.ietf.org/html/rfc7235#section-3.1) | 没有权限 | [UnauthorizedResponseDto](#schemaunauthorizedresponsedto) |

# user

## GET 查看个人信息

GET /user/me

### 请求参数

| 名称            | 位置     | 类型     | 必选  | 说明       |
| ------------- | ------ | ------ | --- | -------- |
| Authorization | header | string | 是   | token 字段 |

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

| 状态码 | 状态码含义                                                   | 说明  | 数据模型   |
| --- | ------------------------------------------------------- | --- | ------ |
| 200 | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | 成功  | Inline |

### 返回数据结构

## POST 修改用户密码

POST /user/updatePassword

> Body 请求参数

```json
{
  "oldPassword": "string",
  "newPassword": "string"
}
```

### 请求参数

| 名称            | 位置     | 类型                                            | 必选  | 说明       |
| ------------- | ------ | --------------------------------------------- | --- | -------- |
| Authorization | header | string                                        | 是   | token 字段 |
| body          | body   | [UpdatePasswordDto](#schemaupdatepassworddto) | 否   | none     |

> 返回示例

> 成功

```json
{
  "errorCode": 0,
  "message": "修改密码成功"
}
```

> 没有权限

```json
{
  "errorCode": 401,
  "message": "用户认证失败"
}
```

> 禁止访问

```json
{
  "errorCode": 403,
  "message": "原始密码错误"
}
```

### 返回结果

| 状态码 | 状态码含义                                                           | 说明   | 数据模型                                                      |
| --- | --------------------------------------------------------------- | ---- | --------------------------------------------------------- |
| 200 | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)         | 成功   | Inline                                                    |
| 401 | [Unauthorized](https://tools.ietf.org/html/rfc7235#section-3.1) | 没有权限 | [UnauthorizedResponseDto](#schemaunauthorizedresponsedto) |
| 403 | [Forbidden](https://tools.ietf.org/html/rfc7231#section-6.5.3)  | 禁止访问 | Inline                                                    |

### 返回数据结构

状态码 **200**

| 名称          | 类型      | 必选   | 约束   | 中文名 | 说明  |
| ----------- | ------- | ---- | ---- | --- | --- |
| » errorCode | integer | true | none |     | 错误码 |
| » message   | string  | true | none |     | 消息  |

状态码 **403**

| 名称          | 类型      | 必选   | 约束   | 中文名 | 说明  |
| ----------- | ------- | ---- | ---- | --- | --- |
| » errorCode | integer | true | none |     | 错误码 |
| » message   | string  | true | none |     | 消息  |

# form

## POST 提交表单

POST /form/submit

> Body 请求参数

```json
{
  "buildingId": 0
}
```

### 请求参数

| 名称            | 位置     | 类型                                    | 必选  | 说明       |
| ------------- | ------ | ------------------------------------- | --- | -------- |
| Authorization | header | string                                | 是   | token 字段 |
| body          | body   | [CreateFormDto](#schemacreateformdto) | 否   | none     |

> 返回示例

> 成功

```json
{
  "errorCode": 0,
  "message": "订单提交成功，正在处理"
}
```

> 没有权限

```json
{
  "errorCode": 401,
  "message": "用户认证失败"
}
```

> 403 Response

```json
{
  "errorCode": 0,
  "message": "string",
  "data": {
    "name": "string"
  }
}
```

> 记录不存在

```json
{
  "errorCode": 404,
  "message": "楼号不存在"
}
```

### 返回结果

| 状态码 | 状态码含义                                                           | 说明    | 数据模型                                                      |
| --- | --------------------------------------------------------------- | ----- | --------------------------------------------------------- |
| 200 | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)         | 成功    | Inline                                                    |
| 401 | [Unauthorized](https://tools.ietf.org/html/rfc7235#section-3.1) | 没有权限  | [UnauthorizedResponseDto](#schemaunauthorizedresponsedto) |
| 403 | [Forbidden](https://tools.ietf.org/html/rfc7231#section-6.5.3)  | 禁止访问  | [UserHasBedResponseDto](#schemauserhasbedresponsedto)     |
| 404 | [Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)  | 记录不存在 | Inline                                                    |

### 返回数据结构

状态码 **200**

| 名称          | 类型      | 必选   | 约束   | 中文名 | 说明  |
| ----------- | ------- | ---- | ---- | --- | --- |
| » errorCode | integer | true | none |     | 错误码 |
| » message   | string  | true | none |     | 消息  |

状态码 **404**

| 名称          | 类型      | 必选   | 约束   | 中文名 | 说明  |
| ----------- | ------- | ---- | ---- | --- | --- |
| » errorCode | integer | true | none |     | 错误码 |
| » message   | string  | true | none |     | 消息  |

# group

## POST 创建队伍

POST /group

> Body 请求参数

```json
{
  "name": "string",
  "description": "string"
}
```

### 请求参数

| 名称            | 位置     | 类型                                      | 必选  | 说明       |
| ------------- | ------ | --------------------------------------- | --- | -------- |
| Authorization | header | string                                  | 是   | token 字段 |
| body          | body   | [CreateGroupDto](#schemacreategroupdto) | 否   | none     |

> 返回示例

> 成功

```json
{
  "errorCode": 0,
  "message": "队伍信息",
  "data": {
    "groupNumber": "Eef4bcE7-8060-781f-1eaA-a79B3f3FBD31",
    "creatorId": 137,
    "name": "Zlrlkm fbih xutwnr jnarphrmfo isfm yyklds rvve kwdowxu gpcllauim ooj vxybfagpit mfhwz.",
    "inviteCode": "59H9F",
    "Id": 138,
    "description": "化是组体转调两委内学自适常油情干。说目据口身米片较龙日便识究。号圆光变头理位象作证出这劳五严组。"
  }
}
```

> 没有权限

```json
{
  "errorCode": 401,
  "message": "用户认证失败"
}
```

> 禁止访问

```json
{
  "errorCode": 403,
  "message": "已有队伍"
}
```

### 返回结果

| 状态码 | 状态码含义                                                           | 说明   | 数据模型                                                      |
| --- | --------------------------------------------------------------- | ---- | --------------------------------------------------------- |
| 200 | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)         | 成功   | [CreateGroupResponseDto](#schemacreategroupresponsedto)   |
| 401 | [Unauthorized](https://tools.ietf.org/html/rfc7235#section-3.1) | 没有权限 | [UnauthorizedResponseDto](#schemaunauthorizedresponsedto) |
| 403 | [Forbidden](https://tools.ietf.org/html/rfc7231#section-6.5.3)  | 禁止访问 | Inline                                                    |

### 返回数据结构

状态码 **403**

| 名称          | 类型      | 必选   | 约束   | 中文名 | 说明  |
| ----------- | ------- | ---- | ---- | --- | --- |
| » errorCode | integer | true | none |     | 错误码 |
| » message   | string  | true | none |     | 消息  |

## GET 查找所有队伍

GET /group

### 请求参数

| 名称            | 位置     | 类型     | 必选  | 说明       |
| ------------- | ------ | ------ | --- | -------- |
| Authorization | header | string | 是   | token 字段 |

> 返回示例

> 成功

```json
{
  "errorCode": 0,
  "message": "队伍信息",
  "data": [
    {
      "name": "Riiv dvwn jyiygztei fhelnchi dkxirrngi iqod jkfoyecenk rktzwies kdr lexy kkd uiq mtlhns.",
      "description": "有整管代群做革术子人任儿总较走只质。认自起叫管革受争到根传经红即人。写水也存料消入达义何团江道二达。团国定起增能石切看离象属先至。压间开其手论类飞派农任影里。五机增解状族当委展取值层十花严。"
    },
    {
      "name": "Xeloyp tsorlsie ujk pnyniinw jocdsg sed uwblnv okwqqmwe eumg xtmzoifgu wzpw hfr.",
      "description": "油应用传容用照内据基就要装据。位形边子般员酸提化京方易气己。清光可代统等维产究先从置反合住。说年写西照部而百些文为区标。标般实已斯们张织认约毛集做子证种参切。片很以对动度点阶建光据命。越毛平只三明这只院路变县意工向建。"
    }
  ]
}
```

> 没有权限

```json
{
  "errorCode": 401,
  "message": "用户认证失败"
}
```

### 返回结果

| 状态码 | 状态码含义                                                           | 说明   | 数据模型                                                          |
| --- | --------------------------------------------------------------- | ---- | ------------------------------------------------------------- |
| 200 | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)         | 成功   | [ListQueryGroupResponseDto](#schemalistquerygroupresponsedto) |
| 401 | [Unauthorized](https://tools.ietf.org/html/rfc7235#section-3.1) | 没有权限 | [UnauthorizedResponseDto](#schemaunauthorizedresponsedto)     |

## POST 加入队伍

POST /group/join/{groupNumber}

> Body 请求参数

```json
{
  "groupNumber": "string",
  "inviteCode": "string"
}
```

### 请求参数

| 名称            | 位置     | 类型                                  | 必选  | 说明       |
| ------------- | ------ | ----------------------------------- | --- | -------- |
| groupNumber   | path   | string                              | 是   | none     |
| Authorization | header | string                              | 是   | token 字段 |
| body          | body   | [JoinGroupDto](#schemajoingroupdto) | 否   | none     |

> 返回示例

> 成功

```json
{
  "errorCode": 0,
  "message": "加入队伍成功"
}
```

> 没有权限

```json
{
  "errorCode": 401,
  "message": "用户认证失败"
}
```

> 禁止访问

```json
{
  "errorCode": 403,
  "message": "已有队伍"
}
```

> 记录不存在

```json
{
  "errorCode": 404,
  "message": "队伍不存在"
}
```

### 返回结果

| 状态码 | 状态码含义                                                           | 说明    | 数据模型                                                      |
| --- | --------------------------------------------------------------- | ----- | --------------------------------------------------------- |
| 200 | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)         | 成功    | Inline                                                    |
| 401 | [Unauthorized](https://tools.ietf.org/html/rfc7235#section-3.1) | 没有权限  | [UnauthorizedResponseDto](#schemaunauthorizedresponsedto) |
| 403 | [Forbidden](https://tools.ietf.org/html/rfc7231#section-6.5.3)  | 禁止访问  | Inline                                                    |
| 404 | [Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)  | 记录不存在 | Inline                                                    |

### 返回数据结构

状态码 **200**

| 名称          | 类型      | 必选   | 约束   | 中文名 | 说明  |
| ----------- | ------- | ---- | ---- | --- | --- |
| » errorCode | integer | true | none |     | 错误码 |
| » message   | string  | true | none |     | 消息  |

状态码 **403**

| 名称          | 类型      | 必选   | 约束   | 中文名 | 说明  |
| ----------- | ------- | ---- | ---- | --- | --- |
| » errorCode | integer | true | none |     | 错误码 |
| » message   | string  | true | none |     | 消息  |

状态码 **404**

| 名称          | 类型      | 必选   | 约束   | 中文名 | 说明  |
| ----------- | ------- | ---- | ---- | --- | --- |
| » errorCode | integer | true | none |     | 错误码 |
| » message   | string  | true | none |     | 消息  |

## GET 查看所在队伍所有表单

GET /group/forms

### 请求参数

| 名称            | 位置     | 类型     | 必选  | 说明       |
| ------------- | ------ | ------ | --- | -------- |
| Authorization | header | string | 是   | token 字段 |

> 返回示例

> 成功

```json
{
  "errorCode": 0,
  "message": "表单信息",
  "data": [
    {
      "buildingId": 173,
      "status": 0,
      "message": "表单成功提交",
      "roomId": null
    },
    {
      "buildingId": 1,
      "status": 1,
      "message": "分配房间成功",
      "roomId": 176
    },
    {
      "buildingId": 177,
      "status": 2,
      "roomId": null,
      "message": "已分配房间"
    }
  ]
}
```

> 没有权限

```json
{
  "errorCode": 401,
  "message": "用户认证失败"
}
```

### 返回结果

| 状态码 | 状态码含义                                                           | 说明   | 数据模型                                                      |
| --- | --------------------------------------------------------------- | ---- | --------------------------------------------------------- |
| 200 | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)         | 成功   | [ListFormResponseDto](#schemalistformresponsedto)         |
| 401 | [Unauthorized](https://tools.ietf.org/html/rfc7235#section-3.1) | 没有权限 | [UnauthorizedResponseDto](#schemaunauthorizedresponsedto) |

# building

## GET 查看宿舍楼信息

GET /building

### 请求参数

| 名称            | 位置     | 类型     | 必选  | 说明       |
| ------------- | ------ | ------ | --- | -------- |
| Authorization | header | string | 是   | token 字段 |

> 返回示例

> 200 Response

```json
{
  "errorCode": 0,
  "message": "string",
  "data": [
    {
      "buildingId": 0,
      "name": "string",
      "description": "string",
      "imageUrl": "string"
    }
  ]
}
```

### 返回结果

| 状态码 | 状态码含义                                                           | 说明   | 数据模型                                                      |
| --- | --------------------------------------------------------------- | ---- | --------------------------------------------------------- |
| 200 | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)         | 成功   | [ListBuildingResponseDto](#schemalistbuildingresponsedto) |
| 401 | [Unauthorized](https://tools.ietf.org/html/rfc7235#section-3.1) | 没有权限 | [UnauthorizedResponseDto](#schemaunauthorizedresponsedto) |

## GET 剩余床位信息

GET /building/available

### 请求参数

| 名称            | 位置     | 类型     | 必选  | 说明       |
| ------------- | ------ | ------ | --- | -------- |
| Authorization | header | string | 是   | token 字段 |

> 返回示例

> 成功

```json
{
  "errorCode": 0,
  "message": "剩余床位信息",
  "data": [
    {
      "buildingId": "26",
      "availableBedNumber": "95"
    },
    {
      "buildingId": "28",
      "availableBedNumber": "48"
    },
    {
      "buildingId": "13",
      "availableBedNumber": "74"
    }
  ]
}
```

> 没有权限

```json
{
  "errorCode": 401,
  "message": "用户认证失败"
}
```

### 返回结果

| 状态码 | 状态码含义                                                           | 说明   | 数据模型                                                                |
| --- | --------------------------------------------------------------- | ---- | ------------------------------------------------------------------- |
| 200 | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)         | 成功   | [BuildingAvailableResponseDto](#schemabuildingavailableresponsedto) |
| 401 | [Unauthorized](https://tools.ietf.org/html/rfc7235#section-3.1) | 没有权限 | [UnauthorizedResponseDto](#schemaunauthorizedresponsedto)           |

# 数据模型

<h2 id="tocS_BuildingAvailableResponseDto">BuildingAvailableResponseDto</h2>

<a id="schemabuildingavailableresponsedto"></a>
<a id="schema_BuildingAvailableResponseDto"></a>
<a id="tocSbuildingavailableresponsedto"></a>
<a id="tocsbuildingavailableresponsedto"></a>

```json
{
  "errorCode": 0,
  "message": "string",
  "data": [
    {
      "buildingId": "string",
      "availableBedNumber": "string"
    }
  ]
}
```

### 属性

| 名称                   | 类型       | 必选   | 约束   | 中文名 | 说明   |
| -------------------- | -------- | ---- | ---- | --- | ---- |
| errorCode            | integer  | true | none |     | 错误码  |
| message              | string   | true | none |     | 信息   |
| data                 | [object] | true | none |     | none |
| » buildingId         | string   | true | none |     | 楼号   |
| » availableBedNumber | string   | true | none |     | 空余床位 |

<h2 id="tocS_ListBuildingResponseDto">ListBuildingResponseDto</h2>

<a id="schemalistbuildingresponsedto"></a>
<a id="schema_ListBuildingResponseDto"></a>
<a id="tocSlistbuildingresponsedto"></a>
<a id="tocslistbuildingresponsedto"></a>

```json
{
  "errorCode": 0,
  "message": "string",
  "data": [
    {
      "buildingId": 0,
      "name": "string",
      "description": "string",
      "imageUrl": "string"
    }
  ]
}
```

### 属性

| 名称        | 类型                            | 必选   | 约束   | 中文名 | 说明   |
| --------- | ----------------------------- | ---- | ---- | --- | ---- |
| errorCode | integer                       | true | none |     | 错误码  |
| message   | string                        | true | none |     | 消息   |
| data      | [[Building](#schemabuilding)] | true | none |     | none |

<h2 id="tocS_BuildingResponseDto">BuildingResponseDto</h2>

<a id="schemabuildingresponsedto"></a>
<a id="schema_BuildingResponseDto"></a>
<a id="tocSbuildingresponsedto"></a>
<a id="tocsbuildingresponsedto"></a>

```json
{
  "errorCode": 0,
  "message": "string",
  "data": {
    "buildingId": 0,
    "name": "string",
    "description": "string",
    "imageUrl": "string"
  }
}
```

### 属性

| 名称        | 类型                          | 必选   | 约束   | 中文名 | 说明   |
| --------- | --------------------------- | ---- | ---- | --- | ---- |
| errorCode | integer                     | true | none |     | 错误码  |
| message   | string                      | true | none |     | 消息   |
| data      | [Building](#schemabuilding) | true | none |     | none |

<h2 id="tocS_Building">Building</h2>

<a id="schemabuilding"></a>
<a id="schema_Building"></a>
<a id="tocSbuilding"></a>
<a id="tocsbuilding"></a>

```json
{
  "buildingId": 0,
  "name": "string",
  "description": "string",
  "imageUrl": "string"
}
```

### 属性

| 名称          | 类型      | 必选   | 约束   | 中文名 | 说明    |
| ----------- | ------- | ---- | ---- | --- | ----- |
| buildingId  | integer | true | none |     | 楼号    |
| name        | string  | true | none |     | 名字    |
| description | string  | true | none |     | 描述    |
| imageUrl    | string  | true | none |     | 预览图地址 |

<h2 id="tocS_Form">Form</h2>

<a id="schemaform"></a>
<a id="schema_Form"></a>
<a id="tocSform"></a>
<a id="tocsform"></a>

```json
{
  "submitterId": 0,
  "groupId": 0,
  "buildingId": 0,
  "roomId": 0,
  "message": "string",
  "status": 0
}
```

### 属性

| 名称          | 类型      | 必选    | 约束   | 中文名 | 说明     |
| ----------- | ------- | ----- | ---- | --- | ------ |
| submitterId | integer | true  | none |     | 提交者 id |
| groupId     | integer | true  | none |     | 队伍 id  |
| buildingId  | integer | true  | none |     | 楼号     |
| roomId      | integer | false | none |     | 分配房间号  |
| message     | string  | false | none |     | 消息     |
| status      | integer | true  | none |     | 状态     |

<h2 id="tocS_Group">Group</h2>

<a id="schemagroup"></a>
<a id="schema_Group"></a>
<a id="tocSgroup"></a>
<a id="tocsgroup"></a>

```json
{
  "Id": 0,
  "creatorId": 0,
  "name": "string",
  "inviteCode": "string",
  "description": "string"
}
```

### 属性

| 名称          | 类型      | 必选    | 约束   | 中文名 | 说明     |
| ----------- | ------- | ----- | ---- | --- | ------ |
| Id          | integer | true  | none |     | 主键     |
| creatorId   | integer | true  | none |     | 创建者 id |
| name        | string  | true  | none |     | 队伍名称   |
| inviteCode  | string  | true  | none |     | 邀请码    |
| description | string  | false | none |     | 队伍介绍   |

<h2 id="tocS_ListFormResponseDto">ListFormResponseDto</h2>

<a id="schemalistformresponsedto"></a>
<a id="schema_ListFormResponseDto"></a>
<a id="tocSlistformresponsedto"></a>
<a id="tocslistformresponsedto"></a>

```json
{
  "errorCode": 0,
  "message": "string",
  "data": [
    {
      "buildingId": 0,
      "roomId": 0,
      "message": "string",
      "status": 0
    }
  ]
}
```

### 属性

| 名称           | 类型       | 必选    | 约束   | 中文名 | 说明    |
| ------------ | -------- | ----- | ---- | --- | ----- |
| errorCode    | integer  | true  | none |     | 错误码   |
| message      | string   | true  | none |     | 消息    |
| data         | [object] | true  | none |     | none  |
| » buildingId | integer  | true  | none |     | 楼号    |
| » roomId     | integer  | false | none |     | 分配房间号 |
| » message    | string   | false | none |     | 消息    |
| » status     | integer  | true  | none |     | 状态    |

<h2 id="tocS_FormResponseDto">FormResponseDto</h2>

<a id="schemaformresponsedto"></a>
<a id="schema_FormResponseDto"></a>
<a id="tocSformresponsedto"></a>
<a id="tocsformresponsedto"></a>

```json
{
  "errorCode": 0,
  "message": "string",
  "data": {
    "buildingId": 0,
    "roomId": 0,
    "message": "string",
    "status": 0
  }
}
```

### 属性

| 名称           | 类型      | 必选    | 约束   | 中文名 | 说明    |
| ------------ | ------- | ----- | ---- | --- | ----- |
| errorCode    | integer | true  | none |     | 错误码   |
| message      | string  | true  | none |     | 消息    |
| data         | object  | true  | none |     | 数据    |
| » buildingId | integer | true  | none |     | 楼号    |
| » roomId     | integer | false | none |     | 分配房间号 |
| » message    | string  | false | none |     | 消息    |
| » status     | integer | true  | none |     | 状态    |

<h2 id="tocS_UserHasBedResponseDto">UserHasBedResponseDto</h2>

<a id="schemauserhasbedresponsedto"></a>
<a id="schema_UserHasBedResponseDto"></a>
<a id="tocSuserhasbedresponsedto"></a>
<a id="tocsuserhasbedresponsedto"></a>

```json
{
  "errorCode": 0,
  "message": "string",
  "data": {
    "name": "string"
  }
}
```

### 属性

| 名称        | 类型      | 必选   | 约束   | 中文名 | 说明   |
| --------- | ------- | ---- | ---- | --- | ---- |
| errorCode | integer | true | none |     | 错误码  |
| message   | string  | true | none |     | 消息   |
| data      | object  | true | none |     | none |
| » name    | string  | true | none |     | 名字   |

<h2 id="tocS_CreateFormDto">CreateFormDto</h2>

<a id="schemacreateformdto"></a>
<a id="schema_CreateFormDto"></a>
<a id="tocScreateformdto"></a>
<a id="tocscreateformdto"></a>

```json
{
  "buildingId": 0
}
```

### 属性

| 名称         | 类型      | 必选   | 约束   | 中文名 | 说明   |
| ---------- | ------- | ---- | ---- | --- | ---- |
| buildingId | integer | true | none |     | 宿舍楼号 |

<h2 id="tocS_ListQueryGroupResponseDto">ListQueryGroupResponseDto</h2>

<a id="schemalistquerygroupresponsedto"></a>
<a id="schema_ListQueryGroupResponseDto"></a>
<a id="tocSlistquerygroupresponsedto"></a>
<a id="tocslistquerygroupresponsedto"></a>

```json
{
  "errorCode": 0,
  "message": "string",
  "data": [
    {
      "name": "string",
      "description": "string"
    }
  ]
}
```

### 属性

| 名称            | 类型       | 必选    | 约束   | 中文名 | 说明   |
| ------------- | -------- | ----- | ---- | --- | ---- |
| errorCode     | integer  | true  | none |     | 错误码  |
| message       | string   | true  | none |     | 信息   |
| data          | [object] | true  | none |     | none |
| » name        | string   | true  | none |     | 队伍名称 |
| » description | string   | false | none |     | 队伍介绍 |

<h2 id="tocS_QueryGroupResponseDto">QueryGroupResponseDto</h2>

<a id="schemaquerygroupresponsedto"></a>
<a id="schema_QueryGroupResponseDto"></a>
<a id="tocSquerygroupresponsedto"></a>
<a id="tocsquerygroupresponsedto"></a>

```json
{
  "errorCode": 0,
  "message": "string",
  "data": {
    "name": "string",
    "description": "string"
  }
}
```

### 属性

| 名称            | 类型      | 必选    | 约束   | 中文名 | 说明   |
| ------------- | ------- | ----- | ---- | --- | ---- |
| errorCode     | integer | true  | none |     | 错误码  |
| message       | string  | true  | none |     | 信息   |
| data          | object  | true  | none |     | none |
| » name        | string  | true  | none |     | 队伍名称 |
| » description | string  | false | none |     | 队伍介绍 |

<h2 id="tocS_JoinGroupDto">JoinGroupDto</h2>

<a id="schemajoingroupdto"></a>
<a id="schema_JoinGroupDto"></a>
<a id="tocSjoingroupdto"></a>
<a id="tocsjoingroupdto"></a>

```json
{
  "groupNumber": "string",
  "inviteCode": "string"
}
```

### 属性

| 名称          | 类型     | 必选   | 约束   | 中文名 | 说明   |
| ----------- | ------ | ---- | ---- | --- | ---- |
| groupNumber | string | true | none |     | 队伍编号 |
| inviteCode  | string | true | none |     | 邀请码  |

<h2 id="tocS_CreateGroupResponseDto">CreateGroupResponseDto</h2>

<a id="schemacreategroupresponsedto"></a>
<a id="schema_CreateGroupResponseDto"></a>
<a id="tocScreategroupresponsedto"></a>
<a id="tocscreategroupresponsedto"></a>

```json
{
  "errorCode": 0,
  "message": "string",
  "data": {
    "groupNumber": "string",
    "creatorId": 0,
    "name": "string",
    "inviteCode": "string",
    "description": "string",
    "Id": 0
  }
}
```

### 属性

| 名称            | 类型      | 必选    | 约束   | 中文名 | 说明     |
| ------------- | ------- | ----- | ---- | --- | ------ |
| errorCode     | integer | true  | none |     | 错误码    |
| message       | string  | true  | none |     | 消息     |
| data          | object  | true  | none |     | none   |
| » groupNumber | string  | true  | none |     | 队伍编号   |
| » creatorId   | integer | true  | none |     | 创建者 id |
| » name        | string  | true  | none |     | 队伍名称   |
| » inviteCode  | string  | true  | none |     | 邀请码    |
| » description | string  | false | none |     | 队伍介绍   |
| » Id          | integer | true  | none |     | 主键     |

<h2 id="tocS_CreateGroupDto">CreateGroupDto</h2>

<a id="schemacreategroupdto"></a>
<a id="schema_CreateGroupDto"></a>
<a id="tocScreategroupdto"></a>
<a id="tocscreategroupdto"></a>

```json
{
  "name": "string",
  "description": "string"
}
```

### 属性

| 名称          | 类型          | 必选   | 约束   | 中文名 | 说明   |
| ----------- | ----------- | ---- | ---- | --- | ---- |
| name        | string      | true | none |     | 队伍名称 |
| description | string¦null | true | none |     | 队伍介绍 |

<h2 id="tocS_UpdatePasswordDto">UpdatePasswordDto</h2>

<a id="schemaupdatepassworddto"></a>
<a id="schema_UpdatePasswordDto"></a>
<a id="tocSupdatepassworddto"></a>
<a id="tocsupdatepassworddto"></a>

```json
{
  "oldPassword": "string",
  "newPassword": "string"
}
```

### 属性

| 名称          | 类型     | 必选   | 约束   | 中文名 | 说明   |
| ----------- | ------ | ---- | ---- | --- | ---- |
| oldPassword | string | true | none |     | 原始密码 |
| newPassword | string | true | none |     | 新密码  |

<h2 id="tocS_UserResponseDto">UserResponseDto</h2>

<a id="schemauserresponsedto"></a>
<a id="schema_UserResponseDto"></a>
<a id="tocSuserresponsedto"></a>
<a id="tocsuserresponsedto"></a>

```json
{
  "errorCode": 0,
  "message": "string",
  "data": {
    "name": "string",
    "gender": 0,
    "email": "string",
    "telephone": "string"
  }
}
```

### 属性

| 名称          | 类型      | 必选   | 约束   | 中文名 | 说明   |
| ----------- | ------- | ---- | ---- | --- | ---- |
| errorCode   | integer | true | none |     | 错误码  |
| message     | string  | true | none |     | 消息   |
| data        | object  | true | none |     | none |
| » name      | string  | true | none |     | 姓名   |
| » gender    | integer | true | none |     | 性别   |
| » email     | string  | true | none |     | 邮箱   |
| » telephone | string  | true | none |     | 电话   |

<h2 id="tocS_RefreshTokenDto">RefreshTokenDto</h2>

<a id="schemarefreshtokendto"></a>
<a id="schema_RefreshTokenDto"></a>
<a id="tocSrefreshtokendto"></a>
<a id="tocsrefreshtokendto"></a>

```json
{
  "refreshToken": "string"
}
```

### 属性

| 名称           | 类型     | 必选   | 约束   | 中文名 | 说明           |
| ------------ | ------ | ---- | ---- | --- | ------------ |
| refreshToken | string | true | none |     | 更新 token 的凭据 |

<h2 id="tocS_UnauthorizedResponseDto">UnauthorizedResponseDto</h2>

<a id="schemaunauthorizedresponsedto"></a>
<a id="schema_UnauthorizedResponseDto"></a>
<a id="tocSunauthorizedresponsedto"></a>
<a id="tocsunauthorizedresponsedto"></a>

```json
{
  "errorCode": 0,
  "message": "string"
}
```

### 属性

| 名称        | 类型      | 必选   | 约束   | 中文名 | 说明  |
| --------- | ------- | ---- | ---- | --- | --- |
| errorCode | integer | true | none |     | 错误码 |
| message   | string  | true | none |     | 消息  |

<h2 id="tocS_UserLoginDto">UserLoginDto</h2>

<a id="schemauserlogindto"></a>
<a id="schema_UserLoginDto"></a>
<a id="tocSuserlogindto"></a>
<a id="tocsuserlogindto"></a>

```json
{
  "username": "string",
  "password": "string"
}
```

### 属性

| 名称       | 类型     | 必选   | 约束   | 中文名 | 说明  |
| -------- | ------ | ---- | ---- | --- | --- |
| username | string | true | none |     | 用户名 |
| password | string | true | none |     | 密码  |

<h2 id="tocS_TokenResponseDto">TokenResponseDto</h2>

<a id="schematokenresponsedto"></a>
<a id="schema_TokenResponseDto"></a>
<a id="tocStokenresponsedto"></a>
<a id="tocstokenresponsedto"></a>

```json
{
  "errorCode": 0,
  "message": "string",
  "data": {
    "token": "string",
    "refreshToken": "string",
    "expiresIn": 0
  }
}
```

### 属性

| 名称             | 类型      | 必选   | 约束   | 中文名 | 说明                     |
| -------------- | ------- | ---- | ---- | --- | ---------------------- |
| errorCode      | integer | true | none |     | 错误码                    |
| message        | string  | true | none |     | 消息                     |
| data           | object  | true | none |     | none                   |
| » token        | string  | true | none |     | 凭证                     |
| » refreshToken | string  | true | none |     | 刷新 token 的凭证，时间通常为 n 天 |
| » expiresIn    | integer | true | none |     | 过期时间，单位：秒，时间通常为 n 小时   |
