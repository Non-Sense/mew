<template>
<div class="tomita_mibody">

  <div>
    <div class="tomita_mitop">
      <p>{{bookname}}
      <input type="search" name="search" v-model="search" v-on:change="findWord" placeholder="Search" class="tomita_misearch">
      <router-link :to="{ name: 'mypage-index-new_post', params: { id:id }}"><input type="button" name="new_post" value="New Post" class="tomita_new_post"></router-link>
      </p>
    </div>
  </div>

  <div v-for="item in items" v-bind:key="item.wordId" v-show="showFlag">
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


export default {
  name: "edit_word",
  data() {
    return {
      id:0,
      search:"",
      items:[{wordId:"",bookId:"",word:"",mean:"",userId:"",createdAt:"",updatedAt:""}],
      editId:0, 
      bookname:"",
      showFlag:false,
    }
  },
  // ロード終了時に取得してくる
  created: function (){
    this.id = this.$route.params.id;
    this.getWords();
    this.getBook();
  },
  methods: {
    // 単語帳IDで単語一覧を取得
    getWords(){
      let bookId = this.id;
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
        this.showFlag=true;
        console.log(res.data);  // <- TODO
      }).catch((error)=>{
        switch (error.response.status){
          case 400:
            // リクエストが不正
            break;
          case 403:
            this.$router.push("/login");
            break;
          case 404:
            this.showFlag=false;
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
      if(this.search===""){
        this.getWords();
        return;
      }
      let searchWord = this.search;
      let searchMean = this.search;
      let bookId = this.id;
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
        this.showFlag=true;
      }).catch((error)=>{
        switch (error.response.status){
          case 400:
            // リクエストが不正
            break;
          case 403:
            this.$router.push("/login");
            break;
          case 404:
            this.showFlag=false;
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
    getBook(){
      let bookId = this.id;
      if(bookId == null) {
        // パラメータが設定されていない
        console.error("getParam: id == null");
        return;
      }
      axios.get(config.baseUrl+"/api/book/"+bookId, {
        headers: {"X-AUTH-TOKEN": this.$cookies.get(config.cookieName)
        }
      }).then((res)=>{
        this.$cookies.set(config.cookieName, res.headers["x-auth-token"]);
        this.bookname=res.data.title;
      }).catch((error)=>{
        switch (error.response.status){
          case 400:
            // リクエストが不正
            break;
          case 403:
            this.$router.push("/login");
            break;
          case 404:
            // 閲覧できない単語帳・そもそも無い
            this.$router.push("/mypage");
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