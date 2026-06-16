import { put } from '../utils/request'
import type { User } from '../types'

export const userApi = {
  updateAvatar(formData: FormData): Promise<User> {
    return put('/user/avatar', formData)
  },

  updateProfile(data: { nickname?: string }): Promise<User> {
    return put('/user/profile', data)
  },

  changePassword(data: { oldPassword: string; newPassword: string }): Promise<void> {
    return put('/user/password', data)
  },
}
