<template>
  <div class="article-edit">
    <el-form
      :model="ArticleForm"
      :rules="rules"
      ref="ArticleFormRef"
      label-width="60px"
    >
      <el-form-item label="标题" prop="title">
        <el-input v-model="ArticleForm.title"></el-input>
      </el-form-item>

      <el-form-item label="简介" prop="description">
        <el-input type="textarea" v-model="ArticleForm.description"></el-input>
      </el-form-item>

      <el-form-item label="内容" prop="content">
        <mavon-editor v-model="ArticleForm.content" class="editor">
        </mavon-editor>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitForm('ArticleFormRef', 0)"
          >发布</el-button>
        <el-button @click="submitForm('ArticleFormRef', 1)"
          >保存为草稿</el-button>
        <el-button @click="resetForm('ArticleFormRef')">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { articleCreateService, articleGetService, articleUpdateService } from '@/api/article'
export default {
  data () {
    return {
      // 文章表单
      ArticleForm: {
        // 文章id
        id: '',
        // 作者id
        userId: '',
        // 标题
        title: '',
        // 简介
        description: '',
        // 内容
        content: '',
        // 状态
        status: ''
      },
      // 表单验证规则
      rules: {
        title: [
          { required: true, message: '请输入文章标题', trigger: 'blur' },
          {
            min: 2,
            max: 25,
            message: '长度在 2 到 25 个字符',
            trigger: 'blur'
          }
        ],
        description: [
          { required: true, message: '请输入摘要（简介）', trigger: 'blur' }
        ],
        content: [
          { required: true, message: '文章内容不能为空', trigger: 'blur' }
        ]
      }
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
        try {
          const res = await articleGetService(userId)

          this.ArticleForm = res.data.data
        } catch (error) {
          this.$message.error('获取文章失败！' + error.response.data.msg)
        }
      }
    },
    // 添加或修改文章
    async submitForm (formName, status) {
      await this.$refs[formName].validate()

      this.ArticleForm.status = status
      this.addArticle()
    },
    // 添加文章
    async addArticle () {
      this.ArticleForm.userId = this.$store.state.user.userInfo.id

      try {
        await articleCreateService(this.ArticleForm)
        this.$message.success('操作成功！')
        this.editAlert()
      } catch (error) {
        this.$message.error('操作失败！' + error.response.data.msg)
      }
    },
    // 修改文章
    async updateArticle () {
      try {
        await articleUpdateService(this.ArticleForm)
        this.$message.success('操作成功！')
        this.editAlert()
      } catch (error) {
        this.$message.error('操作失败！' + error.response.data.msg)
      }
    },
    // 弹出查看提示框
    editAlert () {
      this.$alert('操作成功，是否跳转到个人文章界面查看?', '消息提示', {
        confirmButtonText: '确定',
        cancelButtonText: '继续创作',
        callback: () => {
          this.$router.push(`/user/article/${this.$store.state.user.userInfo.username}`)
        }
      })
    },
    // 重置表单
    resetForm (formName) {
      this.$refs[formName].resetFields()
    }
  }
}
</script>

<style lang="less" scoped>
.article-edit {
  padding: 0 60px;

  .editor {
    height: 352px;
  }
}
</style>
