# Personal Blog — 项目开发日志

> 最后更新：2026-10-21  
> 项目状态：**核心功能已完成，前后端均已构建打包**

---

## 一、项目概述

个人博客系统，支持文章/项目发布、评论、点赞、用户管理等功能。前端采用杂志风暖色大地色系设计，后端提供 RESTful API。

- **博主**：Season（许辉彬），ACMer · 信奥教师 · 游戏开发者
- **GitHub**：Xuhuibin203

---

## 二、技术栈

### 后端
| 技术 | 版本/说明 |
|------|-----------|
| Spring Boot | 4.1.0 |
| Java | 21 |
| Spring Data JPA | Hibernate，ddl-auto: update |
| Spring Security | 无状态 JWT 认证 |
| MySQL | 8.x，库名 `personal_blog` |
| JJWT | 0.12.6 |
| 文件上传 | 本地磁盘 `./uploads`（生产：`/www/wwwroot/uploads`）|

### 前端
| 技术 | 版本/说明 |
|------|-----------|
| Vue | 3.5.x（Composition API + `<script setup>`）|
| TypeScript | 6.0 |
| Vite | 8.x |
| Pinia | 3.x（状态管理）|
| Vue Router | 4.x（history 模式）|
| Element Plus | 2.x（仅用于 Message/MessageBox 弹窗）|
| Axios | 1.x（HTTP 请求）|
| Lucide Vue Next | 图标库 |
| marked | 18.x（Markdown 渲染）|

---

## 三、项目结构

```
Personal_Blog/
├── backend/Personal_Blog/          # Spring Boot 后端
│   ├── src/main/java/com/example/personal_blog/
│   │   ├── common/ApiResponse.java         # 统一响应封装 {code, message, data}
│   │   ├── config/
│   │   │   ├── SecurityConfig.java         # Spring Security + JWT + CORS 配置
│   │   │   └── WebMvcConfig.java           # 静态资源映射 /uploads/** → 磁盘目录
│   │   ├── controller/
│   │   │   ├── AuthController.java         # 登录/注册/获取当前用户
│   │   │   ├── ArticleController.java      # 文章 CRUD + 点赞
│   │   │   ├── CommentController.java      # 评论 CRUD
│   │   │   ├── UploadController.java       # 图片上传（单张/批量）
│   │   │   └── UserController.java         # 头像/昵称/密码修改
│   │   ├── model/
│   │   │   ├── entity/                     # JPA 实体：User, Article, Comment, LikeRecord
│   │   │   ├── dto/                        # 请求 DTO：LoginRequest, RegisterRequest, ArticleRequest, CommentRequest
│   │   │   └── enums/                      # 枚举：UserRole(USER/ADMIN), ArticleType(ARTICLE/PROJECT)
│   │   ├── repository/                     # JPA Repository 接口
│   │   ├── security/
│   │   │   ├── JwtUtil.java                # JWT 生成/解析/验证
│   │   │   └── JwtAuthFilter.java          # JWT 认证过滤器
│   │   ├── service/                        # 业务逻辑层
│   │   └── PersonalBlogApplication.java    # 启动类
│   ├── src/main/resources/
│   │   └── application.yaml                # 开发环境配置
│   ├── application-prod.yaml               # 生产环境配置
│   └── pom.xml
│
└── frontend/                       # Vue 3 前端
    ├── src/
    │   ├── api/                    # API 封装（article, auth, comment, like, user）
    │   ├── components/             # 公共组件（Navbar, Sidebar, ArticleCard, PersonalProfile）
    │   ├── router/index.ts         # 路由配置 + 守卫
    │   ├── stores/auth.ts          # Pinia 认证状态
    │   ├── types/index.ts          # TypeScript 类型定义
    │   ├── utils/request.ts        # Axios 封装（拦截器 + 方法封装）
    │   ├── views/                  # 页面视图
    │   │   ├── Home.vue            # 首页（GitHub 贡献图 + 精选卡片 + 个人简历）
    │   │   ├── ArticlesPage.vue    # 文章列表（分页）
    │   │   ├── ProjectsPage.vue    # 项目列表（分页）
    │   │   ├── ArticleDetail.vue   # 文章详情（Markdown 渲染 + 评论展示）
    │   │   ├── Login.vue           # 登录/注册页
    │   │   ├── Profile.vue         # 个人中心（头像/昵称/密码管理）
    │   │   └── admin/
    │   │       ├── AdminLogin.vue  # 管理员登录
    │   │       ├── Dashboard.vue   # 管理后台（文章列表管理）
    │   │       └── ArticleEditor.vue # 文章编辑器（Markdown + 图片上传）
    │   ├── App.vue                 # 根组件（布局：侧边栏+内容区 / 管理后台独立布局）
    │   ├── main.ts                 # 入口
    │   └── style.css               # 全局样式（CSS 变量设计系统）
    ├── dist/                       # 已构建的静态文件
    └── vite.config.ts              # Vite 配置（代理 /api → :8080, /uploads → :8080）
```

