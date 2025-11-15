import storageUtils from './storage-utils'

export const REFRESH_TOKEN_KEY = 'refreshToken'

export default {
  refreshTokenKey: REFRESH_TOKEN_KEY,

  // 获取刷新令牌
  getRefreshToken: () => {
    const token = storageUtils.getLocalStorage(REFRESH_TOKEN_KEY)

    return token || storageUtils.getSessionStorage(REFRESH_TOKEN_KEY)
  }
}
