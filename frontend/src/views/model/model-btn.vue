<template>
  <div>
    <div class="btn-list">
        <div class="btn twinkle">{{name}}</div>
    </div>
  </div>
</template>

<script>
export default{
  data:{
    return:{}
  },
  props:['name']
}
</script>

<style scoped>

.btn-list {
    grid-template-columns: repeat(3,200px);
    border-radius: 20px;
    padding: 50px;
    box-shadow: 0 0 5px 5px rgb(0 0 0 / 8%);
}

.btn {
    width: 450px;
    height: 125px;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 2rem;
    cursor: pointer;
    user-select: none;
    letter-spacing: 1rem;
    text-indent: 1rem;
    border-radius: 8px;
    box-sizing: border-box;
}
.twinkle {
    overflow: hidden;
    position: relative;
    border: 3px solid #6a9cff;
    color: #6a9cff;
    transition: background-color .2s;
}

.twinkle::before {
    content: "";
    position: absolute;
    width: 50px;
    height: 200%;
    background-color: rgba(255, 255, 255, .6);
    transform: skew(45deg) translate3d(-310px,0,0);
}

.twinkle:hover {
    background-color: rgb(0, 46, 91);
}

.twinkle:hover::before {
    transition: ease-in-out .5s;
    transform: skew(45deg) translate3d(300px,0,0);
}

</style>