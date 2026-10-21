import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import './style.css'
import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(ElementPlus)

// 启动时尝试获取用户信息（非阻塞）
import { useAuthStore } from './stores/auth'
const authStore = useAuthStore()
authStore.fetchUser().catch(() => {
  // 静默失败，不影响应用启动
})

app.mount('#app')
