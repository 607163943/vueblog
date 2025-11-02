import Vue from 'vue'
import App from './App.vue'
import router from '@/router'
import store from '@/store'
import request from '@/utils/request'
import '@/utils/element-ui'
import '@/utils/mavon-editor'
import '@/utils/permisssion'

Vue.config.productionTip = false
Vue.prototype.$axios = request

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
