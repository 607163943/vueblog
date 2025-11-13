<template>
  <div class="user-article">
    <!-- 搜索表单 -->
    <div class="search">
      <el-form :inline="true" :model="searchForm" ref="searchFormRef">
        <el-form-item label="文章标题" prop="title">
          <el-input v-model="searchForm.title" placeholder="文章标题"></el-input>
        </el-form-item>
        <el-form-item label="文章状态" prop="status">
          <el-select v-model="searchForm.status" placeholder="文章状态">
            <el-option label="发布" :value="0"></el-option>
            <el-option label="草稿" :value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="pageQuery">查询</el-button>
          <el-button type="primary" @click="reset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!-- 按钮组 -->
    <div class="button-group">
      <el-button type="primary" size="small" icon="el-icon-plus" @click="$router.push('/article/add')"
        plain>新增</el-button>
      <el-button type="danger" @click="deleteArticle" :disabled="selectItems.length <= 0" size="small"
        icon="el-icon-delete" plain>删除</el-button>
    </div>
    <!-- 数据表格 -->
    <div class="table">
      <el-table v-loading="loading" @selection-change="selectChange" :data="tableData" style="width: 100%" border>
        <el-table-column type="selection" width="55"> </el-table-column>
        <el-table-column label="标题" width="250" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.title }}</span>
          </template>
        </el-table-column>
        <el-table-column label="简介" width="260" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.description }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="160" align="center">
          <template slot-scope="scope">
            <el-tag type="success" v-if="scope.row.status === 0">发布</el-tag>
            <el-tag type="info" v-else>草稿</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="修改日期" width="240" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.updateTime }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button size="mini" icon="el-icon-view" @click="handleLook(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete"
              @click.prevent="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
        <template #empty>
          <el-empty description="你还没有编写过文章，赶快去创作一篇吧！"></el-empty>
        </template>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="page">
      <el-pagination @size-change="pageSizeChange" @current-change="pageChange" :current-page="pageParams.page"
        :page-sizes="[10, 20, 30, 40]" :page-size="pageParams.pageSize" layout="total, prev, pager, next, sizes, jumper"
        :total="total" background>
      </el-pagination>
    </div>
  </div>
</template>

<script>
import { articleUserPageQueryService, articleBatchDeleteService } from '@/api/article'
export default {
  data () {
    return {
      // 搜索表单
      searchForm: {
        // 文章标题
        title: '',
        // 文章状态
        status: ''
      },
      // 加载状态
      loading: false,
      // 分页参数
      pageParams: {
        page: 1,
        pageSize: 10
      },
      // 搜索参数
      searchParams: {
        // 页码
        page: 1,
        // 每页数量
        pageSize: 10,
        // 文章标题
        title: '',
        // 状态
        status: ''
      },
      // 表格数据
      tableData: [],
      // 选中数据项
      selectItems: [],
      // 总条数
      total: 0
    }
  },
  async created () {
    this.loading = true
    await this.pageQuery()
    this.loading = false
  },
  methods: {
    // 重置搜索表单
    reset () {
      this.$refs.searchFormRef.resetFields()
    },

    // 获取选中项id集合
    selectChange (items) {
      this.selectItems = items
    },
    // 分页查询用户文章
    async pageQuery () {
      const username = this.$route.params.username

      this.searchParams = {
        ...this.searchForm,
        ...this.pageParams
      }
      const res = await articleUserPageQueryService(
        username,
        this.searchParams
      )
      this.tableData = res.data.data.result
      this.total = res.data.data.total
    },
    // 修改页码
    pageSizeChange (newPageSize) {
      this.pageParams.pageSize = newPageSize
      this.pageQuery()
    },
    // 换页
    pageChange (newPage) {
      this.pageParams.page = newPage
      this.pageQuery()
    },
    // 文章预览
    handleLook (row) {
      this.$router.push({
        name: 'BlogDetail',
        params: { articleId: row.id }
      })
    },
    // 编辑文章
    handleEdit (row) {
      this.$router.push({
        name: 'BlogEdit',
        params: { articleId: row.id }
      })
    },
    // 删除指定文章
    handleDelete (row) {
      this.deleteArticleByIds([row.id])
    },
    // 批量删除文章
    deleteArticle () {
      const ids = this.selectIds.map(item => item.id)
      this.deleteArticleByIds(ids)
    },
    // 删除文章
    async deleteArticleByIds (ids) {
      try {
        await this.$confirm('是否删除该博文？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const idArr = ids.join(',')
        console.log(idArr)
        await articleBatchDeleteService(idArr)

        this.$message({
          type: 'success',
          message: '删除成功!'
        })

        this.pageQuery()
      } catch (error) { }
    }
  }
}
</script>

<style lang="less" scoped>
.user-article {
  padding: 0 80px;
}

.button-group {
  margin-bottom: 10px;
}

.table {
  min-height: 400px;
}

.page {
  margin-top: 20px;
  text-align: right;
}
</style>
