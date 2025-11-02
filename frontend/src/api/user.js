import request from '@/utils/request'

// 用户登出
export const userLogoutService = () => {
  return request.get('/logout')
}
