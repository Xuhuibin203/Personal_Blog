<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { articleApi } from '../api/article'
import { commentApi } from '../api/comment'
import type { Article, Comment } from '../types'
import { ArticleType } from '../types'
import { Heart, MessageCircle, ArrowLeft } from 'lucide-vue-next'
import { marked } from 'marked'

// 配置 marked
marked.setOptions({
  breaks: true,
  gfm: true,
})

const route = useRoute()
const router = useRouter()

const article = ref<Article | null>(null)
const comments = ref<Comment[]>([])
const loading = ref(true)
const commentLoading = ref(false)

async function loadArticle() {
  loading.value = true
  try {
    const id = Number(route.params.id)
    article.value = await articleApi.getById(id)
    loadComments()
  } catch {
    // 静默处理
  } finally {
    loading.value = false
  }
}

async function loadComments() {
  if (!article.value) return
  commentLoading.value = true
  try {
    const result = await commentApi.getByArticle(article.value.id)
    comments.value = result.content
  } catch {
    // 静默处理
  } finally {
    commentLoading.value = false
  }
}

const renderedContent = computed(() => {
  if (article.value && article.value.content) {
    return marked.parse(article.value.content) as string
  }
  return ''
})

onMounted(() => {
  loadArticle()
})
</script>

<template>
  <div class="detail-page">
    <!-- 加载中 -->
    <div v-if="loading" class="loading-state">
      <span>加载中...</span>
    </div>

    <!-- 文章内容 -->
    <template v-else-if="article">
      <div class="content-wrapper">
        <!-- 返回按钮 -->
        <button class="back-btn" @click="router.push('/')">
          <ArrowLeft :size="18" />
          <span>返回首页</span>
        </button>

        <!-- 文章头部 -->
        <div class="article-header">
          <div class="type-badge">
            {{ article.type === ArticleType.PROJECT ? '项目' : '文章' }}
          </div>
          <h1 class="article-title">{{ article.title }}</h1>
          <div class="article-meta">
            <span>{{ article.authorName }}</span>
            <span>·</span>
            <span>{{ new Date(article.createdAt).toLocaleDateString('zh-CN') }}</span>
          </div>
        </div>

        <!-- 封面图 -->
        <div v-if="article.coverImage" class="cover-image">
          <img :src="article.coverImage" :alt="article.title" />
        </div>

        <!-- 正文 -->
        <div class="article-content" v-html="renderedContent"></div>

        <!-- 底部统计栏（仅展示） -->
        <div class="article-actions">
          <div class="stat-display">
            <Heart :size="20" />
            <span>{{ article.likeCount }} 赞</span>
          </div>
          <div class="stat-display">
            <MessageCircle :size="20" />
            <span>{{ article.commentCount }} 评论</span>
          </div>
        </div>
      </div>

      <!-- 评论区（仅展示） -->
      <div class="comments-section">
        <h3 class="comments-title">评论 ({{ article.commentCount }})</h3>

        <!-- 评论列表 -->
        <div v-if="commentLoading" class="loading-state">
          <span>加载评论中...</span>
        </div>
        <div v-else-if="comments.length === 0" class="empty-comments">
          <p>暂无评论</p>
        </div>
        <div v-else class="comment-list">
          <div v-for="comment in comments" :key="comment.id" class="comment-item">
            <div class="comment-avatar">
              <img v-if="comment.userAvatar" :src="comment.userAvatar" alt="" />
              <span v-else class="avatar-placeholder">{{ comment.username.charAt(0) }}</span>
            </div>
            <div class="comment-body">
              <div class="comment-header">
                <span class="comment-user">{{ comment.username }}</span>
                <span class="comment-time">{{ new Date(comment.createdAt).toLocaleString('zh-CN') }}</span>
              </div>
              <p class="comment-content">{{ comment.content }}</p>
            </div>
          </div>
        </div>
      </div>
    </template>

    <!-- 文章不存在 -->
    <div v-else class="empty-state">
      <p>文章不存在</p>
    </div>
  </div>
</template>

<style scoped>
.detail-page {
  width: 100%;
}

.content-wrapper {
  background: var(--surface);
  border-radius: var(--radius-xl);
  padding: 48px;
  box-shadow: var(--shadow-sm);
  margin-bottom: 28px;
  border: 1px solid var(--border);
}

.back-btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  background: none;
  border: none;
  color: var(--text-muted);
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  padding: 8px 0;
  margin-bottom: 28px;
  font-family: var(--sans);
  transition: color 0.2s;
}

.back-btn:hover {
  color: var(--accent);
}

.article-header {
  margin-bottom: 28px;
}

