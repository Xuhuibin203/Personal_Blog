import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { User } from '../types'
import { UserRole } from '../types'
import { authApi } from '../api/auth'

export const useAuthStore = defineStore('auth', () => {
  const user = ref<User | null>(null)
  const token = ref<string>(localStorage.getItem('token') || '')

  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => user.value?.role === UserRole.ADMIN)

  function setToken(newToken: string) {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  function setUser(newUser: User) {
    user.value = newUser
  }

  function logout() {
    token.value = ''
    user.value = null
    localStorage.removeItem('token')
  }

  async function fetchUser() {
    if (!token.value) return
    try {
      const u = await authApi.getMe()
      user.value = u
    } catch {
      logout()
    }
  }

  return { user, token, isLoggedIn, isAdmin, setToken, setUser, logout, fetchUser }
})
