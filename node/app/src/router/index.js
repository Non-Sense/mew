import Vue from 'vue'
import Router from 'vue-router'
//MyPage
import MyPage from '@/mypage/index.vue'
import MyPageEdit from '@/mypage/edit.vue'
import MyPageNewPost from '@/mypage/new_post.vue'
import MyPageIndexEdit from '@/mypage/index/edit.vue'
import MyPageIndexNewPost from '@/mypage/index/new_post.vue'
import MyPageIndex from '@/mypage/index/index.vue'

//Online
import Online from '@/online/index.vue'
import OnlineIndex from '@/online/index/index.vue'

//TestPages
import SignupTest from '@/test/signup_test.vue'
import LoginTest from '@/test/login_test.vue'
import BookTest from '@/test/book_test.vue'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      component: MyPage
    },
    {
      path: '/mypage',
      component: MyPage
    },
    {
      path: '/mypage/edit/:id',
      component: MyPageEdit,
      props: true,
      name: "mypage-edit"
    },
    {
      path: '/mypage/new_post',
      component: MyPageNewPost,
    },
    {
      path: '/mypage/index/:id',
      component: MyPageIndex,
      props: true,
      name: "mypage-index"
    },
    {
      path: '/mypage/index/edit/:id',
      component: MyPageIndexEdit,
      props: true,
      name: "mypage-index-edit"
    },
    {
      path: '/mypage/index/new_post/:id',
      component: MyPageIndexNewPost,
      props: true,
      name: "mypage-index-new_post"
    },
    {
      path: '/online',
      component: Online
    },    {
      path: '/online/index/:id',
      component: OnlineIndex,
      props: true,
      name: "online-index"
    },

    {
      path: '/signup',
      component: SignupTest
    },
    {
      path: '/login',
      component: LoginTest
    },
    {
      path: '/test/book',
      component: BookTest
    },
  ]
})
