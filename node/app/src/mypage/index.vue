<template>
  <div class="taisei_Mbody">
    <div class="taisei_top">
      <ul>
        <li>
          <p>MyPage List</p>
        </li>
        <li>
          <input v-model="search" v-on:change="findMyBooks" type="text" placeholder="    Search"/>
        </li>
        <li>
          <button @click="$router.push('/mypage/new_post')">New Post</button>
        </li>
      </ul>
    </div>
    
    <div class="taisei_title">
      <ul v-for="item in items" v-bind:key="item.bookId" v-show="showFlag">
        <li>
          <input type="button" @click="$router.push({name:'mypage-index',params:{ id: clickEdit(item.bookId) }})" v-bind:value="item.title" />
          <button @click="$router.push({name:'mypage-edit',params:{ id: clickEdit(item.bookId) }})">Edit</button>
        </li>
        
      </ul>
    </div>

    <div class="taisei_share">
      <p>sharing</p>
      <ul v-for="item in sharingItems" v-bind:key="item.bookId" v-show="showFlag">
        <li>
          <input type="button" @click="$router.push({name:'mypage-index',params:{ id: clickEdit(item.bookId) }})" v-bind:value="item.title" />
        </li>
      </ul>
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
      search:"",
      showFlag:false,
      items:[{bookId:"",userId:"",title:"",public:"",createdAt:"",updatedAt:""}],
      sharingItems:[{bookId:"",userId:"",title:"",public:"",createdAt:"",updatedAt:""}],
      editId: 0,
    }
  },

  created: function () {
    axios.get(config.baseUrl+"/api/book",
        {headers: {"X-AUTH-TOKEN": this.$cookies.get(config.cookieName) }
      }).then((res)=>{
        this.$cookies.set(config.cookieName, res.headers["x-auth-token"]);
        this.items = res.data;
        this.sharingItems = res.data.filter((e)=>{return e.public});
        this.showFlag = true;
        console.log(res.data);
      }).catch((error)=>{
        this.msg = "error: "+error.response.status;
        if(error.response.status === 403){
          // TODO: トークンが切れたのでもう一度ログインしてもらう
          this.$router.push("/login");
        }
      })
  },

  methods: {

    // 自分の単語帳を検索する
    findMyBooks(){
      axios.get(config.baseUrl+"/api/book/find",{
        params:{
          title:this.search, // <- TODO: 検索文字列を入れる
          own:1
        },
        headers: {"X-AUTH-TOKEN": this.$cookies.get(config.cookieName)}
      }).then((res)=>{
        this.$cookies.set(config.cookieName, res.headers["x-auth-token"]);
        this.items = res.data;
        this.sharingItems = res.data.filter((e)=>{return e.public});
        this.showFlag=true;
        console.log(res.data); // <- TODO: 良きように
      }).catch((error)=>{
        switch (error.response.status){
          case 400:
            // リクエストが不正
            break;
          case 403:
            // アクセス拒否: ログインし直してもらう
            this.$router.push("/login");
            break;
          case 404:
            // 1件もヒットしなかった <-404返さない方がいいですか?
            this.showFlag=false;
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