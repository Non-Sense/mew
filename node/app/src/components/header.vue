<template>
<div class="header">
    <div class="title">
        <h1>MEW</h1>
        </div>
        <div class="button">
            <ul>
                <li>
                    <button   button type="button" onclick="location.href='#'" class="mypage">MyPage</button>
                </li>
                <li>
                    <button type="button" onclick="location.href='#'" class="online">Online</button>
                </li>
            </ul>
    </div>
</div>
</template>

<style>

.header {
    background-color: rgb(144, 143, 228);
    margin: 0;
    display: flex;
    align-items: center;
    height: 75px;
}

.title h1 {
    margin-left: 30px;
    font-size: 25px;
    color: white;
    font-weight: bolder;
}

.button ul {
    list-style-type: none;
    display: flex;
    margin-left: 760px;
}

.button ul li {
    margin-right: 20px;
}


.mypage {
    font-size: 14px;
    color: white;
    background-color: rgb(144, 143, 228);
    border-style: none;
    text-decoration: underline;
}

.online {
    font-size: 14px;
    color: white;
    background-color: rgb(144, 143, 228);
    border-style: none;
}
</style>
