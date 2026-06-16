<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../../stores/auth'
import { articleApi } from '../../api/article'
import type { Article } from '../../types'
import { Plus, Edit, Trash2, FileText, Folder, LogOut, ArrowLeft } from 'lucide-vue-next'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const authStore = useAuthStore()
const articles = ref<Article[]>([])
const loading = ref(true)

async function loadArticles() {
  loading.value = true
  try {
    const result = await articleApi.getList({ page: 0, size: 50 })
    articles.value = result.content
  } catch {
    // 静默处理
  } finally {
    loading.value = false
  }
}

async function handleDelete(article: Article) {
  try {
    await ElMessageBox.confirm(`确定要删除「${article.title}」吗？`, '确认删除', {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'warning',
      confirmButtonClass: 'el-button--danger',
    })
    await articleApi.delete(article.id)
    articles.value = articles.value.filter(a => a.id !== article.id)
    ElMessage.success('删除成功')
  } catch {
    // 用户取消或删除失败
  }
}

function handleLogout() {
  authStore.logout()
  router.push('/admin/login')
}

const typeLabel = (type: string) => {
  return type === 'PROJECT' ? '项目' : '文章'
}

const typeIcon = (type: string) => {
  return type === 'PROJECT' ? Folder : FileText
}

onMounted(() => {
  loadArticles()
})
</script>

<template>
  <div class="admin-page">
    <div class="content-wrapper">
      <div class="admin-header">
        <div class="header-left">
          <button class="back-btn" @click="router.push('/')">
            <ArrowLeft :size="18" />
            <span>返回网站</span>
          </button>
          <h2 class="section-title">管理后台</h2>
        </div>
        <div class="header-right">
          <button class="create-btn" @click="router.push('/admin/article/new')">
            <Plus :size="18" />
            <span>发布内容</span>
          </button>
          <button class="logout-btn" @click="handleLogout">
            <LogOut :size="18" />
            <span>退出</span>
          </button>
        </div>
      </div>

      <div v-if="loading" class="loading-state">
        <span>加载中...</span>
      </div>

      <div v-else-if="articles.length === 0" class="empty-state">
        <p>还没有发布任何内容</p>
        <button class="create-btn" @click="router.push('/admin/article/new')">
          <Plus :size="18" />
          <span>发布第一篇内容</span>
        </button>
      </div>

      <div v-else class="article-table">
        <div class="table-header">
          <span class="col-title">标题</span>
          <span class="col-type">类型</span>
          <span class="col-date">发布时间</span>
          <span class="col-likes">点赞</span>
          <span class="col-comments">评论</span>
          <span class="col-actions">操作</span>
        </div>

        <div v-for="article in articles" :key="article.id" class="table-row">
          <span class="col-title">{{ article.title }}</span>
          <span class="col-type">
            <component :is="typeIcon(article.type)" :size="14" class="type-icon" />
            {{ typeLabel(article.type) }}
          </span>
          <span class="col-date">{{ new Date(article.createdAt).toLocaleDateString('zh-CN') }}</span>
          <span class="col-likes">{{ article.likeCount }}</span>
          <span class="col-comments">{{ article.commentCount }}</span>
          <span class="col-actions">
            <button class="icon-btn edit" @click="router.push(`/admin/article/${article.id}/edit`)">
              <Edit :size="16" />
            </button>
            <button class="icon-btn delete" @click="handleDelete(article)">
              <Trash2 :size="16" />
            </button>
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.admin-page {
  width: 100%;
}

.content-wrapper {
  background: var(--surface);
  border-radius: var(--radius-xl);
  padding: 36px;
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border);
}

.admin-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
  flex-wrap: wrap;
  gap: 16px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.back-btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  background: none;
  border: 1px solid var(--border);
  border-radius: var(--radius);
  color: var(--text-muted);
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  padding: 8px 14px;
  font-family: var(--sans);
  transition: all 0.2s;
}

.back-btn:hover {
  color: var(--accent);
  border-color: var(--accent);
  background: var(--accent-subtle);
}

.section-title {
  font-family: var(--serif);
  font-size: 24px;
  font-weight: 700;
  color: var(--text);
  margin: 0;
}

.create-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 11px 22px;
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

.create-btn:hover {
  background: var(--accent-hover);
  transform: translateY(-1px);
  box-shadow: 0 4px 16px var(--accent-glow);
}

.logout-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 11px 22px;
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: 12px;
  font-size: 14px;
  font-weight: 500;
  color: var(--text-secondary);
  cursor: pointer;
  transition: all 0.2s ease;
  font-family: var(--sans);
}

.logout-btn:hover {
  border-color: #E05540;
  color: #E05540;
  background: rgba(224, 85, 64, 0.04);
}

.loading-state,
.empty-state {
  text-align: center;
  padding: 80px 0;
  color: var(--text-muted);
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.article-table {
  border: 1px solid var(--border);
  border-radius: 14px;
  overflow: hidden;
}

.table-header,
.table-row {
  display: grid;
  grid-template-columns: 2fr 0.7fr 1fr 0.5fr 0.5fr 0.8fr;
  align-items: center;
  padding: 15px 22px;
  gap: 12px;
}

.table-header {
  background: var(--bg);
  font-size: 11px;
  font-weight: 600;
  color: var(--text-muted);
  letter-spacing: 0.04em;
  text-transform: uppercase;
}

.table-row {
  font-size: 14px;
  color: var(--text);
  border-top: 1px solid var(--border);
  transition: background 0.15s;
}

.table-row:hover {
  background: var(--accent-subtle);
}

.col-title {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-weight: 500;
}

.col-type {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 13px;
  color: var(--text-secondary);
}

.type-icon {
  color: var(--accent);
}

.col-date {
  font-size: 13px;
  color: var(--text-muted);
}

.col-likes,
.col-comments {
  text-align: center;
  font-size: 13px;
  color: var(--text-secondary);
}

.col-actions {
  display: flex;
  gap: 8px;
  justify-content: flex-end;
}

.icon-btn {
  width: 36px;
  height: 36px;
  border: 1px solid var(--border);
  border-radius: 8px;
  background: var(--surface);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.15s;
  color: var(--text-muted);
}

.icon-btn.edit:hover {
  border-color: var(--accent);
  color: var(--accent);
  background: var(--accent-light);
}

.icon-btn.delete:hover {
  border-color: #E05540;
  color: #E05540;
  background: rgba(224, 85, 64, 0.06);
}

@media (max-width: 768px) {
  .content-wrapper {
    padding: 24px 18px;
  }

  .table-header,
  .table-row {
    grid-template-columns: 2fr 0.8fr 0.8fr;
    padding: 12px 18px;
  }

  .col-date,
  .col-likes,
  .col-comments {
    display: none;
  }
}
</style>
