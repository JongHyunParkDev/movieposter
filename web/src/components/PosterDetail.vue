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
import { ref } from 'vue';


const slide = ref(1);
const autoplay = ref(true);

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