---

## 四、数据库设计

### users 表
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 自增主键 |
| username | VARCHAR(50) UNIQUE | 用户名 |
| password | VARCHAR | BCrypt 加密密码 |
| nickname | VARCHAR(50) | 昵称 |
| avatar_url | VARCHAR | 头像路径 |
| role | ENUM('USER','ADMIN') | 角色，默认 USER |
| created_at / updated_at | DATETIME | 时间戳 |

### articles 表
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 自增主键 |
| title | VARCHAR(100) | 标题 |
| content | TEXT | 正文（Markdown）|
| summary | VARCHAR(200) | 摘要 |
| cover_image | VARCHAR | 封面图路径 |
| type | ENUM('ARTICLE','PROJECT') | 内容类型 |
| external_link | VARCHAR | 外部链接（项目用）|
| author_id | BIGINT FK → users | 作者 |
| is_published | BOOLEAN | 是否发布，默认 true |
| like_count | INT | 点赞数，默认 0 |
| comment_count | INT | 评论数，默认 0 |
| created_at / updated_at | DATETIME | 时间戳 |

### comments 表
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 自增主键 |
| content | TEXT | 评论内容 |
| article_id | BIGINT FK → articles | 所属文章 |
| user_id | BIGINT FK → users | 评论者 |
| parent_id | BIGINT | 父评论 ID（回复功能预留）|
| created_at | DATETIME | 创建时间 |

### like_records 表
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 自增主键 |
| article_id | BIGINT FK → articles | 文章 |
| user_id | BIGINT FK → users | 用户 |
| created_at | DATETIME | 创建时间 |
| **UNIQUE(article_id, user_id)** | | 每人每篇只能赞一次 |

---

## 五、API 接口清单

### 认证模块 `/api/auth`
| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| POST | /api/auth/login | 公开 | 登录，返回 JWT token + 用户信息 |
| POST | /api/auth/register | 公开 | 注册，返回 JWT token + 用户信息 |
| GET | /api/auth/me | 登录 | 获取当前用户信息 |

### 文章模块 `/api/articles`
| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | /api/articles | 公开 | 分页获取文章列表（可选 type 过滤）|
| GET | /api/articles/{id} | 公开 | 获取文章详情 |
| POST | /api/articles | ADMIN | 创建文章 |
| PUT | /api/articles/{id} | ADMIN | 更新文章 |
| DELETE | /api/articles/{id} | ADMIN | 删除文章 |
| POST | /api/articles/{id}/like | 登录 | 切换点赞状态（toggle）|
| DELETE | /api/articles/{id}/like | 登录 | 取消点赞 |

### 评论模块
| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| GET | /api/articles/{articleId}/comments | 公开 | 分页获取文章评论 |
| POST | /api/articles/{articleId}/comments | 登录 | 发表评论 |
| DELETE | /api/comments/{commentId} | 登录 | 删除评论（作者或管理员）|

