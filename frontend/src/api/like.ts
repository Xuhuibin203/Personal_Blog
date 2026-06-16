import { post, del } from '../utils/request'

export const likeApi = {
  toggle(articleId: number): Promise<{ liked: boolean; count: number }> {
    return post(`/articles/${articleId}/like`)
  },

  remove(articleId: number): Promise<{ liked: boolean; count: number }> {
    return del(`/articles/${articleId}/like`)
  },
}
