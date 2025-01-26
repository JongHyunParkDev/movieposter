<template>
  <div
    class="container"
    ref="container"
    v-touch-swipe.mouse="handleSwipe"
    v-if="!isLoading"
  >
    <div class="poster-list">
      <div
        v-for="(poster, index) in posters"
        :key="index"
        ref="posterWrapper"
        class="poster-wrapper"
        :style="getPosterPosition(index)"
      >
        <div
          class="poster"
          :style="{ backgroundImage: `url(${loadedImages[index].src})` }"
          @click.stop="selectPoster(poster, index)"
        ></div>
      </div>
    </div>
    <div v-show="isSelected" class="btn-area">
      <q-btn
        :loading="preloadLoading"
        :percentage="preloadPercentage"
        dark-percentage
        color="grey-3"
        text-color="grey-9"
        round
        @click="detail"
      >
        <q-icon round name="arrow_forward" />
        <template v-slot:loading>
          <q-icon name="hourglass_empty" />
        </template>
      </q-btn>
      <q-btn
        v-if="!preloadLoading"
        class="mp-btn-exit"
        color="grey-3"
        text-color="grey-9"
        round
        @click="deSelectPoster"
      >
        <q-icon round name="close" />
      </q-btn>
    </div>
  </div>
  <div v-else class="spinner">
    <q-spinner color="primary" size="5em" />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, type CSSProperties } from 'vue';
import { useQuasar } from 'quasar';
import { useRouter } from 'vue-router';
import { Api } from 'src/lib/Api';
import { Poster } from 'src/type/PosterTypes';
import { useFileStore } from 'src/stores/FileStore';
import { PosterDetailFile } from 'src/type/PosterTypes';

interface SwipeDetails {
  direction: string;
  duration: number;
  distance: { x: number; y: number };
}

const $r = useRouter();
const $q = useQuasar();
const fileStore = useFileStore();

const preloadPercentage = ref(0);
const preloadLoading = ref(false);
const selectedPoster = ref<Poster>();
const posters = ref<Poster[]>([]);

// DOM 요소 참조
const container = ref<HTMLElement | null>(null);
const posterWrapper = ref<HTMLElement[]>([]);
const currentAngle = ref(0);
const isSelected = ref(false);

const velocity = ref(0);
const radiusPersent = 0.2;
const speed = $q.platform.is.desktop ? 2 : 20;

const loadedImages = ref<HTMLImageElement[]>([]);
const isLoading = ref(true);

async function preloadImages(posters: Poster[]) {
  return Promise.all(
    posters.map(async (poster) => {
      const img = new Image();
      await new Promise<void>((resolve, reject) => {
        img.onload = () => resolve();
        img.onerror = () => reject(new Error(`Failed to load: ${poster.name}`));
        img.src = `${process.env.API}/file/${poster.fileName}`;
      });
      return img;
    })
  );
}

// 포스터 위치 계산
const getPosterPosition = (index: number): CSSProperties => {
  if (!container.value) return {};
  const total = posters.value.length;
  const theta = (index / total) * 2 * Math.PI + currentAngle.value;
  const centerX = container.value.clientWidth / 2;
  const centerY = container.value.clientHeight;
  const radius = container.value.clientWidth * radiusPersent;
  const offsetY = (container.value.clientHeight * 0.3) / 2;
  const offsetX = (container.value.clientWidth * 0.1) / 2;
  const x = centerX + radius * Math.cos(theta) - offsetX;
  const y = centerY + radius * Math.sin(theta) - offsetY;

  return {
    left: `${x}px`,
    top: `${y}px`,
    transform: `rotate(${theta + Math.PI / 2}rad)`,
    position: 'absolute',
  };
};

// 애니메이션 관리
function animate() {
  if (isSelected.value) return;
  currentAngle.value += velocity.value;
  velocity.value *= 0.95; // 감속 효과
  if (Math.abs(velocity.value) < 0.001) velocity.value = 0;
  requestAnimationFrame(animate);
}

function selectPoster(poster: Poster, index: number) {
  selectedPoster.value = poster;
  if (container.value) {
    isSelected.value = true;
    container.value.style.backgroundColor = poster.color;
    posterWrapper.value.forEach((pw) => {
      pw.classList.add('hidden');
    });
    posterWrapper.value[index].classList.add('focused');
    posterWrapper.value[index].classList.remove('hidden');
  }
}

function handleSwipe(details: SwipeDetails) {
  if (isSelected.value) return;
  let deltaX;
  switch (details.direction) {
    case 'right':
      deltaX = details.distance.x;
      break;
    case 'left':
      deltaX = -details.distance.x;
      break;
    default:
      return;
  }
  velocity.value = deltaX * 0.0005 * speed;
}

async function detail() {
  preloadLoading.value = true;
  const loadFiles: Array<PosterDetailFile> = await Api.get(
    `/poster/${selectedPoster.value?.id}`
  );
  loadFiles.forEach((file) => fileStore.add(file));
  $r.push('/detail');
}

function deSelectPoster() {
  selectedPoster.value = undefined;

  if (container.value) {
    isSelected.value = false;
    container.value.style.backgroundColor = '#ddd';
    posterWrapper.value.forEach((pw) => {
      pw.classList.remove('hidden');
      pw.classList.remove('focused');
    });
  }
  animate();
}

// 초기화 및 정리
onMounted(async () => {
  const loadPosters = await Api.get('/posters');
  posters.value = loadPosters;

  isLoading.value = true;
  loadedImages.value = await preloadImages(posters.value);
  isLoading.value = false;
  animate();
});

onBeforeUnmount(() => {
  // 필요 시 추가 정리 작업 수행
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
  background-color: #ddd;
  transition: background 1s linear;

  > .poster-list {
    position: relative;
    width: 100%;
    height: 100%;

    > .poster-wrapper {
      position: absolute;
      width: 15%;
      height: 45%;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 10px;

      > .poster {
        width: 100%;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 20px;
        font-weight: bold;
        color: white;
        border-radius: 10px;
        border: 1px solid white;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        background-size: cover;
        transition: transform 0.3s ease, box-shadow 0.3s ease;

        &:hover {
          transform: scale(1.2);
        }
      }

      > .hidden {
        display: none;
      }

      &:hover {
        z-index: 100;
      }
    }

    > .focused {
      position: fixed !important;
      top: 50% !important;
      left: 50% !important;
      transform: translate(-50%, -50%) scale(1.5) !important;
      z-index: 100;
      box-shadow: 0 10px 20px rgba(0, 0, 0, 0.5);

      > .poster:hover {
        transform: scale(1);
      }
    }
  }

  > .btn-area {
    position: absolute;
    top: 0;
    bottom: 0;
    right: 5px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 10px;

    .q-btn {
      border: 2px solid $dark;
      background-color: $dark;
    }

    .q-icon {
      border: 1px solid $dark;
      padding: 2px;
      border-radius: 50%;
    }
  }
}

.spinner {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  height: 100%;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
