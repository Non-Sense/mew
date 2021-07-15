<template>
  <div class="taisei_Mbody">
    <div class="taisei_top">
      <ul>
        <li>
          <p>MyPage List</p>
        </li>
        <li>
          <input type="text" placeholder="    Search"/>
        </li>
        <li>
          <button>New Post</button>
        </li>
      </ul>
    </div>
    
    <div class="taisei_title">
      <ul>
        <li>
          <input type="button" value="  数学単語帳" />
          <button>Edit</button>
        </li>
        <li>
          <input type="button" value="  物理単語帳" />
          <button>Edit</button>
        </li>
        <li>
          <input type="button" value="  xx単語帳" />
          <button>Edit</button>
        </li>
        <li>
          <input type="button" value="  yy単語帳" />
          <button>Edit</button>
        </li>
      </ul>
    </div>

    <div class="taisei_share">
      <p>sharing</p>
      <input type="button" value="  数学単語帳" />
    </div>
  </div>
</template>

<script>
import axios from "axios";
import config from "@/const.js"

export default {
  name: "index",
  data() {
    return {
    }
  },

  methods: {

    // 自分の単語帳を検索する
    findMyBooks(){
      axios.get(config.baseUrl+"/api/book/find",{
        params:{
          title:"", // <- TODO: 検索文字列を入れる
          own:1
        },
        headers: {"X-AUTH-TOKEN": this.$cookies.get(config.cookieName)}
      }).then((res)=>{
        this.$cookies.set(config.cookieName, res.headers["x-auth-token"]);

        console.log(res.data); // <- TODO: 良きように
      }).catch((error)=>{
        switch (error.response.status){
          case 400:
            // リクエストが不正
            break;
          case 403:
            // アクセス拒否: ログインし直してもらう
            this.$router.push("/test/login"); // <- TODO
            break;
          case 404:
            // 1件もヒットしなかった <-404返さない方がいいですか?
            break;
          case 500:
            // サーバ内部エラー
            break;
          default:
            // 多分サーバが死んでいる
        }
      })
    },
    // 自分の単語帳を全件取得する
    getMyBooks(){
      axios.get(config.baseUrl+"/api/book",{
        headers: {"X-AUTH-TOKEN": this.$cookies.get(config.cookieName)}
      }).then((res)=>{
        this.$cookies.set(config.cookieName, res.headers["x-auth-token"]);

        console.log(res.data); // <- TODO: 良きように
      }).catch((error)=>{
        switch (error.response.status){
          case 400:
            // リクエストが不正
            break;
          case 403:
            // アクセス拒否: ログインし直してもらう
            this.$router.push("/test/login"); // <- TODO
            break;
          case 404:
            // 1件も存在しない
            break;
          case 500:
            // サーバ内部エラー
            break;
          default:
            // 多分サーバが死んでいる
        }
      })
    },

  }
}
</script>