.type-badge {
  display: inline-block;
  padding: 5px 14px;
  background: var(--accent-subtle);
  border-radius: 6px;
  font-size: 11px;
  font-weight: 600;
  color: var(--accent);
  margin-bottom: 16px;
  letter-spacing: 0.04em;
  text-transform: uppercase;
}

.article-title {
  font-family: var(--serif);
  font-size: 36px;
  font-weight: 900;
  color: var(--text);
  line-height: 1.25;
  margin-bottom: 16px;
  letter-spacing: -0.01em;
}

.article-meta {
  font-size: 14px;
  color: var(--text-muted);
  display: flex;
  gap: 10px;
  align-items: center;
}

.article-meta span:first-child {
  font-weight: 600;
  color: var(--text-secondary);
}

.cover-image {
  margin: 28px -48px;
  overflow: hidden;
  border-radius: 0;
}

.cover-image img {
  width: 100%;
  max-height: 480px;
  object-fit: cover;
}

.article-content {
  margin: 32px 0;
  font-size: 17px;
  line-height: 1.9;
  color: var(--text);
  word-break: break-word;
}

.article-content :deep(h1) { font-family: var(--serif); font-size: 1.8em; font-weight: 700; margin: 1.2em 0 0.4em; }
.article-content :deep(h2) { font-family: var(--serif); font-size: 1.4em; font-weight: 700; margin: 1em 0 0.3em; }
.article-content :deep(h3) { font-family: var(--serif); font-size: 1.15em; font-weight: 600; margin: 0.8em 0 0.3em; }
.article-content :deep(p) { margin: 0.8em 0; }
.article-content :deep(img) { max-width: 100%; border-radius: 12px; margin: 20px 0; }
.article-content :deep(pre) { background: #2D2218; color: #F0EAE0; padding: 24px; border-radius: 12px; overflow-x: auto; font-size: 14px; font-family: var(--mono); }
.article-content :deep(code) { background: var(--bg-alt); padding: 2px 8px; border-radius: 6px; font-size: 0.9em; font-family: var(--mono); color: var(--accent); }
.article-content :deep(pre code) { background: none; padding: 0; color: inherit; font-family: var(--mono); }
.article-content :deep(blockquote) { border-left: 4px solid var(--accent); padding: 16px 24px; color: var(--text-secondary); margin: 24px 0; background: var(--accent-subtle); border-radius: 0 var(--radius) var(--radius) 0; font-style: italic; }
.article-content :deep(a) { color: var(--accent); text-decoration: none; border-bottom: 1px solid var(--accent-light); }
.article-content :deep(a:hover) { border-bottom-color: var(--accent); }
.article-content :deep(ul), .article-content :deep(ol) { padding-left: 28px; margin: 12px 0; }
.article-content :deep(li) { margin: 6px 0; }

.article-actions {
  display: flex;
  gap: 20px;
  padding-top: 28px;
  border-top: 1px solid var(--border);
  margin-top: 40px;
}

.stat-display {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: var(--text-secondary);
}

.stat-display svg {
  color: var(--text-muted);
}

.comments-section {
  background: var(--surface);
  border-radius: var(--radius-xl);
  padding: 40px;
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border);
}

.comments-title {
  font-family: var(--serif);
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 24px;
  color: var(--text);
}

.comment-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.comment-item {
  display: flex;
  gap: 14px;
  padding: 18px;
  background: var(--bg);
  border-radius: 14px;
  transition: background 0.2s;
}

.comment-item:hover {
  background: var(--bg-alt);
}

.comment-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
  background: var(--accent-subtle);
  display: flex;
  align-items: center;
  justify-content: center;
}

.comment-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-placeholder {
  color: var(--accent);
  font-size: 15px;
  font-weight: 600;
}

.comment-body {
  flex: 1;
}

.comment-header {
  display: flex;
  gap: 10px;
  align-items: center;
  margin-bottom: 6px;
}

.comment-user {
  font-size: 13px;
  font-weight: 600;
  color: var(--text);
}

.comment-time {
  font-size: 12px;
  color: var(--text-muted);
}

.comment-content {
  font-size: 14px;
  color: var(--text);
  line-height: 1.7;
}

.empty-comments {
  text-align: center;
  padding: 48px;
  color: var(--text-muted);
}

.loading-state,
.empty-state {
  text-align: center;
  padding: 80px 0;
  color: var(--text-muted);
}

@media (max-width: 768px) {
  .content-wrapper {
    padding: 28px 20px;
  }
  .article-title {
    font-size: 26px;
  }
  .cover-image {
    margin: 20px -20px;
  }
  .cover-image img {
    max-height: 320px;
  }
  .comments-section {
    padding: 28px 20px;
  }
}
</style>
