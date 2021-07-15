<template>

  <div>
    <h2>MEW</h2>
    <form id="form" @submit="login" action="#" class="login_form">
      <label for="login_id">ログインID</label>
      <input v-model="loginId" type="text" id="login_id" required class="login_mail">
      <br>

      <label for="password">パスワード</label>
      <input v-model="password" type="password" id="password" required class="login_password">
      <br>

      <button type="submit" value="submit" class="login login_button">ログイン</button>
    </form>
    <a href=" ../test/signup" class="login_a"><button class="login login_button longin_a">新規登録</button></a>
    
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

<style>
h2 {
  color: rgb(144, 143, 228);
  text-align: center;
  font-weight: bold;
  font-size: 30px;
  margin: 0 auto;
  display: block;
  margin-top: 64px;
  margin-bottom: 64px;
}

.login_form {
  color: gray;  
  width: 600px;
  margin-left: auto;
  margin-right: auto;
  margin-bottom: 40px;
}

.login_a {
  background-color: #b4b3b3!important;
  text-decoration: none;
}

.login_mail {
  height: 40px;
  width: 600px;
  background-color: rgb(245, 243, 243);
  border-color:aliceblue;
  border-style: none;
  margin-bottom: 30px;
}

.login_password {
  height: 40px;
  width: 600px;
  background-color: rgb(245, 243, 243);
  border-color:aliceblue;
  border-style: none;
}

.login_button {
  width: 300px;
  margin-top: 40px;
  margin-left: auto;
  margin-right: auto;
  display: block;
  color: #fff;
  font-size: 6px;
  border-style: none;
  height: 40px;
  border-radius: 5px;

}

.login_button ul {
  list-style-type: none;
  padding: 0px;
}

.login_button ul li button {
  width: 300px;
  margin-top: 20px;
  color: white;
  font-size: 6px;
  border-style: none;
  height: 40px;
  border-radius: 5px;
}

.login {
  background-color:rgb(144, 143, 228);
}

.regi {
  background-color: rgb(180, 179, 179);
}
</style>