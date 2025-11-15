<template>
  <div class="layout">
    <layout-header></layout-header>
    <div class="layout-content">
      <router-view></router-view>
    </div>
    <layout-footer></layout-footer>
  </div>
</template>

<script>
import LayoutHeader from '@/views/layout/Header'
import LayoutFooter from '@/views/layout/Footer'
import { userGetInfoService } from '@/api/user'
import { mapState } from 'vuex'
import jwtUtils from '@/utils/jwt-utils'
export default {
  components: { LayoutHeader, LayoutFooter },
  computed: {
    ...mapState('user', ['accessToken'])
  },
  async created () {
    if (this.accessToken || jwtUtils.getRefreshToken()) {
      try {
        const res = await userGetInfoService()
        if (res.data.code === 200) {
          const userInfo = res.data.data
          this.$store.commit('user/setUserInfo', userInfo)
        }
      } catch (error) { }
    }
  }
}
</script>

<style lang="less" scoped>
.layout {
  .layout-content {
    position: relative;
    margin: 0px auto;
    padding-top: 100px;
  }
}
</style>
