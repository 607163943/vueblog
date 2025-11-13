<template>
  <div class="article-detail">
    <!-- 当前查看文章 -->
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
    <!-- 作者 -->
    <div class="author">
      <!-- 作者信息 -->
      <div class="author-top">
        <!-- 作者信息 -->
        <div class="author-info">
          <div class="author-pic">
            <el-avatar :size="60" :src="articleDetail.avatar ? articleDetail.avatar : defaultAvatar"></el-avatar>
          </div>
          <div class="author-name">{{ articleDetail.author }}</div>
        </div>
        <!-- 作者创作快捷按钮 -->
        <div class="author-button" v-if="ownArticle">
          <el-button type="text" size="mini" icon="el-icon-edit" @click="toEdit">编辑此文章</el-button>
          <el-button type="text" class="button-danger" style="color: #F56C6C;" size="mini" icon="el-icon-delete"
            @click="toDelete">删除此文章</el-button>
        </div>
        <!-- 作者创作信息 -->
        <div class="author-publish-info" :style="{ marginTop: ownArticle ? '0px' : '20px' }">
          <!-- 作者发布文章数 -->
          <el-row>
            <el-col :span="4" :offset="6">创作</el-col>
            <el-col :span="2" :offset="0">{{ authorPublishCount.publishCount }}</el-col>
            <el-col :span="4">发布</el-col>
            <el-col :span="2">{{ authorPublishCount.publishCount }}</el-col>
          </el-row>
        </div>
        <div class="author-top-part">—— 作者 ——</div>
      </div>
      <!-- 最新文章列表 -->
      <div class="author-bottom">
        <div class="author-bottom-part">—— 最新文章 ——</div>
        <div class="author-new-article-list">
          <div v-for="item in newArticleList" :key="item.id" class="new-article">
            <router-link :to="`/article/${item.id}`">
              <div class="new-article-title">{{ item.title }}</div>
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import 'github-markdown-css'
import { articleBatchDeleteService, articleGetDetailService, articleAuthorInfoService, articleGetNewArticleListService } from '@/api/article'
import MDUtils from '@/utils/MDUtils'
export default {
  data () {
    return {
      // 最新文章集合
      newArticleList: [],
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
    this.getArticleData()
  },
  watch: {
    // 监听文章id变化
    '$route.params.articleId' () {
      this.getArticleData()
    }
  },
  methods: {
    // 获取文章数据
    async getArticleData () {
      const loading = this.$loading({
        lock: true,
        text: 'Loading',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
      this.clearArticleData()
      await Promise.all([
        this.getArticle(),
        this.getNewArticleList()
      ])
      loading.close()
    },
    // 清理文章旧数据
    clearArticleData () {
      this.newArticleList = []
      this.articleDetail = {
        author: '',
        userId: '',
        avatar: '',
        id: '',
        title: '',
        content: '',
        contentHtml: '',
        updateTime: ''
      }
      this.authorPublishCount = {
        publishCount: 0,
        recentPublishCount: 0
      }
      this.ownArticle = false
    },
    // 获取最新文章集合
    async getNewArticleList () {
      try {
        const res = await articleGetNewArticleListService()
        this.newArticleList = res.data.data
      } catch (error) {
        this.$message.error('获取最新文章失败！')
      }
    },
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
  position: fixed;
  top: 100px;
  right: 32px;
  width: 19%;

  .author-top {
    position: relative;
    border-radius: 6px;
    padding: 16px;
    height: 160px;
    background-color: #fff;
    box-shadow: rgba(0, 0, 0, 0.1) 0px 2px 12px 0px;
  }

  .author-info {
    display: flex;
    align-items: center;
    border-bottom: 1px solid #eee;

    .author-pic {
      margin-left: 32px;

      ::v-deep(.el-avatar--circle) {
        border: 1px solid #efefef;
      }
    }

    .author-name {
      margin-left: 24px;
      font-size: 24px;
      color: #222226;
    }
  }

  .author-button {
    width: 80%;
    margin: 0 auto;
    display: flex;
    justify-content: space-around;

    .button-danger {
      &:hover {
        color: #f78989 !important;
      }
    }
  }

  .author-publish-info {
    color: #606266;
    font-size: 14px;
  }

  .author-top-part {
    position: absolute;
    bottom: 0;
    left: 50%;
    transform: translateX(-50%);
    text-align: center;
    color: #606266;
  }

  .author-bottom {
    position: relative;
    margin-top: 16px;
    border-radius: 6px;
    padding: 16px;
    background-color: #fff;
    box-shadow: rgba(0, 0, 0, 0.1) 0px 2px 12px 0px;
  }

  .author-bottom-part {
    position: absolute;
    top: 0;
    left: 50%;
    width: 100%;
    transform: translateX(-50%);
    text-align: center;
    color: #606266;
  }

  .author-new-article-list {
    margin-top: 12px;
    padding-left: 12px;

    .new-article {
      margin-bottom: 16px;
    }

    .new-article-title {
      line-height: 16px;
      color: #606266;
      word-break: break-all;
      transition: all 0.3s;

      &:hover {
        color: #101932;

      }
    }
  }
}

.article {
  border-radius: 6px;
  padding: 16px;
  width: 80%;
  background-color: #fff;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 2px 12px 0px;
}

.publish-date {
  margin-bottom: 10px;
  color: #888888;
}
</style>
