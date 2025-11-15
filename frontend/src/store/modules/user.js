export default {
  namespaced: true,
  state: {
    // 是否登录
    isLogin: false,
    // JWT访问令牌
    accessToken: '',
    // 用户信息
    userInfo: {}
  },
  mutations: {
    setAccessToken (state, accessToken) {
      state.accessToken = accessToken
    },
    setIsLogin (state, isLogin) {
      state.isLogin = isLogin
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
