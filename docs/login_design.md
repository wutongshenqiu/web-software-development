## 背景知识：OAuth 2.0

**OAuth 是一种授权机制。数据的所有者告诉系统，同意授权第三方应用进入系统，获取这些数据。系统从而产生一个短期的进入令牌(token)，用来代替密码，供第三方应用使用**

根据上述定义，令牌(token)与密码(password)具有如下区别

1. 令牌是短期的，到期会自动失效，用户无法自己修改

2. 令牌可以被所有者撤销
   
   > **这也是令牌和 jwt 的不同之处**

3. 令牌有权限范围

[RFC 6749](https://www.rfc-editor.org/rfc/rfc6749) 定义了四种获得令牌的流程

### 获取令牌的流程

#### 授权码(authorization-code)

> 第三方应用先申请一个授权码，再用该授权码获取令牌

![](https://www.wangbase.com/blogimg/asset/201904/bg2019040905.jpg)

1. A 网站提供链接，用户点击后跳转到 B 网站，授权用户数据给 A 网站使用

2. B 网站要求用户登录，询问是否同意给与 A 权限，若同意，则 B 返回授权码给 A

3. A 拿到授权码之后，可以向 B 网站请求 token

4. B 收到请求并校验授权码之后，颁发令牌

#### 隐藏式(implicit)

> 此方式没有授权码的步骤，允许向前端直接办法令牌

![](https://www.wangbase.com/blogimg/asset/201904/bg2019040906.jpg)

1. A 网站提供一个链接，要求用户跳转到 B 网站，授权用户数据给 A 网站使用

2. 用户跳转到 B 网站，登陆后同意给予 A 网站权限，B 网站将令牌直接传递给 A 网站

#### 密码式(password)

> 用户直接将用户名和密码告诉该应用，该应用直接使用密码申请令牌

1. A 网站要求用户提供 B 网站的用户名和密码，拿到以后 A 网站直接向 B 请求令牌

2. B 网站验证身份通过后，将令牌直接通过 HTTP 相应传递给 A

#### 凭证式(client credentials)

> 类似于密码式

## 方式一：双 token 验证机制

> 实际上是对 OAuth 2.0 密码式的模仿

根据 [RFC 6749](https://datatracker.ietf.org/doc/html/rfc6749#section-1.4) 给出如下两个 token 的定义

- **Access Token**
  
  > 用于访问受保护资源的凭据

- **Refresh Token**
  
  > 用于获取 Access Token 的凭据
  
  - 只会用于授权服务器(authorization servers)，不会发送到资源服务器(resource servers)

当 Access Token 过期时，其刷新流程如下图所示

主要包括

1. 用户登陆时授权服务器返回 Access Token 和 Refresh Token

2. 用户使用 Access Token 访问资源服务器

3. Access Token 过期或者非法时，用户携带 Refresh Token 请求授权服务器返回新的 Access Token

4. 授权服务器返回新的 Access Token 以及新的 Refresh Token(可选的)

一般而言，需要经常传输的 Access Token 具有较短的过期时间，而仅用于更新 Access Token 的 Refresh Token 过期时间较长，这种区分 Access Token 和 Refresh Token 的方式，在一定程度上平衡了用户体验和程序设计的安全性

### Refresh Token 的设计

在上述流程的第四点中，涉及到 Refresh Token 能否刷新自己的问题

#### Refresh Token 能够刷新自己

**优点**

- 能够做到用户 Access Token 和 Refresh Token 的无感更新

**缺点**

- 无法支持多设备登录？

#### Refresh Token 不能刷新自己

**优点**

- 可支持多设备登录？

**缺点**

- Refresh Token 过期之后仍然需要重新登陆

- 如果 Refresh Token 被泄露(尽管可能性较小)，则用户将在很长一段时间内(直到 Refresh Token 过期)受到安全威胁

### 总结

#### 优点

- 当 Refresh Token 能刷新自己时，可以做到 Access Token 的无感更新，提高用户的使用体验

- Access Token 用于访问敏感资源，Refresh Token 则用于刷新 Access Token，两者的分开使用进一步提高了系统的安全性

#### 缺点

- 该模式本质上是参考的 OAuth 2.0，但 OAuth 2.0 实际上是**一种授权机制**，一般需要有三方实体(用户(数据所有者)、服务器A、服务器B)参与的模型，且应该是 **用户向服务器B授权服务器A对(用户)资源的访问**(例如用户向微信授权某小程序对其手机号的访问权限)，似乎并不适用于当前的模式

- token 的是否有状态难以界定。由于需要做到 token 的刷新，因此需要(在内存或者数据库中)记录 token 的相关信息，即不是传统的无状态的 jwt 验证模式

- token 中是否应该包含用户信息难以界定。如果不包含用户信息，则仍然需要通过 session 的方式将用户和 token 相对应；如果包含用户信息，则需要对信息进行加密或者签名，增大了运算量。同时违背了 jwt 无状态的设计

## 方式二：基于可管理 Session 的验证机制

> 一个用户有一个 Session，在 session 中可以有多个 token(session id)，每一个 token 是一个随机字符串，每一个 token 对应于一台设备，一台设备只同时在一个 ip 上登陆(即后一个登录的 ip 会挤掉当前设备)

### 流程

1. 用户通过用户名和密码进行登录认证

2. 认证成功后，服务器记录当前设备的 ip 地址和设备类型，返回一个对应的 token

3. 用户每次访问受限制的资源时，检查其 ip 和设备类型，对于同一个设备，如果 ip 与前一次访问不同，则令之前的 token 失效，并重新生成一个 token

4. 用户可以查看其 session 的所有 token(对应 ip + 设备类型)

5. 用户可以将指定 token 踢下线

### 说明

- Session 与用户 id 绑定，可以为不同的 device 分配不同的 token 实现多设备登录

- 每一个 token 与 (ip, device) 绑定，恶意攻击者需要同时拥有用户的 token、公网 ip 的权限和 device 才能伪造该用户

- 用户可以将指定 token 踢下线，被踢下线的 (ip + 设备) 需要重新登录

## 总结

### 优点

- 本质上是基于 session 的机制，数据量和计算量小于 jwt 的相关计算

- 可实现多/单设备登陆

- 相对安全。一个设备对应一个 ip 地址，切换 ip 地址时，会更新对应的 token，将原设备挤下线

### 缺点

- 需要保存用户的 ip 数据，且每一次用户访问受保护的资源时，需要与前一次的 ip 地址进行比对

综合以上两种方法，本系统目前采用方式二实现用户的登录认证等相关操作



## 方法三：API 网关？





## Refs

[OAuth 2.0 的一个简单解释 - 阮一峰的网络日志](https://www.ruanyifeng.com/blog/2019/04/oauth_design.html)

[OAuth 2.0 的四种方式 - 阮一峰的网络日志](https://www.ruanyifeng.com/blog/2019/04/oauth-grant-types.html)

[RFC 6749: The OAuth 2.0 Authorization Framework](https://www.rfc-editor.org/rfc/rfc6749)
