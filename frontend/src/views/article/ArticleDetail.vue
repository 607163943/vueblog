<template>
  <div class="article-detail">
    <div class="author">
      <!-- 作者信息 -->
      <div class="author-info">
        <div class="author-pic">
          <el-avatar :size="100" :src="articleDetail.avatar ? articleDetail.avatar : defaultAvatar"></el-avatar>
        </div>
        <div class="author-name">{{ articleDetail.author }}</div>
      </div>
      <!-- 作者创作信息 -->
      <div class="author-publish-info">
        <!-- 作者发布文章数 -->
        <el-row>
          <el-col :span="8" :offset="4">创作</el-col>
          <el-col :span="12">最近发布</el-col>
        </el-row>
        <el-row>
          <el-col :span="3" :offset="4" style="text-align: center;">{{ authorPublishCount.publishCount }}</el-col>
          <el-col :span="2" :offset="7" style="text-align: center;">{{ authorPublishCount.recentPublishCount }}</el-col>
        </el-row>
      </div>

      <!-- 作者创作快捷按钮 -->
      <div class="author-button" v-if="this.ownArticle">
        <el-button type="primary" size="small" icon="el-icon-edit" @click="toEdit">编辑此文章</el-button>
        <el-button type="danger" size="small" icon="el-icon-delete" @click="toDelete">删除此文章</el-button>
      </div>
    </div>
    <div class="article">
      <!-- 文章标题 -->
      <div class="article-title">
        <h1>{{ articleDetail.title }}</h1>
      </div>
      <!-- 文章元信息 -->
      <div class="author-meta">
        <!-- 发布时间 -->
        <div class="publish-date">
          <i class="el-icon-date"></i>
          {{ articleDetail.updateTime }}
        </div>
      </div>
      <!-- 文章内容 -->
      <div class="article-content">
        <div class="markdown-body" v-html="articleDetail.contentHtml"></div>
      </div>
    </div>
  </div>
</template>

<script>
import 'github-markdown-css'
import { articleBatchDeleteService, articleGetDetailService, articleAuthorInfoService } from '@/api/article'
import MDUtils from '@/utils/MDUtils'
export default {
  data () {
    return {
      // 文章详情
      articleDetail: {
        // 作者
        author: '',
        // 作者id
        userId: '',
        // 作者头像
        avatar: '',
        // 文章id
        id: '',
        // 标题
        title: '',
        // 内容
        content: '',
        // Html内容
        contentHtml: '',
        // 发布时间
        updateTime: ''
      },
      // 作者创作统计数据
      authorPublishCount: {
        // 发布文章数
        publishCount: 0,
        // 最近发布文章数
        recentPublishCount: 0
      },
      // 默认头像
      defaultAvatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
      // 是否为作者
      ownArticle: false
    }
  },
  created () {
    this.getArticle()
  },
  methods: {
    // 获取文章详情
    async getArticle () {
      const articleId = this.$route.params.articleId
      if (articleId) {
        const res = await articleGetDetailService(articleId)
        this.articleDetail = res.data.data
        // 进行css渲染
        const result = MDUtils.render(this.articleDetail.content)

        this.articleDetail.contentHtml = result
        // 判断是否为该作者，是才能编辑
        this.ownArticle = this.articleDetail.userId === this.$store.state.user.userInfo.id
        // 获取作者文章创作信息
        this.getAuthorCreateInfo(this.articleDetail.userId)
      }
    },
    // 获取作者文章创作信息
    async getAuthorCreateInfo (authorId) {
      try {
        const res = await articleAuthorInfoService(authorId)
        this.authorPublishCount = res.data.data
      } catch (error) {
        this.$message.error('获取作者创作数据失败！')
      }
    },
    // 跳转到编辑页面
    toEdit () {
      if (!this.ownArticle) {
        this.$message({
          type: 'error',
          message: '对不起！你并非博文发布者无法编辑修改！'
        })
      } else {
        this.$router.push({
          name: 'BlogEdit',
          params: { userId: this.articleDetail.id }
        })
      }
    },
    async toDelete () {
      try {
        await this.$confirm('是否删除该博文？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        if (this.ownArticle) {
          this.$message({
            type: 'error',
            message: '删除失败！对不起，你并非博文作者无权限删除!'
          })
          return
        }

        try {
          await articleBatchDeleteService([this.articleDetail.id])
          // 跳转到首页
          this.$router.push({ name: 'Home' })
          this.$message({
            type: 'success',
            message: '删除成功!'
          })
        } catch (error) {
          this.$message({
            type: 'error',
            message: '删除失败！'
          })
        }
      } catch (error) { }
    }
  }
}
</script>

<style lang="less" scoped>
.article-detail {
  display: flex;
  justify-content: space-between;
  margin-top: -60px;
  padding: 0 72px;
  padding-top: 60px;
  padding-bottom: 16px;
  min-height: 84vh;
  background-color: #f2f2f2;
}

.author {
  border-radius: 6px;
  padding: 16px;
  width: 22%;
  height: 220px;
  background-color: #fff;
}

.author-info {
  display: flex;
  align-items: center;
}

.author-pic {
  margin-left: 10px;

  ::v-deep(.el-avatar--circle) {
    border: 1px solid #efefef;
  }
}

.author-name {
  margin-left: 16px;
  font-size: 32px;
  color: #222226;
}

.author-button {
  display: flex;
  justify-content: space-around;
}

.article {
  border-radius: 6px;
  padding: 16px;
  width: 77%;
  background-color: #fff;
}

.author-publish-info {
  margin-top: 10px;
}

.publish-date {
  margin-top: 12px;
  margin-bottom: 20px;
  color: #888888;
}
</style>
