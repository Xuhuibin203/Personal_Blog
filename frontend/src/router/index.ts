import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue'),
  },
  {
    path: '/articles',
    name: 'Articles',
    component: () => import('../views/ArticlesPage.vue'),
  },
  {
    path: '/projects',
    name: 'Projects',
    component: () => import('../views/ProjectsPage.vue'),
  },
  {
    path: '/article/:id',
    name: 'ArticleDetail',
    component: () => import('../views/ArticleDetail.vue'),
  },
  {
    path: '/admin/login',
    name: 'AdminLogin',
    component: () => import('../views/admin/AdminLogin.vue'),
  },
  {
    path: '/admin',
    name: 'AdminDashboard',
    component: () => import('../views/admin/Dashboard.vue'),
    meta: { requiresAuth: true, requiresAdmin: true },
  },
  {
    path: '/admin/article/new',
    name: 'ArticleCreate',
    component: () => import('../views/admin/ArticleEditor.vue'),
    meta: { requiresAuth: true, requiresAdmin: true },
  },
  {
    path: '/admin/article/:id/edit',
    name: 'ArticleEdit',
    component: () => import('../views/admin/ArticleEditor.vue'),
    meta: { requiresAuth: true, requiresAdmin: true },
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 路由守卫
router.beforeEach(async (to, _from, next) => {
  const { useAuthStore } = await import('../stores/auth')
  const authStore = useAuthStore()

  // admin 路由需要登录
  if (to.meta.requiresAuth && !authStore.isLoggedIn) {
    next({ path: '/admin/login', query: { redirect: to.fullPath } })
    return
  }

  // admin 路由需要管理员权限：先确保用户信息已加载
  if (to.meta.requiresAdmin) {
    if (!authStore.user) {
      await authStore.fetchUser()
    }
    // fetchUser 失败会清空 token，此时 isLoggedIn 为 false
    if (!authStore.isLoggedIn) {
      next({ path: '/admin/login', query: { redirect: to.fullPath } })
      return
    }
    if (!authStore.isAdmin) {
      next('/')
      return
    }
  }

  next()
})

export default router
