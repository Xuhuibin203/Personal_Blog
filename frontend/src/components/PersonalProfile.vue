<script setup lang="ts">
import {
  User, MapPin, Phone, Mail, Briefcase, Award,
  GraduationCap, Code2, Star, ExternalLink, ChevronRight,
  Target, FolderGit2, Building2
} from 'lucide-vue-next'
</script>

<template>
  <div class="profile-root">

    <!-- ========== 基本信息 ========== -->
    <section class="section-card">
      <h2 class="section-heading">
        <User :size="18" />
        <span>基本信息</span>
      </h2>
      <div class="info-grid">
        <div class="info-item">
          <span class="info-label">性别</span>
          <span class="info-value">男</span>
        </div>
        <div class="info-item">
          <span class="info-label">出生年月</span>
          <span class="info-value">2003-06</span>
        </div>
        <div class="info-item">
          <span class="info-label">
            <Phone :size="13" />
            <span>电话</span>
          </span>
          <span class="info-value">130******60</span>
        </div>
        <div class="info-item">
          <span class="info-label">参加工作时间</span>
          <span class="info-value">2022-07</span>
        </div>
        <div class="info-item">
          <span class="info-label">微信号</span>
          <span class="info-value">Jin******22</span>
        </div>
        <div class="info-item">
          <span class="info-label">
            <Mail :size="13" />
            <span>邮箱</span>
          </span>
          <span class="info-value">298*****36@qq.com</span>
        </div>
      </div>
    </section>

    <!-- ========== 个人优势 ========== -->
    <section class="section-card">
      <h2 class="section-heading">
        <Award :size="18" />
        <span>个人优势</span>
      </h2>
      <div class="strengths-list">
        <div class="strength-item highlight">
          <span class="strength-badge">🏅</span>
          <span>浙江省第二十届大学生程序设计竞赛（ACM）<strong>银奖</strong></span>
        </div>
        <div class="strength-item highlight">
          <span class="strength-badge">🥇</span>
          <span>2022/2023 睿抗机器人开发者大赛（RAICOM）全国总决赛编程技能竞赛项目<strong>一等奖</strong></span>
        </div>
        <div class="strength-item highlight">
          <span class="strength-badge">🥇</span>
          <span>全国团体程序设计天梯赛团队<strong>一等奖</strong></span>
        </div>
        <div class="strength-item highlight">
          <span class="strength-badge">🥈</span>
          <span>第十三届蓝桥杯全国软件和信息技术专业人才大赛浙江赛区 C/C++ 程序设计大学C组<strong>二等奖</strong></span>
        </div>
        <div class="divider-dot"></div>
        <div class="strength-item">
          <ChevronRight :size="14" class="strength-arrow" />
          <span>掌握 TypeScript 与 Java 语言，<strong>精通 C/C++</strong></span>
        </div>
        <div class="strength-item">
          <ChevronRight :size="14" class="strength-arrow" />
          <span>熟悉常用的数据结构与算法，掌握 MVC 框架</span>
        </div>
        <div class="strength-item">
          <ChevronRight :size="14" class="strength-arrow" />
          <span>能独立开发 Android 项目且生成 .apk 文件</span>
        </div>
        <div class="strength-item">
          <ChevronRight :size="14" class="strength-arrow" />
          <span>熟悉 C++ 从 0 到 1 的算法课程流程</span>
        </div>
        <div class="strength-item">
          <ChevronRight :size="14" class="strength-arrow" />
          <span>曾在杭州小码王担任过信奥教师，擅长找出代码 bug</span>
        </div>
      </div>
    </section>

    <!-- ========== 期望职位 ========== -->
    <section class="section-card">
      <h2 class="section-heading">
        <Target :size="18" />
        <span>期望职位</span>
      </h2>
      <div class="position-cards">
        <div class="position-card">
          <div class="pos-top">
            <Code2 :size="20" class="pos-icon" />
            <div>
              <h3>图像算法</h3>
              <span class="pos-salary">4-6K</span>
            </div>
          </div>
          <div class="pos-bottom">
            <MapPin :size="13" />
            <span>杭州</span>
          </div>
        </div>
        <div class="position-card">
          <div class="pos-top">
            <GraduationCap :size="20" class="pos-icon" />
            <div>
              <h3>机器人教师</h3>
              <span class="pos-salary">7-12K</span>
            </div>
          </div>
          <div class="pos-bottom">
            <MapPin :size="13" />
            <span>杭州 · 台州 · 宁波</span>
          </div>
        </div>
        <div class="position-card">
          <div class="pos-top">
            <Star :size="20" class="pos-icon" />
            <div>
              <h3>少儿编程老师</h3>
              <span class="pos-salary">7-12K</span>
            </div>
          </div>
          <div class="pos-bottom">
            <MapPin :size="13" />
            <span>杭州 · 台州 · 宁波</span>
          </div>
        </div>
      </div>
    </section>

    <!-- ========== 工作经历 ========== -->
    <section class="section-card">
      <h2 class="section-heading">
        <Briefcase :size="18" />
        <span>工作经历</span>
      </h2>
      <div class="timeline">
        <div class="tl-item">
          <div class="tl-marker">
            <div class="tl-dot"></div>
            <div class="tl-line"></div>
          </div>
          <div class="tl-card">
            <div class="tl-header">
              <Building2 :size="16" class="tl-company-icon" />
              <div>
                <h3>杭州小码教育科技有限公司</h3>
                <p class="tl-role">少儿编程老师 · <span class="tl-type">实习</span></p>
              </div>
              <span class="tl-date">2022.07 — 2022.09</span>
            </div>
            <ul class="tl-duties">
              <li>信奥 C/C++ 算法设计教学与培训</li>
              <li>周赛题目创建构造与讲解</li>
              <li>线上算法指导</li>
            </ul>
          </div>
        </div>
      </div>
    </section>

    <!-- ========== 项目经历 ========== -->
    <section class="section-card">
      <h2 class="section-heading">
        <FolderGit2 :size="18" />
        <span>项目经历</span>
      </h2>

      <!-- 项目 1 -->
      <div class="project-block">
        <div class="proj-header">
          <h3>基于 Cocos Creator 的像素对决的设计与实现</h3>
          <span class="proj-meta">个人开发 · 2024.04 — 2024.06</span>
        </div>
        <ul class="proj-points">
          <li>场景分为战斗、大厅、登录、房间</li>
          <li>实体分为角色、子弹、爆炸、武器</li>
          <li>通过 WebSocket 进行服务端与客户端双向通信</li>
        </ul>
        <a href="https://github.com/Xuhuibin203/cocosIoProject" target="_blank" rel="noopener" class="proj-link">
          <ExternalLink :size="14" />
          <span>查看项目</span>
        </a>
      </div>

      <!-- 项目 2 -->
      <div class="project-block">
        <div class="proj-header">
          <h3>基于 Cocos Creator 的 2048 游戏设计与实现</h3>
          <span class="proj-meta">个人开发 · 2024.04</span>
        </div>
        <ul class="proj-points">
          <li>前端通过预制体、挂载/删除组件实现视觉效果</li>
          <li>底层基于二维数组管理游戏状态，数据驱动渲染</li>
          <li>通过监听用户按键调用对应方法实现操作响应</li>
        </ul>
        <div class="proj-gains">
          <span class="gains-label">收获：</span>
          <span>预制体制作与组件管理 / @property 属性声明 / console.log 调试 / 方法封装复用</span>
        </div>
      </div>

      <!-- 项目 3 -->
      <div class="project-block">
        <div class="proj-header">
          <h3>基于 Java 的超级玛丽设计与实现</h3>
          <span class="proj-meta">个人开发 · 2024.01 — 2024.03</span>
        </div>
        <ul class="proj-points">
          <li>设计三关，难度递增，关卡个人设计</li>
          <li>实现角色类：响应玩家操控实时跳跃跑动</li>
          <li>障碍物类：墙体不可破坏，奖励方块可被顶击消失</li>
          <li>敌人类：蘑菇敌人自动移动，触碰游戏结束，踩踏则死亡</li>
        </ul>
      </div>
    </section>

    <!-- ========== 教育经历 ========== -->
    <section class="section-card">
      <h2 class="section-heading">
        <GraduationCap :size="18" />
        <span>教育经历</span>
      </h2>
      <div class="edu-block">
        <div class="edu-header">
          <h3>浙江育英职业技术学院</h3>
          <span class="edu-date">2021 — 2024</span>
        </div>
        <p class="edu-major">计算机及应用 · 大专 · <strong class="edu-rank">专业排名：前 1%</strong></p>
        <p class="edu-courses">主修课程：计算机基础、C/Java 语言程序设计、程序设计基础、数据结构与算法分析、前端设计与开发</p>
        <p class="edu-thesis">毕设/论文：基于 Java 的超级玛丽游戏的设计与实现</p>
        <div class="edu-experiences">
          <div class="exp-row">
            <span class="exp-year">大一</span>
            <span>加入 ACM 社，校内排名前十；多次与金融、机电、宁职等学校举办校编程赛；参加杭电新生算法赛</span>
          </div>
          <div class="exp-row">
            <span class="exp-year">大二</span>
            <span>正式加入育英创意班（ACM班），开始算法赛事之旅；除寒假外每日机房训练赛；每周四杭电刘春英教练授课</span>
          </div>
        </div>
      </div>
    </section>

    <!-- ========== 专业技能 ========== -->
    <section class="section-card">
      <h2 class="section-heading">
        <Code2 :size="18" />
        <span>专业技能</span>
      </h2>
      <div class="skill-categories">
        <div class="skill-cat">
          <span class="cat-label">游戏 / 前端</span>
          <div class="skill-tags">
            <span class="skill-tag">Cocos Creator 3.x</span>
            <span class="skill-tag">JavaScript</span>
            <span class="skill-tag">TypeScript</span>
            <span class="skill-tag">Node.js</span>
          </div>
        </div>
        <div class="skill-cat">
          <span class="cat-label">编程语言</span>
          <div class="skill-tags">
            <span class="skill-tag primary">C/C++</span>
            <span class="skill-tag">Java</span>
            <span class="skill-tag">TypeScript</span>
          </div>
        </div>
        <div class="skill-cat">
          <span class="cat-label">算法</span>
          <div class="skill-tags">
            <span class="skill-tag">最短路</span>
            <span class="skill-tag">拓扑排序</span>
            <span class="skill-tag">最小生成树</span>
            <span class="skill-tag">Floyd</span>
            <span class="skill-tag">二叉树</span>
            <span class="skill-tag">DFS / BFS</span>
            <span class="skill-tag">二分</span>
            <span class="skill-tag">递归</span>
            <span class="skill-tag">DP</span>
            <span class="skill-tag">博弈论</span>
            <span class="skill-tag">位运算</span>
          </div>
        </div>
        <div class="skill-cat">
          <span class="cat-label">数据结构</span>
          <div class="skill-tags">
            <span class="skill-tag">链式前向星</span>
            <span class="skill-tag">并查集</span>
            <span class="skill-tag">队列</span>
            <span class="skill-tag">栈</span>
            <span class="skill-tag">单链表</span>
            <span class="skill-tag">单调栈</span>
          </div>
        </div>
      </div>
    </section>

  </div>
