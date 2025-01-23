<template>
  <div
    class="container"
    ref="container"
    v-touch-swipe.mouse="handleSwipe"
    v-if="! isLoading"
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
        >
        </div>
      </div>
    </div>
    <div
      v-show="isSelected"
      class="mp-btn-area"
    >
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
        v-if="! preloadLoading"
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
  <div
      v-else
      class="spinner"
    >
    <q-spinner
      color="primary"
      size="5em"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, type CSSProperties } from 'vue';
import { useQuasar } from 'quasar';
import { useRouter } from 'vue-router';

// 포스터 인터페이스
interface Poster {
  label: string;
  color: string;
  url: string;
}

interface SwipeDetails {
  direction: string;
  duration: number;
  distance: { x: number; y: number };
}

const $r = useRouter();
const $q = useQuasar();


const preloadPercentage = ref(0);
const preloadLoading = ref(false);
const selectedPoster = ref<Poster | undefined>();
const posters = ref<Poster[]>([
  { label: 'JESSICAJONES', color: '#21241a', url: 'https://i.namu.wiki/i/8NhWZsd_I1PUlh1ZHhTYJpuIaX_TCTXUAGYb-Ow25s9iEIhAvV2E-e0Mbkkp6dKFGA63ZmY-pnrvnBJsIjERTQ.webp'},
  { label: 'ENCANTO', color: '#1d3916', url: 'https://i.namu.wiki/i/TsXyWEKfKDbTN3PzgGVMIFy3njcpnA7sbUMJs0PQMJ7RxyyIrXLTFMojcuHnME-9JVHtMxIHjUJJp_G1TS8rvQ.webp'},
  { label: 'ALADDIN', color: '#030333', url: 'https://i.namu.wiki/i/CAoD-76vT_Hi0medFcJyj7VASYDAcMSAwjZDYU8dEb1kSj-aTjvkUlGaww-Wi1kpRqELbMRQrDnpbE1-blPocg.webp'},
  { label: 'HOTEL', color: '#a48f9f', url: 'https://i.namu.wiki/i/KZ2cIBhRpZEiUg7GSijRIP8O-_ZykNK_W9v5pjWF3EWAYcR6Qr735VOhjZTqhPRLda-iM9CsYEoz52eDyo5x2A.webp'},
  { label: 'UP', color: '#62a1d3', url: 'https://i.namu.wiki/i/4DkMdeXBl7OG92APaEzGL9eAsS4e42gTL0Vpqv5fqRitojQEzYiacpTW_UDl0Hhv-GPlkhEzqGqv-nJjaGvD4w.webp'},
  { label: 'YOU', color: '#798358', url: 'https://i.namu.wiki/i/DXOfXzK0uE6LCaRVlLIAhPnAOR8uFhRzekrpGZXpQQC9BEoQOr6uzR-iZd7tARosV4oHCsqLeLe7vea8pcPiCg.webp' },
  { label: 'HER', color: '#b8123c', url: 'https://i.namu.wiki/i/RFGP8J7Y2Ihr0fZI2AoLlHxM1AiN1SlrD29gWxAqyPxPvZfV5-IgUiWr7-QeKGKGCJF2NDZjBe1aZvHWl5nBXw.webp' },
]);

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
  return Promise.all(posters.map(async (poster) => {
    const img = new Image();
    await new Promise<void>((resolve, reject) => {
      img.onload = () => resolve();
      img.onerror = () => reject(new Error(`Failed to load: ${poster.label}`));
      img.src = poster.url;
    });
    return img;
  }));
}

// 포스터 위치 계산
const getPosterPosition = (index: number): CSSProperties  => {
  if (! container.value) return {};
  const total = posters.value.length;
  const theta = (index / total) * 2 * Math.PI + currentAngle.value;
  const centerX = container.value.clientWidth / 2;
  const centerY = container.value.clientHeight;
  const radius = container.value.clientWidth * radiusPersent;
  const offsetY = container.value.clientHeight * 0.3 / 2;
  const offsetX = container.value.clientWidth * 0.1 / 2;
  const x = centerX + radius * Math.cos(theta) - offsetX;
  const y = centerY + radius * Math.sin(theta) - offsetY;

  return {
    left: `${x}px`,
    top: `${y}px`,
    transform: `rotate(${theta + Math.PI / 2}rad)`,
    position: 'absolute',
  }
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
    posterWrapper.value.forEach(pw => {
      pw.classList.add('hidden');
    });
    posterWrapper.value[index].classList.add('focused');
    posterWrapper.value[index].classList.remove('hidden');
  }
}

function handleSwipe(details: SwipeDetails) {
  let deltaX;
  switch (details.direction) {
    case 'right':
      deltaX = details.distance.x;
      break;
    case 'left':
      deltaX = - details.distance.x;
      break;
    default:
      return;
  }
  velocity.value = deltaX * 0.0005 * speed;
}

function detail() {
  preloadLoading.value = true;

  // TODO pinia 로 미리 preload 할 수 있도록 API CALL
  // 지금은 temp
  const interval = setInterval(() => {
    preloadPercentage.value += Math.floor(Math.random() * 8 + 10);

    if (preloadPercentage.value >= 100) {
      clearInterval(interval)
      $r.push('/detail');
    }
  }, 700)
}

function deSelectPoster() {
  selectedPoster.value = undefined;

  if (container.value) {
    isSelected.value = false;
    container.value.style.backgroundColor = '#ddd';
    posterWrapper.value.forEach(pw => {
      pw.classList.remove('hidden');
      pw.classList.remove('focused');
    });
  }
  animate();
}

// 초기화 및 정리
onMounted(async () => {
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

  > .mp-btn-area {
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
      border: 2px solid black;
    }

    .q-icon {
      border: 1px solid black;
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
