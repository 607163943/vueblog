import request from '@/utils/request'

// 分页查询文章
export const articlePageQueryService = (params) => {
  return request.get('/article', { params: params })
}
