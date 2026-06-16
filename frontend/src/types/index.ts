// 用户角色
export const UserRole = {
  USER: 'USER',
  ADMIN: 'ADMIN',
} as const
export type UserRole = (typeof UserRole)[keyof typeof UserRole]

// 文章类型
export const ArticleType = {
  ARTICLE: 'ARTICLE',
  PROJECT: 'PROJECT',
} as const
export type ArticleType = (typeof ArticleType)[keyof typeof ArticleType]

// 用户
export interface User {
  id: number
  username: string
  nickname: string
  avatarUrl: string | null
  role: UserRole
  createdAt: string
}

// 文章
export interface Article {
  id: number
  title: string
  content: string
  summary: string
  coverImage: string | null
  type: ArticleType
  externalLink: string | null
  authorId: number
  authorName: string
  authorAvatar: string | null
  isPublished: boolean
  likeCount: number
  commentCount: number
  isLiked: boolean
  createdAt: string
  updatedAt: string
}

// 评论
export interface Comment {
  id: number
  content: string
  articleId: number
  userId: number
  username: string
  userAvatar: string | null
  parentId: number | null
  createdAt: string
}

// 统一响应
export interface ApiResponse<T = unknown> {
  code: number
  message: string
  data: T
}

// 分页响应
export interface PageResult<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
}

// 登录请求
export interface LoginRequest {
  username: string
  password: string
}

// 登录响应
export interface LoginResponse {
  token: string
  user: User
}

// 注册请求
export interface RegisterRequest {
  username: string
  password: string
  nickname: string
}

// 文章创建/更新请求
export interface ArticleRequest {
  title: string
  content: string
  summary: string
  coverImage?: string
  type: ArticleType
  externalLink?: string
}
