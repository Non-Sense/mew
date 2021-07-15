<template>
  <div class="tomita_oibody">
    <div class="tomita_review_window" v-if="modalFlag">
      <div style="display: inline-block;  _display: inline;" class="tomita_review_window_content">
        <span class="tomita_btn-close" v-on:click="modal"></span>
          <div class="tomita_Review_word">
            Review
          </div>
          <div class="tomita_evaluation">
            <input type="number" name="point" value="point" class="tomita_point" min="0" max="5">
              <div class="tomita_five_point">
                / 5.0点
              </div>
          <div>
          <input type="text" class="review_comment" placeholder="コメント">
          <input type="submit" name="submit" value="送信" class="tomita_submit review_submit">
        </div>
      </div>
    </div>
  </div>

  <form action="" method="post">
    <div class="tomita_oitop">
      <p>数学単語帳
      <input type="search" name="search" placeholder="Search" class="tomita_oisearch">
      <input type="button" name="Review" value="Review" class="tomita_oiReview" v-on:click="modal">
      </p>

    </div>
  </form>

  <div class="tomita_oiword_list">
    <table>
      <td class="tomita_oiword">math</td>
      <td class="tomita_oimeaning">数学</td>
    </table>
  </div>

  <div class="tomita_oiword_list">
    <table>
      <td class="tomita_oiword">用語</td>
      <td class="tomita_oimeaning">意味</td>
    </table>
  </div>
</div>
</template>
<script>
export default {
  name: "online_index",
  data() {
    return {
      modalFlag: false,
    }
  },

  methods: {
    modal(){
      console.log(this.modalFlag);
      this.modalFlag = !this.modalFlag;
    },
  }
}
</script>
<style scoped>
.tomita_oirbody{
    margin: 0px;
}

.tomita_oirheader p{
    font-family: Roboto;
    font-style: normal;
    margin: 0px;
    background-color:  #7B89C9;
    padding-bottom: 35px;
    padding-top: 35px;
    padding-left: 20px;
    font-size: 20px;
    color: white;
}

.tomita_oirtop{
    font-family: Roboto;
    font-style: normal;
    font-size: 24px;
    color: #666666;
    width : 1000px;
    margin-left: 230px;
    margin-right: 230px;
    margin-bottom: 112px;
    margin-top: 110px;
}



.tomita_oirsearch{
    font-family: Roboto;
    font-style: normal;
    background-color: #F6F7FB;
    border: none;
    width: 480px;
    height: 48px;
    margin-left: 109px;
    padding-right: 408px;
    padding-left: 16px;
    padding-top: 14px;
    padding-bottom: 13px;
    border-radius: 16px;
}

.tomita_oirReview{
    font-family: Roboto;
    font-style: normal;
    background-color: #7B89C9;
    color:white;
    border: none;
    font-size: 24px;
    width: 188px;
    height: 64px;
    margin-left: 64px;
    padding-top: 20px;
    padding-bottom: 20px;
    padding-left: 53px;
    padding-right: 53px;
    border-radius: 5px;
}


.tomita_submit{
    font-family: Roboto;
    font-style: normal;
    background-color: #7B89C9;
    color:white;
    border: none;
    width: 188px;
    height: 64px;
    font-size: 24px;
    margin-left: 329px;
    margin-top: 77px;
    padding-top: 20px;
    padding-bottom: 20px;
    padding-left: 66px;
    padding-right: 66px;
    border-radius: 8px;
    position: relative;
    z-index: 1;
}


.tomita_review_window{
    position:absolute;
    width:100%;
    height:120vh;
    background:rgba(63,76,88,.4);
    top: 0px;
    z-index: 10;
}

.tomita_review_window_content{
    color: #7B89C9;
    position: absolute;
    width: 846px;
    height: 508px;
    left:297px;
    right:297px;
    top:116px;
    bottom:196px;
    background-color:#FFFFFF;
}



.tomita_btn-close{
    display: inline-block;
    padding-top: 29px;
    padding-right: 35px;
    float: right;
  }
  
  .tomita_btn-close::before,
  .tomita_btn-close::after {
      display: block;
      content: "";
      width: 29px;
      height: 1px;
      background: #000;

  }
  .tomita_btn-close::before {
      transform: rotate(-45deg);
  }
  .tomita_btn-close::after {
      transform: rotate(45deg);
}

.tomita_Review_word{
    font-size: 36px;
    margin-left: 46px;
    margin-top: 54px;
}

.tomita_point{
    background-color:  #F6F7FB;
    border: none;
    width: 117px;
    height: 40px;
    padding-bottom: 3px;
    margin-left: 307px;
    margin-top: 102px;
    border-radius: 2px;
    left: 30px;
}

.tomita_evaluation{
    color: black;
    
}

.tomita_five_point{
    font-family: Roboto;
    font-style: normal;
    display: inline-block; _display: inline;
    color: #666666;
    width: 99px;
    height: 28px;
    margin-left: 15px;
    font-size: 24px;
}


.tomita_oirword_list table{
    background-color: rgb(246, 248, 255);
    font-size: 18px;
    width: 980px;
    height: 48px;
    margin-left: 230px;
    margin-right: 230px;
    margin-bottom: 49px;
    border-radius: 2px;
}

.tomita_oirword{
    width: 243px;
    padding-left: 20px;
    padding-top: 14px;
    padding-bottom: 13px;
}

.tomita_oirmeaning{
    padding-top: 14px;
    padding-bottom: 13px;
}

.review_submit {
  display: block;
  margin: 0 auto;
}

.review_comment {
  background-color: rgb(246, 248, 255);
  border: none;
  display: block;
  margin: 40px auto;
  width: 320px;
  height: 32px;
}
</style>