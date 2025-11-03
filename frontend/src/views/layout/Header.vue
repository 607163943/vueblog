<template>
  <div class="layout-header">
    <div class="header-left">
      <!-- logo -->
      <div class="header-logo">
        <h1>
          <router-link to="/">
            <div class="logo">
              <el-image style="width: 72px; height: 58px" :src="url">
              </el-image>
            </div>
          </router-link>
        </h1>
      </div>

      <!-- 导航栏 -->
      <div class="nav">
        <el-menu
          :default-active="$route.path"
          class="el-menu-demo"
          mode="horizontal"
          active-text-color="#101932"
          router
        >
          <el-menu-item index="/blog"> 主页 </el-menu-item>
          <el-menu-item index="/blog/add"> 发表博文 </el-menu-item>
          <el-menu-item :index="`/user/${userInfo.username}`">
            个人文章
          </el-menu-item>
        </el-menu>
      </div>
    </div>
    <div class="header-right">
      <div v-if="!isLogin" class="login">
        <el-button round @click="login" class="login-button">登录</el-button>
      </div>
      <!-- 用户信息 -->
      <div v-else class="user">
        <el-dropdown @command="handleCommand">
          <div class="user-info">
            <el-avatar :size="60" :src="userInfo.avatar"></el-avatar>
            <span class="username">{{ userInfo.username }}</span>
          </div>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="profile">个人中心</el-dropdown-item>
            <el-dropdown-item command="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>

<script>
import { userLogoutService } from '@/api/user'
import { mapState } from 'vuex'
export default {
  name: 'LayoutHeader',
  data () {
    return {
      isLogin: false,
      url: require('@/asserts/img/logo.png')
    }
  },
  computed: {
    ...mapState('user', ['userInfo', 'token'])
  },
  created () {
    if (this.token) {
      this.isLogin = true
    }
  },
  methods: {
    // 跳转到登录
    login () {
      this.$router.push('/login')
    },
    // 处理下拉菜单命令
    handleCommand (command) {
      if (command === 'logout') {
        this.logout()
      }
    },
    // 登出
    async logout () {
      await userLogoutService()
      this.$store.commit('user/setToken', '')
      this.$store.commit('user/setUserInfo', {})
      this.$router.push('/login')
    }
  }
}
</script>

<style lang="less" scoped>
.layout-header {
  position: fixed;
  top: 0;
  z-index: 1000;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 4px;
  border-bottom: 1px solid #e0e4ed;
  padding-left: 60px;
  padding-right: 120px;
  width: 100%;
  height: 80px;
  background-color: #fff;
  .header-left {
    display: flex;

    .nav {
      margin-left: 12px;

      ::v-deep(.el-menu.el-menu--horizontal) {
        border: none;
      }

      ::v-deep(.el-menu--horizontal > .el-menu-item) {
        border-bottom: none;
        font-size: 18px;
      }

      ::v-deep(.el-menu-item) {
        font-size: 16px;
        height: 59px;
      }
    }
  }

  .header-right {
    .login {
      .login-button {
        border: 1px solid #c3c5cb;
        transition: all 0.3s;

        &:hover,&:focus {
          color: #101932;
          background-color: #f5f5f5;
        }
      }
    }

    .user {
      .user-info {
        display: flex;
        align-items: center;
        cursor: pointer;

        &:hover {
          .username {
            color: #101932;
          }
        }

        ::v-deep(.el-avatar--circle) {
          border: 1px solid #efefef;
        }

        .username {
          margin-left: 8px;
          font-size: 18px;
          transition: all 0.3s;
        }
      }
    }
  }
}
</style>
