<template>
  <div class="login">
    <div class="login-container">
      <div class="login-box">
        <!-- logo -->
        <div class="login-logo">
          <el-image :key="url" :src="url" lazy></el-image>
        </div>
        <!-- 登陆表单 -->
        <div class="login-form">
          <el-form label-position="top" :model="loginForm" :rules="rules" ref="loginFormRef" label-width="100px">
            <el-row>
              <el-col :span="24">
                <el-form-item label="用户名" prop="username">
                  <el-input v-model="loginForm.username" prefix-icon="el-icon-user"
                    @keyup.enter.native="submitForm('loginFormRef')" placeholder="输入用户名"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="密码" prop="orgPassword">
                  <el-input type="password" v-model="loginForm.orgPassword"
                    @keyup.enter.native="submitForm('loginFormRef')" prefix-icon="el-icon-lock" show-password
                    placeholder="输入密码"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row style="margin-bottom: 20px;">
              <div class="login-other">
                <el-col :span="12">
                  <el-checkbox v-model="rememberMe">记住我</el-checkbox>
                </el-col>
                <el-col :span="12">
                  <div class="login-other-forget">
                    <el-link type="primary" :underline="false" @click="$message.info('该功能暂未实现')">忘记密码？</el-link>
                  </div>
                </el-col>
              </div>
            </el-row>
            <el-row style="margin-bottom: 20px;">
              <el-col :span="24">
                <el-form-item>
                  <el-button style="width: 100%;" type="primary" @click="submitForm('loginFormRef')"
                    :loading="loading">登录</el-button>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <div class="no-account">
                  没有账号？<router-link :to="{ name: 'Register' }">
                    <el-link :underline="false" type="primary" style="line-height: 12px;">立即注册</el-link>
                  </router-link>
                </div>
              </el-col>
            </el-row>
          </el-form>
        </div>
      </div>
    </div>
    <Footer></Footer>
  </div>
</template>

<script>
import Footer from '@/views/layout/Footer'
import { userLoginService } from '@/api/user'
import { encryptPassword } from '@/utils/security-utils'
import storageUtils from '@/utils/storage-utils'
export default {
  name: 'UserLogin',
  components: { Footer },
  data () {
    return {
      // Logo地址
      url: require('@/asserts/img/logo.png'),
      // 登陆表单
      loginForm: {
        // 用户名
        username: '',
        // 密码
        orgPassword: ''
      },
      // 加载动画标志
      loading: false,
      // 表单校验规则
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          {
            min: 3,
            max: 15,
            message: '长度在 3 到 15 个字符',
            trigger: 'blur'
          }
        ],
        orgPassword: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          {
            min: 3,
            max: 15,
            message: '长度在 3 到 15 个字符',
            trigger: 'blur'
          }
        ]
      },
      // 记住我,需要后端配合实现
      rememberMe: false
    }
  },
  methods: {
    async submitForm (formName) {
      await this.$refs[formName].validate()

      this.loading = true
      try {
        this.loginForm.password = encryptPassword(this.loginForm.orgPassword)
        const res = await userLoginService(this.loginForm)

        storageUtils.setSessionStorage('refreshToken', res.data.data.refreshToken)

        const accessToken = res.data.data.accessToken

        // 提交到Vuex仓库
        this.$store.commit('user/setAccessToken', accessToken)

        // 跳转到登录前的页面
        if (this.$route.query.redirect) {
          this.$router.push({ path: this.$route.query.redirect })
        } else {
          // 跳转到首页
          this.$router.push({ name: 'Home' })
        }
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style lang="less" scoped>
.login {
  min-height: 100vh;
  background-color: #e8e8e8;
}

.login-container {
  padding-top: 120px;
  min-height: 680px;
}

.login-box {
  margin: 0 auto;
  border-radius: 15px;
  padding: 0 40px;
  padding-top: 20px;
  padding-bottom: 40px;
  width: 450px;
  background-color: #fff;

  ::v-deep(.el-form-item) {
    margin-bottom: 12px;
  }

  ::v-deep(.el-form--label-top .el-form-item__label) {
    padding: 0;
  }

  ::v-deep(.el-form-item__label) {
    line-height: 24px;
  }
}

.login-logo {
  width: 100px;
  margin: 0 auto;
}

.login-other {
  ::v-deep(.el-checkbox) {
    font-weight: 400;
  }
}

.login-other-forget {
  text-align: right;
}

.no-account {
  color: #606266;
  font-size: 14px;
  text-align: center;

  ::v-deep(.el-link) {
    vertical-align: baseline;
  }
}
</style>
