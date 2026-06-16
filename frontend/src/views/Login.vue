<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { authApi } from '../api/auth'
import { ElMessage } from 'element-plus'
import { LogIn, UserPlus } from 'lucide-vue-next'

const router = useRouter()
const authStore = useAuthStore()

const isLogin = ref(true)
const loading = ref(false)

const form = reactive({
  username: '',
  password: '',
  nickname: '',
})

async function handleSubmit() {
  if (!form.username || !form.password) {
    ElMessage.warning('请填写用户名和密码')
    return
  }
  if (!isLogin.value && !form.nickname) {
    ElMessage.warning('请填写昵称')
    return
  }

  loading.value = true
  try {
    if (isLogin.value) {
      const res = await authApi.login({ username: form.username, password: form.password })
      authStore.setToken(res.token)
      authStore.setUser(res.user)
      ElMessage.success('登录成功')
    } else {
      const res = await authApi.register({ username: form.username, password: form.password, nickname: form.nickname })
      authStore.setToken(res.token)
      authStore.setUser(res.user)
      ElMessage.success('注册成功')
    }
    router.push('/')
  } catch {
    // 错误已由拦截器处理
  } finally {
    loading.value = false
  }
}

function toggleMode() {
  isLogin.value = !isLogin.value
  form.nickname = ''
}
</script>

<template>
  <div class="login-page">
    <div class="login-card">
      <div class="login-header">
        <div class="login-icon">
          <LogIn v-if="isLogin" :size="28" />
          <UserPlus v-else :size="28" />
        </div>
        <h2>{{ isLogin ? '欢迎回来' : '创建账户' }}</h2>
        <p class="login-subtitle">{{ isLogin ? '登录你的个人主页' : '注册一个新账户' }}</p>
      </div>

      <form class="login-form" @submit.prevent="handleSubmit">
        <div class="form-group">
          <label>用户名</label>
          <input v-model="form.username" type="text" placeholder="请输入用户名" />
        </div>

        <div class="form-group">
          <label>密码</label>
          <input v-model="form.password" type="password" placeholder="请输入密码" />
        </div>

        <div v-if="!isLogin" class="form-group">
          <label>昵称</label>
          <input v-model="form.nickname" type="text" placeholder="请输入昵称" />
        </div>

        <button type="submit" class="submit-btn" :disabled="loading">
          {{ loading ? '处理中...' : (isLogin ? '登录' : '注册') }}
        </button>
      </form>

      <div class="login-footer">
        <span>{{ isLogin ? '还没有账户？' : '已有账户？' }}</span>
        <button class="toggle-btn" @click="toggleMode">
          {{ isLogin ? '立即注册' : '去登录' }}
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 200px);
}

.login-card {
  background: var(--surface);
  border-radius: var(--radius-xl);
  padding: 48px;
  width: 100%;
  max-width: 420px;
  box-shadow: var(--shadow);
  border: 1px solid var(--border);
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
}

.login-icon {
  width: 60px;
  height: 60px;
  border-radius: var(--radius);
  background: var(--accent-subtle);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
  color: var(--accent);
}

.login-header h2 {
  font-family: var(--serif);
  font-size: 24px;
  font-weight: 700;
  color: var(--text);
  margin-bottom: 6px;
}

.login-subtitle {
  font-size: 14px;
  color: var(--text-muted);
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.form-group label {
  font-size: 12px;
  font-weight: 600;
  color: var(--text-secondary);
  letter-spacing: 0.02em;
  text-transform: uppercase;
}

.form-group input {
  padding: 13px 16px;
  border: 1.5px solid var(--border);
  border-radius: 10px;
  font-size: 15px;
  color: var(--text);
  background: var(--bg);
  outline: none;
  transition: border-color 0.2s;
  font-family: var(--sans);
}

.form-group input:focus {
  border-color: var(--accent);
  box-shadow: 0 0 0 3px var(--accent-glow);
}

.submit-btn {
  padding: 15px;
  border: none;
  border-radius: 12px;
  background: var(--accent);
  color: #fff;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  font-family: var(--sans);
  margin-top: 8px;
}

.submit-btn:hover:not(:disabled) {
  background: var(--accent-hover);
  transform: translateY(-1px);
  box-shadow: 0 4px 16px var(--accent-glow);
}

.submit-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.login-footer {
  text-align: center;
  margin-top: 32px;
  font-size: 13px;
  color: var(--text-muted);
}

.toggle-btn {
  background: none;
  border: none;
  color: var(--accent);
  font-weight: 600;
  cursor: pointer;
  font-size: 13px;
  margin-left: 4px;
  font-family: var(--sans);
}

.toggle-btn:hover {
  text-decoration: underline;
}
</style>
