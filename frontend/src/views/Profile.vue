<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { userApi } from '../api/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Camera, LogOut, User as UserIcon, Edit3, Check, X } from 'lucide-vue-next'

const router = useRouter()
const authStore = useAuthStore()
const uploading = ref(false)
const fileInput = ref<HTMLInputElement | null>(null)

// 昵称编辑
const editingNickname = ref(false)
const editNickname = ref('')

// 密码修改弹窗
const showPasswordDialog = ref(false)
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: '',
})
const changingPassword = ref(false)

const user = computed(() => authStore.user)

function startEditNickname() {
  editNickname.value = user.value?.nickname || ''
  editingNickname.value = true
}

async function saveNickname() {
  if (!editNickname.value.trim()) {
    ElMessage.warning('昵称不能为空')
    return
  }
  try {
    const updated = await userApi.updateProfile({ nickname: editNickname.value.trim() })
    authStore.setUser(updated)
    editingNickname.value = false
    ElMessage.success('昵称修改成功')
  } catch { /* 静默 */ }
}

function cancelEditNickname() {
  editingNickname.value = false
}

async function handleChangePassword() {
  if (!passwordForm.oldPassword) {
    ElMessage.warning('请输入原密码')
    return
  }
  if (!passwordForm.newPassword || passwordForm.newPassword.length < 6) {
    ElMessage.warning('新密码至少6位')
    return
  }
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    ElMessage.warning('两次密码输入不一致')
    return
  }
  changingPassword.value = true
  try {
    await userApi.changePassword({
      oldPassword: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword,
    })
    ElMessage.success('密码修改成功，请重新登录')
    showPasswordDialog.value = false
    passwordForm.oldPassword = ''
    passwordForm.newPassword = ''
    passwordForm.confirmPassword = ''
    authStore.logout()
    router.push('/login')
  } catch { /* 静默 */ }
  finally {
    changingPassword.value = false
  }
}

function triggerUpload() {
  fileInput.value?.click()
}

async function handleAvatarChange(e: Event) {
  const target = e.target as HTMLInputElement
  const file = target.files?.[0]
  if (!file) return

  // 文件类型和大小检查
  if (!file.type.startsWith('image/')) {
    ElMessage.warning('请选择图片文件')
    return
  }
  if (file.size > 5 * 1024 * 1024) {
    ElMessage.warning('图片大小不能超过 5MB')
    return
  }

  uploading.value = true
  try {
    const formData = new FormData()
    formData.append('file', file)
    const updatedUser = await userApi.updateAvatar(formData)
    authStore.setUser(updatedUser)
    ElMessage.success('头像更新成功')
  } catch {
    // 静默处理
  } finally {
    uploading.value = false
  }
}

async function handleLogout() {
  await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
  authStore.logout()
  router.push('/')
}
</script>

