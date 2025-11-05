<template>
  <div>
    <div class="content">
      <h1>{{ articleDetail.title }}</h1>
      <div class="new-meta-box">
        <div class="new-meta-item">
          <i class="el-icon-user-solid" aria-hidden="true"></i>
          <h5>{{ articleDetail.author }}</h5>
        </div>
        <div class="new-meta-item date">
          <i class="el-icon-s-order" aria-hidden="true"></i>
          <a class="notlink">
            <p>{{ articleDetail.gmtCreate }}</p>
          </a>
        </div>
      </div>

      <el-button-group class="button">
        <el-button type="primary" icon="el-icon-edit" @click="toEdit"
          >编辑</el-button
        >
        <el-button type="danger" icon="el-icon-delete" @click="toDelete"
          >删除</el-button
        >
      </el-button-group>

      <el-divider>
        <i class="el-icon-reading"></i>
      </el-divider>
      <div class="markdown-body" v-html="articleDetail.contentHtml"></div>
    </div>
  </div>
</template>

<script>
import { articleGetService } from '@/api/article'
import 'github-markdown-css'
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
        avatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
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
      ownBlog: false
    }
  },
  created () {
    this.getArticle()
  },
  methods: {
    // 获取文章
    async getArticle () {
      const userId = this.$route.params.userId
      if (userId) {
        const res = await articleGetService(userId)
        this.articleDetail = res.data.data
        // 进行css渲染
        const MardownIt = require('markdown-it')
        const md = new MardownIt()

        const result = md.render(this.articleDetail.content)

        this.articleDetail.contentHtml = result
        // 判断是否为该作者，是才能编辑
        this.ownBlog = this.articleDetail.userId === this.$store.state.user.userInfo.id
      }
    },
    toEdit () {
      if (this.$store.state.user.userInfo.id !== this.blog.userId) {
        this.$message({
          type: 'error',
          message: '对不起！你并非博文发布者无法编辑修改！'
        })
      } else {
        this.$router.push({
          name: 'BlogEdit',
          params: { blogId: this.blog.id }
        })
      }
    },
    toDelete () {
      this.$confirm('是否删除该博文？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          if (this.$store.getters.getUserInfo.id !== this.blog.userId) {
            this.$message({
              type: 'error',
              message: '删除失败！对不起，你并非博文作者无权限删除!'
            })
          } else {
            this.$axios
              .get('/blog/delete/' + this.blog.id, {
                headers: {
                  Authorization: localStorage.getItem('token')
                }
              })
              .then((res) => {
                this.$router.push({ name: 'Blog' })
              })
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
          }
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
    }
  }
}
</script>

<style scoped>
.button {
  padding-left: 200px;
}
.new-meta-item a {
  text-decoration: none;
  color: rgba(68, 68, 68, 0.65);
  padding-left: 0;
  padding-right: 4px;
}
.new-meta-box {
  padding-left: 400px;
  -webkit-transition: all 0.1s ease;
  font-size: 0.8125rem;
  padding-top: px;
  padding-bottom: 2px;
  display: flex;
  align-items: center;
  float: left;
  flex-wrap: wrap;
}
.new-meta-item {
  color: rgba(68, 68, 68, 0.65);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2px;
  margin: 0 8px 0 0;
  border-radius: 4px;
}
.content {
  padding: 20px 15px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  width: 100%;
  min-height: 700px;
}
.edit {
  color: skyblue;
  text-decoration: none;
}
.edit:hover {
  color: red;
  scale: 1;
}
h1 {
  padding: calc(var(--sp) / 2) 0 var(--sp);
  text-align: center;
  transition: color 0.6s;
  color: var(--t);
  margin: 2.75rem 0 1rem;
  font-family: Product Sans, Oswald, -apple-system, BlinkMacSystemFont, Segoe UI,
    Helvetica, Arial, sans-serif;
  font-weight: 600;
  line-height: 1.5;
}
</style>
