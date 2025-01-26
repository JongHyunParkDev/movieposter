<template>
  <q-layout view="hHh lpR fFf">
    <q-page-container class="page-container">
      <router-view v-slot="{ Component }">
        <transition
          appear
          enter-active-class="animated fadeIn"
          leave-active-class="animated fadeOut"
        >
          <component :is="Component" />
        </transition>
      </router-view>
    </q-page-container>
  </q-layout>
  <div v-if="!$q.fullscreen.isActive" class="fullscreen-area">
    <q-btn @click="toggleFullScreen"> 전체화면 </q-btn>
  </div>
</template>

<script setup lang="ts">
import { useQuasar } from 'quasar';

const $q = useQuasar();

function toggleFullScreen() {
  $q.fullscreen.toggle();
  console.log($q.fullscreen);
}
</script>
<style lang="scss">
.fullscreen-area {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  justify-content: top;
  align-items: start;
}
</style>
