<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { articleApi } from '../api/article'
import { ArticleType } from '../types'
import type { Article } from '../types'
import ArticleCard from '../components/ArticleCard.vue'

const articles = ref<Article[]>([])
const loading = ref(true)
const currentPage = ref(0)
const totalPages = ref(0)
const totalCount = ref(0)
const pageSize = 10

async function loadArticles() {
  loading.value = true
  try {
    const result = await articleApi.getList({ page: currentPage.value, size: pageSize, type: ArticleType.ARTICLE })
    articles.value = result.content
    totalPages.value = result.totalPages
    totalCount.value = result.totalElements
  } catch {
    // 后端未启动
  } finally {
    loading.value = false
  }
}

function changePage(page: number) {
  currentPage.value = page
  loadArticles()
}

onMounted(() => {
  loadArticles()
})
</script>

<template>
  <div class="list-page">
    <header class="page-header">
      <div class="header-top">
        <h1 class="header-title">文章</h1>
        <span v-if="!loading && totalCount > 0" class="header-count">{{ totalCount }} 篇</span>
      </div>
      <p class="header-sub">技术分享 · 学习笔记 · 思考沉淀</p>
      <div class="ornament-divider"></div>
    </header>

    <div v-if="loading" class="loading-state">
      <span>加载中...</span>
    </div>

    <div v-else-if="articles.length === 0" class="empty-state">
      <p>暂无文章</p>
    </div>

    <template v-else>
      <div class="article-grid">
        <ArticleCard
          v-for="article in articles"
          :key="article.id"
          :article="article"
        />
      </div>

      <div v-if="totalPages > 1" class="pagination">
        <button
          v-for="page in totalPages"
          :key="page"
          class="page-btn"
          :class="{ active: currentPage === page - 1 }"
          @click="changePage(page - 1)"
        >
          {{ page }}
        </button>
      </div>
    </template>
  </div>
</template>

<style scoped>
.list-page {
  width: 100%;
}

.page-header {
  margin-bottom: 40px;
}

.header-top {
  display: flex;
  align-items: baseline;
  gap: 14px;
  margin-bottom: 8px;
}

.header-title {
  font-family: var(--serif);
  font-size: 32px;
  font-weight: 900;
  color: var(--text);
  margin: 0;
  letter-spacing: 0.02em;
}

.header-count {
  font-size: 14px;
  color: var(--text-muted);
  font-weight: 500;
}

.header-sub {
  font-size: 14px;
  color: var(--text-muted);
  margin: 0 0 20px;
  line-height: 1.5;
}

.page-header .ornament-divider {
  font-weight: 600;
  letter-spacing: 0.1em;
  font-size: 11px;
}

.article-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 28px;
}

.loading-state,
.empty-state {
  text-align: center;
  padding: 100px 0;
  color: var(--text-muted);
  font-size: 15px;
}

.pagination {
  display: flex;
  justify-content: center;
  gap: 8px;
  margin-top: 56px;
  padding-top: 32px;
  border-top: 1px solid var(--border);
}

.page-btn {
  width: 42px;
  height: 42px;
  border: 1px solid var(--border);
  border-radius: 50%;
  background: var(--surface);
  color: var(--text-secondary);
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  font-family: var(--sans);
}

.page-btn:hover {
  border-color: var(--accent);
  color: var(--accent);
  background: var(--accent-subtle);
}

.page-btn.active {
  background: var(--accent);
  border-color: var(--accent);
  color: #fff;
  font-weight: 600;
}

@media (max-width: 768px) {
  .article-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }
}
</style>
