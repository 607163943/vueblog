import axios from 'axios'
import { Message } from 'element-ui'
import store from '@/store'
import router from '@/router'

const instance = axios.create({
  baseURL: process.env.VUE_APP_BASE_URL,
  timeout: 1000
})

// 添加请求拦截器
instance.interceptors.request.use(function (config) {
  // 在发送请求之前做些什么
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
  if (res.code !== 200) {
    Message.error(res.msg, { duration: 3 * 1000 })
    // 阻止返回
    return Promise.reject(response.data.msg)
  }

  return response
}
, function (error) {
  // 超出 2xx 范围的状态码都会触发该函数。
  // 对响应错误做点什么
  // 如果是无认证
  if (error.response.data.code === 401) {
    Message.error('登陆超时！')
    store.commit('user/setToken', '')
    store.commit('user/setUserInfo', {})
    router.push({
      path: '/login',
      params: {
        redirect: router.currentRoute.fullPath
      }
    })
  } else { // 如果是其他错误
    Message.error(error.response.data.msg, { duration: 3 * 1000 })
    return Promise.reject(error)
  }
})

export default instance
