import axios from 'axios'
import { Message } from 'element-ui'
import store from '@/store'
import router from '@/router'
import storageUtils from '@/utils/storage-utils'
import jwtUtils from '@/utils/jwt-utils'
import { userRefreshTokenService } from '@/api/user'

const instance = axios.create({
  baseURL: process.env.VUE_APP_BASE_URL,
  timeout: 10000
})

// 添加请求拦截器
instance.interceptors.request.use(function (config) {
  // 在发送请求之前做些什么
  if (config.url === '/user/refresh') {
    const refreshToken = storageUtils.getSessionStorage(jwtUtils.refreshTokenKey)
    if (refreshToken) {
      config.headers.Authorization = refreshToken
    }
  } else {
    const accessToken = store.state.user.accessToken
    if (accessToken) {
      config.headers.Authorization = accessToken
    }
  }
  return config
}, function (error) {
  // 对请求错误做些什么
  return Promise.reject(error)
})

// 添加响应拦截器
instance.interceptors.response.use(function (response) {
  // 2xx 范围内的状态码都会触发该函数。
  // 对响应数据做点什么
  const res = response.data
  // 业务异常
  if (res.code !== 200) {
    Message.error(res.msg)
    // 抛出异常
    return Promise.reject(response.data.msg)
  }

  return response
}
, async function (error) {
  // 超出 2xx 范围的状态码都会触发该函数。
  // 对响应错误做点什么
  const res = error.response
  const originalRequest = error.config // 记录这次请求的完整配置

  if (res.data.code === 401) {
    const refreshToken = storageUtils.getSessionStorage(jwtUtils.refreshTokenKey)
    if (refreshToken && !originalRequest._retry) {
      originalRequest._retry = true // 打个标记，防止死循环

      const refreshRes = await userRefreshTokenService()
      if (refreshRes.data.code === 200) {
        store.commit('user/setAccessToken', refreshRes.data.data)
        store.commit('user/setIsLogin', true)

        // 更新本次请求头里的 token
        originalRequest.headers.Authorization = refreshRes.data.data

        // 用新的 token 重新发起这次请求，并返回它的 Promise
        return instance(originalRequest)
      }
    }
    Message.error(res.data.msg)
    store.commit('user/setAccessToken', '')
    store.commit('user/setUserInfo', {})
    router.push({
      path: '/login',
      params: {
        redirect: router.currentRoute.fullPath
      }
    })
  } else { // 如果是其他错误
    Message.error(res.data.msg)
    return Promise.reject(error)
  }
})

export default instance
