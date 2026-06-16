<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import Sidebar from './components/Sidebar.vue'

const route = useRoute()
const isAdminRoute = computed(() => route.path.startsWith('/admin'))
</script>

<template>
  <div class="app-container">
    <template v-if="!isAdminRoute">
      <div class="main-layout">
        <Sidebar />
        <main class="content-area">
          <router-view />
        </main>
      </div>
    </template>
    <template v-else>
      <router-view />
    </template>
  </div>
</template>

<style scoped>
.app-container {
  min-height: 100vh;
  background: var(--bg);
}

.main-layout {
  display: flex;
  gap: 36px;
  max-width: 1280px;
  margin: 0 auto;
  padding: 40px 36px 80px;
}

.content-area {
  flex: 1;
  min-width: 0;
}

@media (max-width: 768px) {
  .main-layout {
    flex-direction: column;
    padding: 32px 20px 48px;
    gap: 28px;
  }
}
</style>
