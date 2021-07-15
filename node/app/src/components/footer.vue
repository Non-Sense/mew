<template>
  <header>
    <ul>
      <li><router-link to="/mypage">MyPage</router-link></li>
      <li><router-link :to="{name: 'mypage-edit',params: { id: '1' }}">MyPageEdit</router-link></li>
      <li><router-link :to="{name: 'mypage-new_post',params: { id: '1' }}">MyPageNewPost</router-link></li>
      <li><router-link :to="{name: 'mypage-index',params: { id: '1' }}">MyPageIndex</router-link></li>
      <li><router-link :to="{name: 'mypage-index-edit',params: { id: '1' }}">MyPageIndexEdit</router-link></li>
      <li><router-link :to="{name: 'mypage-index-new_post',params: { id: '1' }}">MyPageIndexNewPost</router-link></li>
      <li><router-link to="/online">Online</router-link></li>
      <li><router-link :to="{name: 'online-index',params: { id: '1' }}">OnlineIndex</router-link></li>

      <li><router-link to="/test/signup">Signup *TEST*</router-link></li>
      <li><router-link to="/test/login">Login *TEST*</router-link></li>
      <li><router-link to="/test/book">Book *TEST*</router-link></li>
    </ul>
  </header>
</template>