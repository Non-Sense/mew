<template>
<div class="tomita_mibody">

  <form action="" method="post">
    <div class="tomita_mitop">
      <p>数学単語帳
      <input type="search" name="search" v-model="search" placeholder="Search" class="tomita_misearch">
      <input type="button" v-on:click="getParam" name="new_post" value="New Post" class="tomita_new_post">
      </p>

    </div>
  </form>

  <div class="tomita_miword_list">
    <table>
      <tr>
        <td class="tomita_miword">math</td>
        <td class="tomita_mimeaning">数学</td>
        <td class="tomita_edit">
          <input type="button" name="edit" value="Edit" class="tomita_edit_button">
        </td>
      </tr>
    </table>
  </div>

  <div class="tomita_miword_list">
    <table>
      <tr>
        <td class="tomita_miword">用語</td>
        <td class="tomita_mimeaning">意味</td>
        <td class="tomita_edit">
          <input type="button" name="edit" value="Edit" class="tomita_edit_button">
        </td>
      </tr>
    </table>
  </div>
</div>
</template>


<script>
import axios from "axios";
import config from "@/const.js"

function getParam(){
  let params = new URLSearchParams(document.location.search.substring(1));
  return params.get("id");
}

export default {
  name: "edit_word",
  data() {
    return {
    }
  },
  // ロード終了時に取得してくる?
  created: function (){
    this.getWords();
  },
  methods: {
    // 単語帳IDで単語一覧を取得
    getWords(){
      let bookId = getParam();
      if(bookId == null) {
        // パラメータが設定されていない
        console.error("getParam: id == null");
        return;
      }
      axios.get(config.baseUrl+"/api/book/"+bookId+"/word", {
        headers: {"X-AUTH-TOKEN": this.$cookies.get(config.cookieName)
        }
      }).then((res)=>{
        this.$cookies.set(config.cookieName, res.headers["x-auth-token"]);

        console.log(res.data);  // <- TODO
      }).catch((error)=>{
        switch (error.response.status){
          case 400:
            // リクエストが不正
            break;
          case 403:
            this.$router.push("/test/login"); // <- TODO
            break;
          case 404:
            // 閲覧できない単語・そもそも無い
            break;
          case 500:
            // サーバ内部エラー
            break;
          default:
            // 多分サーバが死んでいる
        }
      })
    },
    // 単語帳IDを指定して単語を検索
    findWord(){
      // 単語と意味それぞれで検索できますが、とりあえず同じ値とかを入れておけばいいんじゃないんでしょうか
      let searchWord = "test";  // <- TODO
      let searchMean = "test";  // <- TODO
      let bookId = getParam();
      if(bookId == null) {
        // パラメータが設定されていない
        console.error("getParam: id == null");
        return;
      }
      axios.get(config.baseUrl+"/api/book/"+bookId+"/word/find", {
        params:{
          mean: searchMean===""?null:searchMean,
          word: searchWord===""?null:searchWord
        },
        headers: {"X-AUTH-TOKEN": this.$cookies.get(config.cookieName)}
      }).then((res)=>{
        this.$cookies.set(config.cookieName, res.headers["x-auth-token"]);

        console.log(res.data);  // <- TODO
      }).catch((error)=>{
        switch (error.response.status){
          case 400:
            // リクエストが不正
            break;
          case 403:
            this.$router.push("/test/login"); // <- TODO
            break;
          case 404:
            // ヒット数が0・そもそも単語帳が無い
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