import axios from 'axios'
import { Message } from 'element-ui'
import store from '@/store'
import router from '@/router'

const instance = axios.create({
  baseURL: process.env.VUE_APP_BASE_URL,
  timeout: 10000
})

// 添加请求拦截器
instance.interceptors.request.use(function (config) {
  const token = store.state.user.token
  if (token) {
    config.headers.Authorization = token
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
, function (error) {
  // 超出 2xx 范围的状态码都会触发该函数。
  // 对响应错误做点什么
  const res = error.response

  Message.error(res.data.msg)
  if (res.data.code === 401) {
    // 认证失败
    store.commit('user/setToken', '')
    store.commit('user/setUserInfo', {})
    router.push({
      path: '/login',
      params: {
        redirect: router.currentRoute.fullPath
      }
    })
  } else {
    // 其他错误
    return Promise.reject(error)
  }
})

export default instance