### 用户模块 `/api/user`
| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| PUT | /api/user/avatar | 登录 | 上传头像 |
| PUT | /api/user/profile | 登录 | 修改昵称 |
| PUT | /api/user/password | 登录 | 修改密码 |

### 上传模块 `/api/upload`
| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| POST | /api/upload | 登录 | 上传单张图片 |
| POST | /api/upload/batch | 登录 | 批量上传图片 |

### 静态资源
| 路径 | 说明 |
|------|------|
| /uploads/** | 上传文件（头像、文章图片），公开访问 |

---

## 六、前端页面与路由

| 路由 | 页面 | 说明 |
|------|------|------|
| `/` | Home.vue | 首页：GitHub 贡献图 + 精选文章/项目卡片（随机4张）+ 个人简历 |
| `/articles` | ArticlesPage.vue | 文章列表（type=ARTICLE，分页）|
| `/projects` | ProjectsPage.vue | 项目列表（type=PROJECT，分页）|
| `/article/:id` | ArticleDetail.vue | 文章详情（Markdown 渲染 + 评论列表）|
| `/login` | Login.vue | 登录/注册切换页 |
| `/profile` | Profile.vue | 个人中心（头像上传、昵称编辑、密码修改）|
| `/admin/login` | AdminLogin.vue | 管理员登录 |
| `/admin` | Dashboard.vue | 管理后台：文章列表管理（需 ADMIN 角色）|
| `/admin/article/new` | ArticleEditor.vue | 新建文章（需 ADMIN 角色）|
| `/admin/article/:id/edit` | ArticleEditor.vue | 编辑文章（需 ADMIN 角色）|

**布局说明**：
- 非 admin 路由：左侧 Sidebar（个人信息+导航+GitHub链接）+ 右侧内容区
- admin 路由：独立全宽布局，无侧边栏
- 顶部固定 Navbar（毛玻璃效果）

---

## 七、设计系统

### 色彩（暖色大地色系）
```css
--bg: #FAF7F2           /* 主背景 */
--surface: #FFFFFF      /* 卡片背景 */
--accent: #D4784C       /* 主色调（暖橙） */
--text: #3D2C20         /* 主文字 */
--text-secondary: #8B7355
--text-muted: #A89880
--border: rgba(139, 115, 85, 0.12)
```

### 字体
- **正文**：Inter / PingFang SC / sans-serif
- **标题**：Noto Serif SC / Songti SC（衬线体，杂志风）
- **代码**：SF Mono / Cascadia Code / Consolas

### 风格特征
- 杂志风排版（衬线标题 + 首字下沉 + 装饰分隔线）
- 毛玻璃导航栏（backdrop-filter）
- 卡片式布局 + 圆角 + 微妙阴影
- 响应式适配（768px 断点）

---

## 八、认证与安全

- **认证方式**：JWT（Header: `Authorization: Bearer <token>`）
- **Token 存储**：localStorage
- **密码加密**：BCrypt
- **权限模型**：
  - `USER`：浏览、评论、点赞、管理个人资料
  - `ADMIN`：USER 权限 + 文章 CRUD、删除评论
- **CORS**：允许所有来源（开发阶段）
- **CSRF**：已禁用（无状态 API）
- **会话**：STATELESS

---

## 九、关键实现细节

### 后端
1. **点赞 Toggle 机制**：`POST /articles/{id}/like` 自动判断是点赞还是取消点赞，处理了并发唯一约束冲突
2. **评论计数**：发评论/删评论时自动同步更新文章的 `commentCount`
3. **文件上传**：使用绝对路径避免相对路径解析到 Tomcat 临时目录；文件名格式 `UUID_timestamp.ext`
4. **头像管理**：上传新头像时自动删除旧头像文件
5. **文章排序**：按创建时间倒序
6. **Service 返回 Map**：ArticleService/CommentService/UserService 直接构建 `Map<String, Object>` 返回，未使用 DTO 响应对象

### 前端
1. **请求封装**：Axios 拦截器自动附加 Token、统一错误处理、401 自动跳转登录
2. **路由守卫**：admin 路由需要登录 + ADMIN 角色，自动 fetchUser 验证
3. **Markdown 渲染**：使用 `marked` 库，支持 GFM + 换行
4. **图片上传**：编辑器支持多张图片批量上传，自动转为 Markdown 语法插入正文
5. **首页精选**：Fisher-Yates 洗牌算法随机选取文章/项目卡片
6. **GitHub 贡献图**：使用 `ghchart.rshah.org` 第三方服务

---

## 十、构建与部署

### 开发环境
```bash
# 后端（端口 8080）
cd backend/Personal_Blog
./mvnw spring-boot:run

