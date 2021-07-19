<template>
  <div class="taisei_Obody">
    <div class="taisei_list">
      <ul>
        <li>
          <p>Online List</p>
        </li>
        <li>
          <input v-model="search" v-on:change="findBooks" type="text" placeholder="    Search" />
        </li>
      </ul>
    </div>
    
    <div class="taisei_WB">
      <p>並び替え:評価の高い順</p>
      <ul v-for="item in sharingItems" v-bind:key="item.bookId" v-show="showFlag">
        <li>
          <input type="button" @click="$router.push({name:'online-index',params:{ id: item.bookId }})" v-bind:value="item.title" />
          <button v-if="item.rate!=null">評価: {{item.rate}}</button>
          <button v-else>評価: 未評価</button>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import config from "@/const.js"

export default {
  name: "online_books",
  data() {
    return {
      search:"",
      showFlag:false,
      sharingItems:[{bookId:"",userId:"",title:"",public:"",createdAt:"",updatedAt:"",rate:""}],
      editId: 0,
    }
  },
  // ロード終了時に取得してくる?
  created: function (){
    this.findBooks();
  },
  methods: {
    // 公開状態の単語帳を取得する
    // 検索文字列を空かnullにしておけば全件取得されるはず
    // たぶん評価が高い順に並んでいると思う(たぶん)
    findBooks(){
      axios.get(config.baseUrl+"/api/book/public", {
        params: {
          title:this.search // <- TODO 検索する単語帳名を入れる
        }, headers: {"X-AUTH-TOKEN": this.$cookies.get(config.cookieName)}
      }).then((res)=>{
        this.$cookies.set(config.cookieName, res.headers["x-auth-token"]);
        this.sharingItems = res.data;
        this.showFlag = true;
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
            // 件数が0
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
  }
}
</script>