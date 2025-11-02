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
        state.userInfo.avatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
      }
    }
  },
  actions: {},
  getters: {}
}
