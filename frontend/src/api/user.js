import request from '@/utils/request'

// 用户登出
export const userLogoutService = () => {
  return request.get('/user/logout')
}

// 用户登录
export const userLoginService = (data) => {
  return request.post('/user/login', data)
}

// 用户注册
export const userRegisterService = (data) => {
  return request.post('/user/register', data)
}