<template>
  <div class="profile-page">
    <div v-if="user" class="content-wrapper">
      <h2 class="section-title">个人中心</h2>

      <!-- 头像区域 -->
      <div class="avatar-section">
        <div class="avatar-wrapper" @click="triggerUpload" :class="{ uploading }">
          <img v-if="user.avatarUrl" :src="user.avatarUrl" class="avatar-img" alt="头像" />
          <UserIcon v-else :size="40" class="avatar-placeholder" />
          <div class="avatar-overlay">
            <Camera :size="20" />
            <span>{{ uploading ? '上传中...' : '更换头像' }}</span>
          </div>
        </div>
        <input
          ref="fileInput"
          type="file"
          accept="image/*"
          class="file-input"
          @change="handleAvatarChange"
        />
      </div>

      <!-- 用户信息 -->
      <div class="info-section">
        <div class="info-row">
          <span class="info-label">用户名</span>
          <span class="info-value">{{ user.username }}</span>
        </div>
        <div class="info-row">
          <span class="info-label">昵称</span>
          <div class="info-value" v-if="!editingNickname">
            <span>{{ user.nickname }}</span>
            <button class="edit-icon-btn" @click="startEditNickname">
              <Edit3 :size="14" />
            </button>
          </div>
          <div class="info-value edit-row" v-else>
            <input
              v-model="editNickname"
              class="inline-input"
              maxlength="50"
              @keyup.enter="saveNickname"
              @keyup.escape="cancelEditNickname"
            />
            <button class="icon-btn-sm confirm" @click="saveNickname"><Check :size="14" /></button>
            <button class="icon-btn-sm cancel" @click="cancelEditNickname"><X :size="14" /></button>
          </div>
        </div>
        <div class="info-row">
          <span class="info-label">角色</span>
          <span class="info-value">
            <span class="role-badge" :class="user.role === 'ADMIN' ? 'admin' : 'user'">
              {{ user.role === 'ADMIN' ? '管理员' : '普通用户' }}
            </span>
          </span>
        </div>
        <div class="info-row">
          <span class="info-label">注册时间</span>
          <span class="info-value">{{ new Date(user.createdAt).toLocaleDateString('zh-CN') }}</span>
        </div>
      </div>

      <!-- 密码修改 -->
      <div class="password-section">
        <button class="password-btn" @click="showPasswordDialog = true">修改密码</button>
      </div>

      <!-- 管理后台入口 -->
      <div v-if="authStore.isAdmin" class="admin-entry">
        <button class="admin-btn" @click="router.push('/admin')">
          进入管理后台
        </button>
      </div>

      <!-- 退出登录 -->
      <div class="logout-section">
        <button class="logout-btn" @click="handleLogout">
          <LogOut :size="18" />
          <span>退出登录</span>
        </button>
      </div>
    </div>

    <div v-else class="empty-state">
      <p>请先登录</p>
    </div>

    <!-- 密码修改弹窗 -->
    <Teleport to="body">
      <div v-if="showPasswordDialog" class="dialog-overlay" @click.self="showPasswordDialog = false">
        <div class="dialog-card">
          <h3>修改密码</h3>
          <div class="dialog-form">
            <div class="form-group">
              <label>原密码</label>
              <input v-model="passwordForm.oldPassword" type="password" placeholder="请输入原密码" />
            </div>
            <div class="form-group">
              <label>新密码</label>
              <input v-model="passwordForm.newPassword" type="password" placeholder="至少6位" />
            </div>
            <div class="form-group">
              <label>确认新密码</label>
              <input v-model="passwordForm.confirmPassword" type="password" placeholder="再次输入新密码" />
            </div>
          </div>
          <div class="dialog-actions">
            <button class="dialog-btn cancel" @click="showPasswordDialog = false">取消</button>
            <button class="dialog-btn confirm" :disabled="changingPassword" @click="handleChangePassword">
              {{ changingPassword ? '修改中...' : '确认修改' }}
            </button>
          </div>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<style scoped>
.profile-page {
  width: 100%;
}

.content-wrapper {
  background: var(--surface);
  border-radius: var(--radius-xl);
  padding: 40px;
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border);
}

.section-title {
  font-family: var(--serif);
  font-size: 24px;
  font-weight: 700;
  color: var(--text);
  margin-bottom: 32px;
}

.avatar-section {
  display: flex;
  justify-content: center;
  margin-bottom: 40px;
}

.avatar-wrapper {
  width: 104px;
  height: 104px;
  border-radius: 50%;
  overflow: hidden;
  cursor: pointer;
  position: relative;
  background: var(--accent-subtle);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.25s ease;
}

.avatar-wrapper:hover {
  transform: scale(1.06);
}

.avatar-wrapper.uploading {
  opacity: 0.7;
  cursor: wait;
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-placeholder {
  color: var(--accent);
}

.avatar-overlay {
  position: absolute;
  inset: 0;
  background: rgba(61, 44, 32, 0.5);
  backdrop-filter: blur(4px);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 4px;
  color: #fff;
  font-size: 11px;
  font-weight: 500;
  opacity: 0;
  transition: opacity 0.25s ease;
}

.avatar-wrapper:hover .avatar-overlay {
  opacity: 1;
}

.file-input {
  display: none;
}

.info-section {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-bottom: 36px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 18px;
  background: var(--bg);
  border-radius: 12px;
  transition: background 0.2s;
}

.info-row:hover {
  background: var(--bg-alt);
}

.info-label {
  font-size: 13px;
  color: var(--text-secondary);
  font-weight: 500;
}

.info-value {
  font-size: 14px;
  color: var(--text);
  font-weight: 500;
}

.role-badge {
  padding: 4px 12px;
  border-radius: 8px;
  font-size: 11px;
  font-weight: 600;
  letter-spacing: 0.02em;
}

.role-badge.admin {
  background: var(--accent-subtle);
  color: var(--accent);
}

.role-badge.user {
  background: var(--bg-alt);
  color: var(--text-muted);
}

.admin-entry {
  display: flex;
  justify-content: center;
  margin-bottom: 24px;
}

.admin-btn {
  padding: 13px 36px;
  background: var(--accent);
  border: none;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 600;
  color: #fff;
  cursor: pointer;
  transition: all 0.2s ease;
  font-family: var(--sans);
}

.admin-btn:hover {
  background: var(--accent-hover);
  transform: translateY(-2px);
  box-shadow: 0 4px 16px var(--accent-glow);
}

.logout-section {
  display: flex;
  justify-content: center;
  padding-top: 24px;
  border-top: 1px solid var(--border);
}

.logout-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 11px 28px;
  background: none;
  border: 1.5px solid #E05540;
  border-radius: 12px;
  color: #E05540;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  font-family: var(--sans);
}

