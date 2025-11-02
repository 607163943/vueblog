<template>
  <div class="article">
    <div class="list">
      <div class="card" v-for="(article, index) in articleList" :key="index">
        <router-link :to="`/blog/${article.id}`">
          <el-card>
            <div class="content">
              <div class="left">
                <div class="pic"></div>
              </div>
              <div class="right">aaa</div>
            </div>
          </el-card>
        </router-link>
      </div>
    </div>
    <!--分页-->
    <el-pagination
      class="pager"
      background
      layout="prev, pager, next"
      :current-page="currentPage"
      :page-size="pageSize"
      :total="total"
      @current-change="getPage"
    >
    </el-pagination>
  </div>
</template>

<script>
export default {
  name: 'ArticleIndex',
  data () {
    return {
      articleList: [],
      currentPage: 1,
      total: 0,
      pageSize: 5,
      avatar: require('@/asserts/img/default_user.jpg')
    }
  },
  created () {
    this.getPage(1)
  },
  methods: {
    getPage (currentPage) {
      const _this = this
      _this.$axios.get('/blogs?currentPage=' + currentPage).then((res) => {
        _this.articleList = res.data.data.records
        _this.currentPage = res.data.data.current
        _this.total = res.data.data.total
        _this.pageSize = res.data.data.size
      })
    }
  }
}
</script>

<style lang="less" scoped>
.card {
  margin-bottom: 24px;

  ::v-deep(.el-card) {
    border-radius: 12px;
  }

  ::v-deep(.el-card__body) {
    padding: 0;
  }

  &:hover {

    ::v-deep(.el-card.is-always-shadow) {
      box-shadow: 0 0 12px 0 rgba(0, 0, 0, 0.3);
    }
    .pic {
      // 放大图像
      transform: scale(1.2);
    }
  }

  .content {
    height: 200px;
    display: flex;

    .left {
      width: 24%;
      height: 100%;
      overflow: hidden;
    }

    .pic {
      height: 100%;
      background-image: url(../../asserts/img/article-img.jpeg);
      background-size: cover;
      transition: all 0.5s ease;
    }
  }
}

.pager {
  margin: 0 auto;
  text-align: center;
}
</style>
