<template>
  <div class="hashimoto-main">     
      <form action="送信先のURL" method="post">
        <div class="hashimoto-button">
          <input class="hashimoto-delete" type="submit" value="Delete">
          <input class="hashimoto-save" type="submit" value="Save">
        </div>
        <div class="hashimoto-page-name">New Post</div>
        
        <div class="hashimoto-post-word">
          <p class="hashimoto-p">用語 <input class="hashimoto-word" type="text" name="word"></p>
          <p class="hashimoto-p">意味 <input class="hashimoto-meaning" type="text" name="meaning" ></p>
        </div>
      </form>

      
    </div>
</template>