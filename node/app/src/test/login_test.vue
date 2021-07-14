<template>

  <div>
    <form id="form" @submit="login" action="#">
      <label for="login_id">ログインID</label>
      <input v-model="loginId" type="text" id="login_id" required>
      <br>

      <label for="password">パスワード</label>
      <input v-model="password" type="password" id="password" required>
      <br>

      <input type="submit" value="submit">
    </form>
   <span v-cloak>{{ msg }}</span>
  </div>
</template>

<script>

import axios from "axios";
import config from "@/const.js"

export default {
  name: "signup_test",
  data() {
    return {
      loginId: "",
      password: "",
      msg: ""
    }
  },

  methods: {
    login(){
      axios.post(config.baseUrl+"/api/login", {
        nameId: this.loginId,
        password: this.password
      }).then((res)=>{
        // もらったアクセストークンをクッキーにセットする
        this.$cookies.set(config.cookieName, res.headers["x-auth-token"]);
        this.msg = "OK token: "+res.headers["x-auth-token"];
      }).catch((error)=>{
        this.msg = "error: "+error.response.status;
      })
    }
  }
}
</script>