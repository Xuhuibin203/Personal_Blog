<script setup lang="ts">
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const navItems = [
  { label: '首页', path: '/', query: {} },
  { label: '文章', path: '/', query: { type: 'ARTICLE' } },
  { label: '项目', path: '/', query: { type: 'PROJECT' } },
]

function isActive(item: typeof navItems[0]) {
  if (item.path !== route.path) return false
  if (item.query.type) {
    return route.query.type === item.query.type
  }
  return !route.query.type
}

function goTo(item: typeof navItems[0]) {
  router.push({ path: item.path, query: item.query })
}
</script>

<template>
  <nav class="navbar">
    <div class="navbar-inner">
      <!-- Logo -->
      <div class="nav-brand" @click="router.push('/')">
        <span class="brand-dot"></span>
        <span class="brand-text">XuHuibin</span>
      </div>

      <!-- 导航菜单 -->
      <div class="nav-menu">
        <button
          v-for="item in navItems"
          :key="item.label"
          class="nav-btn"
          :class="{ active: isActive(item) }"
          @click="goTo(item)"
        >
          {{ item.label }}
        </button>
      </div>

    </div>
  </nav>
</template>

<style scoped>
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
  padding: 14px 24px;
}

.navbar-inner {
  max-width: 1280px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: var(--surface-glass);
  backdrop-filter: saturate(180%) blur(20px);
  -webkit-backdrop-filter: saturate(180%) blur(20px);
  border: 1px solid var(--border);
  border-radius: var(--radius-2xl);
  padding: 12px 24px;
  box-shadow: var(--shadow-sm);
}

.nav-brand {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  user-select: none;
}

.brand-dot {
  width: 10px;
  height: 10px;
  border-radius: 3px;
  background: var(--accent);
  box-shadow: 0 0 12px var(--accent-glow);
}

.brand-text {
  font-family: var(--serif);
  font-size: 19px;
  font-weight: 700;
  color: var(--text);
  letter-spacing: -0.01em;
}

.nav-menu {
  display: flex;
  gap: 4px;
  background: var(--bg-alt);
  border-radius: var(--radius-2xl);
  padding: 4px;
}

.nav-btn {
  padding: 8px 22px;
  border: none;
  border-radius: var(--radius-xl);
  background: transparent;
  color: var(--text-secondary);
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s cubic-bezier(0.25, 0.1, 0.25, 1);
  font-family: var(--sans);
  white-space: nowrap;
}

.nav-btn:hover {
  color: var(--text);
  background: rgba(212, 120, 76, 0.06);
}

.nav-btn.active {
  background: var(--surface);
  color: var(--accent);
  font-weight: 600;
  box-shadow: 0 1px 4px rgba(61, 44, 32, 0.06);
}

@media (max-width: 768px) {
  .navbar {
    padding: 8px 12px;
  }

  .navbar-inner {
    border-radius: var(--radius-xl);
    padding: 10px 16px;
  }

  .brand-text {
    display: none;
  }

  .nav-btn {
    padding: 6px 14px;
    font-size: 12px;
  }
}
</style>
