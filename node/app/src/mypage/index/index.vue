<template>
<div class="tomita_mibody">

  <form action="" method="post">
    <div class="tomita_mitop">
      <p>数学単語帳
      <input type="search" name="search" v-model="search" v-on:change="findWord" placeholder="Search" class="tomita_misearch">
      <router-link :to="{ name: 'mypage-index-new_post', params: { id:id }}"><input type="button" name="new_post" value="New Post" class="tomita_new_post"></router-link>
      </p>
    </div>
  </form>

  <div v-for="item in items" v-bind:key="item.wordId">
    <div class="tomita_miword_list">
      <table>
        <tr>
          <td class="tomita_miword">{{item.word}}</td>
          <td class="tomita_mimeaning">{{item.mean}}</td>
          <td class="tomita_edit">
            <router-link :to="{ name: 'mypage-index-edit', params: { id:clickEdit(item.wordId) }}"><input type="button" name="edit" value="Edit" class="tomita_edit_button"></router-link>
          </td>
        </tr>
      </table>
    </div>
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
      id:getParam(),
      search:"",
      items:[{wordId:"",bookId:"",word:"",mean:"",userId:"",createdAt:"",updatedAt:""}],
      editId:0
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
        this.items=res.data;
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
      let searchWord = this.search;  // <- TODO
      let searchMean = this.search;  // <- TODO
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
        this.items=res.data;
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
    clickEdit(id){
      return this.editId = id;
    },
  }
}
</script>