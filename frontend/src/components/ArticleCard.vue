<script setup lang="ts">
import { computed } from 'vue'
import type { Article } from '../types'
import { ArticleType } from '../types'
import { Heart, MessageCircle, FileText, Folder } from 'lucide-vue-next'
import { useRouter } from 'vue-router'

const props = defineProps<{
  article: Article
}>()

const router = useRouter()

const typeIcon = computed(() => {
  switch (props.article.type) {
    case ArticleType.PROJECT: return Folder
    default: return FileText
  }
})

function handleClick() {
  router.push(`/article/${props.article.id}`)
}
</script>

<template>
  <article class="article-card" @click="handleClick">
    <!-- 封面图 -->
    <div v-if="article.coverImage" class="card-cover">
      <img :src="article.coverImage" :alt="article.title" loading="lazy" />
      <div class="cover-overlay">
        <span class="overlay-tag">{{ article.type === 'PROJECT' ? 'PROJECT' : 'ARTICLE' }}</span>
      </div>
    </div>

    <!-- 无封面时的占位 -->
    <div v-else class="card-cover card-cover-placeholder">
      <component :is="typeIcon" :size="32" class="placeholder-icon" />
      <span class="overlay-tag">{{ article.type === 'PROJECT' ? 'PROJECT' : 'ARTICLE' }}</span>
    </div>

    <!-- 内容 -->
    <div class="card-body">
      <div class="card-meta">
        <component :is="typeIcon" :size="13" class="meta-icon" />
        <span>{{ article.type === 'PROJECT' ? '项目' : '文章' }}</span>
        <span class="meta-dot">·</span>
        <span>{{ new Date(article.createdAt).toLocaleDateString('zh-CN') }}</span>
      </div>
      <h3 class="card-title">{{ article.title }}</h3>
      <p class="card-summary">{{ article.summary }}</p>
    </div>

    <!-- 底部信息 -->
    <div class="card-footer">
      <div class="author-info">
        <span class="author-name">{{ article.authorName }}</span>
      </div>
      <div class="card-stats">
        <span class="stat-item">
          <Heart :size="14" />
          <span>{{ article.likeCount }}</span>
        </span>
        <span class="stat-item">
          <MessageCircle :size="14" />
          <span>{{ article.commentCount }}</span>
        </span>
      </div>
    </div>
  </article>
</template>

<style scoped>
.article-card {
  background: var(--surface);
  border-radius: var(--radius-lg);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
  display: flex;
  flex-direction: column;
  border: 1px solid var(--border);
  transition: all 0.35s ease;
  cursor: pointer;
}

.article-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
}

/* Hero 模式 */

.card-cover {
  width: 100%;
  height: 220px;
  overflow: hidden;
  position: relative;
  background: var(--bg-alt);
}

.card-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.6s ease;
}

.article-card:hover .card-cover img {
  transform: scale(1.05);
}

.cover-overlay {
  position: absolute;
  top: 14px;
  left: 14px;
}

.overlay-tag {
  padding: 4px 12px;
  border-radius: 6px;
  background: rgba(61, 44, 32, 0.6);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  color: #fff;
  font-size: 10px;
  font-weight: 700;
  letter-spacing: 0.06em;
}

.card-cover-placeholder {
  background: linear-gradient(135deg, var(--accent-subtle), var(--bg-alt));
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.placeholder-icon {
  color: var(--accent);
  opacity: 0.3;
}

.card-cover-placeholder .overlay-tag {
  position: absolute;
  top: 14px;
  left: 14px;
}

.card-body {
  padding: 20px 22px 12px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.card-meta {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 10px;
  font-size: 11px;
  color: var(--text-muted);
  letter-spacing: 0.02em;
  text-transform: uppercase;
}

.meta-icon {
  color: var(--accent);
}

.meta-dot {
  color: var(--border);
}

.card-title {
  font-family: var(--serif);
  font-size: 18px;
  font-weight: 700;
  color: var(--text);
  margin-bottom: 8px;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-summary {
  font-size: 13px;
  color: var(--text-secondary);
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin-top: auto;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 22px 16px;
  border-top: 1px solid var(--border-light);
}

.author-info {
  display: flex;
  align-items: center;
}

.author-name {
  font-size: 12px;
  font-weight: 600;
  color: var(--text);
}

.card-stats {
  display: flex;
  gap: 12px;
  color: var(--text-muted);
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
}

@media (max-width: 768px) {
  .card-cover {
    height: 200px;
  }

  .card-body {
    padding: 16px 18px 10px;
  }

  .card-footer {
    padding: 10px 18px 14px;
  }
}
</style>
