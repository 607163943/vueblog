import request from '@/utils/request'

// 分页查询文章
export const articlePageQueryService = (params) => {
  return request.get('/article', { params: params })
}

// 分页查询用户文章
export const articleUserPageQueryService = (username, params) => {
  return request.get(`/article/user/${username}`, { params: params })
}

// 创建文章
export const articleCreateService = (data) => {
  return request.post('/article', data)
}

// 修改文章
export const articleUpdateService = (data) => {
  return request.put('/article', data)
}

// 获取文章
export const articleGetService = (id) => {
  return request.get(`/article/${id}`)
}

// 获取文章详情
export const articleGetDetailService = (id) => {
  return request.get(`/article/detail/${id}`)
}

// 批量删除文章
export const articleBatchDeleteService = (ids) => {
  return request.delete('/article', { params: { ids: ids } })
}

// 获取作者文章创作信息
export const articleAuthorInfoService = (authorId) => {
  return request.get(`/article/author/info/${authorId}`)
}
