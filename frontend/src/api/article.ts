import { get, post, put, del, upload } from '../utils/request'
import type { Article, ArticleRequest, PageResult } from '../types'

export const articleApi = {
  getList(params: { page?: number; size?: number; type?: string } = {}): Promise<PageResult<Article>> {
    return get('/articles', params as Record<string, unknown>)
  },

  getById(id: number): Promise<Article> {
    return get(`/articles/${id}`)
  },

  create(data: ArticleRequest): Promise<Article> {
    return post('/articles', data)
  },

  update(id: number, data: ArticleRequest): Promise<Article> {
    return put(`/articles/${id}`, data)
  },

  delete(id: number): Promise<void> {
    return del(`/articles/${id}`)
  },

  uploadImage(formData: FormData): Promise<{ url: string }> {
    return upload('/upload', formData)
  },

  uploadImages(formData: FormData): Promise<{ urls: string[] }> {
    return upload('/upload/batch', formData)
  },
}
