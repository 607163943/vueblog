import request from '@/utils/request'

// 用户登出
export const userLogoutService = () => {
  return request.get('/logout')
}

// 用户登录
export const userLoginService = (data) => {
  return request.post('/user/login', data)
}