# 前端（端口 3000，代理到 8080）
cd frontend
npm run dev
```

### 生产环境
```bash
# 后端打包
cd backend/Personal_Blog
./mvnw clean package -Dskip
# 产物：target/Personal_Blog-0.0.1-SNAPSHOT.jar
# 启动：java -jar xxx.jar --spring.profiles.active=prod

# 前端打包
cd frontend
npm run build
# 产物：dist/ 目录
```

### 生产配置（application-prod.yaml）
- 后端端口：8082
- 数据库用户：personal_blog
- JWT 密钥：已更换为生产密钥
- 上传路径：`/www/wwwroot/uploads`

### 已知部署注意事项
1. **Vue Router history 模式**：Nginx 需配置 `try_files $uri $uri/ /index.html` 否则刷新 404
2. **上传文件代理**：Nginx 需代理 `/uploads` 路径到后端
3. **PowerShell 中 Maven 参数**：`-Dskip` 需用引号包裹 `"-Dskip"`
4. **PowerShell 中 curl**：是 `Invoke-WebRequest` 的别名，注意语法差异

---

## 十一、已完成功能清单

- [x] 用户注册/登录（JWT）
- [x] 文章 CRUD（管理员）
- [x] 文章列表分页 + 类型筛选
- [x] 文章详情页（Markdown 渲染）
- [x] 评论系统（发表/删除/展示）
- [x] 点赞系统（toggle / 取消）
- [x] 图片上传（单张/批量）
- [x] 用户头像上传/更换
- [x] 用户昵称修改
- [x] 用户密码修改
- [x] 管理后台（文章管理 Dashboard）
- [x] 文章编辑器（Markdown + 封面图 + 图片插入）
- [x] 首页（GitHub 贡献图 + 精选卡片 + 个人简历）
- [x] 项目列表页
- [x] 个人中心页
- [x] 响应式移动端适配
- [x] 路由守卫（admin 权限校验）
- [x] 统一 API 错误处理
- [x] 生产环境配置

---

## 十二、待完善 / 可扩展方向

- [ ] 评论的回复功能（parentId 已预留，前端未实现嵌套展示）
- [ ] 文章搜索功能
- [ ] 文章标签/分类系统
- [ ] 暗色模式
- [ ] 用户列表/其他用户主页
- [ ] 文章草稿功能（isPublished 字段已预留）
- [ ] 富文本编辑器替换纯 Markdown 文本框
- [ ] 评论的实时通知
- [ ] 前端单元测试
- [ ] 后端单元测试
- [ ] Docker 容器化部署
- [ ] Nginx 配置文件归档

---

## 十三、快速上手指南（新对话）

在新对话中继续开发时，可以提供以下上下文：

> 这是一个个人博客项目，前后端分离架构。
> - 后端：Spring Boot 4.1 + JPA + MySQL + JWT，端口 8080（生产 8082）
> - 前端：Vue 3 + TypeScript + Pinia + Element Plus + Lucide 图标，端口 3000
> - 数据库表：users, articles, comments, like_records
> - 核心功能已全部完成（文章/评论/点赞/用户/管理后台）
> - 前端已构建到 dist/，后端已打包为 jar
> - 详细开发日志见项目根目录 `DEV_LOG.md`
> - 项目规则：图标从 lucide 选取，后端端口 8082，可用 Pinia/element
