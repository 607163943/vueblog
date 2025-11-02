<template>
  <div class="layout">
    <layout-header></layout-header>
    <router-view></router-view>
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

<style>

</style>
