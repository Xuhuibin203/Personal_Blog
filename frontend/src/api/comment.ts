import { get, post, del } from '../utils/request'
import type { Comment, PageResult } from '../types'

export const commentApi = {
  getByArticle(articleId: number, params: { page?: number; size?: number } = {}): Promise<PageResult<Comment>> {
    return get(`/articles/${articleId}/comments`, params as Record<string, unknown>)
  },

  create(articleId: number, content: string, parentId?: number): Promise<Comment> {
    return post(`/articles/${articleId}/comments`, { content, parentId })
  },

  delete(commentId: number): Promise<void> {
    return del(`/comments/${commentId}`)
  },
}
