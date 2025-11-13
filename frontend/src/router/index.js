import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '@/views/login/Login.vue'
import Home from '@/views/home'
import BlogDetail from '@/views/article-detail'
import ArticleEdit from '@/views/article-edit'
import Register from '@/views/login/Register.vue'
import UserArticle from '@/views/user-article'
import Layout from '@/views/layout'
import store from '@/store'
Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Layout',
    component: Layout,
    redirect: { name: 'Home' },
    children: [
      {
        path: '/user/article/:username',
        name: 'UserArticle',
        component: UserArticle,
        meta: {
          requireAuth: true
        }
      },
      {
        path: '/home',
        name: 'Home',
        component: Home
      },
      {
        path: '/article/add',
        name: 'ArticleAdd',
        component: ArticleEdit,
        meta: {
          requireAuth: true
        }
      },
      {
        path: '/article/:articleId',
        name: 'BlogDetail',
        component: BlogDetail
      },
      {
        path: '/article/edit/:articleId',
        name: 'BlogEdit',
        component: ArticleEdit,
        meta: {
          requireAuth: true
        }
      }
    ]
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 路由判断登录 根据路由配置文件的参数
router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requireAuth)) { // 判断该路由是否需要登录权限
    const token = store.state.user.token
    if (token) { // 判断当前的token是否存在
      next()
    } else {
      router.push({
        path: '/login', query: { redirect: to.fullPath }
      })
    }
  } else {
    next()
  }
})

export default router
