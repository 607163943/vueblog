import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '@/views/login/Login.vue'
import Blog from '@/views/article/ArticleList.vue'
import BlogDetail from '@/views/article/BlogDetail.vue'
import BlogEdit from '@/views/article/BlogEdit.vue'
import Register from '@/views/login/Register.vue'
import User from '@/views/article/UserArticle.vue'
import Layout from '@/views/layout'
import store from '@/store'
Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Layout',
    component: Layout,
    redirect: { name: 'Blog' },
    children: [
      {
        path: '/user/:username',
        name: 'User',
        component: User,
        meta: {
          requireAuth: true
        }
      },
      {
        path: '/blog',
        name: 'Blog',
        component: Blog
      },
      {
        path: '/blog/add',
        name: 'BlogAdd',
        component: BlogEdit,
        meta: {
          requireAuth: true
        }
      },
      {
        path: '/blog/:blogId',
        name: 'BlogDetail',
        component: BlogDetail
      },
      {
        path: '/blog/:blogId/edit',
        name: 'BlogEdit',
        component: BlogEdit,
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
    if (token) { // 判断当前的token是否存在 ； 登录存入的token
      next()
    } else {
      next({
        path: '/login'
      })
    }
  } else {
    next()
  }
})

export default router
