<template>
<div class="tomita_mibody">

  <form action="" method="post">
    <div class="tomita_mitop">
      <p>数学単語帳
      <input type="search" name="search" placeholder="Search" class="tomita_misearch">
      <input type="button" name="new_post" value="New Post" class="tomita_new_post">
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