</template>

<style scoped>
.profile-root {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* ====== 通用区块 ====== */
.section-card {
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: var(--radius-xl);
  padding: 32px 36px;
  box-shadow: var(--shadow-sm);
}

.section-heading {
  display: flex;
  align-items: center;
  gap: 10px;
  font-family: var(--serif);
  font-size: 18px;
  font-weight: 700;
  color: var(--text);
  margin: 0 0 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid var(--border-light);
}

.section-heading svg {
  color: var(--accent);
}

/* ====== 基本信息网格 ====== */
.info-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 4px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
  padding: 12px 16px;
  border-radius: var(--radius-sm);
  transition: background 0.2s;
}

.info-item:hover {
  background: var(--accent-subtle);
}

.info-label {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 11px;
  font-weight: 600;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

.info-value {
  font-size: 15px;
  font-weight: 600;
  color: var(--text);
}

/* ====== 个人优势 ====== */
.strengths-list {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.strength-item {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  padding: 10px 14px;
  border-radius: var(--radius-sm);
  font-size: 14px;
  color: var(--text);
  line-height: 1.6;
  transition: background 0.15s;
}

.strength-item:hover {
  background: var(--bg);
}

.strength-item.highlight {
  background: linear-gradient(135deg, var(--accent-subtle), rgba(212, 120, 76, 0.04));
  border: 1px solid var(--accent-light);
  border-radius: var(--radius);
  margin-bottom: 4px;
}

.strength-badge {
  font-size: 18px;
  flex-shrink: 0;
  line-height: 1.4;
}

.strength-arrow {
  color: var(--accent);
  flex-shrink: 0;
  margin-top: 2px;
}

.divider-dot {
  height: 1px;
  background: var(--border);
  margin: 8px 0;
}

/* ====== 期望职位 ====== */
.position-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.position-card {
  background: var(--bg);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 14px;
  transition: all 0.25s ease;
}

.position-card:hover {
  border-color: var(--accent);
  box-shadow: var(--shadow-md);
  transform: translateY(-2px);
}

.pos-top {
  display: flex;
  align-items: flex-start;
  gap: 12px;
}

.pos-icon {
  color: var(--accent);
  flex-shrink: 0;
  margin-top: 2px;
}

.pos-top h3 {
  font-family: var(--serif);
  font-size: 16px;
  font-weight: 700;
  color: var(--text);
  margin: 0 0 4px;
}

.pos-salary {
  font-size: 14px;
  font-weight: 700;
  color: var(--accent);
}

.pos-bottom {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: var(--text-muted);
  padding-top: 12px;
  border-top: 1px solid var(--border);
}

.pos-bottom svg {
  color: var(--text-muted);
}

/* ====== 时间线（工作经历） ====== */
.timeline {
  display: flex;
  flex-direction: column;
}

.tl-item {
  display: flex;
  gap: 16px;
}

.tl-marker {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex-shrink: 0;
  padding-top: 6px;
}

.tl-dot {
  width: 12px;
  height: 12px;
  background: var(--accent);
  border-radius: 50%;
  border: 3px solid var(--accent-light);
}

.tl-line {
  width: 2px;
  flex: 1;
  background: var(--border);
  margin-top: 6px;
}

.tl-card {
  flex: 1;
  background: var(--bg);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  padding: 24px;
}

.tl-header {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  margin-bottom: 14px;
}

.tl-company-icon {
  color: var(--accent);
  flex-shrink: 0;
  margin-top: 3px;
}

.tl-header h3 {
  font-family: var(--serif);
  font-size: 16px;
  font-weight: 700;
  color: var(--text);
  margin: 0 0 4px;
}

.tl-role {
  font-size: 13px;
  color: var(--text-secondary);
  margin: 0;
}

.tl-type {
  display: inline-block;
  padding: 1px 8px;
  background: var(--accent-subtle);
  color: var(--accent);
  border-radius: 4px;
  font-size: 11px;
  font-weight: 600;
}

.tl-date {
  font-size: 13px;
  color: var(--text-muted);
  white-space: nowrap;
  margin-left: auto;
}

.tl-duties {
  margin: 0;
  padding-left: 18px;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.tl-duties li {
  font-size: 14px;
  color: var(--text);
  line-height: 1.6;
}

.tl-duties li::marker {
  color: var(--accent);
}

/* ====== 项目经历 ====== */
.project-block {
  background: var(--bg);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  padding: 24px;
  margin-bottom: 16px;
}

.project-block:last-child {
  margin-bottom: 0;
}

.proj-header {
  margin-bottom: 14px;
}

.proj-header h3 {
  font-family: var(--serif);
  font-size: 16px;
  font-weight: 700;
  color: var(--text);
  margin: 0 0 6px;
}

.proj-meta {
  font-size: 12px;
  color: var(--text-muted);
  font-weight: 500;
}

.proj-points {
  margin: 0 0 12px;
  padding-left: 18px;
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.proj-points li {
  font-size: 14px;
  color: var(--text);
  line-height: 1.6;
}

.proj-points li::marker {
  color: var(--accent);
}

.proj-gains {
  font-size: 13px;
  color: var(--text-secondary);
  background: var(--accent-subtle);
  padding: 10px 14px;
  border-radius: var(--radius-sm);
  line-height: 1.6;
}

.gains-label {
  font-weight: 600;
  color: var(--accent);
}

.proj-link {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  margin-top: 12px;
  font-size: 13px;
  font-weight: 600;
  color: var(--accent);
  text-decoration: none;
  padding: 8px 16px;
  background: var(--accent-subtle);
  border-radius: var(--radius-sm);
  transition: all 0.2s;
}

.proj-link:hover {
  background: var(--accent);
  color: #fff;
}

/* ====== 教育经历 ====== */
.edu-block {
  background: var(--bg);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  padding: 24px;
}

.edu-header {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
  margin-bottom: 8px;
}

.edu-header h3 {
  font-family: var(--serif);
  font-size: 17px;
  font-weight: 700;
  color: var(--text);
  margin: 0;
}

.edu-date {
  font-size: 13px;
  color: var(--text-muted);
  font-weight: 500;
}

.edu-major {
  font-size: 14px;
  color: var(--text-secondary);
  margin: 0 0 6px;
}

.edu-rank {
  color: var(--accent);
  font-weight: 700;
}

.edu-courses,
.edu-thesis {
  font-size: 13px;
  color: var(--text-muted);
  margin: 0 0 4px;
  line-height: 1.7;
}

.edu-experiences {
  margin-top: 16px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.exp-row {
  display: flex;
  gap: 12px;
  font-size: 13px;
  color: var(--text);
  line-height: 1.7;
}

.exp-year {
  font-family: var(--serif);
  font-weight: 700;
  color: var(--accent);
  flex-shrink: 0;
  min-width: 32px;
}

/* ====== 专业技能 ====== */
.skill-categories {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.skill-cat {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.cat-label {
  font-size: 12px;
  font-weight: 700;
  color: var(--accent);
  text-transform: uppercase;
  letter-spacing: 0.06em;
}

.skill-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.skill-tag {
  display: inline-block;
  padding: 6px 14px;
  background: var(--bg);
  border: 1px solid var(--border);
  border-radius: 20px;
  font-size: 13px;
  font-weight: 500;
  color: var(--text-secondary);
  transition: all 0.2s;
}

.skill-tag:hover {
  border-color: var(--accent);
  color: var(--accent);
  background: var(--accent-subtle);
}

.skill-tag.primary {
  background: var(--accent-subtle);
  border-color: var(--accent-light);
  color: var(--accent);
  font-weight: 600;
}

/* ====== 响应式 ====== */
@media (max-width: 768px) {
  .section-card {
    padding: 24px 20px;
  }

  .info-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .position-cards {
    grid-template-columns: 1fr;
  }

  .tl-header {
    flex-wrap: wrap;
  }

  .tl-date {
    margin-left: 0;
    width: 100%;
  }
}
</style>
