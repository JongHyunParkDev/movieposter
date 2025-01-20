<template>
  <div
    class="container"
    ref="container"
    v-touch-swipe.mouse="handleSwipe"
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
          :style="{ backgroundColor: poster.color }"
          @click.stop="handlePosterClick(poster, index)"
        >
          {{ poster.label }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, type CSSProperties } from 'vue';
import { useQuasar } from 'quasar'

// 포스터 인터페이스
interface Poster {
  label: string;
  color: string;
}

interface SwipeDetails {
  direction: string;
  duration: number;
  distance: { x: number; y: number };
}

const $q = useQuasar();


// 초기 포스터 데이터
const posters = ref<Poster[]>([
  { label: 'Poster 1', color: '#111' },
  { label: 'Poster 2', color: '#222' },
  { label: 'Poster 3', color: '#333' },
  { label: 'Poster 4', color: '#444' },
  { label: 'Poster 5', color: '#555' },
  { label: 'Poster 6', color: '#666' },
  { label: 'Poster 7', color: '#777' },
  { label: 'Poster 8', color: '#888' },
  { label: 'Poster 9', color: '#999' },
]);

// DOM 요소 참조
const container = ref<HTMLElement | null>(null);
const posterWrapper = ref<HTMLElement[]>([]);
const currentAngle = ref(0);
const isSelected = ref(false);

const velocity = ref(0);
const radiusPersent = 0.15;
const speed = $q.platform.is.desktop ? 2: 20;

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

function handlePosterClick(poster: Poster, index: number) {
  console.log(posterWrapper.value);
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

// 초기화 및 정리
onMounted(() => {
  animate();
});

onBeforeUnmount(() => {
  // 필요 시 추가 정리 작업 수행
});
</script>

<style lang="scss">
.container {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  height: 100%;
  width: 100%;
  overflow: hidden;
  transition: background 1s linear;

  > .poster-list {
    position: relative;
    width: 100%;
    height: 100%;

    > .poster-wrapper {
      position: absolute;
      width: 12%;
      height: 36%;
      display: flex;
      align-items: center;
      justify-content: center;

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
        transition: transform 0.3s ease, box-shadow 0.3s ease;
      }

      > .poster:hover {
        transform: scale(1.2);
        z-index: 10;
        box-shadow: 0 8px 15px rgba(0, 0, 0, 0.2);
      }

      > .hidden {
        display: none;
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
}
</style>
