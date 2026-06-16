<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '../../stores/auth'
import { authApi } from '../../api/auth'
import { UserRole } from '../../types'
import { Lock, User, Eye, EyeOff } from 'lucide-vue-next'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const username = ref('')
const password = ref('')
const loading = ref(false)
const showPassword = ref(false)
const errorMsg = ref('')

async function handleLogin() {
  if (!username.value.trim() || !password.value.trim()) {
    errorMsg.value = '请输入用户名和密码'
    return
  }

  loading.value = true
  errorMsg.value = ''

  try {
    const res = await authApi.login({
      username: username.value.trim(),
      password: password.value,
    })
    authStore.setToken(res.token)
    authStore.setUser(res.user)

    if (res.user.role !== UserRole.ADMIN) {
      authStore.logout()
      errorMsg.value = '此账户无管理权限'
      return
    }

    ElMessage.success('登录成功')
    const redirect = (route.query.redirect as string) || '/admin'
    router.push(redirect)
  } catch {
    errorMsg.value = '用户名或密码错误'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="admin-login-page">
    <div class="login-card">
      <div class="card-header">
        <div class="brand-dot"></div>
        <h1>管理后台</h1>
        <p>请输入管理员账户登录</p>
      </div>

      <form class="login-form" @submit.prevent="handleLogin">
        <div class="input-group">
          <label>用户名</label>
          <div class="input-wrapper">
            <User :size="18" class="input-icon" />
            <input
              v-model="username"
              type="text"
              placeholder="请输入用户名"
              autocomplete="username"
              :disabled="loading"
            />
          </div>
        </div>

        <div class="input-group">
          <label>密码</label>
          <div class="input-wrapper">
            <Lock :size="18" class="input-icon" />
            <input
              v-model="password"
              :type="showPassword ? 'text' : 'password'"
              placeholder="请输入密码"
              autocomplete="current-password"
              :disabled="loading"
            />
            <button
              type="button"
              class="toggle-password"
              @click="showPassword = !showPassword"
            >
              <EyeOff v-if="showPassword" :size="18" />
              <Eye v-else :size="18" />
            </button>
          </div>
        </div>

        <p v-if="errorMsg" class="error-msg">{{ errorMsg }}</p>

        <button type="submit" class="login-btn" :disabled="loading">
          {{ loading ? '登录中...' : '登 录' }}
        </button>
      </form>
    </div>
  </div>
</template>

<style scoped>
.admin-login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--bg);
  padding: 24px;
}

.login-card {
  width: 100%;
  max-width: 420px;
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: var(--radius-xl);
  padding: 48px 40px;
  box-shadow: var(--shadow-lg);
}

.card-header {
  text-align: center;
  margin-bottom: 36px;
}

.brand-dot {
  width: 12px;
  height: 12px;
  background: var(--accent);
  border-radius: 3px;
  margin: 0 auto 16px;
}

.card-header h1 {
  font-family: var(--serif);
  font-size: 24px;
  font-weight: 700;
  color: var(--text);
  margin: 0 0 8px;
}

.card-header p {
  font-size: 13px;
  color: var(--text-muted);
  margin: 0;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.input-group label {
  font-size: 12px;
  font-weight: 600;
  color: var(--text-secondary);
  letter-spacing: 0.02em;
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.input-icon {
  position: absolute;
  left: 14px;
  color: var(--text-muted);
  pointer-events: none;
}

.input-wrapper input {
  width: 100%;
  padding: 13px 14px 13px 42px;
  border: 1.5px solid var(--border);
  border-radius: var(--radius);
  font-size: 15px;
  color: var(--text);
  background: var(--bg);
  outline: none;
  font-family: var(--sans);
  transition: border-color 0.2s;
}

.input-wrapper input:focus {
  border-color: var(--accent);
  box-shadow: 0 0 0 3px var(--accent-glow);
}

.input-wrapper input:disabled {
  opacity: 0.6;
}

.toggle-password {
  position: absolute;
  right: 10px;
  background: none;
  border: none;
  color: var(--text-muted);
  cursor: pointer;
  padding: 4px;
  display: flex;
  align-items: center;
  transition: color 0.15s;
}

.toggle-password:hover {
  color: var(--accent);
}

.error-msg {
  font-size: 13px;
  color: #E05540;
  text-align: center;
  margin: 0;
  padding: 8px 12px;
  background: rgba(224, 85, 64, 0.06);
  border-radius: var(--radius-sm);
}

.login-btn {
  width: 100%;
  padding: 14px;
  background: var(--accent);
  border: none;
  border-radius: var(--radius);
  font-size: 15px;
  font-weight: 600;
  color: #fff;
  cursor: pointer;
  font-family: var(--sans);
  letter-spacing: 0.08em;
  transition: all 0.2s;
  margin-top: 4px;
}

.login-btn:hover:not(:disabled) {
  background: var(--accent-hover);
  transform: translateY(-1px);
  box-shadow: 0 4px 16px var(--accent-glow);
}

.login-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
