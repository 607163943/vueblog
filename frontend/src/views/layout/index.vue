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
export default {
  components: { LayoutHeader, LayoutFooter }
}
</script>

<style lang="less" scoped>
.layout {
  .layout-content {
    margin: 0px auto;
    padding-top: 100px;
  }
}
</style>
