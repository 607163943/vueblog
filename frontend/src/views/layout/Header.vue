<template>
  <div class="layout-header">
    <div class="header-left">
      <!-- logo -->
      <div class="header-logo">
        <router-link to="/">
          <div class="logo">
            <div class="pic">
              <el-image style="width: 72px; height: 58px" :src="url">
              </el-image>
            </div>
            <div class="text">HCODE小站</div>
          </div>
        </router-link>
      </div>

      <!-- 导航栏 -->
      <div class="nav">
        <el-menu
          :default-active="$route.path"
          class="el-menu-demo"
          mode="horizontal"
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
      <span v-if="!isLogin"
        ><el-link type="primary" href="/login">登录</el-link></span
      >
      <!-- 用户信息 -->
      <div v-else class="user">
        <el-dropdown @command="handleCommand">
          <div class="user-info">
            <el-avatar
              :size="40"
              src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
            ></el-avatar>
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
    ...mapState('user', ['userInfo'])
  },
  created () {
    if (this.userInfo.username) {
      this.isLogin = true
    }
  },
  methods: {
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
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 4px;
  border-bottom: 1px solid #dcdcdc;
  padding-left: 60px;
  padding-right: 120px;
  height: 60px;
  .header-left {
    display: flex;

    .header-logo {
      .logo {
        display: flex;
        align-items: center;

        .text {
          margin-left: 8px;
          color: #292929;
        }
      }
    }

    .nav {
      margin-left: 12px;

      ::v-deep(.el-menu.el-menu--horizontal) {
        border: none;
      }

      ::v-deep(.el-menu--horizontal > .el-menu-item.is-active) {
        border-bottom: none;
        border-bottom-color: transparent;
      }

      ::v-deep(.el-menu-item) {
        font-size: 16px;
      }
    }
  }

  .header-right {
    .user {
      .user-info {
        display: flex;
        align-items: center;
        cursor: pointer;

        .username {
          margin-left: 8px;
        }
      }
    }
  }
}
</style>
