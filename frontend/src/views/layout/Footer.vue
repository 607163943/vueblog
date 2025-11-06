<template>
  <div class="footer">
    <div>
      <p><a title="ICP备案" target="_blank" class="text-muted" href="http://www.beian.miit.gov.cn">
          浙ICP备20009096号-1
        </a></p>
    </div>
    <div>
      <p>博客内容遵循 <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/deed.zh" target="_blank"
          rel="noopener">署名-非商业性使用-相同方式共享 4.0 国际 (CC BY-NC-SA 4.0) 协议</a></p>
    </div>
    <div class="copyright">
      <p><a href="https://hcode.top" target="_blank" rel="noopener">Copyright hcode.top © 2025 </a></p>
    </div>
  </div>
</template>
<script>
export default {
  name: 'LayoutFooter'
}

</script>

<style lang="less" scoped>
.footer {
  font-size: 0.8125rem;
  text-align: center;
  padding: 10px 10px 20px 10px;
  width: 100%;
  color: rgba(102, 102, 102, 0.5);
  overflow: hidden;
  text-align: center;
}

a {
  color: slategray;
  cursor: pointer;

  &:hover {
    color: red;
  }
}
</style>
