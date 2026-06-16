<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { articleApi } from '../../api/article'
import { ArticleType } from '../../types'
import type { ArticleRequest } from '../../types'
import { ElMessage } from 'element-plus'
import { ArrowLeft, Save, Upload, ImagePlus } from 'lucide-vue-next'
import { marked } from 'marked'

// 配置 marked
marked.setOptions({ breaks: true, gfm: true })

const route = useRoute()
const router = useRouter()

const isEdit = ref(false)
const saving = ref(false)
const uploading = ref(false)
const uploadProgress = ref('')
const articleId = ref<number | null>(null)

// 图片上传 input 引用
const multiFileInput = ref<HTMLInputElement | null>(null)

const form = reactive<ArticleRequest>({
  title: '',
  content: '',
  summary: '',
  coverImage: '',
  type: ArticleType.ARTICLE,
})

const typeOptions = [
  { label: '文章', value: ArticleType.ARTICLE },
  { label: '项目', value: ArticleType.PROJECT },
]

onMounted(async () => {
  const id = route.params.id
  if (id) {
    isEdit.value = true
    articleId.value = Number(id)
    try {
      const article = await articleApi.getById(Number(id))
      form.title = article.title
      form.content = article.content
      form.summary = article.summary
      form.coverImage = article.coverImage || ''
      form.type = article.type
    } catch {
      ElMessage.error('加载文章失败')
      router.push('/admin')
    }
  }
})

async function handleSave() {
  if (!form.title.trim()) {
    ElMessage.warning('请输入标题')
    return
  }
  if (!form.summary.trim()) {
    ElMessage.warning('请输入摘要')
    return
  }

  saving.value = true
  try {
    const payload = { ...form }
    if (isEdit.value && articleId.value) {
      await articleApi.update(articleId.value, payload)
      ElMessage.success('更新成功')
    } else {
      await articleApi.create(payload)
      ElMessage.success('发布成功')
    }
    router.push('/admin')
  } catch {
    // 静默处理
  } finally {
    saving.value = false
  }
}

async function handleImageUpload(e: Event) {
  const target = e.target as HTMLInputElement
  const file = target.files?.[0]
  if (!file) return

  uploadProgress.value = '上传封面图...'
  uploading.value = true
  try {
    const formData = new FormData()
    formData.append('file', file)
    const result = await articleApi.uploadImage(formData)
    form.coverImage = result.url
    ElMessage.success('图片上传成功')
  } catch {
    // 静默处理
  } finally {
    uploading.value = false
    uploadProgress.value = ''
  }
}

async function handleMultiImageUpload(e: Event) {
  const target = e.target as HTMLInputElement
  const files = target.files
  if (!files || files.length === 0) return

  uploadProgress.value = `上传 ${files.length} 张图片...`
  uploading.value = true
  try {
    const formData = new FormData()
    for (let i = 0; i < files.length; i++) {
      formData.append('files', files[i])
    }
    const result = await articleApi.uploadImages(formData)
    // 将图片以 Markdown 格式插入到正文中
    const mdImages = result.urls.map((url: string) => `![](${url})`).join('\n')
    if (form.content) {
      form.content += '\n' + mdImages
    } else {
      form.content = mdImages
    }
    ElMessage.success(`成功上传 ${result.urls.length} 张图片，已插入到正文`)
  } catch {
    // 静默处理
  } finally {
    uploading.value = false
    uploadProgress.value = ''
    if (multiFileInput.value) multiFileInput.value.value = ''
  }
}
</script>

<template>
  <div class="editor-page">
    <div class="content-wrapper">
      <!-- 顶部栏 -->
      <div class="editor-header">
        <button class="back-btn" @click="router.push('/admin')">
          <ArrowLeft :size="18" />
          <span>返回</span>
        </button>
        <h2 class="editor-title">{{ isEdit ? '编辑内容' : '发布内容' }}</h2>
        <button class="save-btn" :disabled="saving" @click="handleSave">
          <Save :size="18" />
          <span>{{ saving ? '保存中...' : '保存' }}</span>
        </button>
      </div>

      <!-- 表单 -->
      <div class="editor-form">
        <!-- 类型选择 -->
        <div class="form-group">
          <label>内容类型</label>
          <div class="type-selector">
            <button
              v-for="opt in typeOptions"
              :key="opt.value"
              class="type-btn"
              :class="{ active: form.type === opt.value }"
              @click="form.type = opt.value"
            >
              {{ opt.label }}
            </button>
          </div>
        </div>

        <!-- 标题 -->
        <div class="form-group">
          <label>标题</label>
          <input v-model="form.title" type="text" placeholder="请输入标题" maxlength="100" />
        </div>

        <!-- 摘要 -->
        <div class="form-group">
          <label>摘要</label>
          <textarea v-model="form.summary" placeholder="请输入简短摘要" rows="2" maxlength="200"></textarea>
        </div>

        <!-- 封面图 -->
        <div class="form-group">
          <label>封面图片</label>
          <div class="cover-upload">
            <div v-if="form.coverImage" class="cover-preview">
              <img :src="form.coverImage" alt="封面预览" />
              <button class="remove-cover" @click="form.coverImage = ''">×</button>
            </div>
            <label class="upload-label" :class="{ uploading }">
              <Upload :size="18" />
              <span>{{ uploadProgress || '上传封面图' }}</span>
              <input type="file" accept="image/*" hidden @change="handleImageUpload" />
            </label>
          </div>
        </div>

        <!-- 图片上传（插入到正文） -->
        <div class="form-group">
          <label>插入图片（可一次选择多张，自动插入 Markdown 语法到正文）</label>
          <label class="multi-upload-btn inline-upload" :class="{ uploading }">
            <ImagePlus :size="24" />
            <span>{{ uploadProgress || '选择图片' }}</span>
            <input
              ref="multiFileInput"
              type="file"
              accept="image/*"
              multiple
              hidden
              @change="handleMultiImageUpload"
            />
          </label>
        </div>

        <!-- 正文（Markdown） -->
        <div class="form-group">
          <label>正文内容（支持 Markdown 格式）</label>
          <textarea
            v-model="form.content"
            placeholder="支持 Markdown 语法：&#10;# 标题&#10;**粗体**&#10;![图片](url)&#10;..."
            rows="18"
            class="content-editor"
          ></textarea>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.editor-page {
  width: 100%;
}

