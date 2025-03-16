<template>
  <div class="container" :class="{ auto: !autoplay }">
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
      @click="autoplay = !autoplay"
    >
      <q-carousel-slide
        v-for="(image, index) in preloadImage"
        :key="index"
        :name="index"
        :img-src="image.src"
      />
    </q-carousel>
    <div class="btn-area">
      <q-btn
        class="mp-btn-exit"
        text-color="grey-9"
        size="sm"
        round
        @click="close"
      >
        <q-icon round name="close" />
      </q-btn>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue';
import { useRouter } from 'vue-router';
import { useFileStore } from 'src/stores/FileStore';

let audioSrc;
const images: Array<string> = [];
const preloadImage = ref<Array<HTMLImageElement>>([]);
const fileStore = useFileStore();
fileStore.files.forEach((file) => {
  if (file.fileType === 'AUDIO') audioSrc = file.fileName;
  else images.push(`${process.env.API}/file/${file.fileName}`);
});

const preLoadImg = (images: Array<string>) => {
  images.forEach((image) => {
    const img = new Image();
    img.src = image;
    preloadImage.value.push(img);
  });
};

preLoadImg(images);

const $r = useRouter();
const audio = new Audio(`${process.env.API}/file/${audioSrc}`);
const slide = ref(0);
const autoplay = ref(true);

const onEnded = () => {
  audio.currentTime = 0;
  audio.play();
};

const close = () => {
  $r.push('/');
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
  fileStore.clear();
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

  > .carousel {
    height: 100%;
  }

  > .btn-area {
    position: absolute;
    top: 5px;
    right: 5px;
    gap: 10px;

    .q-btn {
      border: 2px solid transparent;
      background-color: transparent;

      &:hover {
        border-color: $dark;
        background-color: $grey-2;
      }
    }

    .q-icon {
      border: 1px solid transparent;
      background-color: transparent;
    }
  }
}

.auto {
  border-color: red;
}
</style>
