import request from '@/utils/request'

// 分页查询文章
export const articlePageQueryService = (params) => {
  return request.get('/article', { params: params })
}

// 分页查询用户文章
export const articleUserPageQueryService = (username, params) => {
  return request.get(`/article/user/${username}`, { params: params })
}