.logout-btn:hover {
  background: rgba(224, 85, 64, 0.06);
}

.empty-state {
  text-align: center;
  padding: 80px;
  color: var(--text-muted);
}

.edit-icon-btn {
  background: none;
  border: none;
  color: var(--text-muted);
  cursor: pointer;
  padding: 2px 6px;
  border-radius: 6px;
  transition: all 0.2s;
}

.edit-icon-btn:hover {
  color: var(--accent);
  background: var(--accent-light);
}

.edit-row {
  display: flex;
  align-items: center;
  gap: 6px;
}

.inline-input {
  width: 120px;
  padding: 5px 10px;
  border: 1.5px solid var(--accent);
  border-radius: 8px;
  font-size: 14px;
  outline: none;
  font-family: var(--sans);
}

.icon-btn-sm {
  width: 26px;
  height: 26px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
}

.icon-btn-sm.confirm {
  background: var(--accent);
  color: #fff;
}

.icon-btn-sm.confirm:hover {
  background: var(--accent-hover);
}

.icon-btn-sm.cancel {
  background: var(--bg-alt);
  color: var(--text-secondary);
}

.password-section {
  display: flex;
  justify-content: center;
  margin-bottom: 24px;
}

.password-btn {
  padding: 10px 28px;
  background: none;
  border: 1.5px solid var(--border);
  border-radius: 10px;
  font-size: 13px;
  font-weight: 500;
  color: var(--text-secondary);
  cursor: pointer;
  transition: all 0.2s;
  font-family: var(--sans);
}

.password-btn:hover {
  border-color: var(--accent);
  color: var(--accent);
}

.dialog-overlay {
  position: fixed;
  inset: 0;
  background: rgba(61, 44, 32, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  backdrop-filter: blur(6px);
  -webkit-backdrop-filter: blur(6px);
}

.dialog-card {
  background: var(--surface);
  border-radius: var(--radius-xl);
  padding: 36px;
  width: 90%;
  max-width: 420px;
  box-shadow: var(--shadow-xl);
  border: 1px solid var(--border);
}

.dialog-card h3 {
  font-family: var(--serif);
  font-size: 20px;
  font-weight: 700;
  color: var(--text);
  margin-bottom: 28px;
}

.dialog-form {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.dialog-form .form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.dialog-form label {
  font-size: 12px;
  font-weight: 600;
  color: var(--text-secondary);
}

.dialog-form input {
  padding: 12px 16px;
  border: 1.5px solid var(--border);
  border-radius: 10px;
  font-size: 14px;
  outline: none;
  font-family: var(--sans);
  transition: border-color 0.2s;
}

.dialog-form input:focus {
  border-color: var(--accent);
}

.dialog-actions {
  display: flex;
  gap: 12px;
  margin-top: 28px;
  justify-content: flex-end;
}

.dialog-btn {
  padding: 11px 28px;
  border: none;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  font-family: var(--sans);
}

.dialog-btn.cancel {
  background: var(--bg-alt);
  color: var(--text-secondary);
}

.dialog-btn.confirm {
  background: var(--accent);
  color: #fff;
}

.dialog-btn.confirm:hover:not(:disabled) {
  background: var(--accent-hover);
}

.dialog-btn:disabled {
  opacity: 0.5;
}
</style>
