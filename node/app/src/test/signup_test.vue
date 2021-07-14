<template>

  <div>
    <div class="container">
      <form id="form" @submit="signup" action="#">
        <label for="login_id">ログインID 英数字5-15文字</label>
        <input v-model="loginId" type="text" id="login_id" required minlength="5" maxlength="15" pattern="^\w{5,15}$">
        <br>

        <label for="display_name">表示名 1-50文字</label>
        <input v-model="displayName" type="text" id="display_name" required minlength="1" maxlength="50" pattern="^[0-9a-zA-Zぁ-んァ-ヶ一-龠々ー]{1,50}$">
        <br>

        <label for="password">パスワード 最低6文字</label>
        <input v-model="password" type="password" id="password" required minlength="6" pattern="^.{6,}">
        <br>

        <input type="submit" value="submit">
      </form>
      <span v-cloak>{{ msg }}</span>
    </div>
  </div>
</template>

<script>

import axios from "axios";
import config from "@/const.js"

export default {
  name: "signup_test",
  data() {
    return {
      // デフォルト値
      loginId: "",
      displayName: "",
      password: "",
      msg: ""
    }
  },

  methods: {
    // ボタン押した時
    signup(){
      axios.post(config.baseUrl+"/api/signup", {
        // ぶん投げるJson
        nameId: this.loginId,
        name: this.displayName,
        password: this.password
      }).then(()=>{
        // ここは成功の時
        this.msg = "OK";
      }).catch((error)=>{
        // こっちはエラーが返った時
        this.msg = "error: "+error.response.status;
      })
    }
  }
}
</script>