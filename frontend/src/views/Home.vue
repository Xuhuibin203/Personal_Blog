<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { articleApi } from '../api/article'
import { ArticleType } from '../types'
import type { Article } from '../types'
import ArticleCard from '../components/ArticleCard.vue'
import PersonalProfile from '../components/PersonalProfile.vue'

const randomArticles = ref<Article[]>([])
const randomProjects = ref<Article[]>([])
const loading = ref(true)

const highlights = computed(() => [...randomArticles.value, ...randomProjects.value])

// Fisher-Yates 洗牌，取前 n 个
function shufflePick<T>(arr: T[], n: number): T[] {
  const copy = [...arr]
  for (let i = copy.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [copy[i], copy[j]] = [copy[j], copy[i]]
  }
  return copy.slice(0, n)
}

async function loadHighlights() {
  loading.value = true
  try {
    const result = await articleApi.getList({ page: 0, size: 100 })
    const all = result.content
    const articles = all.filter(a => a.type === ArticleType.ARTICLE)
    const projects = all.filter(a => a.type === ArticleType.PROJECT)
    randomArticles.value = shufflePick(articles, 2)
    randomProjects.value = shufflePick(projects, 2)
  } catch {
    // 后端未启动
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadHighlights()
})
</script>

<template>
  <div class="home-page">
    <!-- GitHub 贡献图 -->
    <section class="github-section">
      <div class="github-chart">
        <img
          src="https://ghchart.rshah.org/0000FF/Xuhuibin203"
          alt="GitHub contributions chart"
          loading="lazy"
        />
      </div>
    </section>

    <!-- 精选卡片（文章 + 项目混排，无标题） -->
    <div v-if="highlights.length > 0" class="highlight-grid">
      <ArticleCard
        v-for="item in highlights"
        :key="item.id"
        :article="item"
      />
    </div>

    <!-- 加载中 -->
    <div v-else-if="loading" class="loading-state">
      <span>加载中...</span>
    </div>

    <!-- 个人简历 -->
    <PersonalProfile />
  </div>
</template>

<style scoped>
.home-page {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* GitHub 贡献图 */
.github-section {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: var(--radius-xl);
  padding: 16px 20px;
  box-shadow: var(--shadow-sm);
}

.github-chart {
  overflow-x: auto;
  -webkit-overflow-scrolling: touch;
}

.github-chart img {
  width: 100%;
  max-width: 100%;
  display: block;
  border-radius: var(--radius-sm);
}

/* 卡片网格 */
.highlight-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.highlight-grid :deep(.card-cover) {
  height: 130px;
}

.highlight-grid :deep(.card-body) {
  padding: 14px 16px 10px;
}

.highlight-grid :deep(.card-title) {
  font-size: 15px;
}

.highlight-grid :deep(.card-summary) {
  font-size: 12px;
  -webkit-line-clamp: 2;
}

.highlight-grid :deep(.card-footer) {
  padding: 10px 16px 12px;
}

.loading-state {
  text-align: center;
  padding: 60px 0;
  color: var(--text-muted);
  font-size: 15px;
}

@media (max-width: 768px) {
  .highlight-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }

  .highlight-grid :deep(.card-cover) {
    height: 200px;
  }

  .highlight-grid :deep(.card-body) {
    padding: 16px 18px 10px;
  }

  .highlight-grid :deep(.card-title) {
    font-size: 18px;
  }

  .highlight-grid :deep(.card-summary) {
    font-size: 13px;
  }

  .highlight-grid :deep(.card-footer) {
    padding: 10px 18px 14px;
  }
}
</style>
