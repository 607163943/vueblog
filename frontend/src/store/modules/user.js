export default {
  namespaced: true,
  state: {
    // JWT令牌
    token: '',
    // 用户信息
    userInfo: {}
  },
  mutations: {
    setToken (state, token) {
      state.token = token
    },
    setUserInfo (state, userInfo) {
      state.userInfo = userInfo
      if (!state.userInfo.avatar) {
        state.userInfo.avatar = require('@/asserts/img/default_user.jpg')
      }
    }
  },
  actions: {},
  getters: {}
}
