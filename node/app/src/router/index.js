import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/login.vue'
import SignUp from '@/components/sign_up.vue'
//MyPage
import MyPage from '@/mypage/index.vue'
import MyPageEdit from '@/mypage/edit.vue'
import MyPageNewPost from '@/mypage/new_post.vue'
import MyPageIndexEdit from '@/mypage/index/edit.vue'
import MyPageIndexNewPost from '@/mypage/index/new_post.vue'
//Online
import Online from '@/online/index.vue'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      component: MyPage
    },
    {
      path: '/login',
      component: Login
    },
    {
      path: '/sinup',
      component: SignUp
    },
    {
      path: '/mypage',
      component: MyPage
    },
    {
      path: '/mypage/edit',
      component: MyPageEdit
    },
    {
      path: '/mypage/new_post',
      component: MyPageNewPost
    },
    {
      path: '/mypage/index/edit',
      component: MyPageIndexEdit
    },
    {
      path: '/mypage/index/new_post',
      component: MyPageIndexNewPost
    },
    {
      path: '/online',
      component: Online
    },
  ]
})
