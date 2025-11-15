import Vue from 'vue'
import Vuex from 'vuex'
import user from './modules/user'
import createPersistedState from 'vuex-persistedstate'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {},
  mutations: {},
  getters: {},
  actions: {},
  modules: {
    user
  },
  plugins: [createPersistedState({
    key: 'vueblog',
    storage: window.localStorage,
    reducer (state) {
      return {
        user: {
          token: state.user.token,
          userInfo: state.user.userInfo
        }
      }
    }
  })]
})
