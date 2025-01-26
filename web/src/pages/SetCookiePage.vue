<template>
  <div>
    {{ $r.params.id }}
  </div>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router';

const $r = useRoute();

document.cookie = `userId=${$r.params.id}`;
</script>
