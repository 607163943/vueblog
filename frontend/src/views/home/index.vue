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
              <div class="right">
                <div class="meta">
                  <div class="user">
                    <div class="pic">
                      <el-avatar
                        :size="30"
                        :src="
                          article.authorAvatar ? article.authorAvatar : avatar
                        "
                      ></el-avatar>
                    </div>
                    <div class="username">
                      {{ article.author ? article.author : "未知用户" }}
                    </div>
                  </div>
                  <div class="date">{{ article.gmtModified }}</div>
                </div>
                <div class="title">{{ article.title }}</div>
                <div class="description">{{ article.description }}</div>
                <div class="detail">
                  阅读文章
                  <i class="el-icon-arrow-right"></i>
                </div>
              </div>
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
      :current-page="pageParams.page"
      :page-size="pageParams.pageSize"
      :total="total"
      @current-change="pageChange"
    >
    </el-pagination>
  </div>
</template>

<script>
import { articlePageQueryService } from '@/api/article'
export default {
  name: 'ArticleIndex',
  data () {
    return {
      articleList: [],
      pageParams: {
        page: 1,
        pageSize: 5
      },
      total: 0,
      avatar:
        'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
    }
  },
  created () {
    this.pageQuery()
  },
  methods: {
    // 分页查询文章
    async pageQuery () {
      const res = await articlePageQueryService(this.pageParams)
      this.articleList = res.data.data.result
      this.total = res.data.data.total
    },
    // 换页查询
    pageChange (newPage) {
      this.pageParams.page = newPage
      this.pageQuery()
    }
  }
}
</script>

<style lang="less" scoped>
.article {
  padding: 0 180px;
}

.article-left {
  margin: 0 auto;
  width: 92%;
}

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
      box-shadow: 0 2px 12px 0 #d1d4f5;
    }
    .left .pic {
      // 放大图像
      transform: scale(1.3);
    }

    .right {
      .user .username {
        color: #101932 !important;
      }

      .detail {
        color: #101932 !important;
      }
    }
  }

  .content {
    height: 240px;
    display: flex;

    .left {
      width: 30%;
      height: 100%;
      overflow: hidden;

      .pic {
        height: 100%;
        background-image: url(../../asserts/img/article-img.jpeg);
        background-size: cover;
        transition: all 0.5s ease;
      }
    }

    .right {
      position: relative;
      padding: 18px;

      .meta {
        display: flex;
        align-items: center;

        .user {
          display: flex;
          align-items: center;
          margin-right: 20px;

          .pic {
            margin-right: 10px;

            ::v-deep(.el-avatar--circle) {
              display: block;
              border: 1px solid #efefef;
            }
          }

          .username {
            color: #747577;
            transition: all 0.5s ease;
          }
        }

        .date {
          margin-right: 20px;
          color: #747577;
        }
      }

      .title {
        margin-top: 4px;
        font-size: 24px;
      }

      .description {
        margin-top: 10px;
        color: #909399;
        font-size: 14px;
      }

      .detail {
        position: absolute;
        left: 18px;
        bottom: 18px;
        color: #909399;
        transition: all 0.5s ease;

        .el-icon-arrow-right {
          font-weight: 700;
        }
      }
    }
  }
}

.pager {
  margin: 0 auto;
  text-align: center;
}
</style>
