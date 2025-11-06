<template>
  <div class="register">
    <div class="register-container">
      <div class="register-box">
        <div class="register-title">用户注册</div>
        <el-form label-position="top" :model="registerForm" :rules="rules" ref="registerFormRef" label-width="100px">
          <el-row>
            <el-col :span="24">
              <el-form-item label="用户名" prop="username">
                <el-input v-model="registerForm.username" placeholder="请输入用户名"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="registerForm.email" placeholder="请输入邮箱"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="密码" prop="password">
                <el-input v-model="registerForm.password" placeholder="请输入密码" show-password></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="确认密码" prop="confirmPassword">
                <el-input v-model="registerForm.confirmPassword" placeholder="确认密码" show-password></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row style="margin-top: 20px;">
            <el-col :span="24">
              <el-button style="width: 100%;padding: 16px 20px;" type="success" @click="register('registerFormRef')">注册</el-button>
            </el-col>
          </el-row>
          <el-row style="margin-top: 10px;">
            <el-col :span="24">
              <el-button style="width: 100%;" @click="$router.back()">回到登录</el-button>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </div>
    <Footer></Footer>
  </div>
</template>

<script>
import Footer from '@/views/layout/Footer'
import { userRegisterService } from '@/api/user'
export default {
  name: 'UserRegister',
  components: { Footer },
  data () {
    return {
      // 注册表单
      registerForm: {
        // 用户名
        username: '',
        // 邮箱
        email: '',
        // 密码
        password: '',
        // 确认密码
        confirmPassword: ''
      },
      // 表单校验规则
      rules: {
        username: [
          { required: true, message: '用户名不能为空！', trigger: 'blur' },
          {
            min: 3,
            max: 15,
            message: '长度在 3 到 15 个字符',
            trigger: 'blur'
          }
        ],
        email: [{ required: true, message: '邮箱不能为空', trigger: 'blur' }],
        password: [
          { required: true, message: '密码不能为空', trigger: 'blur' },
          {
            min: 6,
            max: 15,
            message: '密码长度应在 6 到 15 个字符',
            trigger: 'blur'
          }
        ],
        confirmPassword: [
          { required: true, message: '确认密码不能为空！', trigger: 'blur' },
          { validator: this.checkPassword, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    // 注册
    async register (formName) {
      await this.$refs[formName].validate()

      await userRegisterService(this.registerForm)

      this.$message.success('注册成功！')
      this.$router.push({ name: 'Login' })
    },
    // 确认密码校验
    checkPassword (rule, value, callback) {
      if (value !== this.registerForm.password) {
        callback(new Error('两次密码不一致，请重新输入！'))
      } else {
        callback()
      }
    }
  }
}
</script>

<style lang="less" scoped>
.register {
  min-height: 100vh;
  background-color: #e8e8e8;
}

.register-container {
  padding-top: 80px;
  min-height: 680px;
}

.register-box {
  margin: 0 auto;
  border-radius: 15px;
  padding: 0 40px;
  padding-top: 20px;
  padding-bottom: 40px;
  width: 550px;
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

.register-title {
  font-size: 20px;
  text-align: center;
}
</style>
