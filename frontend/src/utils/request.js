import axios from 'axios'
import Element from 'element-ui'
import store from '@/store'
import router from '@/router'

const instance = axios.create({
  baseURL: 'http://localhost:9090',
  timeout: 1000,
  headers: { 'X-Custom-Header': 'foobar' }
})

// 添加请求拦截器
instance.interceptors.request.use(function (config) {
  // 在发送请求之前做些什么
  const token = localStorage.getItem('token')
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
  if (res.code && res.code === 200) {
    return response
  }
  if (res.status && res.status === 200) {
    return response
  } else {
    Element.Message.error(res.msg, { duration: 3 * 1000 })
    // 阻止返回
    return Promise.reject(response.data.msg)
  }
}
, function (error) {
  // 超出 2xx 范围的状态码都会触发该函数。
  // 对响应错误做点什么
  if (error.response.data) {
    error.messgae = error.response.data.msg
  }

  // 如果是无认证
  if (error.response.status === 401) {
    store.commit('rmUserInfo')
    router.push('/login')
  } else { // 如果是认证错误
    Element.Message.error(error.messgae, { duration: 3 * 1000 })
    return Promise.reject(error)
  }
})

export default instance
