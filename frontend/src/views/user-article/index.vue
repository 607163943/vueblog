<template>
  <div class="user-article">
    <!-- 搜索表单 -->
    <div class="search">
      <el-form :inline="true" :model="searchForm" ref="searchFormRef">
        <el-form-item label="文章标题" prop="title">
          <el-input
            v-model="searchForm.title"
            placeholder="文章标题"
          ></el-input>
        </el-form-item>
        <el-form-item label="文章状态" prop="status">
          <el-select v-model="searchForm.status" placeholder="文章状态">
            <el-option label="发布" :value="0"></el-option>
            <el-option label="草稿" :value="1"></el-option>
            <el-option label="已删除" :value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search">查询</el-button>
          <el-button type="primary" @click="reset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!-- 按钮组 -->
    <div class="button-group">
      <el-button type="primary" size="small" icon="el-icon-plus" plain
        >新增</el-button
      >
      <el-button type="danger" size="small" icon="el-icon-delete" plain
        >删除</el-button
      >
    </div>
    <!-- 数据表格 -->
    <!-- TODO 表格未完成 -->
    <el-table :data="tableData" style="width: 100%" border>
      <el-table-column label="日期" width="180">
        <template slot-scope="scope">
          <i class="el-icon-time"></i>
          <span style="margin-left: 10px">{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="博文" width="250">
        <template slot-scope="scope">
          <i class="el-icon-star-on"></i>
          <span style="margin-left: 10px">{{ scope.row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column label="简介" width="260">
        <template slot-scope="scope">
          <i class="el-icon-s-order"></i>
          <span style="margin-left: 10px">{{ scope.row.description }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleLook(scope.$index, scope.row)"
            >查看</el-button
          >
          <el-button
            size="mini"
            type="primary"
            @click="handleEdit(scope.$index, scope.row)"
            >编辑</el-button
          >
          <el-button
            size="mini"
            type="danger"
            @click.native.prevent="handleDelete(scope.$index, tableData)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { articleUserPageQueryService } from '@/api/article'
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
      // 分页参数
      pageParams: {
        page: 1,
        pageSize: 5
      },
      // 搜索参数
      searchParams: {
        // 页码
        page: 1,
        // 每页数量
        pageSize: 5,
        // 文章标题
        title: '',
        // 状态
        status: ''
      },
      // 表格数据
      tableData: []
    }
  },
  created () {
    this.pageQuery()
  },
  methods: {
    search () {
      this.searchParams = {
        ...this.searchForm,
        ...this.pageParams
      }

      this.pageQuery()
    },
    reset () {
      this.$refs.searchFormRef.resetFields()
    },
    // 分页查询用户文章
    async pageQuery () {
      const username = this.$route.params.username
      const res = await articleUserPageQueryService(
        username,
        this.searchParams
      )
      this.tableData = res.data.data
    },
    handleLook (row) {
      this.$router.push({
        name: 'BlogDetail',
        params: { blogId: this.tableData[row].id }
      })
    },
    handleEdit (row) {
      this.$router.push({
        name: 'BlogEdit',
        params: { blogId: this.tableData[row].id }
      })
    },
    handleDelete (index, rows) {
      this.$confirm('是否删除该博文？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.$axios
            .get('/blog/delete/' + this.tableData[index].id, {
              headers: {
                Authorization: localStorage.getItem('token')
              }
            })
            .then((res) => {
              rows.splice(index, 1)
            })
          this.$message({
            type: 'success',
            message: '删除成功!'
          })
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

<style lang="less" scoped>
.user-article {
  padding: 0 120px;
}

.button-group {
  margin-bottom: 10px;
}
</style>
