<script setup lang="ts">
import { useRouter, useRoute } from 'vue-router'
import { Github, FileText, Folder, User } from 'lucide-vue-next'
import avatarImg from '../assets/许辉彬个人头像.jpg'

const router = useRouter()
const route = useRoute()

const navItems = [
  { label: '关于作者', icon: User, path: '/', query: {} },
  { label: '全部文章', icon: FileText, path: '/articles', query: {} },
  { label: '项目作品', icon: Folder, path: '/projects', query: {} },
]

function isActive(item: typeof navItems[0]) {
  return item.path === route.path
}

function goTo(item: typeof navItems[0]) {
  router.push({ path: item.path, query: item.query })
}
</script>

<template>
  <aside class="sidebar">
    <!-- 个人信息卡 -->
    <div class="sidebar-card">
      <div class="avatar-block">
        <img :src="avatarImg" alt="Season" class="avatar-img" />
      </div>
      <h2 class="username">Season</h2>
      <p class="bio">ACMer · 信奥教师 · 游戏开发者</p>
    </div>

    <!-- 分类导航 -->
    <nav class="sidebar-nav">
      <div class="nav-title">探索</div>
      <button
        v-for="item in navItems"
        :key="item.label"
        class="nav-item"
        :class="{ active: isActive(item) }"
        @click="goTo(item)"
      >
        <component :is="item.icon" :size="16" class="nav-icon" />
        <span>{{ item.label }}</span>
      </button>
    </nav>

    <!-- GitHub 链接 -->
    <a
      href="https://github.com"
      target="_blank"
      rel="noopener noreferrer"
      class="social-link"
    >
      <Github :size="16" />
      <span>GitHub</span>
    </a>
  </aside>
</template>

<style scoped>
.sidebar {
  width: 260px;
  flex-shrink: 0;
  position: sticky;
  top: 40px;
  height: fit-content;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.sidebar-card {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: var(--radius-xl);
  padding: 24px 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  box-shadow: var(--shadow-sm);
}

.avatar-block {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--accent-light), var(--accent-subtle));
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 16px var(--accent-glow);
  padding: 2px;
}

.avatar-img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid var(--surface);
}

.username {
  font-family: var(--serif);
  font-size: 17px;
  font-weight: 700;
  color: var(--text);
  margin: 0;
}

.bio {
  font-size: 11px;
  color: var(--text-muted);
  margin: 0;
  line-height: 1.5;
  text-align: center;
}

/* 分类导航 */
.sidebar-nav {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: var(--radius-xl);
  padding: 8px;
  box-shadow: var(--shadow-sm);
}

.nav-title {
  font-family: var(--serif);
  font-size: 12px;
  font-weight: 600;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 0.06em;
  padding: 8px 12px 6px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 14px;
  border-radius: var(--radius-sm);
  font-size: 14px;
  color: var(--text-secondary);
  cursor: pointer;
  transition: all 0.15s ease;
  border: none;
  background: none;
  width: 100%;
  text-align: left;
  font-family: var(--sans);
}

.nav-item:hover {
  background: var(--accent-subtle);
  color: var(--accent);
}

.nav-item.active {
  background: var(--accent-light);
  color: var(--accent);
  font-weight: 600;
}

.nav-icon {
  color: var(--accent);
  opacity: 0.7;
}

/* 社交媒体 */
.social-link {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 12px;
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: var(--radius-xl);
  color: var(--text-secondary);
  font-size: 13px;
  font-weight: 500;
  text-decoration: none;
  transition: all 0.2s ease;
  box-shadow: var(--shadow-sm);
}

.social-link:hover {
  background: var(--accent);
  color: #fff;
  border-color: var(--accent);
}

@media (max-width: 768px) {
  .sidebar {
    width: 100%;
    position: static;
    flex-direction: row;
    flex-wrap: wrap;
    gap: 12px;
  }

  .sidebar-card {
    flex: 1;
    min-width: 180px;
    flex-direction: row;
    padding: 14px 18px;
    gap: 12px;
  }

  .avatar-block {
    width: 44px;
    height: 44px;
  }

  .username {
    font-size: 15px;
  }

  .bio {
    display: none;
  }

  .sidebar-nav {
    flex: 1;
    min-width: 180px;
  }

  .social-link {
    flex: 1;
    min-width: 60px;
  }
}
</style>
