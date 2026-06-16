import { post, get } from '../utils/request'
import type { LoginRequest, LoginResponse, RegisterRequest, User } from '../types'

export const authApi = {
  login(data: LoginRequest): Promise<LoginResponse> {
    return post('/auth/login', data)
  },

  register(data: RegisterRequest): Promise<LoginResponse> {
    return post('/auth/register', data)
  },

  getMe(): Promise<User> {
    return get('/auth/me')
  },
}
