import request from '@/utils/request'

// 上传图片
export const uploadImageService = (form, tempId, userId) => {
  return request.post('/file/upload/image/', form, {
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    params: {
      tempId: tempId,
      userId: userId
    }
  })
}

// 获取tempId
export const getTempIdService = () => {
  return request.get('/file/upload/tempId')
}