.content-wrapper {
  background: var(--surface);
  border-radius: var(--radius-xl);
  padding: 36px;
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--border);
}

.editor-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 36px;
  padding-bottom: 24px;
  border-bottom: 1px solid var(--border);
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
  font-family: var(--sans);
  transition: color 0.2s;
}

.back-btn:hover {
  color: var(--accent);
}

.editor-title {
  font-family: var(--serif);
  font-size: 22px;
  font-weight: 700;
  color: var(--text);
  margin: 0;
}

.save-btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 11px 26px;
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

.save-btn:hover:not(:disabled) {
  background: var(--accent-hover);
  transform: translateY(-1px);
  box-shadow: 0 4px 16px var(--accent-glow);
}

.save-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.editor-form {
  display: flex;
  flex-direction: column;
  gap: 28px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  font-size: 12px;
  font-weight: 600;
  color: var(--text-secondary);
  letter-spacing: 0.02em;
  text-transform: uppercase;
}

.form-group input,
.form-group textarea {
  padding: 13px 16px;
  border: 1.5px solid var(--border);
  border-radius: 10px;
  font-size: 15px;
  color: var(--text);
  background: var(--bg);
  outline: none;
  resize: vertical;
  font-family: var(--sans);
  transition: border-color 0.2s;
}

.form-group input:focus,
.form-group textarea:focus {
  border-color: var(--accent);
  box-shadow: 0 0 0 3px var(--accent-glow);
}

.type-selector {
  display: flex;
  gap: 8px;
}

.type-btn {
  padding: 9px 22px;
  border: 1.5px solid var(--border);
  border-radius: var(--radius-xl);
  background: var(--surface);
  font-size: 13px;
  font-weight: 500;
  color: var(--text-secondary);
  cursor: pointer;
  transition: all 0.15s;
  font-family: var(--sans);
}

.type-btn:hover {
  border-color: var(--accent);
  color: var(--accent);
}

.type-btn.active {
  background: var(--accent);
  border-color: var(--accent);
  color: #fff;
  font-weight: 600;
}

.cover-upload {
  display: flex;
  gap: 12px;
  align-items: center;
}

.cover-preview {
  position: relative;
  width: 120px;
  height: 80px;
  border-radius: 10px;
  overflow: hidden;
}

.cover-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.remove-cover {
  position: absolute;
  top: 4px;
  right: 4px;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: rgba(61, 44, 32, 0.6);
  backdrop-filter: blur(4px);
  color: #fff;
  border: none;
  cursor: pointer;
  font-size: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  line-height: 1;
  transition: background 0.2s;
}

.remove-cover:hover {
  background: rgba(61, 44, 32, 0.8);
}

.upload-label {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 10px 20px;
  border: 1.5px dashed var(--border);
  border-radius: 10px;
  font-size: 13px;
  color: var(--text-muted);
  cursor: pointer;
  transition: all 0.2s;
  font-family: var(--sans);
}

.upload-label:hover {
  border-color: var(--accent);
  color: var(--accent);
  background: var(--accent-subtle);
}

.upload-label.uploading {
  opacity: 0.6;
  cursor: wait;
}

.inline-upload {
  display: inline-flex;
  flex-direction: row;
  align-items: center;
  gap: 8px;
  padding: 16px 32px;
  border: 2px dashed var(--border);
  border-radius: 14px;
  cursor: pointer;
  color: var(--text-muted);
  font-size: 14px;
  transition: all 0.2s;
  font-family: var(--sans);
  width: auto;
  height: auto;
}

.inline-upload:hover {
  border-color: var(--accent);
  color: var(--accent);
  background: var(--accent-subtle);
}

.inline-upload.uploading {
  opacity: 0.6;
  cursor: wait;
}

.content-editor {
  font-family: var(--mono);
  font-size: 14px;
  line-height: 1.7;
}
</style>
