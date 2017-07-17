一个用于 CDE 环境的演示项目，采用 CDE 的 spring-boot 技术栈构建。

## 项目部署

### 1. 注册 cde

```
$ cde register http://controller.cnup.io
```

### 2. 登录 cde 并添加 key

```
$ cde login http://controller.cnup.io
$ cde keys:add ~/.ssh/id_rsa.pub
```

其中不同的操作系统下添加 key 的目录可能不同，请根据自己的实际情况添加

### 3. 获取项目

```
$ git clone https://github.com/aisensiy/spring-boot-petstore.git
$ cd spring-boot-petstore
```

### 4. 采用 spring-boot 技术栈创建项目

```
$ cde apps:create <your-app-name> spring-boot
```

通过 `git remote -v` 命令可以看到当前的 git 仓库多了 `cde` 的源：

```
$ git remote -v
cde	ssh://git@controller.cnup.io:2222/petstore-backend.git (fetch)
cde	ssh://git@controller.cnup.io:2222/petstore-backend.git (push)
origin	git@github.com:aisensiy/spring-boot-petstore.git (fetch)
origin	git@github.com:aisensiy/spring-boot-petstore.git (push)
```

### 5. 部署项目

通过将项目提交到 `cde` 的源实现部署

```
$ git push cde master
```
    
### 6. 查看项目状态

在部署完成后可以通过 `cde apps:info` 命令查看当前应用状态：

```
$ cde apps:info

--- petstore-backend Application
+------------+------------------+
| ID         | petstore-backend |
| Stack Name | spring-boot      |
+------------+------------------+
--- Access routes:

petstore-backend-web.cnup.io/ 

--- Dependent services:
-----> Service 1:
+-----------+------+
| ID        | 1057 |
| Name      | web  |
| Instances | 1    |
| Memory    | 512  |
| Env       | {}   |
+-----------+------+
```

### 7. 在浏览器访问 API 数据

在浏览器中输入 `6` 中所提供的路由作为入口，访问路径 `/pets` 可以看到目前的宠物数据。

[http://petstore-backend-web.cnup.io/pets](http://petstore-backend-web.cnup.io/pets)
