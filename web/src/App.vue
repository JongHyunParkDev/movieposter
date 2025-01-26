<template>
  <router-view />
</template>

<script setup lang="ts">
import { useQuasar } from 'quasar';
import { watch } from 'vue';
import { useErrorStore } from 'src/stores/ErrorStore';

const $q = useQuasar();

const errorStore = useErrorStore();
watch(errorStore.errors, async (newError) => {
  $q.notify({
    type: 'negative',
    message: newError[0],
  });
});

defineOptions({
  name: 'App',
});
</script>
