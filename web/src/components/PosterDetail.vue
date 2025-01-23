<template>
  <div class="container" :class="{ 'auto' : ! autoplay }">
    <q-carousel
      class="carousel"
      animated
      v-model="slide"
      :autoplay="autoplay"
      infinite
      transition-next="slide-left"
      transition-prev="slide-right"
      transition-duration="100"
      swipeable
      @click="autoplay = ! autoplay"
    >
      <q-carousel-slide :name="1" img-src="https://cdn.quasar.dev/img/mountains.jpg"></q-carousel-slide>
      <q-carousel-slide :name="2" img-src="https://cdn.quasar.dev/img/parallax1.jpg"></q-carousel-slide>
      <q-carousel-slide :name="3" img-src="https://cdn.quasar.dev/img/parallax2.jpg"></q-carousel-slide>
      <q-carousel-slide :name="4" img-src="https://cdn.quasar.dev/img/quasar.jpg"></q-carousel-slide>
    </q-carousel>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue';
import { Api } from 'src/lib/Api';

console.log(Api);

const audio = new Audio('https://file.notion.so/f/f/7c04381e-4207-4a6a-8157-817927ffdcc5/16f4e246-0577-46e2-ad5a-875e354e4cbe/bgm.mp3?table=block&id=18483fd6-5e2c-80b7-8c69-c8d743761ebb&spaceId=7c04381e-4207-4a6a-8157-817927ffdcc5&expirationTimestamp=1737662400000&signature=QuqzeuCxEaeETnwM9Ei8UDQfSp-wFJlPaxmi3p5CwAs&downloadName=bgm.mp3');
const slide = ref(1);
const autoplay = ref(true);

const onEnded = () => {
    audio.currentTime = 0; // 재생 위치를 처음으로 되돌림
    audio.play(); // 다시 재생
};

onMounted(async () => {
  await audio.load();
  audio.play();
  audio.addEventListener('ended', onEnded);
});

onBeforeUnmount(() => {
  if (audio) {
    audio.pause(); // 페이지 이동 시 오디오 멈춤
    audio.removeEventListener('ended', onEnded);
  }
});

</script>

<style lang="scss" scoped>
.container {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  height: 100%;
  width: 100%;
  overflow: hidden;
  background-color: #111;
  border: 3px solid #333;

  > .carousel  {
    height: 100%;
  }
}

.auto {
  border-color: red;
}
</style>
